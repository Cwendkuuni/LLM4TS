import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {

 private Compiler compiler;
 private RuntimeTypeCheck runtimeTypeCheck;

 @Before
 public void setUp() {
 compiler = new Compiler();
 runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
 }

 @Test
 public void testCheckTypeForStringParameter() {
 Node rootNode = NodeUtil.parseCode("function foo(s) {}");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node stringChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.valueChecker", -1, -1);
 Node stringValue = NodeUtil.newString("hello");
 Node checkTypeCall = new Node(Token.CALL, stringChecker, stringValue);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.getLastChild().replaceChild(modifiedFooFunction.getLastChild().getFirstChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForNumberParameter() {
 Node rootNode = NodeUtil.parseCode("function foo(n) {}");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node numberChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.valueChecker", -1, -1);
 Node numberValue = new Node(Token.NUMBER, 42);
 Node checkTypeCall = new Node(Token.CALL, numberChecker, numberValue);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.getLastChild().replaceChild(modifiedFooFunction.getLastChild().getFirstChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForBooleanParameter() {
 Node rootNode = NodeUtil.parseCode("function foo(b) {}");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node booleanChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.valueChecker", -1, -1);
 Node booleanValue = new Node(Token.BOOLEAN, true);
 Node checkTypeCall = new Node(Token.CALL, booleanChecker, booleanValue);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.getLastChild().replaceChild(modifiedFooFunction.getLastChild().getFirstChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForNullParameter() {
 Node rootNode = NodeUtil.parseCode("function foo(n) {}");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node nullChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.nullChecker", -1, -1);
 Node nullValue = Node.newNull();
 Node checkTypeCall = new Node(Token.CALL, nullChecker, nullValue);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.getLastChild().replaceChild(modifiedFooFunction.getLastChild().getFirstChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForClassParameter() {
 String className = "MyClass";
 Node rootNode = NodeUtil.parseCode("function foo(o) {}");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node classChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.classChecker", -1, -1);
 Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);
 Node checkTypeCall = new Node(Token.CALL, classChecker, classNode);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.getLastChild().replaceChild(modifiedFooFunction.getLastChild().getFirstChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForInterfaceParameter() {
 String interfaceName = "MyInterface";
 Node rootNode = NodeUtil.parseCode("function foo(o) {}");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node interfaceChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.interfaceChecker", -1, -1);
 Node classNode = NodeUtil.newQualifiedNameNode(interfaceName, -1, -1);
 Node checkTypeCall = new Node(Token.CALL, interfaceChecker, classNode);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.getLastChild().replaceChild(modifiedFooFunction.getLastChild().getFirstChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForMismatchedType() {
 Node rootNode = NodeUtil.parseCode("function foo(s) { return 42; }");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node stringChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.valueChecker", -1, -1);
 Node stringValue = NodeUtil.newString("hello");
 Node checkTypeCall = new Node(Token.CALL, stringChecker, stringValue);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.getLastChild().replaceChild(modifiedFooFunction.getLastChild().getFirstChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertFalse(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForUnionTypeParameter() {
 Node rootNode = NodeUtil.parseCode("function foo(n) {}");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node unionTypeChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.unionTypeChecker", -1, -1);
 Node numberValue = new Node(Token.NUMBER, 42);
 Node stringValue = NodeUtil.newString("hello");
 Node checkTypeCall1 = new Node(Token.CALL, unionTypeChecker, numberValue);
 Node checkTypeCall2 = new Node(Token.CALL, unionTypeChecker, stringValue);
 Node arrayNode = new Node(Token.ARRAYLIT);
 arrayNode.addChildToBack(checkTypeCall1);
 arrayNode.addChildToBack(checkTypeCall2);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.getLastChild().replaceChild(modifiedFooFunction.getLastChild().getFirstChild(), arrayNode);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForNullReturnValue() {
 Node rootNode = NodeUtil.parseCode("function foo() { return null; }");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node nullChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.nullChecker", -1, -1);
 Node checkTypeCall = new Node(Token.CALL, nullChecker, Node.newNull());
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.replaceChild(modifiedFooFunction.getLastChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForNumberReturnValue() {
 Node rootNode = NodeUtil.parseCode("function foo() { return 42; }");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node numberChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.valueChecker", -1, -1);
 Node numberValue = new Node(Token.NUMBER, 42);
 Node checkTypeCall = new Node(Token.CALL, numberChecker, numberValue);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.replaceChild(modifiedFooFunction.getLastChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForStringReturnValue() {
 Node rootNode = NodeUtil.parseCode("function foo() { return 'hello'; }");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node stringChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.valueChecker", -1, -1);
 Node stringValue = NodeUtil.newString("hello");
 Node checkTypeCall = new Node(Token.CALL, stringChecker, stringValue);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.replaceChild(modifiedFooFunction.getLastChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }

 @Test
 public void testCheckTypeForBooleanReturnValue() {
 Node rootNode = NodeUtil.parseCode("function foo() { return true; }");
 runtimeTypeCheck.process(null, rootNode);
 Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 Node booleanChecker = NodeUtil.newQualifiedNameNode("jscomp.typecheck.valueChecker", -1, -1);
 Node booleanValue = new Node(Token.BOOLEAN, true);
 Node checkTypeCall = new Node(Token.CALL, booleanChecker, booleanValue);
 Node modifiedFooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
 modifiedFooFunction.replaceChild(modifiedFooFunction.getLastChild(), checkTypeCall);
 Node result = (Node) compiler.compile(rootNode);
 assertTrue(result.getStatus().getCode() == 0);
 }
}