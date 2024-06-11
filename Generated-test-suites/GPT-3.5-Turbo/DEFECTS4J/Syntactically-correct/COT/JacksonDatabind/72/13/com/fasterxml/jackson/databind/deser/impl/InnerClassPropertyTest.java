
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        assertNotNull(innerClassProperty);
        assertSame(delegate, innerClassProperty._delegate);
        assertSame(ctor, innerClassProperty._creator);
    }

    @Test
    public void testDeserializationAndSet() throws Exception {
        // Initialize the necessary objects
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        // Test with JsonToken.VALUE_NULL
        jp.setCurrentToken(JsonToken.VALUE_NULL);
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        innerClassProperty._valueDeserializer = new JsonDeserializer();
        Object value = innerClassProperty._valueDeserializer.getNullValue(ctxt);
        innerClassProperty.set(bean, value);
        // Test with value type deserializer
        jp.setCurrentToken(JsonToken.START_OBJECT);
        innerClassProperty._valueTypeDeserializer = new ValueTypeDeserializer();
        value = innerClassProperty._valueDeserializer.deserializeWithType(jp, ctxt, innerClassProperty._valueTypeDeserializer);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        // Test the usual case
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        Constructor<?> creator = new Constructor<>();
        Object instance = creator.newInstance(bean);
        value = "test";
        innerClassProperty._valueDeserializer.deserialize(jp, ctxt, value);
        innerClassProperty.set(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        // Initialize the necessary objects
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        // Test the usual case
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        innerClassProperty._valueDeserializer = new JsonDeserializer();
        Object value = innerClassProperty._valueDeserializer.deserialize(jp, ctxt);
        Object result = innerClassProperty.setAndReturn(instance, value);
        assertSame(instance, result);
    }

    @Test
    public void testWithMethods() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        PropertyName newName = new PropertyName();
        assertSame(innerClassProperty, innerClassProperty.withName(newName));
        JsonDeserializer<?> deser = new JsonDeserializer();
        assertSame(innerClassProperty, innerClassProperty.withValueDeserializer(deser));
        innerClassProperty.assignIndex(1);
        assertEquals(1, innerClassProperty.getPropertyIndex());
        Class<A> acls = A.class;
        assertSame(delegate.getAnnotation(acls), innerClassProperty.getAnnotation(acls));
        assertSame(delegate.getMember(), innerClassProperty.getMember());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInnerClassPropertyConstructor() {
        InnerClassProperty src = new InnerClassProperty(null, null);
        AnnotatedConstructor ann = null;
        new InnerClassProperty(src, ann);
    }

}

class SettableBeanProperty {
    public void set(Object instance, Object value) {}
    public Object setAndReturn(Object instance, Object value) { return null; }
    public <A extends Annotation> A getAnnotation(Class<A> acls) { return null; }
    public AnnotatedMember getMember() { return null; }
    public void assignIndex(int index) {}
    public int getPropertyIndex() { return 0; }
}

class PropertyName {}

class JsonDeserializer {}
class JsonParser {
    private JsonToken token;

    public void setCurrentToken(JsonToken token) {
        this.token = token;
    }

    public JsonToken getCurrentToken() {
        return token;
    }
}

class DeserializationContext {}

class ObjectMapper {}

class JsonToken {
    public static final JsonToken VALUE_NULL = new JsonToken();
    public static final JsonToken START_OBJECT = new JsonToken();
    public static final JsonToken VALUE_STRING = new JsonToken();
}

class ValueTypeDeserializer {}

class A {}

class AnnotatedConstructor {
    private Constructor<?> constructor;

    public AnnotatedConstructor(Object obj, Constructor<?> constructor, Object selection, Object empty) {
        this.constructor = constructor;
    }

    public Constructor<?> getAnnotated() {
        return constructor;
    }
}

class Constructor<T> {
    public T newInstance(Object obj) throws Exception {
        return null;
    }
}
