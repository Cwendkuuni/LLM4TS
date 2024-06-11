package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;
  private RecordType expectedRecordType;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    expectedRecordType = mock(RecordType.class);
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testEmptyRecordType() {
    JSType actualRecordType = builder.build();
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testAddSingleProperty() {
    String propertyName = "name";
    JSType propertyType = JSType.STRING;
    Node propertyNode = mock(Node.class);
    builder.addProperty(propertyName, propertyType, propertyNode);
    RecordProperty expectedProperty = new RecordProperty(propertyType, propertyNode);
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(expectedRecordType);
    when(expectedRecordType.getProperties()).thenReturn(Collections.singletonMap(propertyName, expectedProperty));
    JSType actualRecordType = builder.build();
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testAddDuplicateProperty() {
    String propertyName = "name";
    JSType propertyType = JSType.STRING;
    Node propertyNode = mock(Node.class);
    builder.addProperty(propertyName, propertyType, propertyNode);
    assertNull(builder.addProperty(propertyName, propertyType, propertyNode));
  }

  @Test
  public void testAddMultipleProperties() {
    String propertyName1 = "name1";
    JSType propertyType1 = JSType.NUMBER;
    Node propertyNode1 = mock(Node.class);
    String propertyName2 = "name2";
    JSType propertyType2 = JSType.BOOLEAN;
    Node propertyNode2 = mock(Node.class);

    builder.addProperty(propertyName1, propertyType1, propertyNode1);
    builder.addProperty(propertyName2, propertyType2, propertyNode2);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(expectedRecordType);
    HashMap<String, RecordProperty> expectedProperties = Maps.newHashMap();
    expectedProperties.put(propertyName1, new RecordProperty(propertyType1, propertyNode1));
    expectedProperties.put(propertyName2, new RecordProperty(propertyType2, propertyNode2));
    when(expectedRecordType.getProperties()).thenReturn(expectedProperties);

    JSType actualRecordType = builder.build();
    assertEquals(expectedRecordType, actualRecordType);
  }
}