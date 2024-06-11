package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Tests for {@link DatabindContext}.
 */
public class DatabindContextTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonTest
    public void testDatabindContext_basics() throws Exception {
        DatabindContext context = MAPPER.getRootContext();
        assertNotNull(context);
        assertInstanceOf(DatabindContext.class, context);

        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
        assertInstanceOf(MapperConfig.class, config);

        assertEquals(MapperConfig.StandardConfig.class, config.getClass());

        assertSame(config, context.getConfig());

        AnnotationIntrospector introspector = context.getAnnotationIntrospector();
        assertNotNull(introspector);
        assertInstanceOf(AnnotationIntrospector.class, introspector);

        assertEquals(AnnotationIntrospector.Default.class, introspector.getClass());

        assertSame(introspector, context.getAnnotationIntrospector());

        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));

        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_SERIALIZERS));

        assertFalse(context.canOverrideAccessModifiers());

        assertNull(context.getActiveView());

        assertEquals(Locale.getDefault(), context.getLocale());

        assertEquals(TimeZone.getDefault(), context.getTimeZone());

        assertEquals(JsonFormat.Defaults.generalObject, context.getDefaultPropertyFormat(Object.class));
    }

    @JsonTest
    public void testDatabindContext_genericAccess() throws Exception {
        DatabindContext context = MAPPER.getRootContext();

        MapperConfig<?> config = context.getConfig();

        TypeFactory typeFactory = context.getTypeFactory();

        AnnotationIntrospector introspector = context.getAnnotationIntrospector();

        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));

        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_SERIALIZERS));

        assertFalse(context.canOverrideAccessModifiers());

        Object activeView = context.getActiveView();
        assertNull(activeView);

        assertEquals(Locale.getDefault(), context.getLocale());

        assertEquals(TimeZone.getDefault(), context.getTimeZone());

        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(Object.class);
        assertEquals(JsonFormat.Defaults.generalObject, defaultPropertyFormat);

        Object attr = context.getAttribute("attrKey");
        assertNull(attr);

        context = context.setAttribute("attrKey", "attrValue");

        attr = context.getAttribute("attrKey");
        assertEquals("attrValue", attr);

        Type javaType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javatype = context.constructType(javaType);
        assertSame(javaType, javatype.getType());

        JavaType baseType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType specializedType = context.constructSpecializedType(baseType, Integer[].class);
        assertEquals(specializedType.getRawClass().getComponentType(), Integer.class);

        JavaType subType = context.resolveSubType(baseType, "java.util.ArrayList");
        assertNotNull(subType);
        assertEquals(JavaType.constructType(ArrayList.class), subType);

        JavaType invalidSubType = context.resolveSubType(baseType, "java.util.HashMap");
        assertNull(invalidSubType);
    }

    @JsonTest
    public void testDatabindContext_errorReporting() throws Exception {
        DatabindContext context = MAPPER.getRootContext();

        try {
            context.reportBadDefinition(null, "msg");
            fail();
        } catch (JsonMappingException e) {
            // expected
            String msg = e.getMessage();
            assertTrue(msg.startsWith("Cannot construct instance"));
            // TODO: test that the rest of the message is correct
        }

        try {
            context.reportBadDefinition(TypeFactory.defaultInstance().constructType(String.class), "msg");
            fail();
        } catch (JsonMappingException e) {
            // expected
            String msg = e.getMessage();
            assertTrue(msg.startsWith("Cannot construct instance"));
            // TODO: test that the rest of the message is correct
        }

        try {
            context.reportBadDefinition(Object.class, "msg");
            fail();
        } catch (JsonMappingException e) {
            // expected
            String msg = e.getMessage();
            assertTrue(msg.startsWith("Cannot construct instance"));
            // TODO: test that the rest of the message is correct
        }

        try {
            context.reportBadDefinition(String.class, "msg");
            fail();
        } catch (JsonMappingException e) {
            // expected, but message should be different from others
            String msg = e.getMessage();
            assertTrue(msg.startsWith("Cannot construct instance of value class"));
            // TODO: test that the rest of the message is correct
        }
    }
}