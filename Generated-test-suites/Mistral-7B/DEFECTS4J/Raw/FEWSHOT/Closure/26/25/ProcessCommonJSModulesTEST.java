package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String testSrc = "test.js";
    String testModuleName = "test$";

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput(testSrc);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".");

    Node rootNode = NodeFactory.createProgram();
    Node scriptNode = NodeFactory.createScript("// test script", null);
    scriptNode.setSourceFileName(testSrc);
    rootNode.addChildToBack(scriptNode);

    when(compiler.getInput()).thenReturn(input);

    processor.process(input.getExterns(), rootNode);

    Node module = processor.getModule();
    assertEquals(testModuleName, module.getName());

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, new AbstractPostOrderCallback() {

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isName()) {
          String name = n.getString();
          if (name.equals("test")) {
            assertEquals(testModuleName + "$test", n.getString());
          }
        }
      }
    });
    traversal.traverse(rootNode);
  }

  @Test
  public void testGuessCJSModuleName() {
    String testSrc = "test/test.js";
    String testModuleName = "test$";
    String expected = "test$";
    assertEquals(expected, ProcessCommonJSModules.toModuleName(testSrc));

    String relativeTestSrc = "../test/test.js";
    String expectedRelative = "test$";
    assertEquals(expectedRelative, ProcessCommonJSModules.toModuleName(relativeTestSrc, "src/main.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    String testSrc = "test/test.js";
    String expected = "test";
    assertEquals(expected, ProcessCommonJSModules.normalizeSourceName(testSrc));

    String testSrcWithPrefix = "test.js";
    assertEquals(expected, ProcessCommonJSModules.normalizeSourceName(testSrcWithPrefix));

    String testSrcWithRelativePath = "./test/test.js";
    assertEquals(expected, ProcessCommonJSModules.normalizeSourceName(testSrcWithRelativePath));
  }

  @Test
  public void testProcessCommonJsModulesCallback() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js");

    Node rootNode = NodeFactory.createProgram();
    Node scriptNode = NodeFactory.createScript("// test script", null);
    scriptNode.setSourceFileName("test.js");
    rootNode.addChildToBack(scriptNode);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, callback);
    traversal.traverse(rootNode);

    assertEquals(1, callback.scriptNodeCount);

    Node requireCall = NodeFactory.createCall("require", NodeFactory.createString("utils"));
    Node requireParent = NodeFactory.createPostfixExpression(requireCall, "-");
    Node requireScript = NodeFactory.createScript("// require call", null);
    requireScript.addChildToBack(requireCall);
    Node requireParentScript = NodeFactory.createScript("// require parent script", null);
    requireParentScript.addChildToBack(requireParent);
    requireParentScript.addChildToBack(requireScript);
    rootNode.addChildToBack(requireParentScript);

    traversal = new NodeTraversal(compiler, rootNode, callback);
    traversal.traverse(rootNode);

    assertEquals(2, callback.scriptNodeCount);

    Node exports = NodeFactory.createObjectLiteral();
    Node exportsProp = NodeFactory.createGetProperty(NodeFactory.createName("exports"), exports);
    scriptNode.addChildToBack(exportsProp);

    traversal = new NodeTraversal(compiler, rootNode, callback);
    traversal.traverse(rootNode);

    assertEquals(3, callback.scriptNodeCount);

    JSModule expectedModule = new JSModule(testModuleName);
    when(compiler.getInput()).thenReturn(input);
    when(input.addProvide(expectedModule.getName())).thenReturn(expectedModule);

    processor.process(input.getExterns(), rootNode);

    assertEquals(expectedModule, processor.getModule());
  }

  @Test
  public void testVisitRequireCall() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);

    Node rootNode = NodeFactory.createProgram();
    Node scriptNode = NodeFactory.createScript("// test script", null);
    scriptNode.setSourceFileName("test.js");
    rootNode.addChildToBack(scriptNode);

    Node requireCall = NodeFactory.createCall("require", NodeFactory.createString("utils"));
    Node requireParent = NodeFactory.createPostfixExpression(requireCall, "-");
    scriptNode.addChildToBack(requireParent);
    scriptNode.addChildToBack(requireCall);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, processor.new ProcessCommonJsModulesCallback());
    traversal.traverse(rootNode);

    assertEquals(1, rootNode.getChildCount());

    Node googRequireCall = rootNode.getChildAtIndex(0);
    assertEquals(1, googRequireCall.getChildCount());
    assertEquals("utils", googRequireCall.getChildAtIndex(0).getString());

    assertEquals(1, scriptNode.getChildCount());
    assertEquals("utils", scriptNode.getChildAtIndex(0).getString());
  }

  @Test
  public void testVisitModuleExports() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput input = new CompilerInput("test.js");
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);

    Node rootNode = NodeFactory.createProgram();
    Node scriptNode = NodeFactory.createScript("// test script", null);
    scriptNode.setSourceFileName("test.js");
    rootNode.addChildToBack(scriptNode);

    Node moduleExports = NodeFactory.createObjectLiteral();
    Node exportsProp = NodeFactory.createGetProperty(NodeFactory.createName("exports"), moduleExports);
    Node exportsAssignment = NodeFactory.createAssign(exportsProp, NodeFactory.createObjectLiteral());
    Node exportsValue = NodeFactory.createString("test");
    Node exportsValueAssignment = NodeFactory.createAssign(exportsValue, NodeFactory.createName("test"));
    moduleExports.addChildToBack(exportsValueAssignment);
    scriptNode.addChildToBack(exportsAssignment);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, processor.new ProcessCommonJsModulesCallback());
    traversal.traverse(rootNode);

    assertEquals(1, rootNode.getChildCount());

    Node googProvideCall = rootNode.getChildAtIndex(0);
    assertEquals(1, googProvideCall.getChildCount());
    assertEquals("test$", googProvideCall.getChildAtIndex(0).getString());

    assertEquals(1, scriptNode.getChildCount());
    Node module = scriptNode.getChildAtIndex(0);
    assertEquals("test$", module.getName());
    assertEquals(1, module.getChildCount());
    assertEquals("test$test", module.getChildAtIndex(0).getString());
  }
}