package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedName;
import com.fasterxml.jackson.databind.introspect.ValueTypeDescriptor;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class InnerClassPropertyTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testInnerClassPropertyDeserialization() throws IOException {
        // Test with a simple inner class
        class OuterClass {
            @JsonDeserialize(using = InnerClassProperty.InnerClassDeserializer.class)
            private InnerClass innerClass;

            public InnerClass getInnerClass() {
                return innerClass;
            }

            public void setInnerClass(InnerClass innerClass) {
                this.innerClass = innerClass;
            }
        }

        class InnerClass {
            private int value;

            public InnerClass() {
            }

            public InnerClass(int value) {
                this.value = value;
            }
        }

        String json = "{\"outerClass\":{}}";
        OuterClass outerClass = mapper.readValue(json, OuterClass.class);
        assertEquals(null, outerClass.getInnerClass());

        json = "{\"outerClass\":{\"innerClass\":1}}";
        outerClass = mapper.readValue(json, OuterClass.class);
        InnerClass innerClass = outerClass.getInnerClass();
        assertEquals(0, innerClass.getValue());

        json = "{\"outerClass\":{\"innerClass\":{\"value\":2}}}";
        outerClass = mapper.readValue(json, OuterClass.class);
        innerClass = outerClass.getInnerClass();
        assertEquals(2, innerClass.getValue());
    }

    @Test
    public void testInnerClassPropertySerialization() throws IOException {
        // Test with a simple inner class
        class OuterClass {
            @JsonSerialize(using = InnerClassSerializer.class)
            private InnerClass innerClass;

            public InnerClass getInnerClass() {
                return innerClass;
            }

            public void setInnerClass(InnerClass innerClass) {
                this.innerClass = innerClass;
            }
        }

        class InnerClass {
            private int value;

            public InnerClass() {
            }

            public InnerClass(int value) {
                this.value = value;
            }

            @JsonValue
            public int intValue() {
                return value;
            }
        }

        InnerClass innerClass = new InnerClass(1);
        OuterClass outerClass = new OuterClass();
        outerClass.setInnerClass(innerClass);

        String json = mapper.writeValueAsString(outerClass);
        OuterClass deserializedOuterClass = mapper.readValue(json, OuterClass.class);
        InnerClass deserializedInnerClass = deserializedOuterClass.getInnerClass();
        assertEquals(1, deserializedInnerClass.intValue());
    }

    static class InnerClassPropertyTest_InnerClassDeserializer
            extends StdSerializer<InnerClass> {
        public InnerClassPropertyTest_InnerClassDeserializer() {
            this(null);
        }

        public InnerClassPropertyTest_InnerClassDeserializer(Class<InnerClass> t) {
            this(null, t);
        }

        public InnerClassPropertyTest_InnerClassDeserializer(Class<InnerClass> t,
                Constructor<InnerClass> c) {
            super(t, c);
        }

        @Override
        public InnerClass deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            JsonDeserializer<InnerClass> deserializer = ctxt.findBeanDeserializer(InnerClass.class, null);
            return deserializer.deserialize(p, ctxt, null);
        }
    }

    static class InnerClassSerializer
            extends StdSerializer<InnerClass> {
        public InnerClassSerializer() {
            this(null);
        }

        public InnerClassSerializer(Class<InnerClass> t) {
            super(t);
        }

        @Override
        public void serialize(InnerClass value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            JsonSerializer<InnerClass> serializer = serializers.findValueSerializer(InnerClass.class, null);
            serializer.serialize(value, gen, serializers);
        }
    }
}