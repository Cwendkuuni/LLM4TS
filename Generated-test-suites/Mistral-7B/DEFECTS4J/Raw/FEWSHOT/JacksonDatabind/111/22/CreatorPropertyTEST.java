package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {

    private ObjectBean objectBean;
    private CreatorProperty creatorProperty;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        ObjectConstructor constructor = new ObjectConstructor();
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        };
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializers().findValueDeserializer(Object.class, null);
        creatorProperty = new CreatorProperty(
                PropertyName.construct("name"),
                JavaType.construct(Object.class),
                null,
                typeDeserializer,
                Annotations.empty(),
                AnnotatedParameter.construct("paramName", JsonCreator.class, new Object() {}.getClass().getDeclaredMethod("create", null)),
                0,
                null,
                PropertyMetadata.STD_DEFAULTS
        );
        objectBean = mapper.findBeanDeserializer(Object.class).deserialize(node, Object.class, null);
    }

    @Test
    public void testGetAnnotation() {
        JsonCreator creatorAnnotation = creatorProperty.getAnnotation(JsonCreator.class);
        assertTrue(creatorAnnotation != null);
    }

    @Test
    public void testGetMember() {
        AnnotatedParameter annotatedParameter = creatorProperty.getMember();
        assertTrue(annotatedParameter != null);
    }

    @Test
    public void testGetCreatorIndex() {
        int creatorIndex = creatorProperty.getCreatorIndex();
        assertEquals(0, creatorIndex);
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        DeserializationContext context = new TestDeserializationContext();
        Object value = creatorProperty.findInjectableValue(context, objectBean);
        assertNull(value);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueWithInjectedValueId() throws Exception {
        DeserializationContext context = new TestDeserializationContext();
        Object id = new Object();
        context.addInjectableValues(id, this);
        Object value = creatorProperty.findInjectableValue(context, objectBean);
        assertSame(id, value);
    }

    @Test
    public void testInject() throws Exception {
        DeserializationContext context = new TestDeserializationContext();
        Object id = new Object();
        context.addInjectableValues(id, this);
        creatorProperty.inject(context, objectBean);
        Object value = creatorProperty.get(objectBean);
        assertSame(id, value);
    }

    @Test
    public void testIsIgnorable() {
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testMarkAsIgnorable() {
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test(expected = IOException.class)
    public void testSetFallbackSetterWithNull() throws Exception {
        creatorProperty.setFallbackSetter(null);
    }

    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = new SettableBeanProperty(
                PropertyName.construct("fallbackName"),
                JavaType.construct(Object.class),
                null,
                null,
                null,
                null,
                null,
                null,
                PropertyMetadata.STD_DEFAULTS
        );
        creatorProperty.setFallbackSetter(fallbackSetter);
        assertSame(fallbackSetter, creatorProperty._fallbackSetter);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String json = "{\"name\":\"value\"}";
        JsonParser p = mapper.getFactory().createParser(json);
        creatorProperty.deserializeAndSet(p, null, objectBean);
        Object value = creatorProperty.get(objectBean);
        assertEquals("value", value.toString());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        String json = "{\"name\":\"value\"}";
        JsonParser p = mapper.getFactory().createParser(json);
        Object returnedValue = creatorProperty.deserializeSetAndReturn(p, null, objectBean);
        Object value = creatorProperty.get(objectBean);
        assertEquals("value", value.toString());
        assertEquals(value, returnedValue);
    }

    @Test
    public void testSet() throws Exception {
        creatorProperty.set(objectBean, new Object());
        Object value = creatorProperty.get(objectBean);
        assertEquals(value, creatorProperty.get(objectBean));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object returnedValue = creatorProperty.setAndReturn(objectBean, new Object());
        Object value = creatorProperty.get(objectBean);
        assertEquals(value, returnedValue);
    }

    private static class ObjectBean {
        @JsonProperty("name")
        private Object name;

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }
    }

    private static class ObjectConstructor {
        public static Object create() {
            return new Object();
        }
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public Object findInjectableValue(ObjectId id, BeanProperty property, Object bean)
                throws JsonMappingException {
            return null;
        }
    }
}