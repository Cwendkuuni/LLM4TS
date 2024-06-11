import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnionTypeTest {

 private static final JSTypeRegistry registry = new JSTypeRegistry();

 @Test
 public void testUnionTypeCreation() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 assertEquals("(String|Number)", stringOrNumber.toString());
 }

 @Test
 public void testUnionTypeContains() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 assertTrue(stringOrNumber.contains(JSType.getNativeType(JSTypeNative.STRING)));
 assertTrue(stringOrNumber.contains(JSType.getNativeType(JSTypeNative.NUMBER)));
 assertFalse(stringOrNumber.contains(JSType.getNativeType(JSTypeNative.BOOLEAN)));
 }

 @Test
 public void testUnionTypeEquals() {
 UnionType stringOrNumber1 = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 UnionType stringOrNumber2 = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 assertTrue(stringOrNumber1.equals(stringOrNumber2));
 }

 @Test
 public void testUnionTypeGetAlternates() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 Iterable<JSType> alternates = stringOrNumber.getAlternates();
 ImmutableSet<JSType> expectedAlternates = ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 );

 assertEquals(expectedAlternates, ImmutableSet.copyOf(alternates));
 }

 @Test
 public void testUnionTypeIsSubtype() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 assertTrue(JSType.getNativeType(JSTypeNative.NUMBER).isSubtype(stringOrNumber));
 assertTrue(JSType.getNativeType(JSTypeNative.STRING).isSubtype(stringOrNumber));
 assertFalse(JSType.getNativeType(JSTypeNative.BOOLEAN).isSubtype(stringOrNumber));
 }

 @Test
 public void testUnionTypeMatchesStringContext() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 assertTrue(stringOrNumber.matchesStringContext());
 }

 @Test
 public void testUnionTypeMatchesNumberContext() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 assertTrue(stringOrNumber.matchesNumberContext());
 }

 @Test
 public void testUnionTypeMatchesObjectContext() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 assertTrue(stringOrNumber.matchesObjectContext());
 }

 @Test
 public void testUnionTypeCanAssignTo() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 JSType stringType = JSType.getNativeType(JSTypeNative.STRING);
 JSType numberType = JSType.getNativeType(JSTypeNative.NUMBER);

 assertTrue(stringType.canAssignTo(stringOrNumber));
 assertTrue(numberType.canAssignTo(stringOrNumber));
 }

 @Test
 public void testUnionTypeCanBeCalled() {
 UnionType stringOrFunction = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.newFunctionType(registry, ImmutableSet.of(), null)
 ));

 assertTrue(stringOrFunction.canBeCalled());
 }

 @Test
 public void testUnionTypeGetLeastSupertype() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 JSType string = JSType.getNativeType(JSTypeNative.STRING);
 JSType number = JSType.getNativeType(JSTypeNative.NUMBER);
 JSType object = JSType.getNativeType(JSTypeNative.OBJECT);

 assertEquals(object, stringOrNumber.getLeastSupertype(string));
 assertEquals(object, stringOrNumber.getLeastSupertype(number));
 assertNull(stringOrNumber.getLeastSupertype(object));
 }

 @Test
 public void testUnionTypeGetRestrictedUnion() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 JSType number = JSType.getNativeType(JSTypeNative.NUMBER);
 JSType string = JSType.getNativeType(JSTypeNative.STRING);

 UnionType restricted = stringOrNumber.getRestrictedUnion(number);
 assertEquals(string, restricted);
 }

 @Test
 public void testUnionTypeToString() {
 UnionType stringOrNumber = new UnionType(registry, ImmutableSet.of(
 JSType.getNativeType(JSTypeNative.STRING),
 JSType.getNativeType(JSTypeNative.NUMBER)
 ));

 assertEquals("(String|Number)", stringOrNumber.toString());
 }
}