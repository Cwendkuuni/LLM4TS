package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        DeserializationContext ctxt = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                if (typeId.equals("TypeA")) {
                    return new TypeReference<String>() {}.getType();
                }
                return null;
            }
        }, "typePropertyName", true, new TypeReference<String>() {}.getType());

        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, "TypeA");
        assertNotNull(deserializer);
        assertEquals(String.class, deserializer.getParameterType());
    }

    @Test
    public void testFindDeserializerWithInvalidTypeId() throws IOException {
        DeserializationContext ctxt = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        }, "typePropertyName", true, new TypeReference<String>() {}.getType());

        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, "InvalidTypeId");
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, "typePropertyName", true, null);

        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
        assertEquals(NullifyingDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, "typePropertyName", true, null);

        JavaType javaType = typeDeserializer._handleUnknownTypeId(ctxt, "UnknownTypeId", null, null);
        assertNull(javaType);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        DeserializationContext ctxt = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, "typePropertyName", true, null);

        JsonParser jp = new TestingParser(new TestingObjectMapper().readTree("{\"typeId\":\"TypeA\"}"));
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, "TypeA");
        assertEquals("TypeA", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdNull() throws IOException {
        DeserializationContext ctxt = new TestingDeserializationContext();
        TypeDeserializerBase typeDeserializer = new TestTypeDeserializerBase(null, null, "typePropertyName", true, null);

        JsonParser jp = new TestingParser(new TestingObjectMapper().readTree("{}"));
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, null);
    }
}

class TestTypeDeserializerBase extends TypeDeserializerBase {

    public TestTypeDeserializerBase(TypeIdResolver idResolver, JavaType baseType,
            String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    TestTypeDeserializerBase(TypeDeserializerBase src, BeanProperty property) {
        super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return new TestTypeDeserializerBase(this, prop);
    }

    @Override
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.EXISTING_PROPERTY;
    }
}

class TestingDeserializationContext extends DeserializationContext {
    @Override
    public JavaType findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
        return new TestStringDeserializer();
    }
}

class TestStringDeserializer extends StdDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return "Test";
    }
}

class TestingParser extends BaseParser {
    public TestingParser(JsonNode node) {
        super(new TestingObjectMapper().getFactory().getParserConfig(), new TestingObjectMapper().getFactory());
        this.parse(((TestMappingWriter) new TestingObjectMapper().getFactory().getGenerator().findValueSerializer(Object.class))
                .writeValueAsString(node));
    }
}

class TestingObjectMapper extends ObjectMapper {
    @Override
    public JavaType typeFromId(String typeId) throws IOException {
        if (typeId.equals("TypeA")) {
            return new TypeReference<String>() {}.getType();
        }
        return super.typeFromId(typeId);
    }
}