package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);
        CreatorCollector collector = mapper.readerForType(beanDesc).readValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(CreatorCollector.class, collector.getClass());
    }

    @Test
    public void testStringCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);
        String creatorName = "CreatorString";
        ObjectNode node = mapper.createObjectNode();
        node.put("className", CreatorString.class.getName());
        node.put("value", creatorName);

        CreatorCollector collector = mapper.readerForType(beanDesc).readValue(node);
        assertEquals(Integer.class, collector.getClassForCreator(0, CreatorCollector.C_STRING));
        assertEquals(creatorName, collector.getCreatorName(0, CreatorCollector.C_STRING));
    }

    @Test
    public void testIntCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);
        int creatorValue = 1;

        CreatorCollector collector = mapper.readerForType(beanDesc)
                .withValue("creator.", new AnnotatedClass.StdCreatorAnnotation(CreatorInt.class))
                .withValue("creator.value", creatorValue)
                .readValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(Integer.TYPE, collector.getClassForCreator(0, CreatorCollector.C_INT));
        assertEquals(creatorValue, collector.getIntCreatorValue(0));
    }

    @Test
    public void testLongCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);
        long creatorValue = 1L;

        CreatorCollector collector = mapper.readerForType(beanDesc)
                .withValue("creator.", new AnnotatedClass.StdCreatorAnnotation(CreatorLong.class))
                .withValue("creator.value", creatorValue)
                .readValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(Long.TYPE, collector.getClassForCreator(0, CreatorCollector.C_LONG));
        assertEquals(creatorValue, collector.getLongCreatorValue(0));
    }

    @Test
    public void testDoubleCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);
        double creatorValue = 1.0;

        CreatorCollector collector = mapper.readerForType(beanDesc)
                .withValue("creator.", new AnnotatedClass.StdCreatorAnnotation(CreatorDouble.class))
                .withValue("creator.value", creatorValue)
                .readValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(Double.TYPE, collector.getClassForCreator(0, CreatorCollector.C_DOUBLE));
        assertEquals(creatorValue, collector.getDoubleCreatorValue(0));
    }

    @Test
    public void testBooleanCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);
        boolean creatorValue = true;

        CreatorCollector collector = mapper.readerForType(beanDesc)
                .withValue("creator.", new AnnotatedClass.StdCreatorAnnotation(CreatorBoolean.class))
                .withValue("creator.value", creatorValue)
                .readValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(Boolean.TYPE, collector.getClassForCreator(0, CreatorCollector.C_BOOLEAN));
        assertEquals(creatorValue, collector.getBooleanCreatorValue(0));
    }

    @Test
    public void testDelegateCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);
        Class<?> delegateType = String[].class;

        CreatorCollector collector = mapper.readerForType(beanDesc)
                .withValue("creator.", new AnnotatedClass.StdCreatorAnnotation(CreatorDelegate.class))
                .withValue("delegateType", delegateType)
                .readValue(new ObjectNode(mapper.createObjectNode()));
        assertEquals(delegateType, collector.getClassForCreator(0, CreatorCollector.C_DELEGATE));
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);
        BeanDescription propertyDesc = mapper.getTypeFactory().constructType(PropertyCreator.class);
        AnnotatedClass creatorClass = mapper.getTypeFactory().constructType(PropertyCreator.class.getName());
        String propertyName = "propertyName";
        ObjectNode node = mapper.createObjectNode();
        node.put("className", propertyDesc.getRawClass().getName());
        node.put("name", propertyName);

        CreatorCollector collector = mapper.readerForType(beanDesc)
                .withValue("creator.", new AnnotatedClass.StdCreatorAnnotation(CreatorProperties.class))
                .withValue("creatorProperties", new AnnotatedWithParams[]{new AnnotatedWithParams(creatorClass, new AnnotatedMember[]{new AnnotatedMember(propertyDesc, new BeanPropertyDefinition(propertyName))})})
                .readValue(node);
        TypeReference typeRef = new TypeReference<CreatorProperties>() {}.getType();
        assertEquals(typeRef, collector.getClassForCreator(0, CreatorCollector.C_PROPS).getType());
        assertEquals(propertyName, collector.getPropertyCreatorName(0));
    }

    @Test
    public void testMultipleCreators() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("className", CreatorMultiple.class.getName());
        node.put("value", "multipleValue");

        CreatorCollector collector = mapper.readerForType(beanDesc).readValue(node);
        assertEquals(CreatorMultiple.class, collector.getClass());
        assertNotNull(collector.getDefaultCreator());
        assertNotNull(collector.getStringCreator());
        assertNotNull(collector.getIntCreator());
        assertNotNull(collector.getLongCreator());
        assertNotNull(collector.getDoubleCreator());
        assertNotNull(collector.getBooleanCreator());
        assertNotNull(collector.getDelegateCreator());
        assertNotNull(collector.getPropertyBasedCreator());
    }

    @Test
    public void testExplicitCreators() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("className", CreatorExplicit.class.getName());
        node.put("value", "explicitValue");

        CreatorCollector collector = mapper.readerForType(beanDesc)
                .withValue("creator.", new AnnotatedClass.StdCreatorAnnotation(CreatorExplicit.class))
                .readValue(node);
        assertEquals(CreatorExplicit.class, collector.getClass());
        assertEquals("explicitValue", collector.getStringCreatorValue(0));
        assertEquals(CreatorExplicit.class.getName(), collector.getClassForCreator(0, CreatorCollector.C_STRING).getName());
        assertNull(collector.getIntCreator());
        assertNull(collector.getLongCreator());
        assertNull(collector.getDoubleCreator());
        assertNull(collector.getBooleanCreator());
        assertNull(collector.getDelegateCreator());
        assertNull(collector.getPropertyBasedCreator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreators() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(CreatorCollector.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("className", CreatorDuplicate.class.getName());
        node.put("value", "duplicateValue");

        CreatorCollector collector = mapper.readerForType(beanDesc)
                .withValue("creator.", new AnnotatedClass.StdCreatorAnnotation(CreatorDuplicate.class))
                .withValue("creator.", new AnnotatedClass.StdCreatorAnnotation(CreatorDuplicate.class))
                .readValue(node);
    }
}