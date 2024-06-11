package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.EmptyBeanSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test cases for {@link JsonValueSerializer}.
 */
public class JsonValueSerializerTest {

    static class TestClass {
        @JsonValue
        public String getValue() {
            return "test";
        }

        @JsonSerialize(using = TestSerializer.class)
        public static class TestNestedClass {
            private int value;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }

    static class TestSerializer extends StdSerializer<Object> implements ContextualSerializer, SchemaAware {
        private final Method _accessorMethod;
        private final JsonSerializer<Object> _valueSerializer;
        private final BeanProperty _property;
        private final boolean _forceTypeInformation;

        public TestSerializer(AnnotatedMethod valueMethod, JsonSerializer<?> ser) {
            super(valueMethod.getType());
            _accessorMethod = valueMethod;
            _valueSerializer = (JsonSerializer<Object>) ser;
            _property = null;
            _forceTypeInformation = true;
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
                throws JsonMappingException {
            return this;
        }

        @Override
        public void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
            Object value = null;
            try {
                value = _accessorMethod.getValue(bean);
                if (value == null) {
                    provider.defaultSerializeNull(gen);
                    return;
                }
                _valueSerializer.serialize(value, gen, provider);
            } catch (IOException ioe) {
                throw ioe;
            } catch (Exception e) {
                throw new JsonProcessingException(e.getMessage(), e.getCause());
            }
        }

        @Override
        public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
                throws IOException {
            Object value = null;
            try {
                value = _accessorMethod.getValue(bean);
                if (value == null) {
                    provider.defaultSerializeNull(gen);
                    return;
                }
                _valueSerializer.serializeWithType(value, gen, provider, typeSer);
            } catch (IOException ioe) {
                throw ioe;
            } catch (Exception e) {
                throw new JsonProcessingException(e.getMessage(), e.getCause());
            }
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                throws JsonMappingException {
            return EmptyBeanSerializer.INSTANCE.getSchema(provider, typeHint);
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                throws JsonMappingException {
            if (_valueSerializer instanceof SchemaAware) {
                ((SchemaAware)_valueSerializer).acceptJsonFormatVisitor(visitor, null);
            }
        }
    }

    @Test
    public void testDefaultSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        String result = mapper.writeValueAsString(bean);
        assertEquals("{\"value\":\"test\"}", result);
    }

    @Test
    public void testSerializationWithCustomValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        JsonSerializer<String> valueSerializer = mock(JsonSerializer.class);
        TestSerializer serializer = new TestSerializer(mock(AnnotatedMethod.class), valueSerializer);
        mapper.setSerializerInstance(TestClass.class, serializer);
        TestClass beanWithCustomSerializer = new TestClass();
        String result = mapper.writeValueAsString(beanWithCustomSerializer);
        assertSame(valueSerializer, serializer._valueSerializer);
        assertEquals("{\"value\":\"test\"}", result);
    }

    @Test
    public void testSerializationWithNestedClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        bean.setValue(new TestClass.TestNestedClass());
        String result = mapper.writeValueAsString(bean);
        assertEquals("{\"value\":{\"@class\":\"com.example.JsonValueSerializerTest$TestClass$TestNestedClass\",\"value\":0}}", result);
    }

    @Test
    public void testSerializationWithNaturalType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        int value = 42;
        String result = mapper.writeValueAsString(value);
        assertEquals("42", result);
    }

    @Test
    public void testSerializationWithNaturalTypeAndCustomSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        int value = 42;
        JsonSerializer<Integer> integerSerializer = mock(JsonSerializer.class);
        mapper.setSerializerInstance(Integer.class, integerSerializer);
        String result = mapper.writeValueAsString(value);
        assertSame(integerSerializer, mapper.findValueSerializer(Integer.class, null));
        assertEquals("42", result);
    }

    @Test
    public void testSerializationWithNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        String result = mapper.writeValueAsString(bean);
        assertNull(result);
        bean = new TestClass();
        bean.setValue(null);
        result = mapper.writeValueAsString(bean);
        assertEquals("null", result);
    }

    @Test
    public void testSerializationWithNullValueAndCustomSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        JsonSerializer<String> nullSerializer = mock(JsonSerializer.class);
        mapper.setSerializerInstance(String.class, nullSerializer);
        bean.setValue(null);
        String result = mapper.writeValueAsString(bean);
        assertSame(nullSerializer, mapper.findValueSerializer(String.class, null));
        assertEquals("null", result);
    }

    @Test
    public void testSerializationWithCustomTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        String result = mapper.writeValueAsString(bean);
        assertEquals("{\"value\":\"test\"}", result);
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(TestClass.class);
        JsonSerializer<Object> serializer = mapper.findSerializer(TestClass.class, null);
        assertEquals("TestClass", serializer.getSerializerProperties().get("_class")) ;
        assertEquals("TestClass", serializer.getClass().getSimpleName());
        assertEquals("TestClass", serializer.getDescription().getClass().getSimpleName());
        assertEquals("TestClass", TypeSerializer.instance.writeValueAsString(jsonParser().parse(result), null));
    }

    @Test
    public void testSerializationWithCustomTypeIdAndCustomValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        JsonSerializer<String> valueSerializer = mock(JsonSerializer.class);
        TestSerializer serializer = new TestSerializer(mock(AnnotatedMethod.class), valueSerializer);
        mapper.setSerializerInstance(TestClass.class, serializer);
        String result = mapper.writeValueAsString(bean);
        assertEquals("{\"value\":\"test\"}", result);
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(TestClass.class);
        JsonSerializer<Object> serializer = mapper.findSerializer(TestClass.class, null);
        assertEquals("TestClass", serializer.getSerializerProperties().get("_class"));
        assertEquals("TestClass", serializer.getClass().getSimpleName());
        assertEquals("TestClass", serializer.getDescription().getClass().getSimpleName());
        assertSame(valueSerializer, serializer._valueSerializer);
        assertEquals("TestClass", TypeSerializer.instance.writeValueAsString(jsonParser().parse(result), null));
    }

    @Test
    public void testSerializationWithCustomTypeIdAndNaturalTypeValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        int value = 42;
        bean.setValue(value);
        String result = mapper.writeValueAsString(bean);
        assertEquals("{\"value\":42}", result);
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(TestClass.class);
        JsonSerializer<Object> serializer = mapper.findSerializer(TestClass.class, null);
        assertEquals("TestClass", serializer.getSerializerProperties().get("_class"));
        assertEquals("TestClass", serializer.getClass().getSimpleName());
        assertEquals("TestClass", serializer.getDescription().getClass().getSimpleName());
        assertEquals("TestClass", TypeSerializer.instance.writeValueAsString(jsonParser().parse(result), null));
    }

    @Test
    public void testSerializationWithCustomTypeIdAndNaturalTypeValueAndCustomValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        int value = 42;
        JsonSerializer<Integer> integerSerializer = mock(JsonSerializer.class);
        mapper.setSerializerInstance(Integer.class, integerSerializer);
        bean.setValue(value);
        String result = mapper.writeValueAsString(bean);
        assertEquals("{\"value\":42}", result);
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(TestClass.class);
        JsonSerializer<Object> serializer = mapper.findSerializer(TestClass.class, null);
        assertEquals("TestClass", serializer.getSerializerProperties().get("_class"));
        assertEquals("TestClass", serializer.getClass().getSimpleName());
        assertEquals("TestClass", serializer.getDescription().getClass().getSimpleName());
        assertSame(integerSerializer, mapper.findValueSerializer(Integer.class, null));
        assertEquals("TestClass", TypeSerializer.instance.writeValueAsString(jsonParser().parse(result), null));
    }

    @Test
    public void testSerializationWithCustomTypeIdAndNestedClassValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        bean.setValue(new TestClass.TestNestedClass());
        String result = mapper.writeValueAsString(bean);
        assertEquals("{\"value\":{\"@class\":\"com.example.JsonValueSerializerTest$TestClass$TestNestedClass\",\"value\":0}}", result);
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(TestClass.class);
        JsonSerializer<Object> serializer = mapper.findSerializer(TestClass.class, null);
        assertEquals("TestClass", serializer.getSerializerProperties().get("_class"));
        assertEquals("TestClass", serializer.getClass().getSimpleName());
        assertEquals("TestClass", serializer.getDescription().getClass().getSimpleName());
        assertEquals("TestClass", TypeSerializer.instance.writeValueAsString(jsonParser().parse(result), null));
    }

    @Test
    public void testSerializationWithCustomTypeIdAndNestedClassValueAndCustomValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestClass bean = new TestClass();
        bean.setValue(new TestClass.TestNestedClass());
        JsonSerializer<TestClass.TestNestedClass> nestedClassSerializer = mock(JsonSerializer.class);
        TestSerializer serializer = new TestSerializer(mock(AnnotatedMethod.class), nestedClassSerializer);
        mapper.setSerializerInstance(TestClass.class, serializer);
        String result = mapper.writeValueAsString(bean);
        assertEquals("{\"value\":{\"@class\":\"com.example.JsonValueSerializerTest$TestClass$TestNestedClass\",\"value\":0}}", result);
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(TestClass.class);
        JsonSerializer<Object> serializer = mapper.findSerializer(TestClass.class, null);
        assertEquals("TestClass", serializer.getSerializerProperties().get("_class"));
        assertEquals("TestClass", serializer.getClass().getSimpleName());
        assertEquals("TestClass", serializer.getDescription().getClass().getSimpleName());
        assertSame(nestedClassSerializer, serializer._valueSerializer);
        assertEquals("TestClass", TypeSerializer.instance.writeValueAsString(jsonParser().parse(result), null));
    }

    @Test
    public void testDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":\"test\"}";
        TestClass bean = mapper.readValue(json, TestClass.class);
        assertEquals("test", bean.getValue());
    }

    @Test
    public void testDeserializationWithCustomValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        mapper.setDeserializerInstance(String.class, stringDeserializer);
        String json = "{\"value\":\"test\"}";
        TestClass bean = mapper.readValue(json, TestClass.class);
        assertSame(stringDeserializer, mapper.findDeserializer(String.class, null));
        assertEquals("test", bean.getValue());
    }

    @Test
    public void testDeserializationWithNestedClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"value\":{\"@class\":\"com.example.JsonValueSerializerTest$TestClass$TestNestedClass\",\"value\":42}}";
        TestClass bean = mapper.readValue(json, TestClass.class);
        TestClass.TestNestedClass nestedClass = bean.getValue();
        assertEquals(42, nestedClass.getValue());
    }

    @Test
    public void testDeserializationWithNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        TestClass bean = mapper.readValue(json, TestClass.class);
        assertNull(bean.getValue());
    }

    @Test
    public void testDeserializationWithNullValueAndCustomDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> nullDeserializer = mock(JsonDeserializer.class);
        mapper.setDeserializerInstance(String.class, nullDeserializer);
        String json = "{}";
        TestClass bean = mapper.readValue(json, TestClass.class);
        assertSame(nullDeserializer, mapper.findDeserializer(String.class, null));
        assertNull(bean.getValue());
    }

    private JsonParser jsonParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(""));
    }
}