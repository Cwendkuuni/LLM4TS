package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    private ObjectNode rootNode;
    private ObjectMapper mapper;
    private InnerClassProperty innerClassProperty;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        rootNode = mapper.createObjectNode();
        Class<?> innerClass = DummyInnerClass.class;
        Object value = mapper.readValue(getClass().getResource("/testInnerClass.json"), innerClass);
        innerClassProperty = new InnerClassProperty(null, innerClass.getDeclaredConstructor());
        innerClassProperty = new InnerClassProperty(innerClassProperty, mapper.findValueDeserializer(innerClass));
    }

    @Test
    public void testInnerClassPropertyConstruction() {
        assertNotNull(innerClassProperty);
        assertEquals(innerClassProperty.getClass(), InnerClassProperty.class);
    }

    @Test
    public void testDelegateProperty() throws Exception {
        assertNull(innerClassProperty._delegate);
        SettableBeanProperty delegate = new DummySettableBeanProperty();
        innerClassProperty = new InnerClassProperty(delegate, innerClassProperty._creator);
        assertEquals(innerClassProperty._delegate, delegate);
    }

    @Test
    public void testCreatorProperty() throws Exception {
        Constructor<?> constructor = innerClassProperty._creator;
        assertNotNull(constructor);
        assertEquals(constructor.getClass(), Constructor.class);
    }

    @Test
    public void testAnnotatedConstructorProperty() throws Exception {
        assertNull(innerClassProperty._annotated);
        AnnotatedConstructor<?> annotated = mapper.constructAnnotated(innerClassProperty._creator, null);
        innerClassProperty = new InnerClassProperty(innerClassProperty, annotated);
        assertEquals(innerClassProperty._annotated, annotated);
    }

    @Test
    public void testSetName() throws Exception {
        PropertyName newName = PropertyName.construct("newName");
        innerClassProperty = new InnerClassProperty(innerClassProperty, newName);
        assertEquals(innerClassProperty.getName(), newName);
    }

    @Test
    public void testSetValueDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = mapper.findValueDeserializer(Object.class);
        innerClassProperty = new InnerClassProperty(innerClassProperty, deserializer);
        assertEquals(innerClassProperty.getValueDeserializer(), deserializer);
    }

    @Test
    public void testSetIndex() throws Exception {
        int index = 0;
        innerClassProperty.assignIndex(index);
        assertEquals(innerClassProperty.getPropertyIndex(), index);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        Annotation annotation = mapper.getTypeFactory().constructType(Annotation.class).newInstance();
        innerClassProperty.setAnnotation(annotation);
        assertSame(innerClassProperty.getAnnotation(Annotation.class), annotation);
    }

    @Test
    public void testGetMember() throws Exception {
        Field field = innerClassProperty.getMember().getMember();
        assertNotNull(field);
        assertEquals(field.getName(), "innerField");
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        rootNode.put("innerField", mapper.valueToTree(new Object()));
        Object bean = mapper.readTree(rootNode).traverse().get("bean").asPojo(Object.class);
        innerClassProperty.deserializeAndSet(mapper.getFactory().createParser(rootNode), mapper, bean);
        Object innerObject = bean.getClass().getDeclaredField("innerField").get(bean);
        assertInstanceOf(innerClass, innerObject.getClass());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object newBean = mapper.readValue(getClass().getResource("/testBean.json"), Object.class);
        Object innerObject = innerClassProperty.deserializeSetAndReturn(mapper.getFactory().createParser(rootNode), mapper, newBean);
        assertEquals(innerObject, newBean.getClass().getDeclaredField("innerField").get(newBean));
    }

    @Test
    public void testSet() throws Exception {
        Object bean = mapper.readValue(getClass().getResource("/testBean.json"), Object.class);
        Object innerObject = mapper.readValue(getClass().getResource("/testInnerClass.json"), innerClass);
        innerClassProperty.set(bean, innerObject);
        Object innerField = bean.getClass().getDeclaredField("innerField").get(bean);
        assertEquals(innerField, innerObject);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object bean = mapper.readValue(getClass().getResource("/testBean.json"), Object.class);
        Object innerObject = mapper.readValue(getClass().getResource("/testInnerClass.json"), innerClass);
        Object result = innerClassProperty.setAndReturn(bean, innerObject);
        assertEquals(innerObject, result);
        Object innerField = bean.getClass().getDeclaredField("innerField").get(bean);
        assertEquals(innerObject, innerField);
    }

    @Test
    public void testReadResolve() throws Exception {
        Object resolvedObject = innerClassProperty.readResolve();
        assertInstanceOf(InnerClassProperty.class, resolvedObject);
        InnerClassProperty resolvedInnerClassProperty = (InnerClassProperty) resolvedObject;
        assertEquals(resolvedInnerClassProperty.getClass(), InnerClassProperty.class);
    }

    @Test
    public void testWriteReplace() throws Exception {
        Object replacedObject = innerClassProperty.writeReplace();
        assertInstanceOf(InnerClassProperty.class, replacedObject);
        InnerClassProperty replacedInnerClassProperty = (InnerClassProperty) replacedObject;
        assertEquals(replacedInnerClassProperty.getClass(), InnerClassProperty.class);
    }

    public static class DummyInnerClass {
        private Object innerField;

        public DummyInnerClass() {
        }
    }

    public static class DummySettableBeanProperty implements SettableBeanProperty {
        private int index = -1;

        @Override
        public void assignIndex(int index) {
            this.index = index;
        }

        @Override
        public int getPropertyIndex() {
            return index;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public <T> T get(Object bean) throws IOException {
            return null;
        }

        @Override
        public <T> void set(Object bean, T value) throws IOException {
        }

        @Override
        public <T> T setAndReturn(Object bean, T value) throws IOException {
            return value;
        }

        @Override
        public void setValueDeserializer(JsonDeserializer<?> deserializer) {
        }

        @Override
        public JsonDeserializer<?> getValueDeserializer() {
            return null;
        }
    }
}