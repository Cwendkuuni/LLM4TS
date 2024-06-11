package com.example.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperConfiguration;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MapperFactory mapperFactory = new MapperFactory(new JsonMapperConfiguration());
        this.objectMapper = mapperFactory.getObjectMapper();
    }

    @Test
    public void testGetConfig() {
        DatabindContext context = objectMapper.getSerializationConfig();
        Assert.assertNotNull(context.getConfig());
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = objectMapper.getSerializationConfig();
        Assert.assertTrue(context.isEnabled(MapperFeature.DEFAULT_INTENTS));
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = objectMapper.getSerializationConfig();
        Assert.assertNull(context.getActiveView());
    }

    @Test
    public void testConstructType() {
        DatabindContext context = objectMapper.getSerializationConfig();
        Type type = new TypeToken<String>() {}.getType();
        JavaType javaType = context.constructType(type);
        Assert.assertNotNull(javaType);
        Assert.assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializationConfig();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.util.ArrayList");
        Assert.assertNotNull(subType);
        Assert.assertTrue(subType.isTypeOrSubTypeOf(java.util.ArrayList.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializationConfig();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "InvalidType";
        context.invalidTypeIdException(baseType, typeId, "");
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = objectMapper.getSerializationConfig();
        TypeFactory typeFactory = context.getTypeFactory();
        Assert.assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializationConfig();
        Annotated annotated = new Annotated();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        Assert.assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = objectMapper.getSerializationConfig();
        Annotated annotated = new Annotated();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        Assert.assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializationConfig();
        Annotated annotated = new Annotated();
        Object converterDef = JsonTypeInfo.Id.CLASS;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        Assert.assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializationConfig();
        JavaType type = context.getTypeFactory().constructType(String.class);
        context.reportBadDefinition(type, "Invalid Definition");
    }
}