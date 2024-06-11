package com.fasterxml.jackson.databind.test;

import static com.fasterxml.jackson.databind.util.TestUtil.constructType;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
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
        DatabindContext context = Mockito.spy(DatabindContext.class);
        assertNotNull(context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        assertNotNull(context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS.name()));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        assertTrue(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        assertEquals(JsonFormat.Value.STANDARD, context.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testgetAttribute() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        Object key = new Object();
        assertNull(context.getAttribute(key));
    }

    @Test
    public void testSetAttribute() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        Object key = new Object();
        Object value = new Object();
        assertEquals(context, context.setAttribute(key, value));
    }

    @Test
    public void testConstructType() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        Type type = new TypeToken<String>() {}.getType();
        JavaType javaType = context.constructType(type);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        JavaType baseType = constructType(String.class);
        Class<?> subclass = String[].class;
        JavaType javaType = context.constructSpecializedType(baseType, subclass);
        assertEquals(subclass, javaType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeWithInvalidTypeId() throws JsonMappingException {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        JavaType baseType = constructType(String.class);
        String typeId = "InvalidType";
        context.resolveSubType(baseType, typeId);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeWithUnresolvableTypeId() throws JsonMappingException {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        JavaType baseType = constructType(String.class);
        String typeId = "MyCustomCollection";
        context.resolveSubType(baseType, typeId);
    }

    @Test
    public void testInvalidTypeIdException() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        JavaType baseType = constructType(String.class);
        String typeId = "MyCustomCollection";
        JsonMappingException expected = context.<JsonMappingException>reportBadDefinition(baseType, "Invalid type: " + typeId);
        assertEquals("Invalid type: MyCustomCollection", expected.getMessage());
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        Annotated annotated = Mockito.mock(Annotated.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        Annotated annotated = Mockito.mock(Annotated.class);
        Object converterDef = new JacksonAnnotationIntrospector.ValueConverterAnnotation();
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionWithInvalidType() throws JsonMappingException {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        JavaType type = null;
        String msg = "Invalid type";
        context.reportBadDefinition(type, msg);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionWithInvalidClass() throws JsonMappingException {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        String msg = "Invalid class";
        context.reportBadDefinition(String.class, msg);
    }

    @Test
    public void testFormat() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        String format = "{0}: {1}";
        String msg = context._format(format, "message", "description");
        assertEquals("message: description", msg);
    }

    @Test
    public void testTruncate() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        String desc = "this is a very long description that needs to be truncated";
        String truncated = context._truncate(desc);
        assertTrue(truncated.endsWith("]..."));
    }

    @Test
    public void testQuotedString() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        String desc = "this is a description with quotes \"in it\"";
        String quoted = context._quotedString(desc);
        assertTrue(quoted.startsWith("\"this is a description with quotes \""));
        assertTrue(quoted.endsWith("\""));
    }

    @Test
    public void testColonConcat() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        String msgBase = "Base message";
        String extra = "Extra information";
        String concat = context._colonConcat(msgBase, extra);
        assertEquals(msgBase + ": " + extra, concat);
    }

    @Test
    public void testDesc() {
        DatabindContext context = Mockito.spy(DatabindContext.class);
        String desc = "this is a description";
        String result = context._desc(desc);
        assertEquals("[this is a description]", result);
    }
}