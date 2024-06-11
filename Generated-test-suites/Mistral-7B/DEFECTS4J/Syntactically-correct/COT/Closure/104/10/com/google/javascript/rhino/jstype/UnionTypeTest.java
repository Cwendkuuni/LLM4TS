import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Set;
import java.util.TreeSet;

public class UnionTypeTest {

  private static final ErrorReporter ERROR_REPORTER = mock(ErrorReporter.class);
  private JSTypeRegistry registry;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    registry = new JSTypeRegistry() {
      @Override
      public JSType getNativeType(JSTypeNative type) {
        return null;
      }
    };
  }

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.BOOLEAN));
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals("(String|Boolean)", unionType.toString());
  }

  @Test
  public void testGetAlternates() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.BOOLEAN));
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals(alternates, unionType.getAlternates());
  }

  @Test
  public void testForgiveUnknownNames() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.BOOLEAN));
    UnionType unionType = new UnionType(registry, alternates);

    verify(ERROR_REPORTER, times(2)).forgiveUnknownName("String", anyString());
    verify(ERROR_REPORTER, times(2)).forgiveUnknownName("Boolean", anyString());
    unionType.forgiveUnknownNames();

    verifyNoMoreInteractions(ERROR_REPORTER);
  }

  @Test
  public void testMatchesNumberContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.BOOLEAN));
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.BOOLEAN));
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.NULL), registry.getNativeType(JSTypeNative.OBJECT));
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    Set<JSType> alternates = ImmutableSet.of(
        new StringType(registry),
        new NumberType(registry),
        new BooleanType(registry)
    );
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals(registry.getNativeType(JSTypeNative.STRING), unionType.findPropertyType("toString"));
  }

  @Test
  public void testCanAssignTo() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING));
    UnionType unionType = new UnionType(registry, alternates);

    JSType that = registry.getNativeType(JSTypeNative.STRING);
    assertTrue(unionType.canAssignTo(that));
  }

  @Test
  public void testCanBeCalled() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.BOOLEAN));
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.NULL), registry.getNativeType(JSTypeNative.NUMBER));
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals(registry.getNativeType(JSTypeNative.NULL), unionType.restrictByNotNullOrUndefined());
  }

  @Test(expected = RuntimeException.class)
  public void testTestForEqualityWithNull() {
    Set<JSType> stringAlternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING));
    UnionType stringUnionType = new UnionType(registry, stringAlternates);

    JSType nullType = registry.getNativeType(JSTypeNative.NULL);
    UnionType nullUnionType = new UnionType(registry, ImmutableSet.of(nullType));

    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringUnionType, nullUnionType));

    JSType that = null;
    assertEquals(UNKNOWN, unionType.testForEquality(that));
  }

  @Test
  public void testMeet() {
    Set<JSType> intAlternates = ImmutableSet.of(new NumberType(registry, ImmutableSet.of(JSTypeNative.INT)));
    Set<JSType> stringAlternates = ImmutableSet.of(new StringType(registry));

    UnionType intUnionType = new UnionType(registry, intAlternates);
    UnionType stringUnionType = new UnionType(registry, stringAlternates);

    UnionType result = intUnionType.meet(stringUnionType);
    assertNotNull(result);
    assertTrue(result.isObject());
  }

  @Test
  public void testEquals() {
    Set<JSType> alternates1 = ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.BOOLEAN));
    UnionType unionType1 = new UnionType(registry, alternates1);

    Set<JSType> alternates2 = ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.BOOLEAN));
    UnionType unionType2 = new UnionType(registry, alternates2);

    Set<JSType> alternates3 = ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.NUMBER));
    UnionType unionType3 = new UnionType(registry, alternates3);

    assertTrue(unionType1.equals(unionType1));
    assertFalse(unionType1.equals(unionType2));
    assertFalse(unionType1.equals(unionType3));
  }

  @Test
  public void testIsNullable() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.NULL), registry.getNativeType(JSTypeNative.NUMBER));
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.NULL), registry.getNativeType(JSTypeNative.NUMBER));
    UnionType unionType = new UnionType(registry, alternates);
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    Set<JSType> alternates1 = ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING));
    UnionType unionType1 = new UnionType(registry, alternates1);

    Set<JSType> alternates2 = ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.BOOLEAN));
    UnionType unionType2 = new UnionType(registry, alternates2);

    UnionType result = unionType1.getLeastSupertype(unionType2);
    assertEquals(unionType1, result);
  }
}