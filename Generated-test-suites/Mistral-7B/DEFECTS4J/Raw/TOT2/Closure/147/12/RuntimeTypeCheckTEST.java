import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {

 private AbstractCompiler compiler;
 private Scope globalScope;

 @Before
 public void setUp() {
 compiler = new Compiler();
 globalScope = compiler.getGlobalScope();
 }

 /**
 * Test case for adding markers for user-defined interfaces and classes.
 */
 @Test
 public void testAddMarkers() {
 // Create a sample class
 String className = "MyClass";
 Node classNode = Node.newClass(className, null, null);
 Node constructor = Node.newFunction(className, new Node[0], null);
 constructor.setJSType(FunctionType.getInstance(constructor));
 classNode.addChildToBack(constructor);

 Node script = Node.newBlock("myClassScript");
 script.addChildToBack(classNode);

 // Process the script with the RuntimeTypeCheck pass
 RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
 typeCheck.process(null, script);
 compiler.process();

 // Check that markers have been added
 Node prototype = globalScope.getVariable(className + ".prototype");
 assertEquals("instance_of__" + className, prototype.getChild(0).toString());

 // Test for inherited interfaces
 String interfaceName = "MyInterface";
 Node interfaceType = Node.newObjectType(interfaceName, null);
 constructor.addImplementedInterface(interfaceType);
 Node interfaceNode = Node.newClass(interfaceName, null, null);
 interfaceNode.setJSType(new ObjectType(interfaceName));
 classNode.addChildToBack(interfaceNode);

 typeCheck.process(null, script);
 compiler.process();

 Node prototypeInterface = prototype.getChild(1);
 assertEquals("implements__" + interfaceName, prototypeInterface.toString());
 }

 /**
 * Test case for adding runtime type checks for function parameters and return value.
 */
 @Test
 public void testAddChecks() {
 // Create a sample function
 String functionName = "myFunction";
 Node functionNode = Node.newFunction(functionName, new Node[0], null);
 Node body = Node.newBlock("myFunctionBody");
 body.addChildToBack(Node.newReturn(Node.newNumberLiteral(42)));
 functionNode.setBody(body);
 functionNode.setJSType(FunctionType.getInstance(functionNode));

 Node script = Node.newBlock("myScript");
 script.addChildToBack(functionNode);

 // Process the script with the RuntimeTypeCheck pass
 RuntimeTypeCheck typeCheck = new RuntimeTypeCheck(compiler, null);
 typeCheck.process(null, script);
 compiler.process();

 // Check that checks have been added for the function parameters
 Node parameters = functionNode.getFirstChild();
 Node checkNode;
 for (int i = 0; i < parameters.getNumChildren(); i++) {
 checkNode = findCheckTypeCallNode(parameters.getChild(i));
 assertNotNull(checkNode);
 }

 // Check that a check has been added for the return value
 Node returnNode = functionNode.getLastChild();
 checkNode = findCheckTypeCallNode(returnNode);
 assertNotNull(checkNode);
 }

 /**
 * Helper method to find the checkType call node for a given node.
 */
 private Node findCheckTypeCallNode(Node node) {
 for (int i = 0; i < node.getNumChildren(); i++) {
 if (node.getChild(i).getType() == Token.CALL) {
 Node callNode = node.getChild(i);
 if (callNode.getFirstChild().toString().equals("checkType")) {
 return callNode;
 }
 }
 node = node.getNext();
 }
 return null;
 }
}