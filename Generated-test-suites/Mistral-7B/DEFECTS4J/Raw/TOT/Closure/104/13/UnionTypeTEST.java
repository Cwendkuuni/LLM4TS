import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

  @Test
  public void testUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

    // Test getAlternates() method
    assertEquals(2, unionType.getAlternates().size());
    assertTrue(unionType.getAlternates().contains(stringType));
    assertTrue(unionType.getAlternates().contains(booleanType));

    // Test matchesNumberContext() method
    assertFalse(unionType.matchesNumberContext());

    // Test matchesStringContext() method
    assertTrue(unionType.matchesStringContext());

    // Test matchesObjectContext() method
    assertTrue(unionType.matchesObjectContext());

    // Test findPropertyType() method
    JSType propertyType = unionType.findPropertyType("length");
    assertNull(propertyType);

    // Test canAssignTo() method
    JSType assignableType = registry.getIntegerType();
    assertTrue(unionType.canAssignTo(assignableType));

    // Test canBeCalled() method
    BooleanFunction function = new BooleanFunction() {
      @Override
      public boolean call(BooleanContext ctx) {
        return true;
      }
    };
    assertFalse(unionType.canBeCalled());
    unionType = new UnionType(registry, ImmutableSet.of(function.getType()));
    assertTrue(unionType.canBeCalled());

    // Test restrictByNotNullOrUndefined() method
    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertNotNull(restrictedType);
    assertFalse(restrictedType.isNullable());

    // Test testForEquality() method
    JSType that = registry.getStringType();
    TernaryValue result = unionType.testForEquality(that);
    assertEquals(UNKNOWN, result);

    // Test isNullable() method
    assertFalse(unionType.isNullable());

    // Test isUnknownType() method
    assertFalse(unionType.isUnknownType());

    // Test getLeastSupertype() method
    UnionType leastSupertype = new UnionType(registry, ImmutableSet.of(stringType));
    assertEquals(unionType, leastSupertype.getLeastSupertype(unionType));

    // Test meet() method
    UnionType meetType = new UnionType(registry, ImmutableSet.of(booleanType));
    JSType meetResult = unionType.meet(meetType);
    assertNotNull(meetResult);
    assertEquals(unionType, meetResult);

    // Test equals() method
    UnionType equalsType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));
    assertTrue(unionType.equals(equalsType));

    // Test hashCode() method
    UnionType hashCodeType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));
    assertEquals(hashCodeType.hashCode(), unionType.hashCode());

    // Test isUnionType() method
    assertTrue(unionType.isUnionType());

    // Test isObject() method
    assertTrue(unionType.isObject());

    // Test contains() method
    JSType stringAlternate = registry.getStringType();
    assertTrue(unionType.contains(stringAlternate));

    // Test getRestrictedUnion() method
    JSType restrictedType2 = unionType.getRestrictedUnion(booleanType);
    assertNotNull(restrictedType2);
    assertFalse(restrictedType2.isSubtype(unionType));

    // Test toString() method
    assertEquals("(boolean, String)", unionType.toString());
  }
}