package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.time.ZoneId;
import java.util.Locale;

import static org.junit.Assert.*;

public class DatabindContextTest {

    @Test
    public void testGenericConfigAccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getDeserializationContext();

        // Test getConfig() method
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);

        // Test getAnnotationIntrospector() method
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);

        // Test isEnabled() method
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_SERIALIZABLE));

        // Test canOverrideAccessModifiers() method
        assertTrue(context.canOverrideAccessModifiers());

        // Test getActiveView() method
        Class<?> activeView = context.getActiveView();
        assertNull(activeView);

        // Test getLocale() method
        assertEquals(Locale.getDefault(), context.getLocale());

        // Test getTimeZone() method
        TimeZone timeZone = TimeZone.getDefault();
        assertEquals(timeZone, context.getTimeZone());

        // Test getDefaultPropertyFormat() method
        JavaType baseType = getClassType(DatabindContext.class);
        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(baseType);
        assertNotNull(defaultPropertyFormat);
    }

    @Test
    public void testAttributes() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getDeserializationContext();

        // Test getAttribute() method
        Object attribute = context.getAttribute("customAttribute");
        assertNull(attribute);

        context.setAttribute("customAttribute", "customValue");
        assertEquals("customValue", context.getAttribute("customAttribute"));

        // Test setAttribute() method
        context.setAttribute("customAttribute", null);
        assertNull(context.getAttribute("customAttribute"));

        context.setAttribute("customAttribute", "customValue2");
        assertEquals("customValue2", context.getAttribute("customAttribute"));
    }

    @Test
    public void testTypeInstantiationResolution() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getDeserializationContext();

        // Test constructType() method
        Type type = new TypeReference<Integer[][]>() {}.getType();
        JavaType javaType = context.constructType(type);
        assertEquals(javaType.getRawClass(), int[].class);
        assertEquals(javaType.getComponentType().getComponentType(), int[].class);
        assertEquals(javaType.getContentType(), int[][].class);

        // Test constructSpecializedType() method
        JavaType baseType = JavaType.constructType(int[].class);
        JavaType subType = context.constructSpecializedType(baseType, String[].class);
        assertEquals(subType.getRawClass(), String[].class);

        // Test resolveSubType() method
        JavaType baseJavaType = JavaType.constructType(int[].class);
        String baseClass = baseJavaType.getRawClass().getName();
        String subClass = "java.lang.String[]";
        JavaType resolvedType = context.resolveSubType(baseJavaType, subClass);
        assertEquals(resolvedType.getRawClass(), String[].class);

        // Test invalidTypeIdException() method
        JavaType baseType2 = JavaType.constructType(Integer.class);
        String typeId = "java.lang.String[]";
        try {
            context.invalidTypeIdException(baseType2, typeId, "Not a subtype");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testHelperObjectConstruction() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getDeserializationContext();

        // Test objectIdGeneratorInstance() method
        Annotated annotated = null;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonAutoDetect.Std.class, null);
        ObjectIdGenerator<?> idGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNull(idGenerator);

        // Test objectIdResolverInstance() method
        annotated = new AnnotatedClass(String.class, null, null);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo(JsonAutoDetect.Std.class, null);
        ObjectIdResolver idResolver = context.objectIdResolverInstance(annotated, objectIdInfo2);
        assertNotNull(idResolver);

        // Test converterInstance() method
        Annotated annotatedConverter = new AnnotatedMethod(getClass(), "converterInstance", new Class[] {Annotated.class, Object.class});
        Object converterDef = JsonSerialize.With.class;
        Converter<Object, Object> converter = context.converterInstance(annotatedConverter, converterDef);
        assertNotNull(converter);
    }

    @Test
    public void testErrorReporting() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getDeserializationContext();

        // Test reportBadDefinition() method
        JavaType type = getClassType(DatabindContext.class);
        String msg = "Invalid type definition: " + _desc(type.getRawClassName());
        try {
            context.reportBadDefinition(type, msg);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertEquals(msg, e.getMessage());
        }

        // Test reportBadDefinition() method with custom message
        JavaType type2 = getClassType(FromStringDeserializer.class);
        String msg2 = _format("Invalid type definition: %s (%s)", _desc(type2.getRawClassName()), _quotedString(_desc(type2.getDeclaredConstructor().getName())));
        try {
            context.reportBadDefinition(type2, msg2);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
            assertEquals(msg2, e.getMessage());
        }
    }

    @JsonDeserialize(using = FromStringDeserializer.class)
    static class TestClass {
        private int value;

        TestClass(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @JsonSerialize(using = ToStringSerializer.class)
    static class TestEnum {
        private ZoneId zoneId;

        TestEnum(ZoneId zoneId) {
            this.zoneId = zoneId;
        }

        public ZoneId getZoneId() {
            return zoneId;
        }

        @Override
        public String toString() {
            return zoneId.getId();
        }
    }

    private static JavaType getClassType(Class<?> clazz) {
        return JavaType.constructType(clazz);
    }
}