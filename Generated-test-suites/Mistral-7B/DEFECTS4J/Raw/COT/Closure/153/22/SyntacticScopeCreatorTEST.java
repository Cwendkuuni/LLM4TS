import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.JSError.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

 private ScopeCreator syntacticScopeCreator;
 private Node rootNode;

 @Before
 public void setUp() {
 // Initialize the syntacticScopeCreator with a dummy AbstractCompiler
 syntacticScopeCreator = new SyntacticScopeCreator(new DummyCompiler());

 // Initialize the rootNode with a simple expression like "function test() {}"
 rootNode = new Node(Token.FUNCTION, null, null, null, null, new SourceName("test.js"));
 rootNode.setProp(Node.FUNCTION_IS_GEN_EXP_PROP, false);
 rootNode.setFirstChild(new Node(Token.NAME, "test"));
 rootNode.getLastChild().setNext(new Node(Token.LPAREN, null, null, null, null, null));
 rootNode.getLastChild().getLastChild().setNext(new Node(Token.RPAREN, null, null, null, null, null));
 rootNode.getLastChild().getNext().setType(Token.LCURLYBRACE);
 }

 @Test
 public void testCreateScope() {
 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
 assertNotNull(globalScope);
 assertEquals(Scope.Type.GLOBAL, globalScope.getType());
 }

 @Test(expected = AssertionError.class)
 public void testCreateScopeTwice() {
 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
 Scope globalScope2 = syntacticScopeCreator.createScope(rootNode, null);
 fail("Expected an error, but received a valid scope instead.");
 }

 @Test
 public void testCreateFunctionScope() {
 Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null);
 functionNode.setProp(Node.FUNCTION_IS_GEN_EXP_PROP, false);
 functionNode.setFirstChild(new Node(Token.NAME, "test"));
 functionNode.getLastChild().setNext(new Node(Token.LPAREN, null, null, null, null, null));
 functionNode.getLastChild().getLastChild().setNext(new Node(Token.RPAREN, null, null, null, null, null));
 functionNode.getLastChild().getNext().setType(Token.LCURLYBRACE);

 // Create a global scope
 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);

 // Create a function scope
 Scope functionScope = syntacticScopeCreator.createScope(functionNode, globalScope);

 // Check that the function scope is a child of the global scope
 assertEquals(globalScope, functionScope.getParent());
 }

 @Test
 public void testScanRootFunctionWithArgument() {
 Node functionNodeWithArgument = new Node(Token.FUNCTION, null, null, null, null, new SourceName("test.js"));
 functionNodeWithArgument.setProp(Node.FUNCTION_IS_GEN_EXP_PROP, false);
 functionNodeWithArgument.setFirstChild(new Node(Token.NAME, "test"));
 functionNodeWithArgument.getLastChild().setNext(new Node(Token.LPAREN, null, null, null, null, null));
 Node argumentsNode = functionNodeWithArgument.getLastChild().getNext();
 argumentsNode.setType(Token.LP);
 Node argumentNode = argumentsNode.getFirstChild();
 argumentNode.setType(Token.NAME);
 argumentNode.setNext(new Node(Token.COMMA, null, null, null, null, null));
 argumentNode.getString().setValue("arg1");
 Node bodyNode = new Node(Token.LCURLYBRACE, null, null, null, null, null);
 functionNodeWithArgument.getLastChild().setNext(bodyNode);

 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
 Scope functionScope = syntacticScopeCreator.createScope(functionNodeWithArgument, globalScope);

 // Check that 'arguments' is in the function scope
 Scope.Var argumentsVar = functionScope.getVar("arguments");
 assertNotNull(argumentsVar);
 assertEquals(Node.class, argumentsVar.getNode().getClass());
 assertTrue(argumentsVar.getNode().getParentNode() instanceof Scope);
 assertTrue(argumentsVar.getNode().getParentNode().equals(functionScope));

 // Check that 'arg1' is in the function scope
 Scope.Var arg1Var = functionScope.getVar("arg1");
 assertNotNull(arg1Var);
 assertEquals(Node.class, arg1Var.getNode().getClass());
 assertTrue(arg1Var.getNode().getParentNode() instanceof Scope);
 assertTrue(arg1Var.getNode().getParentNode().equals(functionScope));
 }

 @Test
 public void testScanRootFunctionWithEmptyName() {
 Node functionNodeWithEmptyName = new Node(Token.FUNCTION, null, null, null, null, new SourceName("test.js"));
 functionNodeWithEmptyName.setProp(Node.FUNCTION_IS_GEN_EXP_PROP, false);
 functionNodeWithEmptyName.setFirstChild(new Node(Token.NAME, ""));
 functionNodeWithEmptyName.getLastChild().setNext(new Node(Token.LPAREN, null, null, null, null, null));
 functionNodeWithEmptyName.getLastChild().getLastChild().setNext(new Node(Token.RPAREN, null, null, null, null, null));
 functionNodeWithEmptyName.getLastChild().getNext().setType(Token.LCURLYBRACE);

 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
 Scope functionScope = syntacticScopeCreator.createScope(functionNodeWithEmptyName, globalScope);

 // Check that the function scope is empty
 assertTrue(functionScope.isEmpty());
 }

 @Test
 public void testScanRootBlock() {
 Node blockNode = new Node(Token.SCRIPT, null, null, null, null, null);

 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
 Scope blockScope = syntacticScopeCreator.createScope(blockNode, globalScope);

 // Check that the block scope is empty
 assertTrue(blockScope.isEmpty());
 }

 @Test
 public void testScanVarsFunctionExpression() {
 Node functionNodeWithArguments = new Node(Token.FUNCTION, null, null, null, null, new SourceName("test.js"));
 functionNodeWithArguments.setProp(Node.FUNCTION_IS_GEN_EXP_PROP, false);
 functionNodeWithArguments.setFirstChild(new Node(Token.NAME, "test"));
 functionNodeWithArguments.getLastChild().setNext(new Node(Token.LPAREN, null, null, null, null, null));
 Node argumentsNode = functionNodeWithArguments.getLastChild().getNext();
 argumentsNode.setType(Token.LP);
 Node argumentNode1 = argumentsNode.getFirstChild();
 argumentNode1.setType(Token.NAME);
 argumentNode1.setNext(new Node(Token.COMMA, null, null, null, null, null));
 Node argumentNode2 = argumentNode1.getNext();
 argumentNode2.setType(Token.NAME);
 argumentNode2.setNext(new Node(Token.COMMA, null, null, null, null, null));
 Node argumentNode3 = argumentNode2.getNext();
 argumentNode3.setType(Token.NAME);
 Node bodyNode = new Node(Token.LCURLYBRACE, null, null, null, null, null);
 functionNodeWithArguments.getLastChild().setNext(bodyNode);

 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
 Scope functionScope = syntacticScopeCreator.createScope(functionNodeWithArguments, globalScope);

 // Check that 'test' is in the function scope
 Scope.Var testVar = functionScope.getVar("test");
 assertNotNull(testVar);
 assertEquals(Node.class, testVar.getNode().getClass());
 assertTrue(testVar.getNode().getParentNode() instanceof Scope);
 assertTrue(testVar.getNode().getParentNode().equals(functionScope));

 // Check that 'arg1', 'arg2' and 'arg3' are in the function scope
 Scope.Var arg1Var = functionScope.getVar("arg1");
 assertNotNull(arg1Var);
 assertEquals(Node.class, arg1Var.getNode().getClass());
 assertTrue(arg1Var.getNode().getParentNode() instanceof Scope);
 assertTrue(arg1Var.getNode().getParentNode().equals(functionScope));

 Scope.Var arg2Var = functionScope.getVar("arg2");
 assertNotNull(arg2Var);
 assertEquals(Node.class, arg2Var.getNode().getClass());
 assertTrue(arg2Var.getNode().getParentNode() instanceof Scope);
 assertTrue(arg2Var.getNode().getParentNode().equals(functionScope));

 Scope.Var arg3Var = functionScope.getVar("arg3");
 assertNotNull(arg3Var);
 assertEquals(Node.class, arg3Var.getNode().getClass());
 assertTrue(arg3Var.getNode().getParentNode() instanceof Scope);
 assertTrue(arg3Var.getNode().getParentNode().equals(functionScope));
 }

 @Test
 public void testScanVarsFunctionDeclaration() {
 Node functionNodeWithArguments = new Node(Token.FUNCTION, null, null, null, null, new SourceName("test.js"));
 functionNodeWithArguments.setProp(Node.FUNCTION_IS_GEN_EXP_PROP, false);
 functionNodeWithArguments.setFirstChild(new Node(Token.NAME, "test"));
 functionNodeWithArguments.getLastChild().setNext(new Node(Token.LPAREN, null, null, null, null, null));
 Node argumentsNode = functionNodeWithArguments.getLastChild().getNext();
 argumentsNode.setType(Token.LP);
 Node argumentNode1 = argumentsNode.getFirstChild();
 argumentNode1.setType(Token.NAME);
 argumentNode1.setNext(new Node(Token.COMMA, null, null, null, null, null));
 Node argumentNode2 = argumentNode1.getNext();
 argumentNode2.setType(Token.NAME);
 argumentNode2.setNext(new Node(Token.RPAREN, null, null, null, null, null));
 Node bodyNode = new Node(Token.LCURLYBRACE, null, null, null, null, null);
 functionNodeWithArguments.getLastChild().setNext(bodyNode);
 Node functionNameNode = functionNodeWithArguments.getFirstChild();

 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
 Scope functionScope = syntacticScopeCreator.createScope(functionNodeWithArguments, globalScope);

 // Check that 'test' is in the function scope
 Scope.Var testVar = functionScope.getVar("test");
 assertNotNull(testVar);
 assertEquals(Node.class, testVar.getNode().getClass());
 assertTrue(testVar.getNode().getParentNode() instanceof Scope);
 assertTrue(testVar.getNode().getParentNode().equals(functionScope));

 // Check that 'arg1' and 'arg2' are not in the function scope
 assertNull(functionScope.getVar("arg1"));
 assertNull(functionScope.getVar("arg2"));
 }

 @Test
 public void testScanVarsFunctionWithEmptyName() {
 Node functionNodeWithArguments = new Node(Token.FUNCTION, null, null, null, null, new SourceName("test.js"));
 functionNodeWithArguments.setProp(Node.FUNCTION_IS_GEN_EXP_PROP, false);
 functionNodeWithArguments.setFirstChild(new Node(Token.NAME, ""));
 functionNodeWithArguments.getLastChild().setNext(new Node(Token.LPAREN, null, null, null, null, null));
 Node argumentsNode = functionNodeWithArguments.getLastChild().getNext();
 argumentsNode.setType(Token.LP);
 Node argumentNode1 = argumentsNode.getFirstChild();
 argumentNode1.setType(Token.NAME);
 argumentNode1.setNext(new Node(Token.RPAREN, null, null, null, null, null));
 Node bodyNode = new Node(Token.LCURLYBRACE, null, null, null, null, null);
 functionNodeWithArguments.getLastChild().setNext(bodyNode);

 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
 Scope functionScope = syntacticScopeCreator.createScope(functionNodeWithArguments, globalScope);

 // Check that the function scope is empty
 assertTrue(functionScope.isEmpty());
 }

 @Test
 public void testScanVarsCatchBlock() {
 Node catchNode = new Node(Token.CATCH, null, null, null, null, null);
 Node functionNodeWithArguments = new Node(Token.FUNCTION, null, null, null, null, new SourceName("test.js"));
 functionNodeWithArguments.setProp(Node.FUNCTION_IS_GEN_EXP_PROP, false);
 functionNodeWithArguments.setFirstChild(new Node(Token.NAME, "test"));
 functionNodeWithArguments.getLastChild().setNext(new Node(Token.LPAREN, null, null, null, null, null));
 Node argumentsNode = functionNodeWithArguments.getLastChild().getNext();
 argumentsNode.setType(Token.LP);
 Node argumentNode1 = argumentsNode.getFirstChild();
 argumentNode1.setType(Token.NAME);
 argumentNode1.setNext(new Node(Token.RPAREN, null, null, null, null, null));
 Node bodyNode = new Node(Token.LCURLYBRACE, null, null, null, null, null);
 Node catchBlockNode = new Node(Token.NAME, "catchBlock");
 catchBlockNode.setNext(new Node(Token.LPAREN, null, null, null, null, null));
 catchBlockNode.getLastChild().setType(Token.RPAREN);
 catchNode.setFirstChild(new Node(Token.NAME, "test"));
 catchNode.getLastChild().setNext(catchBlockNode);
 functionNodeWithArguments.getLastChild().setNext(catchNode);
 functionNodeWithArguments.getLastChild().getNext().setNext(bodyNode);

 Scope globalScope = syntacticScopeCreator.createScope(rootNode, null);
 Scope functionScope = syntacticScopeCreator.createScope(functionNodeWithArguments, globalScope);

 // Check that 'test' is in the function scope
 Scope.Var testVar = functionScope.getVar("test");
 assertNotNull(testVar);
 assertEquals(Node.class, testVar.getNode().getClass());
 assertTrue(testVar.getNode().getParentNode() instanceof Scope);
 assertTrue(testVar.getNode().getParentNode().equals(functionScope));

 // Check that 'catchBlock' is in the function scope
 Scope.Var catchBlockVar = functionScope.getVar("catchBlock");
 assertNotNull(catchBlockVar);
 assertEquals(Node.class, catchBlockVar.getNode().getClass());
 assertTrue(catchBlockVar.getNode().getParentNode() instanceof Scope);
 assertTrue(catchBlockVar.getNode().getParentNode().equals(functionScope));
 }

}

class DummyCompiler implements AbstractCompiler {
 @Override
 public void report(JSError error) {
 throw new AssertionError(error.getMessage());
 }
}