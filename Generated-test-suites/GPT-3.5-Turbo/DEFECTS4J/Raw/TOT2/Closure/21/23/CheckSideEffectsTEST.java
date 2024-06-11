import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckSideEffects;

public class CheckSideEffectsTest {

  private CheckSideEffects checker;

  @Before
  public void setUp() {
    checker = new CheckSideEffects();
  }

  @Test
  public void testEmptyNode() {
    // Test if an empty node is properly handled
    Node node = new Node(Token.EMPTY);
    assertFalse("Empty node should not be flagged", checker.checkNode(node));
  }

  @Test
  public void testSimpleOperation() {
    // Test if a simple operation with no side effects is flagged
    Node node = new Node(Token.ADD)
                    .addChildToBack(new Node(Token.NUMBER))
                    .addChildToBack(new Node(Token.NUMBER));
    assertTrue("Simple operation with no side effects should be flagged", checker.checkNode(node));
  }

  @Test
  public void testMissingConcatenation() {
    // Test if a string without proper concatenation is flagged
    Node node = new Node(Token.STRING);
    assertTrue("String without proper concatenation should be flagged", checker.checkNode(node));
  }

  @Test
  public void testMissingOperatorUse() {
    // Test if the result of an operator without use is flagged
    Node node = new Node(Token.ADD)
                    .addChildToBack(new Node(Token.NUMBER))
                    .addChildToBack(new Node(Token.NUMBER));
    assertTrue("Unused result of operator should be flagged", checker.checkNode(node));
  }

  @Test
  public void testExcessSemicolonInBlock() {
    // Test if an extra semicolon in a block is properly handled
    Node node = new Node(Token.BLOCK)
                    .addChildToBack(new Node(Token.EMPTY))
                    .addChildToBack(new Node(Token.EMPTY));
    assertFalse("Extra semicolon in block should not be flagged", checker.checkNode(node));
  }

  @Test
  public void testQualifiedNameWithJSDoc() {
    // Test if a qualified name with JSDoc is properly handled
    Node node = new Node(Token.QNAME)
                    .setJSDocInfo(new JSDocInfo())
                    .addChildToBack(new Node(Token.NAME));
    assertFalse("Qualified name with JSDoc should not be flagged", checker.checkNode(node));
  }
}