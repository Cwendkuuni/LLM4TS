import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ArrowTypeTest {

 @Test
 public void testArrowTypeCreationWithNullParametersAndNullReturnType() {
 ArrowType arrowType = new ArrowType(new JSTypeRegistry(), null, null);
 assertNotNull(arrowType);
 }

 @Test
 public void testArrowTypeCreationWithValidParametersAndNullReturnType() {
 Node parameters = Node.newList();
 ArrowType arrowType = new ArrowType(new JSTypeRegistry(), parameters, null);
 assertNotNull(arrowType);
 }

 @Test
 public void testArrowTypeCreationWithValidParametersAndValidReturnType() {
 Node parameters = Node.newList();
 JSType returnType = JSTypeNative.createIntType();
 ArrowType arrowType = new ArrowType(new JSTypeRegistry(), parameters, returnType);
 assertNotNull(arrowType);
 }

 @Test
 public void testArrowTypeCreationWithValidParametersAndInferredReturnType() {
 Node parameters = Node.newList();
 ArrowType arrowType = new ArrowType(new JSTypeRegistry(), parameters, null, true);
 assertNotNull(arrowType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeCreationWithInvalidParametersAndNullReturnType() {
 Node invalidParameters = Node.newList(Node.newIdentifier("invalid"));
 ArrowType arrowType = new ArrowType(new JSTypeRegistry(), invalidParameters, null);
 // This test case should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeCreationWithNullParametersAndValidReturnType() {
 ArrowType arrowType = new ArrowType(new JSTypeRegistry(), null, JSTypeNative.createIntType());
 // This test case should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeCreationWithInvalidParametersAndValidReturnType() {
 Node invalidParameters = Node.newList(Node.newIdentifier("invalid"));
 JSType returnType = JSTypeNative.createIntType();
 ArrowType arrowType = new ArrowType(new JSTypeRegistry(), invalidParameters, returnType);
 // This test case should throw an IllegalArgumentException
 }

 @Test
 public void testArrowTypeIsSubtypeOfArrowTypeWithSameParametersAndReturnType() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(), JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(), JSTypeNative.createIntType());
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeOfArrowTypeWithDifferentParametersButSameReturnType() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList(Node.newIdentifier("a"), Node.newIdentifier("b"))),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("a"), Node.newIdentifier("b")),
 Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y"))),
 JSTypeNative.createIntType());
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeOfArrowTypeWithDifferentReturnTypes() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createDoubleType());
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeOfArrowTypeWithMissingParameters() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x")), Node.newList()),
 JSTypeNative.createIntType());
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeOfArrowTypeWithExtraParameters() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y"),
 Node.newIdentifier("z")), Node.newList()),
 JSTypeNative.createIntType());
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeHasEqualParameters() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testArrowTypeIsEquivalentToArrowTypeWithSameParametersAndReturnType() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 assertTrue(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testArrowTypeIsEquivalentToArrowTypeWithDifferentParametersAndReturnType() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("a"), Node.newIdentifier("b")),
 Node.newList()),
 JSTypeNative.createIntType());
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testArrowTypeHashCodeWithSameParametersAndReturnType() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
 }

 @Test
 public void testArrowTypeHashCodeWithDifferentParametersAndReturnType() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("a"), Node.newIdentifier("b")),
 Node.newList()),
 JSTypeNative.createIntType());
 assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
 }

 @Test
 public void testArrowTypeIsLeastSupertypeOfArrowTypeWithSameParametersAndReturnType() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("a"), Node.newIdentifier("b")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowType leastCommonSuperType = new ArrowType(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("a"), Node.newIdentifier("b")),
 Node.newList()),
 JSTypeNative.createIntType());
 assertTrue(arrowType1.isSubtype(leastCommonSuperType));
 assertTrue(arrowType2.isSubtype(leastCommonSuperType));
 assertTrue(leastCommonSuperType.isSubtype(arrowType1));
 assertTrue(leastCommonSuperType.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsGreaterSubtypeOfArrowTypeWithSameParametersAndReturnType() {
 ArrowTypeArrowType1 arrowType1 = new ArrowTypeArrowType1(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 ArrowTypeArrowType2 arrowType2 = new ArrowTypeArrowType2(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("a"), Node.newIdentifier("b")),
 Node.newList()),
 JSTypeNative.createIntType());
 assertFalse(arrowType1.isSubtype(arrowType2));
 assertFalse(arrowType2.isSubtype(arrowType1));
 }

 @Test
 public void testArrowTypeToString() {
 ArrowTypeArrowType arrowType = new ArrowTypeArrowType(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType());
 assertEquals("function(x: any, y: any) => int", arrowType.toString());
 }

 @Test
 public void testArrowTypeToStringWithInferredReturnType() {
 ArrowTypeArrowType arrowType = new ArrowTypeArrowType(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 null, true);
 assertEquals("function(x: any, y: any) => any", arrowType.toString());
 }

 @Test
 public void testArrowTypeToStringWithMissingParameters() {
 ArrowTypeArrowType arrowType = new ArrowTypeArrowType(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList()),
 JSTypeNative.createIntType(), true);
 assertEquals("function(x: any, y: any)", arrowType.toString());
 }

 @Test
 public void testArrowTypeToStringWithExtraParameters() {
 ArrowTypeArrowType arrowType = new ArrowTypeArrowType(new JSTypeRegistry(),
 Node.newList(Node.newList(Node.newIdentifier("x"), Node.newIdentifier("y")),
 Node.newList(Node.newIdentifier("z"), Node.newIdentifier("w"))),
 JSTypeNative.createIntType());
 assertEquals("function(x: any, y: any, z: any, w: any) => int", arrowType.toString());
 }
}

class ArrowTypeArrowType extends ArrowType {
 ArrowTypeArrowType(JSTypeRegistry registry, Node parameters, JSType returnType) {
 super(registry, parameters, returnType);
 }

 ArrowTypeArrowType(JSTypeRegistry registry, Node parameters, JSType returnType,
 boolean returnTypeInferred) {
 super(registry, parameters, returnType, returnTypeInferred);
 }
}