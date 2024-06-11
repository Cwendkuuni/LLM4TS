package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class InnerClassPropertyTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testInnerClassPropertyDeserialization() throws IOException {
        // Given
        class OuterClass {
            private InnerClass innerClass;

            @JsonDeserialize(using = InnerClassProperty.InnerClassDeserializer.class)
            public InnerClass getInnerClass() {
                return innerClass;
            }

            public void setInnerClass(InnerClass innerClass) {
                this.innerClass = innerClass;
            }
        }

        class InnerClass {
            private Integer value;

            public InnerClass() {
                // empty constructor
            }

            public InnerClass(Integer value) {
                this.value = value;
            }
        }

        // When
        String json = "{\"outerClass\":{\"innerClass\":1}}";
        OuterClass outerClass = objectMapper.readValue(json, OuterClass.class);

        // Then
        Assert.assertNotNull(outerClass.getInnerClass());
        Assert.assertEquals(Integer.valueOf(1), outerClass.getInnerClass().getValue());
    }

    @Test
    public void testInnerClassPropertySerialization() throws IOException {
        // Given
        class OuterClass {
            private InnerClass innerClass;

            @JsonDeserialize(using = InnerClassProperty.InnerClassDeserializer.class)
            @JsonSerialize(using = InnerClassProperty.InnerClassSerializer.class)
            public InnerClass getInnerClass() {
                return innerClass;
            }

            public void setInnerClass(InnerClass innerClass) {
                this.innerClass = innerClass;
            }
        }

        class InnerClass {
            private Integer value;

            public InnerClass() {
                // empty constructor
            }

            public InnerClass(Integer value) {
                this.value = value;
            }
        }

        InnerClass innerClass = new InnerClass(1);
        OuterClass outerClass = new OuterClass();
        outerClass.setInnerClass(innerClass);

        // When
        String json = objectMapper.writeValueAsString(outerClass);

        // Then
        OuterClass deserializedOuterClass = objectMapper.readValue(json, OuterClass.class);
        InnerClass deserializedInnerClass = deserializedOuterClass.getInnerClass();
        Assert.assertNotNull(deserializedInnerClass);
        Assert.assertEquals(Integer.valueOf(1), deserializedInnerClass.getValue());
    }

    @Test
    public void testErrorScenarioInnerClassPropertyDeserialization() throws IOException {
        // Given
        class OuterClass {
            private InnerClass innerClass;

            @JsonDeserialize(using = InnerClassProperty.InnerClassDeserializer.class)
            public InnerClass getInnerClass() {
                return innerClass;
            }

            public void setInnerClass(InnerClass innerClass) {
                this.innerClass = innerClass;
            }
        }

        class InnerClass {
            public InnerClass() {
                // empty constructor
            }

            @JsonDeserialize(using = JacksonUnknownProperty.UnknownPropertyDeserializer.class) // fails on purpose
            public Integer value;
        }

        String json = "{\"outerClass\":{\"innerClass\":1}}"; // valid JSON

        // When & Then
        Assert.assertTrue("Expected deserialization to fail due to incorrect deserializer", () -> {
            try {
                OuterClass outerClass = objectMapper.readValue(json, OuterClass.class);
            } catch (JsonProcessingException e) {
                return true;
            }
            return false;
        });
    }

    static class InnerClassDeserializer extends JsonDeserializer<InnerClass> {
        @Override
        public InnerClass deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException {
            Object value = jp.getValueAsToken();
            if (value.isIntValue()) {
                return new InnerClass(value.intValue());
            }
            throw new IOException("Expected int value but found " + value);
        }
    }

    static class InnerClassSerializer extends JsonSerializer<InnerClass> {
        @Override
        public void serialize(InnerClass value, JsonGenerator jg, SerializerProvider sp)
                throws IOException {
            jg.writeNumber(value.value);
        }
    }
}