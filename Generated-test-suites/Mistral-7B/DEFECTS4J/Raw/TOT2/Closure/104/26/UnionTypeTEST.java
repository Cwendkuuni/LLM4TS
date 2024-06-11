import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 @Test
 public void testUnionTypeBasicFunctionality() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(StringType.getInstance(), BooleanType.getInstance()));

 assertEquals("(String, Boolean)", unionType.toString());
 assertTrue(unionType.matchesNumberContext());
 assertTrue(unionType.matchesStringContext());
 assertTrue(unionType.matchesObjectContext());
 }

 @Test
 public void testFindPropertyType() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(StringType.getInstance(), BooleanType.getInstance()));
 assertTrue(unionType.findPropertyType("length") instanceof NumberType);
 assertTrue(unionType.findPropertyType("toString") instanceof StringType);
 }

 @Test
 public void testGetAlternates() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(StringType.getInstance(), BooleanType.getInstance()));
 Set<JSType> alternates = unionType.getAlternates();
 assertEquals(ImmutableSet.of(StringType.getInstance(), BooleanType.getInstance()), alternates);
 }

 @Test(expected = AssertionError.class)
 public void testErrorScenarioUnknownType() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(StringType.getInstance(), BooleanType.getInstance()));
 JSType unknownType = new UnknownType(JSTypeRegistry.getInstance());
 unionType.canAssignTo(unknownType);
 }

 @Test
 public void testCanBeCalled() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(FunctionType.getInstance(), NumberType.getInstance()));
 assertTrue(unionType.canBeCalled());
 }

 @Test(expected = AssertionError.class)
 public void testErrorScenarioCannotBeCalled() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(NullType.getInstance(), FunctionType.getInstance()));
 unionType.canBeCalled();
 }

 @Test
 public void testRestrictByNotNullOrUndefined() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(NullType.getInstance(), StringType.getInstance()));
 UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
 assertNotNull(restrictedUnionType);
 assertFalse(restrictedUnionType.isNullable());
 }

 @Test
 public void testTestForEquality() {
 UnionType unionType1 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(StringType.getInstance(), BooleanType.getInstance()));
 UnionType unionType2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(BooleanType.getInstance(), StringType.getInstance()));
 assertEquals(unionType1, unionType2);
 }

 @Test
 public void testIsNullable() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(NullType.getInstance(), StringType.getInstance()));
 assertFalse(unionType.isNullable());
 }

 @Test
 public void testIsUnknownType() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(UnknownType.getInstance(), StringType.getInstance()));
 assertTrue(unionType.isUnknownType());
 }

 @Test
 public void testGetLeastSupertype() {
 UnionType unionType1 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(NumberType.getInstance(), StringType.getInstance()));
 UnionType unionType2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(StringType.getInstance(), BooleanType.getInstance()));
 UnionType leastSupertype = unionType1.getLeastSupertype(unionType2);
 assertEquals(unionType1, leastSupertype);
 }

 @Test
 public void testMeet() {
 UnionType unionType1 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(NumberType.getInstance(), StringType.getInstance()));
 UnionType unionType2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(BooleanType.getInstance(), StringType.getInstance()));
 UnionType result = unionType1.meet(unionType2);
 assertEquals(new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(StringType.getInstance())), result);
 }
}