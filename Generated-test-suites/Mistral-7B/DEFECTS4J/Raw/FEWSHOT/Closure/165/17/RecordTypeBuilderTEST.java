package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    builder = new RecordTypeBuilder(registry);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testAddPropertyAndBuildEmptyRecord() {
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);
    builder.addProperty("name", type, propertyNode);
    JSType emptyRecord = builder.build();
    assertThat(emptyRecord).isInstanceOf(JSTypeNative.OBJECT_TYPE);
  }

  @Test
  public void testAddTwoPropertiesAndBuildRecord() {
    JSType type1 = mock(JSType.class);
    Node propertyNode1 = mock(Node.class);
    JSType type2 = mock(JSType.class);
    Node propertyNode2 = mock(Node.class);

    builder.addProperty("name1", type1, propertyNode1);
    builder.addProperty("name2", type2, propertyNode2);

    Map<String, RecordProperty> properties = builder.build().getProperties();

    assertThat(properties.size()).isEqualTo(2);
    RecordProperty recordProperty1 = properties.get("name1");
    RecordProperty recordProperty2 = properties.get("name2");

    assertThat(recordProperty1.getType()).isEqualTo(type1);
    assertThat(recordProperty2.getType()).isEqualTo(type2);
  }

  @Test
  public void testAddDuplicateProperty() {
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    builder.addProperty("name", type, propertyNode);
    RecordTypeBuilder builder2 = new RecordTypeBuilder(registry);
    RecordProperty duplicateProperty = builder2.addProperty("name", type, propertyNode);

    assertThat(duplicateProperty).isNull();
  }
}