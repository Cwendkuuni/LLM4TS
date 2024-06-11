package com.fasterxml.jackson.databind.test;

import static com.fasterxml.jackson.databind.util.TestUtil.constructTestType;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.ClassIdResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.None;

import junit.framework.TestCase;

public class DatabindContextTest extends TestCase {

    private static final Type FOO_TYPE = constructTestType(Foo.class, String.class);
    private static final Type BAR_TYPE = constructTestType(Bar.class, Foo.class);
    private static final Type BAZ_TYPE = constructTestType(Baz.class, Foo.class);

    @Test
    public void testGetConfig() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        assertSame(mapper.getConfig(), ctx.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        assertSame(mapper.getAnnotationIntrospector(), ctx.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        assertTrue(ctx.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(ctx.isEnabled(MapperFeature.AUTO_DETECT_CREATORS.negate()));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        assertTrue(ctx.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        assertNull(ctx.getActiveView());
    }

    @Test
    public void testGetLocale() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        assertEquals(Locale.getDefault(), ctx.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        assertEquals(TimeZone.getDefault(), ctx.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        JsonFormat.Value defaultFormat = ctx.getDefaultPropertyFormat(String.class);
        assertNotNull(defaultFormat);
        assertEquals(JsonFormat.Value.DEFAULT, defaultFormat);
    }

    @Test
    public void testGetAttribute() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        assertNull(ctx.getAttribute("foo"));
    }

    @Test
    public void testSetAttribute() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        ctx.setAttribute("foo", "bar");
        assertEquals("bar", ctx.getAttribute("foo"));
    }

    @Test
    public void testConstructType() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        JavaType javaType = ctx.constructType(FOO_TYPE);
        assertNotNull(javaType);
        assertEquals(FOO_TYPE, javaType.getType());
    }

    @Test
    public void testConstructSpecializedType() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        JavaType javaType = ctx.constructSpecializedType(FOO_TYPE, Bar.class);
        assertNotNull(javaType);
        assertEquals(BAR_TYPE, javaType.getType());
    }

    @Test
    public void testResolveSubType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        JavaType javaType = ctx.resolveSubType(FOO_TYPE, "Bar");
        assertNotNull(javaType);
        assertEquals(BAR_TYPE, javaType.getType());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        ctx.invalidTypeIdException(FOO_TYPE, "Baz", "Not a subtype");
    }

    @Test
    public void testGetTypeFactory() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        TypeFactory typeFactory = ctx.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        JsonFactory jsonFactory = new JsonFactory();
        AnnotatedClass annotated = jsonFactory.getParserUtils().constructAnnotatedClass(Foo.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, Foo.class, null, null, null);
        ObjectIdGenerator<?> objectIdGenerator = ctx.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        JsonFactory jsonFactory = new JsonFactory();
        AnnotatedClass annotated = jsonFactory.getParserUtils().constructAnnotatedClass(Foo.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, Foo.class, null, null, null);
        ObjectIdResolver objectIdResolver = ctx.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        AnnotatedClass annotated = mapper.getAnnotationIntrospector().findAnnotation(Foo.class, JsonSubTypes.class);
        Object converterDef = annotated.getAnnotation(JsonSubTypes.With.class).value()[0];
        Converter<Object, Object> converter = ctx.converterInstance(annotated, converterDef);
        assertNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        JavaType javaType = ctx.reportBadDefinition(FOO_TYPE, "Invalid definition");
        assertNull(javaType);
    }

    @Test
    public void testReportBadDefinitionClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        JavaType javaType = ctx.reportBadDefinition(Foo.class, "Invalid definition");
        assertNull(javaType);
    }

    @Test
    public void testFormat() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        String msg = "_format(Hello, {world=foo})";
        String result = ctx._format(msg, new Object(){
            String world = "foo";
        });
        assertEquals("Hello {world=foo}", result);
    }

    @Test
    public void testTruncate() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        String desc = "This is a long description with more than 500 characters.";
        String result = ctx._truncate(desc);
        assertTrue(desc.length() > 500);
        assertTrue(result.length() <= 500);
        assertTrue(result.endsWith("..."));
    }

    @Test
    public void testQuotedString() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        String desc = "This is a string with some \"special\" characters: \"foo\"";
        String result = ctx._quotedString(desc);
        assertEquals("\"This is a string with some \\\"special\\\" characters: \\\"foo\\\"\"", result);
    }

    @Test
    public void testColonConcat() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        String msg = "_colonConcat(Hello, World)";
        String result = ctx._colonConcat(msg, "foo");
        assertEquals("Hello: foo", result);
    }

    @Test
    public void testDesc() {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctx = mapper.getSerializationConfig();
        String desc = "This is a long description with more than 500 characters.";
        String result = ctx._desc(desc);
        assertTrue(desc.length() > 500);
        assertTrue(result.length() <= 500);
        assertTrue(result.endsWith("..."));
    }

    private static class Foo {}

    @JsonSubTypes({
        @JsonSubTypes.With(value = Bar.class, name = "BAR")
    })

    private static class Bar extends Foo {}

    @JsonSubTypes({
        @JsonSubTypes.With(value = Baz.class, name = "BAZ")
    })

    private static class Baz extends Foo {}

    @JsonFormat(with = JsonFormat.Feature.AUTO_DETECT_GETTERS)
    static class CustomClass {
        private String field;

        @JsonValue
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

    @Test
    public void testCustomClassJsonFormatFeature() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapperWithoutFeature = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_GETTERS, true);
        mapperWithoutFeature.configure(MapperFeature.AUTO_DETECT_GETTERS, false);

        ClassIdResolverBuilder idResolverBuilder = mapper.getConfig().getTypeIdResolverBuilder();
        idResolverBuilder.registerIdResolver(CustomClass.class, new TypeIdResolver.Std(CustomClass.class));

        CustomClass customClass = new CustomClass();
        customClass.setField("abc");

        String jsonWithFeature = mapper.writeValueAsString(customClass);
        String jsonWithoutFeature = mapperWithoutFeature.writeValueAsString(customClass);

        assertEquals(jsonWithFeature, jsonWithoutFeature);
    }
}