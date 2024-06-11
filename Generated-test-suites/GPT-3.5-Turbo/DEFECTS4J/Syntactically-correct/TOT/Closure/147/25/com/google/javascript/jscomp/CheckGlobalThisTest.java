import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_NoTraverse() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    JSDocInfo jsDoc = createMock(JSDocInfo.class);

    expect(n.getType()).andReturn(Token.FUNCTION);
    expect(jsDoc.isConstructor()).andReturn(true);
    expect(checker.getFunctionJsDocInfo(n)).andReturn(jsDoc);

    replay(t, n, parent, jsDoc);

    assertFalse(checker.shouldTraverse(t, n, parent));

    verify(t, n, parent, jsDoc);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_NoTraverse() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    JSDocInfo jsDoc = createMock(JSDocInfo.class);

    expect(n.getType()).andReturn(Token.FUNCTION);
    expect(jsDoc.hasThisType()).andReturn(true);
    expect(checker.getFunctionJsDocInfo(n)).andReturn(jsDoc);

    replay(t, n, parent, jsDoc);

    assertFalse(checker.shouldTraverse(t, n, parent));

    verify(t, n, parent, jsDoc);
  }

  @Test
  public void testShouldTraverse_FunctionWithLogicalPlaceForThisAnnotation_NoTraverse() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    JSDocInfo jsDoc = createMock(JSDocInfo.class);

    expect(n.getType()).andReturn(Token.FUNCTION);
    expect(checker.getFunctionJsDocInfo(n)).andReturn(jsDoc);
    expect(parent.getType()).andReturn(Token.BLOCK);

    replay(t, n, parent, jsDoc);

    assertFalse(checker.shouldTraverse(t, n, parent));

    verify(t, n, parent, jsDoc);
  }

  @Test
  public void testShouldTraverse_FunctionWithNonLogicalPlaceForThisAnnotation_Traverse() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    JSDocInfo jsDoc = createMock(JSDocInfo.class);

    expect(n.getType()).andReturn(Token.FUNCTION);
    expect(checker.getFunctionJsDocInfo(n)).andReturn(jsDoc);
    expect(parent.getType()).andReturn(Token.IF);

    replay(t, n, parent, jsDoc);

    assertTrue(checker.shouldTraverse(t, n, parent));

    verify(t, n, parent, jsDoc);
  }

  @Test
  public void testShouldTraverse_LeftSideOfAssignment_Traverse() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    Node assign = createMock(Node.class);

    expect(n.getType()).andReturn(Token.NAME);
    expect(parent.getType()).andReturn(Token.ASSIGN);
    expect(parent.getFirstChild()).andReturn(assign);
    expect(assign.getNext()).andReturn(n);

    replay(t, n, parent, assign);

    assertTrue(checker.shouldTraverse(t, n, parent));

    verify(t, n, parent, assign);
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignmentToPrototypeProperty_NoTraverse() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    Node assign = createMock(Node.class);
    Node lhs = createMock(Node.class);
    Node prototypeProp = createMock(Node.class);
    Node getProp = createMock(Node.class);
    Node llhs = createMock(Node.class);

    expect(n.getType()).andReturn(Token.THIS);
    expect(parent.getType()).andReturn(Token.NAME);
    expect(parent.getFirstChild()).andReturn(assign);
    expect(assign.getNext()).andReturn(n);
    expect(assign.getFirstChild()).andReturn(lhs);
    expect(NodeUtil.isGet(lhs)).andReturn(true);
    expect(lhs.getType()).andReturn(Token.GETPROP);
    expect(lhs.getLastChild().getString()).andReturn("prototype");
    expect(lhs.getFirstChild()).andReturn(prototypeProp);

    expect(prototypeProp.getType()).andReturn(Token.GETPROP);
    expect(prototypeProp.getLastChild().getString()).andReturn("x");

    expect(getProp.getType()).andReturn(Token.GETPROP);
    expect(getProp.getLastChild().getString()).andReturn("x");

    expect(prototypeProp.getFirstChild()).andReturn(getProp);
    expect(llhs.getType()).andReturn(Token.GETPROP);
    expect(llhs.getLastChild().getString()).andReturn("prototype");

    replay(t, n, parent, assign, lhs, prototypeProp, getProp, llhs);

    assertFalse(checker.shouldTraverse(t, n, parent));

    verify(t, n, parent, assign, lhs, prototypeProp, getProp, llhs);
  }

  @Test
  public void testShouldTraverse_RightSideOfAssignmentToNonPrototypeProperty_Traverse() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    Node assign = createMock(Node.class);
    Node lhs = createMock(Node.class);
    Node prop = createMock(Node.class);

    expect(n.getType()).andReturn(Token.THIS);
    expect(parent.getType()).andReturn(Token.NAME);
    expect(parent.getFirstChild()).andReturn(assign);
    expect(assign.getNext()).andReturn(n);
    expect(assign.getFirstChild()).andReturn(lhs);
    expect(NodeUtil.isGet(lhs)).andReturn(true);
    expect(lhs.getType()).andReturn(Token.GETPROP);
    expect(lhs.getLastChild().getString()).andReturn("property");
    expect(lhs.getFirstChild()).andReturn(prop);

    replay(t, n, parent, assign, lhs, prop);

    assertTrue(checker.shouldTraverse(t, n, parent));

    verify(t, n, parent, assign, lhs, prop);
  }

  @Test
  public void testVisit_GlobalThisEncountered_ErrorReported() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    AbstractCompiler compiler = createMock(AbstractCompiler.class);

    expect(n.getType()).andReturn(Token.THIS);
    expect(checker.shouldReportThis(n, parent)).andReturn(true);
    compiler.report(isA(JSError.class));

    replay(t, n, parent, compiler);

    checker.visit(t, n, parent);

    verify(t, n, parent, compiler);
  }

  @Test
  public void testVisit_LeftSideOfAssignment_GlobalThisEncountered_AssignLhsChildSet() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    Node assign = createMock(Node.class);

    expect(n.getType()).andReturn(Token.NAME);
    expect(parent.getType()).andReturn(Token.ASSIGN);
    expect(parent.getFirstChild()).andReturn(assign);
    expect(assign.getFirstChild()).andReturn(n);

    replay(t, n, parent, assign);

    checker.visit(t, n, parent);
    assertEquals(assign, checker.assignLhsChild);

    verify(t, n, parent, assign);
  }

  @Test
  public void testVisit_RightSideOfAssignment_GlobalThisEncountered_AssignLhsChildReset() {
    NodeTraversal t = createMock(NodeTraversal.class);
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);

    expect(n.getType()).andReturn(Token.THIS);
    expect(checker.shouldReportThis(n, parent)).andReturn(false);

    replay(t, n, parent);

    checker.visit(t, n, parent);
    assertNull(checker.assignLhsChild);

    verify(t, n, parent);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJsDoc_ReturnJsDocInfo() {
    Node n = createMock(Node.class);
    JSDocInfo jsDoc = createMock(JSDocInfo.class);

    expect(n.getJSDocInfo()).andReturn(jsDoc);

    replay(n, jsDoc);

    assertEquals(jsDoc, checker.getFunctionJsDocInfo(n));

    verify(n, jsDoc);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc_HasJsDocInParent_ReturnJsDocInfo() {
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    JSDocInfo jsDoc = createMock(JSDocInfo.class);

    expect(n.getJSDocInfo()).andReturn(null);
    expect(n.getParent()).andReturn(parent);
    expect(parent.getType()).andReturn(Token.NAME);
    expect(parent.getJSDocInfo()).andReturn(jsDoc);

    replay(n, parent, jsDoc);

    assertEquals(jsDoc, checker.getFunctionJsDocInfo(n));

    verify(n, parent, jsDoc);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc_HasVarParent_ReturnJsDocInfo() {
    Node n = createMock(Node.class);
    Node parent = createMock(Node.class);
    Node gramps = createMock(Node.class);
    JSDocInfo jsDoc = createMock(JSDocInfo.class);

    expect(n.getJSDocInfo()).andReturn(null);
    expect(n.getParent()).andReturn(parent);
    expect(parent.getType()).andReturn(Token.NAME);
    expect(parent.getParent()).andReturn(gramps);
    expect(gramps.getType()).andReturn(Token.VAR);
    expect(gramps.getJSDocInfo()).andReturn(jsDoc);

    replay(n, parent, gramps, jsDoc);

    assertEquals(jsDoc, checker.getFunctionJsDocInfo(n));

    verify(n, parent, gramps, jsDoc);
  }

}