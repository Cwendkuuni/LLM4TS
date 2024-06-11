package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorImpl;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtils;
import com.fasterxml.jackson.databind.util.DefaultObjectMapperConfiguration;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.fasterxml.jackson.databind.util.TypeFactory;
import com.fasterxml.jackson.databind.util.TypeUtil;

public class DatabindContextTest {

    private DatabindContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MapperConfig<Object> config = new DefaultObjectMapperConfiguration();
        config.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        objectMapper = new ObjectMapper(config);
        context = objectMapper.getDeserializationContext();
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector introspector = context.getAnnotationIntrospector();
        assertNotNull(introspector);
    }

    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS.name() + "wrong"));
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
        Locale locale = Locale.getDefault();
        assertEquals(locale, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        assertEquals(timeZone, context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        Class<?> baseType = String.class;
        JsonFormat.Value format = context.getDefaultPropertyFormat(baseType);
        assertNotNull(format);
    }

    @Test
    public void testGetAttribute() {
        String key = "testKey";
        Object value = "testValue";
        context.setAttribute(key, value);
        Object attribute = context.getAttribute(key);
        assertEquals(value, attribute);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeWithNullKey() {
        context.getAttribute(null);
    }

    @Test
    public void testGetAttributeWithNonExistentKey() {
        Object attribute = context.getAttribute("nonExistentKey");
        assertNull(attribute);
    }

    @Test
    public void testConstructType() {
        Type type = String.class;
        JavaType javaType = context.constructType(type);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        Class<?> subclass = Object.class;
        JavaType javaType = context.constructSpecializedType(baseType, subclass);
        assertNotNull(javaType);
        assertFalse(javaType.isTypeOrSubTypeOf(String.class));
        assertTrue(javaType.isTypeOrSubTypeOf(Object.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeWithInvalidTypeId() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String subClass = "java.util.List<java.lang.Integer>";
        context.resolveSubType(baseType, subClass);
    }

    @Test
    public void testInvalidTypeIdException() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "notAString";
        try {
            context.invalidTypeIdException(baseType, typeId, "Not a String");
        } catch (JsonMappingException e) {
            assertEquals("Not a String: Not a subtype", e.getMessage());
        }
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        AnnotatedClass annotatedClass = new AnnotatedClass(Object.class, null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerator.class, null);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        AnnotatedClass annotatedClass = new AnnotatedClass(Object.class, null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdResolver.class, null);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws Exception {
        JsonProperty prop = new JsonProperty();
        prop.value = Converter.StringConverter.class;
        Annotated annotated = new AnnotatedClass(String.class, null, prop);
        Converter<Object, Object> converter = context.converterInstance(annotated, null);
        assertNotNull(converter);
        assertTrue(converter instanceof StdConverter.StdStringConverter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionWithInvalidType() {
        ClassType type = context.getTypeFactory().constructType(String[].class);
        context.reportBadDefinition(type, "Invalid type");
    }

    @Test
    public void testReportBadDefinitionWithString() {
        SimpleType type = context.getTypeFactory().constructSimpleType(String.class);
        String msg = "Invalid String type";
        context.reportBadDefinition(type, msg);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConstructConverter() throws Exception {
        Class<?> converterClass = ConverterUtils.Converting.class;
        Annotated annotated = new AnnotatedClass(String.class, null, null);
        Converter<Object, Object> converter = context.converterInstance(annotated, converterClass);
        assertEquals(ConverterUtils.Converting.class, converter.getClass());
    }

    @Test
    public void testFormat() {
        String msg = "Test message {0} with extra {1}";
        String formattedMsg = context._format(msg, "testValue1", "testValue2");
        assertEquals("Test message testValue1 with extra testValue2", formattedMsg);
    }

    @Test
    public void testTruncate() {
        String desc = "This is a long description that needs to be truncated for error reporting";
        String truncatedDesc = context._truncate(desc);
        assertTrue(truncatedDesc.contains("This is a long description"));
        assertTrue(truncatedDesc.contains("error reporting"));
        assertTrue(truncatedDesc.length() <= 500);
    }

    @Test
    public void testColonConcat() {
        String msgBase = "Test message base";
        String extra = "with extra info";
        String formattedMsg = context._colonConcat(msgBase, extra);
        assertEquals("Test message base: with extra info", formattedMsg);
    }

    @Test
    public void testDesc() {
        String desc = "This is a description of an object";
        String formattedDesc = context._desc(desc);
        assertEquals("This is a description of an object", formattedDesc);
    }

    @Test
    public void testQuotedString() {
        String desc = "This is a string that needs to be quoted for error reporting";
        String formattedDesc = context._quotedString(desc);
        assertTrue(formattedDesc.contains("This is a string that needs to be quoted"));
        assertTrue(formattedDesc.startsWith("\"") && formattedDesc.endsWith("\""));
    }
}