import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

 private static JSTypeRegistry registry;
 private static ErrorReporter errorReporter;
 private static StaticScope<JSType> scope;

 @BeforeClass
 public static void setup() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter();
 scope = new StaticScope<>(registry);
 }

 @Test
 public void testArrowTypeConstructor_WithParametersAndReturnType() {
 ArrowType arrowType = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 assertEquals("ArrowType[int]", arrowType.toString());
 }

 @Test
 public void testArrowTypeConstructor_WithOnlyReturnType() {
 ArrowType arrowType = new ArrowType(registry, null,
 new IntegerType(registry, null), false);
 assertEquals("ArrowType<int>", arrowType.toString());
 }

 @Test
 public void testArrowTypeConstructor_WithParametersAndInferredReturnType() {
 ArrowType arrowType = new ArrowType(registry, new Node(), null, true);
 assertEquals("ArrowType[?]", arrowType.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeConstructor_WithNullParameters() {
 new ArrowType(registry, null, new IntegerType(registry, null), false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeConstructor_WithNullReturnType() {
 new ArrowType(registry, new Node(), null, false);
 }

 @Test
 public void testIsSubtype_SameType() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testIsSubtype_SuperType() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new NumberType(registry), false);
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testIsSubtype_SubType() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new NumberType(registry), false);
 assertFalse(arrowType2.isSubtype(arrowType1));
 }

 @Test
 public void testIsSubtype_DifferentParameters() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new StringType(registry), false);
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testIsSubtype_WithVarArgs() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new ArrayType(registry, new IntegerType(registry), true), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new ArrayType(registry, new StringType(registry), true), false);
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testHasEqualParameters_SameType() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testHasEqualParameters_DifferentTypes() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new StringType(registry), false);
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testIsEquivalentTo_SameType() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 assertTrue(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testIsEquivalentTo_DifferentTypes() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new StringType(registry), false);
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testHashCode_SameType() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
 }

 @Test
 public void testHashCode_DifferentTypes() {
 ArrowType arrowType1 = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 ArrowType arrowType2 = new ArrowType(registry, new Node(),
 new StringType(registry), false);
 assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
 }

 @Test
 public void testHasUnknownParamsOrReturn_WithUnknownParams() {
 ArrowType arrowType = new ArrowType(registry, new Node(), null, false);
 arrowType.parameters = new Node();
 arrowType.parameters.setJSType(new JSTypeNative(registry, UNKNOWN_TYPE));
 assertTrue(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testHasUnknownParamsOrReturn_WithUnknownReturn() {
 ArrowType arrowType = new ArrowType(registry, new Node(), null, false);
 arrowType.returnType = new JSTypeNative(registry, UNKNOWN_TYPE);
 assertTrue(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testHasUnknownParamsOrReturn_WithoutUnknowns() {
 ArrowType arrowType = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testToString() {
 ArrowType arrowType = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 assertEquals("ArrowType[int]", arrowType.toString());
 }

 @Test
 public void testToString_WithInferredReturnType() {
 ArrowType arrowType = new ArrowType(registry, new Node(), null, true);
 assertEquals("ArrowType[?]", arrowType.toString());
 }

 @Test
 public void testToString_WithVarArgs() {
 ArrowType arrowType = new ArrowType(registry, new Node(),
 new ArrayType(registry, new IntegerType(registry), true), false);
 assertEquals("ArrowType[int...]", arrowType.toString());
 }

 @Test
 public void testResolveInternal_WithUnknownReturnType() {
 ArrowType arrowType = new ArrowType(registry, new Node(), null, false);
 arrowType.resolveInternal(errorReporter, scope);
 assertEquals(UNKNOWN_TYPE, arrowType.returnType.getType());
 }

 @Test
 public void testResolveInternal_WithKnownReturnType() {
 ArrowType arrowType = new ArrowType(registry, new Node(),
 new IntegerType(registry, null), false);
 arrowType.resolveInternal(errorReporter, scope);
 assertEquals(IntegerType.class, arrowType.returnType.getClass());
 }

 @Test
 public void testResolveInternal_WithUnknownParameters() {
 ArrowType arrowType = new ArrowType(registry, new Node(), null, false);
 arrowType.parameters = new Node();
 arrowType.parameters.setJSType(new JSTypeNative(registry, UNKNOWN_TYPE));
 arrowType.resolveInternal(errorReporter, scope);
 assertEquals(ArrayType.class, arrowType.parameters.getType().getClass());
 assertEquals(JSTypeNative.class, arrowType.parameters.getType().getTypeClass());
 assertEquals(UNKNOWN_TYPE, arrowType.parameters.getType().getType());
 }

 @Test
 public void testResolveInternal_WithKnownParameters() {
 ArrowType arrowType = new ArrowType(registry, new Node(), null, false);
 arrowType.parameters = new Node();
 arrowType.parameters.setJSType(new IntegerType(registry, null));
 arrowType.resolveInternal(errorReporter, scope);
 assertEquals(ArrayType.class, arrowType.parameters.getType().getClass());
 assertEquals(IntegerType.class, arrowType.parameters.getType().getTypeClass());
 }

 @Test
 public void testResolveInternal_WithVarArgs() {
 ArrowType arrowType = new ArrowType(registry, new Node(), null, false);
 arrowType.parameters = new Node();
 arrowType.parameters.setJSType(new ArrayType(registry, new IntegerType(registry), true));
 arrowType.resolveInternal(errorReporter, scope);
 assertEquals(JSType.class, arrowType.parameters.getType().getClass());
 assertEquals(ArrayType.class, arrowType.parameters.getType().getTypeClass());
 }
}