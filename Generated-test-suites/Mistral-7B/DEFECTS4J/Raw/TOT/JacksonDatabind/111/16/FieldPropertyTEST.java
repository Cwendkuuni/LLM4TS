package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

public class FieldPropertyTest {

    private ObjectMapper mapper;
    private Foo foo;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setAnnotationIntrospector(new StandardAnnotationIntrospector());
        foo = new Foo();
    }

    @Test
    public void testNormalDeserialization() throws IOException {
        String json = "{\"bar\": 42}";
        Foo deserializedFoo = mapper.readValue(json, Foo.class);
        assertEquals(42, deserializedFoo.getBar());
    }

    @Test
    public void testNullDeserialization() throws IOException {
        String json = "{\"bar\": null}";
        Foo deserializedFoo = mapper.readValue(json, Foo.class);
        assertNull(deserializedFoo.getBar());
    }

    @Test
    public void testEmptyDeserialization() throws IOException {
        String json = "{}";
        Foo deserializedFoo = mapper.readValue(json, Foo.class);
        assertNull(deserializedFoo.getBar());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJson() throws IOException {
        String json = "{\"bar\":}";
        mapper.readValue(json, Foo.class);
    }

    @Test
    public void testDeserializationWithAnnotation() throws IOException {
        String json = "{\"@MyAnnotation\": 42, \"bar\": 42}";
        Foo deserializedFoo = mapper.readValue(json, Foo.class);
        assertTrue(deserializedFoo.hasAnnotation());
    }

    @Test
    public void testSerializingAndDeserializing() throws IOException {
        Foo foo = new Foo(42, "test");
        String json = mapper.writeValueAsString(foo);
        Foo deserializedFoo = mapper.readValue(json, Foo.class);
        assertEquals(foo, deserializedFoo);
    }

    @Test
    public void testSettingValue() throws IOException, IllegalAccessException {
        Field field = Foo.class.getField("bar");
        ObjectMapper mapper = new ObjectMapper();
        Foo foo = new Foo();
        mapper.writeValue(foo, 42);
        field.set(foo, 43);
        assertEquals(43, mapper.readValue(mapper.writeValueAsString(foo), Foo.class).getBar());
    }

    @Test
    public void testNullValueSetting() throws IOException, IllegalAccessException {
        Field field = Foo.class.getField("bar");
        ObjectMapper mapper = new ObjectMapper();
        Foo foo = new Foo();
        mapper.writeNullValue(foo, "bar");
        assertNull(field.get(foo));
        assertEquals(null, mapper.readValue(mapper.writeValueAsString(foo), Foo.class).getBar());
    }

    @Test
    public void testAccessingFieldWithPrivateSetter() throws IOException, IllegalAccessException {
        Field field = Foo.class.getDeclaredField("bar");
        field.setAccessible(true);
        ObjectMapper mapper = new ObjectMapper();
        Foo foo = new Foo();
        mapper.writeValue(foo, 42);
        field.set(foo, 43);
        field.setAccessible(false);
        assertEquals(43, mapper.readValue(mapper.writeValueAsString(foo), Foo.class).getBar());
    }

    @Test(expected = IllegalAccessException.class)
    public void testAccessingPrivateFieldWithNoSetter() throws IOException {
        Field field = Foo.class.getDeclaredField("privateBar");
        field.setAccessible(true);
        ObjectMapper mapper = new ObjectMapper();
        Foo foo = new Foo();
        mapper.writeValue(foo, 42);
        field.set(foo, 43);
        field.setAccessible(false);
    }

    @Test
    public void testSerializingAndDeserializingWithCustomNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setNullValueProvider(new CustomNullValueProvider());
        Foo foo = new Foo(42, "test");
        String json = mapper.writeValueAsString(foo);
        Foo deserializedFoo = mapper.readValue(json, Foo.class);
        assertEquals(foo, deserializedFoo);
    }

    @After
    public void cleanup() {
        mapper = null;
        foo = null;
    }

    static class Foo {
        private int bar;
        private String privateBar;

        public Foo() {
        }

        public Foo(int bar) {
            this.bar = bar;
        }

        public Foo(int bar, String privateBar) {
            this.bar = bar;
            this.privateBar = privateBar;
        }

        public int getBar() {
            return bar;
        }

        public void setBar(int bar) {
            this.bar = bar;
        }

        public boolean hasAnnotation() {
            return getClass().isAnnotationPresent(MyAnnotation.class);
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "bar=" + bar +
                    ", privateBar='" + privateBar + '\'' +
                    '}';
        }

        public static class MyAnnotation {
        }
    }

    static class CustomNullValueProvider extends NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext context) {
            return new Object();
        }
    }
}