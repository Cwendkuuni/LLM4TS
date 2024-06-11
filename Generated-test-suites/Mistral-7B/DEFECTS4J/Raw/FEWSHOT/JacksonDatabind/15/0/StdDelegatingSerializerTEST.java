package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.Version.VERSION_2_11;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.Converter;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.ConverterFunctions;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.test.BaseTest;

@RunWith(MockitoJUnitRunner.class)
public class StdDelegatingSerializerTest extends BaseTest {

    @Test
    public void testDelegateSerializerSerialization() throws IOException, JsonProcessingException {
        final Object delegateValue = new Object();
        final Converter<Object, String> converter = Mockito.mock(Converter.class);
        final JavaType delegateType = MapUtil.constructType(String.class, Object.class);
        final JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter) {
            @Override
            protected Object convertValue(Object value) {
                return value;
            }

            @Override
            protected void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                visitor.visitObjectSerializer(this, delegateType);
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                assertEquals(delegateType, provider.getSerializerProvider().findValueSerializer(delegateType));
                delegateSerializer.serialize(delegateValue, gen, provider);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSerializer(delegateType, delegateSerializer);

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(delegateValue);
        Object newDelegateValue = mapper.readValue(json, Object.class);
        assertEquals(delegateValue, newDelegateValue);
    }

    @Test
    public void testConvertValue() throws IOException {
        final Object input = "test";
        final Converter<Object, String> converter = ConverterFunctions.stringConverter(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        assertEquals("test", serializer.convertValue(input));
    }

    @Test
    public void testNullInput() {
        final Converter<Object, String> converter = Mockito.mock(Converter.class);
        final JavaType delegateType = MapUtil.constructType(String.class, Object.class);
        final JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null) {
            @Override
            protected Object convertValue(Object value) {
                return null;
            }
        };
        assertNull(serializer.getDelegateSerializer());

        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer) {
            @Override
            protected Object convertValue(Object value) {
                return null;
            }
        };
        assertNull(serializer.getDelegateSerializer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentTypes() {
        final Converter<Object, String> converter = Mockito.mock(Converter.class);
        final JavaType delegateType = MapUtil.constructType(int.class, Object.class);
        final JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testContextualization() throws IOException {
        final Object delegateValue = new Object();
        final Converter<Object, String> converter = Mockito.mock(Converter.class);
        final JavaType delegateType = MapUtil.constructType(String.class, Object.class);
        final JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);

        ObjectMapper mapper = new ObjectMapper();

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter) {
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
                throws JsonMappingException {
                return delegateSerializer;
            }
        };

        mapper.registerSerializer(delegateType, serializer);

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(delegateValue);
        Object newDelegateValue = mapper.readValue(json, Object.class);
        assertEquals(delegateValue, newDelegateValue);
    }

    @Test
    public void testDynamicLookup() throws IOException {
        final Object input = new Object();
        final Converter<Object, String> converter = Mockito.mock(Converter.class);
        final JavaType delegateType = MapUtil.constructType(String.class, Object.class);
        final JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        final ObjectMapper mapper = new ObjectMapper();

        SerializerProvider provider = mapper.getSerializerProvider();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
                delegateSerializer.serialize(value, gen, provider);
            }
        };

        mapper.registerSerializer(delegateType, serializer);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(input);
        Object newDelegateValue = mapper.readValue(json, Object.class);
        assertEquals(input, newDelegateValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testNoDelegateSerializer() throws IOException {
        final Object input = new Object();
        final Converter<Object, String> converter = Mockito.mock(Converter.class);
        final JavaType delegateType = MapUtil.constructType(String.class, Object.class);
        final JsonSerializer<Object> delegateSerializer = null;
        final ObjectMapper mapper = new ObjectMapper();

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        writer.writeValueAsString(input);
    }

    @Test
    public void testSchema() throws IOException {
        final Object delegateValue = new Object();
        final Converter<Object, String> converter = Mockito.mock(Converter.class);
        final JavaType delegateType = MapUtil.constructType(String.class, Object.class);
        final JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        final SchemaAware delegateSchemaAware = Mockito.mock(SchemaAware.class);

        ObjectMapper mapper = new ObjectMapper();

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer) {
            @Override
            public JsonSchema getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return delegateSchemaAware.getSchema(provider, typeHint);
            }
        };

        mapper.registerSerializer(delegateType, serializer);

        JsonSchema schema = mapper.getSerializerProvider().getDefaultSchemaProvider().findSchema(delegateType, null);
        assertEquals(delegateSchemaAware, schema.getSchemaProvider());
    }
}