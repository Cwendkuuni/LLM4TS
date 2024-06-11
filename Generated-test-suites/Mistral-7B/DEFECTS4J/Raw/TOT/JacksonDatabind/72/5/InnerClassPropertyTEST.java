package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.test.BaseMapTest;

import java.io.*;
import java.lang.reflect.*;

import org.junit.*;

public class InnerClassPropertyTest extends BaseMapTest {

    @Test
    public void testInnerClassProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test normal usage
        InnerClassWithInner innerClass = new InnerClassWithInner();
        InnerClassProperty property = mapper.findProperty("inner", InnerClassWithInner.class, InnerClassProperty.Std.INNER);
        assertNotNull(property);

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        ObjectNode node = writer.writeValueAsTree(innerClass);
        Node innerNode = node.get("inner");
        NodeType innerNodeType = innerNode.getNodeType();
        assertEquals(NodeType.POJO, innerNodeType);

        JsonParser parser = mapper.parser();
        Object readValue = parser.readValueAs(InnerClassWithInner.class, node);
        InnerClassWithInner readInnerClass = (InnerClassWithInner) readValue;
        InnerClass inner = readInnerClass.getInner();
        assertEquals("Inner Class Value", inner.getValue());

        // Test setting value
        inner.setValue("New Inner Class Value");
        node = mapper.writerWithDefaultPrettyPrinter().writeValueAsTree(readInnerClass);
        JsonParser parser2 = mapper.parser();
        Object readValue2 = parser2.readValueAs(InnerClassWithInner.class, node);
        InnerClassWithInner readInnerClass2 = (InnerClassWithInner) readValue2;
        InnerClass newInner = readInnerClass2.getInner();
        assertEquals("New Inner Class Value", newInner.getValue());

        // Test null value
        InnerClassProperty nullProperty = mapper.findProperty("innerNull", InnerClassWithInner.class, InnerClassProperty.Std.INNER);
        assertNotNull(nullProperty);

        ObjectNode nodeNull = mapper.createObjectNode();
        ObjectWriter writerNull = mapper.writerWithDefaultPrettyPrinter();
        writerNull.writeValue(nodeNull, null);
        JsonParser parserNull = mapper.parser();
        Object readValueNull = parserNull.readValueAs(InnerClassWithInner.class, nodeNull);
        InnerClassWithInner readInnerClassNull = (InnerClassWithInner) readValueNull;
        InnerClass innerNull = readInnerClassNull.getInner();
        assertNull(innerNull);

        // Test deserialization with custom deserializer
        ObjectNode nodeWithCustomDeserializer = mapper.createObjectNode();
        ObjectWriter writerWithCustomDeserializer = mapper.writerWith(new CustomInnerClassDeserializer());
        writerWithCustomDeserializer.writeValue(nodeWithCustomDeserializer, "Custom Inner Class Value");
        JsonParser parserWithCustomDeserializer = mapper.parser();
        Object readValueWithCustomDeserializer = parserWithCustomDeserializer.readValueAs(InnerClassWithInner.class, nodeWithCustomDeserializer);
        InnerClassWithInner readInnerClassWithCustomDeserializer = (InnerClassWithInner) readValueWithCustomDeserializer;
        InnerClass customInner = readInnerClassWithCustomDeserializer.getInner();
        assertEquals("Custom Inner Class Value", customInner.getValue());

        // Test error cases
        try {
            ObjectMapper mapperWithException = new ObjectMapper();
            ObjectNode nodeWithException = mapperWithException.createObjectNode();
            ObjectWriter writerWithException = mapperWithException.writerWithDefaultPrettyPrinter();
            writerWithException.writeValue(nodeWithException, "Invalid JSON String");
            JsonParser parserWithException = mapperWithException.parser();
            parserWithException.readValueAs(InnerClassWithInner.class, nodeWithException);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            ObjectMapper mapperWithMissingCreator = new ObjectMapper();
            ObjectNode nodeWithMissingCreator = mapperWithMissingCreator.createObjectNode();
            ObjectWriter writerWithMissingCreator = mapperWithMissingCreator.writerWithDefaultPrettyPrinter();
            writerWithMissingCreator.writeValue(nodeWithMissingCreator, "{ \"inner\": null }");
            JsonParser parserWithMissingCreator = mapperWithMissingCreator.parser();
            parserWithMissingCreator.readValueAs(InnerClassWithInner.class, nodeWithMissingCreator);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            ObjectMapper mapperWithEmptyCreator = new ObjectMapper();
            ObjectNode nodeWithEmptyCreator = mapperWithEmptyCreator.createObjectNode();
            ObjectWriter writerWithEmptyCreator = mapperWithEmptyCreator.writerWithDefaultPrettyPrinter();
            writerWithEmptyCreator.writeValue(nodeWithEmptyCreator, "{ \"inner\": {} }");
            JsonParser parserWithEmptyCreator = mapperWithEmptyCreator.parser();
            parserWithEmptyCreator.readValueAs(InnerClassWithInner.class, nodeWithEmptyCreator);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    static class CustomInnerClassDeserializer extends StdDeserializer<InnerClass> {
        @Override
        public InnerClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Object readValue = p.readValueAs(String.class);
            return new InnerClass(readValue);
        }
    }

    static class InnerClassWithInner {
        private InnerClass inner;

        public InnerClassWithInner() {}

        public InnerClass getInner() {
            return inner;
        }

        public void setInner(InnerClass inner) {
            this.inner = inner;
        }
    }

    static class InnerClass {
        private String value;

        public InnerClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}