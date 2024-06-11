package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(JsonType.THE_STRING_TYPE, mapper.getTypeFactory(), "type", true, String.class);

        BeanProperty beanProperty = new BeanProperty("typeProperty", String.class);
        TypeDeserializer propertyTypeDeserializer = typeDeserializerBase.forProperty(beanProperty);

        assertEquals("BaseTypeDeserializer", propertyTypeDeserializer.getClass().getSimpleName());
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, propertyTypeDeserializer.getTypeInclusion());
    }

    @Test
    public void testFindDeserializer() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(JsonType.THE_STRING_TYPE, mapper.getTypeFactory(), "type", true, String.class);
        DeserializationContext context = mapper.getDeserializationContext();

        String typeId1 = "com.example.StringType1";
        JsonDeserializer<Object> deserializer1 = new StringDeserializer();
        typeDeserializerBase._deserializers.put(typeId1, deserializer1);

        String typeId2 = "com.example.StringType2";
        JsonDeserializer<Object> deserializer2 = new StringDeserializer();
        typeDeserializerBase._deserializers.put(typeId2, deserializer2);

        JsonParser parser = factory.createParser("{\"type\":\"");
        parser.nextToken();
        parser.getValueAsString(); // consume "type"
        parser.nextToken();
        parser.setValue("" + typeId1 + '"');
        Object result1 = typeDeserializerBase._deserializeWithNativeTypeId(parser, context);
        assertEquals(deserializer1.getClass(), result1.getClass());

        parser = factory.createParser("{\"type\":\"");
        parser.nextToken();
        parser.getValueAsString(); // consume "type"
        parser.nextToken();
        parser.setValue("" + typeId2 + '"');
        Object result2 = typeDeserializerBase._deserializeWithNativeTypeId(parser, context);
        assertEquals(deserializer2.getClass(), result2.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownTypeId() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(JsonType.THE_STRING_TYPE, mapper.getTypeFactory(), "type", true, String.class);
        DeserializationContext context = mapper.getDeserializationContext();

        context.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String typeId = "com.example.UnknownType";
        JsonParser parser = factory.createParser("{\"type\":\"");
        parser.nextToken();
        parser.getValueAsString(); // consume "type"
        parser.nextToken();
        parser.setValue("" + typeId + '"');
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, context);
        fail("Unexpected result: " + result.getClass().getName());
    }

    static class BaseTypeDeserializer extends TypeDeserializerBase {
        public BaseTypeDeserializer(JavaType baseType, TypeIdResolver idResolver,
                                    String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }
    }

    static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}