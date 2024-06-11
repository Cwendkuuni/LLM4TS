package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler();
    options = new CompilerOptions();
    nodeFactory = compiler.getFactory();
  }

  @org.junit.Test
  public void testProcess_ExternsIsNull() {
    // Arrange
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node root = nodeFactory.literal(null);

    // Act
    prepareAst.process(null, root);

    // Assert
    NodeUtil.assertNoControlStructures(root);
  }

  @org.junit.Test
  public void testProcess_ExternsIsNotNull() {
    // Arrange
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node externs = nodeFactory.objectLit();
    Node root = nodeFactory.literal(null);

    // Act
    prepareAst.process(externs, root);

    // Assert
    NodeUtil.assertNoControlStructures(root);
  }

  @org.junit.Test
  public void testProcess_CheckOnly() {
    // Arrange
    PrepareAst prepareAst = new PrepareAst(compiler, true);
    Node externs = nodeFactory.objectLit();
    Node root = nodeFactory.literal(null);

    // Act
    prepareAst.process(externs, root);

    // Assert
    assertThrows(RuntimeException.class, () -> prepareAst.reportChange());
    NodeUtil.assertNoControlStructures(root);
  }

  @org.junit.Test
  public void testProcess_ExternsAndRootIsNotNull() {
    // Arrange
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node externs = nodeFactory.objectLit();
    Node root = nodeFactory.literal(null);
    root.setTokenPosition(1, 1);

    // Act
    prepareAst.process(externs, root);

    // Assert
    NodeTraversal.traverse(compiler, externs, new NodeTraversal.Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return true;
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isBlock()) {
          // Ensure blocks were added to control structures
          Node firstChild = n.getFirstChild();
          assertThat(firstChild).isNotNull();
          NodeUtil.assertIsControlStructureCodeBlock(parent, firstChild);
        }
      }
    });

    NodeUtil.assertNoControlStructures(root);
  }

  @org.junit.Test
  public void testNormalizeNodeTypes() {
    // Arrange
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node root = nodeFactory.literal(null);
    Node nodeWithVoidType = nodeFactory.expressionStatement(
        nodeFactory.call(nodeFactory.name("voidFunction"), nodeFactory.literal(42)));
    root.addChildrenToFront(nodeWithVoidType);

    // Act
    prepareAst.process(null, root);

    // Assert
    Node expectedResult = nodeFactory.expressionStatement(
        nodeFactory.assignment(nodeFactory.name("result"), nodeFactory.call(
            nodeFactory.name("result"), nodeFactory.literal(42))));
    Node actualResult = root.getFirstChild();
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @org.junit.Test
  public void testNormalizeBlocks() {
    // Arrange
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node root = nodeFactory.literal(null);
    Node controlStructure = nodeFactory.ifStatement(
        nodeFactory.booleanLiteral(true), nodeFactory.literal(42), null);

    root.addChildrenToFront(controlStructure);

    // Act
    prepareAst.process(null, root);

    // Assert
    Node expectedBlock = nodeFactory.block();
    Node actualBlock = controlStructure.getFirstChild();
    assertThat(actualBlock).isEqualTo(expectedBlock);
  }

  @org.junit.Test
  public void testPrepareAnnotations() {
    // Arrange
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node externs = nodeFactory.objectLit();
    Node root = nodeFactory.literal(null);

    // Act
    prepareAst.process(externs, root);

    // Assert
    Node functionWithJsDoc = nodeFactory.function(
        nodeFactory.name("functionName"), null, null, nodeFactory.block(nodeFactory.literal(42)));
    functionWithJsDoc.setJSDocInfo(nodeFactory.jsDocInfo("Function docs"));
    externs.addChildrenToFront(functionWithJsDoc);

    Node functionWithoutJsDoc = nodeFactory.function(
        nodeFactory.name("functionName2"), null, null, nodeFactory.block(nodeFactory.literal(42)));
    root.addChildrenToFront(functionWithoutJsDoc);

    Node expectedFunctionWithJsDoc = nodeFactory.function(
        nodeFactory.name("functionName"), null, null, nodeFactory.block(nodeFactory.literal(42)));
    expectedFunctionWithJsDoc.setJSDocInfo(nodeFactory.jsDocInfo("Function docs"));

    Node actualFunctionWithJsDoc = NodeUtil.findSingleChildWithType(root, Token.FUNCTION);
    Node actualFunctionWithoutJsDoc = NodeUtil.findSingleChildWithType(root, Token.FUNCTION, 1);

    assertThat(actualFunctionWithJsDoc).isEqualTo(expectedFunctionWithJsDoc);
    assertThat(actualFunctionWithoutJsDoc).isNotNull();
    assertThat(actualFunctionWithoutJsDoc.getJSDocInfo()).isNull();
  }

  @org.junit.Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    // Arrange
    PrepareAst prepareAst = new PrepareAst(compiler, false);
    Node objlit = nodeFactory.objectLit();
    Node key = nodeFactory.name("keyName");
    Node value = nodeFactory.function(nodeFactory.name("valueName"), null, null, nodeFactory.block(nodeFactory.literal(42)));
    value.setJSDocInfo(nodeFactory.jsDocInfo("Value docs"));
    objlit.addChildToProperty(key, value);

    // Act
    prepareAst.process(null, objlit);

    // Assert
    Node expectedValue = nodeFactory.function(nodeFactory.name("valueName"), null, null, nodeFactory.block(nodeFactory.literal(42)));
    expectedValue.setJSDocInfo(nodeFactory.jsDocInfo("Value docs"));
    Node actualValue = NodeUtil.findSingleChildWithType(objlit, Token.FUNCTION, 0);

    assertThat(actualValue).isEqualTo(expectedValue);
  }
}