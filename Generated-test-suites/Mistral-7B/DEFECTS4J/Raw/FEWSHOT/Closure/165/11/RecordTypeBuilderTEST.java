package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testEmptyRecord() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    JSType result = recordTypeBuilder.build();

    // An empty record should return the Object type
    assertThat(result).isEqualTo(mock(JSTypeNative.ObjectType.class));
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String propertyName = "name";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    RecordTypeBuilder builder = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

    Map<String, RecordProperty> properties = recordTypeBuilder.properties;
    assertThat(properties).hasSize(1);
    RecordProperty recordProperty = properties.get(propertyName);
    assertThat(recordProperty).isNotNull();
    assertThat(recordProperty.getType()).isSameInstanceAs(propertyType);
    assertThat(recordProperty.getPropertyNode()).isSameInstanceAs(propertyNode);

    // Test the builder chaining
    assertThat(builder).isSameInstanceAs(recordTypeBuilder);
  }

  @Test
  public void testAddDuplicateProperty() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String propertyName = "name";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    RecordTypeBuilder builder1 = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

    // Adding the same property again should return null
    RecordTypeBuilder builder2 = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

    assertThat(builder2).isNull();
  }

  @Test
  public void testBuildRecordType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String propertyName1 = "name1";
    String propertyName2 = "name2";
    JSType propertyType1 = mock(JSType.class);
    JSType propertyType2 = mock(JSType.class);
    Node propertyNode1 = mock(Node.class);
    Node propertyNode2 = mock(Node.class);

    RecordTypeBuilder builder = recordTypeBuilder
        .addProperty(propertyName1, propertyType1, propertyNode1)
        .addProperty(propertyName2, propertyType2, propertyNode2);

    JSType result = builder.build();

    Map<String, RecordProperty> properties = ((RecordType) result).getProperties();
    assertThat(properties).hasSize(2);
    RecordProperty recordProperty1 = properties.get(propertyName1);
    RecordProperty recordProperty2 = properties.get(propertyName2);

    assertThat(recordProperty1).isNotNull();
    assertThat(recordProperty1.getType()).isSameInstanceAs(propertyType1);
    assertThat(recordProperty1.getPropertyNode()).isSameInstanceAs(propertyNode1);

    assertThat(recordProperty2).isNotNull();
    assertThat(recordProperty2.getType()).isSameInstanceAs(propertyType2);
    assertThat(recordProperty2.getPropertyNode()).isSameInstanceAs(propertyNode2);
  }
}