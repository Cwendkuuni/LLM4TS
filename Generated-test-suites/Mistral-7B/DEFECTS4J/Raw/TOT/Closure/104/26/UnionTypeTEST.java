import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionTypeTest {
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testStringBooleanUnionType() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType stringBooleanUnionType = new UnionType(registry, alternates);

    // Test matchesContext methods
    assertTrue(stringBooleanUnionType.matchesStringContext());
    assertTrue(stringBooleanUnionType.matchesObjectContext());
    assertFalse(stringBooleanUnionType.matchesNumberContext());

    // Test getAlternates, findPropertyType, and canAssignTo methods
    assertEquals(alternates, stringBooleanUnionType.getAlternates());
    assertNull(stringBooleanUnionType.findPropertyType("toString"));
    assertTrue(stringBooleanUnionType.canAssignTo(registry.getStringType()));
    assertFalse(stringBooleanUnionType.canAssignTo(registry.getNumberType()));

    // Test isSubtype method
    assertTrue(registry.getStringType().isSubtype(stringBooleanUnionType));
    assertTrue(registry.getBooleanType().isSubtype(stringBooleanUnionType));
    assertFalse(registry.getNumberType().isSubtype(stringBooleanUnionType));

    // Test testForEquality method
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    assertEquals(UNKNOWN, stringBooleanUnionType.testForEquality(stringType));
    assertEquals(UNKNOWN, stringBooleanUnionType.testForEquality(booleanType));
    assertEquals(booleanType, booleanType.testForEquality(stringBooleanUnionType));
    assertEquals(stringType, stringType.testForEquality(stringBooleanUnionType));

    // Test isNullable and isUnknownType methods
    assertFalse(stringBooleanUnionType.isNullable());
    assertFalse(stringBooleanUnionType.isUnknownType());

    // Test getLeastSupertype method
    JSType leastSupertype = stringBooleanUnionType.getLeastSupertype(registry.getObjectType());
    assertNotNull(leastSupertype);
    assertFalse(leastSupertype.isUnionType());

    // Test meet method
    UnionType stringOrBooleanUnionType = new UnionType(registry, ImmutableSet.of(stringBooleanUnionType, registry.getNumberType()));
    JSType meetResult = stringBooleanUnionType.meet(registry.getNumberType());
    assertNull(meetResult);

    meetResult = stringOrBooleanUnionType.meet(registry.getObjectType());
    assertNotNull(meetResult);
    assertFalse(meetResult.isUnionType());
  }

  @Test
  public void testEmptyUnionType() {
    UnionType emptyUnionType = new UnionType(registry, ImmutableSet.of());
    assertEquals(0, emptyUnionType.getAlternates().size());
    assertFalse(emptyUnionType.matchesStringContext());
    assertFalse(emptyUnionType.matchesObjectContext());
    assertFalse(emptyUnionType.matchesNumberContext());
    assertFalse(emptyUnionType.isNullable());
    assertFalse(emptyUnionType.isUnknownType());
    assertNull(emptyUnionType.findPropertyType("toString"));
    assertFalse(emptyUnionType.canAssignTo(registry.getStringType()));
    assertFalse(emptyUnionType.canAssignTo(registry.getNumberType()));
    assertTrue(emptyUnionType.isSubtype(registry.getObjectType()));
    assertTrue(emptyUnionType.isSubtype(registry.getNumberType()));
    assertFalse(registry.getNumberType().isSubtype(emptyUnionType));
    assertEquals(UNKNOWN, emptyUnionType.testForEquality(registry.getStringType()));
    assertEquals(UNKNOWN, emptyUnionType.testForEquality(registry.getBooleanType()));
    assertNull(emptyUnionType.getLeastSupertype(registry.getObjectType()));
  }
}