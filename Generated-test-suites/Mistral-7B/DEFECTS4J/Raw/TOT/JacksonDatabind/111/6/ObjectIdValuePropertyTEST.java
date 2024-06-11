package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static com.fasterxml.jackson.core.JsonParser.CurrentMode.PARSING_AT_CURRENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader FORSTRING = new ObjectIdReaderForString();
    private static final ObjectIdReader FORLONG = new ObjectIdReaderForLong();
    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Object testInstance;
    private ObjectIdValueProperty property;
    private AnnotationIntrospector introspector;
    private JsonParser parser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        introspector = new AnnotationIntrospector();
        testInstance = new TestClass();
        property = new ObjectIdValueProperty(FORSTRING, new PropertyMetadata() {
            @Override public JsonDeserializer<?> getEmptyPropertyValueDeserializer() {
                return null;
            }
        }, null);
        property.setName(PropertyName.construct("id"));
        property.setIdType(String.class);
        property.setValueDeserializer(FORSTRING);
        property.setNullProvider(NullValueProvider.noOpValue());
        parser = JSON_FACTORY.createParser(MAPPER.getSerializerProvider().getBaseJsonParser().getEmptyByteArray());
    }

    @Test
    public void testDefault() throws Exception {
        Object result = property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), testInstance);
        assertNull(result);
        assertEquals(null, testInstance.getClass().getDeclaredField("id").get(testInstance));
    }

    @Test
    public void testNullValue() throws Exception {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        Object result = property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), testInstance);
        assertNull(result);
        assertEquals(null, testInstance.getClass().getDeclaredField("id").get(testInstance));
    }

    @Test
    public void testStringValue() throws Exception {
        String expectedId = "testId";
        parser.setValue(expectedId);
        Object result = property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), testInstance);
        assertEquals(expectedId, testInstance.getClass().getDeclaredField("id").get(testInstance));
    }

    @Test(expected = IOException.class)
    public void testInvalidStringValue() throws Exception {
        parser.setValue("invalid");
        property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), testInstance);
    }

    @Test
    public void testLongValue() throws Exception {
        long expectedId = 123L;
        ObjectIdValueProperty longProperty = new ObjectIdValueProperty(FORLONG, new PropertyMetadata() {
            @Override public JsonDeserializer<?> getEmptyPropertyValueDeserializer() {
                return null;
            }
        }, null);
        longProperty.setName(PropertyName.construct("id"));
        longProperty.setIdType(Long.class);
        longProperty.setValueDeserializer(FORLONG);
        longProperty.setNullProvider(NullValueProvider.noOpValue());
        longProperty.setIdReader(_objectIdReader -> new ObjectId());
        property.setNext(longProperty);
        parser.setValue(String.valueOf(expectedId));
        Object result = property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), testInstance);
        assertEquals(expectedId, testInstance.getClass().getDeclaredField("id").get(testInstance));
    }

    @Test(expected = IOException.class)
    public void testInvalidLongValue() throws Exception {
        parser.setValue("invalid");
        property.deserializeAndSet(parser, Mockito.mock(DeserializationContext.class), testInstance);
    }

    @Test
    public void testSetValue() throws Exception {
        String id = "testId";
        testInstance.getClass().getDeclaredField("id").set(testInstance, id);
        assertEquals(id, property.getValueDeserializer().deserialize(parser, Mockito.mock(DeserializationContext.class)));
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    static class TestClass {
        @JsonDeserialize(using = ObjectIdValueProperty.class)
        private String id;

        @JsonProperty
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static class ObjectId {
        private final String id;

        ObjectId() {
            this("testObjectId");
        }

        ObjectId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void bindItem(Object instance) {}
    }

    static class PropertyMetadata {
        JsonDeserializer<?> getEmptyPropertyValueDeserializer() {}
    }

    static class ObjectIdReaderForString extends ObjectIdReader {
        @Override public ReadableObjectId readId(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new ObjectId(p.getValueAsString());
        }
    }

    static class ObjectIdReaderForLong extends ObjectIdReader {
        @Override public ReadableObjectId readId(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new ObjectId(p.getValueAsLong());
        }
    }

    static class TestSerializer extends StandardSerializer<ObjectIdValueProperty> {
        TestSerializer() {
            this(null);
        }

        TestSerializer(Class<ObjectIdValueProperty> t) {
            this(null, t);
        }

        TestSerializer(Class<ObjectIdValueProperty> t, Class<?> valueClass) {
            super(valueClass, null);
        }

        @Override public void serializeValues(JsonGenerator g, SerializerProvider p, Object[] v) throws IOException {
            for (ObjectIdValueProperty property : v) {
                g.writeString(property.getName().asString());
            }
        }
    }

    static class TestFilter extends SimpleBeanPropertyFilter {
        @Override public boolean includes(PropertyName name) {
            return name.asString().startsWith("id");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface TestAnnotation {
    }
}