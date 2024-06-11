package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DatabindContextTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws IOException {
        objectMapper.close();
    }

    @Test
    public void testGenericConfigAccess() {
        DatabindContext context = objectMapper.getSerializationConfig();

        assertEquals(objectMapper.getSerializationConfig(), context.getConfig());
        assertEquals(objectMapper.getAnnotationIntrospector(), context.getAnnotationIntrospector());

        assertEquals(MapperFeature.AUTO_DETECT_CREATORS, context.isEnabled(MapperFeature.AutoDetectCreators));
        assertEquals(MapperFeature.AUTO_DETECT_FIELD_VISIBILITY, context.isEnabled(MapperFeature.AutoDetectFields));
        assertEquals(MapperFeature.AUTO_DETECT_GETTERS, context.isEnabled(MapperFeature.AutoDetectGetters));
        assertEquals(MapperFeature.AUTO_DETECT_IS_EMPTY, context.isEnabled(MapperFeature.AutoDetectIsEmpty));
        assertEquals(MapperFeature.AUTO_DETECT_UNSAFE_FOR_DESERIALIZATION, context.isEnabled(MapperFeature.AutoDetectUnsafeForDeserialization));
        assertEquals(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, context.canOverrideAccessModifiers());
        assertNull(context.getActiveView());

        assertEquals(Locale.ENGLISH, context.getLocale());
        assertEquals(TimeZone.getDefault(), context.getTimeZone());

        assertEquals(JsonFormat.Value.serializableDateFormat(), context.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testAttributes() throws Exception {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("key", "value");

        JsonNodeReader reader = objectMapper.readerForTree(ObjectNode.class);
        ObjectNode result = reader.readValue(node);

        DatabindContext context = result.getParser().getSerializationConfig();

        Object attribute = context.getAttribute("testAttribute");
        assertNull(attribute);

        context = context.setAttribute("testAttribute", "testValue");
        assertNotNull(context.getAttribute("testAttribute"));
        assertEquals("testValue", context.getAttribute("testAttribute"));
    }

    @Test
    public void testTypeInstantiation() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String[].class);
        assertEquals(String[].class, javaType.getRawClass());

        JavaType specializedType = objectMapper.getTypeFactory().constructSpecializedType(javaType, String[].class);
        assertEquals(String[].class, specializedType.getRawClass());
        assertEquals(String[].class, specializedType.getRawClass());

        JavaType resolvedSubType = objectMapper.getTypeFactory().resolveSubType(javaType, "java.lang.String[]");
        assertEquals(String[].class, resolvedSubType.getRawClass());
    }

    @Test
    public void testInvalidTypeIdException() throws Exception {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
        JavaType subType = null;

        try {
            subType = objectMapper.getTypeFactory().resolveSubType(baseType, "java.lang.Integer");
        } catch (JsonMappingException e) {
            assertEquals("Not a subtype", e.getMessage());
            return;
        }

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        AnnotatedClass annotatedClass = objectMapper.getAnnotationIntrospector().findAnnotation(Object.class, JsonProperty.PropertyAnnotation.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotatedClass, null, null);

        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testReportBadDefinition() throws Exception {
        Class<?> clazz = String[].class;
        assertEquals(String[].class, ((TypeFactory) objectMapper.getTypeFactory()).constructType(clazz).getRawClass());
        assertEquals("java.lang.Class<java.lang.String[]>", clazz.getName());

        String msg = "Test error message";
        assertEquals(new InvalidDefinitionException(msg), context.reportBadDefinition(clazz, msg));
    }
}