
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class StdSerializerTest {

    @Test
    public void testSerializeValue() throws IOException, JsonGenerationException {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        stdSerializer.serializeValue("test", jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeString("test");
    }

    @Test
    public void testGetSchema() throws IOException, JsonMappingException {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);

        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", "string");

        assert stdSerializer.getSchema(serializerProvider, typeHint).equals(schema);
    }

    @Test
    public void testGetSchemaWithOptional() throws IOException, JsonMappingException {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);

        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", "string");
        schema.put("required", true);

        assert stdSerializer.getSchema(serializerProvider, typeHint, false).equals(schema);
    }

    @Test
    public void testCreateObjectNode() {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();

        assert stdSerializer.createObjectNode().equals(objectNode);
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", "string");

        assert stdSerializer.createSchemaNode("string").equals(schema);
    }

    @Test
    public void testCreateSchemaNodeWithOptional() {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", "string");
        schema.put("required", true);

        assert stdSerializer.createSchemaNode("string", false).equals(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = Mockito.mock(JavaType.class);

        stdSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        Mockito.verify(jsonFormatVisitorWrapper, Mockito.times(1)).expectAnyFormat(javaType);
    }

    @Test
    public void testWrapAndThrowWithException() throws IOException {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        IOException exception = new IOException("test_exception");

        try {
            stdSerializer.wrapAndThrow(serializerProvider, exception, null, null);
        } catch (IOException e) {
            assert e.getMessage().equals("test_exception");
        }
    }

    @Test
    public void testWrapAndThrowWithError() throws IOException {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        Error error = new Error("test_error");

        try {
            stdSerializer.wrapAndThrow(serializerProvider, error, null, null);
        } catch (Error e) {
            assert e.getMessage().equals("test_error");
        }
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanProperty beanProperty = Mockito.mock(BeanProperty.class);
        JsonSerializer jsonSerializer = Mockito.mock(JsonSerializer.class);
        Converter converter = Mockito.mock(Converter.class);
        JavaType javaType = Mockito.mock(JavaType.class);

        Mockito.when(converter.getOutputType(Mockito.any())).thenReturn(javaType);
        Mockito.when(serializerProvider.getTypeFactory()).thenReturn(Mockito.mock(Constructor.NONE.getClass()));
        Mockito.when(serializerProvider.findValueSerializer(javaType)).thenReturn(jsonSerializer);

        JsonSerializer convertedJsonSerializer = new StdDelegatingSerializer(converter, javaType, jsonSerializer);

        assert stdSerializer.findConvertingContentSerializer(serializerProvider, beanProperty, jsonSerializer).equals(convertedJsonSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer stdSerializer = new StdSerializer(String.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }
        };

        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        FilterProvider filterProvider = Mockito.mock(FilterProvider.class);
        PropertyFilter propertyFilter = Mockito.mock(PropertyFilter.class);
        Object value = Mockito.mock(Object.class);

        Mockito.when(serializerProvider.getFilterProvider()).thenReturn(filterProvider);
        Mockito.when(filterProvider.findPropertyFilter(Mockito.any(), Mockito.any())).thenReturn(propertyFilter);
        assert stdSerializer.findPropertyFilter(serializerProvider, "TEST_FILTER", value).equals(propertyFilter);
    }
}
