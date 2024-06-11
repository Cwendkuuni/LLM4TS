import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compilerMock;

  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = new Node(Token.FUNCTION);
    assertTrue(checker.shouldTraverse(new NodeTraversal(null), functionNode, null));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node constructorNode = new Node(Token.FUNCTION);
    JSDocInfo constructorJsDoc = new JSDocInfo();
    constructorJsDoc.setConstructor(true);
    constructorNode.setJSDocInfo(constructorJsDoc);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), constructorNode, null));
    assertFalse(checker.shouldTraverse(new NodeTraversal(null), constructorNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo functionJsDoc = new JSDocInfo();
    functionJsDoc.setThisType(true);
    functionNode.setJSDocInfo(functionJsDoc);

    assertFalse(checker.shouldTraverse(new NodeTraversal(null), functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionOutsideOfAssignOrPropertyAccess() {
    Node functionNode = new Node(Token.FUNCTION);

    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.NAME, "a");
    Node rhsNode = functionNode;
    assignNode.addChildrenToEnd(lhsNode, rhsNode);

    Node propertyAccessNode = new Node(Token.GETPROP, lhsNode, new Node(Token.NAME, "prototype"));

    assertFalse(checker.shouldTraverse(new NodeTraversal(null), functionNode, null));
    assertFalse(checker.shouldTraverse(new NodeTraversal(null), functionNode, assignNode));
    assertFalse(checker.shouldTraverse(new NodeTraversal(null), functionNode, propertyAccessNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.THIS);
    Node rhsNode = new Node(Token.NAME, "a");
    assignNode.addChildrenToEnd(lhsNode, rhsNode);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), lhsNode, assignNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.THIS);
    Node rhsNode = new Node(Token.NAME, "a");
    assignNode.addChildrenToEnd(lhsNode, rhsNode);

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildrenToEnd(assignNode, functionNode);

    when(compilerMock.report(mock(NodeTraversal.class).makeError(lhsNode, checker.level, DiagnosticType.warning(null, null)))).thenReturn(null);

    checker.visit(new NodeTraversal(null), functionNode, parent);

    assertTrue(when(compilerMock.reportCount()).thenReturn(1).get());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.NAME, "a");
    Node rhsNode = new Node(Token.THIS);
    assignNode.addChildrenToEnd(lhsNode, rhsNode);

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildrenToEnd(assignNode, functionNode);

    checker.visit(new NodeTraversal(null), functionNode, parent);

    assertTrue(when(compilerMock.reportCount()).thenReturn(0).get());
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    Node propertyAccessNode = new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.NAME, "prototype"));
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildrenToEnd(propertyAccessNode, functionNode);

    when(compilerMock.report(mock(NodeTraversal.class).makeError(propertyAccessNode, checker.level, DiagnosticType.warning(null, null)))).thenReturn(null);

    checker.visit(new NodeTraversal(null), functionNode, parent);

    assertTrue(when(compilerMock.reportCount()).thenReturn(1).get());
  }

  @Test
  public void testShouldNotReportThisOnPrototypePropertyAccess() {
    Node propertyAccessNode = new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.NAME, "prototype"));
    Node propertyNameNode = new Node(Token.NAME, "prototype");
    Node getPropertyNode = new Node(Token.GETPROP, new Node(Token.THIS), propertyNameNode);
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildrenToEnd(getPropertyNode, functionNode);

    checker.visit(new NodeTraversal(null), functionNode, parent);

    assertTrue(when(compilerMock.reportCount()).thenReturn(0).get());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfNestedAssignment() {
    Node assignNode1 = new Node(Token.ASSIGN);
    Node lhsNode1 = new Node(Token.THIS);
    Node rhsNode1 = new Node(Token.NAME, "a");
    assignNode1.addChildrenToEnd(lhsNode1, rhsNode1);

    Node assignNode2 = new Node(Token.ASSIGN);
    Node lhsNode2 = new Node(Token.NAME, "b");
    Node rhsNode2 = new Node(Token.NAME, "c");
    assignNode2.addChildrenToEnd(lhsNode2, rhsNode2);

    Node assignNode3 = new Node(Token.ASSIGN);
    Node lhsNode3 = assignNode1;
    Node rhsNode3 = assignNode2;
    assignNode3.addChildrenToEnd(lhsNode3, rhsNode3);

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildrenToEnd(assignNode3, functionNode);

    when(compilerMock.report(mock(NodeTraversal.class).makeError(lhsNode1, checker.level, DiagnosticType.warning(null, null)))).thenReturn(null);

    checker.visit(new NodeTraversal(null), functionNode, parent);

    assertTrue(when(compilerMock.reportCount()).thenReturn(1).get());
  }
}