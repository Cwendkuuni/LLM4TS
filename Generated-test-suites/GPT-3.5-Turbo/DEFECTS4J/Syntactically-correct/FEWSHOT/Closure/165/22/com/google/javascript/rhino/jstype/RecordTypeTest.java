package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
    assertTrue(recordType1.isEquivalentTo(recordType1));

    RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
    assertTrue(recordType1.isEquivalentTo(recordType2));
    assertTrue(recordType2.isEquivalentTo(recordType1));

    Map<String, JSType> properties = Maps.newHashMap();
    properties.put("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    properties.put("property2", registry.getNativeType(JSTypeNative.STRING_TYPE));

    RecordType recordType3 = new RecordType(registry, properties);
    assertFalse(recordType1.isEquivalentTo(recordType3));
    assertFalse(recordType3.isEquivalentTo(recordType1));
    assertTrue(recordType2.isEquivalentTo(recordType3));
    assertTrue(recordType3.isEquivalentTo(recordType2));

    // Test different order of properties
    Map<String, JSType> properties2 = Maps.newHashMap();
    properties2.put("property2", registry.getNativeType(JSTypeNative.STRING_TYPE));
    properties2.put("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    RecordType recordType4 = new RecordType(registry, properties2);
    assertTrue(recordType3.isEquivalentTo(recordType4));
    assertTrue(recordType4.isEquivalentTo(recordType3));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, JSType> properties1 = Maps.newHashMap();
    properties1.put("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    properties1.put("property2", registry.getNativeType(JSTypeNative.STRING_TYPE));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, JSType> properties2 = Maps.newHashMap();
    properties2.put("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    properties2.put("property2", registry.getNativeType(JSTypeNative.STRING_TYPE));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordType2.isSubtype(registry.getNativeType(JSTypeNative.OBJECT_TYPE)));

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));

    assertFalse(recordType2.isSubtype(recordType1));

    Map<String, JSType> properties3 = Maps.newHashMap();
    properties3.put("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    properties3.put("property2", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
    RecordType recordType3 = new RecordType(registry, properties3);

    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType2.isSubtype(recordType3));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap());

    assertTrue(recordType.defineProperty("property1", registry.getNativeType(JSTypeNative.NUMBER_TYPE), true, null));
    assertTrue(recordType.hasProperty("property1"));
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("property1"));

    assertFalse(recordType.defineProperty("property1", registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), true, null));
    assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("property1"));
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    ErrorReporter errorReporter = new ErrorReporter(registry);
    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), resolvedType);
  }

}