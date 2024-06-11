package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Type;

public class DatabindContextTest {

    private DatabindContext context;

    @Before
    public void setUp() {
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());
        context = config.getDeserializationContext();
    }

    @Test
    public void testGetConfig() {
        assertNotNull(context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertNotNull(context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATOR_FROM_ANNOTATIONS));
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
    public void testgetDefaultPropertyFormat() {
        assertEquals(null, context.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testGetAttribute() {
        assertNull(context.getAttribute(Object.class));
    }

    @Test
    public void testSetAttribute() {
        Object attr = new Object();
        context.setAttribute(Object.class, attr);
        assertEquals(attr, context.getAttribute(Object.class));
    }

    @Test
    public void testConstructType() {
        Type type = String[].class;
        JavaType javaType = context.constructType(type);
        assertEquals(javaType.getRawClass(), String[].class);
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.constructType(String.class);
        JavaType subType = context.constructSpecializedType(baseType, String[].class);
        assertEquals(subType.getRawClass(), String[].class);
        assertEquals(baseType, subType.getBaseType());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.String[]");
        assertEquals(subType.getRawClass(), String[].class);

        JavaType invalidSubType = context.resolveSubType(baseType, "com.test.InvalidType");
        assertNull(invalidSubType);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.constructType(String.class);
        context.invalidTypeIdException(baseType, "com.test.InvalidType", "Invalid type");
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, null);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = new AnnotatedClass(String[].class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, null);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(String[].class);
        Object converterDef = new Object() {};
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = context.constructType(String.class);
        String msg = "Invalid definition for type %s";
        assertEquals(null, context.reportBadDefinition(type, msg));

        String className = "com.test.InvalidType";
        throwExpected(JsonMappingException.class, () -> context.reportBadDefinition(type, msg, className));
    }
}