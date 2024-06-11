import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrowTypeTest {

 private static ErrorReporter tr;
 private static JSTypeRegistry registry;

 @BeforeClass
 public static void setup() {
 tr = new ErrorReporter();
 registry = new JSTypeRegistry(tr);
 }

 @Test
 public void testArrowTypeWithNoParametersAndNoReturnType() {
 // Arrow type with no parameters and no return type
 ArrowType arrowType1 = new ArrowType(registry, null, null);

 // Test equality with itself
 assertEquals(arrowType1, arrowType1);

 // Test inequality with different arrow types
 ArrowType arrowType2 = new ArrowType(registry, null, new FunctionType(registry, null, new JSType[]{}, null, null, null, null, null, false), false);
 assertNotEquals(arrowType1, arrowType2);

 // Test inequality with a different type
 JSType intType = registry.createNumberType(registry.getIntType());
 assertNotEquals(arrowType1, intType);
 }

 @Test
 public void testArrowTypeWithParametersAndNoReturnType() {
 // Arrow type with parameters and no return type
 Node node = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param2", registry.createIdentifier("y"))));
 ArrowType arrowType1 = new ArrowType(registry, node, null);

 // Test equality with itself
 assertEquals(arrowType1, arrowType1);

 // Test inequality with a different arrow type with different parameters
 Node node2 = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param3", registry.createIdentifier("z"))));
 ArrowType arrowType2 = new ArrowType(registry, node2, null);
 assertNotEquals(arrowType1, arrowType2);

 // Test inequality with a different type
 JSType intType = registry.createNumberType(registry.getIntType());
 assertNotEquals(arrowType1, intType);
 }

 @Test
 public void testArrowTypeWithParametersAndInferredReturnType() {
 // Arrow type with parameters and inferred return type
 Node node = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param2", registry.createIdentifier("y"))));
 ArrowType arrowType1 = new ArrowType(registry, node, null, true);

 // Test equality with itself
 assertEquals(arrowType1, arrowType1);

 // Test inequality with a different arrow type with different parameters
 Node node2 = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param3", registry.createIdentifier("z"))));
 ArrowType arrowType2 = new ArrowType(registry, node2, null, true);
 assertNotEquals(arrowType1, arrowType2);

 // Test inequality with a different type
 JSType intType = registry.createNumberType(registry.getIntType());
 assertNotEquals(arrowType1, intType);
 }

 @Test
 public void testArrowTypeWithParametersAndExplicitReturnType() {
 // Arrow type with parameters and explicit return type
 Node node = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param2", registry.createIdentifier("y"))));
 ArrowType arrowType1 = new ArrowType(registry, node, registry.createNumberType(registry.getNativeType(JSTypeNative.INT)), false);

 // Test equality with itself
 assertEquals(arrowType1, arrowType1);

 // Test inequality with a different arrow type with different parameters
 Node node2 = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param3", registry.createIdentifier("z"))));
 ArrowType arrowType2 = new ArrowType(registry, node2, registry.createNumberType(registry.getNativeType(JSTypeNative.INT)), false);
 assertNotEquals(arrowType1, arrowType2);

 // Test inequality with a different type
 JSType intType = registry.createNumberType(registry.getIntType());
 assertNotEquals(arrowType1, intType);

 // Test subtyping: arrow type with explicit int return type is a subtype of arrow type with inferred int return type
 ArrowType arrowType3 = new ArrowType(registry, node, registry.getNativeType(JSTypeNative.INT), true);
 assertTrue(arrowType1.isSubtype(arrowType3));
 }

 @Test
 public void testArrowTypeWithParametersAndExplicitVoidReturnType() {
 // Arrow type with parameters and explicit void return type
 Node node = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param2", registry.createIdentifier("y"))));
 ArrowType arrowType1 = new ArrowType(registry, node, registry.createVoidType(), false);

 // Test equality with itself
 assertEquals(arrowType1, arrowType1);

 // Test inequality with a different arrow type with different parameters
 Node node2 = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param3", registry.createIdentifier("z"))));
 ArrowType arrowType2 = new ArrowType(registry, node2, registry.createVoidType(), false);
 assertNotEquals(arrowType1, arrowType2);

 // Test inequality with a different type
 JSType intType = registry.createNumberType(registry.getIntType());
 assertNotEquals(arrowType1, intType);

 // Test subtyping: arrow type with explicit void return type is a subtype of arrow type with inferred void return type
 ArrowType arrowType3 = new ArrowType(registry, node, registry.createVoidType(), true);
 assertTrue(arrowType1.isSubtype(arrowType3));
 }

 @Test
 public void testArrowTypeWithParametersAndExplicitFunctionReturnType() {
 // Arrow type with parameters and explicit function return type
 Node node = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param2", registry.createIdentifier("y"))));
 FunctionType funcType = new FunctionType(registry, null, new JSType[]{registry.createIdentifierType("int")}, null, null, null, null, null, false);
 ArrowType arrowType1 = new ArrowType(registry, node, funcType, false);

 // Test equality with itself
 assertEquals(arrowType1, arrowType1);

 // Test inequality with a different arrow type with different parameters
 Node node2 = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param3", registry.createIdentifier("z"))));
 FunctionType funcType2 = new FunctionType(registry, null, new JSType[]{registry.createIdentifierType("int")}, null, null, null, null, null, false);
 ArrowType arrowType2 = new ArrowType(registry, node2, funcType2, false);
 assertNotEquals(arrowType1, arrowType2);

 // Test inequality with a different type
 JSType intType = registry.createNumberType(registry.getIntType());
 assertNotEquals(arrowType1, intType);

 // Test subtyping: arrow type with explicit function return type is a subtype of arrow type with inferred function return type
 ArrowType arrowType3 = new ArrowType(registry, node, null, true);
 FunctionType funcType3 = new FunctionType(registry, null, new JSType[]{registry.createIdentifierType("int")}, null, null, null, null, null, false);
 assertTrue(arrowType1.isSubtype(new ArrowType(registry, node, funcType3, true)));
 }

 @Test
 public void testArrowTypeWithParametersAndExplicitUnknownReturnType() {
 // Arrow type with parameters and explicit unknown return type
 Node node = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param2", registry.createIdentifier("y"))));
 ArrowType arrowType1 = new ArrowType(registry, node, null, true);

 // Test equality with itself
 assertEquals(arrowType1, arrowType1);

 // Test inequality with a different arrow type with different parameters
 Node node2 = registry.createList(
 new Node("parameters", new Node("param1", registry.createIdentifier("x")),
 new Node("param3", registry.createIdentifier("z"))));
 ArrowType arrowType2 = new ArrowType(registry, node2, null, true);
 assertNotEquals(arrowType1, arrowType2);

 // Test inequality with a different type
 JSType intType = registry.createNumberType(registry.getIntType());
 assertNotEquals(arrowType1, intType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeWithNullParameters() {
 // Arrow type with null parameters should throw an IllegalArgumentException
 ArrowType arrowType = new ArrowType(registry, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeWithNullReturnType() {
 // Arrow type with null return type should throw an IllegalArgumentException
 ArrowType arrowType = new ArrowType(registry, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeWithNullRegistry() {
 // ArrowType constructor with null registry should throw an IllegalArgumentException
 new ArrowType(null, null, null);
 }
}