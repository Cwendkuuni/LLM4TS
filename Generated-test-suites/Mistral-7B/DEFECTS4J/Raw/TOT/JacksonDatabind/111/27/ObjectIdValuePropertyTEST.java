package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class ObjectIdValuePropertyTest {

    ObjectMapper mapper;

    @Before
    public void setup() {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader(PropertyName.construct("id"));
        mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addDeserializer(String.class, new StringDeserializer()));
        mapper.addBeanPropertyEditor(ObjectIdValueProperty.class, new ObjectIdValueProperty.Builder()
                .withObjectIdReader(objectIdReader)
                .build());
    }

    @Test
    public void testNullValue() throws IOException {
        MyPojo pojo = mapper.readValue( "{ \"id\": null }", MyPojo.class);
        Assert.assertNull(pojo.getId());
    }

    @Test
    public void testEmptyStringValue() throws IOException, JsonProcessingException {
        MyPojo pojo = mapper.readValue( "{ \"id\": \"\" }", MyPojo.class);
        Assert.assertNull(pojo.getId());
    }

    @Test
    public void testStringValue() throws IOException {
        MyPojo pojo = mapper.readValue( "{ \"id\": \"123\" }", MyPojo.class);
        Assert.assertEquals("123", pojo.getId());
    }

    @Test(expected = IOException.class)
    public void testInvalidStringValue() throws IOException {
        mapper.readValue( "{ \"id\": 'not a number' }", MyPojo.class);
    }

    @Test
    public void testIdPropertySet() throws IOException {
        MyPojo pojo = new MyPojo();
        pojo.setId("123");
        ObjectIdValueProperty idProperty = ObjectIdValueProperty.finder(MyPojo.class, "id");
        Object id = idProperty.getValue(pojo);
        Assert.assertEquals("123", id);
    }

    @Test
    public void testIdPropertySetFromJson() throws IOException {
        MyPojo pojo = mapper.readValue( "{ \"id\": \"123\" }", MyPojo.class);
        ObjectIdValueProperty idProperty = ObjectIdValueProperty.finder(MyPojo.class, "id");
        Object id = idProperty.getValue(pojo);
        Assert.assertEquals("123", id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetIdPropertyWithoutIdProperty() throws IOException {
        MyPojo pojo = new MyPojo();
        ObjectIdValueProperty idProperty = ObjectIdValueProperty.finder(MyPojo.class, "nonexistentProperty");
        idProperty.setAndReturn(pojo, "123");
    }

    static class MyPojo {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static class SimpleObjectIdReader extends ObjectIdReaderBase {
        private PropertyName propertyName;

        public SimpleObjectIdReader(PropertyName propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public PropertyName propertyName() {
            return propertyName;
        }

        public ObjectIdGenerator idGenerator() {
            return null;
        }

        public ObjectIdResolver idResolver() {
            return null;
        }
    }

    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }

    static class ObjectIdValueProperty$Builder {
        private ObjectIdReader objectIdReader;

        public ObjectIdValueProperty$Builder withObjectIdReader(ObjectIdReader objectIdReader) {
            this.objectIdReader = objectIdReader;
            return this;
        }

        public ObjectIdValueProperty build() {
            return new ObjectIdValueProperty(objectIdReader, null);
        }
    }
}