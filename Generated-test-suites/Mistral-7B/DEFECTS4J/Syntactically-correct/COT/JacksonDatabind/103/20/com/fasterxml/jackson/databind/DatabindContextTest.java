package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        DatabindContext context = createMockDatabindContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = createMockDatabindContext();
        AnnotationIntrospector introspector = context.getAnnotationIntrospector();
        assertNotNull(introspector);
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = createMockDatabindContext();
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_INTENTS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_FOR_SUBTYPES));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = createMockDatabindContext();
        assertTrue(context.canOverrideAccessModifiers());
        assertFalse(context.canOverrideAccessModifiers(false));
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = createMockDatabindContext();
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = createMockDatabindContext();
        assertNull(context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = createMockDatabindContext();
        assertNull(context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = createMockDatabindContext();
        JavaType baseType = createType(String.class);
        JsonFormat.Value format = context.getDefaultPropertyFormat(baseType);
        assertNotNull(format);
    }

    @Test
    public void testGetAttribute() {
        DatabindContext context = createMockDatabindContext();
        context.setAttribute("test", 123);
        assertEquals(123, context.getAttribute("test"));
        assertNull(context.getAttribute("unknown"));
    }

    @Test
    public void testSetAttribute() {
        DatabindContext context = createMockDatabindContext();
        context.setAttribute("test", "test");
        assertEquals("test", context.getAttribute("test"));
    }

    @Test(expected = JsonMappingException.class)
    public void testConstructTypeNullType() {
        DatabindContext context = createMockDatabindContext();
        context.getTypeFactory().constructType(null);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testConstructType() {
        DatabindContext context = createMockDatabindContext();
        Type type = new TypeToken<Object>() {}.getType();
        JavaType javaType = context.getTypeFactory().constructType(type);
        assertNotNull(javaType);
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext context = createMockDatabindContext();
        JavaType baseType = createType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.String");
        assertNotNull(subType);
        assertEquals(String.class, subType.getRawClass());
    }

    @Test
    public void testInvalidTypeIdException() {
        DatabindContext context = createMockDatabindContext();
        JavaType baseType = createType(String.class);
        String typeId = "InvalidTypeId";
        try {
            context.invalidTypeIdException(baseType, typeId, "");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = createMockDatabindContext();
        Annotated annotated = createMockAnnotated();
        ObjectIdInfo objectIdInfo = createMockObjectIdInfo();
        ObjectIdGenerator<?> idGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(idGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = createMockDatabindContext();
        Annotated annotated = createMockAnnotated();
        ObjectIdInfo objectIdInfo = createMockObjectIdInfo();
        ObjectIdResolver idResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(idResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = createMockDatabindContext();
        Annotated annotated = createMockAnnotated();
        Object converterDef = createMockConverterDefinition(String.class, String[].class);
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionNullType() throws JsonMappingException {
        DatabindContext context = createMockDatabindContext();
        String msg = "Test message";
        context.reportBadDefinition(null, msg);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = createMockDatabindContext();
        JavaType type = createType(String.class);
        String msg = "Test message";
        Object result = context.reportBadDefinition(type, msg);
        assertNull(result);
    }

    private DatabindContext createMockDatabindContext() {
        return (DatabindContext) Mockito.mock(DatabindContext.class);
    }

    private Annotated createMockAnnotated() {
        return (Annotated) Mockito.mock(Annotated.class);
    }

    private ObjectIdInfo createMockObjectIdInfo() {
        return (ObjectIdInfo) Mockito.mock(ObjectIdInfo.class);
    }

    private Object createMockConverterDefinition(Class<?> inputType, Class<?> outputType) {
        JsonTypeName typeNameAnnotation = Mockito.mock(JsonTypeName.class);
        when(typeNameAnnotation.value()).thenReturn("TestTypeName");
        when(typeNameAnnotation.typeName()).thenReturn("TestTypeName");
        when(typeNameAnnotation.value()).thenReturn("TestTypeName");
        return typeNameAnnotation;
    }

    private JavaType createType(Class<?> clazz) {
        return new TypeFactory().constructType(clazz);
    }
}