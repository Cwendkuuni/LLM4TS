package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.test.MapTestBase;
import com.fasterxml.jackson.databind.test.TestBase;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest extends BaseMapTest {

    static class TestBean {
        private Object id;

        @JsonProperty("idProperty")
        public Object getIdProperty() {
            return id;
        }

        public void setIdProperty(Object id) {
            this.id = id;
        }
    }

    @Test
    public void testSerializeDeserialize() throws JsonProcessingException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> getDeserializer() {
                return new StdSerializer<Object>(Object.class) {};
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty.Std(
                        new PropertyName("idProperty"), Long.class, null, null);
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        ObjectNode node = mapper().createObjectNode();
        node.put("id", 1L);

        TestBean bean = mapper().readValue(node.toString(), TestBean.class);
        assertEquals(1L, bean.getId());
        assertEquals(1L, bean.getIdProperty());

        ObjectNode setterNode = mapper().createObjectNode();
        setterNode.put("id", null);
        TestBean setterBean = mapper().readValue(setterNode.toString(), TestBean.class);
        assertNull(setterBean.getId());
        assertNull(setterBean.getIdProperty());

        ObjectNode deserNode = mapper().createObjectNode();
        deserNode.put("id", mapper().valueToTree(1L));
        TestBean deserBean = mapper().readValue(deserNode.toString(), TestBean.class);
        assertEquals(1L, deserBean.getId());
        assertEquals(1L, deserBean.getIdProperty());
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> getDeserializer() {
                return new StdSerializer<Object>(Object.class) {};
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty.Std(
                        new PropertyName("idProperty"), Long.class, null, null);
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        JsonParser p = mapper().getFactory()
                .createParser(mapper().createObjectNode().toString());
        p.nextValue(); // skip null value

        DeserializationContext ctxt = mapper()._context.defaultContext();
        TestBean instance = new TestBean();

        property.deserializeAndSet(p, ctxt, instance);
        assertNull(instance.getId());
        assertNull(instance.getIdProperty());

        Object id = 1L;
        JsonParser idParser = mapper().getFactory().createParser(mapper().valueToTree(id).toString());
        idParser.nextValue(); // skip id value

        property.deserializeAndSet(idParser, ctxt, instance);
        assertEquals(id, instance.getId());
        assertEquals(id, instance.getIdProperty());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> getDeserializer() {
                return new StdSerializer<Object>(Object.class) {};
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty.Std(
                        new PropertyName("idProperty"), Long.class, null, null);
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        TestBean testBean = new TestBean();
        AnnotationIntrospector introspector = mapper()._config.introspector;
        assertNull(property.getAnnotation(Id.class));
        assertNull(introspector.findAnnotation(testBean.getClass(), Id.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> getDeserializer() {
                return new StdSerializer<Object>(Object.class) {};
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SettableBeanProperty.Std(
                        new PropertyName("idProperty"), Long.class, null, null);
            }
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        AnnotatedMember member = property.getMember();
        assertNull(member);
    }
}