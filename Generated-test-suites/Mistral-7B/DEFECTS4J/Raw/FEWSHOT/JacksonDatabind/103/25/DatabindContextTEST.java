package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultInitTracker;
import com.fasterxml.jackson.databind.deser.StdDeserializationContext;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeFactory;
import com.fasterxml.jackson.databind.util.TypeUtil;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator.ArrayIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator.ObjectIdGeneratorBase;
import com.fasterxml.jackson.databind.util.ObjectIdResolver;
import com.fasterxml.jackson.databind.util.StdConverter;

import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = mapper.getConfig();
        assertSame(mapper.getConfig(), config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = mapper.getAnnotationIntrospector();
        assertSame(mapper.getAnnotationIntrospector(), annotationIntrospector);
    }

    @Test
    public void testIsEnabled_MapperFeature() {
        MapperConfig<?> config = mapper.getConfig();
        boolean isEnabled = config.isEnabled(MapperFeature.DEFAULT_INTROSPECTION_ANNOTATIONS);
        assertTrue(isEnabled);
    }

    @Test
    public void testIsEnabled_False() {
        MapperConfig<?> config = mapper.getConfig();
        config.disable(MapperFeature.DEFAULT_INTROSPECTION_ANNOTATIONS);
        boolean isEnabled = config.isEnabled(MapperFeature.DEFAULT_INTROSPECTION_ANNOTATIONS);
        assertFalse(isEnabled);
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        MapperConfig<?> config = mapper.getConfig();
        assertTrue(config.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        Class<?> activeView = mapper.getActiveView();
        assertNull(activeView);
    }

    @Test
    public void testGetLocale() {
        Locale locale = mapper.getLocale();
        assertSame(Locale.getDefault(), locale);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = mapper.getTimeZone();
        assertSame(TimeZone.getDefault(), timeZone);
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        MapperConfig<?> config = mapper.getConfig();
        JsonFormat.Value defaultPropertyFormat = config.getDefaultPropertyFormat(Integer.class);
        assertNotNull(defaultPropertyFormat);
        assertEquals(JsonFormat.Value.standardSerialFormat(), defaultPropertyFormat);
    }

    @Test
    public void testGetAttribute() {
        Object attribute = mapper.getAttribute(StandardSerializer.NullSerializer.class);
        assertNull(attribute);
    }

    @Test
    public void testSetAttribute() {
        mapper.setAttribute(MapperFeature.DEFAULT_INTROSPECTION_ANNOTATIONS, Boolean.TRUE);
        Object attribute = mapper.getAttribute(MapperFeature.DEFAULT_INTROSPECTION_ANNOTATIONS);
        assertTrue((Boolean) attribute);
    }

    @Test
    public void testConstructType() {
        Type type = Integer[].class;
        JavaType javaType = mapper.getConfig().getTypeFactory().constructType(type);
        assertNotNull(javaType);
        assertEquals(Integer[].class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = mapper.getConfig().getTypeFactory().constructType(Integer[].class);
        Class<?> subclass = String[].class;
        JavaType javaType = mapper.getConfig().constructSpecializedType(baseType, subclass);
        assertNotNull(javaType);
        assertEquals(String[].class, javaType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = mapper.getConfig().getTypeFactory().constructType(String.class);
        String subClass = "java.util.ArrayList";
        JavaType javaType = mapper.resolveSubType(baseType, subClass);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = mapper.getConfig().getTypeFactory().constructType(String.class);
        String typeId = "java.util.HashMap";
        mapper.invalidTypeIdException(baseType, typeId, null);
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = mapper.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        JavaType javaType = mapper.getConfig().getTypeFactory().constructType(String.class);
        Annotated annotated = new AnnotatedClass(javaType);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.CLASS, javaType, null);
        ObjectIdGenerator<?> objectIdGenerator = mapper.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
        assertTrue(objectIdGenerator instanceof ArrayIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        JavaType javaType = mapper.getConfig().getTypeFactory().constructType(String.class);
        Annotated annotated = new AnnotatedClass(javaType);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(Id.PROPERTY, "name", javaType);
        ObjectIdResolver objectIdResolver = mapper.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
        assertTrue(objectIdResolver instanceof ObjectIdGeneratorBase);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        JavaType javaType = mapper.getConfig().getTypeFactory().constructType(String.class);
        Annotated annotated = new AnnotatedClass(javaType);
        Object converterDef = StringToIntegerConverter.class;
        Converter<Object, Object> converter = mapper.converterInstance(annotated, converterDef);
        assertNotNull(converter);
        assertTrue(converter instanceof StdConverter.StdConverterDeserializer);
    }

    @Test
    public void testReportBadDefinition() throws JsonProcessingException {
        JavaType javaType = mapper.getConfig().getTypeFactory().constructType(String[].class);
        String msg = "Type definition error: ";
        assertThrows(JsonMappingException.class, () -> mapper.reportBadDefinition(javaType, msg));
    }

    @Test
    public void testReportBadDefinition_Class() throws JsonProcessingException {
        Class<?> clazz = String[].class;
        String msg = "Type definition error: ";
        assertThrows(JsonMappingException.class, () -> mapper.reportBadDefinition(clazz, msg));
    }
}

class AnnotatedClass implements Annotated {
    private final JavaType _type;

    AnnotatedClass(JavaType type) {
        _type = type;
    }

    @Override
    public JavaType getType() {
        return _type;
    }

    @Override
    public ObjectIdInfo getAnnotationId() {
        return null;
    }

    @Override
    public MapperConfig<?> getConfig() {
        return null;
    }

    @Override
    public TypeIdResolver getTypeResolver() {
        return null;
    }

    @Override
    public DefaultInitTracker getInitTracker() {
        return null;
    }

    @Override
    public AnnotationIntrospector getAnnotationIntrospector() {
        return null;
    }

    @Override
    public Object getAnnotation(Class<?> cls) {
        return null;
    }

    @Override
    public Object getAnnotation(Class<?> cls1, Class<?> aClass) {
        return null;
    }

    @Override
    public boolean hasAnnotation(Class<?> cls) {
        return false;
    }

    @Override
    public JsonMappingException processAnnotation(Class<?> cls) throws JsonMappingException {
        return null;
    }
}

class StringToIntegerConverter extends StdConverter<String, Integer> {
    @JsonDeserialize
    static class StringDeserializer extends StdDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getValueAsString();
            return Integer.parseInt(text);
        }
    }

    @JsonSerialize
    static class IntegerSerializer extends StdSerializer<Integer> {
        @Override
        public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value);
        }
    }
}