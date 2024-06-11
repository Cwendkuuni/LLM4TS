package com.example.test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private ObjectMapper objectMapper;
    private DatabindContext context;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        context = objectMapper.getDeserializationContext();
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        Assert.assertNotNull(config);
    }

    @Test
    public void testIsEnabled() {
        Assert.assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        Assert.assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));

        MapperConfig<ObjectMapper.DefaultConfig> config = context.getConfig();
        config.disable(MapperFeature.AUTO_DETECT_TYPES);
        Assert.assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
    }

    @Test
    public void testGetActiveView() {
        Assert.assertNull(context.getActiveView());

        context.setActiveView(String.class);
        Assert.assertEquals(String.class, context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        Assert.assertEquals(Locale.getDefault(), context.getLocale());

        context.setLocale(Locale.US);
        Assert.assertEquals(Locale.US, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        Assert.assertEquals(TimeZone.getDefault(), context.getTimeZone());

        context.setTimeZone(TimeZone.getTimeZone("UTC"));
        Assert.assertEquals(TimeZone.getTimeZone("UTC"), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        Assert.assertNull(context.getDefaultPropertyFormat(Object.class));

        context.setDefaultPropertyFormat(new JsonFormat.Value());
        Assert.assertNotNull(context.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testGetStringAttribute() {
        context.setAttribute("test", "testValue");
        Assert.assertEquals("testValue", context.getAttribute("test"));
    }

    @Test
    public void testConstructType() {
        Type intType = new TypeToken<Integer>() {}.getType();
        JavaType javaIntType = context.constructType(intType);
        Assert.assertEquals(SimpleType.constructUnresolved(int.class), javaIntType.getType());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType javaStringType = context.getTypeFactory().constructType(String.class);
        JavaType javaListType = context.getTypeFactory().constructType(List.class, javaStringType);

        String subType = "java.util.ArrayList";
        JavaType resolvedSubType = context.resolveSubType(javaListType, subType);
        Assert.assertEquals(ArrayList.class, resolvedSubType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType javaStringType = context.getTypeFactory().constructType(String.class);
        JavaType javaInvalidType = context.getTypeFactory().constructType("InvalidType");

        context.invalidTypeIdException(javaStringType, "InvalidType", null);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        ObjectReader reader = objectMapper.readerForClass(String.class);
        Annotated annotated = reader._annotationIntrospector.findPropertyAnnotation(reader.getParameterType(), "name");
        ObjectIdInfo objectIdInfo = reader._annotationIntrospector.findObjectIdInfo(annotated);

        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        Assert.assertNotNull(objectIdGenerator);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        ObjectReader reader = objectMapper.readerForClass(String.class);
        Annotated annotated = reader._annotationIntrospector.findPropertyAnnotation(reader.getParameterType(), "name");
        Object converterDef = reader._annotationIntrospector.findConverterAnnotation(annotated);

        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        Assert.assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        Type type = new TypeToken<List<String>>() {}.getType();
        context.reportBadDefinition(type, "Invalid type definition: " + _quotedString("List<String>"));
    }
}