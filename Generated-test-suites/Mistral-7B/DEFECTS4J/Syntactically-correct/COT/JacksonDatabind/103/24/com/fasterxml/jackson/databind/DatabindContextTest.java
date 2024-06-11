package com.example.test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        assertTrue(context.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATOR_FROM_ANNOTATIONS));
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        assertNull(context.getActiveView());

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.AUTO_DETECT_CREATOR_FROM_ANNOTATIONS);
        context = mapper.getSerializerProvider().getDefaultSerializers();
        assertNull(context.getActiveView());

        JavaClass viewClass = mapper.getTypeFactory().constructType(String.class);
        context.setActiveView(viewClass);
        assertEquals(viewClass, context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        assertNull(context.getLocale());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setLocale(Locale.US);
        context = mapper.getSerializerProvider().getDefaultSerializers();
        assertEquals(Locale.US, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        assertNull(context.getTimeZone());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getDefault());
        context = mapper.getSerializerProvider().getDefaultSerializers();
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        assertNull(context.getDefaultPropertyFormat(Object.class));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(new MapperConfig(new JsonFactory(), true));
        context = mapper.getSerializerProvider().getDefaultSerializers();
        assertEquals(JsonFormat.Value.dateTimeNoTimezone(), context.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testGetAttribute() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        assertNull(context.getAttribute("key"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(new MapperConfig(new JsonFactory(), true));
        context = mapper.getSerializerProvider().getDefaultSerializers();
        context.setAttribute("key", "value");
        assertEquals("value", context.getAttribute("key"));
    }

    @Test
    public void testConstructType() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        JavaType javaType = context.constructType(int[].class);
        assertNotNull(javaType);
        assertEquals(JavaType.UNDEFINED, javaType.getGenericType());

        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getSerializerProvider().getDefaultSerializers();
        JavaType listType = context.constructType(List.class);
        assertNotNull(listType);
        assertEquals(JavaType.DEFINED, listType.getGenericType());
        assertEquals(JavaType.constructFromCanonical("java.util.List"), listType.getRawType());
    }

    @Test
    public void testResolveSubType() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> {
            context.resolveSubType(JavaType.constructFromCanonical("com.example.Foo"), "com.example.Bar");
        });
        assertTrue(exception.getMessage().contains("Not a subtype"));

        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getSerializerProvider().getDefaultSerializers();
        JavaType baseType = JavaType.constructFromCanonical("com.example.Foo");
        JavaType subType = context.resolveSubType(baseType, "com.example.Bar");
        assertNotNull(subType);
        assertEquals(baseType.getRawClass(), subType.getRawClass());
        assertEquals("com.example.Bar", subType.getName());
    }

    @Test
    public void testInvalidTypeIdException() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        try {
            context.invalidTypeIdException(JavaType.constructFromCanonical("com.example.Foo"), "com.example.Bar", null);
            fail();
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().startsWith("Not a subtype"));
        }

        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getSerializerProvider().getDefaultSerializers();
        try {
            context.invalidTypeIdException(JavaType.constructFromCanonical("com.example.Foo"), "com.example.Bar", "message");
            fail();
        } catch (JsonMappingException e) {
            assertEquals("message: Not a subtype", e.getMessage());
        }
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, "id", null);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(null, objectIdInfo);
        assertNull(objectIdGenerator);

        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getSerializerProvider().getDefaultSerializers();
        ObjectIdGenerator<?> objectIdGenerator1 = context.objectIdGeneratorInstance(null, objectIdInfo);
        assertNotNull(objectIdGenerator1);

        ObjectIdGenerator<?> objectIdGenerator2 = context.objectIdGeneratorInstance(null, objectIdInfo);
        assertEquals(objectIdGenerator1, objectIdGenerator2);
    }

    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, "id", null);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(null, objectIdInfo);
        assertNull(objectIdResolver);

        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getSerializerProvider().getDefaultSerializers();
        ObjectIdResolver objectIdResolver1 = context.objectIdResolverInstance(null, objectIdInfo);
        assertNotNull(objectIdResolver1);

        ObjectIdResolver objectIdResolver2 = context.objectIdResolverInstance(null, objectIdInfo);
        assertEquals(objectIdResolver1, objectIdResolver2);
    }

    @Test
    public void testConverterInstance() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        Annotated annotated = new ObjectMapper().getTypeFactory().constructType(String.class);
        Object converterDef = String.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);

        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getSerializerProvider().getDefaultSerializers();
        Converter<Object, Object> converter1 = context.converterInstance(annotated, converterDef);
        assertNotNull(converter1);

        Converter<Object, Object> converter2 = context.converterInstance(annotated, converterDef);
        assertEquals(converter1, converter2);
    }

    @Test
    public void testReportBadDefinition() {
        DatabindContext context = new ObjectMapper().getSerializerProvider().getDefaultSerializers();
        JavaType javaType = JavaType.constructFromCanonical("com.example.Foo");
        String message = "message";
        try {
            @SuppressWarnings("unchecked")
            Class<RuntimeException> expectedType = (Class<RuntimeException>) Class.forName("java.lang.RuntimeException");
            @SuppressWarnings("unchecked")
            RuntimeException exception = (RuntimeException) context.reportBadDefinition(javaType, message);
            fail();
        } catch (ClassNotFoundException e) {
            // expected
        }

        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getSerializerProvider().getDefaultSerializers();
        exception = assertThrows(JsonMappingException.class, () -> {
            context.reportBadDefinition(javaType, message);
        });
        assertEquals("message", exception.getMessage());
    }
}