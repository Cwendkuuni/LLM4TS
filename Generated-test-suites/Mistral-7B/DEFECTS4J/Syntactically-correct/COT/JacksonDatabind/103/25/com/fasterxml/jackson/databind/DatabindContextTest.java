package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdResolver;
import com.fasterxml.jackson.databind.util.StdConverter;

import com.fasterxml.jackson.databind.test.BaseTest;
import com.fasterxml.jackson.databind.test.MockMapperConfig;

public class DatabindContextTest extends BaseTest {

    @Test
    public void testGetConfig() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        MapperConfig<?> config = ctx.getConfig();
        assertNotNull(config);
        assertEquals(MapperConfig.StandardConfig.class, config.getClass());
    }

    @Test
    public void testGetAnnotationIntrospector() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        AnnotationIntrospector ai = ctx.getAnnotationIntrospector();
        assertNotNull(ai);
    }

    @Test
    public void testIsEnabled() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        assertTrue(ctx.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        assertTrue(ctx.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
        assertTrue(ctx.isEnabled(MapperFeature.CAN_Empty_BEANS_AS_NULL));
        assertFalse(ctx.isEnabled(MapperFeature.FAIL_ON_EMPTY_BEANS));
    }

    @Test
    public void testCanOverrideAccessModifiers() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        assertTrue(ctx.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        assertNull(ctx.getActiveView());
    }

    @Test
    public void testGetLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        assertNull(ctx.getLocale());
    }

    @Test
    public void testGetTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        assertNull(ctx.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        ClassType cls = mapper.getTypeFactory().constructType(Integer.class);
        JsonFormat.Value value = ctx.getDefaultPropertyFormat(cls);
        assertNotNull(value);
        assertEquals(JsonFormat.Value.STANDARD, value);
    }

    @Test
    public void testGetAttribute() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        Object attr = ctx.getAttribute(Include.ALWAYS);
        assertNotNull(attr);
        assertEquals(Include.ALWAYS, attr);
    }

    @Test
    public void testSetAttribute() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        ctx.setAttribute(MapperFeature.AUTO_DETECT_TYPES, Boolean.FALSE);
        assertFalse(ctx.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
    }

    @Test
    public void testConstructType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        Type type = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType javaType = ctx.constructType(type);
        assertNotNull(javaType);
        assertEquals(SimpleType.construct(Integer.class), javaType);
    }

    @Test
    public void testConstructSpecializedType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        JavaType baseType = mapper.getTypeFactory().constructType(Integer.class);
        ClassType subClass = mapper.getTypeFactory().constructType(Long.class);
        JavaType javaType = ctx.constructSpecializedType(baseType, subClass.getRawClass());
        assertNotNull(javaType);
        assertEquals(subClass, javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeInvalid() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        JavaType baseType = mapper.getTypeFactory().constructType(Integer.class);
        String typeId = "java.lang.String";
        ctx.resolveSubType(baseType, typeId);
    }

    @Test
    public void testResolveSubTypeValid() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        JavaType baseType = mapper.getTypeFactory().constructType(Integer.class);
        String typeId = "java.lang.Integer";
        JavaType javaType = ctx.resolveSubType(baseType, typeId);
        assertNotNull(javaType);
        assertEquals(ClassType.construct(Integer.class), javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdExceptionType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        JavaType baseType = mapper.getTypeFactory().constructType(Integer.class);
        String typeId = "java.lang.Object";
        ctx.invalidTypeIdException(baseType, typeId, "Invalid TypeId");
    }

    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        AnnotatedClass ac = mapper.getAnnotationIntrospector().findBeanProperties(Object.class, null);
        ObjectIdGenerator<?> objectIdGenerator = ctx.objectIdGeneratorInstance(ac.forProperty("id"), null);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        AnnotatedClass ac = mapper.getAnnotationIntrospector().findBeanProperties(Object.class, null);
        ObjectIdResolver objectIdResolver = ctx.objectIdResolverInstance(ac.forProperty("id"), null);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        AnnotatedClass ac = mapper.getAnnotationIntrospector().findBeanProperties(Object.class, null);
        Converter<String,Integer> converter = ctx.converterInstance(ac.forProperty("value"), StdConverter.instance(String.class, Integer.class));
        assertNotNull(converter);
        assertEquals(StdConverter.class, converter.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionNullType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        ctx.reportBadDefinition(null, "Test");
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionNullMessage() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        JavaType type = mapper.getTypeFactory().constructType(Integer.class);
        ctx.reportBadDefinition(type, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionInvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        JavaType type = mapper.getTypeFactory().constructType("invalidType");
        ctx.reportBadDefinition(type, "Test");
    }

    @Test
    public void testFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        String msg = "{0}: {1}";
        String fmt = ctx._format(msg, "test", "result");
        assertEquals("test: result", fmt);
    }

    @Test
    public void testTruncate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        String desc = "This is a very long description with more than 500 characters.";
        String trunc = ctx._truncate(desc);
        assertEquals("This is a very long description with more than 500 characters.", trunc);
        assertEquals("This is a very long description...", trunc.substring(0, trunc.length()-3));
        assertEquals("500", String.valueOf(trunc.length()-desc.length()+desc.substring(desc.length()-500).length()));
    }

    @Test
    public void testQuotedString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        String desc = "This is a description with a control character: \b and a linefeed: \n";
        String quoted = ctx._quotedString(desc);
        assertEquals("\"" + desc.replaceAll("(\\.)", "\\\\$1") + "\"", quoted);
    }

    @Test
    public void testColonConcat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        String msgBase = "Base message: ";
        String extra = "Extra message";
        String fmt = ctx._colonConcat(msgBase, extra);
        assertEquals(msgBase + ": " + extra, fmt);
    }

    @Test
    public void testDesc() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        String desc = "This is a description.";
        String fmt = ctx._desc(desc);
        assertEquals(desc, fmt);
    }

    @Test
    public void testCreateMapperConfig() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = new MockMapperConfig();
        DatabindContext ctx = mapper.getSerializationConfig().getContext();
        assertEquals(config, ctx.getConfig());
    }
}