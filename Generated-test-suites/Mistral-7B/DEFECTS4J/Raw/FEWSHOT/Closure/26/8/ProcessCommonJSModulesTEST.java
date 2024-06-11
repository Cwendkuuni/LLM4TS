package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.net.URI;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Test class for {@link ProcessCommonJSModules}.
 */
public class ProcessCommonJSModulesTest {

  private static final String TEST_MODULE_NAME = "testModule";
  private static final String TEST_SOURCE_NAME = "test.js";

  @Test
  public void testGuessCJSModuleName() {
    assertEquals(TEST_MODULE_NAME, ProcessCommonJSModules.toModuleName(TEST_SOURCE_NAME));
  }

  @Test
  public void testGuessCJSModuleNameWithRelativePath() {
    String currentFileName = "folder/test.js";
    String requiredFileName = "relative/test.js";
    assertEquals("test$relative", ProcessCommonJSModules.toModuleName(requiredFileName, currentFileName));
  }

  @Test
  public void testNormalizeSourceName() {
    assertEquals(TEST_SOURCE_NAME, ProcessCommonJSModules.normalizeSourceName(TEST_SOURCE_NAME));
  }

  @Test
  public void testProcessCommonJsModulesCallback_visitRequireCall() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CompilerInput inputMock = mock(CompilerInput.class);
    NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
    Node requireNode = IR.call(IR.getprop(IR.name("require"), IR.string("module")),
        IR.string("test")).copyInformationFromForTree(new Node(IR.CALL, null));
    Node scriptNodeMock = mock(Node.class);
    Node moduleRef = new Node(IR.NAME, "test");

    when(compilerMock.getSourceName()).thenReturn(new SourceName("test.js"));

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    callback.visit(nodeTraversalMock, requireNode, null);

    assertEquals(moduleRef, requireNode.getFirstChild());

    // Check that the input has been updated
    when(inputMock.addRequire).thenAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      assertEquals("test", args[0]);
      return null;
    });

    Node rootNodeMock = new Node(IR.PROGRAM, null);
    callback.process(nodeTraversalMock, rootNodeMock);

    assertEquals(1, inputMock.getRequireList().size());
  }

  @Test
  public void testProcessCommonJsModulesCallback_visitScript() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CompilerInput inputMock = mock(CompilerInput.class);
    NodeTraversal nodeTraversalMock = mock(NodeTraversal.class);
    Node scriptNode = mock(Node.class);
    Node moduleNameNode = new Node(IR.NAME, TEST_MODULE_NAME);
    Node moduleProvideNode = new Node(IR.CALL, null);
    Node moduleExportsNode = new Node(IR.NAME, "module$exports");
    Node exportsNode = new Node(IR.OBJECT_LITERAL, null);

    when(compilerMock.getSourceName()).thenReturn(new SourceName("test.js"));

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    // Call visitScript
    callback.visit(nodeTraversalMock, scriptNode, null);

    // Check that the script node has been modified
    Node expectedScriptNode = new Node(IR.PROGRAM, null);
    expectedScriptNode.addChildToFront(moduleNameNode);
    expectedScriptNode.addChildToFront(moduleProvideNode);
    expectedScriptNode.addChildToFront(IR.var(moduleNameNode, exportsNode));
    expectedScriptNode.addChildToBack(scriptNode);

    assertEquals(expectedScriptNode, scriptNode);

    // Check that the input has been updated
    when(inputMock.addProvide).thenAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      assertEquals(TEST_MODULE_NAME, args[0]);
      return new JSModule(TEST_MODULE_NAME);
    });

    when(inputMock.addRequiresFromFile).thenAnswer(invocation -> Sets.newHashSet());

    Node rootNodeMock = new Node(IR.PROGRAM, null);
    callback.process(nodeTraversalMock, rootNodeMock);

    JSModule jsModule = (JSModule) inputMock.getProvides().get(TEST_MODULE_NAME);
    assertEquals(1, jsModule.getRequireList().size());
    assertEquals("test.js", jsModule.getRequireList().get(0));
  }
}