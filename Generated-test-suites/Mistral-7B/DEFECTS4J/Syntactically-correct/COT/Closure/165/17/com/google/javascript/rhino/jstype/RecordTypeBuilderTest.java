package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

/**
 * Test cases for RecordTypeBuilder class.
 *
 */
public class RecordTypeBuilderTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddPropertyEmptyRecord() {
    // Given
    RecordType expectedResult = null;

    // When
    JSType result = recordTypeBuilder.build();

    // Then
    assertNull(result);
    assertNotNull(expectedResult);
  }

  @Test
  public void testAddPropertyExistingProperty() {
    // Given
    String propertyName = "propertyName";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);
    RecordProperty existingProperty = new RecordProperty(propertyType, propertyNode);
    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
        .thenReturn(mock(JSType.class));

    // When & Then
    assertNull(recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode));
  }

  @Test
  public void testAddPropertyNewProperty() {
    // Given
    String propertyName = "propertyName";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);
    RecordProperty expectedProperty = new RecordProperty(propertyType, propertyNode);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
        .thenReturn(mock(JSType.class));

    // When
    RecordType expectedResult = new RecordType(registry,
        Collections.singletonMap(propertyName, expectedProperty));
    JSType result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode)
        .build();

    // Then
    assertNotNull(result);
    assertNotNull(expectedResult);
    assertEquals(expectedResult, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullPropertyType() {
    // Given
    String propertyName = "propertyName";
    Node propertyNode = mock(Node.class);

    // When
    recordTypeBuilder.addProperty(propertyName, null, propertyNode);

    // Then
    // Illegal argument exception should be thrown
  }

  @Test
  public void testBuildEmptyRecord() {
    // Given
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
        .thenReturn(mock(JSType.class));

    // When
    JSType result = recordTypeBuilder.build();

    // Then
    assertNotNull(result);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }

  @Test
  public void testBuildRecordWithProperties() {
    // Given
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    RecordProperty property = new RecordProperty(propertyType, propertyNode);
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("propertyName", property);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
        .thenReturn(mock(JSType.class));

    // When
    RecordType expectedResult = new RecordType(registry, properties);
    JSType result = recordTypeBuilder.addProperty("propertyName", propertyType, propertyNode)
        .build();

    // Then
    assertNotNull(result);
    assertNotNull(expectedResult);
    assertEquals(expectedResult, result);
  }
}