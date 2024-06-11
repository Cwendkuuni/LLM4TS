package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdGenerator$AlwaysStubGenerator;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValueProperty() {
        ObjectIdGenerator generator = new ObjectIdGenerator$AlwaysStubGenerator();
        ObjectIdReader objectIdReader = new ObjectIdReader("idProperty", ObjectIdGenerator.class, generator);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        assertEquals("idProperty", property.getName());
        assertEquals(ObjectIdGenerator.class, property.getIdType());
        assertNull(property.getAnnotation(JsonProperty.class));
        assertNull(property.getMember());

        ObjectIdValueProperty copy = property.withName(new PropertyName("newName"));
        assertEquals("newName", copy.getName());
        assertEquals("idProperty", property.getName());

        JsonDeserializer<Object> deserializer = new StdDeserializer<>();
        ObjectIdValueProperty deserProperty = new ObjectIdValueProperty(objectIdReader, deserializer, null);
        assertEquals(deserializer, deserProperty.getvalueDeserializer());

        ObjectIdValueProperty nullProviderProperty = deserProperty.withNullProvider(new NullProperty());
        assertNull(nullProviderProperty.getNullProvider());
        assertEquals(deserializer, nullProviderProperty.getvalueDeserializer());

        ObjectIdValueProperty nameFilterProperty = new ObjectIdValueProperty(deserProperty, new SimpleBeanPropertyFilter.FilterProvider() {
            @Override
            public SimpleBeanPropertyFilter getFilterInstance(DeserializationConfig config, BeanDescription beanDesc) {
                return new SimpleBeanPropertyFilter(config, new NameTransformer() {
                    @Override
                    public String transform(String name, JsonParser p) {
                        return "filteredName";
                    }
                });
            }
        });
        assertEquals("filteredName", nameFilterProperty.getName());
        assertEquals(deserializer, nameFilterProperty.getvalueDeserializer());
    }

    @Test
    public void testDeserialization() throws Exception {
        ObjectIdGenerator generator = new ObjectIdGenerator$AlwaysStubGenerator();
        ObjectIdReader objectIdReader = new ObjectIdReader("idProperty", ObjectIdGenerator.class, generator);
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, new StdDeserializer<>());

        Object ac = new Object();
        JsonParser p = new TestJsonParser();
        p.setValue("123");

        Object result = idProperty.deserializeAndSet(p, new TestContext(), ac);
        assertEquals("123", result);
        assertEquals("123", ((TestObject) ac).id);

        p.setValue(null);
        result = idProperty.deserializeAndSet(p, new TestContext(), ac);
        assertNull(result);
        assertNull(((TestObject) ac).id);

        p.setValue("foo");
        try {
            idProperty.deserializeAndSet(p, new TestContext(), ac);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testSet() throws Exception {
        ObjectIdGenerator generator = new ObjectIdGenerator$AlwaysStubGenerator();
        ObjectIdReader objectIdReader = new ObjectIdReader("idProperty", ObjectIdGenerator.class, generator);
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, new StdDeserializer<>());

        Object ac = new TestObject();

        Object id = idProperty.setAndReturn(ac, 123);
        assertEquals(123, id);
        assertEquals(123, ((TestObject) ac).id);

        idProperty.setAndReturn(ac, null);
        assertNull(((TestObject) ac).id);

        try {
            idProperty.setAndReturn(ac, "foo");
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }
}

class TestContext extends DeserializationContext {
    @Override
    public ObjectIdGenerator defaultObjectIdGenerator() {
        return new ObjectIdGenerator$AlwaysStubGenerator();
    }

    @Override
    public Object findValueId(Object id, ObjectIdGenerator generator) throws IOException {
        return id;
    }

    @Override
    public ObjectIdResolver findObjectIdResolver() throws IOException {
        return null;
    }
}

class TestObject {
    private Object id;

    public ObjectIdValueProperty idProperty;

    // getter and setter for id
}

class StdDeserializer<T> extends JsonDeserializer<T> {
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }
}

class NullProperty implements NullValueProvider {
    @Override
    public NullValueProvider nullProviderForProperty(BeanProperty property) {
        return this;
    }

    @Override
    public Object getNullValue(DeserializationContext ctxt) throws IOException {
        return null;
    }
}

class TestJsonParser extends JsonParser {
    private Object currentValue;

    @Override
    public boolean nextToken() throws IOException {
        currentValue = null;
        return super.nextToken();
    }

    @Override
    public Object getValueAsString() throws IOException {
        String value = (String) currentValue;
        currentValue = null;
        return value;
    }

    @Override
    public void valueString(String text) throws IOException {
        currentValue = text;
    }
}