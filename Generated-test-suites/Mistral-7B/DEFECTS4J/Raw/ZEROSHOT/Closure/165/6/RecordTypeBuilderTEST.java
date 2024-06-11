package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;

public class RecordTypeBuilderTest {

  @Test
  public void testEmptyRecordBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();
    assertNotNull(recordType);
    assertNotNull(recordType.getTypeName());
    assertNotNull(recordType.getPrototype());
    assertNull(recordType.getProperties());
    assertNull(recordType.getSuperclass());
    assertNull(recordType.getConstructor());
    JSType nativeObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertNotNull(nativeObjectType);
    assertNotNull(nativeObjectType.getTypeName());
    assertNull(nativeObjectType.getProperties());
    assertSame(nativeObjectType, recordType);
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "name";
    JSType propertyType = registry.getBooleanType();
    Node propertyNode = Node.newIdentifier("name");
    RecordTypeBuilder addedBuilder = builder.addProperty(propertyName, propertyType, propertyNode);

    assertNotNull(addedBuilder);
    Map<String, RecordProperty> properties = builder.properties;
    RecordProperty recordProperty = properties.get(propertyName);
    assertNotNull(recordProperty);
    assertSame(propertyType, recordProperty.getType());
    assertSame(propertyNode, recordProperty.getPropertyNode());

    JSType recordType = builder.build();
    Map<String, JSType> recordProperties = recordType.getProperties();
    JSType recordPropertyValue = recordProperties.get(propertyName);
    assertNotNull(recordPropertyValue);
    assertSame(propertyType, recordPropertyValue);
  }

  @Test(expected = NullPointerException.class)
  public void testAddDuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "name";
    JSType propertyType = registry.getBooleanType();
    Node propertyNode = Node.newIdentifier("name");
    RecordTypeBuilder addedBuilder = builder.addProperty(propertyName, propertyType, propertyNode);
    RecordTypeBuilder addedBuilder2 = builder.addProperty(propertyName, propertyType, propertyNode);
  }

  @Test
  public void testAddPropertyWithNullType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "name";
    Node propertyNode = Node.newIdentifier("name");
    RecordTypeBuilder addedBuilder = builder.addProperty(propertyName, null, propertyNode);
    assertNull(addedBuilder);
  }

  @Test
  public void testAddPropertyWithNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "name";
    JSType propertyType = registry.getBooleanType();
    RecordTypeBuilder addedBuilder = builder.addProperty(propertyName, propertyType, null);
    assertNull(addedBuilder);
  }

  @Test
  public void testAddPropertyWithNullPropertyTypeAndNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "name";
    RecordTypeBuilder addedBuilder = builder.addProperty(propertyName, null, null);
    assertNull(addedBuilder);
  }

  @Test
  public void testAddProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String property1Name = "name1";
    JSType property1Type = registry.getStringType();
    Node property1Node = Node.newIdentifier("name1");
    builder.addProperty(property1Name, property1Type, property1Node);

    String property2Name = "name2";
    JSType property2Type = registry.getNumberType();
    Node property2Node = Node.newIdentifier("name2");
    builder.addProperty(property2Name, property2Type, property2Node);

    Map<String, RecordProperty> properties = builder.properties;
    int propertiesSize = properties.size();
    assertNotNull(properties);
    assertEquivalentMaps(Collections.singletonMap(property1Name, properties.get(property1Name)), Collections.singletonMap(property2Name, properties.get(property2Name)));
    assertSame(propertiesSize, 2);

    JSType recordType = builder.build();
    Map<String, JSType> recordProperties = recordType.getProperties();
    JSType recordProperty1Value = recordProperties.get(property1Name);
    JSType recordProperty2Value = recordProperties.get(property2Name);
    assertNotNull(recordProperty1Value);
    assertSame(property1Type, recordProperty1Value);
    assertNotNull(recordProperty2Value);
    assertSame(property2Type, recordProperty2Value);
  }
}

class AssertEquivalentMaps {
  static boolean assertEquivalentMaps(Map<String, Object> expectedMap, Map<String, Object> actualMap) {
    if (expectedMap.size() != actualMap.size()) {
      return false;
    }

    for (Map.Entry<String, Object> entry : expectedMap.entrySet()) {
      String key = entry.getKey();
      Object expectedValue = entry.getValue();
      Object actualValue = actualMap.get(key);

      if (expectedValue == null) {
        if (actualValue != null) {
          return false;
        }
      } else if (actualValue == null) {
        return false;
      } else if (!expectedValue.equals(actualValue)) {
        return false;
      }
    }

    return true;
  }
}