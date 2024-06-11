import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

 private static JSTypeRegistry registry;
 private static ErrorReporter t;

 @BeforeClass
 public static void setUp() {
 registry = new JSTypeRegistry();
 t = new ErrorReporter();
 }

 @Test
 public void testArrowTypeCreation() {
 // Test ArrowType creation with known types
 Node parameters1 = registry.createList(Node.createIdent("x"), Node.createIdent("y"));
 JSType returnType1 = registry.createStringType("string");
 ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1, false);

 Node parameters2 = registry.createList(Node.createIdent("x"), Node.createIdent("y"), Node.createIdent("z"));
 JSType returnType2 = JSTypeNative.VOID;
 ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2, true);

 // Test ArrowType creation with unknown type
 ArrowType arrowType3 = new ArrowType(registry, null, null, false);

 // Test ArrowType creation with var args
 ArrowType arrowType4 = new ArrowType(registry, Node.createIdent("param"), null, true);

 // Test ArrowType creation with empty list
 ArrowType arrowType5 = new ArrowType(registry, null, JSTypeNative.VOID, false);

 // Test ArrowType creation with null parameters and return type
 ArrowType arrowType6 = new ArrowType(registry, null, null, false);

 // Test ArrowType creation with no parameters
 ArrowType arrowType7 = new ArrowType(registry, null, null, false);
 }

 @Test
 public void testIsSubtype() {
 ArrowType arrowType1 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 ArrowType arrowType2 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y"), Node.createIdent("z")),
 JSTypeNative.VOID, true);
 ArrowType arrowType3 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y"), Node.createIdent("z")),
 registry.createStringType("number"), false);

 assertTrue(arrowType1.isSubtype(arrowType1));
 assertTrue(arrowType1.isSubtype(arrowType3));
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testHasEqualParameters() {
 ArrowType arrowType1 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 ArrowType arrowType2 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 ArrowType arrowType3 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("number"), false);
 ArrowType arrowType4 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("z")),
 registry.createStringType("string"), false);

 assertTrue(arrowType1.hasEqualParameters(arrowType1));
 assertTrue(arrowType1.hasEqualParameters(arrowType3));
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 assertFalse(arrowType1.hasEqualParameters(arrowType4));
 }

 @Test
 public void testIsEquivalentTo() {
 ArrowType arrowType1 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 ArrowType arrowType2 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 ArrowType arrowType3 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("number"), false);
 ArrowType arrowType4 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("z")),
 registry.createStringType("string"), false);
 ArrowType arrowType5 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), true);

 assertTrue(arrowType1.isEquivalentTo(arrowType1));
 assertTrue(arrowType1.isEquivalentTo(arrowType3));
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 assertFalse(arrowType1.isEquivalentTo(arrowType4));
 assertFalse(arrowType1.isEquivalentTo(arrowType5));
 }

 @Test
 public void testHashCode() {
 ArrowType arrowType1 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 ArrowType arrowType2 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("number"), false);
 ArrowType arrowType3 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), true);

 int hashCode1 = arrowType1.hashCode();
 int hashCode2 = arrowType1.hashCode();
 int hashCode3 = arrowType2.hashCode();
 int hashCode4 = arrowType3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 }

 @Test
 public void testGetLeastSupertype() {
 ArrowType arrowType1 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 ArrowType arrowType2 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("number"), false);
 ArrowType arrowType3 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("number"), true);
 ArrowType arrowType4 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("z")),
 registry.createStringType("string"), false);

 assertNull(arrowType1.getLeastSupertype(arrowType1));
 assertNull(arrowType1.getLeastSupertype(arrowType1));
 assertNull(arrowType1.getLeastSupertype(arrowType3));
 assertNull(arrowType3.getLeastSupertype(arrowType1));
 assertNull(arrowType3.getLeastSupertype(arrowType3));
 assertNull(arrowType3.getLeastSupertype(arrowType4));
 assertNull(arrowType4.getLeastSupertype(arrowType1));
 assertNull(arrowType4.getLeastSupertype(arrowType3));
 assertNull(arrowType4.getLeastSupertype(arrowType4));
 }

 @Test
 public void testGetGreatestSubtype() {
 ArrowType arrowType1 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 ArrowType arrowType2 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("number"), false);
 ArrowType arrowType3 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("number"), true);
 ArrowType arrowType4 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("z")),
 registry.createStringType("string"), false);

 assertNull(arrowType1.getGreatestSubtype(arrowType1));
 assertNull(arrowType1.getGreatestSubtype(arrowType1));
 assertNull(arrowType1.getGreatestSubtype(arrowType3));
 assertNull(arrowType3.getGreatestSubtype(arrowType1));
 assertNull(arrowType3.getGreatestSubtype(arrowType3));
 assertNull(arrowType3.getGreatestSubtype(arrowType4));
 assertNull(arrowType4.getGreatestSubtype(arrowType1));
 assertNull(arrowType4.getGreatestSubtype(arrowType3));
 assertNull(arrowType4.getGreatestSubtype(arrowType4));
 }

 @Test
 public void testToString() {
 ArrowType arrowType = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 String toString = arrowType.toString();
 String expected = "-> (x: any, y: string)";
 assertEquals(expected, toString);
 }

 @Test
 public void testToStringForAnnotations() {
 ArrowType arrowType = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 String toString = arrowType.toStringForAnnotations();
 String expected = "-> (x: any, y: java.lang.String)";
 assertEquals(expected, toString);
 }

 @Test
 public void testResolveInternal() {
 ArrowType arrowType = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 arrowType.resolveInternal(t, registry.createEmptyScope());
 }

 @Test
 public void testHasUnknownParamsOrReturn() {
 ArrowType arrowType1 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), false);
 ArrowType arrowType2 = new ArrowType(registry, null, null, false);
 ArrowType arrowType3 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 null, false);
 ArrowType arrowType4 = new ArrowType(registry, Node.createList(Node.createIdent("x"), Node.createIdent("y")),
 registry.createStringType("string"), true);

 assertFalse(arrowType1.hasUnknownParamsOrReturn());
 assertTrue(arrowType2.hasUnknownParamsOrReturn());
 assertTrue(arrowType3.hasUnknownParamsOrReturn());
 assertTrue(arrowType4.hasUnknownParamsOrReturn());
 }
}