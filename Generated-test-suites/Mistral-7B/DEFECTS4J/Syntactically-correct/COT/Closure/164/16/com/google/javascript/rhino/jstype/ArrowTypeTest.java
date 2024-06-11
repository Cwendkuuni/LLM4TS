import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter();
 }

 @Test
 public void testArrowTypeConstructorWithParametersAndReturnType() {
 Node parametersNode = new Node("(number, string)");
 ArrowType arrowType = new ArrowType(registry, parametersNode,
 registry.createNamedType("Number"), false);
 assertEquals("ArrowType[Number, String]", arrowType.toString());
 }

 @Test
 public void testArrowTypeConstructorWithNoParametersAndReturnType() {
 ArrowType arrowType = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 assertEquals("ArrowType[Number]", arrowType.toString());
 }

 @Test
 public void testArrowTypeConstructorWithParametersAndReturnTypeInferred() {
 Node parametersNode = new Node("(number, string)");
 ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);
 assertEquals("ArrowType[?, ?]", arrowType.toString());
 }

 @Test
 public void testArrowTypeConstructorWithNoParametersAndReturnTypeInferred() {
 ArrowType arrowType = new ArrowType(registry, null, null, true);
 assertEquals("ArrowType[?, ?]", arrowType.toString());
 }

 @Test
 public void testIsSubtypeWithSameArrowType() {
 ArrowType arrowType1 = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 ArrowType arrowType2 = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 assertTrue(arrowType1.isSubtype(arrowType2));
 assertTrue(arrowType2.isSubtype(arrowType1));
 }

 @Test
 public void testIsSubtypeWithArrowTypeAndFunctionType() {
 ArrowType arrowType = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 JSType functionType = registry.createFunctionType(
 registry.createNamedType("Number"), registry.createNamedType("String"));
 assertFalse(arrowType.isSubtype(functionType));
 }

 @Test
 public void testIsSubtypeWithArrowTypeAndDifferentReturnType() {
 ArrowType arrowType1 = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 ArrowType arrowType2 = new ArrowType(registry, null,
 registry.createNamedType("String"), false);
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testHasEqualParametersWithSameArrowType() {
 ArrowType arrowType1 = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 ArrowType arrowType2 = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testHasEqualParametersWithDifferentArrowType() {
 ArrowType arrowType1 = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 ArrowType arrowType2 = new ArrowType(registry, null,
 registry.createNamedType("String"), false);
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testIsEquivalentToWithSameArrowType() {
 ArrowType arrowType1 = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 ArrowType arrowType2 = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 assertTrue(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testIsEquivalentToWithDifferentArrowType() {
 ArrowType arrowType1 = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 ArrowType arrowType2 = new ArrowType(registry, null,
 registry.createNamedType("String"), false);
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testGetLeastSupertypeUnsupportedOperationException() {
 ArrowType arrowType = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 try {
 arrowType.getLeastSupertype(new ArrowType(registry, null,
 registry.createNamedType("String"), false));
 fail("UnsupportedOperationException expected");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testGetGreatestSubtypeUnsupportedOperationException() {
 ArrowType arrowType = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 try {
 arrowType.getGreatestSubtype(new ArrowType(registry, null,
 registry.createNamedType("Number"), true));
 fail("UnsupportedOperationException expected");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testVisit() {
 ArrowType arrowType = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 ArrowType arrowTypeVisitorSpy = new SpyArrowTypeVisitor();
 arrowType.visit(arrowTypeVisitorSpy);
 assertTrue(arrowTypeVisitorSpy.wasVisited);
 }

 @Test
 public void testToStringHelper() {
 ArrowType arrowType = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 String result = arrowType.toStringHelper(false);
 assertEquals("ArrowType[Number]", result);
 }

 @Test
 public void testHasUnknownParamsOrReturnTrueWithUnknownType() {
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 assertTrue(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testHasUnknownParamsOrReturnTrueWithNull() {
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 assertTrue(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testHasUnknownParamsOrReturnFalseWithKnownTypes() {
 ArrowType arrowType = new ArrowType(registry, null,
 registry.createNamedType("Number"), false);
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }
}

 class SpyArrowTypeVisitor extends ArrowType {
 boolean wasVisited = false;

 @Override
 public TernaryValue testForEquality(JSType that) {
 wasVisited = true;
 return super.testForEquality(that);
 }
 }