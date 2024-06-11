package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonValueDeserializer;
import com.fasterxml.jackson.databind.deser.impl.AnnotatedConstructor;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyName;
import com.fasterxml.jackson.databind.deser.impl.SettableBeanProperty;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import junit.framework.TestCase;

public class InnerClassPropertyTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty(null, null, null);
        Constructor<Object> ctor = MockConstructor.class.getDeclaredConstructor();
        InnerClassProperty icp = new InnerClassProperty(delegate, ctor);
        assertEquals(delegate, icp._delegate);
        assertNotNull(icp._creator);
    }

    @SuppressWarnings("unchecked")
    public void testInnerConstructor() {
        SettableBeanProperty src = new SettableBeanProperty(null, null, null);
        AnnotatedConstructor ann = new AnnotatedConstructor(null, MockConstructor.class.getDeclaredConstructor(), null, null);
        InnerClassProperty icp = new InnerClassProperty(src, ann);
        assertEquals(src._delegate, icp._delegate);
        assertNull(icp._annotated);
        assertNotNull(icp._creator);
    }

    @SuppressWarnings("unchecked")
    public void testJsonDeserializerConstructor() {
        SettableBeanProperty src = new SettableBeanProperty(null, null, null);
        JsonDeserializer<Object> deser = new StdSerializer<Object>(MockClass.class) {};
        InnerClassProperty icp = new InnerClassProperty(src, deser);
        assertEquals(src._delegate, icp._delegate);
        assertNotNull(icp._creator);
        assertEquals(deser, icp._valueDeserializer);
    }

    @SuppressWarnings("unchecked")
    public void testNameConstructor() {
        SettableBeanProperty src = new SettableBeanProperty(null, null, null);
        PropertyName newName = new PropertyName("test");
        InnerClassProperty icp = new InnerClassProperty(src, newName);
        assertEquals(src._delegate, icp._delegate);
        assertNotNull(icp._creator);
        assertEquals(newName, icp._name);
    }

    @SuppressWarnings("unchecked")
    public void testWithName() {
        SettableBeanProperty src = new SettableBeanProperty(null, null, null);
        PropertyName newName = new PropertyName("test");
        InnerClassProperty icp = new InnerClassProperty(src);
        InnerClassProperty icpNewName = icp.withName(newName);
        assertEquals(src._delegate, icp._delegate);
        assertNotNull(icp._creator);
        assertEquals(newName, icpNewName._name);
    }

    @SuppressWarnings("unchecked")
    public void testWithValueDeserializer() {
        SettableBeanProperty src = new SettableBeanProperty(null, null, null);
        JsonDeserializer<Object> deser = new StdSerializer<Object>(MockClass.class) {};
        InnerClassProperty icp = new InnerClassProperty(src);
        InnerClassProperty icpDeser = icp.withValueDeserializer(deser);
        assertEquals(src._delegate, icp._delegate);
        assertNotNull(icp._creator);
        assertEquals(deser, icpDeser._valueDeserializer);
    }

    public void testDeserializeAndSet() throws IOException {
        Object instance = new MockClass();
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("{\"inner\":null}");
        InnerClassProperty icp = new InnerClassProperty(new SettableBeanProperty(null, null, null), MockConstructor.class.getDeclaredConstructor());
        icp.deserializeAndSet(jp, null, instance);
        assertNull(instance.getClass().getDeclaredField("inner").get(instance));
        jp.nextToken();
        icp.deserializeAndSet(jp, null, instance);
        assertEquals(new MockInnerClass(), instance.getClass().getDeclaredField("inner").get(instance));
    }

    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new MockClass();
        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser("{\"inner\":null}");
        InnerClassProperty icp = new InnerClassProperty(new SettableBeanProperty(null, null, null), MockConstructor.class.getDeclaredConstructor());
        Object result = icp.deserializeSetAndReturn(jp, null, instance);
        assertNull(result);
        jp.nextToken();
        result = icp.deserializeSetAndReturn(jp, null, instance);
        assertEquals(new MockInnerClass(), result);
    }

    public void testSet() throws IOException {
        Object instance = new MockClass();
        InnerClassProperty icp = new InnerClassProperty(new SettableBeanProperty(null, null, null), MockConstructor.class.getDeclaredConstructor());
        icp.set(instance, new MockInnerClass());
        assertEquals(new MockInnerClass(), instance.getClass().getDeclaredField("inner").get(instance));
    }

    public void testSetAndReturn() throws IOException {
        Object instance = new MockClass();
        InnerClassProperty icp = new InnerClassProperty(new SettableBeanProperty(null, null, null), MockConstructor.class.getDeclaredConstructor());
        Object result = icp.setAndReturn(instance, new MockInnerClass());
        assertEquals(new MockInnerClass(), result);
        assertEquals(new MockInnerClass(), instance.getClass().getDeclaredField("inner").get(instance));
    }

    @JsonDeserialize(using = InnerClassDeserializer.class)
    static class MockClass {
        private InnerClass _inner;

        public InnerClass getInner() {
            return _inner;
        }

        public void setInner(InnerClass inner) {
            _inner = inner;
        }

        public static class InnerClass {
            @JsonValueDeserializer(value = InnerValueDeserializer.class)
            private Converter<String, Integer> _innerValue;

            public Converter<String, Integer> getInnerValue() {
                return _innerValue;
            }

            public void setInnerValue(Converter<String, Integer> innerValue) {
                _innerValue = innerValue;
            }
        }
    }

    static class MockConstructor {
        @SuppressWarnings("unused")
        public MockClass(Object obj) {
        }
    }

    static class MockInnerClass {
    }

    static class InnerClassDeserializer extends JsonDeserializer<MockClass> {
        @Override
        public MockClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectBean context = (ObjectBean) p.getCodec().treeAsTokens(p.readValueAsTree());
            MockClass result = new MockClass();
            result.setInner(InnerClassDeserializer.deserializeInner(context, ctxt));
            return result;
        }

        static InnerClass deserializeInner(ObjectBean context, DeserializationContext ctxt) throws IOException {
            InnerClass result = new MockClass.InnerClass();
            PropertyName propertyName = context.findValue("innerValue", PropertyName.class);
            result.setInnerValue(ctxt.findValueSerializer(propertyName.getShortName(), String.class, Integer.class));
            return result;
        }
    }

    static class InnerValueDeserializer extends JsonDeserializer<Converter<String, Integer>> {
        @Override
        public Converter<String, Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new StringToInteger();
        }
    }

    static class StringToInteger implements Converter<String, Integer> {
        @Override
        public Integer convert(String text) {
            return Integer.parseInt(text);
        }
    }
}