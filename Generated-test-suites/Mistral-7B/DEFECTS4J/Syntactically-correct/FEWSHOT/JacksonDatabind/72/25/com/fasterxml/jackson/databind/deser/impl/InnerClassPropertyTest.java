package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Unit tests for InnerClassProperty class
 */
public class InnerClassPropertyTest {

    @Test
    public void testInnerClassProperty() throws Exception {
        Class<InnerClass> innerClass = InnerClass.class;
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, innerClass.getDeclaredConstructor());

        // Test basic functionality
        assertEquals(innerClassProperty.getClass(), InnerClassProperty.class);
        assertEquals(innerClassProperty.getName(), "");
        assertNull(innerClassProperty.getValueDeserializer());
        assertNull(innerClassProperty.getMember());
        assertNull(innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);

        // Test constructor with delegate and creator
        SettableBeanProperty delegate = new DummySettableBeanProperty();
        InnerClassProperty innerClassPropertyWithDelegate = new InnerClassProperty(delegate, innerClass.getDeclaredConstructor());
        assertEquals(delegate, innerClassPropertyWithDelegate._delegate);
        assertEquals(innerClassPropertyWithDelegate.getClass(), InnerClassProperty.class);
        assertEquals(innerClassPropertyWithDelegate.getName(), "");
        assertEquals(innerClassPropertyWithDelegate.getValueDeserializer(), null);
        assertNull(innerClassPropertyWithDelegate.getMember());
        assertEquals(delegate.getClass(), innerClassPropertyWithDelegate._delegate.getClass());
        assertEquals(innerClassPropertyWithDelegate._creator, innerClass.getDeclaredConstructor());

        // Test constructor with annotated constructor
        _setupAnnotatedConstructor(innerClass);
        InnerClassProperty innerClassPropertyWithAnnotated = new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(_innerClassPropertyAnnotated));
        assertEquals(innerClassPropertyWithAnnotated.getClass(), InnerClassProperty.class);
        assertEquals(innerClassPropertyWithAnnotated.getName(), "");
        assertEquals(innerClassPropertyWithAnnotated.getValueDeserializer(), null);
        assertNotNull(innerClassPropertyWithAnnotated.getMember());
        assertEquals(innerClassPropertyWithAnnotated._delegate, innerClassProperty);
        assertEquals(innerClassPropertyWithAnnotated._creator, null);
        assertEquals(innerClassPropertyWithAnnotated._annotated, _innerClassPropertyAnnotated);

        // Test constructor with JsonDeserializer
        JsonDeserializer<Object> jsonDeserializer = new StdSerializer<Object>(Object.class) {
            @Override
            public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        };
        InnerClassProperty innerClassPropertyWithDeserializer = new InnerClassProperty(innerClassProperty, jsonDeserializer);
        assertEquals(innerClassPropertyWithDeserializer.getClass(), InnerClassProperty.class);
        assertEquals(innerClassPropertyWithDeserializer.getName(), "");
        assertNotNull(innerClassPropertyWithDeserializer.getValueDeserializer());
        assertNull(innerClassPropertyWithDeserializer.getMember());
        assertEquals(innerClassPropertyWithDeserializer._delegate, innerClassProperty);
        assertEquals(innerClassPropertyWithDeserializer._creator, null);
        assertEquals(innerClassPropertyWithDeserializer._annotated, null);

        // Test constructor with PropertyName
        PropertyName propertyName = PropertyName.construct("testPropertyName");
        InnerClassProperty innerClassPropertyWithPropertyName = new InnerClassProperty(innerClassProperty, propertyName);
        assertEquals(innerClassPropertyWithPropertyName.getClass(), InnerClassProperty.class);
        assertEquals(innerClassPropertyWithPropertyName.getName(), "testPropertyName");
        assertNull(innerClassPropertyWithPropertyName.getValueDeserializer());
        assertNull(innerClassPropertyWithPropertyName.getMember());
        assertEquals(innerClassPropertyWithPropertyName._delegate, innerClassProperty);
        assertEquals(innerClassPropertyWithPropertyName._creator, null);
        assertEquals(innerClassPropertyWithPropertyName._annotated, null);

        // Test deserialization
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"testProperty\":{\"innerClassProperty\":{\"class\":\"com.fasterxml.jackson.databind.deser.impl.InnerClass\",\"name\":\"testName\",\"value\":1}}}}";
        InnerClass expectedInnerClass = new InnerClass("testName", 1);
        InnerClass innerClassDeserialized = mapper.readValue(json, InnerClass.class);
        InnerClassProperty innerClassPropertyDeserialized = mapper.findPropertyValue(innerClassDeserialized, "innerClassProperty");
        assertEquals(innerClassPropertyDeserialized.getValue(), expectedInnerClass);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"testProperty\":{\"innerClassProperty\":{\"class\":\"com.fasterxml.jackson.databind.deser.impl.InnerClass\",\"name\":\"testName\",\"value\":1}}}}";
        InnerClass expectedInnerClass = new InnerClass("testName", 1);

        InnerClassProperty innerClassPropertyWithValueDeserializer = new InnerClassProperty(null, InnerClass.class.getDeclaredConstructor());
        innerClassPropertyWithValueDeserializer.setValueDeserializer(new StdDeserializer<InnerClass>(InnerClass.class) {
            @Override
            public InnerClass deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new InnerClass("testName", 1);
            }
        });

        Object result = mapper.readValue(json, new SimpleBeanBuilder(InnerClass.class).with(innerClassPropertyWithValueDeserializer).build().getRawValue());
        InnerClass innerClassDeserialized = (InnerClass) result;
        assertEquals(innerClassDeserialized.getName(), "testName");
        assertEquals(innerClassDeserialized.getValue(), expectedInnerClass);
    }

    @Test
    public void testWriteReplace() throws Exception {
        Class<InnerClass> innerClass = InnerClass.class;
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, innerClass.getDeclaredConstructor());
        InnerClassProperty writeReplace = innerClassProperty.writeReplace();
        assertInstanceOf(InnerClassProperty.class, writeReplace);
        assertEquals(innerClassProperty.getName(), writeReplace.getName());
        assertEquals(innerClassProperty.getValueDeserializer(), writeReplace.getValueDeserializer());
        assertNull(writeReplace._delegate);
        assertNull(writeReplace._creator);
        assertNull(writeReplace._annotated);
    }

    @Test
    public void testReadResolve() throws Exception {
        Class<InnerClass> innerClass = InnerClass.class;
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, innerClass.getDeclaredConstructor());
        InnerClassProperty readResolve = innerClassProperty.readResolve();
        assertInstanceOf(InnerClassProperty.class, readResolve);
        assertEquals(innerClassProperty.getName(), readResolve.getName());
        assertEquals(innerClassProperty.getValueDeserializer(), readResolve.getValueDeserializer());
        assertNull(readResolve._delegate);
        assertNull(readResolve._creator);
        assertNull(readResolve._annotated);
    }

    @Test
    public void testSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"testProperty\":{\"innerClassProperty\":{\"class\":\"com.fasterxml.jackson.databind.deser.impl.InnerClass\",\"name\":\"testName\",\"value\":1}}}}";
        InnerClass expectedInnerClass = new InnerClass("testName", 1);

        InnerClassProperty innerClassPropertyWithValueDeserializer = new InnerClassProperty(null, InnerClass.class.getDeclaredConstructor());
        InnerClass innerClassDeserialized = mapper.readValue(json, InnerClass.class);
        InnerClassProperty innerClassPropertyInInnerClass = innerClassDeserialized.getInnerClassProperty();
        InnerClass innerClassDeserializedValue = innerClassPropertyInInnerClass.getValue();
        assertEquals(innerClassDeserializedValue.getName(), "testName");
        assertEquals(innerClassDeserializedValue.getValue(), expectedInnerClass);

        ObjectMapper mapper2 = new ObjectMapper();
        InnerClass innerClassToSet = new InnerClass("testName2", 2);
        InnerClassProperty innerClassPropertyToSet = new InnerClassProperty(null, InnerClass.class.getDeclaredConstructor());
        InnerClass innerClassToSetProperty = innerClassPropertyToSet.getValue();
        innerClassPropertyToSet.setValue(innerClassToSetProperty, innerClassToSet);
        InnerClass innerClassDeserializedWithSet = mapper2.readValue(json, InnerClass.class);
        InnerClassProperty innerClassPropertyInInnerClassWithSet = innerClassDeserializedWithSet.getInnerClassProperty();
        InnerClass innerClassDeserializedValueWithSet = innerClassPropertyInInnerClassWithSet.getValue();
        assertEquals(innerClassDeserializedValueWithSet.getName(), "testName2");
        assertNotEquals(innerClassDeserializedValue, innerClassDeserializedValueWithSet);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"testProperty\":{\"innerClassProperty\":{\"class\":\"com.fasterxml.jackson.databind.deser.impl.InnerClass\",\"name\":\"testName\",\"value\":1}}}}";
        InnerClass expectedInnerClass = new InnerClass("testName", 1);

        InnerClassProperty innerClassPropertyWithValueDeserializer = new InnerClassProperty(null, InnerClass.class.getDeclaredConstructor());
        InnerClass innerClassDeserialized = mapper.readValue(json, InnerClass.class);
        InnerClassProperty innerClassPropertyInInnerClass = innerClassDeserialized.getInnerClassProperty();
        Object returnedValue = innerClassPropertyInInnerClass.setAndReturn(innerClassDeserialized, expectedInnerClass);
        assertEquals(returnedValue, expectedInnerClass);
    }

    private static void _setupAnnotatedConstructor(Class<InnerClass> innerClass) {
        _innerClassPropertyAnnotated = new AnnotatedConstructor(new AnnotatedMember(), innerClass.getDeclaredConstructor(), null, null);
    }

    private AnnotatedConstructor _innerClassPropertyAnnotated;

    private static class DummySettableBeanProperty extends SettableBeanProperty {
        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void setValue(Object bean, Object value) throws IOException {
        }

        @Override
        public Object getValueDeserializer() {
            return null;
        }

        @Override
        public void setValueDeserializer(JsonDeserializer<?> deser) {
        }

        @Override
        public PropertyName getName() {
            return new PropertyName("testProperty");
        }
    }
}