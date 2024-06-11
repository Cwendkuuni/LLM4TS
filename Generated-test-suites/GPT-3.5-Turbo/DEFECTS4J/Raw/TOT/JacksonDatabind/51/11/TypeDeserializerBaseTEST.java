
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.*;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeWithTypeProperty() throws Exception {
        TypeDeserializerBase deserializer = new TypeDeserializerImpl(null, null, "type", true, null);
        String json = "{\"type\": \"A\", \"name\": \"Alice\", \"age\": 25}";
        TypeBean bean = (TypeBean) deserializer.deserializeWithType(
                mapper.getFactory().createParser(json), mapper.getDeserializationContext(), TypeBean.class);
        assertEquals("A", bean.type);
        assertEquals("Alice", bean.name);
        assertEquals(25, bean.age);
    }

    @Test
    public void testDeserializeWithDefaultImpl() throws Exception {
        TypeDeserializerBase deserializer = new TypeDeserializerImpl(null, null, null, true, TypeBean.class);
        String json = "{\"name\": \"Bob\", \"age\": 30}";
        TypeBean bean = (TypeBean) deserializer.deserializeWithType(
                mapper.getFactory().createParser(json), mapper.getDeserializationContext(), TypeBean.class);
        assertEquals("TypeBean", bean.type);
        assertEquals("Bob", bean.name);
        assertEquals(30, bean.age);
    }

    @Test
    public void testDeserializeWithUnknownTypeId() throws Exception {
        TypeDeserializerBase deserializer = new TypeDeserializerImpl(null, null, "type", false, null);
        String json = "{\"type\": \"C\", \"name\": \"Carol\", \"age\": 35}";
        TypeBean bean = (TypeBean) deserializer.deserializeWithType(
                mapper.getFactory().createParser(json), mapper.getDeserializationContext(), TypeBean.class);
        assertNull(bean);
    }

    @Test
    public void testDeserializeWithMissingTypeId() throws Exception {
        TypeDeserializerBase deserializer = new TypeDeserializerImpl(null, null, "type", false, null);
        String json = "{\"name\": \"David\", \"age\": 40}";
        TypeBean bean = (TypeBean) deserializer.deserializeWithType(
                mapper.getFactory().createParser(json), mapper.getDeserializationContext(), TypeBean.class);
        assertNull(bean);
    }

    private static class TypeDeserializerImpl extends TypeDeserializerBase {

        protected TypeDeserializerImpl(JavaType baseType, TypeIdResolver idResolver,
                String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt,
                TypeBean bean) throws JsonProcessingException {
            try {
                jp.nextToken();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String fieldname = jp.getCurrentName();
                    jp.nextToken();
                    if ("type".equals(fieldname)) {
                        bean.type = jp.getText();
                    } else if ("name".equals(fieldname)) {
                        bean.name = jp.getText();
                    } else if ("age".equals(fieldname)) {
                        bean.age = jp.getIntValue();
                    }
                }
                return bean;
            } catch (IOException e) {
                throw new JsonParseException(jp, "Failed to deserialize", e);
            }
        }

    }

    private static class TypeBean {

        public String type;
        public String name;
        public int age;

    }

}
