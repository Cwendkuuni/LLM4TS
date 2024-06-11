import static com.google.javascript.rhino.jstype.JSType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeVisitor;
import com.google.javascript.rhino.NativeType;
import com.google.javascript.rhino.Type;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private JSTypeFactory factory;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
    factory = new JSTypeFactory(registry, errorReporter);
  }

  @Test
  public void testSimpleUnion() {
    Set<JSType> alternates = ImmutableSet.of(intType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);

    // Test basic functionality
    assertEquals("(int, boolean)", unionType.toString());
    assertEquals(2, unionType.getAlternates().size());
    assertFalse(unionType.isNullType());
    assertFalse(unionType.isVoidType());
    assertFalse(unionType.isUnknownType());
    assertFalse(unionType.isObject());
    assertFalse(unionType.isPrimitive());
    assertFalse(unionType.isSubtype(intType()));
    assertFalse(intType().isSubtype(unionType));
    assertFalse(booleanType().isSubtype(unionType));
    assertFalse(unionType.isSubtype(booleanType()));
    assertTrue(unionType.isSubtype(ObjectType.INSTANCE));

    // Test type checking
    assertEquals(intType(), unionType.restrictByNotNullOrUndefined());
    assertEquals(booleanType(), unionType.restrictByNotNullOrUndefined().restrictByNotNullOrUndefined());

    // Test equality
    UnionType equalUnionType = new UnionType(registry, alternates);
    assertTrue(unionType.equals(equalUnionType));
    assertFalse(unionType.equals(intType()));

    // Test type inference
    Type type = factory.compileString("3 + 4");
    assertEquals(unionType, type.getType());

    // Test property lookup
    UnionType propertyUnionType = new UnionType(registry, ImmutableSet.of(StringType.INSTANCE, NumberType.INSTANCE));
    assertEquals(StringType.INSTANCE, propertyUnionType.findPropertyType("toString"));

    // Test assignment
    UnionType assignUnionType = new UnionType(registry, ImmutableSet.of(NumberType.INSTANCE, StringType.INSTANCE));
    assertTrue(assignUnionType.canAssignTo(NumberType.INSTANCE));
    assertTrue(assignUnionType.canAssignTo(StringType.INSTANCE));
    assertFalse(NumberType.INSTANCE.canAssignTo(assignUnionType));
    assertFalse(StringType.INSTANCE.canAssignTo(assignUnionType));

    // Test callability
    UnionType callableUnionType = new UnionType(registry, ImmutableSet.of(FunctionType.INSTANCE, NumberType.INSTANCE));
    assertTrue(callableUnionType.canBeCalled());

    // Test meet
    UnionType meetUnionTypeA = new UnionType(registry, ImmutableSet.of(NumberType.INSTANCE, StringType.INSTANCE));
    UnionType meetUnionTypeB = new UnionType(registry, ImmutableSet.of(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType meetUnionType = meetUnionTypeA.meet(meetUnionTypeB);
    assertEquals(NumberType.INSTANCE, meetUnionType);

    // Test restriction
    UnionType restrictedUnionType = unionType.getRestrictedUnion(intType());
    assertEquals(booleanType(), restrictedUnionType);
  }

  @Test
  public void testEmptyUnion() {
    UnionType emptyUnionType = new UnionType(registry, ImmutableSet.of());
    assertTrue(emptyUnionType.isUnknownType());
  }

  @Test
  public void testNestedUnion() {
    Set<JSType> nestedAlternates = ImmutableSet.of(
        new UnionType(registry, ImmutableSet.of(intType(), booleanType())),
        stringType());
    UnionType nestedUnionType = new UnionType(registry, nestedAlternates);

    // Test equality
    UnionType equalNestedUnionType = new UnionType(registry, nestedAlternates);
    assertTrue(nestedUnionType.equals(equalNestedUnionType));
    assertFalse(nestedUnionType.equals(intType()));

    // Test type checking
    assertEquals(stringType(), nestedUnionType.restrictByNotNullOrUndefined());

    // Test assignment
    assertFalse(intType().canAssignTo(nestedUnionType));
    assertFalse(nestedUnionType.canAssignTo(intType()));
    assertTrue(nestedUnionType.canAssignTo(nestedUnionType));

    // Test callability
    assertFalse(nestedUnionType.canBeCalled());

    // Test meet
    UnionType meetNestedUnionTypeA = new UnionType(registry, ImmutableSet.of(intType(), booleanType()));
    UnionType meetNestedUnionTypeB = nestedUnionType;
    UnionType meetNestedUnionType = meetNestedUnionTypeA.meet(meetNestedUnionTypeB);
    assertEquals(intType(), meetNestedUnionType);

    // Test restriction
    UnionType restrictedNestedUnionType = nestedUnionType.getRestrictedUnion(intType());
    assertEquals(booleanType(), restrictedNestedUnionType);
  }

  @Test
  public void testUnknownUnionType() {
    UnionType unknownUnionType = new UnionType(registry, ImmutableSet.of(UNKNOWN));
    assertTrue(unknownUnionType.isUnknownType());
  }
}