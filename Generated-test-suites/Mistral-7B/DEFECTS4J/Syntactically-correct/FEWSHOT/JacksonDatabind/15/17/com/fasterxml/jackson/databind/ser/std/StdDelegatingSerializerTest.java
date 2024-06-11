package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest extends TestBase {

    public static class DummyConverter implements Converter<Object, Object> {
        @Override
        public Object convert(Object value) {
            return value;
        }

        @Override
        public Type getInputType(Serializers serializers, Type typeHint) {
            return Object.class;
        }

        @Override
        public Type getOutputType(SerializerProvider provider, Type typeHint) {
            return Object.class;
        }
    }

    public static class DummySerializer extends StdSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeValue(value.toString());
        }
    }

    @Test
    public void testEmpty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {{
            addSerializer(Object.class, new StdDelegatingSerializer(new DummyConverter()));
        }});
        String json = mapper.writeValueAsString(null);
        assertEquals("null", json);
    }

    @Test
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "class")
    public void testSimpleType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {{
            addSerializer(Integer.class, new StdDelegatingSerializer(new DummyConverter(), JavaType.construct(Integer.class), new DummySerializer()));
        }});
        String json = mapper.writeValueAsString(42);
        assertEquals("42", json);
    }

    @Test
    public void testTypeHint() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {{
            addSerializer(String[].class, new StdDelegatingSerializer(new DummyConverter(), JavaType.construct(String[].class), new StdArraySerializers.ArraySerializer(String.class, null)));
        }});
        ObjectWriter writer = mapper.writerWithTypeValidator(null);
        String json = writer.writeValueAsString(new String[]{"foo", "bar"});
        assertEquals("[\"foo\",\"bar\"]", json);
    }

    @Test
    public void testContextual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {{
            addSerializer(MyClass.class, createContextualSerializer(mapper, MyClass.class));
        }});
        String json = mapper.writeValueAsString(new MyClass("foo"));
        assertEquals("{\"str\":\"foo\"}", json);
    }

    @Test
    public void testDynamicResolution() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {{
            addSerializer(Object.class, new StdDelegatingSerializer(new DummyConverter()));
        }});
        ObjectWriter writer = mapper.writerWithTypeValidator(null);
        Object value = new MyDynamicClass();
        String json = writer.writeValueAsString(value);
        assertEquals("{\"int\":42}", json);
    }

    @Test
    public void testSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {{
            addSerializer(MyClass.class, createContextualSerializer(mapper, MyClass.class));
        }});
        JsonNode schema = mapper.readerForUpdating(MyClass.class).getSchema(mapper.getSerializationConfig(), null);
        assertEquals("{\"type\":\"object\",\"properties\":{\"str\":{\"type\":\"string\"}}}", schema.toString());
    }

    private JsonSerializer<?> createContextualSerializer(ObjectMapper mapper, Class<?> clazz) {
        return new StdDelegatingSerializer(clazz, new DummyConverter(), mapper.findValueSerializer(clazz), null) {
            @Override
            protected StdDelegatingSerializer withDelegate(Converter<Object,?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
                return super.withDelegate(converter, delegateType, mapper.findValueSerializer(delegateType));
            }
        };
    }

    public static class MyClass {
        private final String str;

        public MyClass(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }
    }

    public static class MyDynamicClass {
        private int intValue;

        public int getInt() {
            return intValue;
        }

        public void setInt(int value) {
            intValue = value;
        }
    }
}