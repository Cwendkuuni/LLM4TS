import static org.junit.Assert.*;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {
  
  @Test
  public void testRewriteMethodDefinition() {
    // create a mock compiler
    AbstractCompiler compiler = new MockCompiler();
    
    // create an instance of DevirtualizePrototypeMethods
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    
    // create a method definition node to test
    Node methodDefNode = new Node(Token.FUNCTION,
        new Node(Token.NAME, new Node(Token.STRING, "foo")),
        new Node(Token.PARAM_LIST),
        new Node(Token.BLOCK));
    
    // create a test case for rewriting method definition
    devirtualizer.rewriteDefinition(methodDefNode, "bar");
    
    // verify the rewritten method definition
    assertEquals("function bar(bar$self) {}", methodDefNode.toString());
  }
  
  @Test
  public void testRewriteCallSites() {
    // create a mock compiler
    AbstractCompiler compiler = new MockCompiler();
    
    // create an instance of DevirtualizePrototypeMethods
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    
    // create a method definition node to test
    Node methodCallNode = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.NAME, "obj"), new Node(Token.STRING, "foo")),
        new Node(Token.STRING, "arg1"));
    
    // create a test case for rewriting call sites
    devirtualizer.rewriteCallSites(methodCallNode, "newFoo");
    
    // verify the rewritten call site
    assertEquals("newFoo(obj, 'arg1')", methodCallNode.toString());
  }
  
  @Test
  public void testIsPrototypeMethodDefinition() {
    // create a test method node to test
    Node testNode = new Node(Token.GETPROP,
        new Node(Token.GETPROP, new Node(Token.NAME, "Test"), new Node(Token.STRING, "prototype")),
        new Node(Token.STRING, "method"));
    
    // check if the node is a prototype method definition
    assertTrue(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(testNode));
  }
  
  @Test
  public void testIsEligibleDefinition() {
    // create a mock compiler
    AbstractCompiler compiler = new MockCompiler();
    
    // create an instance of DevirtualizePrototypeMethods
    DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
    
    // create a test definition site
    DefinitionSite testDefSite = new DefinitionSite();
    testDefSite.inExterns = false;
    testDefSite.inGlobalScope = true;
    
    // check if the definition site is eligible for rewrite
    assertTrue(devirtualizer.isEligibleDefinition(testDefSite));
  }
  
  @Test
  public void testReplaceReferencesToThis() {
    // create a test node with references to "this"
    Node testNode = new Node(Token.THIS);
    
    // replace references to "this" with a new name
    DevirtualizePrototypeMethods.replaceReferencesToThis(testNode, "self");
    
    // verify the replacement
    assertEquals("self", testNode.getString());
  }
}