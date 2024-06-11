package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.TypeDeserializer;
import org.junit.Test;

public class SubTypeValidatorTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testValidateSubType_ValidTypes() {
        JavaType validType1 = mapper.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, validType1);

        JavaType validType2 = mapper.getTypeFactory().constructType(Integer.class);
        validator.validateSubType(null, validType2);
    }

    @Test
    public void testValidateSubType_NastyClasses() {
        JavaType nastyType1 = mapper.getTypeFactory().constructType(Object[].class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, nastyType1));

        JavaType nastyType2 = mapper.getTypeFactory().constructType(SubTypeValidator.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, nastyType2));

        JavaType nastyType3 = mapper.getTypeFactory().constructType(com.mchange.v2.c3p0.ComboPooledDataSource.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, nastyType3));
    }

    @Test
    public void testValidateSubType_SpringClasses() throws JsonProcessingException {
        JavaType springType1 = mapper.getTypeFactory().constructType(org.springframework.core.io.Resource.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, springType1);

        JavaType springType2 = mapper.getTypeFactory().constructType(org.springframework.core.type.ClassMetadata.class);
        TypeDeserializer typeDeserializer = mapper.getFactory().getDefaultInstance().findValueDeserializer(Type.class, null);
        JavaType springType3 = mapper.getTypeFactory().constructType(typeDeserializer.deserialize(new Object(), null).getClass());
        validator.validateSubType(null, springType3);
    }
}