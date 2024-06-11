import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UnionTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setUpBeforeClass() throws Exception {
    registry = new JSTypeRegistry();
  }

  @org.junit.Test
  public void testNewUnionType() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getFunctionType(new Class[0], new Class[0]));
    alternates.add(registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals(2, unionType.getAlternates().size());
  }

  @org.junit.Test
  public void testMatchesNumberContext() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getBooleanType());
    alternates.add(registry.getNumberType());
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesNumberContext());

    alternates.clear();
    alternates.add(registry.getNumberType());
    unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesNumberContext());

    alternates.clear();
    alternates.add(registry.getStringType());
    unionType = new UnionType(registry, alternates);
    assertFalse(unionType.matchesNumberContext());
  }

  @org.junit.Test
  public void testMatchesStringContext() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesStringContext());

    alternates.clear();
    alternates.add(registry.getNumberType());
    unionType = new UnionType(registry, alternates);
    assertFalse(unionType.matchesStringContext());
  }

  @org.junit.Test
  public void testMatchesObjectContext() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getFunctionType(new Class[0], new Class[0]));
    UnionType unionType = new UnionType(registry, alternates);
    assertFalse(unionType.matchesObjectContext());

    alternates.clear();
    alternates.add(registry.getObjectType());
    unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesObjectContext());
  }

  @org.junit.Test
  public void testFindPropertyType() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getObjectType());
    alternates.add(registry.getBooleanType());

    UnionType unionType = new UnionType(registry, alternates);
    assertEquals(registry.getObjectType(), unionType.findPropertyType("toString"));

    alternates.clear();
    alternates.add(registry.getStringType());
    alternates.add(registry.getBooleanType());
    unionType = new UnionType(registry, alternates);
    assertNull(unionType.findPropertyType("toString"));
  }

  @org.junit.Test
  public void testCanAssignTo() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getNumberType());
    alternates.add(registry.getStringType());

    UnionType unionType = new UnionType(registry, alternates);
    UnionType other = new UnionType(registry, Arrays.asList(registry.getBooleanType()));
    assertTrue(unionType.canAssignTo(registry.getObjectType()));
    assertTrue(unionType.canAssignTo(other));
    assertTrue(unionType.canAssignTo(registry.getIntType()));
    assertTrue(unionType.canAssignTo(registry.getStringType()));
    assertFalse(unionType.canAssignTo(registry.getBooleanType()));
  }

  @org.junit.Test
  public void testCanBeCalled() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getFunctionType(new Class[0], new Class[0]));
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.canBeCalled());
  }

  @org.junit.Test
  public void testRestrictByNotNullOrUndefined() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    UnionType restricted = unionType.restrictByNotNullOrUndefined();
    assertNotNull(restricted);
    assertEquals(0, restricted.getAlternates().size());
    assertEquals(registry.getNullType(), restricted);

    alternates.clear();
    alternates.add(registry.getNumberType());
    alternates.add(registry.getStringType());
    unionType = new UnionType(registry, alternates);
    restricted = unionType.restrictByNotNullOrUndefined();
    assertNotNull(restricted);
    assertEquals(1, restricted.getAlternates().size());
    assertEquals(registry.getVoidType(), restricted);
  }

  @org.junit.Test
  public void testTestForEquality() {
    Set<JSType> alternates1 = new HashSet<>();
    alternates1.add(registry.getStringType());
    UnionType unionType1 = new UnionType(registry, alternates1);

    Set<JSType> alternates2 = new HashSet<>();
    alternates2.add(registry.getBooleanType());
    UnionType unionType2 = new UnionType(registry, alternates2);

    assertEquals(UNKNOWN, unionType1.testForEquality(unionType2));
    assertEquals(UNKNOWN, unionType1.testForEquality(unionType1));
    assertEquals(registry.getBooleanType(), unionType1.testForEquality(registry.getBooleanType()));
    assertEquals(registry.getStringType(), unionType1.testForEquality(registry.getStringType()));
  }

  @org.junit.Test
  public void testIsNullable() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getNumberType());
    UnionType unionType = new UnionType(registry, alternates);
    assertFalse(unionType.isNullable());

    alternates.clear();
    alternatives.add(registry.getNullType());
    unionType = new UnionType(registry, alternatives);
    assertTrue(unionType.isNullable());
  }

  @org.junit.Test
  public void testIsUnknownType() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertFalse(unionType.isUnknownType());

    alternates.clear();
    alternatives.add(registry.getUnknownType());
    unionType = new UnionType(registry, alternatives);
    assertTrue(unionType.isUnknownType());
  }

  @org.junit.Test
  public void testGetLeastSupertype() {
    Set<JSType> alternates1 = new HashSet<>();
    alternates1.add(registry.getStringType());
    UnionType unionType1 = new UnionType(registry, alternates1);

    Set<JSType> alternates2 = new HashSet<>();
    alternates2.add(registry.getBooleanType());
    UnionType unionType2 = new UnionType(registry, alternates2);

    assertEquals(registry.getObjectType(), unionType1.getLeastSupertype(unionType2));
    assertEquals(unionType1, unionType1.getLeastSupertype(unionType1));
  }

  @org.junit.Test
  public void testMeet() {
    Set<JSType> alternates1 = new HashSet<>();
    alternates1.add(registry.getStringType());
    UnionType unionType1 = new UnionType(registry, alternates1);

    Set<JSType> alternates2 = new HashSet<>();
    alternates2.add(registry.getBooleanType());
    UnionType unionType2 = new UnionType(registry, alternates2);

    UnionType result = unionType1.meet(unionType2);
    assertNull(result);

    alternates1.clear();
    alternates1.add(registry.getFunctionType(new Class[0], new Class[0]));
    unionType1 = new UnionType(registry, alternates1);

    alternates2.clear();
    alternates2.add(registry.getNumberType());
    unionType2 = new UnionType(registry, alternates2);

    result = unionType1.meet(unionType2);
    assertEquals(registry.getObjectType(), result);

    unionType1 = new UnionType(registry, Arrays.asList(registry.getFunctionType(new Class[0], new Class[0]), registry.getStringType()));
    unionType2 = new UnionType(registry, Arrays.asList(registry.getBooleanType(), registry.getStringType()));

    result = unionType1.meet(unionType2);
    assertEquals(registry.getStringType(), result);
  }

  @org.junit.Test
  public void testEquals() {
    Set<JSType> alternates1 = new HashSet<>();
    alternates1.add(registry.getStringType());
    UnionType unionType1 = new UnionType(registry, alternates1);

    Set<JSType> alternates2 = new HashSet<>();
    alternates2.add(registry.getStringType());
    UnionType unionType2 = new UnionType(registry, alternates2);

    assertTrue(unionType1.equals(unionType2));

    alternates1.clear();
    alternates1.add(registry.getStringType());
    alternates1.add(registry.getBooleanType());
    unionType1 = new UnionType(registry, alternates1);

    alternates2.clear();
    alternates2.add(registry.getStringType());
    alternates2.add(registry.getObjectType());
    unionType2 = new UnionType(registry, alternates2);

    assertFalse(unionType1.equals(unionType2));
  }
}