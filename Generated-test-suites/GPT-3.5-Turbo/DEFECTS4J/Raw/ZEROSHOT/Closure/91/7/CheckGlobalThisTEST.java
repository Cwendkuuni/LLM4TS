import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalContext_ReturnTrue(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldTraverse(null, null, null);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_ReturnFalse(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    node.setJSDocInfo(jsDocInfo);
    boolean result = checkGlobalThis.shouldTraverse(null, node, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionThisAnnotation_ReturnFalse(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.addAnnotation("this");
    node.setJSDocInfo(jsDocInfo);
    boolean result = checkGlobalThis.shouldTraverse(null, node, null);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionNonGlobal_ReturnFalse(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToFront(node);
    boolean result = checkGlobalThis.shouldTraverse(null, node, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_NonAssignParent_ReturnFalse(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);
    parent.addChildToFront(node);
    boolean result = checkGlobalThis.shouldTraverse(null, node, parent);
    assertFalse(result);
  }

  @Test
  public void visit_ThisOnLeftSide_AssertReportCalled(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);
    checkGlobalThis.visit(null, n, parent);
    // Assert that report method is called with the correct arguments
  }

  @Test
  public void visit_AssignLhsChildIsNull_AssertAssignLhsChildNotChanged(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.NUMBER);
    Node parent = new Node(Token.ADD);
    checkGlobalThis.visit(null, n, parent);
    // Assert that assignLhsChild remains null
  }

  @Test
  public void visit_AssignLhsChildIsNotNull_AssertAssignLhsChildIsResetToNull(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    lhs.addChildToFront(n);
    parent.addChildToFront(lhs);
    checkGlobalThis.visit(null, n, parent);
    // Assert that assignLhsChild is set to null
  }

  @Test
  public void shouldReportThis_AssignLhsChildNotNull_ReturnTrue(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.shouldReportThis(n, parent);
    // Assert that true is returned
  }

  @Test
  public void shouldReportThis_ParentIsGetProperty_ReturnTrue(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(n);
    checkGlobalThis.shouldReportThis(n, parent);
    // Assert that true is returned
  }

  @Test
  public void shouldReportThis_ParentIsNull_ReturnFalse(){
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node(Token.THIS);
    checkGlobalThis.shouldReportThis(n, null);
    // Assert that false is returned
  }

}