package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.test.testcase.AbstractCompilerTestCase;
import com.google.javascript.rhino.NodeFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Test class for ProcessCommonJSModules.
 */
public class ProcessCommonJSModulesTest extends AbstractCompilerTestCase {

  private static final String FILE_NAME = "testFile.js";
  private static final String REQUIRED_MODULE_NAME = "requiredModule";

  @Override
  protected CompilerPass getProcessorUnderTest() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerInput ci = new CompilerInput(getTestFile(FILE_NAME),
        new CompilerOptions());
    return new ProcessCommonJSModules(compiler, ".", false);
  }

  @Override
  protected Node getSourceNode() {
    Node sourceNode = getTestNodeFactory().createScriptletNode(null, null);
    sourceNode.setSourceFileName(FILE_NAME);

    Node requireCall = getTestNodeFactory().createCallNode("require",
        Collections.singletonList(getTestNodeFactory().createStringNode(
            REQUIRED_MODULE_NAME)));
    sourceNode.addChild(requireCall);

    Node moduleExportsAssignment = getTestNodeFactory().createAssign(
        getTestNodeFactory().createGetprop(getTestNodeFactory().createName("module"),
            "exports"), getTestNodeFactory().createName("myModuleExports"));
    sourceNode.addChild(moduleExportsAssignment);

    Node exportsObject = getTestNodeFactory().createObjectLiteralNode();
    sourceNode.addChild(exportsObject);
    moduleExportsAssignment.setRightChild(exportsObject);

    Node script = getTestNodeFactory().createScriptNode(sourceNode);
    return script;
  }

  @org.junit.Test
  public void testProcessCommonJSModule() {
    ProcessCommonJSModules processor = getProcessorUnderTest();
    CompilerPass processorToRunAfter = mock(CompilerPass.class);

    NodeFactory nodeFactory = getTestNodeFactory();
    NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null,
        processor, processorToRunAfter);

    Node root = getSourceNode();
    processor.process(getTestInput(), root);

    Node requireCall = root.getFirstChild();
    assertEquals(REQUIRED_MODULE_NAME, requireCall.getChildAtIndex(1)
        .getString());

    Node moduleExports = requireCall.getParent();
    assertNull(moduleExports.getChildAtIndex(1));

    String moduleName = ProcessCommonJSModules.toModuleName(
        ProcessCommonJSModules.toModuleName(FILE_NAME), REQUIRED_MODULE_NAME);
    assertEquals(moduleName, getTestInput().getRequire(REQUIRED_MODULE_NAME)
        .getFullName());
  }

  @org.junit.Test
  public void testProcessCommonJSModuleWithModuleExports() {
    ProcessCommonJSModules processor = getProcessorUnderTest();
    CompilerPass processorToRunAfter = mock(CompilerPass.class);

    NodeFactory nodeFactory = getTestNodeFactory();
    NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null,
        processor, processorToRunAfter);

    Node root = getSourceNode();
    processor.process(getTestInput(), root);

    Node requireCall = root.getFirstChild();
    assertEquals(REQUIRED_MODULE_NAME, requireCall.getChildAtIndex(1)
        .getString());

    Node moduleExports = requireCall.getParent();
    Node exportsObject = moduleExports.getChildAtIndex(1);
    assertEquals(1, exportsObject.getChildCount());

    Node moduleExportsVariable = exportsObject.getChildAtIndex(0);
    assertEquals("myModuleExports", moduleExportsVariable.getString());
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testURISyntaxExceptionWhenNormalizingRelativeSourceName() {
    ProcessCommonJSModules processor = getProcessorUnderTest();
    CompilerPass processorToRunAfter = mock(CompilerPass.class);

    NodeFactory nodeFactory = getTestNodeFactory();
    NodeTraversal traversal = new NodeTraversal(nodeFactory, null, null,
        processor, processorToRunAfter);

    String currentFileName = "currentFile.js";
    String requiredFileName = "./relative/file.js";

    Node root = getSourceNode();
    NodeTraversal.traverse(processor, root, traverser -> {
      if (traverser.getNode().isCall() &&
          "require".equals(traverser.getNode().getFirstChild().getQualifiedName())) {
        String requiredModuleName = traverser.getNode().getChildAtIndex(1)
            .getString();
        assertEquals(requiredFileName, requiredModuleName);
      }
    });

    processor.process(getTestInput(currentFileName), root);
  }
}