package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaProvider.SchemaFactory;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.IOException;
import java.lang.reflect.AnnotatedMethod;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

public class JsonValueSerializerTest {

    private static final Object TEST_VALUE = new Object() {
        @JsonSerialize(using = TestValueSerializer.class)
        Object getValue() {
            return "Test Value";
        }
    };

    @SuppressWarnings("unchecked")
    private static final JsonValueSerializer<Object> TEST_SERIALIZER = new JsonValueSerializer<>(
            mock(AnnotatedMethod.class), null, null, false);

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        TEST_SERIALIZER._valueSerializer = mock(JsonSerializer.class);
        TEST_SERIALIZER.serialize(TEST_VALUE, gen, mapper.getSerializerProvider());

        verify(TEST_SERIALIZER._valueSerializer).serialize(eq("Test Value"), eq(gen), eq(mapper.getSerializerProvider()));
    }

    @Test
    public void testSerializeWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        TEST_SERIALIZER._valueSerializer = mock(JsonSerializer.class);
        TEST_SERIALIZER.serializeWithType(TEST_VALUE, gen, mapper.getSerializerProvider(), typeSerializer);

        verify(TEST_SERIALIZER._valueSerializer).serializeWithType(eq("Test Value"), eq(gen), eq(mapper.getSerializerProvider()), eq(typeSerializer));
    }

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonValueSerializer<Object> serializer1 = TEST_SERIALIZER;
        JsonValueSerializer<Object> serializer2 = TEST_SERIALIZER.withResolved(property, valueSerializer, false);

        assertEquals(serializer1, serializer2);

        JsonValueSerializer<Object> ser = TEST_SERIALIZER.createContextual(mapper.getSerializerProvider(), property);
        assertEquals(valueSerializer, ser._valueSerializer);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mapper.getTypeFactory().constructType(Object.class);

        TEST_SERIALIZER.acceptJsonFormatVisitor(visitor, typeHint);

        // Call to JsonSerializer<Object>
        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        when(visitor.getProvider()).thenReturn(mapper.getSerializerProvider());
        when(visitor.findValueSerializer(eq(String.class), any()))
                .thenReturn(valueSerializer);

        TEST_SERIALIZER.acceptJsonFormatVisitor(visitor, typeHint);
        verify(valueSerializer).acceptJsonFormatVisitor(visitor, null);

        // Call to SchemaAware JsonSerializer<Object>
        JsonSerializer<Object> schemaAwareSerializer = mock(JsonSerializer.class);
        when(visitor.getProvider()).thenReturn(mapper.getSerializerProvider());
        when(visitor.expectStringFormat(typeHint)).thenReturn(mock(JsonStringFormatVisitor.class));
        when(schemaAwareSerializer).thenReturn(schemaAwareSerializer);
        when(visitor.getSchemaFactory()).thenReturn(new SchemaFactory() {
            @Override
            public JsonSchema getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return JsonSchema.getDefaultSchemaNode();
            }
        });
        when(visitor.expectAnyFormat(typeHint)).thenReturn(null);

        when(visitor.findValueSerializer(eq(Class.forName("com.test.TestEnum")), any()))
                .thenReturn(schemaAwareSerializer);

        TEST_SERIALIZER.acceptJsonFormatVisitor(visitor, typeHint);
        verify(schemaAwareSerializer).acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        assertEquals(true, JsonValueSerializer.isNaturalTypeWithStdHandling(boolean.class, null));
        assertEquals(true, JsonValueSerializer.isNaturalTypeWithStdHandling(int.class, null));
        assertEquals(true, JsonValueSerializer.isNaturalTypeWithStdHandling(double.class, null));
        assertEquals(true, JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, null));
        assertEquals(false, JsonValueSerializer.isNaturalTypeWithStdHandling(Object.class, null));
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mapper.getTypeFactory().constructType(Object.class);
        Class<?> enumType = Class.forName("com.test.TestEnum");

        TEST_SERIALIZER._acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType);

        Set<String> enums = new LinkedHashSet<>();
        enums.add("VALUE1");
        enums.add("VALUE2");

        when(visitor.expectStringFormat(typeHint)).thenReturn(mock(JsonStringFormatVisitor.class));

        TEST_SERIALIZER._acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType);
        verify(visitor.enumTypes(eq(enums))).wasCalledOnce();
    }

    private static class TestValueSerializer extends StdSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString("Test Value");
        }
    }

    private static class TestEnum {
        @JsonValue
        public String getValue() {
            return "VALUE1";
        }

        public static enum TestEnumEnum {
            VALUE1,
            VALUE2
        }
    }
}