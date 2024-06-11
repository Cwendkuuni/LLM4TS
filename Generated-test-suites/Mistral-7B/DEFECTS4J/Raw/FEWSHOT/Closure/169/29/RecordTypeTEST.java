package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {
  private static final ErrorReporter REPORTER = mock(ErrorReporter.class);
  private JSTypeRegistry registry;
  private RecordType typeA;
  private RecordType typeB;
  private RecordType typeC;
  private RecordType typeD;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.getStringType()));
    propertiesA.put("y", new RecordProperty("y", registry.getNumberType()));
    typeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.getStringType()));
    propertiesB.put("y", new RecordProperty("y", registry.getNumberType()));
    propertiesB.put("z", new RecordProperty("z", registry.getNumberType()));
    typeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("x", new RecordProperty("x", registry.getStringType()));
    propertiesC.put("z", new RecordProperty("z", registry.getNumberType()));
    typeC = new RecordType(registry, propertiesC, true);

    typeD = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
  }

  @Test
  public void testIsSynthetic() {
    assertThat(typeA.isSynthetic()).isFalse();
    assertThat(typeB.isSynthetic()).isFalse();
    assertThat(typeC.isSynthetic()).isFalse();
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    assertThat(typeA.checkRecordEquivalenceHelper(typeA, true)).isTrue();
    assertThat(typeA.checkRecordEquivalenceHelper(typeB, true)).isTrue();
    assertThat(typeB.checkRecordEquivalenceHelper(typeB, true)).isTrue();
    assertThat(typeA.checkRecordEquivalenceHelper(typeC, true)).isFalse();
  }

  @Test
  public void testGetImplicitPrototype() {
    assertThat(typeA.getImplicitPrototype()).isEqualTo(typeD);
  }

  @Test
  public void testDefineProperty() {
    String propertyE = "e";
    JSType typeE = registry.getNumberType();

    typeA.defineProperty(propertyE, typeE, false, null);
    assertThat(typeA.hasProperty(propertyE)).isTrue();
    assertThat(typeA.getPropertyType(propertyE)).isEqualTo(typeE);
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyFrozen() {
    typeA.defineProperty("x", registry.getStringType(), false, null);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType typeE = registry.getNumberType();
    JSType typeF = registry.getNativeType(JSTypeNative.NO_TYPE);

    JSType greatestSubtype = typeA.getGreatestSubtypeHelper(typeF);
    assertThat(greatestSubtype).isEqualTo(typeF);

    greatestSubtype = typeA.getGreatestSubtypeHelper(typeE);
    greatestSubtype = greatestSubtype.getGreatestSubtype(typeE);
    assertThat(greatestSubtype).isEqualTo(typeE);

    greatestSubtype = typeB.getGreatestSubtypeHelper(typeE);
    greatestSubtype = greatestSubtype.getLeastSupertype(new UnionTypeBuilder(registry)
        .addAlternate(typeA)
        .addAlternate(typeD)
        .build());
    assertThat(greatestSubtype).isEqualTo(typeB);
  }

  @Test
  public void testIsSubtype() {
    // typeA is a subtype of typeB
    assertThat(typeA.isSubtype(typeB)).isTrue();

    // typeB is a subtype of typeA
    assertThat(typeB.isSubtype(typeA)).isTrue();

    // typeA is not a subtype of itself
    assertThat(typeA.isSubtype(typeA)).isFalse();

    // typeA is not a subtype of typeC
    assertThat(typeA.isSubtype(typeC)).isFalse();

    // typeD is a subtype of typeB
    assertThat(typeD.isSubtype(typeB)).isTrue();

    // typeB is not a subtype of typeD
    // (since it's a subtype of Object)
    assertThat(typeB.isSubtype(typeD)).isFalse();

    // typeD is not a subtype of typeA
    assertThat(typeD.isSubtype(typeA)).isFalse();

    // typeD is a subtype of typeC
    // (since it's a supertype of Object)
    assertThat(typeD.isSubtype(typeC)).isTrue();

    // Record types are not subtypes of primitives
    JSType numberType = registry.getNumberType();
    JSType booleanType = registry.getBooleanType();
    JSType stringType = registry.getStringType();

    // typeA is not a subtype of numberType
    assertThat(typeA.isSubtype(numberType)).isFalse();

    // numberType is not a subtype of typeA
    assertThat(numberType.isSubtype(typeA)).isFalse();

    // typeA is not a subtype of booleanType
    assertThat(typeA.isSubtype(booleanType)).isFalse();

    // booleanType is not a subtype of typeA
    assertThat(booleanType.isSubtype(typeA)).isFalse();

    // typeA is not a subtype of stringType
    assertThat(typeA.isSubtype(stringType)).isFalse();

    // stringType is not a subtype of typeA
    assertThat(stringType.isSubtype(typeA)).isFalse();
  }

  @Test
  public void testToString() {
    String expected = "{x: string, y: number}";
    String actual = typeA.toString();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testResolveInternal() {
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();
    StaticScope<JSType> scope = mock(StaticScope.class);

    when(scope.getVariable("x")).thenReturn(stringType);
    when(scope.getVariable("y")).thenReturn(numberType);

    typeA.resolveInternal(REPORTER, scope);
  }

  @Test
  public void testUnsupportedOperation() {
    ExpectedException exception = ExpectedException.none();

    exception.expect(UnsupportedOperationException.class);
    typeA.getPropertyNodes();
  }
}