package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TestBase;
import com.google.javascript.rhino.testing.type.RecordType;
import com.google.javascript.rhino.testing.type.RecordTypeBuilder;
import com.google.javascript.rhino.testing.type.RecordTypeRegistry;

public class RecordTypeBuilderTest extends TestBase {

  @Test
  public void testRecordTypeBuilder() {
    RecordTypeRegistry registry = mock(RecordTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    assertNull(recordTypeBuilder.build());
  }

  @Test
  public void testAddProperty() {
    RecordTypeRegistry registry = mock(RecordTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = getTypeOf("String");
    Node propertyNode = getMockNode();

    RecordProperty expectedProperty = new RecordProperty(type, propertyNode);
    RecordProperty actualProperty = recordTypeBuilder.addProperty(name, type, propertyNode);

    assertNotNull(actualProperty);
    assertNotNull(actualProperty.getType());
    assertNotNull(actualProperty.getPropertyNode());
    assertEquals(expectedProperty.getType(), actualProperty.getType());
    assertEquals(expectedProperty.getPropertyNode(), actualProperty.getPropertyNode());

    Map<String, RecordProperty> properties = recordTypeBuilder.properties;
    assertEquals(1, properties.size());
    assertEquals(expectedProperty, properties.get(name));
  }

  @Test
  public void testAddDuplicateProperty() {
    RecordTypeRegistry registry = mock(RecordTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = getTypeOf("String");
    Node propertyNode = getMockNode();

    RecordProperty expectedProperty = new RecordProperty(type, propertyNode);
    RecordProperty actualProperty1 = recordTypeBuilder.addProperty(name, type, propertyNode);
    RecordProperty actualProperty2 = recordTypeBuilder.addProperty(name, type, propertyNode);

    assertNotNull(actualProperty1);
    assertNotNull(actualProperty2);
    assertEquals(expectedProperty, actualProperty1);
    assertEquals(expectedProperty, actualProperty2);

    Map<String, RecordProperty> properties = recordTypeBuilder.properties;
    assertEquals(1, properties.size());
  }

  @Test(expected = NullPointerException.class)
  public void testAddNullPropertyType() {
    RecordTypeRegistry registry = mock(RecordTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    Node propertyNode = getMockNode();
    JSType type = null;

    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test(expected = NullPointerException.class)
  public void testAddNullPropertyNode() {
    RecordTypeRegistry registry = mock(RecordTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name = "name";
    JSType type = getTypeOf("String");
    Node propertyNode = null;

    recordTypeBuilder.addProperty(name, type, propertyNode);
  }

  @Test
  public void testBuildEmptyRecordType() {
    RecordTypeRegistry registry = mock(RecordTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType actualRecordType = recordTypeBuilder.build();

    JSType expectedRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testBuildRecordType() {
    RecordTypeRegistry registry = mock(RecordTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String name1 = "name1";
    String name2 = "name2";
    JSType type1 = getTypeOf("Number");
    JSType type2 = getTypeOf("Boolean");

    RecordProperty property1 = new RecordProperty(type1, getMockNode());
    RecordProperty property2 = new RecordProperty(type2, getMockNode());

    recordTypeBuilder.addProperty(name1, type1, getMockNode());
    recordTypeBuilder.addProperty(name2, type2, getMockNode());

    JSType actualRecordType = recordTypeBuilder.build();
    JSType expectedRecordType = new RecordType(registry, Maps.newHashMap(
        Maps.<String, RecordProperty>of(name1, property1),
        Maps.<String, RecordProperty>of(name2, property2)
    ));

    assertEquals(expectedRecordType.getClass(), actualRecordType.getClass());
    assertEquals(expectedRecordType.getProperties().size(), actualRecordType.getProperties().size());

    Map<String, RecordProperty> expectedProperties = expectedRecordType.getProperties();
    Map<String, RecordProperty> actualProperties = actualRecordType.getProperties();

    assertEquals(expectedProperties.get(name1), actualProperties.get(name1));
    assertEquals(expectedProperties.get(name2), actualProperties.get(name2));
  }

  private Node getMockNode() {
    return mock(Node.class);
  }

  private JSType getTypeOf(String className) {
    return mock(JSType.class, new Class<?>[] {className});
  }
}