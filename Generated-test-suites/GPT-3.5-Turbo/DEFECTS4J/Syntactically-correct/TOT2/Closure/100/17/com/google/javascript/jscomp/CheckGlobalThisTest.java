import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // Test the shouldTraverse method with a function node that is not a constructor or has @this annotation
    CheckGlobalThis tester = new CheckGlobalThis(null, null);
    assertTrue(tester.shouldTraverse(null, new Node(Token.FUNCTION), new Node(Token.FUNCTION)));
  }

  @Test
  public void testVisit() {
    // Test the visit method with a Node representing 'this' and a parent node
    CheckGlobalThis tester = new CheckGlobalThis(null, CheckLevel.ERROR);
    tester.visit(new NodeTraversal(null, null), new Node(Token.THIS), new Node(Token.ASSIGN));
    // Assert that a GLOBAL_THIS diagnostic type is reported
    assertNotNull(tester.compiler.getErrors());
  }

  @Test
  public void testShouldReportThis() {
    // Test the shouldReportThis method with a node representing 'this' and a parent node
    CheckGlobalThis tester = new CheckGlobalThis(null, null);
    // Simulate a 'this' node on the left side of an assignment
    tester.visit(new NodeTraversal(null, null), new Node(Token.THIS), new Node(Token.ASSIGN));
    assertTrue(tester.shouldReportThis(new Node(Token.THIS), new Node(Token.ASSIGN)));
  }
}