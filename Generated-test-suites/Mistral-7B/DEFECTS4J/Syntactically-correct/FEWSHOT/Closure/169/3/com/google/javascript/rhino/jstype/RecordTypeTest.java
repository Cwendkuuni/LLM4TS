package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, Node node) {
        throw new RuntimeException(message);
      }
    };

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));

    recordType = new RecordType(registry, properties, true);
  }

  @Test
  public void testIsSynthetic() {
    assertTrue(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    Map<String, RecordProperty> otherProperties = Maps.newHashMap();
    otherProperties.putAll(properties);
    RecordType otherRecord = new RecordType(registry, otherProperties, false);

    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, false));
  }

  @Test
  public void testCheckRecordEquivalenceHelperWithDifferentProperties() {
    Map<String, RecordProperty> otherProperties = Maps.newHashMap();
    otherProperties.put("a", new RecordProperty("a", registry.getNumberType()));
    otherProperties.put("c", new RecordProperty("c", registry.getStringType()));

    RecordType otherRecord = new RecordType(registry, otherProperties, false);

    assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, false));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSType implicitPrototype = recordType.getImplicitPrototype();
    JSType nativeObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertTrue(nativeObjectType.isSubtype(implicitPrototype));
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "c";
    JSType propertyType = registry.getStringType();

    recordType.defineProperty(propertyName, propertyType, false, null);

    assertTrue(recordType.hasProperty(propertyName));
    assertTrue(recordType.getPropertyType(propertyName).isEquivalentTo(propertyType));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefinePropertyWithNullPropertyType() {
    recordType.defineProperty("c", null, false, null);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType otherRecord = new RecordType(registry, properties, false);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherRecord);

    assertTrue(recordType.isSubtype(grearestSubtype));
  }

  @Test
  public void testGetGreatestSubtypeHelperWithIncompatibleProperties() {
    Map<String, RecordProperty> otherProperties = Maps.newHashMap();
    otherProperties.put("a", new RecordProperty("a", registry.getBooleanType()));

    RecordType otherRecord = new RecordType(registry, otherProperties, false);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(otherRecord);

    assertTrue(registry.getNativeObjectType(JSTypeNative.NO_TYPE).isEquivalentTo(greatestSubtype));
  }
}