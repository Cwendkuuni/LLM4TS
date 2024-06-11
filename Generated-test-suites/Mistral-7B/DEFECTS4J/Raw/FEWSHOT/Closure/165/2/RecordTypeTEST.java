package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JsContext;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {
  private static final ErrorReporter REPORTER = new ErrorReporter() {
    @Override
    public void error(String message, int line, int lineNum, int column) {}

    @Override
    public void warning(String message, int line, int lineNum, int column) {}
  };

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));
    properties.put("b", new RecordProperty("b", registry.numberType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(RecordType.class, recordType.getClass());
    assertEquals(registry, recordType.registry);
    assertSame(properties, recordType.properties);
  }

  @Test
  public void testCreateRecordTypeWithEmptyProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, properties);
    assertTrue(recordType.properties.isEmpty());
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.stringType()));
    properties1.put("b", new RecordProperty("b", registry.numberType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.stringType()));
    properties2.put("b", new RecordProperty("b", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", registry.stringType()));
    properties3.put("c", new RecordProperty("c", registry.numberType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    assertEquals(recordType1, recordType1);
    assertEquals(recordType1, recordType2);
    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, properties);

    JSType propertyType = registry.booleanType();
    Node propertyNode = JsContext.newJsFunction(new Object[0], null);

    String propertyName = "property";
    boolean result = recordType.defineProperty(
        propertyName, propertyType, false, propertyNode);

    assertTrue(result);
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(propertyType, recordType.getPropertyType(propertyName));
  }

  @Test
  public void testDefineProperty_isFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, properties);

    JSType propertyType = registry.booleanType();
    Node propertyNode = JsContext.newJsFunction(new Object[0], null);

    String propertyName = "property";
    boolean result = recordType.defineProperty(
        propertyName, propertyType, false, propertyNode);

    assertTrue(result);

    recordType.isFrozen = true;

    result = recordType.defineProperty(
        propertyName, propertyType, false, propertyNode);

    assertFalse(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.stringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.numberType()));
    properties2.put("b", new RecordProperty("b", registry.booleanType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", registry.stringType()));
    properties3.put("b", new RecordProperty("b", registry.numberType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType3, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType3, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.stringType());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.stringType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.numberType()));
    properties2.put("b", new RecordProperty("b", registry.booleanType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", registry.numberType()));
    properties3.put("b", new RecordProperty("b", registry.numberType()));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("a", new RecordProperty("a", registry.booleanType()));
    properties4.put("b", new RecordProperty("b", registry.numberType()));
    RecordType recordType4 = new RecordType(registry, properties4);

    Map<String, RecordProperty> properties5 = Maps.newHashMap();
    properties5.put("c", new RecordProperty("c", registry.numberType()));
    properties5.put("a", new RecordProperty("a", registry.numberType()));
    RecordType recordType5 = new RecordType(registry, properties5);

    Map<String, RecordProperty> properties6 = Maps.newHashMap();
    properties6.put("d", new RecordProperty("d", registry.numberType()));
    properties6.put("a", new RecordProperty("a", registry.booleanType()));
    RecordType recordType6 = new RecordType(registry, properties6);

    Map<String, RecordProperty> properties7 = Maps.newHashMap();
    properties7.put("d", new RecordProperty("d", registry.numberType()));
    properties7.put("a", new RecordProperty("a", registry.booleanType()));
    RecordType recordType7 = new RecordType(registry, properties7);

    Map<String, RecordProperty> properties8 = Maps.newHashMap();
    properties8.put("d", new RecordProperty("d", registry.numberType()));
    properties8.put("c", new RecordProperty("c", registry.numberType()));
    RecordType recordType8 = new RecordType(registry, properties8);

    assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    assertTrue(recordType1.isSubtype(recordType1));

    assertTrue(recordType1.isSubtype(recordType2));

    assertTrue(recordType2.isSubtype(recordType3));

    assertTrue(recordType3.isSubtype(recordType4));

    assertFalse(recordType1.isSubtype(recordType5));

    assertFalse(recordType1.isSubtype(recordType6));

    assertFalse(recordType1.isSubtype(recordType7));

    assertFalse(recordType1.isSubtype(recordType8));

    assertTrue(recordType3.isSubtype(recordType4));

    assertTrue(recordType3.isSubtype(recordType5));

    assertTrue(recordType3.isSubtype(recordType6));

    assertTrue(recordType3.isSubtype(recordType7));

    assertFalse(recordType3.isSubtype(recordType8));

    assertTrue(recordType4.isSubtype(recordType5));

    assertTrue(recordType4.isSubtype(recordType6));

    assertTrue(recordType4.isSubtype(recordType7));

    assertTrue(recordType4.isSubtype(recordType8));

    assertFalse(recordType5.isSubtype(recordType6));

    assertFalse(recordType5.isSubtype(recordType7));

    assertFalse(recordType5.isSubtype(recordType8));

    assertFalse(recordType6.isSubtype(recordType7));

    assertFalse(recordType6.isSubtype(recordType8));

    assertFalse(recordType7.isSubtype(recordType8));
  }

  @Test
  public void testIsSubtype_recordTypesWithSamePropertiesButDifferentTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.booleanType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));

    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtype_recordTypeWithNullProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.booleanType()));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = null;
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));

    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testGetPropertyType_nonExistentProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertNull(recordType.getPropertyType("nonExistentProperty"));
  }

  @Test
  public void testGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.stringType(), recordType.getPropertyType("a"));
  }

  @Test
  public void testGetPropertyType_recordTypeWithEmptyProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newHashMap());

    assertNull(recordType.getPropertyType("a"));
  }

  @Test
  public void testGetPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()) {
      @Override
      public Node getPropertyNode() {
        return JsContext.newJsFunction(new Object[0], null);
      }
    });
    RecordType recordType = new RecordType(registry, properties);

    Node propertyNode = recordType.getPropertyNode("a");

    assertNotNull(propertyNode);
  }

  @Test
  public void testHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.hasProperty("a"));

    Map<String, RecordProperty> emptyProperties = Maps.newHashMap();
    RecordType emptyRecordType = new RecordType(registry, emptyProperties);

    assertFalse(emptyRecordType.hasProperty("a"));
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.stringType()));
    RecordType recordType = new RecordType(registry, properties);

    String toString = recordType.toString();

    String expected = "{type=%s, properties=%s}";
    String expectedFormatted = String.format(expected, "RecordType", properties);

    assertEquals(expectedFormatted, toString);
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.numberType()));
    RecordType recordType = new RecordType(registry, properties);

    JSType resolvedType = recordType.resolveInternal(REPORTER, null);

    Map<String, JSType> resolvedProperties = resolvedType.toMaybeRecordType().properties;

    assertEquals(properties.get("a").getType(), resolvedProperties.get("a"));
  }
}