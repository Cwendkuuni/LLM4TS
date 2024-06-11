package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DatabindContextTest {

    private static final DatabindContext context = new BaseTestMapper().getMapper().getDeserializationContext();

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
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATOR_FROM_ANNOTATIONS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATOR_FROM_ANNOTATIONS_ACCESSED_BY_NAME));
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
        assertEquals(Locale.US, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertEquals("America/Los_Angeles", context.getTimeZone().getId());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(javaType);
        assertNotNull(defaultPropertyFormat);
    }

    @Test
    public void testGetAttribute() {
        Object attribute = context.getAttribute("test");
        assertNull(attribute);
    }

    @Test
    public void testSetAttribute() {
        context.setAttribute("test", "value");
        Object attribute = context.getAttribute("test");
        assertEquals("value", attribute);
    }

    @Test
    public void testConstructType() {
        Type type = Integer[].class;
        JavaType javaType = context.constructType(type);
        assertEquals(JavaType.ArrayType.getInstance(Integer.TYPE), javaType.getBaseType());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.getTypeFactory().constructType(List.class);
        JavaType subclass = context.getTypeFactory().constructType(ArrayList.class);
        JavaType specializedType = context.constructSpecializedType(baseType, subclass);
        assertEquals(subclass, specializedType);
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String subClass = "java.util.ArrayList<java.lang.String>";
        JavaType resolvedSubType = context.resolveSubType(baseType, subClass);
        assertEquals(JavaType.ArrayType.getInstance(JavaType.constructType(ArrayList.class)), resolvedSubType);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "NotAValidType";
        context.invalidTypeIdException(baseType, typeId, "Not a subtype");
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertInstanceof(TypeFactory.class, typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        AnnotatedClass annotated = new AnnotatedClass(String.class, new JsonTypeInfo.Id(JsonTypeInfo.As.PROPERTY, "@foo"));
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, null);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        AnnotatedClass annotated = new AnnotatedClass(String.class, new JsonTypeInfo.Id(JsonTypeInfo.As.PROPERTY, "@foo"));
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, null);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(String.class.getMethod("toUpperCase"), new Annotation[0]);
        Converter<String, String> converter = context.converterInstance(annotatedMethod, Converter.stringConverter());
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        String msg = "Bad definition";
        context.reportBadDefinition(javaType, msg);
    }

    @Test
    public void testReportBadDefinitionWithClass() throws JsonMappingException {
        Class<?> clazz = Integer.class;
        String msg = "Bad definition";
        context.reportBadDefinition(clazz, msg);
    }
}