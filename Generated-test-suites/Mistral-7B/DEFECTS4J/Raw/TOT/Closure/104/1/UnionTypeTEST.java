import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.VoidType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {

  private JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testUnionTypeBasic() {
    Set<JSType> alternates = new HashSet<JSType>();
    JSType intType = registry.findOrCreatePrimitiveType("int");
    JSType booleanType = registry.findOrCreatePrimitiveType("boolean");
    alternates.add(intType);
    alternates.add(booleanType);
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(int|boolean)", unionType.toString());

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());

    assertEquals(intType, unionType.findPropertyType("length"));
    assertEquals(booleanType, unionType.findPropertyType("toString"));

    JSType intUnion = new UnionType(registry, Arrays.asList(intType));
    assertEquals(intUnion, unionType.getLeastSupertype(intUnion));

    UnionType stringUnion = new UnionType(registry, Arrays.asList(booleanType, intType));
    assertEquals(stringUnion, unionType.getLeastSupertype(stringUnion));

    assertTrue(intType.canAssignTo(unionType));
    assertTrue(booleanType.canAssignTo(unionType));
    assertFalse(intType.canAssignTo(booleanType));

    assertEquals(unionType, unionType.restrictByNotNullOrUndefined());

    UnionType intStringUnion = new UnionType(registry, Arrays.asList(intType, booleanType));
    assertEquals(unionType, intStringUnion.meet(unionType));

    assertEquals(unionType.getAlternates(), intStringUnion.getAlternates());

    assertEquals(unionType, unionType.testForEquality(unionType));
    assertFalse(unionType.testForEquality(new VoidType(registry)));
  }

  @Test
  public void testUnionTypeWithUnknownType() {
    Set<JSType> alternates = new HashSet<JSType>();
    JSType intType = registry.findOrCreatePrimitiveType("int");
    JSType booleanType = registry.findOrCreatePrimitiveType("boolean");
    JSType unknownType = new UnknownType(registry);
    alternates.add(intType);
    alternates.add(booleanType);
    alternates.add(unknownType);
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(int|boolean|Unknown)", unionType.toString());

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());

    assertFalse(unionType.findPropertyType("toString") instanceof StringType);

    assertTrue(intType.canAssignTo(unionType));
    assertTrue(booleanType.canAssignTo(unionType));
    assertTrue(unknownType.canAssignTo(unionType));

    assertEquals(unionType, unionType.restrictByNotNullOrUndefined());

    UnionType intStringUnknownUnion = new UnionType(registry, Arrays.asList(intType, booleanType, unknownType));
    assertEquals(unionType, intStringUnknownUnion.meet(unionType));

    assertEquals(unionType.getAlternates(), intStringUnknownUnion.getAlternates());

    assertEquals(unionType, unionType.testForEquality(unionType));
    assertFalse(unionType.testForEquality(new VoidType(registry)));
  }

  @Test
  public void testUnionTypeWithSubtypes() {
    Set<JSType> alternates = new HashSet<JSType>();
    JSType intType = registry.findOrCreatePrimitiveType("int");
    JSType numberType = registry.findOrCreatePrimitiveType("number");
    JSType booleanType = registry.findOrCreatePrimitiveType("boolean");
    alternates.add(intType);
    alternates.add(numberType);
    alternates.add(booleanType);
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(int|number|boolean)", unionType.toString());

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());

    assertEquals(intType, unionType.getLeastSupertype(intType));
    assertEquals(numberType, unionType.getLeastSupertype(numberType));

    assertFalse(intType.isSubtype(numberType));
    assertFalse(numberType.isSubtype(intType));

    assertTrue(intType.isSubtype(unionType));
    assertTrue(numberType.isSubtype(unionType));
    assertTrue(booleanType.isSubtype(unionType));

    assertEquals(unionType, unionType.getLeastSupertype(unionType));

    UnionType intNumberUnion = new UnionType(registry, Arrays.asList(intType, numberType));
    assertEquals(unionType, intNumberUnion.meet(unionType));

    assertEquals(unionType.getAlternates(), intNumberUnion.getAlternates());

    assertEquals(unionType, unionType.testForEquality(unionType));
    assertFalse(unionType.testForEquality(new VoidType(registry)));
  }

  @Test
  public void testUnionTypeWithNull() {
    Set<JSType> alternates = new HashSet<JSType>();
    JSType nullType = new NullType(registry);
    JSType intType = registry.findOrCreatePrimitiveType("int");
    alternates.add(intType);
    alternates.add(nullType);
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(int|null)", unionType.toString());

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertFalse(unionType.matchesObjectContext());

    assertEquals(intType, unionType.getLeastSupertype(intType));
    assertEquals(nullType, unionType.getLeastSupertype(nullType));

    assertFalse(intType.isSubtype(nullType));
    assertFalse(nullType.isSubtype(intType));

    assertTrue(intType.isSubtype(unionType));
    assertTrue(nullType.isSubtype(unionType));

    assertEquals(unionType, unionType.getLeastSupertype(unionType));

    UnionType intNullUnion = new UnionType(registry, Arrays.asList(intType, nullType));
    assertEquals(unionType, intNullUnion.meet(unionType));

    assertEquals(unionType.getAlternates(), intNullUnion.getAlternates());

    assertEquals(unionType, unionType.testForEquality(unionType));
    assertFalse(unionType.testForEquality(new VoidType(registry)));
  }

  @Test
  public void testUnionTypeWithVoid() {
    Set<JSType> alternates = new HashSet<JSType>();
    JSType intType = registry.findOrCreatePrimitiveType("int");
    JSType voidType = new VoidType(registry);
    alternates.add(intType);
    alternates.add(voidType);
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(int|void)", unionType.toString());

    assertFalse(unionType.matchesNumberContext());
    assertFalse(unionType.matchesStringContext());
    assertFalse(unionType.matchesObjectContext());

    assertEquals(intType, unionType.getLeastSupertype(intType));
    assertEquals(voidType, unionType.getLeastSupertype(voidType));

    assertFalse(intType.isSubtype(voidType));
    assertFalse(voidType.isSubtype(intType));

    assertFalse(intType.isSubtype(unionType));
    assertFalse(voidType.isSubtype(unionType));

    assertEquals(unionType, unionType.getLeastSupertype(unionType));

    UnionType intVoidUnion = new UnionType(registry, Arrays.asList(intType, voidType));
    assertEquals(unionType, intVoidUnion.meet(unionType));

    assertEquals(unionType.getAlternates(), intVoidUnion.getAlternates());

    assertFalse(unionType.testForEquality(unionType));
    assertFalse(unionType.testForEquality(new VoidType(registry)));
  }

  @Test
  public void testUnionTypeWithSameType() {
    Set<JSType> alternates = new HashSet<JSType>();
    JSType intType = registry.findOrCreatePrimitiveType("int");
    alternates.add(intType);
    alternates.add(intType);
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(int|int)", unionType.toString());

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());

    assertEquals(intType, unionType.getLeastSupertype(intType));
    assertEquals(intType, unionType.getLeastSupertype(intType));

    assertTrue(intType.isSubtype(unionType));
    assertTrue(intType.isSubtype(unionType));

    assertEquals(unionType, unionType.getLeastSupertype(unionType));

    UnionType intIntUnion = new UnionType(registry, Arrays.asList(intType, intType));
    assertEquals(unionType, intIntUnion.meet(unionType));

    assertEquals(unionType.getAlternates(), intIntUnion.getAlternates());

    assertEquals(unionType, unionType.testForEquality(unionType));
    assertFalse(unionType.testForEquality(new VoidType(registry)));
  }
}