package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyCreation() {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.STRING);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        assertEquals("idPropertyName", property.getName());
        assertEquals(ObjectIdType.STRING, property.getIdType());
        assertNull(property.getIdDeserializer());
        assertNull(property.getNullProvider());
        assertEquals("idPropertyName", property.getMember().getName());
        assertNull(property.getAnnotation(ObjectIdGenerator.class));
        assertEquals(ObjectIdType.STRING, property.getIdType());
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.OBJECT);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ, new StdScalarDeserializer(String.class));
        assertEquals("idPropertyName", property.getName());
        assertEquals(ObjectIdType.OBJECT, property.getIdType());
        assertNotNull(property.getIdDeserializer());
        assertNull(property.getNullProvider());
        assertEquals("idPropertyName", property.getMember().getName());
        assertEquals(String.class, property.getIdDeserializer().getClass());
    }

    @Test
    public void testObjectIdValuePropertyWithNullProvider() {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.LONG);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ, new StandardNullValueProvider());
        assertEquals("idPropertyName", property.getName());
        assertEquals(ObjectIdType.LONG, property.getIdType());
        assertNull(property.getIdDeserializer());
        assertNotNull(property.getNullProvider());
        assertEquals(StandardNullValueProvider.class, property.getNullProvider().getClass());
    }

    @Test
    public void testObjectIdValuePropertyWithIdProperty() {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.STRING);
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        ObjectIdValueProperty idPropertyWithIdProperty = new ObjectIdValueProperty(idProperty, new StdScalarDeserializer(String.class), null);
        assertEquals("idPropertyName", idPropertyWithIdProperty.getName());
        assertEquals(ObjectIdType.STRING, idPropertyWithIdProperty.getIdType());
        assertNotNull(idPropertyWithIdProperty.getIdDeserializer());
        assertNull(idPropertyWithIdProperty.getNullProvider());
        assertEquals("idPropertyName", idPropertyWithIdProperty.getMember().getName());
        assertEquals("idPropertyName", idProperty.getName());
        assertEquals(ObjectIdType.STRING, idProperty.getIdType());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.STRING);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("{\"idPropertyName\": \"idValue\"}");

        Object result = property.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), new Object());
        assertEquals("idValue", result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.STRING);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("{\"idPropertyName\": \"idValue\"}");

        Object idValue = property.deserializeSetAndReturn(jsonParser, mapper.getDeserializationContext(), new Object());
        assertEquals("idValue", idValue);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.STRING);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("idPropertyName", "idValue");

        Object idValue = property.setAndReturn(new Object(), "idValue");
        assertEquals("idValue", idValue);
    }

    @Test
    public void testJsonDeserialization() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.STRING);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JacksonAnnotationIntrospector());

        MyPojo pojo = mapper.readValue(new ObjectMapper().createParser("{\"idPropertyName\": \"idValue\"}"), MyPojo.class);
        assertEquals("idValue", pojo.getId());
    }

    @Test
    public void testContextualSerialization() throws IOException {
        ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.STRING);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {{
            addSerializer(MyPojo.class, new ContextualSerializer() {
                @Override
                public JsonSerializer<?> createSerializer(SerializerProvider prov, BeanDescription beanDesc) {
                    return property;
                }
            });
        }});

        ObjectWriter writer = mapper.writer();
        ObjectNode node = writer.writeValueAsObject(new MyPojo("idValue"));
        String jsonString = node.toString();
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        Object idValue = parser.readValueAs(MyPojo.class).getId();
        assertEquals("idValue", idValue);
    }

    private static class MyPojo {
        private String id;

        public MyPojo(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    private static class SimpleObjectIdReader implements ObjectIdReader {
        private final String propertyName;
        private final ObjectIdType idType;

        public SimpleObjectIdReader(String propertyName, ObjectIdType idType) {
            this.propertyName = propertyName;
            this.idType = idType;
        }

        @Override
        public PropertyName propertyName() {
            return PropertyName.construct(propertyName, null);
        }

        @Override
        public ObjectIdType idType() {
            return idType;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return null;
        }

        @Override
        public ObjectIdResolver idResolver() {
            return null;
        }
    }

    static class SimpleModule extends StandardSerializer {
        SimpleModule() {
            this(null);
        }

        SimpleModule(Class<?> _valueClass) {
            this(_valueClass, null);
        }

        SimpleModule(Class<?> _valueClass, AnnotationMappingParser.Key _key) {
            this(_valueClass, _key, null);
        }

        SimpleModule(Class<?> _valueClass, AnnotationMappingParser.Key _key, JsonSerializer<Object> _valueSerializer) {
            super(_valueClass);
            this._valueClass = _valueClass;
            this._key = _key;
            this._valueSerializer = _valueSerializer;
        }

        @Override
        public void configure(DeserializationConfig config, AnnotationIntrospector introspector) throws JsonMappingException {
            super.configure(config, introspector);
            config.addDeserializer(ObjectIdValueProperty.class, new ObjectIdValuePropertyDeserializer());
        }

        static class ObjectIdValuePropertyDeserializer
                extends JsonDeserializer<ObjectIdValueProperty> {

            @Override
            public ObjectIdValueProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                ObjectIdReader objectIdReader = new SimpleObjectIdReader("idPropertyName", ObjectIdType.STRING);
                return new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
            }
        }
    }
}