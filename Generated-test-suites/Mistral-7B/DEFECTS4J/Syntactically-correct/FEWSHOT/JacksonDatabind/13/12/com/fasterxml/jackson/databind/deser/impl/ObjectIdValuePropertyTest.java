package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectIdNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testBasic() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public IdType getIdType() {
                return IdType.STRING;
            }

            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public ObjectIdResolver getIdResolver() {
                return null;
            }

            @Override
            public ObjectIdGenerator getIdGenerator() {
                return null;
            }

            @Override
            public boolean isEmpty(ObjectIdNode id) {
                return id == null;
            }

            @JsonDeserialize(using = ObjectIdValueProperty.class)
            public static class TestPojo {
                @JsonDeserialize(using = ToStringSerializer.class)
                private ObjectId id;

                private String name;

                public ObjectId getId() {
                    return id;
                }

                public void setId(ObjectId id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class TestPojoDeserializer extends BeanDeserializer {
                public TestPojoDeserializer() {
                    super(TestPojo.class);
                }

                @Override
                public ObjectIdReader getObjectIdReader() {
                    return this;
                }
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new AnnotationIntrospector());
        mapper.setNameTransformer(NameTransformer.getInstance());

        JsonDeserializer<TestPojo> deserializer = new TestPojoDeserializer();
        ObjectIdValueProperty property = mapper.findProperty("id", deserializer);

        TestPojo pojo = new TestPojo();
        pojo.setName("Test");

        ObjectId id = mapper.readValue(ObjectIdNode.class.getResourceAsStream("/testId.json"), ObjectIdNode.class).getObjectIdValue();
        property.setValue(pojo, id);

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo);
        TestPojo deserializedPojo = mapper.readValue(json, TestPojo.class);
        assertEquals(id, deserializedPojo.getId());
    }

    @Test
    public void testWithIdProperty() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public IdType getIdType() {
                return IdType.LONG;
            }

            @Override
            public PropertyName propertyName() {
                return new PropertyName("id");
            }

            @Override
            public ObjectIdResolver getIdResolver() {
                return null;
            }

            @Override
            public ObjectIdGenerator getIdGenerator() {
                return null;
            }

            @JsonDeserialize(using = ObjectIdValueProperty.class)
            public static class TestPojo {
                @JsonDeserialize(using = ToStringSerializer.class)
                private Long id;

                @JsonDeserialize(using = ObjectIdValueProperty.class)
                private ObjectIdValueProperty idProperty;

                private String name;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public ObjectIdValueProperty getIdProperty() {
                    return idProperty;
                }

                public void setIdProperty(ObjectIdValueProperty idProperty) {
                    this.idProperty = idProperty;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class TestPojoDeserializer extends BeanDeserializer {
                public TestPojoDeserializer() {
                    super(TestPojo.class);
                }

                @Override
                public ObjectIdReader getObjectIdReader() {
                    return this;
                }
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new AnnotationIntrospector());
        mapper.setNameTransformer(NameTransformer.getInstance());

        JsonDeserializer<TestPojo> deserializer = new TestPojoDeserializer();
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        idProperty.setName(new PropertyName("idProperty"));
        TestPojo pojo = new TestPojo();
        pojo.setName("Test");
        pojo.setIdProperty(idProperty);

        ObjectId id = mapper.readValue(ObjectIdNode.class.getResourceAsStream("/testId.json"), ObjectIdNode.class).getObjectIdValue();
        idProperty.setValue(pojo, id);

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo);
        TestPojo deserializedPojo = mapper.readValue(json, TestPojo.class);
        assertEquals(id, deserializedPojo.getIdProperty().getValue());
    }
}