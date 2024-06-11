package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassProperty() throws Exception {
        AnnotatedClass ac = new AnnotatedClass(InnerClassPropertyTest.class, MyClassName.class);
        Constructor<MyClassName> constructor = MyClassName.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        AnnotatedConstructor annotatedConstructor = AnnotatedConstructor.construct(constructor, ac);

        InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(ac.findProperty("property")), annotatedConstructor);

        // Test constructor
        InnerClassProperty copy = new InnerClassProperty(property, annotatedConstructor);
        assertEquals(property, copy);

        // Test withValueDeserializer
        JsonDeserializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};
        InnerClassProperty propertyWithDeserializer = new InnerClassProperty(property, deserializer);
        assertEquals(propertyWithDeserializer, propertyWithDeserializer.withValueDeserializer(deserializer));

        // Test withName
        PropertyName newName = PropertyName.construct("newName");
        InnerClassProperty propertyWithName = new InnerClassProperty(propertyWithDeserializer, newName);
        assertEquals(propertyWithName.getName(), newName);

        // Test deserializeAndSet
        Object bean = new Object();
        JsonParser jp = new JsonParser();
        jp.setValue("{\"property\":\"Test\"}");
        property.deserializeAndSet(jp, new DeserializationContext(), bean);
        assertEquals("Test", property.get(bean));

        // Test deserializeSetAndReturn
        Object result = property.deserializeSetAndReturn(jp, new DeserializationContext(), bean);
        assertEquals("Test", result);

        // Test set and return
        property.set(bean, "Test2");
        assertEquals("Test2", property.get(bean));

        // Test readResolve
        Object readResolve = property.readResolve();
        assertEquals(property, readResolve);

        // Test writeReplace
        Object writeReplace = property.writeReplace();
        assertEquals(property, writeReplace);
    }

    @Test
    public void testInnerClassPropertyWithAnnotation() throws Exception {
        AnnotatedClass ac = new AnnotatedClass(InnerClassPropertyTest.class, MyClassNameWithAnnotation.class);
        Constructor<MyClassNameWithAnnotation> constructor = MyClassNameWithAnnotation.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        AnnotatedConstructor annotatedConstructor = AnnotatedConstructor.construct(constructor, ac);

        InnerClassProperty property = new InnerClassProperty(new SettableBeanProperty(ac.findProperty("property")), annotatedConstructor);

        // Test constructor
        InnerClassProperty copy = new InnerClassProperty(property, annotatedConstructor);
        assertEquals(property, copy);

        // Test withValueDeserializer
        JsonDeserializer<Object> deserializer = new StdSerializer<Object>(Object.class) {};
        InnerClassProperty propertyWithDeserializer = new InnerClassProperty(property, deserializer);
        assertEquals(propertyWithDeserializer, propertyWithDeserializer.withValueDeserializer(deserializer));

        // Test withName
        PropertyName newName = PropertyName.construct("newName");
        InnerClassProperty propertyWithName = new InnerClassProperty(propertyWithDeserializer, newName);
        assertEquals(propertyWithName.getName(), newName);

        // Test deserializeAndSet
        Object bean = new Object();
        JsonParser jp = new JsonParser();
        jp.setValue("{\"property\":{\"annotation\":\"Test\"}}");
        property.deserializeAndSet(jp, new DeserializationContext(), bean);
        assertEquals("Test", property.get(bean).getAnnotation("annotation").getValue());

        // Test deserializeSetAndReturn
        Object result = property.deserializeSetAndReturn(jp, new DeserializationContext(), bean);
        assertEquals("Test", result.getAnnotation("annotation").getValue());

        // Test set and return
        property.set(bean, new MyAnnotation("Test2"));
        assertEquals("Test2", property.get(bean).getAnnotation("annotation").getValue());

        // Test readResolve
        Object readResolve = property.readResolve();
        assertEquals(property, readResolve);

        // Test writeReplace
        Object writeReplace = property.writeReplace();
        assertEquals(property, writeReplace);
    }

    static class MyClassName {
        private Object property;

        MyClassName(Object property) {
            this.property = property;
        }

        Object getProperty() {
            return property;
        }

        void setProperty(Object property) {
            this.property = property;
        }
    }

    static class MyClassNameWithAnnotation {
        @MyAnnotation
        private Object property;

        MyClassNameWithAnnotation(Object property) {
            this.property = property;
        }

        Object getProperty() {
            return property;
        }

        void setProperty(Object property) {
            this.property = property;
        }
    }

    @MyAnnotation
    static class MyAnnotation {
        private String value;

        MyAnnotation(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }
}