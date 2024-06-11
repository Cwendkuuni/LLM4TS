package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.util.TestUtils.createParser;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testHandleMissingTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String extraDesc = "missing type id";
        JavaType result = new TypeDeserializerBaseBase(baseType, null, null, false, null)._handleMissingTypeId(ctxt, extraDesc);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String typeId = "UnknownTypeId";
        String extraDesc = "type ids are not statically known";
        JavaType result = new TypeDeserializerBaseBase(baseType, null, null, false, null)._handleUnknownTypeId(ctxt, typeId);
        assertNull(result);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String typeId = "MyTypeId";
        JsonDeserializer<Object> deserializer = new StubDeserializer();
        JsonDeserializer<Object> expectedDeserializer = deserializer;

        ctxt.addDeserializerModifier(new BaseJsonDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationContext context, BeanProperty property, JsonDeserializer<?> deserializer) {
                if (TypeDeserializerBase.class.isInstance(context.getCurrentValueDeserializer())) {
                    TypeDeserializerBase instance = (TypeDeserializerBase) context.getCurrentValueDeserializer();
                    instance._deserializers.put(typeId, deserializer);
                }
                return deserializer;
            }
        });

        assertEquals(expectedDeserializer, new TypeDeserializerBaseBase(baseType, idResolver, null, false, null)._findDeserializer(ctxt, typeId));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        assertEquals(NullifyingDeserializer.instance, new TypeDeserializerBaseBase(baseType, null, null, false, null)._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String typeId = "MyTypeId";

        JsonParser jp = createParser("{\"typeId\":\"" + typeId + "\"}");

        assertEquals(null, new TypeDeserializerBaseBase(baseType, null, null, false, null)._deserializeWithNativeTypeId(jp, ctxt));
    }

    @Test
    public void testDeserializeWithNativeTypeIdAndDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String typeId = "MyTypeId";
        JsonDeserializer<Object> deserializer = new StubDeserializer();

        ctxt.addDeserializerModifier(new BaseJsonDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationContext context, BeanProperty property, JsonDeserializer<?> deserializer) {
                if (TypeDeserializerBase.class.isInstance(context.getCurrentValueDeserializer())) {
                    TypeDeserializerBase instance = (TypeDeserializerBase) context.getCurrentValueDeserializer();
                    instance._deserializers.put(typeId, deserializer);
                }
                return deserializer;
            }
        });

        JsonParser jp = createParser("{\"typeId\":\"" + typeId + "\"}");

        assertEquals(deserializer, new TypeDeserializerBaseBase(baseType, idResolver, null, false, null)._deserializeWithNativeTypeId(jp, ctxt, typeId));
    }

    static class StubDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new Object();
        }
    }
}