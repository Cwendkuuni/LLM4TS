import static org.junit.Assert.*;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;

  @InjectMocks
  private CheckGlobalThis checker;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node node = Node.newNode(Token.FUNCTION, null, null);

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node node = Node.newNode(Token.FUNCTION, null, null);
    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setConstructor(true);

    node.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node node = Node.newNode(Token.FUNCTION, null, null);
    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setThisType(true);

    node.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionOutsideOfScriptOrBlock() {
    Node node = Node.newNode(Token.FUNCTION, null, null);

    Node parent = Node.newNode(Token.ASSIGN, null, null);
    parent.addChildToEnd(node);
    Node grandparent = Node.newNode(Token.ASSIGN, null, null);
    grandparent.addChildToEnd(parent);

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, node, grandparent));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assign = Node.newNode(Token.ASSIGN, null, null);
    Node leftSide = Node.newNode(Token.THIS, null, null);

    assign.addChildToEnd(leftSide);

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, leftSide, assign));
  }

  @Test
  public void testShouldTraverseLeftSideOfGetProperty() {
    Node getProp = Node.newNode(Token.GETPROP, null, null);
    Node leftSide = Node.newNode(Token.THIS, null, null);
    getProp.addChildToEnd(leftSide);

    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, leftSide, getProp));
  }

  @Test
  public void testShouldNotTraverseLeftSideOfGetPropertyWithPrototype() {
    Node getProp = Node.newNode(Token.GETPROP, null, null);
    Node leftSide = Node.newNode(Token.THIS, null, null);
    getProp.addChildToEnd(Node.newNode(Token.STRING, "prototype", null));

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, leftSide, getProp));
  }

  @Test
  public void testShouldNotTraverseLeftSideOfGetQualifiedName() {
    Node getQualifiedName = Node.newNode(Token.GETQUALIFIEDNAME, null, null);
    Node leftSide = Node.newNode(Token.THIS, null, null);
    Node qualifiedName = Node.newNode(Token.STRING, "obj.prototype", null);

    getQualifiedName.addChildToEnd(leftSide);
    getQualifiedName.addChildToEnd(qualifiedName);

    assertFalse(checker.shouldTraverse(NodeTraversal.EMPTY, leftSide, getQualifiedName));
  }

  @Test
  public void testShouldReportThisAtLeftSideOfAssignment() {
    Node assign = Node.newNode(Token.ASSIGN, null, null);
    Node leftSide = Node.newNode(Token.THIS, null, null);

    assign.addChildToEnd(leftSide);

    Node nodeToCheck = Node.newNode(Token.THIS, null, null);

    List<Node> children = Lists.newArrayList(assign);
    NodeTraversal traversal = NodeTraversal.create(compiler, new ArrayList<>(), children);

    checker.visit(traversal, nodeToCheck, assign);

    assertEquals(CheckLevel.WARNING, checker.level);
    assertTrue(compiler.reportedErrors.size() > 0);
  }

  @Test
  public void testShouldReportThisAtLeftSideOfGetProperty() {
    Node getProp = Node.newNode(Token.GETPROP, null, null);
    Node leftSide = Node.newNode(Token.THIS, null, null);
    getProp.addChildToEnd(leftSide);

    Node nodeToCheck = Node.newNode(Token.THIS, null, null);

    List<Node> children = Lists.newArrayList(getProp);
    NodeTraversal traversal = NodeTraversal.create(compiler, new ArrayList<>(), children);

    checker.visit(traversal, nodeToCheck, getProp);

    assertEquals(CheckLevel.WARNING, checker.level);
    assertTrue(compiler.reportedErrors.size() > 0);
  }

  @Test
  public void testShouldNotReportThisAtRightSideOfAssignment() {
    Node assign = Node.newNode(Token.ASSIGN, null, null);
    Node leftSide = Node.newNode(Node.newNode(Token.THIS, null, null), null, null);
    Node rightSide = Node.newNode(Node.newNode(Token.THIS, null, null), null, null);

    assign.addChildToEnd(leftSide);
    assign.addChildToEnd(rightSide);

    Node nodeToCheck = Node.newNode(Token.THIS, null, null);

    List<Node> children = Lists.newArrayList(assign);
    NodeTraversal traversal = NodeTraversal.create(compiler, new ArrayList<>(), children);

    checker.visit(traversal, nodeToCheck, assign);

    assertEquals(CheckLevel.UNKNOWN, checker.level);
    assertFalse(compiler.reportedErrors.isEmpty());
  }

  @Test
  public void testShouldNotReportThisAtRightSideOfGetProperty() {
    Node getProp = Node.newNode(Token.GETPROP, null, null);
    Node leftSide = Node.newNode(Node.newNode(Token.THIS, null, null), null, null);
    getProp.addChildToEnd(leftSide);

    Node nodeToCheck = Node.newNode(Token.THIS, null, null);

    List<Node> children = Lists.newArrayList(getProp);
    NodeTraversal traversal = NodeTraversal.create(compiler, new ArrayList<>(), children);

    checker.visit(traversal, nodeToCheck, getProp);

    assertEquals(CheckLevel.UNKNOWN, checker.level);
    assertFalse(compiler.reportedErrors.isEmpty());
  }

  @Test
  public void testShouldNotReportThisAtRightSideOfGetPropertyWithPrototype() {
    Node getProp = Node.newNode(Token.GETPROP, null, null);
    Node leftSide = Node.newNode(Node.newNode(Token.THIS, null, null), null, null);
    Node prototype = Node.newNode(Token.STRING, "prototype", null);
    getProp.addChildToEnd(leftSide);
    getProp.addChildToEnd(prototype);

    Node nodeToCheck = Node.newNode(Token.THIS, null, null);

    List<Node> children = Lists.newArrayList(getProp);
    NodeTraversal traversal = NodeTraversal.create(compiler, new ArrayList<>(), children);

    checker.visit(traversal, nodeToCheck, getProp);

    assertEquals(CheckLevel.UNKNOWN, checker.level);
    assertFalse(compiler.reportedErrors.isEmpty());
  }

  @Test
  public void testShouldNotReportThisAtRightSideOfGetQualifiedName() {
    Node getQualifiedName = Node.newNode(Token.GETQUALIFIEDNAME, null, null);
    Node leftSide = Node.newNode(Node.newNode(Token.THIS, null, null), null, null);
    Node qualifiedName = Node.newNode(Token.STRING, "obj.prototype", null);

    getQualifiedName.addChildToEnd(leftSide);
    getQualifiedName.addChildToEnd(qualifiedName);

    Node nodeToCheck = Node.newNode(Token.THIS, null, null);

    List<Node> children = Lists.newArrayList(getQualifiedName);
    NodeTraversal traversal = NodeTraversal.create(compiler, new ArrayList<>(), children);

    checker.visit(traversal, nodeToCheck, getQualifiedName);

    assertEquals(CheckLevel.UNKNOWN, checker.level);
    assertFalse(compiler.reportedErrors.isEmpty());
  }

  @Test
  public void testShouldReportThisAnnotatedFunction() {
    Node function = Node.newNode(Token.FUNCTION, null, null);

    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setThisType(true);

    function.setJSDocInfo(jsDoc);

    Node nodeToCheck = Node.newNode(Token.THIS, null, null);

    List<Node> children = Lists.newArrayList(function);
    NodeTraversal traversal = NodeTraversal.create(compiler, new ArrayList<>(), children);

    checker.visit(traversal, nodeToCheck, function);

    assertEquals(CheckLevel.WARNING, checker.level);
    assertTrue(compiler.reportedErrors.size() > 0);
  }

  @Test
  public void testShouldReportThisAtPrototypeMethod() {
    Node function = Node.newNode(Token.FUNCTION, null, null);

    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setConstructor(true);
    jsDoc.setPrototype(true);

    function.setJSDocInfo(jsDoc);

    Node nodeToCheck = Node.newNode(Token.THIS, null, null);

    List<Node> children = Lists.newArrayList(function);
    NodeTraversal traversal = NodeTraversal.create(compiler, new ArrayList<>(), children);

    checker.visit(traversal, nodeToCheck, function);

    assertEquals(CheckLevel.UNKNOWN, checker.level);
    assertFalse(compiler.reportedErrors.isEmpty());
  }

  @Test
  public void testShouldReportThisAtFunctionWithoutAnnotation() {
    Node function = Node.newNode(Token.FUNCTION, null, null);

    Node nodeToCheck = Node.newNode(Token.THIS, null, null);

    List<Node> children = Lists.newArrayList(function);
    NodeTraversal traversal = NodeTraversal.create(compiler, new ArrayList<>(), children);

    checker.visit(traversal, nodeToCheck, function);

    assertEquals(CheckLevel.WARNING, checker.level);
    assertTrue(compiler.reportedErrors.size() > 0);
  }

  @Test(expected = NullPointerException.class)
  public void testShouldThrowExceptionIfCompilerIsNull() {
    CheckGlobalThis checker = new CheckGlobalThis(null, OFF);
  }
}