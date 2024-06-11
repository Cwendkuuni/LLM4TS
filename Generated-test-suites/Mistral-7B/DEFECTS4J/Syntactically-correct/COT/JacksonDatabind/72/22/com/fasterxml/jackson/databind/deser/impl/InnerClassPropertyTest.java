package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class InnerClassPropertyTest {

    InnerClassProperty property;
    Object bean;
    JsonParser parser;
    ObjectMapper mapper;

    @org.junit.Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        bean = new TestBean();
        parser = mapper.getFactory().createParser("{}");
        property = new InnerClassProperty(new InnerClassProperty(new TestDelegateProperty(), mapper.findConstractor(TestInnerClass.class)), TestInnerClass.class.getDeclaredConstructor()); {
            property.setName(new PropertyName("testProperty"));
            property.setValueDeserializer(mapper.readValueAsValue(getClass().getResourceAsStream("/TestInnerClassDeserializer.json"), new TypeReference<JsonDeserializer<Object>>() {}));
        }
    }

    @org.junit.Test
    public void testConstructor() {
        InnerClassProperty property1 = new InnerClassProperty(new TestDelegateProperty(), mapper.findConstructor(TestInnerClass.class));
        InnerClassProperty property2 = new InnerClassProperty(property1, new AnnotatedConstructor(TestInnerClass.class.getDeclaredConstructor())); {
            property2.setName(new PropertyName("testProperty2"));
        };
        assertEquals(property1, property2);
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        Object value = parser.readValueAs(TestInnerClass.class);
        property.deserializeAndSet(parser, mapper.getDeserializationContext(), bean);
        assertEquals(value, bean.getClass().getDeclaredMethod("getTestInnerClass").invoke(bean));
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        TestInnerClass testInnerClass = property.deserializeSetAndReturn(parser, mapper.getDeserializationContext(), bean);
        assertEquals(testInnerClass, bean.getClass().getDeclaredMethod("getTestInnerClass").invoke(bean));
    }

    @org.junit.Test
    public void testSet() throws IOException {
        Object value = parser.readValueAs(TestInnerClass.class);
        property.set(bean, value);
        assertEquals(value, bean.getClass().getDeclaredMethod("getTestInnerClass").invoke(bean));
    }

    @org.junit.Test
    public void testSetValueDeserializer() {
        property.setValueDeserializer(null);
        AnnotatedMethod setter = new TestDelegateProperty().getClass().getDeclaredMethod("setTestInnerClass", TestInnerClass.class);
        assertNotNull(property.getValueDeserializer());
        property.setValueDeserializer(mapper.findValueDeserializer(TestInnerClass.class, setter));
        assertNotNull(property.getValueDeserializer());
        property.setValueDeserializer(null);
        assertNull(property.getValueDeserializer());
    }

    @org.junit.Test
    public void testGetValueDeserializer() {
        assertNotNull(property.getValueDeserializer());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetValueDeserializerWithNull() {
        property.setValueDeserializer(null);
        property.getValueDeserializer();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testDeserializeAndSetWithNull() throws IOException {
        property.deserializeAndSet(parser, mapper.getDeserializationContext(), null);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testDeserializeSetAndReturnWithNull() throws IOException {
        property.deserializeSetAndReturn(parser, mapper.getDeserializationContext(), null);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSetWithNull() throws IOException {
        property.set(null, parser.readValueAs(TestInnerClass.class));
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testSetAndReturnWithNull() throws IOException {
        property.setAndReturn(null, parser.readValueAs(TestInnerClass.class));
    }

    @org.junit.Test
    public void testWriteReplace() {
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, new AnnotatedConstructor(TestInnerClass.class.getDeclaredConstructor()));
        Object innerClassPropertyWrapped = innerClassProperty.writeReplace();
        assertInstanceOf(InnerClassProperty.class, innerClassPropertyWrapped);
        InnerClassProperty innerClassPropertyUnwrapped = (InnerClassProperty) innerClassPropertyWrapped;
        assertEquals(innerClassProperty, innerClassPropertyUnwrapped);
    }

    @org.junit.Test
    public void testReadResolve() {
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, new AnnotatedConstructor(TestInnerClass.class.getDeclaredConstructor()));
        Object innerClassPropertySerialized = innerClassProperty.writeReplace();
        Object innerClassPropertyDeserialized = innerClassPropertySerialized.readResolve();
        assertInstanceOf(InnerClassProperty.class, innerClassPropertyDeserialized);
        InnerClassProperty innerClassPropertyUnwrapped = (InnerClassProperty) innerClassPropertyDeserialized;
        assertEquals(innerClassProperty, innerClassPropertyUnwrapped);
    }

    @org.junit.Test
    public void testAnnotatedConstructor() {
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        AnnotatedConstructor annotatedConstructor = mapper.findAnnotatedConstructor(TestInnerClass.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, annotatedConstructor);
        assertEquals(annotatedConstructor, innerClassProperty.getAnnotated());
    }

    @org.junit.Test
    public void testAnnotatedConstructorNull() {
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, null);
        assertNull(innerClassProperty.getAnnotated());
    }

    @org.junit.Test
    public void testCreator() {
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        Constructor<TestInnerClass> constructor = TestInnerClass.class.getDeclaredConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, constructor);
        assertEquals(constructor, innerClassProperty.getCreator());
    }

    @org.junit.Test
    public void testCreatorNull() {
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, null);
        assertNull(innerClassProperty.getCreator());
    }

    @org.junit.Test
    public void testGetPrototypeBeanProperty() {
        Object prototypeBean = mapper.readValue(getClass().getResourceAsStream("/TestDelegatePropertyPrototype.json"), TestDelegateProperty.class);
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassPropertyPrototype = innerClassProperty.withValueDeserializer(mapper.findValueDeserializer(TestInnerClass.class, TestDelegateProperty.class.getDeclaredMethod("getTestInnerClass", (Class[]) null)));
        assertEquals(innerClassPropertyPrototype, innerClassProperty.getPrototypeBeanProperty());
        assertEquals(innerClassPropertyPrototype, innerClassPropertyPrototype.getPrototypeBeanProperty());
        assertEquals(prototypeBean, innerClassPropertyPrototype.getDelegate().getTestInnerClass());
        assertEquals(prototypeBean, innerClassPropertyPrototype.getValue());
    }

    @org.junit.Test
    public void testGetPrototypeBeanPropertyNull() {
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, null);
        assertNull(innerClassProperty.getPrototypeBeanProperty());
    }

    @org.junit.Test
    public void testIsPrototypeBeanProperty() {
        Object prototypeBean = mapper.readValue(getClass().getResourceAsStream("/TestDelegatePropertyPrototype.json"), TestDelegateProperty.class);
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassPropertyPrototype = new InnerClassProperty(testDelegateProperty, TestInnerClass.class.getDeclaredConstructor());
        assertTrue(innerClassPropertyPrototype.isPrototypeBeanProperty());
        assertFalse(innerClassProperty.isPrototypeBeanProperty());
    }

    @org.junit.Test
    public void testIsPrototypeBeanPropertyWithPrototypeBeanProperty() {
        Object prototypeBean = mapper.readValue(getClass().getResourceAsStream("/TestDelegatePropertyPrototype.json"), TestDelegateProperty.class);
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassPropertyPrototype = new InnerClassProperty(testDelegateProperty, TestInnerClass.class.getDeclaredConstructor());
        innerClassPropertyPrototype.setPrototypeBeanProperty(innerClassProperty);
        assertTrue(innerClassPropertyPrototype.isPrototypeBeanProperty());
        assertFalse(innerClassProperty.isPrototypeBeanProperty());
    }

    @org.junit.Test
    public void testEquals() {
        TestDelegateProperty testDelegateProperty1 = new TestDelegateProperty();
        TestDelegateProperty testDelegateProperty2 = new TestDelegateProperty();
        InnerClassProperty innerClassProperty1 = new InnerClassProperty(testDelegateProperty1, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassProperty2 = new InnerClassProperty(testDelegateProperty2, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassProperty3 = new InnerClassProperty(testDelegateProperty1, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassProperty4 = new InnerClassProperty(testDelegateProperty2, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassProperty5 = new InnerClassProperty(testDelegateProperty1, TestInnerClass.class.getDeclaredConstructor());
        List<InnerClassProperty> innerClassProperties = Arrays.asList(innerClassProperty1, innerClassProperty2, innerClassProperty3, innerClassProperty4, innerClassProperty5);
        for (InnerClassProperty innerClassProperty : innerClassProperties) {
            for (InnerClassProperty innerClassProperty2 : innerClassProperties) {
                if (innerClassProperty != innerClassProperty2) {
                    assertFalse(innerClassProperty.equals(innerClassProperty2));
                } else {
                    assertTrue(innerClassProperty.equals(innerClassProperty2));
                }
            }
        }
    }

    @org.junit.Test
    public void testHashCode() {
        TestDelegateProperty testDelegateProperty1 = new TestDelegateProperty();
        TestDelegateProperty testDelegateProperty2 = new TestDelegateProperty();
        InnerClassProperty innerClassProperty1 = new InnerClassProperty(testDelegateProperty1, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassProperty2 = new InnerClassProperty(testDelegateProperty2, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassProperty3 = new InnerClassProperty(testDelegateProperty1, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassProperty4 = new InnerClassProperty(testDelegateProperty2, TestInnerClass.class.getDeclaredConstructor());
        InnerClassProperty innerClassProperty5 = new InnerClassProperty(testDelegateProperty1, TestInnerClass.class.getDeclaredConstructor());
        List<InnerClassProperty> innerClassProperties = Arrays.asList(innerClassProperty1, innerClassProperty2, innerClassProperty3, innerClassProperty4, innerClassProperty5);
        for (InnerClassProperty innerClassProperty : innerClassProperties) {
            for (InnerClassProperty innerClassProperty2 : innerClassProperties) {
                if (innerClassProperty != innerClassProperty2) {
                    assertNotEquals(innerClassProperty.hashCode(), innerClassProperty2.hashCode());
                }
            }
            InnerClassProperty innerClassPropertyClone = new InnerClassProperty(innerClassProperty);
            assertEquals(innerClassProperty.hashCode(), innerClassPropertyClone.hashCode());
        }
    }

    @org.junit.Test
    public void testToString() {
        TestDelegateProperty testDelegateProperty = new TestDelegateProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(testDelegateProperty, TestInnerClass.class.getDeclaredConstructor());
        assertEquals("InnerClassProperty[name='testProperty', getter=class com.example.TestInnerClass, setter=class com.example.TestDelegateProperty, creator=class com.example.TestInnerClass]", innerClassProperty.toString());
    }

    static class TestBean {
        TestInnerClass testInnerClass;
    }

    static class TestDelegateProperty {
        TestInnerClass testInnerClass;

        public TestDelegateProperty() {
            this.testInnerClass = new TestInnerClass();
        }

        public TestInnerClass getTestInnerClass() {
            return testInnerClass;
        }

        public void setTestInnerClass(TestInnerClass testInnerClass) {
            this.testInnerClass = testInnerClass;
        }
    }

    static class TestInnerClass {
        int testInt;

        public int getTestInt() {
            return testInt;
        }

        public void setTestInt(int testInt) {
            this.testInt = testInt;
        }
    }

    static class TestInnerClassDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            Converter<String, TestInnerClass> converter = (String s) -> new TestInnerClass();
            return converter.convertValue(p, new TypeReference<TestInnerClass>() {});
        }
    }
}