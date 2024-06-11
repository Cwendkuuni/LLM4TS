package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.AnnotatedMethod;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializer() throws IOException, JsonMappingException {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);

        assertNotNull(jsonValueSerializer);
        assertEquals(JsonValueSerializer.class, jsonValueSerializer.getClass());
        assertEquals(accessorMethod, jsonValueSerializer._accessorMethod);
        assertEquals(valueSerializer, jsonValueSerializer._valueSerializer);
        assertNull(jsonValueSerializer._property);
        assertNull(jsonValueSerializer._forceTypeInformation);
    }

    @Test
    public void testCreateContextual() throws IOException, JsonMappingException {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        Type type = Mockito.mock(Type.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);

        JsonSerializer<Object> serializedSerializer = Mockito.mock(JsonSerializer.class);
        when(provider.findPrimaryPropertySerializer(type, property)).thenReturn(serializedSerializer);

        JsonValueSerializer result = jsonValueSerializer.createContextual(provider, property);

        assertEquals(jsonValueSerializer, result);
        assertEquals(serializedSerializer, result._valueSerializer);
        assertEquals(property, result._property);
        assertEquals(jsonValueSerializer._forceTypeInformation, result._forceTypeInformation);
    }

    @Test
    public void testSerialize() throws IOException {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object bean = new Object();
        Object value = new Object();
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);

        when(accessorMethod.getValue(bean)).thenReturn(value);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);

        jsonValueSerializer.serialize(bean, gen, prov);

        verify(valueSerializer).serialize(value, gen, prov);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        Object bean = new Object();
        Object value = new Object();
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);

        when(accessorMethod.getValue(bean)).thenReturn(value);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);

        jsonValueSerializer.serializeWithType(bean, gen, provider, typeSer);

        verify(valueSerializer).serializeWithType(value, gen, provider, typeSer);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonSerializer<Object> valueSerializer = Mockito.mock(JsonSerializer.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);

        when(valueSerializer instanceof SchemaAware).thenReturn(true);
        JsonSchema schema = Mockito.mock(JsonSchema.class);
        when(provider.getSchemaProvider()).thenReturn(SchemaFactoryProvider.instance);
        when(((SchemaAware) valueSerializer).getSchema(provider, null)).thenReturn(schema);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);

        JsonNode result = jsonValueSerializer.getSchema(provider, typeHint);

        assertEquals(schema, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);

        jsonValueSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectAnyFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws JsonMappingException {
        AnnotatedMethod accessorMethod = Mockito.mock(AnnotatedMethod.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        Class<?> enumType = Mockito.mock(Class.class);

        Set<String> enums = new LinkedHashSet<>();
        enums.add("VALUE1");
        enums.add("VALUE2");

        when(_acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType)).thenReturn(true);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(accessorMethod, null);

        jsonValueSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).enumTypes(enums);
    }

    private boolean _acceptJsonFormatVisitorForEnum(JsonFormatVisitorWrapper visitor, JavaType typeHint, Class<?> enumType)
            throws JsonMappingException {
        return jsonValueSerializer._acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType);
    }
}