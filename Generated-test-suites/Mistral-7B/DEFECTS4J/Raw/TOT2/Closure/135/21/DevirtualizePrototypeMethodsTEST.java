import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {

 private Compiler compiler;
 private DevirtualizePrototypeMethods devirtualizer;

 @Before
 public void setUp() {
 compiler = new Compiler();
 devirtualizer = new DevirtualizePrototypeMethods(compiler);
 }

 @Test
 public void testEligibleMethodRewriting() {
 // Create test case for a method that is eligible for rewriting
 Node root = new Node(Token.BLOCK, null);
 Node defNode = new Node(
 Token.GETPROP, null,
 new Node(Token.NAME, "prototype"),
 new Node(Token.NAME, "foo")
 );
 Node funcNode = new Node(Token.FUNCTION, null, new Node[0]);
 Node assignNode = new Node(Token.SETPROP, null, defNode, new Node(Token.NAME, "foo"), funcNode);
 Node callSiteNode = new Node(Token.GETPROP, null, new Node(Token.NAME, "o"), defNode);
 Node callSiteModule = null;
 Node rootModule = new Node(Token.MODULE, null, Lists.newArrayList(root));
 Node callSiteParent = Node.newBlock("callSiteParent");
 Node callSiteGrandParent = Node.newBlock("callSiteGrandParent");
 callSiteGrandParent.addChildToFront(callSiteParent);
 callSiteParent.addChildToFront(callSiteNode);
 rootModule.addChildToFront(callSiteGrandParent);
 compiler.init(rootModule);
 devirtualizer.process(compiler.getExterns(), root);

 // Check that the method was rewritten as a global function
 Node functionDefinition = findFunctionDefinition("JSCompiler_StaticMethods_foo", root);
 Node expectedFunctionBody = new Node(Token.BLOCK, null,
 new Node(Token.VAR, new Node(Token.NAME, "$self")),
 new Node(Token.ASSIGN, null, new Node(Token.NAME, "$self"), callSiteNode.getFirstChild())
 );
 assertEquals("Unexpected function body", expectedFunctionBody, functionDefinition.getLastChild());

 // Check that the call site was updated to call the new global function
 assertEquals("Unexpected call site argument list",
 new Node(Token.CALL, null, new Node(Token.NAME, "JSCompiler_StaticMethods_foo"), new Node[0]),
 callSiteNode.getLastChild());
 }

 @Test
 public void testNotEligibleMethodRewriting() {
 // Create test case for a method that is not eligible for rewriting
 Node root = new Node(Token.BLOCK, null);
 Node defNode = new Node(
 Token.GETPROP, null,
 new Node(Token.NAME, "prototype"),
 new Node(Token.NAME, "bar")
 );
 Node funcNode = new Node(Token.FUNCTION, null, new Node[0]);
 Node assignNode = new Node(Token.SETPROP, null, defNode, new Node(Token.NAME, "bar"), funcNode);
 Node callSiteNode = new Node(Token.GETPROP, null, new Node(Token.NAME, "o"), defNode);
 Node callSiteModule = null;
 Node rootModule = new Node(Token.MODULE, null, Lists.newArrayList(root));
 Node callSiteParent = Node.newBlock("callSiteParent");
 Node callSiteGrandParent = Node.newBlock("callSiteGrandParent");
 callSiteGrandParent.addChildToFront(callSiteParent);
 callSiteParent.addChildToFront(callSiteNode);
 rootModule.addChildToFront(callSiteGrandParent);
 compiler.init(rootModule);
 devirtualizer.process(compiler.getExterns(), root);

 // Check that the method was not rewritten
 Node functionDefinition = findFunctionDefinition("JSCompiler_StaticMethods_bar", root);
 assertNull("Method should not have been rewritten", functionDefinition);

 // Check that the call site was not updated
 assertEquals("Unexpected call site argument list",
 new Node(Token.GETPROP, null, new Node(Token.NAME, "o"), defNode),
 callSiteNode.getLastChild());
 }

 @Test
 public void testGlobalFunctionRenameCollision() {
 // Create test case for a method that collides with a global function
 Node root = new Node(Token.BLOCK, null);
 Node defNode = new Node(
 Token.GETPROP, null,
 new Node(Token.NAME, "prototype"),
 new Node(Token.NAME, "foo")
 );
 Node funcNode = new Node(Token.FUNCTION, null, new Node[0]);
 Node assignNode = new Node(Token.SETPROP, null, defNode, new Node(Token.NAME, "foo"), funcNode);
 Node callSiteNode = new Node(Token.GETPROP, null, new Node(Token.NAME, "o"), defNode);
 Node globalFunctionNode = new Node(
 Token.FUNCTION, null,
 new Node[0],
 new Node(Token.BLOCK, null, new Node(Token.RETURN, null, new Node(Token.NUMBER, 42)))
 );
 Node rootModule = new Node(Token.MODULE, null, Lists.newArrayList(root));
 Node callSiteParent = Node.newBlock("callSiteParent");
 Node callSiteGrandParent = Node.newBlock("callSiteGrandParent");
 Node globalFunctionParent = Node.newBlock("globalFunctionParent");
 Node globalFunctionGrandParent = Node.newBlock("globalFunctionGrandParent");
 globalFunctionGrandParent.addChildToFront(globalFunctionParent);
 globalFunctionParent.addChildToFront(globalFunctionNode);
 rootModule.addChildToFront(callSiteGrandParent);
 callSiteGrandParent.addChildToFront(callSiteParent);
 callSiteParent.addChildToFront(callSiteNode);
 rootModule.addChildToFront(globalFunctionGrandParent);
 compiler.init(rootModule);
 devirtualizer.process(compiler.getExterns(), root);

 // Check that the method was rewritten with a unique name
 Node functionDefinition = findFunctionDefinition("JSCompiler_StaticMethods_foo", root);
 String functionName = functionDefinition.getFirstChild().getString();
 assertTrue("Function name should be unique",
 functionName.contains("_"));

 // Check that the call site was updated to call the new global function
 assertEquals("Unexpected call site argument list",
 new Node(Token.CALL, null, new Node(Token.NAME, "JSCompiler_StaticMethods_foo"), new Node[0]),
 callSiteNode.getLastChild());
 }

 @Test
 public void testFunctionWithVarArgs() {
 // Create test case for a method with var args
 Node root = new Node(Token.BLOCK, null);
 Node defNode = new Node(
 Token.GETPROP, null,
 new Node(Token.NAME, "prototype"),
 new Node(Token.NAME, "foo")
 );
 Node funcNode = new Node(
 Token.FUNCTION, null,
 new Node[]{new Node(Token.NAME, "args")},
 new Node(Token.BLOCK, null, new Node[0])
 );
 Node assignNode = new Node(Token.SETPROP, null, defNode, new Node(Token.NAME, "foo"), funcNode);
 Node callSiteNode = new Node(Token.GETPROP, null, new Node(Token.NAME, "o"), defNode);
 Node callSiteModule = null;
 Node rootModule = new Node(Token.MODULE, null, Lists.newArrayList(root));
 Node callSiteParent = Node.newBlock("callSiteParent");
 Node callSiteGrandParent = Node.newBlock("callSiteGrandParent");
 callSiteGrandParent.addChildToFront(callSiteParent);
 callSiteParent.addChildToFront(callSiteNode);
 rootModule.addChildToFront(callSiteGrandParent);
 compiler.init(rootModule);
 devirtualizer.process(compiler.getExterns(), root);

 // Check that the method was rewritten with var args
 Node functionDefinition = findFunctionDefinition("JSCompiler_StaticMethods_foo", root);
 FunctionType functionType = (FunctionType) functionDefinition.getJSType();
 assertTrue("Function should have var args",
 functionType.isVarArgs());

 // Check that the call site was updated to pass var args as an array
 assertEquals("Unexpected call site argument list",
 new Node(Token.CALL, null, new Node(Token.NAME, "JSCompiler_StaticMethods_foo"),
 new Node(Token.ARRAY, null, new Node[0])),
 callSiteNode.getLastChild());
 }

 @Test(expected = AssertionError.class)
 public void testFunctionWithTypeError() {
 // Create test case for a method that throws a TypeError
 Node root = new Node(Token.BLOCK, null);
 Node defNode = new Node(
 Token.GETPROP, null,
 new Node(Token.NAME, "prototype"),
 new Node(Token.NAME, "foo")
 );
 Node funcNode = new Node(Token.FUNCTION, null, new Node[0]);
 Node assignNode = new Node(Token.SETPROP, null, defNode, new Node(Token.NAME, "foo"), funcNode);
 Node callSiteNode = new Node(Token.GETPROP, null, new Node(Token.NAME, "o"), defNode);
 Node callSiteModule = null;
 Node rootModule = new Node(Token.MODULE, null, Lists.newArrayList(root));
 Node callSiteParent = Node.newBlock("callSiteParent");
 Node callSiteGrandParent = Node.newBlock("callSiteGrandParent");
 callSiteGrandParent.addChildToFront(callSiteParent);
 callSiteParent.addChildToFront(callSiteNode);
 rootModule.addChildToFront(callSiteGrandParent);
 Node throwNode = new Node(Token.THROW, null, new Node(Token.NEW, null, new Node(Token.TYPE, null, "TypeError"), new Node[0]));
 Node body = new Node(Token.BLOCK, null, new Node[0]);
 body.addChildToFront(throwNode);
 funcNode.setLastChild(body);
 compiler.init(rootModule);
 devirtualizer.process(compiler.getExterns(), root);
 }

 @Test
 public void testReentrancy() {
 // Create test case for a method that can be reentered
 Node root = new Node(Token.BLOCK, null);
 Node defNode = new Node(
 Token.GETPROP, null,
 new Node(Token.NAME, "prototype"),
 new Node(Token.NAME, "foo")
 );
 Node funcNode = new Node(Token.FUNCTION, null, new Node[0]);
 Node assignNode = new Node(Token.SETPROP, null, defNode, new Node(Token.NAME, "foo"), funcNode);
 Node callSiteNode = new Node(Token.GETPROP, null, new Node(Token.NAME, "o"), defNode);
 Node callSiteModule = null;
 Node rootModule = new Node(Token.MODULE, null, Lists.newArrayList(root));
 Node callSiteParent = Node.newBlock("callSiteParent");
 Node callSiteGrandParent = Node.newBlock("callSiteGrandParent");
 callSiteGrandParent.addChildToFront(callSiteParent);
 callSiteParent.addChildToFront(callSiteNode);
 rootModule.addChildToFront(callSiteGrandParent);
 compiler.init(rootModule);
 devirtualizer.process(compiler.getExterns(), root);

 Node functionDefinition = findFunctionDefinition("JSCompiler_StaticMethods_foo", root);

 // Call the method directly
 Node directCall = new Node(Token.CALL, null, functionDefinition, new Node[0]);
 Node directCallModule = new Node(Token.MODULE, null, new Node[0]);
 Node directCallParent = Node.newBlock("directCallParent");
 directCallParent.addChildToFront(directCallModule);
 directCallModule.addChildToFront(directCall);
 Node directCallTopLevel = new Node(Token.BLOCK, null, Lists.newArrayList(directCallModule));
 compiler.init(directCallTopLevel);
 compiler.compileTopLevel(directCallTopLevel.getFirstChild());

 // Check that the method was called directly
 Node result = compiler.getCurrentScope().get("result");
 assertEquals("Unexpected result", 42, result.getInt());

 // Call the method through an object
 Node objectNode = new Node(Token.NEW, null, new Node(Token.NAME, "Object"));
 Node objectCallSiteNode = new Node(Token.GETPROP, null, objectNode, defNode);
 Node objectCallSiteModule = new Node(Token.MODULE, null, new Node[0]);
 Node objectCallSiteParent = Node.newBlock("objectCallSiteParent");
 objectCallSiteParent.addChildToFront(objectCallSiteModule);
 objectCallSiteModule.addChildToFront(objectCallSiteParent);
 objectCallSiteParent.addChildToFront(objectCallSiteNode);
 Node objectCallSiteTopLevel = new Node(Token.BLOCK, null, Lists.newArrayList(objectCallSiteModule));
 compiler.init(objectCallSiteTopLevel);
 Node objectThis = compiler.getCurrentScope().get("this");
 objectCallSiteNode.setFirstChild(objectThis);
 compiler.compileTopLevel(objectCallSiteTopLevel.getFirstChild());

 // Check that the method was reentered and returned the same result
 Node objectResult = compiler.getCurrentScope().get("result");
 assertEquals("Unexpected result", 42, objectResult.getInt());
 }

 private Node findFunctionDefinition(String name, Node root) {
 Node currentNode = root;
 while (currentNode != null) {
 if (currentNode.getJSType() != null &&
 currentNode.getJSType().isFunction() &&
 currentNode.getFirstChild().getString().equals(name)) {
 return currentNode;
 }
 currentNode = currentNode.getParent();
 }
 return null;
 }
}