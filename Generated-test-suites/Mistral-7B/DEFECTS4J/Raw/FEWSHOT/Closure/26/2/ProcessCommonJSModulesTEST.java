package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassManager.PassPhase;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CheckTypes;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeVisitResult;
import com.google.javascript.jscomp.RewrittenSourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.NodeOperations;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private NodeOperations nodeOperations;
  private CompilerPassManager passManager;
  private CompilerOptions options;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    nodeOperations = mock(NodeOperations.class);
    options = new CompilerOptions();

    passManager = new CompilerPassManager(compiler, options);
  }

  @Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String inputFileName = "test.js";
    String moduleName = "testModule";
    String sourceFileName = "testModule.js";

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".");

    RewrittenSourceFile rewrittenSourceFile = new RewrittenSourceFile(
        new File(inputFileName), sourceFileName);

    // Set up node factory to create test nodes

    Node rootNode = nodeFactory.literalNull();
    Node requireCallNode = nodeFactory.newCall("require", nodeFactory.string("testModule"));
    Node scriptNode = nodeFactory.newScript("var test = 1;");
    Node moduleExportsNode = nodeFactory.newGetProp("module.exports", nodeFactory.objectLiteral());

    rootNode.addChild(requireCallNode);
    rootNode.addChild(scriptNode);
    rootNode.addChild(moduleExportsNode);

    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getFactory()).thenReturn(nodeFactory);
    when(compiler.getOperations()).thenReturn(nodeOperations);
    when(compiler.getPassManager()).thenReturn(passManager);

    // Traverse input node tree with ProcessCommonJSModules

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, rootNode);
    CompilerPass commonJSModulesPass = processor;
    NodeVisitResult result = nodeTraversal.traverse(commonJSModulesPass);

    // Check validate result

    assertNotNull(result);
    assertNull(result.getError());

    // Check moduleName is correct

    String rewrittenSource = rewrittenSourceFile.getSource();
    String expectedRewrittenSource =
        "goog.provide('testModule');" +
        "var test$$testModule = (function(){" +
        "  var exports = {};" +
        "  goog.exportProperty(exports, 'test', test);" +
        "  return exports;" +
        "})();";
    assertEquals(expectedRewrittenSource, rewrittenSource);

    // Check module exports is correct

    Node moduleExportsNodeInRewrittenSource = findNode(nodeFactory, rewrittenSource, "exports");
    assertEquals("exports", moduleExportsNodeInRewrittenSource.getString());

    // Check global variables are renamed

    Set<String> originalGlobalVariableNames = Sets.newHashSet("test");
    Set<String> renamedGlobalVariableNames = Sets.newHashSet();
    NodeTraversal globalVariableVisitor = new NodeTraversal(compiler, null, rootNode);
    Node currentNode = null;

    while ((currentNode = globalVariableVisitor.next()) != null) {
      if (currentNode.isName()) {
        String name = currentNode.getString();
        if (originalGlobalVariableNames.contains(name)) {
          assertTrue(name.endsWith("$$testModule"));
          renamedGlobalVariableNames.add(name.substring(0, name.length() - 9));
        }
      }
    }

    assertEquals(originalGlobalVariableNames, renamedGlobalVariableNames);

    // Check require statements are replaced

    List<String> requires = passManager.getInputs().getRequires();

    assertEquals(1, requires.size());
    assertEquals("testModule", requires.get(0));

    // Check goog.provide statement is emitted

    String googProvideStatement = findNode(nodeFactory, rewrittenSource, "goog.provide")
        .getParent()
        .getString();
    assertEquals("goog.provide('testModule');", googProvideStatement);
  }

  private Node findNode(NodeFactory nodeFactory, String source, String nodeName) {
    Node rootNode = nodeFactory.parse(source);
    NodeTraversal nodeTraversal = new NodeTraversal(null, null, rootNode);

    Node node = null;
    while ((node = nodeTraversal.next()) != null) {
      if (nodeName.equals(node.getString())) {
        return node;
      }
    }

    return null;
  }
}