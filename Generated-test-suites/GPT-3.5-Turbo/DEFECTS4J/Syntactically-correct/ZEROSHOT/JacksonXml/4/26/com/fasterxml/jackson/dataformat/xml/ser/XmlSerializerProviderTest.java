package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

  @Test
  public void testConstructor_withXmlRootNameLookup() {
    // Arrange
    XmlRootNameLookup rootNames = new XmlRootNameLookup();

    // Act
    XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);

    // Assert
    assertNotNull(provider);
  }

  @Test
  public void testConstructor_withSerializationConfigAndSerializerFactoryNotNull() {
    // Arrange
    XmlRootNameLookup rootNames = new XmlRootNameLookup();
    SerializationConfig config = new SerializationConfig();
    SerializerFactory f = new SerializerFactory();

    // Act
    XmlSerializerProvider provider = new XmlSerializerProvider(null, config, f);

    // Assert
    assertNotNull(provider);
  }

  @Test
  public void testCreateInstance_withSerializationConfigAndSerializerFactoryNotNull() {
    // Arrange
    SerializationConfig config = new SerializationConfig();
    SerializerFactory f = new SerializerFactory();
    XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(), null, null);

    // Act
    DefaultSerializerProvider instance = provider.createInstance(config, f);

    // Assert
    assertNotNull(instance);
  }

  @Test
  public void testSerializeValue_withNullJsonGeneratorAndNotNullObject() {
    // Arrange
    Object value = new Object();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider.serializeValue(null, value));
  }

  @Test
  public void testSerializeValue_withNotNullJsonGeneratorAndNullObject() {
    // Arrange
    JsonGenerator gen = new JsonGenerator();
    Object value = null;
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider.serializeValue(gen, value));
  }

  @Test
  public void testSerializeValue_withNotNullJsonGeneratorAndNotNullObject() {
    // Arrange
    JsonGenerator gen = new JsonGenerator();
    Object value = new Object();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertDoesNotThrow(() -> provider.serializeValue(gen, value));
  }

  @Test
  public void testSerializeValue_withNullJsonGeneratorAndNotNullObjectAndJavaType() {
    // Arrange
    Object value = new Object();
    JavaType rootType = new JavaType();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider.serializeValue(null, value, rootType));
  }

  @Test
  public void testSerializeValue_withNotNullJsonGeneratorAndNullObjectAndJavaType() {
    // Arrange
    JsonGenerator gen = new JsonGenerator();
    Object value = null;
    JavaType rootType = new JavaType();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider.serializeValue(gen, value, rootType));
  }

  @Test
  public void testSerializeValue_withNotNullJsonGeneratorAndNotNullObjectAndJavaType() {
    // Arrange
    JsonGenerator gen = new JsonGenerator();
    Object value = new Object();
    JavaType rootType = new JavaType();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertDoesNotThrow(() -> provider.serializeValue(gen, value, rootType));
  }

  @Test
  public void testSerializeValue_withNullJsonGeneratorAndNotNullObjectAndJavaTypeAndJsonSerializer() {
    // Arrange
    Object value = new Object();
    JavaType rootType = new JavaType();
    JsonSerializer<Object> ser = new JsonSerializer<>();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider.serializeValue(null, value, rootType, ser));
  }

  @Test
  public void testSerializeValue_withNotNullJsonGeneratorAndNullObjectAndJavaTypeAndJsonSerializer() {
    // Arrange
    JsonGenerator gen = new JsonGenerator();
    Object value = null;
    JavaType rootType = new JavaType();
    JsonSerializer<Object> ser = new JsonSerializer<>();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider.serializeValue(gen, value, rootType, ser));
  }

  @Test
  public void testSerializeValue_withNotNullJsonGeneratorAndNotNullObjectAndJavaTypeAndJsonSerializer() {
    // Arrange
    JsonGenerator gen = new JsonGenerator();
    Object value = new Object();
    JavaType rootType = new JavaType();
    JsonSerializer<Object> ser = new JsonSerializer<>();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertDoesNotThrow(() -> provider.serializeValue(gen, value, rootType, ser));
  }

  @Test
  public void testSerializeXmlNull_withNullJsonGenerator() {
    // Arrange
    JsonGenerator jgen = null;
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider._serializeXmlNull(jgen));
  }

  @Test
  public void testStartRootArray_withNullToXmlGeneratorAndNullQName() {
    // Arrange
    ToXmlGenerator xgen = null;
    QName rootName = null;
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider._startRootArray(xgen, rootName));
  }

  @Test
  public void testInitWithRootName_withNullToXmlGeneratorAndNullQName() {
    // Arrange
    ToXmlGenerator xgen = null;
    QName rootName = null;
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider._initWithRootName(xgen, rootName));
  }

  @Test
  public void testRootNameFromConfig_withNonNullPropertyNameAndNullOrEmptyPropertyNameNamespace() {
    // Arrange
    PropertyName name = new PropertyName();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertDoesNotThrow(() -> provider._rootNameFromConfig());
  }

  @Test
  public void testRootNameFromConfig_withNonNullPropertyNameAndNonNullPropertyNameNamespace() {
    // Arrange
    PropertyName name = new PropertyName();
    name.setNamespace("namespace");
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertDoesNotThrow(() -> provider._rootNameFromConfig());
  }

  @Test
  public void testRootNameFromConfig_withNullPropertyName() {
    // Arrange
    PropertyName name = null;
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertNull(provider._rootNameFromConfig());
  }

  @Test
  public void testAsXmlGenerator_withNonNullJsonGenerator() {
    // Arrange
    JsonGenerator gen = new JsonGenerator();
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertDoesNotThrow(() -> provider._asXmlGenerator(gen));
  }

  @Test
  public void testAsXmlGenerator_withNullJsonGenerator() {
    // Arrange
    JsonGenerator gen = null;
    XmlSerializerProvider provider = new XmlSerializerProvider(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> provider._asXmlGenerator(gen));
  }

}