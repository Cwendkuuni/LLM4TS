package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class DatabindContextTest {

    private DatabindContext context;

    @Before
    public void setUp() {
        context = Mockito.spy(new BaseDatabindContext());
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector introspector = context.getAnnotationIntrospector();
        assertNotNull(introspector);
    }

    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS.getId())); // test id
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
        Locale locale = context.getLocale();
        assertNull(locale);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = context.getTimeZone();
        assertNull(timeZone);
    }

    @Test
    public void testgetDefaultPropertyFormat() {
        JsonFormat.Value format = context.getDefaultPropertyFormat(String.class);
        assertNull(format);
    }

    @Test
    public void testGetAttribute() {
        Object attribute = context.getAttribute("testAttribute");
        assertNull(attribute);
    }

    @Test
    public void testSetAttribute() {
        context.setAttribute("testAttribute", "testValue");
        Object attribute = context.getAttribute("testAttribute");
        assertEquals("testValue", attribute);
    }

    @Test
    public void testConstructType() {
        Type type = Integer.TYPE;
        JavaType javaType = context.constructType(type);
        assertEquals(Integer.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.constructType(Integer.class);
        JavaType specializedType = context.constructSpecializedType(baseType, Long.class);
        assertEquals(Long.class, specializedType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeFailure() throws JsonMappingException {
        JavaType baseType = context.constructType(String.class);
        context.resolveSubType(baseType, "java.lang.Integer");
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = context.constructType(String.class);
        String msg = "Test message";
        assertEquals(_format("Invalid definition: %s", msg), context.reportBadDefinition(type, msg).getMessage());
    }
}

class BaseDatabindContext extends DatabindContext {
    @Override
    protected JsonMappingException invalidTypeIdException(JavaType baseType, String typeId, String extraDesc) {
        throw new JsonMappingException(_format("Invalid type id: %s for type %s: %s", typeId, _desc(baseType.getRawClass()), extraDesc));
    }

    @Override
    public TypeFactory getTypeFactory() {
        return new TypeFactory();
    }
}