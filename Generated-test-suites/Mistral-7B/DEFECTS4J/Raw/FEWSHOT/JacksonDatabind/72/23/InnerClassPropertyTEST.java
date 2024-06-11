package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyConstruction() {
        ObjectNode rootNode = ObjectMapper.getDefaultInstance().createObjectNode();
        rootNode.put("propertyName", "testProperty");
        rootNode.put("innerClass", "com.test.InnerClass");
        rootNode.put("constructorArgs", asJson(new Object[]{"arg1", "arg2"}));

        JavaType propertyType = new TypeFactory().constructType("com.test.OuterClass$InnerClass");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SettableBeanProperty property = mapper.readerForMap().readValue(rootNode, SettableBeanProperty.class);
        InnerClassProperty innerProperty = (InnerClassProperty) property;

        assertEquals("testProperty", innerProperty.getName());
        assertEquals("com.test.InnerClass", innerProperty.getType().getRawClass().getName());
        assertEquals(2, innerProperty.getConstructorArgs().length);
        assertEquals("arg1", innerProperty.getConstructorArgs()[0].toString());
        assertEquals("arg2", innerProperty.getConstructorArgs()[1].toString());
        assertEquals(InnerClassProperty.class, innerProperty.getClass());
    }

    @Test
    public void testSetValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        InnerClassProperty property = new InnerClassProperty(null, null, null, null);
        JavaType innerClassType = new TypeFactory().constructType("com.test.InnerClass");
        property.setType(innerClassType);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("propertyName", "testProperty");

        JsonParser parser = mapper.parserForObject(rootNode);
        property.deserializeAndSet(parser, new DeserializationContext(), new Object());

        Object innerInstance = property.get("testProperty");
        assertNotNull(innerInstance);

        JsonNode innerNode = mapper.writerWithView(new SimpleBeanPropertyFilter("testProperty")).writeValueAsTree(innerInstance);
        assertNotNull(innerNode);
        assertEquals("InnerClass: arg1, arg2", innerNode.toString());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("propertyName", "testProperty");

        InnerClassProperty property = new InnerClassProperty(null, null, null, null);
        JavaType innerClassType = new TypeFactory().constructType("com.test.InnerClass");
        property.setType(innerClassType);

        property.setValueDeserializer(new StdDeserializer.StdValueDeserializer(InnerClass.class));

        ObjectNode innerNode = mapper.createObjectNode();
        innerNode.put("arg1", "value1");
        innerNode.put("arg2", "value2");
        rootNode.put("value", innerNode);

        Object result = property.deserializeSetAndReturn(mapper.parserForObject(rootNode), new DeserializationContext(), new Object());
        assertInstanceOf(InnerClass.class, result);

        InnerClass innerInstance = (InnerClass) result;
        assertEquals("value1", innerInstance.getArg1());
        assertEquals("value2", innerInstance.getArg2());
    }

    @Test
    public void testSetValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        InnerClassProperty property = new InnerClassProperty(null, null, null, null);
        JavaType innerClassType = new TypeFactory().constructType("com.test.InnerClass");
        property.setType(innerClassType);
        property.setValueDeserializer(new StdDeserializer.StdValueDeserializer(String.class));

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("propertyName", "testProperty");
        rootNode.put("value", "testValue");

        Object result = property.deserializeSetAndReturn(mapper.parserForObject(rootNode), new DeserializationContext(), new Object());
        assertEquals("testValue", result);
    }

    @Test
    public void testWriteReplace() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        InnerClassProperty property = new InnerClassProperty(null, null, null, null);
        JavaType innerClassType = new TypeFactory().constructType("com.test.InnerClass");
        property.setType(innerClassType);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("propertyName", "testProperty");

        Object innerInstance = property.get("testProperty");
        assertNull(innerInstance);

        Object result = property.writeReplace(new Object());
        assertNotNull(result);
        assertTrue(result instanceof InnerClassProperty);

        InnerClassProperty deserializedProperty = mapper.readerForObject(new ObjectNode(mapper.getFactory().createObjectNode()))
                .findValue1("InnerClassProperty");
        assertEquals(property, deserializedProperty);
    }

    @Test
    public void testReadResolve() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        InnerClassProperty property = new InnerClassProperty(null, null, null, null);
        JavaType innerClassType = new TypeFactory().constructType("com.test.InnerClass");
        property.setType(innerClassType);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("propertyName", "testProperty");

        Object innerInstance = property.get("testProperty");
        assertNull(innerInstance);

        Object result = property.readResolve();
        assertNotNull(result);
        assertTrue(result instanceof InnerClassProperty);

        InnerClassProperty deserializedProperty = (InnerClassProperty) result;
        assertEquals(property, deserializedProperty);
    }

    @Test
    public void testDelegatePropertyAccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        InnerClassProperty property = new InnerClassProperty(null, null, null, null);
        JavaType innerClassType = new TypeFactory().constructType("com.test.InnerClass");
        property.setType(innerClassType);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("propertyName", "testProperty");

        Object innerInstance = property.get("testProperty");
        assertNull(innerInstance);

        property.setValue(new Object(), new Object());
        innerInstance = property.get("testProperty");
        assertNotNull(innerInstance);

        ObjectNode innerNode = mapper.writerWithView(new SimpleBeanPropertyFilter("testProperty")).writeValueAsTree(innerInstance);
        assertNotNull(innerNode);
        assertEquals("InnerClass: arg1, arg2", innerNode.toString());

        property.set(new Object(), null);
        innerInstance = property.get("testProperty");
        assertNull(innerInstance);
    }

    @Test
    public void testSetterMethods() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        InnerClassProperty property = new InnerClassProperty(null, null, null, null);
        JavaType innerClassType = new TypeFactory().constructType("com.test.InnerClass");
        property.setType(innerClassType);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("propertyName", "testProperty");

        property.setValueDeserializer(new StdDeserializer.StdValueDeserializer(String.class));

        property.setValue(new Object(), "testValue");
        Object innerInstance = property.get("testProperty");
        assertEquals("testValue", innerInstance.toString());

        JavaType innerClassPropertyType = new TypeFactory().constructType("com.test.OuterClass$InnerClassProperty");
        InnerClassProperty innerProperty = new InnerClassProperty(null, null, null, null);
        innerProperty.setType(innerClassType);
        innerProperty.setName("innerTestProperty");
        innerProperty.setValueDeserializer(new StdDeserializer.StdValueDeserializer(String.class));
        property.setDelegate(innerProperty);

        property.setValue(new Object(), "testDelegateValue");
        innerInstance = property.get("innerTestProperty");
        assertEquals("testDelegateValue", innerInstance.toString());

        property.setName(new PropertyName("newTestPropertyName"));
        String newTestPropertyName = property.getName().getName();
        assertEquals("newTestPropertyName", newTestPropertyName);

        property.setValueDeserializer(new StdDeserializer.StdValueDeserializer(InnerClass.class));
        property.setValue(new Object(), new Object());
        innerInstance = property.get("newTestPropertyName");
        assertNotNull(innerInstance);
    }

    private JsonNode asJson(Object[] values) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(values);
    }
}