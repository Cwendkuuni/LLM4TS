package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        DatabindContext context = new TestDatabindContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = new TestDatabindContext();
        AnnotationIntrospector introspector = context.getAnnotationIntrospector();
        assertNotNull(introspector);
    }

    @Test
    public void testIsEnabled_MapperFeature() {
        DatabindContext context = new TestDatabindContext();
        boolean result = context.isEnabled(MapperFeature.DEFAULT_INTROSPECTION);
        assertTrue(result);
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = new TestDatabindContext();
        boolean result = context.canOverrideAccessModifiers();
        assertTrue(result);
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = new TestDatabindContext();
        Class<?> activeView = context.getActiveView();
        assertNull(activeView);
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = new TestDatabindContext();
        Locale locale = context.getLocale();
        assertEquals(Locale.getDefault(), locale);
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = new TestDatabindContext();
        TimeZone timeZone = context.getTimeZone();
        assertEquals(TimeZone.getDefault(), timeZone);
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = new TestDatabindContext();
        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(String.class);
        assertNull(defaultPropertyFormat);
    }

    @Test
    public void testGetAttribute() {
        DatabindContext context = new TestDatabindContext();
        context.setAttribute("testKey", "testValue");
        Object attribute = context.getAttribute("testKey");
        assertEquals("testValue", attribute);
    }

    @Test
    public void testSetAttribute() {
        DatabindContext context = new TestDatabindContext();
        context.setAttribute("testKey", "testValue");
        DatabindContext newContext = context.setAttribute("testKey", "newTestValue");
        assertEquals("newTestValue", newContext.getAttribute("testKey"));
    }

    @Test
    public void testConstructType() {
        DatabindContext context = new TestDatabindContext();
        Type type = Object.class;
        JavaType javaType = context.constructType(type);
        assertNotNull(javaType);
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = new TestDatabindContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        Class<?> subclass = String[].class;
        JavaType specializedType = context.constructSpecializedType(baseType, subclass);
        assertNotNull(specializedType);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType_InvalidTypeId() {
        DatabindContext context = new TestDatabindContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "com.invalid.package.InvalidType";
        context.resolveSubType(baseType, typeId);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() {
        DatabindContext context = new TestDatabindContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "NotASubtype";
        context.invalidTypeIdException(baseType, typeId, "");
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = new TestDatabindContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        DatabindContext context = new TestDatabindContext();
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = new TestDatabindContext();
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() {
        DatabindContext context = new TestDatabindContext();
        Annotated annotated = mock(Annotated.class);
        Object converterDef = Converter.stringConverter(null);
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() {
        DatabindContext context = new TestDatabindContext();
        JavaType type = context.getTypeFactory().constructType(String.class);
        String msg = "test error message";
        try {
            context.reportBadDefinition(type, msg);
            fail();
        } catch (JsonMappingException e) {
            assertEquals(msg, e.getMessage());
        }
    }
}

class TestDatabindContext extends DatabindContext {
    @Override
    TypeFactory getTypeFactory() {
        return new TypeFactoryForTest();
    }

    @Override
    JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
        return new InvalidDefinitionException(baseType, typeId, extraDesc);
    }
}

class TypeFactoryForTest extends TypeFactory {
    @Override
    public JavaType constructFromCanonical(String name) throws ClassNotFoundException {
        try {
            return super.constructFromCanonical(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // for testing purposes
        }
    }
}

class InvalidDefinitionException extends JsonMappingException {
    public InvalidDefinitionException(JavaType baseType, String typeId, String extraDesc) {
        super("Invalid definition for type: [" + baseType.getRawClass().getName() + "] " + extraDesc);
    }
}