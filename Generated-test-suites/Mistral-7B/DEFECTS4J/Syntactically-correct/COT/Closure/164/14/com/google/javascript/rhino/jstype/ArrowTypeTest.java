import static org.junit.Assert.*;
import static com.google.javascript.rhino.node.NodeFactory.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.node.ErrorReporter;

import org.junit.Test;

public class ArrowTypeTest {

 private static JSTypeRegistry registry;
 private static ErrorReporter t;

 @BeforeClass
 public static void setUp() {
 registry = new JSTypeRegistry();
 t = ErrorReporter.NULL;
 }

 @Test
 public void testArrowTypeConstructorWithNullParameters() {
 ArrowType arrowType = new ArrowType(registry, null, getNativeType(JSTypeNative.VOID));
 assertEquals(getNativeType(JSTypeNative.FUNCTION), arrowType.getGreatestSubtype(JSType.FUNCTION));
 }

 @Test
 public void testArrowTypeConstructorWithNullReturnType() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.VOID));
 assertEquals(getNativeType(JSTypeNative.FUNCTION), arrowType.getGreatestSubtype(JSType.FUNCTION));
 }

 @Test
 public void testArrowTypeConstructor() {
 ArrowType arrowType = new ArrowType(registry, createList("x", "y"), getNativeType(JSTypeNative.NUMBER));
 assertEquals(JSTypeFactory.createFunctionType(createList("x", "y"), getNativeType(JSTypeNative.NUMBER)), arrowType.getGreatestSubtype(JSType.FUNCTION));
 }

 @Test
 public void testArrowTypeConstructorWithInferredReturnType() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), null);
 assertEquals(getNativeType(JSTypeNative.FUNCTION), arrowType.getGreatestSubtype(JSType.FUNCTION));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSubtypeThrowsException() {
 ArrowType arrowType1 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, createList("y"), getNativeType(JSTypeNative.STRING));
 arrowType1.isSubtype(arrowType2);
 }

 @Test
 public void testHasEqualParameters() {
 ArrowType arrowType1 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testHasEqualParametersFalse() {
 ArrowType arrowType1 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, createList("y"), getNativeType(JSTypeNative.STRING));
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testHasEqualParametersWithVarArgs() {
 ArrowType arrowType1 = new ArrowType(registry, createList("x", "..."), getNativeType(JSTypeNative.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, createList("y", "..."), getNativeType(JSTypeNative.STRING));
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testIsEquivalentTo() {
 ArrowType arrowType1 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 assertTrue(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testIsEquivalentToFalse() {
 ArrowType arrowType1 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, createList("y"), getNativeType(JSTypeNative.STRING));
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testIsEquivalentToWithInferredReturnType() {
 ArrowType arrowType1 = new ArrowType(registry, createList("x"), null);
 ArrowType arrowType2 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testHashCode() {
 ArrowType arrowType1 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 ArrowType arrowType2 = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
 ArrowType arrowType3 = new ArrowType(registry, createList("y"), getNativeType(JSTypeNative.STRING));
 assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());
 }

 @Test
 public void testGetLeastSupertypeThrowsException() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 try {
 arrowType.getLeastSupertype(JSType.FUNCTION);
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testGetGreatestSubtype() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 JSType greatestSubtype = arrowType.getGreatestSubtype(JSType.FUNCTION);
 assertEquals(JSTypeFactory.createFunctionType(createList("x"), getNativeType(JSTypeNative.NUMBER)), greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeWithNullReturnType() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), null);
 JSType greatestSubtype = arrowType.getGreatestSubtype(JSType.FUNCTION);
 assertEquals(getNativeType(JSTypeNative.FUNCTION), greatestSubtype);
 }

 @Test
 public void testTestForEqualityThrowsException() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 try {
 arrowType.testForEquality(JSType.FUNCTION);
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testVisitThrowsException() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 try {
 arrowType.visit(new Visitor<Object>() {
 @Override
 public Object visitArrowType(ArrowType arrowType, Object arg) {
 fail();
 }
 });
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testGetPossibleToBooleanOutcomes() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
 }

 @Test
 public void testResolveInternal() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 arrowType.resolveInternal(t, registry.getEmptyScope());
 assertNotNull(arrowType.returnType);
 assertNotNull(arrowType.parameters.getFirstChild().getJSType());
 }

 @Test
 public void testHasUnknownParamsOrReturn() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), null);
 assertTrue(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testHasUnknownParamsOrReturnFalse() {
 ArrowType arrowType = new ArrowType(registry, createList("x"), getNativeType(JSTypeNative.NUMBER));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }
}