package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private DatabindContext context;

    @Before
    public void setUp() {
        context = new TestDatabindContext();
    }

    @Test
    public void testGetConfig() {
        assertNull(context.getConfig());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConfigThrowsUOE() {
        context.getConfig();
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertNull(context.getAnnotationIntrospector());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAnnotationIntrospectorThrowsUOE() {
        context.getAnnotationIntrospector();
    }

    @Test
    public void testIsEnabled() {
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_FOR_PRIMITIVES));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsEnabledThrowsUOE() {
        context.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_FOR_PRIMITIVES);
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertFalse(context.canOverrideAccessModifiers());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCanOverrideAccessModifiersThrowsUOE() {
        context.canOverrideAccessModifiers();
    }

    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActiveViewThrowsUOE() {
        context.getActiveView();
    }

    @Test
    public void testGetLocale() {
        assertNull(context.getLocale());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocaleThrowsUOE() {
        context.getLocale();
    }

    @Test
    public void testGetTimeZone() {
        assertNull(context.getTimeZone());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTimeZoneThrowsUOE() {
        context.getTimeZone();
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        assertNull(context.getDefaultPropertyFormat(Object.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDefaultPropertyFormatThrowsUOE() {
        context.getDefaultPropertyFormat(Object.class);
    }

    @Test
    public void testGetAttribute() {
        assertNull(context.getAttribute(getClass()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAttributeThrowsUOE() {
        context.getAttribute(getClass());
    }

    @Test
    public void testSetAttribute() {
        context.setAttribute(getClass(), "value");
        Object attr = context.getAttribute(getClass());
        assertEquals("value", attr);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAttributeThrowsUOE() {
        context.setAttribute(getClass(), "value");
    }

    @Test
    public void testConstructType() {
        JavaType type = context.constructType(String[].class);
        assertNotNull(type);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructTypeThrowsUOE() {
        context.constructType(String[].class);
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.constructType(String[].class);
        JavaType subType = context.constructSpecializedType(baseType, String[].class);
        assertNotNull(subType);
        assertEquals(String[].class, subType.getRawClass());
        assertEquals(baseType.getRawClass(), subType.getBaseType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructSpecializedTypeThrowsUOE() {
        context.constructSpecializedType(String[].class, String[].class);
    }

    @Test
    public void testResolveSubType() {
        JavaType baseType = context.constructType(String[].class);
        JavaType subType = context.resolveSubType(baseType, "java.util.List[Ljava.lang.String;]");
        assertNotNull(subType);
        assertEquals(String[].class, subType.getRawClass());
        assertEquals(baseType.getRawClass(), subType.getBaseType());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() {
        JavaType baseType = context.constructType(String[].class);
        context.invalidTypeIdException(baseType, "java.util.List[Ljava.lang.Integer;]", "InvalidTypeIdException");
    }

    @Test
    public void testGetTypeFactory() {
        assertNotNull(context.getTypeFactory());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeFactoryThrowsUOE() {
        context.getTypeFactory();
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        assertNull(context.objectIdGeneratorInstance(null, null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectIdGeneratorInstanceThrowsUOE() {
        context.objectIdGeneratorInstance(null, null);
    }

    @Test
    public void testObjectIdResolverInstance() {
        assertNull(context.objectIdResolverInstance(null, null));
    }

    @Test
    public void testConversionInstance() {
        assertNull(context.converterInstance(null, null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConversionInstanceThrowsUOE() {
        context.converterInstance(null, null);
    }

    @Test
    public void testReportBadDefinition() {
        assertThrows(JsonMappingException.class, () -> context.reportBadDefinition(null, "Message"));
    }

    @Test
    public void testReportBadDefinitionString() {
        assertThrows(JsonMappingException.class, () -> context.reportBadDefinition("Class", "Message"));
    }
}

class TestDatabindContext extends DatabindContext {
    @Override
    public MapperConfig<?> getConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AnnotationIntrospector getAnnotationIntrospector() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled(MapperFeature feature) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canOverrideAccessModifiers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getActiveView() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TimeZone getTimeZone() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DatabindContext setAttribute(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaType constructType(Type type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaType resolveSubType(JavaType baseType, String subClass) throws JsonMappingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
        throw new JsonMappingException("InvalidTypeIdException: " + extraDesc);
    }

    @Override
    public TypeFactory getTypeFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
            throws JsonMappingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef)
            throws JsonMappingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
        throw new JsonMappingException("BadDefinition: " + msg);
    }
}