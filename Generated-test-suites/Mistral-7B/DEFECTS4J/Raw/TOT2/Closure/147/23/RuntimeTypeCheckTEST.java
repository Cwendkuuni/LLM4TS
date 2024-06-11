import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

 private Compiler compiler;
 private RuntimeTypeCheck runtimeTypeCheck;

 @Before
 public void setUp() {
 compiler = new Compiler(null);
 runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
 }

 @Test
 public void testCheckTypeForBasicTypes() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for basic types
 runtimeTypeCheck.process(externs, root);

 Node strLiteral = Node.newString("test");
 Node numLiteral = Node.newNumber(10);
 Node boolLiteral = Node.newBoolean(true);
 Node nullLiteral = Node.nullLiteral();

 Node stringChecker = Node.newString("valueChecker('string')");
 Node numberChecker = Node.newString("valueChecker('number')");
 Node booleanChecker = Node.newString("valueChecker('boolean')");
 Node nullChecker = Node.nullLiteral();

 Node checkTypeCall = jsCode("checkType")
 .call(
 Node.newString("test"),
 Node.newArray(stringChecker)
 );
 assertEquals(strLiteral, checkTypeCall.getFirstChild());

 checkTypeCall = jsCode("checkType")
 .call(
 Node.newNumber(10),
 Node.newArray(numberChecker)
 );
 assertEquals(numLiteral, checkTypeCall.getFirstChild());

 checkTypeCall = jsCode("checkType")
 .call(
 Node.newBoolean(true),
 Node.newArray(booleanChecker)
 );
 assertEquals(boolLiteral, checkTypeCall.getFirstChild());

 checkTypeCall = jsCode("checkType")
 .call(
 nullLiteral,
 Node.newArray(nullChecker)
 );
 assertNull(checkTypeCall.getFirstChild());
 }

 @Test
 public void testCheckTypeForValueTypes() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for value types
 runtimeTypeCheck.process(externs, root);

 Node stringValue = Node.newString("test");
 Node numberValue = Node.newNumber(10);
 Node booleanValue = Node.newBoolean(true);
 Node nullValue = Node.nullLiteral();
 Node undefinedValue = Node.undefined();
 Node symbolValue = Node.newSymbol("test");

 Node stringChecker = Node.newString("valueChecker('string')");
 Node numberChecker = Node.newString("valueChecker('number')");
 Node booleanChecker = Node.newString("valueChecker('boolean')");
 Node nullChecker = Node.nullLiteral();
 Node undefinedChecker = Node.newString("valueChecker('undefined')");
 Node symbolChecker = Node.newString("valueChecker('Symbol')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 stringValue,
 Node.newArray(stringChecker)
 );
 assertEquals(stringValue, checkTypeCall.getFirstChild());

 checkTypeCall = jsCode("checkType")
 .call(
 numberValue,
 Node.newArray(numberChecker)
 );
 assertEquals(numberValue, checkTypeCall.getFirstChild());

 checkTypeCall = jsCode("checkType")
 .call(
 booleanValue,
 Node.newArray(booleanChecker)
 );
 assertEquals(booleanValue, checkTypeCall.getFirstChild());

 checkTypeCall = jsCode("checkType")
 .call(
 nullValue,
 Node.newArray(nullChecker)
 );
 assertNull(checkTypeCall.getFirstChild());

 checkTypeCall = jsCode("checkType")
 .call(
 undefinedValue,
 Node.newArray(undefinedChecker)
 );
 assertNull(checkTypeCall.getFirstChild());

 checkTypeCall = jsCode("checkType")
 .call(
 symbolValue,
 Node.newArray(symbolChecker)
 );
 assertEquals(symbolValue, checkTypeCall.getFirstChild());
 }

 @Test
 public void testCheckTypeForClassTypes() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for class types
 runtimeTypeCheck.process(externs, root);

 String className = "MyClass";
 Node classConstructor = NodeUtil.newQualifiedNameNode(className, -1, -1);
 Node classInstance = new Node(Token.NEW, classConstructor);

 Node classChecker = Node.newString("classChecker('" + className + "')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 classInstance,
 Node.newArray(classChecker)
 );
 assertEquals(classInstance, checkTypeCall.getFirstChild());
 }

 @Test
 public void testCheckTypeForInterfaceTypes() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for interface types
 runtimeTypeCheck.process(externs, root);

 String interfaceName = "MyInterface";
 Node interfaceConstructor = NodeUtil.newQualifiedNameNode(interfaceName, -1, -1);
 Node interfaceInstance = new Node(Token.NEW, interfaceConstructor);

 Node interfaceChecker = Node.newString("interfaceChecker('" + interfaceName + "')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 interfaceInstance,
 Node.newArray(interfaceChecker)
 );
 assertEquals(interfaceInstance, checkTypeCall.getFirstChild());
 }

 @Test
 public void testCheckTypeForNullAndUndefined() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for null and undefined
 runtimeTypeCheck.process(externs, root);

 Node nullValue = Node.nullLiteral();
 Node undefinedValue = Node.undefined();
 Node nullChecker = Node.nullLiteral();
 Node undefinedChecker = Node.newString("valueChecker('undefined')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 nullValue,
 Node.newArray(nullChecker)
 );
 assertNull(checkTypeCall.getFirstChild());

 checkTypeCall = jsCode("checkType")
 .call(
 undefinedValue,
 Node.newArray(undefinedChecker)
 );
 assertNull(checkTypeCall.getFirstChild());
 }

 @Test
 public void testCheckTypeForBooleanAndNumberOperators() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for boolean and number operators
 runtimeTypeCheck.process(externs, root);

 Node boolValue1 = Node.newBoolean(true);
 Node boolValue2 = Node.newBoolean(false);
 Node numValue1 = Node.newNumber(1);
 Node numValue2 = Node.newNumber(2);
 Node boolChecker = Node.newString("valueChecker('boolean')");
 Node numChecker = Node.newString("valueChecker('number')");

 Node checkTypeCall1 = jsCode("checkType")
 .call(
 Node.newBinaryOp(Token.LOGICAL_OR, boolValue1, boolValue2),
 Node.newArray(boolChecker, boolChecker)
 );
 assertEquals(Node.newBoolean(true), checkTypeCall1.getFirstChild());

 checkTypeCall1 = jsCode("checkType")
 .call(
 Node.newBinaryOp(Token.LOGICAL_AND, boolValue1, boolValue2),
 Node.newArray(boolChecker, boolChecker)
 );
 assertEquals(Node.newBoolean(false), checkTypeCall1.getFirstChild());

 checkTypeCall1 = jsCode("checkType")
 .call(
 Node.newBinaryOp(Token.ADD, numValue1, numValue2),
 Node.newArray(numChecker, numChecker)
 );
 assertEquals(Node.newNumber(3), checkTypeCall1.getFirstChild());
 }

 @Test
 public void testCheckTypeForFunctionCall() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for function call
 runtimeTypeCheck.process(externs, root);

 Node func = NodeUtil.newQualifiedNameNode("Math", -1, -1)
 .call(Node.newString("sin"));
 Node funcChecker = Node.newString("functionChecker(Math.sin)");

 Node checkTypeCall = jsCode("checkType")
 .call(
 func,
 Node.newArray(funcChecker)
 );
 assertEquals(func, checkTypeCall.getFirstChild());
 }

 @Test(expected = AssertionError.class)
 public void testCheckTypeForIncorrectType() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for incorrect type
 runtimeTypeCheck.process(externs, root);

 Node numberValue = Node.newNumber(10);
 Node stringChecker = Node.newString("valueChecker('string')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 numberValue,
 Node.newArray(stringChecker)
 );
 fail("checkType should throw an exception");
 }

 @Test(expected = AssertionError.class)
 public void testCheckTypeForFunctionType() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for function type
 runtimeTypeCheck.process(externs, root);

 Node functionType = Node.newObjectType(
 NodeUtil.newQualifiedNameNode("Function", -1, -1));
 Node functionValue = new Node(Token.FUNCTION, functionType);
 Node functionChecker = Node.newString("functionChecker(Function)");

 Node checkTypeCall = jsCode("checkType")
 .call(
 functionValue,
 Node.newArray(functionChecker)
 );
 fail("checkType should throw an exception");
 }

 @Test(expected = AssertionError.class)
 public void testCheckTypeForClassTypeWithNoConstructor() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for class type with no constructor
 runtimeTypeCheck.process(externs, root);

 String className = "MyClassNoConstructor";
 Node classType = Node.newObjectType(
 NodeUtil.newQualifiedNameNode(className, -1, -1));
 Node classInstance = new Node(Token.NEW, classType);
 Node classChecker = Node.newString("classChecker('" + className + "')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 classInstance,
 Node.newArray(classChecker)
 );
 fail("checkType should throw an exception");
 }

 @Test(expected = AssertionError.class)
 public void testCheckTypeForInterfaceTypeWithNoConstructor() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for interface type with no constructor
 runtimeTypeCheck.process(externs, root);

 String interfaceName = "MyInterfaceNoConstructor";
 Node interfaceType = Node.newObjectType(
 NodeUtil.newQualifiedNameNode(interfaceName, -1, -1));
 Node interfaceInstance = new Node(Token.NEW, interfaceType);
 Node interfaceChecker = Node.newString("interfaceChecker('" + interfaceName + "')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 interfaceInstance,
 Node.newArray(interfaceChecker)
 );
 fail("checkType should throw an exception");
 }

 @Test
 public void testCheckTypeForArray() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for array
 runtimeTypeCheck.process(externs, root);

 Node arrayValue = Node.newArray(Node.newNumber(1));
 Node arrayChecker = Node.newString("valueChecker('number[]')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 arrayValue,
 Node.newArray(arrayChecker)
 );
 assertEquals(arrayValue, checkTypeCall.getFirstChild());
 }

 @Test(expected = AssertionError.class)
 public void testCheckTypeForArrayWithMismatchedType() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for array with mismatched type
 runtimeTypeCheck.process(externs, root);

 Node arrayValue = Node.newArray(Node.newNumber(1));
 Node arrayChecker = Node.newString("valueChecker('boolean[]')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 arrayValue,
 Node.newArray(arrayChecker)
 );
 fail("checkType should throw an exception");
 }

 @Test
 public void testCheckTypeForMap() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for Map
 runtimeTypeCheck.process(externs, root);

 Node mapValue = new Node(Token.NEW, NodeUtil.newQualifiedNameNode("Map", -1, -1));
 Node mapChecker = Node.newString("valueChecker('Map<string, number>')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 mapValue,
 Node.newArray(mapChecker)
 );
 assertEquals(mapValue, checkTypeCall.getFirstChild());
 }

 @Test(expected = AssertionError.class)
 public void testCheckTypeForMapWithMismatchedKeyType() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for Map with mismatched key type
 runtimeTypeCheck.process(externs, root);

 Node mapValue = new Node(Token.NEW, NodeUtil.newQualifiedNameNode("Map", -1, -1));
 Node mapChecker = Node.newString("valueChecker('Map<boolean, number>')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 mapValue,
 Node.newArray(mapChecker)
 );
 fail("checkType should throw an exception");
 }

 @Test(expected = AssertionError.class)
 public void testCheckTypeForMapWithMismatchedValueType() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for Map with mismatched value type
 runtimeTypeCheck.process(externs, root);

 Node mapValue = new Node(Token.NEW, NodeUtil.newQualifiedNameNode("Map", -1, -1));
 Node mapChecker = Node.newString("valueChecker('Map<string, boolean>')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 mapValue,
 Node.newArray(mapChecker)
 );
 fail("checkType should throw an exception");
 }

 @Test
 public void testCheckTypeForSet() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for Set
 runtimeTypeCheck.process(externs, root);

 Node setValue = new Node(Token.NEW, NodeUtil.newQualifiedNameNode("Set", -1, -1));
 Node setChecker = Node.newString("valueChecker('Set<number>')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 setValue,
 Node.newArray(setChecker)
 );
 assertEquals(setValue, checkTypeCall.getFirstChild());
 }

 @Test(expected = AssertionError.class)
 public void testCheckTypeForSetWithMismatchedType() {
 Node externs = Node.object();
 Node root = Node.object();

 // Test checkType for Set with mismatched type
 runtimeTypeCheck.process(externs, root);

 Node setValue = new Node(Token.NEW, NodeUtil.newQualifiedNameNode("Set", -1, -1));
 Node setChecker = Node.newString("valueChecker('Set<boolean>')");

 Node checkTypeCall = jsCode("checkType")
 .call(
 setValue,
 Node.newArray(setChecker)
 );
 fail("checkType should throw an exception");
 }
}