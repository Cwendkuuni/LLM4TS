package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertNotNull(context.getConfig());
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertNotNull(context.getAnnotationIntrospector());
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertTrue(context.isEnabled(MapperFeature.AUTODETECT_TYPES));
            assertFalse(context.isEnabled(MapperFeature.DEFAULT_LOCALE_BEHAVIOR_ENFORCED));
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertTrue(context.canOverrideAccessModifiers());
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertNull(context.getActiveView());
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertEquals("default", context.getLocale().getDisplayName());
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertEquals("UTC", context.getTimeZone().getID());
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertNull(context.getDefaultPropertyFormat(Object.class));
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testGetAttribute() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertNull(context.getAttribute("unknown"));
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }

        context = new TestMapperConfig().getDefaultDeserializationContext();
        context.setAttribute("test", "value");
        assertEquals("value", context.getAttribute("test"));
    }

    @Test
    public void testConstructType() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            JavaType type = context.constructType(int[].class);
            assertNotNull(type);
            assertEquals(int[].class, type.getRawClass());
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            JavaType baseType = context.constructType(String.class);
            JavaType subType = context.constructSpecializedType(baseType, StringBuffer.class);
            assertNotNull(subType);
            assertEquals(StringBuffer.class, subType.getRawClass());
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testResolveSubType() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            JavaType baseType = context.constructType(String.class);
            JavaType subType = context.resolveSubType(baseType, "StringBuffer");
            assertNotNull(subType);
            assertEquals(StringBuffer.class, subType.getRawClass());
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }

        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            JavaType baseType = context.constructType(String.class);
            JavaType subType = context.resolveSubType(baseType, "ArrayList<Integer>");
            assertNull(subType);
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidTypeIdException() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            assertNotNull(context.invalidTypeIdException(null, null, null));
            fail("Expected exception");
        } catch (JsonMappingException e) {
            // expected
        }

        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            JavaType baseType = context.constructType(String.class);
            JavaType subType = context.resolveSubType(baseType, "StringBuffer");
            assertNotNull(subType);
            try {
                context.invalidTypeIdException(baseType, "ArrayList<Integer>", null);
                fail("Expected exception");
            } catch (JsonMappingException e) {
                // expected
            }
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            TypeFactory typeFactory = context.getTypeFactory();
            assertNotNull(typeFactory);
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(null, null);
            assertNull(objectIdGenerator);
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }

        try {
            Annotated annotated = mock(Annotated.class);
            ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
            ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
            assertNotNull(objectIdGenerator);
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonMappingException {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(null, null);
            assertNull(objectIdResolver);
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }

        try {
            Annotated annotated = mock(Annotated.class);
            ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
            ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
            assertNotNull(objectIdResolver);
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            Converter<Object, Object> converter = context.converterInstance(null, null);
            assertNull(converter);
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }

        try {
            Annotated annotated = mock(Annotated.class);
            Object converterDef = JsonTypeInfo.As.CLASS.value();
            Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
            assertNotNull(converter);
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = null;
        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            JavaType type = context.constructType(String.class);
            assertNull(context.reportBadDefinition(type, "Error message"));
        } catch (JsonMappingException e) {
            fail("Failed to initialize context: " + e.getMessage());
        }

        try {
            context = new TestMapperConfig().getDefaultDeserializationContext();
            JavaType type = context.constructType(String.class);
            throw context.reportBadDefinition(type, "Error message");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}

class TestMapperConfig extends MapperConfig<Object> {
    @Override
    public HandlerInstantiator getHandlerInstantiator() {
        return null;
    }
}