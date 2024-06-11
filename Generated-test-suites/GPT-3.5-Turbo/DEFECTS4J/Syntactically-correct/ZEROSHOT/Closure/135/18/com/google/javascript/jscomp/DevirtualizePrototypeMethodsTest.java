import static org.junit.Assert.*;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {
  
  @Test
  public void testIsCall() {
    // Test case 1: site is a call expression
    Node node = new Node(Token.NAME, "foo");
    Node parent = new Node(Token.CALL, node);
    UseSite site = new UseSite(node, parent);
    assertTrue(DevirtualizePrototypeMethods.isCall(site));

    // Test case 2: site is not a call expression
    node = new Node(Token.NAME, "foo");
    parent = new Node(Token.ADD, node);
    site = new UseSite(node, parent);
    assertFalse(DevirtualizePrototypeMethods.isCall(site));
  }

  @Test
  public void testIsPrototypeMethodDefinition() {
    // Test case 1: node is a prototype method definition
    Node node = new Node(Token.GETPROP,
      new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "prototype")),
      new Node(Token.STRING, "foo")
    );
    Node parent = new Node(Token.ASSIGN, node, new Node(Token.FUNCTION));
    assertTrue(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(node));

    // Test case 2: node is not a prototype method definition
    node = new Node(Token.GETPROP,
      new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "foo")),
      new Node(Token.STRING, "bar")
    );
    parent = new Node(Token.ASSIGN, node, new Node(Token.FUNCTION));
    assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(node));
  }

  @Test
  public void testGetRewrittenMethodName() {
    String originalMethodName = "bar";
    String expected = "JSCompiler_StaticMethods_bar";
    String actual = DevirtualizePrototypeMethods.getRewrittenMethodName(originalMethodName);
    assertEquals(expected, actual);
  }

  @Test
  public void testRewriteDefinitionIfEligible() {
    // Test case 1: definition is eligible for rewrite
    // TODO: Add test case

    // Test case 2: definition is not eligible for rewrite
    // TODO: Add test case
  }

  @Test
  public void testIsEligibleDefinition() {
    // Test case 1: definition is eligible
    // TODO: Add test case

    // Test case 2: definition is not eligible
    // TODO: Add test case
  }

  @Test
  public void testRewriteCallSites() {
    // TODO: Add test case
  }

  @Test
  public void testRewriteDefinition() {
    // TODO: Add test case
  }

  @Test
  public void testFixFunctionType() {
    // TODO: Add test case
  }

  @Test
  public void testReplaceReferencesToThis() {
    // TODO: Add test case
  }
  
}