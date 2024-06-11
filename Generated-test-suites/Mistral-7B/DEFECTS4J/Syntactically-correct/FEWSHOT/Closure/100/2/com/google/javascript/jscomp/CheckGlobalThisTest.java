package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checker;
  private Parser parser;
  private NodeFactory nodeFactory;
  private SourceFile sourceFile;
  private Node rootNode;

  @Before
  public void setUp() {
    level = CheckLevel.WARNING;
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, level);
    parser = mock(Parser.class);
    nodeFactory = mock(NodeFactory.class);
    sourceFile = mock(SourceFile.class);
    when(sourceFile.getTokenList()).thenReturn(parser.parse(null, null));
    when(parser.parseCompilationUnit(null, null)).thenReturn(sourceFile);
    rootNode = parser.parseCompilationUnit("src/CheckGlobalThisTest.js", null).getTree();
  }

  @Test
  public void testNoGlobalThis() {
    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();
    assertThat(compiler.getDiagnostics()).isEmpty();
  }

  @Test
  public void testGlobalThisInFunction() {
    Node functionNode = nodeFactory.createFunction("foo", null, null, false, null, null);
    Node thisNode = nodeFactory.createThis();

    Node functionParent = nodeFactory.createStatement(functionNode);
    Node parentParent = nodeFactory.createStatement(functionParent);

    Node assignParent = nodeFactory.createStatement(nodeFactory.createAssign(functionNode, thisNode));
    Node parent = nodeFactory.createStatement(assignParent);
    Node root = nodeFactory.createStatement(parentParent);

    rootNode.replaceChildren(0, root);

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();

    ArgumentCaptor<DiagnosticPosition> positionArgumentCaptor = ArgumentCaptor.forClass(DiagnosticPosition.class);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);

    assertThat(compiler.getDiagnostics().size()).isEqualTo(1);
    compiler.getDiagnostics().get(0).accept(positionArgumentCaptor.capture(), nodeArgumentCaptor.capture());

    Node expectedNode = thisNode;
    Node actualNode = nodeArgumentCaptor.getValue();
    assertThat(actualNode).isSameInstanceAs(expectedNode);
    assertThat(positionArgumentCaptor.getValue().getSourceName()).isEqualTo("src/CheckGlobalThisTest.js");
    assertThat(positionArgumentCaptor.getValue().getLineNumber()).isEqualTo(2);
  }

  @Test
  public void testGlobalThisInPropertyAccess() {
    Node thisNode = nodeFactory.createThis();
    Node propertyNode = nodeFactory.createIdentifier("foo");
    Node propertyParent = nodeFactory.createGetProperty(thisNode, propertyNode);

    Node assignParent = nodeFactory.createStatement(nodeFactory.createAssign(propertyParent, nodeFactory.createLiteral(42)));
    Node parent = nodeFactory.createStatement(assignParent);
    Node root = nodeFactory.createStatement(parent);

    rootNode.replaceChildren(0, root);

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();

    ArgumentCaptor<DiagnosticPosition> positionArgumentCaptor = ArgumentCaptor.forClass(DiagnosticPosition.class);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);

    assertThat(compiler.getDiagnostics().size()).isEqualTo(1);
    compiler.getDiagnostics().get(0).accept(positionArgumentCaptor.capture(), nodeArgumentCaptor.capture());

    Node expectedNode = propertyParent;
    Node actualNode = nodeArgumentCaptor.getValue();
    assertThat(actualNode).isSameInstanceAs(expectedNode);
    assertThat(positionArgumentCaptor.getValue().getSourceName()).isEqualTo("src/CheckGlobalThisTest.js");
    assertThat(positionArgumentCaptor.getValue().getLineNumber()).isEqualTo(2);
  }

  @Test
  public void testGlobalThisInPrototypePropertyAccess() {
    Node objectNode = nodeFactory.createObjectLiteral();
    Node thisNode = nodeFactory.createThis();
    Node propertyNameNode = nodeFactory.createIdentifier("prototype");
    Node prototypeNode = nodeFactory.createGetProperty(thisNode, propertyNameNode);
    Node propertyParent = nodeFactory.createGetProperty(prototypeNode, nodeFactory.createIdentifier("foo"));

    Node assignParent = nodeFactory.createStatement(nodeFactory.createAssign(propertyParent, nodeFactory.createLiteral(42)));
    Node parent = nodeFactory.createStatement(assignParent);
    Node root = nodeFactory.createStatement(parent);

    rootNode.replaceChildren(0, root);

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();

    ArgumentCaptor<DiagnosticPosition> positionArgumentCaptor = ArgumentCaptor.forClass(DiagnosticPosition.class);
    ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);

    assertThat(compiler.getDiagnostics().size()).isEqualTo(1);
    compiler.getDiagnostics().get(0).accept(positionArgumentCaptor.capture(), nodeArgumentCaptor.capture());

    Node expectedNode = propertyParent;
    Node actualNode = nodeArgumentCaptor.getValue();
    assertThat(actualNode).isSameInstanceAs(expectedNode);
    assertThat(positionArgumentCaptor.getValue().getSourceName()).isEqualTo("src/CheckGlobalThisTest.js");
    assertThat(positionArgumentCaptor.getValue().getLineNumber()).isEqualTo(2);
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    Node functionNode = nodeFactory.createFunction("foo", null, null, false, null, nodeFactory.createThisAnnotation());

    Node root = nodeFactory.createStatement(nodeFactory.createStatement(functionNode));

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();

    assertThat(compiler.getDiagnostics()).isEmpty();
  }

  @Test
  public void testConstructor() {
    Node constructorNode = nodeFactory.createFunction("Constructor", nodeFactory.createThisKeyword(), null, true, null, null);

    Node root = nodeFactory.createStatement(nodeFactory.createStatement(constructorNode));

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();

    assertThat(compiler.getDiagnostics()).isEmpty();
  }

  @Test
  public void testFunctionWithPrototypeProperty() {
    Node functionNode = nodeFactory.createFunction("foo", null, null, false, null, null);
    Node prototypeNode = nodeFactory.createObjectLiteral();
    Node propertyNode = nodeFactory.createIdentifier("prototype");
    Node propertyParent = nodeFactory.createSetProperty(prototypeNode, propertyNode, nodeFactory.createFunctionExpression(functionNode, null, null));
    Node root = nodeFactory.createStatement(nodeFactory.createStatement(nodeFactory.createStatement(nodeFactory.createAssign(functionNode, prototypeNode))));

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();

    assertThat(compiler.getDiagnostics()).isEmpty();
  }

  @Test
  public void testFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = nodeFactory.createFunction("foo", null, null, false, null, null);
    Node root = nodeFactory.createStatement(nodeFactory.createStatement(functionNode));

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();

    assertThat(compiler.getDiagnostics()).isEmpty();
  }

  @Test
  public void testNullParent() {
    Node root = nodeFactory.createStatement(null);

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();

    assertNull(compiler.getDiagnostics());
  }

  @Test
  public void testNullNode() {
    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    traversal.traverse();

    assertNull(compiler.getDiagnostics());
  }
}