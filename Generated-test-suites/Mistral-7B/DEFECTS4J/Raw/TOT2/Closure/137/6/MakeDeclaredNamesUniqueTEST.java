import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MakeDeclaredNamesUniqueTest {

 @Test
 public void testMakeDeclaredNamesUnique_SimpleFunction() {
 Compiler compiler = new Compiler();
 Node rootNode = new Node(Token.PROGRAM, null);

 // Create a simple function declaration
 Node functionNode = new Node(Token.FUNCTION, null);
 functionNode.setString("testFunction");
 Node functionBody = new Node(Token.BLOCK, null);
 Node returnNode = new Node(Token.RETURN, null);
 Node valueNode = new Node(Token.NUMBER, null, 5);
 returnNode.setFirstChild(valueNode);
 functionBody.setFirstChild(returnNode);
 functionNode.setLastChild(functionBody);

 // Add the function declaration to the root node
 rootNode.setFirstChild(functionNode);

 // Initialize the MakeDeclaredNamesUnique visitor
 MakeDeclaredNamesUnique visitor = new MakeDeclaredNamesUnique();
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
 traversal.addCallback(visitor);

 // Traverse the node tree
 traversal.traverse();

 // Verify that the function name was modified
 assertEquals("testFunction$0", functionNode.getString());
 }

 @Test
 public void testMakeDeclaredNamesUnique_FunctionWithArguments() {
 Compiler compiler = new Compiler();
 Node rootNode = new Node(Token.PROGRAM, null);

 // Create a function declaration with arguments
 Node functionNode = new Node(Token.FUNCTION, null);
 Node functionNameNode = new Node(Token.NAME, null, "testFunction");
 functionNode.setString(functionNameNode.getString());
 Node parametersNode = new Node(Token.BLOCK, null);
 Node arg1Node = new Node(Token.NAME, null, "arg1");
 Node arg2Node = new Node(Token.NAME, null, "arg2");
 parametersNode.setFirstChild(arg1Node);
 parametersNode.setLastChild(arg2Node);
 functionNode.setFirstChild(parametersNode);
 Node functionBody = new Node(Token.BLOCK, null);
 Node returnNode = new Node(Token.RETURN, null);
 Node valueNode = new Node(Token.NUMBER, null, 5);
 returnNode.setFirstChild(valueNode);
 functionBody.setFirstChild(returnNode);
 functionNode.setLastChild(functionBody);

 // Add the function declaration to the root node
 rootNode.setFirstChild(functionNode);

 // Initialize the MakeDeclaredNamesUnique visitor
 MakeDeclaredNamesUnique visitor = new MakeDeclaredNamesUnique();
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
 traversal.addCallback(visitor);

 // Traverse the node tree
 traversal.traverse();

 // Verify that the function name and argument names were modified
 assertEquals("testFunction$0", functionNode.getString());
 assertEquals("arg1$0", arg1Node.getString());
 assertEquals("arg2$1", arg2Node.getString());
 }

 @Test
 public void testMakeDeclaredNamesUnique_FunctionWithSameNameArgument() {
 Compiler compiler = new Compiler();
 Node rootNode = new Node(Token.PROGRAM, null);

 // Create a function declaration with same name argument
 Node functionNode = new Node(Token.FUNCTION, null);
 Node functionNameNode = new Node(Token.NAME, null, "testFunction");
 functionNode.setString(functionNameNode.getString());
 Node parametersNode = new Node(Token.BLOCK, null);
 Node arg1Node = new Node(Token.NAME, null, "arg");
 arg1Node.setFirstChild(new Node(Token.ASSIGN, null, null, null, null, null,
 new Node(Token.NUMBER, null, 1), null));
 arg1Node.setLastChild(new Node(Token.ASSIGN, null, null, null, null, null,
 new Node(Token.NUMBER, null, 2), null));
 parametersNode.setFirstChild(arg1Node);
 parametersNode.setLastChild(arg1Node);
 functionNode.setFirstChild(parametersNode);
 Node functionBody = new Node(Token.BLOCK, null);
 Node returnNode = new Node(Token.RETURN, null);
 Node valueNode = new Node(Token.NUMBER, null, 5);
 returnNode.setFirstChild(valueNode);
 functionBody.setFirstChild(returnNode);
 functionNode.setLastChild(functionBody);

 // Add the function declaration to the root node
 rootNode.setFirstChild(functionNode);

 // Initialize the MakeDeclaredNamesUnique visitor
 MakeDeclaredNamesUnique visitor = new MakeDeclaredNamesUnique();
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
 traversal.addCallback(visitor);

 // Traverse the node tree
 traversal.traverse();

 // Verify that the function name and argument names were modified
 assertEquals("testFunction$0", functionNode.getString());
 assertEquals("arg$0", arg1Node.getFirstChild().getString());
 assertEquals("arg$1", arg1Node.getLastChild().getString());
 }

 @Test
 public void testMakeDeclaredNamesUnique_FunctionWithMultipleArguments() {
 Compiler compiler = new Compiler();
 Node rootNode = new Node(Token.PROGRAM, null);

 // Create a function declaration with multiple arguments
 Node functionNode = new Node(Token.FUNCTION, null);
 Node functionNameNode = new Node(Token.NAME, null, "testFunction");
 functionNode.setString(functionNameNode.getString());
 Node parametersNode = new Node(Token.BLOCK, null);
 Node arg1Node = new Node(Token.NAME, null, "arg1");
 arg1Node.setLastChild(new Node(Token.COMMA, null));
 Node arg2Node = new Node(Token.NAME, null, "arg2");
 parametersNode.setFirstChild(arg1Node);
 parametersNode.setLastChild(arg2Node);
 functionNode.setFirstChild(parametersNode);
 Node functionBody = new Node(Token.BLOCK, null);
 Node returnNode = new Node(Token.RETURN, null);
 Node valueNode = new Node(Token.NUMBER, null, 5);
 returnNode.setFirstChild(valueNode);
 functionBody.setFirstChild(returnNode);
 functionNode.setLastChild(functionBody);

 // Add the function declaration to the root node
 rootNode.setFirstChild(functionNode);

 // Initialize the MakeDeclaredNamesUnique visitor
 MakeDeclaredNamesUnique visitor = new MakeDeclaredNamesUnique();
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
 traversal.addCallback(visitor);

 // Traverse the node tree
 traversal.traverse();

 // Verify that the function name and argument names were modified
 assertEquals("testFunction$0", functionNode.getString());
 assertEquals("arg1$0", arg1Node.getString());
 assertEquals("arg2$1", arg2Node.getString());
 }

 @Test(expected = RuntimeException.class)
 public void testMakeDeclaredNamesUnique_Exception() {
 Compiler compiler = new Compiler();
 Node rootNode = new Node(Token.PROGRAM, null);

 // Create a function declaration with exception
 Node functionNode = new Node(Token.FUNCTION, null);
 Node functionNameNode = new Node(Token.NAME, null, "testFunction");
 functionNode.setString(functionNameNode.getString());
 Node functionBody = new Node(Token.BLOCK, null);
 Node catchNode = new Node(Token.CATCH, null);
 Node catchNameNode = new Node(Token.NAME, null, "exception");
 catchNode.setFirstChild(catchNameNode);
 functionBody.setFirstChild(catchNode);
 functionNode.setLastChild(functionBody);
 Node throwNode = new Node(Token.THROW, null);
 functionBody.setLastChild(throwNode);

 // Add the function declaration to the root node
 rootNode.setFirstChild(functionNode);

 // Initialize the MakeDeclaredNamesUnique visitor
 MakeDeclaredNamesUnique visitor = new MakeDeclaredNamesUnique();
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
 traversal.addCallback(visitor);

 // Traverse the node tree
 try {
 traversal.traverse();
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test(expected = RuntimeException.class)
 public void testMakeDeclaredNamesUnique_RecursiveFunction() {
 Compiler compiler = new Compiler();
 Node rootNode = new Node(Token.PROGRAM, null);

 // Create a recursive function
 Node functionNode = new Node(Token.FUNCTION, null);
 Node functionNameNode = new Node(Token.NAME, null, "testFunction");
 functionNode.setString(functionNameNode.getString());
 Node functionBody = new Node(Token.BLOCK, null);
 Node callNode = new Node(Token.CALL, null, null, null, null, null);
 Node functionCallNode = new Node(Token.NAME, null, "testFunction");
 callNode.setFirstChild(functionCallNode);
 Node returnNode = new Node(Token.RETURN, null);
 Node valueNode = new Node(Token.NUMBER, null, 5);
 returnNode.setFirstChild(valueNode);
 functionBody.setFirstChild(callNode);
 functionBody.setLastChild(returnNode);
 functionNode.setLastChild(functionBody);

 // Add the function declaration to the root node
 rootNode.setFirstChild(functionNode);

 // Initialize the MakeDeclaredNamesUnique visitor
 MakeDeclaredNamesUnique visitor = new MakeDeclaredNamesUnique();
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
 traversal.addCallback(visitor);

 // Traverse the node tree
 try {
 traversal.traverse();
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testMakeDeclaredNamesUnique_GlobalVariable() {
 Compiler compiler = new Compiler();
 Node rootNode = new Node(Token.PROGRAM, null);
 Node globalVariableNode = new Node(Token.VAR, null, new Node(Token.NAME, null, "test"));
 globalVariableNode.setLastChild(new Node(Token.ASSIGN, null, null, null, null, null,
 new Node(Token.NUMBER, null, 5), null));
 rootNode.setFirstChild(globalVariableNode);

 // Initialize the MakeDeclaredNamesUnique visitor
 MakeDeclaredNamesUnique visitor = new MakeDeclaredNamesUnique();
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
 traversal.addCallback(visitor);

 // Traverse the node tree
 traversal.traverse();

 // Verify that the global variable name was not modified
 assertEquals("test", globalVariableNode.getString());
 }

 @Test
 public void testMakeDeclaredNamesUnique_VariableDeclarationInFunction() {
 Compiler compiler = new Compiler();
 Node rootNode = new Node(Token.PROGRAM, null);

 // Create a function declaration with variable declaration
 Node functionNode = new Node(Token.FUNCTION, null);
 Node functionNameNode = new Node(Token.NAME, null, "testFunction");
 functionNode.setString(functionNameNode.getString());
 Node parametersNode = new Node(Token.BLOCK, null);
 Node argNode = new Node(Token.NAME, null, "arg");
 parametersNode.setFirstChild(argNode);
 parametersNode.setLastChild(argNode);
 functionNode.setFirstChild(parametersNode);
 Node functionBody = new Node(Token.BLOCK, null);
 Node varNode = new Node(Token.VAR, null, new Node(Token.NAME, null, "test"));
 varNode.setLastChild(new Node(Token.ASSIGN, null, null, null, null, null,
 new Node(Token.NUMBER, null, 5), null));
 functionBody.setFirstChild(varNode);
 functionNode.setLastChild(functionBody);

 // Add the function declaration to the root node
 rootNode.setFirstChild(functionNode);

 // Initialize the MakeDeclaredNamesUnique visitor
 MakeDeclaredNamesUnique visitor = new MakeDeclaredNamesUnique();
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
 traversal.addCallback(visitor);

 // Traverse the node tree
 traversal.traverse();

 // Verify that the variable name was modified
 assertEquals("test$0", varNode.getString());
 }

 @Test
 public void testMakeDeclaredNamesUnique_VariableDeclarationInFunctionWithSameName() {
 Compiler compiler = new Compiler();
 Node rootNode = new Node(Token.PROGRAM, null);

 // Create a function declaration with multiple variable declarations with the same name
 Node functionNode = new Node(Token.FUNCTION, null);
 Node functionNameNode = new Node(Token.NAME, null, "testFunction");
 functionNode.setString(functionNameNode.getString());
 Node parametersNode = new Node(Token.BLOCK, null);
 Node argNode = new Node(Token.NAME, null, "arg");
 parametersNode.setFirstChild(argNode);
 parametersNode.setLastChild(argNode);
 functionNode.setFirstChild(parametersNode);
 Node functionBody = new Node(Token.BLOCK, null);
 Node var1Node = new Node(Token.VAR, null, new Node(Token.NAME, null, "test"));
 var1Node.setLastChild(new Node(Token.ASSIGN, null, null, null, null, null,
 new Node(Token.NUMBER, null, 1), null));
 Node var2Node = new Node(Token.VAR, null, new Node(Token.NAME, null, "test"));
 var2Node.setLastChild(new Node(Token.ASSIGN, null, null, null, null, null,
 new Node(Token.NUMBER, null, 2), null));
 functionBody.setFirstChild(var1Node);
 functionBody.setLastChild(var2Node);
 functionNode.setLastChild(functionBody);

 // Add the function declaration to the root node
 rootNode.setFirstChild(functionNode);

 // Initialize the MakeDeclaredNamesUnique visitor
 MakeDeclaredNamesUnique visitor = new MakeDeclaredNamesUnique();
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
 traversal.addCallback(visitor);

 // Traverse the node tree
 traversal.traverse();

 // Verify that the variable names were modified
 assertEquals("test$0", var1Node.getString());
 assertEquals("test$1", var2Node.getString());
 }

}