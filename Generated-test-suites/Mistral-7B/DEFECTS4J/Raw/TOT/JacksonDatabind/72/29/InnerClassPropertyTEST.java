package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyWithDefaultConstructor() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            {
                addDeserializer(InnerClass.class, new InnerClassDeserializer());
            }
        });

        InnerClass innerClass = mapper.readValue(jsonFactory.createParser("{\"innerClass\":{}}"), InnerClass.class);
        Assert.assertNotNull(innerClass);
        Assert.assertNull(innerClass.innerProperty);

        InnerClassProperty innerClassProperty = mapper.findProperty("innerClass.innerProperty", InnerClass.class);
        Assert.assertNotNull(innerClassProperty);
        Assert.assertNull(innerClassProperty.getValue());
    }

    @Test
    public void testInnerClassPropertyWithCustomConstructor() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            {
                addDeserializer(InnerClass.class, new InnerClassDeserializer());
            }
        });

        InnerClass innerClass = mapper.readValue(jsonFactory.createParser("{\"innerClass\":{\"value\":{\"some\":\"value\"}}}"), InnerClass.class);
        Assert.assertNotNull(innerClass);
        Assert.assertNotNull(innerClass.innerProperty);
        Assert.assertEquals("value", innerClass.innerProperty.getValue().getClass().getSimpleName());
        Assert.assertEquals("some value", innerClass.innerProperty.getValue().toString());

        InnerClassProperty innerClassProperty = mapper.findProperty("innerClass.innerProperty", InnerClass.class);
        Assert.assertNotNull(innerClassProperty);
        Assert.assertNotNull(innerClassProperty.getValue());
        Assert.assertEquals("value", innerClassProperty.getValue().getClass().getSimpleName());
        Assert.assertEquals("some value", innerClassProperty.getValue().toString());
    }

    @Test
    public void testErrorHandling() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            {
                addDeserializer(InnerClass.class, new InnerClassDeserializer());
            }
        });

        try {
            mapper.readValue(new JsonFactory(), InnerClass.class);
            Assert.fail();
        } catch (JsonProcessingException e) {
            // Expected
        }

        try {
            InnerClassProperty innerClassProperty = mapper.findProperty("nonExistent.innerProperty", InnerClass.class);
            Assert.fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    static class InnerClass {
        public InnerClassProperty innerProperty;

        public InnerClass() {}

        public InnerClass(String value) {
            this.innerProperty = new InnerClassProperty(null, new InnerClassProperty.InnerClassPropertyBuilder()
                    .withDelegate(new SettableBeanProperty.StdDelegate())
                    .withCreator(new Constructor<InnerClassPropertyValue>(InnerClassPropertyValue.class) {
                        @Override
                        public InnerClassPropertyValue newInstance(Object instance) throws IOException {
                            return new InnerClassPropertyValue(value);
                        }
                    })
                    .build());
        }
    }

    static class InnerClassDeserializer extends StdSerializer<InnerClass> {
        public InnerClassDeserializer() { this(null); }

        public InnerClassDeserializer(Class<InnerClass> t, JsonDeserializer<?> deserializer) { super(t, deserializer); }

        @Override
        public InnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Object value = p.getValueAsTree();
            if (value == null || value.isNullValue()) {
                return new InnerClass();
            }

            InnerClass innerClass = new InnerClass();
            JsonNode node = ((ObjectNode) value).get("value");
            if (node != null) {
                innerClass.innerProperty = ctxt.findRootValueDeserializer(InnerClassProperty.class).deserializeAndSet(p, ctxt, innerClass);
            }

            return innerClass;
        }
    }

    static class InnerClassPropertyValue {
        private final String value;

        public InnerClassPropertyValue() {}

        public InnerClassPropertyValue(String value) {
            this.value = value;
        }

        @JsonDeserialize(using = InnerClassPropertyValueDeserializer.class)
        public String getValue() {
            return value;
        }
    }

    static class InnerClassPropertyValueDeserializer extends StdSerializer<InnerClassPropertyValue> {
        public InnerClassPropertyValueDeserializer() { this(null); }

        public InnerClassPropertyValueDeserializer(Class<InnerClassPropertyValue> t, JsonDeserializer<?> deserializer) { super(t, deserializer); }

        @Override
        public InnerClassPropertyValue deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new InnerClassPropertyValue(p.getValueAsString());
        }
    }
}