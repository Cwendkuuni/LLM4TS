package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class InnerClassPropertyTest {
    @Test
    public void testBasic() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test regular SettableBeanProperty instance
        SettableBeanProperty delegate = new SimpleBeanProperty("delegate");
        InnerClassProperty property = new InnerClassProperty(delegate, null);

        // Test deserialization
        String json = "{\"delegate\":{\"class\":\"java.lang.String\",\"name\":\"delegate\",\"value\":\"foo\"}}";
        Object readValue = mapper.readValue(json, Object.class);
        assertEquals("foo", readValue);
        assertEquals("delegate", property._delegate.getName());

        // Test setting value via deserializeAndSet
        json = "{\"delegate\":null}";
        Object instance = mapper.readValue(json, Object.class);
        property.deserializeAndSet(mapper.getFactory().createParser(json), new TestContext(), instance);
        assertEquals("", property._delegate.getValue());

        // Test setting value via constructor
        json = "{\"delegate\":{\"class\":\"java.lang.String\",\"name\":\"delegate\",\"value\":\"foo\"}}";
        Object instanceWithValue = mapper.readValue(json, Object.class);
        assertEquals("foo", property._delegate.getValue());

        // Test JDK serialization
        Object writerValue = new InnerClassProperty(property, null);
        Object writeReplace = writerValue.writeReplace();
        assertEquals(writerValue, writeReplace);

        // Test setting value via setter
        Object instanceToSet = new Object();
        property.set(instanceToSet, "bar");
        assertEquals("bar", property._delegate.getValue());
    }

    @Test
    public void testAnnotatedConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Class<?> innerClass = ClassUtil.findConcreteSubclass(MyInnerClass.class);
        Constructor<?> innerClassConstructor = innerClass.getDeclaredConstructor();
        AnnotatedConstructor annotatedConstructor = mapper.constructorAnnotationIntrospector().findConstructor(innerClass, innerClassConstructor);

        SettableBeanProperty delegate = new SimpleBeanProperty("delegate");
        InnerClassProperty property = new InnerClassProperty(delegate, annotatedConstructor);

        // Test deserialization
        String json = "{\"delegate\":{\"class\":\"com.fasterxml.jackson.databind.deser.impl.MyInnerClass\",\"name\":\"delegate\",\"value\":{\"class\":\"java.lang.String\",\"value\":\"foo\"}}}";
        Object readValue = mapper.readValue(json, Object.class);
        MyInnerClass innerClassInstance = (MyInnerClass) readValue;
        assertEquals("foo", innerClassInstance.getValue());
        assertEquals("delegate", property._delegate.getName());

        // Test setting value via deserializeAndSet
        json = "{\"delegate\":null}";
        Object instance = mapper.readValue(json, Object.class);
        property.deserializeAndSet(mapper.getFactory().createParser(json), new TestContext(), instance);
        assertNull(property._delegate.getValue());

        // Test setting value via constructor
        json = "{\"delegate\":{\"class\":\"com.fasterxml.jackson.databind.deser.impl.MyInnerClass\",\"name\":\"delegate\",\"value\":{\"class\":\"java.lang.String\",\"value\":\"foo\"}}}";
        Object instanceWithValue = mapper.readValue(json, Object.class);
        assertEquals("foo", property._delegate.getValue());

        // Test JDK serialization
        Object writerValue = new InnerClassProperty(property, annotatedConstructor);
        Object writeReplace = writerValue.writeReplace();
        assertEquals(writerValue, writeReplace);

        // Test setting value via setter
        Object instanceToSet = new Object();
        property.set(instanceToSet, new Object());
        MyInnerClass innerClassInstanceToSet = (MyInnerClass) instanceToSet;
        assertNull(innerClassInstanceToSet.getValue());
    }
}

class MyInnerClass {
    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

class SimpleBeanProperty implements SettableBeanProperty {
    private final String name;

    public SimpleBeanProperty(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return "";
    }

    @Override
    public void setValue(Object value) {
    }

    @Override
    public AnnotatedMember getMember() {
        return null;
    }

    @Override
    public Object getValueDeserializer() {
        return null;
    }

    @Override
    public void setValueDeserializer(JsonDeserializer<?> v) {
    }

    @Override
    public PropertyName getName(PropertyName name) {
        return name;
    }

    @Override
    public void assignIndex(int index) {
    }

    @Override
    public int getPropertyIndex() {
        return -1;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotations) {
        return null;
    }
}

class TestContext extends DeserializationContext {
}