package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.core.JsonParser.CurrentName;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@JsonAutoDetect(getterVisibility = ANY, setterVisibility = ANY)
public static class TestObject {
    @JsonProperty("id")
    private Object id;

    @JsonTypeName("TestObject")
    public String getTypeName() { return "TestObject"; }
}

public class ObjectIdValuePropertyTest {

    private static class TestObjectIdReader extends ObjectIdReader {
        @Override
        public ObjectIdReader.IdType idType() { return ObjectIdReader.IdType.INT; }

        @Override
        public String propertyName() { return "id"; }

        @Override
        public JsonDeserializer<?> idDeserializer() { return new IntDeserializer(); }

        @Override
        public ObjectIdGenerator idGenerator() { return new AlwaysNullObjectIdGenerator(); }

        @Override
        public ObjectIdResolver idResolver() { return new AlwaysNullObjectIdResolver(); }

        @Override
        public BeanProperty getIdProperty() { return null; }

        @Override
        public ObjectIdValueProperty withName(PropertyName newName) {
            return new ObjectIdValueProperty(this, newName);
        }
    }

    @Test
    public void testConstructors() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestObjectIdReader(null), null);
        assertNotNull(property);
        assertEquals("id", property.getName());
        assertNull(property.getIdProperty());
        assertEquals(IntDeserializer.class, property.getValueDeserializer().getClass());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestObjectIdReader(null), null);
        JsonParser parser = createParser("[1]");
        Object instance = new Object();

        property.deserializeAndSet(parser, new DeserializationContext(), instance);

        Object id = parser.getValueAsToken().getValueAsInt();
        TestObject testObject = (TestObject) instance;
        assertEquals(id, testObject.getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestObjectIdReader(null), null);

        JsonParser parser = createParser("[1]");
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(parser, new DeserializationContext(), instance);
        assertEquals(instance, result);
        Object id = parser.getValueAsToken().getValueAsInt();
        TestObject testObject = (TestObject) instance;
        assertEquals(id, testObject.getId());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestObjectIdReader(null), null);
        Object instance = new TestObject();

        Object id = 1;
        Object result = property.setAndReturn(instance, id);
        assertEquals(instance, result);
        assertEquals(id, ((TestObject) instance).getId());
    }

    @Test
    public void testSetNull() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestObjectIdReader(null), null);
        Object instance = new TestObject();

        property.setAndReturn(instance, null);
        assertNull(((TestObject) instance).getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new TestObjectIdReader(null), new TestObjectIdReader(null).getIdProperty());
        Object instance = new Object();

        property.setAndReturn(instance, 1);
    }

    private JsonParser createParser(String content) throws IOException {
        ObjectCodec objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createParser(new ByteArrayInputStream(content.getBytes()));
    }
}