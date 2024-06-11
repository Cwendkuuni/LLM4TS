import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 private static JSTypeRegistry registry = new JSTypeRegistry();

 @Test
 public void testUnionTypeCreation() {
 UnionType unionType = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));

 assertEquals("(Integer, Boolean)", unionType.toString());
 }

 @Test
 public void testUnionTypeMatchesContexts() {
 UnionType unionType = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));

 assertTrue(unionType.matchesNumberContext());
 assertTrue(unionType.matchesStringContext());
 assertTrue(unionType.matchesObjectContext());
 }

 @Test
 public void testUnionTypeFindPropertyType() {
 UnionType unionType = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 UnionType stringUnionType = new UnionType(registry, ImmutableSet.of(
 StringType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));

 assertEquals(StringType.getPrimitiveType(registry), unionType.findPropertyType("toString"));
 assertEquals(BooleanType.getPrimitiveType(registry), unionType.findPropertyType("length"));
 assertNull(unionType.findPropertyType("length")); // IntegerType has no "length" property
 assertNull(unionType.findPropertyType("toString")).findPropertyType("toUpperCase"); // StringType has no "toUpperCase" property

 UnionType stringOrBooleanUnionType = new UnionType(registry, ImmutableSet.of(
 StringType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 assertEquals(StringType.getPrimitiveType(registry), stringOrBooleanUnionType.findPropertyType("toString"));
 assertEquals(BooleanType.getPrimitiveType(registry), stringOrBooleanUnionType.findPropertyType("toString")); // False positive, both types can have a "toString" property
 assertNull(stringOrBooleanUnionType.findPropertyType("length"));
 }

 @Test
 public void testUnionTypeCanAssignTo() {
 UnionType unionType = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 IntegerType integerType = (IntegerType) registry.getType("int");

 assertTrue(integerType.canAssignTo(unionType));

 unionType = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry),
 StringType.getPrimitiveType(registry)
 ));
 assertFalse(integerType.canAssignTo(unionType));
 }

 @Test
 public void testUnionTypeCanBeCalled() {
 UnionType unionType = new UnionType(registry, ImmutableSet.of(
 FunctionType.getPrimitiveType(registry, IntegerType.getPrimitiveType(registry), VoidType.getBoundType()),
 StringType.getPrimitiveType(registry)
 ));
 assertFalse(unionType.canBeCalled());

 unionType = new UnionType(registry, ImmutableSet.of(
 FunctionType.getPrimitiveType(registry, IntegerType.getPrimitiveType(registry), VoidType.getBoundType()),
 StringType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 assertTrue(unionType.canBeCalled());
 }

 @Test
 public void testUnionTypeRestrictByNotNullOrUndefined() {
 UnionType unionType = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();

 assertTrue(restrictedUnionType.isSubtype(unionType));
 assertFalse(restrictedUnionType.isSubtype(IntegerType.getPrimitiveType(registry)));
 assertFalse(unionType.isSubtype(restrictedUnionType));
 }

 @Test
 public void testUnionTypeTestForEquality() {
 UnionType unionType1 = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 UnionType unionType2 = new UnionType(registry, ImmutableSet.of(
 BooleanType.getPrimitiveType(registry),
 IntegerType.getPrimitiveType(registry)
 ));
 UnionType unionType3 = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 IntegerType.getPrimitiveType(registry)
 ));
 UnionType unionType4 = new UnionType(registry, ImmutableSet.of(
 BooleanType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));

 assertEquals(unionType1, unionType1);
 assertNotEquals(unionType1, unionType2);
 assertNotEquals(unionType1, unionType3);
 assertNotEquals(unionType1, unionType4);

 assertTrue(unionType1.testForEquality(unionType1));
 assertFalse(unionType1.testForEquality(unionType2));
 assertFalse(unionType1.testForEquality(unionType3));
 assertFalse(unionType1.testForEquality(unionType4));

 assertEquals(unionType2.testForEquality(unionType1), UNKNOWN);
 assertEquals(unionType2.testForEquality(unionType2), TRUE);
 assertEquals(unionType2.testForEquality(unionType3), FALSE);
 assertEquals(unionType2.testForEquality(unionType4), FALSE);

 assertEquals(unionType3.testForEquality(unionType1), FALSE);
 assertEquals(unionType3.testForEquality(unionType3), TRUE);
 assertEquals(unionType3.testForEquality(unionType4), FALSE);

 assertEquals(unionType4.testForEquality(unionType1), FALSE);
 assertEquals(unionType4.testForEquality(unionType2), FALSE);
 assertEquals(unionType4.testForEquality(unionType4), TRUE);
 }

 @Test
 public void testUnionTypeIsNullable() {
 UnionType unionType = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 assertFalse(unionType.isNullable());

 unionType = new UnionType(registry, ImmutableSet.of(
 NullType.INSTANCE,
 BooleanType.getPrimitiveType(registry)
 ));
 assertTrue(unionType.isNullable());
 }

 @Test
 public void testUnionTypeIsUnknownType() {
 UnionType unionType = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 assertFalse(unionType.isUnknownType());

 unionType = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 UnknownType.INSTANCE
 ));
 assertTrue(unionType.isUnknownType());
 }

 @Test
 public void testUnionTypeGetLeastSupertype() {
 UnionType unionType1 = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 UnionType unionType2 = new UnionType(registry, ImmutableSet.of(
 BooleanType.getPrimitiveType(registry),
 IntegerType.getPrimitiveType(registry),
 StringType.getPrimitiveType(registry)
 ));

 JSType leastSupertype = unionType1.getLeastSupertype(unionType2);
 assertEquals(IntegerType.getPrimitiveType(registry), leastSupertype);
 }

 @Test
 public void testUnionTypeMeet() {
 UnionType unionType1 = new UnionType(registry, ImmutableSet.of(
 IntegerType.getPrimitiveType(registry),
 BooleanType.getPrimitiveType(registry)
 ));
 UnionType unionType2 = new UnionType(registry, ImmutableSet.of(
 BooleanType.getPrimitiveType(registry),
 StringType.getPrimitiveType(registry)
 ));

 assertEquals(new UnionType(registry, ImmutableSet.of(
 BooleanType.getPrimitiveType(registry),
 IntegerType.getPrimitiveType(registry),
 StringType.getPrimitiveType(registry)
 )), unionType1.meet(unionType2));
 }
}