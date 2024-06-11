package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.JavaType;

public class JsonValueSerializerTest {

    // Test cases for JsonValueSerializer constructor

    @Test
    public void testJsonValueSerializerConstructor_ValueMethodAndSerializer() {
        AnnotatedMethod valueMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, serializer);
        assertEquals(JsonValueSerializer.class, jsonValueSerializer.getClass());
        assertEquals(valueMethod, jsonValueSerializer._accessorMethod);
        assertEquals(serializer, jsonValueSerializer._valueSerializer);
        assertNull(jsonValueSerializer._property);
        assertFalse(jsonValueSerializer._forceTypeInformation);
    }

    @Test
    public void testJsonValueSerializerConstructor_FromAnotherInstance() {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(this, accessorMethod, valueSerializer, false);
        JsonValueSerializer anotherInstance = new JsonValueSerializer(jsonValueSerializer);
        assertEquals(jsonValueSerializer.getClass(), anotherInstance.getClass());
        assertEquals(accessorMethod, anotherInstance._accessorMethod);
        assertEquals(valueSerializer, anotherInstance._valueSerializer);
        assertEquals(property, anotherInstance._property);
        assertEquals(false, anotherInstance._forceTypeInformation);
    }

    // Test cases for JsonValueSerializer methods

    @Test
    public void testWithResolved() {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(this, accessorMethod, valueSerializer, false);
        JsonValueSerializer resolvedSerializer = jsonValueSerializer.withResolved(property, valueSerializer, false);
        assertNotSame(jsonValueSerializer, resolvedSerializer);
        assertEquals(jsonValueSerializer.getClass(), resolvedSerializer.getClass());
        assertEquals(accessorMethod, resolvedSerializer._accessorMethod);
        assertEquals(valueSerializer, resolvedSerializer._valueSerializer);
        assertEquals(property, resolvedSerializer._property);
        assertEquals(false, resolvedSerializer._forceTypeInformation);
    }

    @Test
    public void testCreateContextual() {
        AnnotatedMethod valueMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        when(valueMethod.getType()).thenReturn(Mockito.mock(JavaType.class));
        when(mapper.isEnabled(eq(MapperFeature.USE_STATIC_TYPING))).thenReturn(false);
        JsonSerializer<Object> result = new JsonValueSerializer(valueMethod, serializer);
        JsonSerializer<Object> contextualSerializer = result.createContextual(Mockito.mock(SerializerProvider.class), property);
        assertEquals(result.getClass(), contextualSerializer.getClass());
        assertEquals(valueMethod, contextualSerializer._accessorMethod);
        assertEquals(serializer, contextualSerializer._valueSerializer);
        assertEquals(property, contextualSerializer._property);
        assertFalse(contextualSerializer._forceTypeInformation);
    }

    @Test
    public void testSerialize() throws JsonMappingException, IOException {
        Object bean = Mockito.mock(Object.class);
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object value = Mockito.mock(Object.class);
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        when(accessorMethod.getValue(bean)).thenReturn(value);
        when(_accessorMethod.getValue(bean)).thenReturn(value);
        when(valueSerializer.serialize(value, generator, provider)).thenReturn(null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);
        jsonValueSerializer.serialize(bean, generator, provider);
        verify(generator).writeStartObject();
        verify(generator).writeFieldName("value");
        verify(generator).writeValue(value);
        verify(generator).writeEndObject();
    }

    @Test
    public void testSerializeWithType() throws JsonMappingException, IOException {
        Object bean = Mockito.mock(Object.class);
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object value = Mockito.mock(Object.class);
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        when(accessorMethod.getValue(bean)).thenReturn(value);
        when(_accessorMethod.getValue(bean)).thenReturn(value);
        when(valueSerializer.serializeWithType(value, generator, provider, typeSerializer)).thenReturn(null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);
        jsonValueSerializer.serializeWithType(bean, generator, provider, typeSerializer);
        verify(generator).writeStartObject();
        verify(generator).writeFieldName("value");
        verify(generator).writeValue(value);
        verify(typeSerializer).writeTypePrefixForScalar(bean, generator);
        verify(valueSerializer).serializeWithType(value, generator, provider, typeSerializer);
        verify(typeSerializer).writeTypeSuffixForScalar(bean, generator);
    }

    // Test cases for JsonValueSerializer helper methods

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        Class<?> rawType = Integer.TYPE;
        JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
        assertTrue(new JsonValueSerializer().isNaturalTypeWithStdHandling(rawType, serializer));

        rawType = Double.TYPE;
        assertTrue(new JsonValueSerializer().isNaturalTypeWithStdHandling(rawType, serializer));

        rawType = Boolean.TYPE;
        assertTrue(new JsonValueSerializer().isNaturalTypeWithStdHandling(rawType, serializer));

        rawType = String.class;
        assertFalse(new JsonValueSerializer().isNaturalTypeWithStdHandling(rawType, serializer));

        rawType = Integer.class;
        assertFalse(new JsonValueSerializer().isNaturalTypeWithStdHandling(rawType, serializer));

        rawType = CustomType.class;
        assertFalse(new JsonValueSerializer().isNaturalTypeWithStdHandling(rawType, serializer));

        when(serializer.isDefaultSerializer()).thenReturn(false);
        assertFalse(new JsonValueSerializer().isNaturalTypeWithStdHandling(rawType, serializer));
    }

    // Test cases for JsonValueSerializer acceptance of JsonFormatVisitor

    @Test
    public void testAcceptJsonFormatVisitor_EnumType() throws JsonMappingException {
        AnnotatedMethod valueMethod = Mockito.mock(AnnotatedMethod.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        Class<?> enumType = EnumType.class;
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, null);
        assertTrue(jsonValueSerializer._acceptJsonFormatVisitorForEnum(Mockito.mock(JsonFormatVisitorWrapper.class), typeHint, enumType));
    }

    @Test
    public void testAcceptJsonFormatVisitor_NonEnumType() throws JsonMappingException {
        AnnotatedMethod valueMethod = Mockito.mock(AnnotatedMethod.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, null);
        JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
        when(valueMethod.getType()).thenReturn(typeHint);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        when(visitor.expectStringFormat(typeHint)).thenReturn(Mockito.mock(JsonStringFormatVisitor.class));
        jsonValueSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectAnyFormat(typeHint);
    }

    // Test cases for helper classes

    @Test
    public void testStdSerializerWrapper() {
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        StdSerializer<Object> wrapper = new StdSerializer<Object>(delegateSerializer.getClass());
        assertEquals(delegateSerializer.getClass(), wrapper.getClass());
        assertEquals(delegateSerializer, wrapper.delegate);
    }
}

class CustomType {
    // Custom type to test JsonValueSerializer with non-natural types
}

class EnumType extends Enum {
    @JsonValue
    public String getValue() {
        return name();
    }
}