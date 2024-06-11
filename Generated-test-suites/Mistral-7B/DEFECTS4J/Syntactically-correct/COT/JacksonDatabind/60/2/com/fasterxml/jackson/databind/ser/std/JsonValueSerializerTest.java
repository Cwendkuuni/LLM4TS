package com.example.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaGenerator;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyInstance;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JsonValueSerializer.class})
public class JsonValueSerializerTest {
    private JsonValueSerializer jsonValueSerializer;
    private SerializerProvider serializerProvider;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        serializerProvider = mock(SerializerProvider.class);
        objectMapper = mock(ObjectMapper.class);
        PowerMockito.when(objectMapper.getSerializerProvider()).thenReturn(serializerProvider);

        Method method = getClass().getDeclaredMethod("testMethod");
        jsonValueSerializer = new JsonValueSerializer(method, objectMapper.findValueSerializer(int.class, null));
    }

    @Test
    public void testBasicFunctionality() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);

        jsonValueSerializer.serialize(bean, gen, serializerProvider);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("jsonValue");
        verify(gen).writeObject(any());
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializationWithType() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        jsonValueSerializer.serializeWithType(bean, gen, serializerProvider, typeSerializer);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("jsonValue");
        verify(gen).writeObject(any());
        verify(typeSerializer).writeTypePrefixForScalar(bean, gen);
        verify(typeSerializer).writeTypeSuffixForScalar(bean, gen);
    }

    @Test
    public void testSerializationWithNullValue() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);

        jsonValueSerializer.serialize(bean, gen, serializerProvider);

        verify(gen).writeNull();
    }

    @Test(expected = JsonMappingException.class)
    public void testExceptionDuringSerialization() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);

        PowerMockito.when(jsonValueSerializer.getValueSerializer()).thenThrow(new IOException());

        jsonValueSerializer.serialize(bean, gen, serializerProvider);
    }

    @Test
    public void testSerializationWithCustomValueSerializer() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);

        jsonValueSerializer = new JsonValueSerializer(null, null, stringSerializer, false);

        jsonValueSerializer.serialize(bean, gen, serializerProvider);

        verify(gen).writeString("testString");
    }

    @Test
    public void testGetSchema() throws Exception {
        JsonSchemaGenerator schemaGenerator = mock(JsonSchemaGenerator.class);
        JsonSchema schema = mock(JsonSchema.class);

        PowerMockito.when(schemaGenerator.findSchemaFor(any(), anyInt(), any()))
                .thenReturn(schema);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);

        JsonSchema actualSchema = jsonValueSerializer.getSchema(serializerProvider, null);

        assertEquals(schema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws Exception {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);

        JavaType javaType = mock(JavaType.class);
        Class<?> enumType = mock(Class.class);

        PowerMockito.when(jsonFormatVisitorWrapper.expectStringFormat(any()))
                .thenReturn(mock(JsonStringFormatVisitor.class));

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);

        jsonValueSerializer._acceptJsonFormatVisitorForEnum(jsonFormatVisitorWrapper, javaType, enumType);

        Set<String> enums = new LinkedHashSet<>();
        enums.add("ONE");
        enums.add("TWO");

        verify(jsonFormatVisitorWrapper).enumTypes(eq(enums));
    }

    private Method testMethod() {
        return new Method("testMethod", new Class[0]);
    }
}