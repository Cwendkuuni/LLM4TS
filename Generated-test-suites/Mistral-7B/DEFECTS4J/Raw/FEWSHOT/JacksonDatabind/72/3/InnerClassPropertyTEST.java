package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

public class InnerClassPropertyTest extends ObjectMapperTestBase {

    @Test
    public void testConstructorWithDelegateAndCreator() {
        // create test object
        final Object dummyObject = new Object() {
            @InnerClassProperty
            private InnerClass innerClass = new InnerClass();
        };
        final Constructor<InnerClass> constructor = InnerClass.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // test constructor
        InnerClassProperty property = new InnerClassProperty(new InnerClassProperty(dummyObject.innerClass, constructor), constructor);

        // check properties
        assertSame(dummyObject.innerClass, property.getDelegate().getValue());
        assertSame(constructor, property._creator);
    }

    @Test
    public void testConstructorWithDelegateAndAnnotatedConstructor() throws IOException {
        // create test object
        final Object dummyObject = new Object() {
            @InnerClassProperty
            private InnerClass innerClass = new InnerClass();
        };
        final AnnotatedConstructor<InnerClass> annConstructor = Introspector.deintrospect(InnerClass.class.getDeclaredConstructor());

        // test constructor
        InnerClassProperty property = new InnerClassProperty(new InnerClassProperty(dummyObject.innerClass, null), annConstructor);

        // check properties
        assertSame(dummyObject.innerClass, property.getDelegate().getValue());
        assertSame(annConstructor, property._annotated);
        assertNull(property._creator);
    }

    @Test
    public void testConstructorWithDelegateAndJsonDeserializer() throws IOException {
        // create test object
        final Object dummyObject = new Object();
        final InnerClass innerClass = new InnerClass();
        final JsonDeserializer<InnerClass> deserializer = new StdDeserializer<InnerClass>() {};

        // test constructor
        InnerClassProperty property = new InnerClassProperty(new InnerClassProperty(dummyObject, innerClass), deserializer);

        // check properties
        assertSame(innerClass, property.getDelegate().getValue());
        assertNull(property._annotated);
        assertSame(deserializer, property._delegate.getValueDeserializer());
    }

    @Test
    public void testConstructorWithDelegateAndPropertyName() {
        // create test object
        final Object dummyObject = new Object();
        final InnerClassProperty innerClassProperty = new InnerClassProperty(new InnerClassProperty(dummyObject, "test"), null);

        // test constructor
        InnerClassProperty property = new InnerClassProperty(innerClassProperty, new PropertyName("test2"));

        // check properties
        assertSame(innerClassProperty, property.getDelegate());
        assertSame("test2", property.getName());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        // create test object
        final ObjectMapper mapper = new ObjectMapper();
        final Object dummyObject = new Object();
        final InnerClass innerClass = new InnerClass();
        final JsonDeserializer<InnerClass> deserializer = new StdDeserializer<InnerClass>() {};

        // set value deserializer
        final InnerClassProperty property = new InnerClassProperty(new InnerClassProperty(dummyObject, innerClass), deserializer);

        // test deserialization
        mapper.registerValTypes(InnerClass.class);
        mapper.registerDeserializer(InnerClass.class, deserializer);
        JsonNode node = mapper.readTree("{\"test\":{\"class\":\"com.fasterxml.jackson.databind.deser.impl.InnerClass\",\"value\":{\"foo\":\"bar\"}})");
        Object result = mapper.readerForUpdating(dummyObject).readValueAs(InnerClass.class);

        // check result
        assertTrue(result instanceof InnerClass);
        assertEquals("bar", ((InnerClass) result).getValue().toString());

        // check property
        assertEquals(result, property.getDelegate().getValue());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // create test object
        final ObjectMapper mapper = new ObjectMapper();
        final Object dummyObject = new Object();
        final InnerClass innerClass = new InnerClass();
        final JsonDeserializer<InnerClass> deserializer = new StdDeserializer<InnerClass>() {};

        // set value deserializer
        final InnerClassProperty property = new InnerClassProperty(new InnerClassProperty(dummyObject, innerClass), deserializer);

        // test deserialization
        mapper.registerValTypes(InnerClass.class);
        mapper.registerDeserializer(InnerClass.class, deserializer);
        Object result = mapper.readValue(new ByteArrayInputStream(new ReadableByteChannel() {
            @Override
            public int read(ByteBuffer dest, long offset, long length) throws IOException {
                dest.put(("{\"class\":\"com.fasterxml.jackson.databind.deser.impl.InnerClass\",\"value\":{\"foo\":\"bar\"}}").getBytes());
                return -1;
            }
        }.channel()), InnerClass.class);

        // check result
        assertTrue(result instanceof InnerClass);
        assertEquals("bar", ((InnerClass) result).getValue().toString());

        // check property
        assertEquals(result, property.getDelegate().getValue());
        assertSame(result, property.getValue());
    }

    @Test
    public void testSerializeAndReadResolve() throws IOException {
        // create test object
        final ObjectMapper mapper = new ObjectMapper();
        final Object dummyObject = new Object();
        final InnerClass innerClass = new InnerClass();
        final JsonDeserializer<InnerClass> deserializer = new StdDeserializer<InnerClass>() {};

        // set value deserializer
        final InnerClassProperty property = new InnerClassProperty(new InnerClassProperty(dummyObject, innerClass), deserializer);

        // test serialization and readResolve
        mapper.registerValTypes(InnerClass.class);
        mapper.registerDeserializer(InnerClass.class, deserializer);
        Object output = mapper.writeValueAsBytes(property);
        Object result = mapper.readValue(output);

        // check result
        assertTrue(result instanceof InnerClassProperty);
        assertEquals(property, result);
    }

    @Test
    public void testWriteReplace() throws IOException {
        // create test object
        final ObjectMapper mapper = new ObjectMapper();
        final Object dummyObject = new Object();
        final InnerClass innerClass = new InnerClass();
        final JsonDeserializer<InnerClass> deserializer = new StdDeserializer<InnerClass>() {};

        // set value deserializer
        final InnerClassProperty property = new InnerClassProperty(new InnerClassProperty(dummyObject, innerClass), deserializer);

        // test writeReplace
        Object output = mapper.writeValueAsBytes(property);
        Object result = mapper.readValue(output);

        // check result
        assertTrue(result instanceof InnerClassProperty);
        assertEquals(property, result);
    }

    public static class InnerClass {
        private static final class Value {
            private String value;

            public Value() {}

            public String getValue() { return value; }

            public void setValue(String value) { this.value = value; }

            @Override
            public String toString() { return value; }
        }

        private Value value;

        public Value getValue() { return value; }

        public void setValue(Value value) { this.value = value; }

        @JsonCreator
        public InnerClass(@JsonProperty("value") Value value) {
            this.value = value;
        }

        @JsonProperty
        public Value getValueDeserializer() { return value; }
    }

    public static class StdDeserializer<T> extends JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}