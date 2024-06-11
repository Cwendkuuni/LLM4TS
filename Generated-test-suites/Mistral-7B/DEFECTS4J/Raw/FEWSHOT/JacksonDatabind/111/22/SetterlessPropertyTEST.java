package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testGetter() throws Exception {
        Object instance = new Object() {
            Object _collection;

            public Object getCollection() {
                return _collection;
            }

            public void setCollection(Object c) {
                _collection = c;
            }
        };

        SettableBeanProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new PropertyName("collection"), Object.class),
                JsonTypeFactory.instance.constructType(Object.class),
                null, Annotations.empty(), new AnnotatedMethod(
                        new MethodToken(GetterlessPropertyTest.class.getDeclaredMethod("getCollection")))) {
            @Override
            public Object deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                Object value = p.readValueAs(Object.class);
                instance.setCollection(value);
                return null;
            }
        };

        JsonParser parser = new TestJsonParser();
        parser.setValue("[]");
        Object readValue = property.deserializeAndSet(parser, new TestDeserializationContext(), instance);
        assertEquals(JsonToken.END_ARRAY, parser.getCurrentToken());
        assertEquals(new ArrayList<>() {}, instance.getCollection());

        parser.setValue("[1]");
        readValue = property.deserializeAndSet(parser, new TestDeserializationContext(), instance);
        assertEquals(JsonToken.END_ARRAY, parser.getCurrentToken());
        assertEquals(Arrays.asList(1), instance.getCollection());

        parser.setValue("null");
        readValue = property.deserializeAndSet(parser, new TestDeserializationContext(), instance);
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
        assertNull(instance.getCollection());
    }

    @Test
    public void testSetterlessPropertyWithValueDeserializer() throws Exception {
        Object instance = new Object() {
            List<String> _collection;

            public List<String> getCollection() {
                return _collection;
            }

            public void setCollection(List<String> c) {
                _collection = c;
            }
        };

        SettableBeanProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new PropertyName("collection"), List.class),
                JsonTypeFactory.instance.constructType(List.class),
                null, Annotations.empty(), new AnnotatedMethod(
                        new MethodToken(GetterlessPropertyTest.class.getDeclaredMethod("getCollection")))) {
            @Override
            public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
                    TypeDeserializer typeDeserializer) throws IOException {
                return typeDeserializer.deserializeValue(p, ctxt, String.class);
            }

            @Override
            public Object deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                    throws IOException {
                Object value = p.readValueAs(List.class);
                instance.setCollection((List<String>) value);
                return null;
            }
        };

        JsonParser parser = new TestJsonParser();
        parser.setValue("[\"foo\",\"bar\"]");
        Object readValue = property.deserializeAndSet(parser, new TestDeserializationContext(), instance);
        assertEquals(JsonToken.END_ARRAY, parser.getCurrentToken());
        assertEquals(Arrays.asList("foo", "bar"), instance.getCollection());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValue() throws Exception {
        Object instance = new Object();
        SettableBeanProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(new PropertyName("collection"), List.class),
                JsonTypeFactory.instance.constructType(List.class),
                null, Annotations.empty(), new AnnotatedMethod(
                        new MethodToken(GetterlessPropertyTest.class.getDeclaredMethod("getCollection")))) {
        };

        property.set(instance, Arrays.asList("foo"));
    }

    static class TestJsonParser extends JsonParser {
        @Override
        public JsonToken nextToken() throws IOException {
            return JsonToken.VALUE_NULL;
        }

        @Override
        public JsonToken nextValue() throws IOException {
            return null;
        }
    }

    static class TestDeserializationContext extends DeserializationContext {
        @Override
        public Object findValueDeserializer(DeserializationConfig config, BeanProperty property,
                JavaType propertyType) {
            return null;
        }
    }
}