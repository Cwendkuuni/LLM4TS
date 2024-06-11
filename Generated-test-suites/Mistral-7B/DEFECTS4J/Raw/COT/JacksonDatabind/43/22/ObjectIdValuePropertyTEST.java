package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ObjectIdReaderBuilder;

/**
 * Test cases for the ObjectIdValueProperty class.
 */
public class ObjectIdValuePropertyTest {

    private static final ObjectIdReaderBuilder OBJECT_ID_READER_BUILDER = new ObjectIdReaderBuilder(
            JsonTestUtil.stdDeserializationConfig());
    private static final JsonDeserializer<ObjectIdValueProperty> DESERIALIZER = JSON_FACTORY.deserializerForClass(
            ObjectIdValueProperty.class);

    @SuppressWarnings("unchecked")
    @JsonDeserialize(using = ObjectIdValueProperty.class)
    static class TestPojo {
        @JsonId
        private Object id;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        // for testing setAndReturn()
        private transient BeanPropertyDefinition idProp;

        public BeanPropertyDefinition getIdPropertyDefinition() {
            return (BeanPropertyDefinition) AnnotationIntrospector.findProperty(this, "id");
        }

        public ObjectIdValueProperty getIdProperty() {
            return AnnotationIntrospector.findPropertyValue(this, "id");
        }
    }

    @org.junit.Test
    public void testNewInstance() throws JsonProcessingException, IOException {
        ObjectIdReader objectIdReader = OBJECT_ID_READER_BUILDER.forDeserializer(TestPojo.class)
                .build();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OR_DEFAULT);
        assertEquals("id", property.getName());
        assertEquals(TestPojo.class, property.getType());
        assertNull(property.getMember());
        assertNull(property.getAnnotation(JsonId.class));
        assertNotNull(property._objectIdReader);
    }

    @org.junit.Test
    public void testSerialization() throws JsonProcessingException, IOException {
        ObjectIdReader objectIdReader = OBJECT_ID_READER_BUILDER.forDeserializer(TestPojo.class)
                .build();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OR_DEFAULT);

        ObjectWriter writer = JsonTestUtil.stdObjectMapper().writerWithView(JsonTestUtil.ALL_ANNOTATIONS);
        ObjectWriter writerWithValueDeserializer = JsonTestUtil.stdObjectMapper()
                .writerWithView(JsonTestUtil.ALL_ANNOTATIONS)
                .withValueHandler(new ValueHandlerBase() {
                    @Override
                    public void writeValue(JsonGenerator g, Object value) throws IOException {
                        g.writeRawValue(property.getValueDeserializer().serializeValueAsString(value, null, null));
                    }
                });

        TestPojo pojo = new TestPojo();
        pojo.setId("id123");
        pojo.setName("name123");

        String json = writer.writeValueAsString(pojo);
        TestPojo deserializedPojo = writerWithValueDeserializer.readValue(JsonParser.parseString(json), TestPojo.class);
        assertEquals(pojo.getId(), deserializedPojo.getId());
        assertEquals(pojo.getName(), deserializedPojo.getName());
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = OBJECT_ID_READER_BUILDER.forDeserializer(TestPojo.class)
                .build();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OR_DEFAULT);

        TestPojo pojo = new TestPojo();

        String json = "{\"id\":\"id123\", \"name\":\"name123\"}";

        JsonParser p = JsonTestUtil.parse(json);
        Object deserializedObject = property.deserializerAndSet(p, JsonTestUtil.stdDeserializationContext(), pojo);
        assertEquals(json, JsonTestUtil.asPrettyString(p));
        assertEquals("id123", pojo.getId());
        assertEquals("name123", pojo.getName());
    }

    @org.junit.Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = OBJECT_ID_READER_BUILDER.forDeserializer(TestPojo.class)
                .build();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OR_DEFAULT);

        TestPojo pojo = new TestPojo();

        String json = "{\"id\":\"id123\", \"name\":\"name123\"}";

        Object deserializedObject = property.deserializeSetAndReturn(JsonTestUtil.stdParser(json),
                JsonTestUtil.stdDeserializationContext(), pojo);
        assertEquals("id123", pojo.getId());
        assertEquals("name123", pojo.getName());
        assertEquals(pojo, deserializedObject);
    }

    @org.junit.Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = OBJECT_ID_READER_BUILDER.forDeserializer(TestPojo.class)
                .build();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OR_DEFAULT);

        TestPojo pojo = new TestPojo();

        String id = "id123";
        ObjectIdValueProperty idProperty = pojo.getIdProperty();
        Converter<String, ObjectId> stringToObjectIdConverter = idProperty.getConverter();

        property.set(pojo, id);
        assertEquals(id, pojo.getId());

        Object deserializedObject = property.deserializeSetAndReturn(JsonTestUtil.stdParser(json),
                JsonTestUtil.stdDeserializationContext(), pojo);
        assertEquals("id123", pojo.getId());
        assertEquals("name123", pojo.getName());
        assertEquals(pojo, deserializedObject);

        // test with null id
        String nullId = null;
        property.set(pojo, nullId);
        assertEquals(null, pojo.getId());
    }

    @org.junit.Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = OBJECT_ID_READER_BUILDER.forDeserializer(TestPojo.class)
                .build();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OR_DEFAULT);

        TestPojo pojo = new TestPojo();

        String id = "id123";
        ObjectIdValueProperty idProperty = pojo.getIdProperty();
        Converter<String, ObjectId> stringToObjectIdConverter = idProperty.getConverter();

        Object deserializedObject = property.setAndReturn(pojo, id);
        assertEquals(id, pojo.getId());
        assertEquals("name123", pojo.getName());
        assertEquals(pojo, deserializedObject);

        // test with null id
        String nullId = null;
        Object deserializedObject2 = property.setAndReturn(pojo, nullId);
        assertEquals(null, pojo.getId());
        assertNotSame(pojo, deserializedObject2);
        assertNull(deserializedObject2);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetOnPropertyWithoutIdProperty() throws IOException {
        ObjectIdReader objectIdReader = OBJECT_ID_READER_BUILDER.forDeserializer(TestPojoWithoutIdProperty.class)
                .build();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OR_DEFAULT);

        TestPojoWithoutIdProperty pojo = new TestPojoWithoutIdProperty();

        String id = "id123";
        property.set(pojo, id);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnOnPropertyWithoutIdProperty() throws IOException {
        ObjectIdReader objectIdReader = OBJECT_ID_READER_BUILDER.forDeserializer(TestPojoWithoutIdProperty.class)
                .build();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OR_DEFAULT);

        TestPojoWithoutIdProperty pojo = new TestPojoWithoutIdProperty();

        String id = "id123";
        Object deserializedObject = property.setAndReturn(pojo, id);
    }

    static class TestPojoWithoutIdProperty {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}