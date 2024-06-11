import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Set;
import java.util.HashSet;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.TypePair;
import com.google.javascript.rhino.jstype.UnionType;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = new HashSet<>();
    // Creating a union type with String and boolean alternates
    JSType stringType = new JSType("String");
    JSType booleanType = new JSType("boolean");
    alternates.add(stringType);
    alternates.add(booleanType);

    UnionType unionType = new UnionType(alternates);

    assertNotNull(unionType);
    assertNotNull(unionType.getAlternates());
  }

  @Test
  public void testMatchesNumberContext() {
    // Testing if a union type matching a number context works correctly
    Set<JSType> alternates = new HashSet<>();
    JSType numberType = new JSType("Number");
    JSType booleanType = new JSType("boolean");
    alternates.add(numberType);
    alternates.add(booleanType);

    UnionType unionType = new UnionType(alternates);

    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testFindPropertyType() {
    // Testing if findPropertyType method works correctly
    // Creating a union type with Number and String alternates
    Set<JSType> alternates = new HashSet<>();
    JSType numberType = new JSType("Number");
    JSType stringType = new JSType("String");
    alternates.add(numberType);
    alternates.add(stringType);

    UnionType unionType = new UnionType(alternates);

    JSType propertyType = unionType.findPropertyType("length");

    assertNotNull(propertyType);
    assertEquals("Number", propertyType.getName());
  }

  @Test
  public void testCanAssignTo() {
    // Testing if canAssignTo method works correctly
    // Creating a union type with String and boolean alternates
    Set<JSType> alternates = new HashSet<>();
    JSType stringType = new JSType("String");
    JSType booleanType = new JSType("boolean");
    alternates.add(stringType);
    alternates.add(booleanType);

    UnionType unionType = new UnionType(alternates);

    JSType targetType = new JSType("String");
    boolean result = unionType.canAssignTo(targetType);

    assertTrue(result);
  }

  @Test
  public void testIsNullable() {
    // Testing if isNullable method works correctly
    // Creating a union type with Number and String alternates
    Set<JSType> alternates = new HashSet<>();
    JSType numberType = new JSType("Number");
    JSType stringType = new JSType("String");
    alternates.add(numberType);
    alternates.add(stringType);

    UnionType unionType = new UnionType(alternates);

    assertFalse(unionType.isNullable());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    // Testing if restrictByNotNullOrUndefined method works correctly
    // Creating a union type with Number and String alternates
    Set<JSType> alternates = new HashSet<>();
    JSType numberType = new JSType("Number");
    JSType stringType = new JSType("String");
    alternates.add(numberType);
    alternates.add(stringType);

    UnionType unionType = new UnionType(alternates);

    JSType restrictedType = unionType.restrictByNotNullOrUndefined();

    assertNotNull(restrictedType);
    assertNotEquals(unionType, restrictedType);
  }

  @Test
  public void testTestForEquality() {
    // Testing if testForEquality method works correctly
    // Creating a union type with String and boolean alternates
    Set<JSType> alternates = new HashSet<>();
    JSType stringType = new JSType("String");
    JSType booleanType = new JSType("boolean");
    alternates.add(stringType);
    alternates.add(booleanType);

    UnionType unionType = new UnionType(alternates);

    JSType targetType = new JSType("String");
    TernaryValue result = unionType.testForEquality(targetType);

    assertNotNull(result);
    assertEquals(TernaryValue.UNKNOWN, result);
  }

  @Test
  public void testContains() {
    // Testing if contains method works correctly
    // Creating a union type with Number and String alternates
    Set<JSType> alternates = new HashSet<>();
    JSType numberType = new JSType("Number");
    JSType stringType = new JSType("String");
    alternates.add(numberType);
    alternates.add(stringType);

    UnionType unionType = new UnionType(alternates);

    assertTrue(unionType.contains(numberType));
  }

  @Test
  public void testGetRestrictedUnion() {
    // Testing if getRestrictedUnion method works correctly
    // Creating a union type with Number and String alternates
    Set<JSType> alternates = new HashSet<>();
    JSType numberType = new JSType("Number");
    JSType stringType = new JSType("String");
    alternates.add(numberType);
    alternates.add(stringType);

    UnionType unionType = new UnionType(alternates);

    JSType restrictedType = new JSType("Number");
    JSType result = unionType.getRestrictedUnion(restrictedType);

    assertNotNull(result);
    assertNotEquals(unionType, result);
  }

}