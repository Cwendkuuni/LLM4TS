import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.any;
import static com.google.javascript.rhino.jstype.JSType.booleanType;
import static com.google.javascript.rhino.jstype.JSType.numberType;
import static com.google.javascript.rhino.jstype.JSType.nullType;
import static com.google.javascript.rhino.jstype.JSType.stringType;
import static com.google.javascript.rhino.jstype.JSType.undefinedType;
import static com.google.javascript.rhino.jstype.JSType.voidType;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;

public class UnionTypeTest {

  private static UnionType createUnionType(JSTypeRegistry registry, JSType... types) {
    ImmutableSet.Builder<JSType> builder = ImmutableSet.builder();
    for (JSType type : types) {
      builder.add(type);
    }
    return new UnionType(registry, builder.build());
  }

  @Test
  public void testEmptyUnion() {
    UnionType emptyUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of());
    assertEquals(voidType, emptyUnion.getLeastSupertype(any));
  }

  @Test
  public void testSingleAlternate() {
    JSType singleAlternate = stringType;
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(singleAlternate));
    assertEquals(singleAlternate, unionType.getLeastSupertype(singleAlternate));
    assertEquals(stringType, unionType.getLeastSupertype(numberType));
  }

  @Test
  public void testMultipleAlternates() {
    JSType stringAlternate = stringType;
    JSType numberAlternate = numberType;
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(stringAlternate, numberAlternate));

    // Test with a string type
    assertEquals(stringAlternate, unionType.getLeastSupertype(stringType));
    assertEquals(numberAlternate, unionType.getLeastSupertype(numberType));
    assertEquals(stringAlternate, unionType.getLeastSupertype(stringAlternate));
    assertEquals(numberAlternate, unionType.getLeastSupertype(numberAlternate));

    // Test with a null type
    assertEquals(nullType, unionType.getLeastSupertype(nullType));

    // Test with a void type
    assertEquals(voidType, unionType.getLeastSupertype(voidType));

    // Test with an undefined type
    assertEquals(undefinedType, unionType.getLeastSupertype(undefinedType));

    // Test with a boolean type
    assertEquals(booleanType, unionType.getLeastSupertype(booleanType));
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    JSType nullType = JSTypeFactory.getInstance().nullType();
    JSType stringType = JSTypeFactory.getInstance().stringType();
    JSType booleanType = JSTypeFactory.getInstance().booleanType();
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(nullType, stringType, booleanType));

    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();

    // Test with null type
    assertEquals(nullType, restrictedUnionType.getLeastSupertype(nullType));

    // Test with string type
    assertEquals(stringType, restrictedUnionType.getLeastSupertype(stringType));

    // Test with boolean type
    assertEquals(booleanType, restrictedUnionType.getLeastSupertype(booleanType));

    // Test with null type again
    assertEquals(nullType, restrictedUnionType.getLeastSupertype(nullType));
  }

  @Test
  public void testGetRestrictedUnion() {
    JSType numberType = JSTypeFactory.getInstance().numberType();
    JSType stringType = JSTypeFactory.getInstance().stringType();
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(numberType, stringType));

    UnionType restrictedNumberTypeUnion = unionType.getRestrictedUnion(numberType);

    // Test with number type
    assertEquals(numberType, restrictedNumberTypeUnion.getLeastSupertype(numberType));

    // Test with string type
    assertEquals(stringType, restrictedNumberTypeUnion.getLeastSupertype(stringType));

    // Test with number type again
    assertEquals(numberType, restrictedNumberTypeUnion.getLeastSupertype(numberType));

    UnionType restrictedStringTypeUnion = unionType.getRestrictedUnion(stringType);

    // Test with number type
    assertEquals(numberType, restrictedStringTypeUnion.getLeastSupertype(numberType));

    // Test with string type
    assertEquals(stringType, restrictedStringTypeUnion.getLeastSupertype(stringType));

    // Test with string type again
    assertEquals(stringType, restrictedStringTypeUnion.getLeastSupertype(stringType));
  }

  @Test
  public void testMeet() {
    JSType numberType = JSTypeFactory.getInstance().numberType();
    JSType stringType = JSTypeFactory.getInstance().stringType();
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(numberType, stringType));
    UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(stringType, numberType));

    UnionType result = unionTypeA.meet(unionTypeB);

    assertEquals(numberType, result.getLeastSupertype(numberType));
    assertEquals(stringType, result.getLeastSupertype(stringType));
  }

  @Test
  public void testCanAssignTo() {
    JSType numberType = JSTypeFactory.getInstance().numberType();
    JSType stringType = JSTypeFactory.getInstance().stringType();
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(numberType, stringType));

    JSType intType = JSTypeFactory.getInstance().intType();
    JSType stringAlternate = JSTypeFactory.getInstance().stringType();

    assertTrue(unionType.canAssignTo(intType));
    assertTrue(unionType.canAssignTo(numberType));
    assertTrue(unionType.canAssignTo(stringAlternate));
    assertTrue(unionType.canAssignTo(stringType));
  }

  @Test
  public void testCanBeCalled() {
    JSType numberType = JSTypeFactory.getInstance().numberType();
    JSType functionType = new FunctionType(JSTypeRegistry.getInstance(), voidType, ImmutableSet.of(numberType));
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(numberType, functionType));

    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testFindPropertyType() {
    JSType numberType = JSTypeFactory.getInstance().numberType();
    JSType stringType = JSTypeFactory.getInstance().stringType();
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(numberType, stringType));

    JSType numberPropertyType = JSTypeFactory.getInstance().numberType();
    JSType stringPropertyType = JSTypeFactory.getInstance().stringType();

    assertEquals(numberPropertyType, unionType.findPropertyType("length"));
    assertEquals(stringPropertyType, unionType.findPropertyType("toString"));
  }

  @Test
  public void testIsSubtype() {
    JSType numberType = JSTypeFactory.getInstance().numberType();
    JSType stringType = JSTypeFactory.getInstance().stringType();
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(numberType, stringType));

    assertTrue(numberType.isSubtype(unionType));
    assertTrue(stringType.isSubtype(unionType));
    assertFalse(booleanType.isSubtype(unionType));
  }

  @Test
  public void testEquals() {
    JSType numberType = JSTypeFactory.getInstance().numberType();
    JSType stringType = JSTypeFactory.getInstance().stringType();
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(numberType, stringType));
    UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(stringType, numberType));

    assertFalse(unionTypeA.equals(voidType));
    assertFalse(unionTypeA.equals(numberType));
    assertFalse(unionTypeA.equals(stringType));
    assertFalse(unionTypeA.equals(booleanType));

    assertTrue(unionTypeA.equals(unionTypeB));
  }
}