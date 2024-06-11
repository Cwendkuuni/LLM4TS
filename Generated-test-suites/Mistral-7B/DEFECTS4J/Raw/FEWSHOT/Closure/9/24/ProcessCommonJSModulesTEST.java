package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Test class for ProcessCommonJSModules.
 */
public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    // Given
    String filename = "test.js";
    String moduleName = "myModule";
    CompilerInput input = mock(CompilerInput.class);
    CompilerPass processor = new ProcessCommonJSModules(null, filename);
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    when(passManager.getNextPass()).thenReturn(processor);

    Node root = NodeFactory.createScript(1, null, null);
    root.setSourceFileName(filename);

    // When
    processor.process(input, root);

    // Then
    Node module = root.getChildAtIndex(0);
    assertEquals(moduleName, module.getString());
    assertNull(module.getParent());

    Scope scope = mock(Scope.class);
    NodeFactory factory = mock(NodeFactory.class);
    when(input.getFactory()).thenReturn(factory);
    when(input.getScope()).thenReturn(scope);

    NodeTraversal traversal = NodeTraversal.forTree(root, null, passManager);
    Node scriptNode = traversal.getRoot();

    // Check if script has been modified
    assertEquals(1, scriptNode.getChildCount());
    Node moduleNode = scriptNode.getChildAtIndex(0);
    assertEquals(moduleName + "$$myModule", moduleNode.getString());

    // Check if variables have been renamed
    Node variableNode = NodeFactory.createName("myVariable");
    scriptNode.addChild(variableNode);
    traversal.traverse(scriptNode);
    assertEquals("myVariable$$myModule", variableNode.getString());

    // Check if exports have been renamed
    Node exportsNode = NodeFactory.createObjectLiteral();
    Node exportsProp = NodeFactory.createGetProp(moduleNode, "exports");
    exportsProp.setParent(moduleNode);
    exportsNode.addChild(exportsProp);
    scriptNode.addChild(exportsNode);
    traversal.traverse(scriptNode);
    assertEquals("exports$$myModule", exportsProp.getChildAtIndex(0).getString());

    // Check if require calls have been rewritten
    Node requireCall = NodeFactory.createCall(
        NodeFactory.createGetprop(NodeFactory.createName("require"), null),
        NodeFactory.createString("otherModule"));
    scriptNode.addChild(requireCall);
    traversal.traverse(scriptNode);
    assertEquals(
        3,
        scriptNode.getChildCount(),
        "Expecting goog.provide, module assignment and require call"
    );
    Node googProvideCall = scriptNode.getChildAtIndex(0);
    Node requireStatement = scriptNode.getChildAtIndex(1);
    Node newRequireCall = scriptNode.getChildAtIndex(2);

    // goog.provide call should have been emitted
    assertEquals("myModule", googProvideCall.getString());

    // module assignment should have been emitted
    assertEquals(moduleName, requireStatement.getChildAtIndex(0).getString());

    // require call should have been rewritten to reference otherModule directly
    assertEquals("otherModule", newRequireCall.getChildAtIndex(1).getString());

    // Check if dependencies have been reported
    verify(input, times(1)).addRequire("otherModule");
  }
}