import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.TernaryValue.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.NativeType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testConstructWithEmptyAlternates() {
    UnionType emptyUnion = new UnionType(registry, ImmutableSet.of());
    assertTrue(emptyUnion.isObject());
    assertFalse(emptyUnion.canAssignTo(NativeType.NUMBER));
    assertFalse(emptyUnion.canAssignTo(NativeType.BOOLEAN));
    assertEquals(ImmutableSet.of(), emptyUnion.getAlternates());
  }

  @Test
  public void testConstructWithValidAlternates() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    assertTrue(unionType.isObject());
    assertFalse(unionType.canAssignTo(NativeType.NUMBER));
    assertFalse(unionType.canAssignTo(NativeType.BOOLEAN));
    assertEquals(ImmutableSet.of(numberType, booleanType), unionType.getAlternates());
  }

  @Test
  public void testGetAlternates() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    ImmutableSet<JSType> alternatives = unionType.getAlternates();
    assertEquals(ImmutableSet.of(numberType, booleanType), alternatives);
  }

  @Test
  public void testCanAssignTo() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    assertFalse(unionType.canAssignTo(NativeType.NUMBER));
    assertFalse(unionType.canAssignTo(NativeType.BOOLEAN));
    assertTrue(unionType.canAssignTo(unionType));

    JSType stringType = registry.lookupPrimitiveType("String");
    assertTrue(unionType.canAssignTo(stringType));

    JSType objectType = registry.lookupNativeType("Object");
    assertTrue(unionType.canAssignTo(objectType));
  }

  @Test
  public void testCanBeCalled() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertFalse(restrictedUnionType.isObject());
    assertFalse(restrictedUnionType.canAssignTo(NativeType.NUMBER));
    assertFalse(restrictedUnionType.canAssignTo(NativeType.BOOLEAN));
    assertNull(restrictedUnionType.getAlternates());
  }

  @Test
  public void testTestForEquality() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));

    JSType numberType2 = registry.lookupPrimitiveType("Number");
    UnionType numberUnionType = new UnionType(registry, ImmutableSet.of(numberType2));
    JSType booleanType2 = registry.lookupPrimitiveType("Boolean");
    UnionType booleanUnionType = new UnionType(registry, ImmutableSet.of(booleanType2));
    UnionType otherUnionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType2));

    TernaryValue result = unionType.testForEquality(numberType);
    assertEquals(UNKNOWN, result);

    result = unionType.testForEquality(numberUnionType);
    assertEquals(UNKNOWN, result);

    result = unionType.testForEquality(booleanType);
    assertEquals(UNKNOWN, result);

    result = unionType.testForEquality(booleanUnionType);
    assertEquals(UNKNOWN, result);

    result = unionType.testForEquality(otherUnionType);
    assertEquals(UNKNOWN, result);

    result = numberUnionType.testForEquality(booleanUnionType);
    assertEquals(UNKNOWN, result);

    result = numberType.testForEquality(booleanType);
    assertEquals(FALSE, result);

    result = numberType.testForEquality(numberType);
    assertEquals(TRUE, result);

    result = booleanType.testForEquality(booleanType);
    assertEquals(TRUE, result);
  }

  @Test
  public void testFindPropertyType() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));

    JSType numberString = registry.lookupNativeType("Number");
    JSType string = registry.lookupPrimitiveType("String");
    JSType stringNumber = new UnionType(registry, ImmutableSet.of(numberString, string));
    JSType stringBoolean = new UnionType(registry, ImmutableSet.of(string, booleanType));

    JSType property = registry.newJSFunction("property", new Class[0], new Class[0]);
    JSType numberProperty = new JSTypeAnnotatedFunction(registry, property, ImmutableSet.of(numberType));
    JSType booleanProperty = new JSTypeAnnotatedFunction(registry, property, ImmutableSet.of(booleanType));

    JSType numberPropertyType = unionType.findPropertyType("property");
    assertNull(numberPropertyType);

    numberPropertyType = numberUnionType.findPropertyType("property");
    assertEquals(numberProperty, numberPropertyType);

    numberPropertyType = stringNumber.findPropertyType("property");
    assertEquals(numberString, numberPropertyType.getLeastSupertype(numberProperty));

    numberPropertyType = stringBoolean.findPropertyType("property");
    assertEquals(stringBoolean, numberPropertyType.getLeastSupertype(numberProperty));

    numberPropertyType = string.findPropertyType("property");
    assertEquals(stringBoolean, numberPropertyType.getLeastSupertype(numberProperty));

    numberPropertyType = booleanType.findPropertyType("property");
    assertEquals(stringBoolean, numberPropertyType.getLeastSupertype(numberProperty));

    numberPropertyType = numberType.findPropertyType("property");
    assertEquals(numberPropertyType, numberPropertyType);
  }

  @Test(expected = AssertionError.class)
  public void testCanAssignToSelf() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, numberType));
    unionType.canAssignTo(unionType);
  }

  @Test
  public void testEqualsAndHashcode() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType1 = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    UnionType unionType2 = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    assertEquals(unionType1, unionType2);
    assertEquals(unionType1.hashCode(), unionType2.hashCode());
  }

  @Test
  public void testNotEquals() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType1 = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    UnionType numberUnionType = new UnionType(registry, ImmutableSet.of(numberType));
    assertNotEquals(unionType1, numberUnionType);
  }

  @Test
  public void testToString() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    assertEquals("(Number, Boolean)", unionType.toString());
  }

  @Test
  public void testIsUnionType() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testIsObject() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    assertTrue(unionType.isObject());
  }

  @Test
  public void testContains() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    assertFalse(unionType.contains(NativeType.NUMBER));
    assertFalse(unionType.contains(NativeType.BOOLEAN));
    assertTrue(unionType.contains(numberType));
    assertTrue(unionType.contains(booleanType));
  }

  @Test
  public void testGetRestrictedUnion() {
    JSType numberType = registry.lookupPrimitiveType("Number");
    JSType booleanType = registry.lookupPrimitiveType("Boolean");
    UnionType unionType = new UnionType(registry, ImmutableSet.of(numberType, booleanType));
    JSType restrictedNumberType = unionType.getRestrictedUnion(numberType);
    assertFalse(restrictedNumberType.isObject());
    assertSame(numberType, restrictedNumberType);

    JSType restrictedBooleanType = unionType.getRestrictedUnion(booleanType);
    assertFalse(restrictedBooleanType.isObject());
    assertSame(booleanType, restrictedBooleanType);

    JSType restrictedOtherType = unionType.getRestrictedUnion(NativeType.BOOLEAN);
    assertNull(restrictedOtherType);
  }
}