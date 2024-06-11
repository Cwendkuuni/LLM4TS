
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

@Test
public void testWithFormat_method() {
    DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>();
    DateTimeSerializerBase<Object> newSerializer = serializer.withFormat(true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    assertNotNull(newSerializer);
}

@Test
public void testCreateContextual_method_withNullProperty() {
    DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>();
    SerializerProvider serializers = null;
    BeanProperty property = null;
    JsonSerializer<?> newSerializer = serializer.createContextual(serializers, property);
    assertNotNull(newSerializer);
}

@Test
public void testCreateContextual_method_withNumericShape() {
    DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>();
    SerializerProvider serializers = new SerializerProvider();
    BeanProperty property = new BeanProperty();
    JsonSerializer<?> newSerializer = serializer.createContextual(serializers, property);
    assertNotNull(newSerializer);
}

@Test
public void testIsEmpty_method_withNullValue() {
    DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>();
    assertTrue(serializer.isEmpty(null));
}

@Test
public void testIsEmpty_method_withNonNullValue() {
    DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>();
    assertFalse(serializer.isEmpty(new Object()));
}

@Test
public void testGetSchema_method() {
    DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>();
    SerializerProvider serializers = new SerializerProvider();
    assertNotNull(serializer.getSchema(serializers, Object.class));
}

@Test
public void testAcceptJsonFormatVisitor_method() {
    DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>();
    JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
    JavaType typeHint = new JavaType();
    assertThrows(JsonMappingException.class, () -> serializer.acceptJsonFormatVisitor(visitor, typeHint));
}

@Test
public void testSerialize_method() {
    DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>();
    JsonGenerator gen = new JsonGenerator();
    SerializerProvider serializers = new SerializerProvider();
    assertThrows(IOException.class, () -> serializer.serialize(new Object(), gen, serializers));
}

}
