import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

public class ArrowTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter() {};
 }

 @Test
 public void testArrowTypeConstructor() {
 Node parameters = Node.newList();
 JSType returnType = registry.createStringType();
 ArrowType arrowType = new ArrowType(registry, parameters, returnType);
 assertEquals(ArrowType.class, arrowType.getClass());
 assertEquals(parameters, arrowType.getParameters());
 assertEquals(returnType, arrowType.getReturnType());
 assertFalse(arrowType.isReturnTypeInferred());
 }

 @Test
 public void testArrowTypeConstructorWithInferredReturnType() {
 Node parameters = Node.newList();
 ArrowType arrowType = new ArrowType(registry, parameters, null, true);
 assertEquals(ArrowType.class, arrowType.getClass());
 assertEquals(parameters, arrowType.getParameters());
 assertTrue(arrowType.isReturnTypeInferred());
 }

 @Test
 public void testIsSubtype() {
 ArrowType arrowType1 = new ArrowType(registry, Node.newList(),
 registry.createNumberType());
 ArrowType arrowType2 = new ArrowType(registry, Node.newList(),
 registry.createStringType());
 ArrowType arrowType3 = new ArrowType(registry, Node.newList(),
 registry.createBooleanType());

 ArrowType arrowType4 = new ArrowType(registry, Node.newList(),
 registry.createNumberType());
 ArrowType arrowType5 = new ArrowType(registry, Node.newList(),
 registry.createStringType());
 ArrowType arrowType6 = new ArrowType(registry, Node.newList(),
 registry.createBooleanType());

 ArrowType arrowType7 = new ArrowType(registry, Node.newList(),
 registry.createObjectType());

 assertTrue(arrowType1.isSubtype(arrowType1));
 assertTrue(arrowType1.isSubtype(arrowType4));
 assertTrue(arrowType4.isSubtype(arrowType1));
 assertTrue(arrowType4.isSubtype(arrowType4));

 assertFalse(arrowType1.isSubtype(arrowType2));
 assertFalse(arrowType2.isSubtype(arrowType1));
 assertFalse(arrowType1.isSubtype(arrowType3));
 assertFalse(arrowType3.isSubtype(arrowType1));

 assertFalse(arrowType1.isSubtype(arrowType7));
 assertFalse(arrowType7.isSubtype(arrowType1));
 }

 @Test
 public void testHasEqualParameters() {
 ArrowType arrowType1 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("y")),
 registry.createNumberType());
 ArrowType arrowType2 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("z")),
 registry.createNumberType());
 ArrowType arrowType3 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("y")),
 registry.createNumberType());

 assertTrue(arrowType1.hasEqualParameters(arrowType3));
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testIsEquivalentTo() {
 ArrowType arrowType1 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("y")),
 registry.createNumberType());
 ArrowType arrowType2 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("z")),
 registry.createNumberType());
 ArrowType arrowType3 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("y")),
 registry.createNumberType());

 ArrowType arrowType4 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("y")),
 registry.createStringType());

 assertTrue(arrowType1.isEquivalentTo(arrowType3));
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 assertFalse(arrowType1.isEquivalentTo(arrowType4));
 }

 @Test
 public void testHashCode() {
 ArrowType arrowType1 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("y")),
 registry.createNumberType());
 ArrowType arrowType2 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("z")),
 registry.createNumberType());
 ArrowType arrowType3 = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("y")),
 registry.createNumberType());

 int hashCode1 = arrowType1.hashCode();
 int hashCode2 = arrowType2.hashCode();
 int hashCode3 = arrowType3.hashCode();

 assertTrue(hashCode1 == hashCode3);
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testGetLeastSupertype() {
 throw new TestCaseNotFoundException("Method getLeastSupertype is not implemented");
 }

 @Test
 public void testGetGreatestSubtype() {
 throw new TestCaseNotFoundException("Method getGreatestSubtype is not implemented");
 }

 @Test
 public void testToString() {
 ArrowType arrowType = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("y")),
 registry.createNumberType());
 String toString = arrowType.toString();
 assertTrue(toString.contains("ArrowType"));
 assertTrue(toString.contains("Number"));
 }

 @Test
 public void testResolveInternal() {
 ArrowType arrowType = new ArrowType(registry, Node.newList(), null);
 arrowType.resolveInternal(errorReporter, registry.createEmptyScope());
 assertNotNull(arrowType.getReturnType());
 }

 @Test
 public void testHasUnknownParamsOrReturn() {
 ArrowType arrowType = new ArrowType(registry, Node.newList(), null);
 assertTrue(arrowType.hasUnknownParamsOrReturn());

 arrowType = new ArrowType(registry, Node.newList(
 Node.newIdentifier("x"), Node.newIdentifier("y")), null);
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }
}