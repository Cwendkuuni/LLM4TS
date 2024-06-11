
package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {
  
  private RecordType recordType;
  private JSTypeRegistry registry;
  private Map<String, RecordProperty> properties;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    properties = Maps.newHashMap();
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo_sameRecordType_returnsTrue() {
    RecordType other = new RecordType(registry, properties);
    assertTrue(recordType.isEquivalentTo(other));
  }

  @Test
  public void testIsEquivalentTo_differentRecordType_returnsFalse() {
    Map<String, RecordProperty> otherProperties = Maps.newHashMap();
    otherProperties.put("prop1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
    RecordType other = new RecordType(registry, otherProperties);
    assertFalse(recordType.isEquivalentTo(other));
  }

  @Test
  public void testGetImplicitPrototype_returnsNativeObjectType() {
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertTrue(implicitPrototype instanceof ObjectType);
    assertTrue(implicitPrototype.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testIsSubtype_sameRecordType_returnsTrue() {
    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtype_differentRecordType_returnsFalse() {
    Map<String, RecordProperty> otherProperties = Maps.newHashMap();
    otherProperties.put("prop1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
    RecordType other = new RecordType(registry, otherProperties);
    assertFalse(recordType.isSubtype(other));
  }

  @Test
  public void testIsSubtype_subtypeRecordType_returnsTrue() {
    properties.put("prop1", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE)));
    RecordType subType = new RecordType(registry, properties);
    assertTrue(subType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtype_notSubtypeRecordType_returnsFalse() {
    properties.put("prop1", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
    RecordType subType = new RecordType(registry, properties);
    assertFalse(subType.isSubtype(recordType));
  }

  @Test
  public void testDefineProperty_notInferredAndNotFrozen_returnsTrue() {
    assertTrue(recordType.defineProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
    assertEquals(registry.getNativeType(JSTypeNative.STRING_TYPE), recordType.getPropertyType("prop1"));
  }

  @Test
  public void testDefineProperty_notInferredAndFrozen_returnsFalse() {
    recordType = new RecordType(registry, properties);
    assertFalse(recordType.defineProperty("prop1", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
  }

  @Test
  public void testResolveInternal_resolvesTypes() {
    JSType resolvedType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    properties.put("prop1", new RecordProperty(registry.getNativeUnknownType()) {
      @Override
      JSType resolve(ErrorReporter t, StaticScope<JSType> scope) {
        return resolvedType;
      }
    });
    recordType = new RecordType(registry, properties);
    recordType.resolve(null, null);
    assertEquals(resolvedType, recordType.getPropertyType("prop1"));
  }
}
