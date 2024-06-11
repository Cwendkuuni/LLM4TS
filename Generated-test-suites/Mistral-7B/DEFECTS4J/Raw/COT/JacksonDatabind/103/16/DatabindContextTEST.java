package com.example.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdResolver;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DatabindContextTest {

    private DatabindContext context;
    private MapperConfig<ObjectMapper> config;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        config = new MapperConfig<>(ObjectMapper.getDefaultConfig());
        config.enable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        objectMapper = config.builder().build();
        context = objectMapper.getSerializationConfig();
    }

    @Test
    public void testGetConfig() {
        assertEquals(config, context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertEquals(config.getDefaultAnnotationIntrospector(), context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        assertEquals(context.getDefaultPropertyFormat(javaType), context.getDefaultPropertyFormat(String.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetDefaultPropertyFormatNullType() {
        context.getDefaultPropertyFormat(null);
    }

    @Test
    public void testGetAttribute() {
        String key = "testKey";
        context.setAttribute(key, "testValue");
        assertEquals("testValue", context.getAttribute(key));
        assertNull(context.getAttribute("unknownKey"));
    }

    @Test
    public void testSetAttribute() {
        context.setAttribute(null, null);
        context.setAttribute("testKey", null);
        assertEquals(context, context.setAttribute("testKey", "testValue"));
    }

    @Test
    public void testConstructType() {
        Type type = String.class;
        JavaType javaType = context.constructType(type);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        Class<String> subclass = String[].class;
        JavaType specializedType = context.constructSpecializedType(baseType, subclass);
        assertEquals(subclass, specializedType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeInvalidType() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "InvalidType";
        context.invalidTypeIdException(baseType, typeId, "InvalidType");
    }

    @Test
    public void testInvalidTypeIdException() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "java.util.List";
        assertThrows(JsonMappingException.class, () -> context.invalidTypeIdException(baseType, typeId, ""));
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        Annotated annotated = new AnnotatedClassInfo(String.class, null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonTypeInfo.Id.CLASS, null, null, null, null, null);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = new AnnotatedClassInfo(String.class, null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonTypeInfo.Id.NONE, null, null, null, null, null);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() {
        Annotated annotated = new AnnotatedClassInfo(String.class, null, null);
        Converter<?> converterDef = StringToIntegerConverter.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        context.reportBadDefinition(javaType, "Bad Definition");
    }

    @Test
    public void testReportBadDefinitionWithMessage() {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        String msg = "Bad Definition: %s";
        assertEquals(null, context.reportBadDefinition(javaType, msg));
        assertThrows(JsonMappingException.class, () -> context.reportBadDefinition(javaType, "%s".formatted("Test")));
    }

    @Test
    public void testFormat() {
        String msg = "Test: %s, %d";
        String test = "TestValue";
        int num = 123;
        assertEquals(_format(msg, test, num), "Test: TestValue, 123");
    }

    @Test
    public void testTruncate() {
        String desc = "This is a very long description that needs to be truncated.";
        assertEquals(_truncate(desc), "This is a very long description that needs to be truncated.]...");
    }

    @Test
    public void testQuotedString() {
        String desc = "This is a \'test\' string with control characters.";
        assertEquals(_quotedString(desc), "\"This is a \\'test\\' string with control characters.\"");
    }

    @Test
    public void testColonConcat() {
        String msgBase = "Test Base Message";
        String extra = "Extra Message";
        assertEquals(_colonConcat(msgBase, extra), "Test Base Message: Extra Message");
    }

    @Test
    public void testDesc() {
        String desc = "This is a test description.";
        assertEquals(_desc(desc), "This is a test description.");
    }

    @Test
    public void testFormatWithNullMsg() {
        String msg = null;
        String test = "TestValue";
        int num = 123;
        assertNull(_format(msg, test, num));
    }

    @Test
    public void testTruncateNullDesc() {
        String desc = null;
        assertEquals(_truncate(desc), "");
    }

    @Test
    public void testQuotedStringWithNullDesc() {
        String desc = null;
        assertEquals(_quotedString(desc), "[N/A]");
    }

    @Test
    public void testColonConcatWithNullMsgBase() {
        String msgBase = null;
        String extra = "Extra Message";
        assertEquals(_colonConcat(msgBase, extra), "Extra Message");
    }
}

class AnnotatedClassInfo implements Annotated {

    private Class<?> clazz;
    private JsonType annotation;
    private JsonSubTypes annotationSubTypes;

    public AnnotatedClassInfo(Class<?> clazz) {
        this.clazz = clazz;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public JsonType getAnnotation() {
        return null;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "subType")
    public JsonSubTypes getAnnotationSubTypes() {
        return null;
    }

    @Override
    public Class<?> getRawClass() {
        return clazz;
    }
}

class StringToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String value, Class<Integer> targetType) {
        return Integer.parseInt(value);
    }
}