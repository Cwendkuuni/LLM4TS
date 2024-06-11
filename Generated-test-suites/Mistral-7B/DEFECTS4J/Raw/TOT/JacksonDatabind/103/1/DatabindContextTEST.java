package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.StaticSubType;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.type.JavaType;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

public class DatabindContextTest {

    protected ObjectMapper mapper;
    protected DatabindContext ctx;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        ctx = mapper.getDeserializationContext();
    }

    /**
     * Test for getConfig() method.
     *
     * @throws IOException
     */
    @Test
    public void testGetConfig() throws IOException {
        MapperConfig<?> config = ctx.getConfig();
        assertNotNull(config);
        assertNotNull(config.getAnnotationIntrospector());
    }

    /**
     * Test for isEnabled() method.
     */
    @Test
    public void testIsEnabled() {
        assertTrue(ctx.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS));
        assertFalse(ctx.isEnabled(MapperFeature.USE_STATIC_TYPES));

        MapperConfig<?> config = new MapperConfig.Builder()
                .disable(MapperFeature.USE_STATIC_TYPES)
                .build();
        ctx = new BaseDatabindContext(config);
        assertFalse(ctx.isEnabled(MapperFeature.USE_STATIC_TYPES));
    }

    /**
     * Test for getActiveView() method.
     */
    @Test
    public void testGetActiveView() {
        // Since DatabindContext is abstract, we cannot create an instance
        // of it directly, but we can test it with an instance of a subclass
        SerializerProvider provider = mapper.getSerializerProvider();
        DatabindContext context = provider.getDeserializationContext();

        assertNull(context.getActiveView());
    }

    /**
     * Test for getLocale() method.
     */
    @Test
    public void testGetLocale() {
        assertEquals(Locale.getDefault(), ctx.getLocale());
    }

    /**
     * Test for getTimeZone() method.
     */
    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getDefault(), ctx.getTimeZone());
    }

    /**
     * Test for getDefaultPropertyFormat() method.
     */
    @Test
    public void testGetDefaultPropertyFormat() {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonFormat.Value defaultPropertyFormat = ctx.getDefaultPropertyFormat(javaType);
        assertNotNull(defaultPropertyFormat);
    }

    /**
     * Test for getAttribute() method.
     */
    @Test
    public void testGetAttribute() {
        ctx.setAttribute("test", "value");
        Object attribute = ctx.getAttribute("test");
        assertEquals("value", attribute);
    }

    /**
     * Test for constructType() method.
     */
    @Test
    public void testConstructType() {
        Type type = MapperTest.TestClassWithGenerics.class;
        JavaType javaType = ctx.constructType(type);
        assertNotNull(javaType);
        assertEquals(MapperTest.TestClassWithGenerics.class, javaType.getRawClass());
    }

    /**
     * Test for constructSpecializedType() method.
     */
    @Test
    public void testConstructSpecializedType() {
        JavaType javaType = ctx.getTypeFactory().constructType(MapperTest.TestClassWithSubtypes.class);
        JavaType specializedType = ctx.constructSpecializedType(javaType, MapperTest.TestSubclass1.class);
        assertNotNull(specializedType);
        assertEquals(MapperTest.TestSubclass1.class, specializedType.getRawClass());
    }

    /**
     * Test for resolveSubType() method.
     */
    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = ctx.getTypeFactory().constructType(MapperTest.TestClassWithSubtypes.class);
        JavaType subType = ctx.resolveSubType(baseType, "MapperTest.TestSubclass1");
        assertNotNull(subType);
        assertEquals(MapperTest.TestSubclass1.class, subType.getRawClass());
    }

    /**
     * Test for invalidTypeIdException() method.
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = ctx.getTypeFactory().constructType(MapperTest.TestClassWithSubtypes.class);
        ctx.invalidTypeIdException(baseType, "MapperTest.TestSubclass3");
    }

    /**
     * Test for getTypeFactory() method.
     */
    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = ctx.getTypeFactory();
        assertNotNull(typeFactory);
    }

    /**
     * Test for objectIdGeneratorInstance() method.
     */
    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(MapperTest.TestClassWithId.class);
        ObjectIdInfo objectIdInfo = annotated.findAnnotation(JsonSubTypes.class);
        ObjectIdGenerator<?> generator = ctx.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(generator);
    }

    /**
     * Test for objectIdResolverInstance() method.
     */
    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = new AnnotatedClass(MapperTest.TestClassWithId.class);
        ObjectIdInfo objectIdInfo = annotated.findAnnotation(JsonSubTypes.class);
        ObjectIdResolver resolver = ctx.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(resolver);
    }

    /**
     * Test for converterInstance() method.
     */
    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = new AnnotatedClass(MapperTest.TestClassWithConverter.class);
        Object converterDef = annotated.findAnnotation(JsonConverter.class);
        Converter<Object, Object> converter = ctx.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    /**
     * Test for reportBadDefinition() method.
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType javaType = ctx.getTypeFactory().constructType(String[].class);
        ctx.reportBadDefinition(javaType, "String array type cannot be instantiated directly");
    }
}