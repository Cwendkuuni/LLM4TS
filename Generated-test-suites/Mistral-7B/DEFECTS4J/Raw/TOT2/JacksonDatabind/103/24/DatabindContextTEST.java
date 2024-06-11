import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DatabindContextTest {

    private DatabindContext context;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        context = new BaseDatabindContext();
        mapper = new ObjectMapper();
        mapper.setConfig(context);
    }

    @Test
    public void testGetConfig() {
        MapperConfig<?> config = context.getConfig();
        assertSame(mapper.getConfig(), config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector introspector = context.getAnnotationIntrospector();
        assertSame(mapper.getSerializationConfig().getDefaultAnnotationIntrospector(), introspector);
    }

    @Test
    public void testIsEnabled_MapProgrammatically() {
        context.setConfig(mapper.getConfig());
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        assertFalse(context.isEnabled(MapperFeature.USE_STATIC_TYPES));
    }

    @Test
    public void testIsEnabled_MapFromAnnotation() {
        context.setConfig(mapper.getConfig());
        context.getConfig().disable(MapperFeature.AUTO_DETECT_TYPES);
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        context.getConfig().enable(MapperFeature.AUTO_DETECT_TYPES);
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
    }

    @Test(expected = JsonMappingException.class)
    public void testIsEnabled_InvalidFeature() {
        context.setConfig(mapper.getConfig());
        context.isEnabled(new EnumValue(MapperFeature.class, "INVALID_FEATURE"));
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
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() throws JsonProcessingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JsonFormat.Value format = context.getDefaultPropertyFormat(baseType);
        assertSame(JsonFormat.DateFormat.YMD_HMS_Z, format.getFormat());
    }

    @Test
    public void testGetAttribute() {
        context.setAttribute("testKey", "testValue");
        assertEquals("testValue", context.getAttribute("testKey"));
    }

    @Test
    public void testSetAttribute() {
        context.setAttribute("testKey", "testValue");
        assertEquals("testValue", context.getAttribute("testKey"));
        context.setAttribute("testKey", null);
        assertNull(context.getAttribute("testKey"));
    }

    @Test
    public void testConstructType() {
        JavaType javaType = context.constructType(String.class);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.constructSpecializedType(baseType, String[].class);
        assertEquals(String[].class, subType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType_InvalidTypeId() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.resolveSubType(baseType, "InvalidTypeId");
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType_NotASubtype() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.resolveSubType(baseType, Integer.class.getName());
    }

    @Test
    public void testResolveSubType_ResolveClass() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, Integer.class.getName());
        assertEquals(Integer.class, subType.getRawClass());
    }

    @Test
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "InvalidTypeId";
        String msg = context.invalidTypeIdException(baseType, typeId, "Invalid TypeId");
        assertTrue(msg.contains("Not a subtype"));
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertSame(mapper.getTypeFactory(), typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = createMockAnnotated(null);
        ObjectIdInfo objectIdInfo = createMockObjectIdInfo();
        ObjectIdGenerator<?> gen = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNull(gen);
    }

    @Test
    public void testObjectIdGeneratorInstance_NullGeneratorType() throws JsonMappingException {
        Annotated annotated = createMockAnnotated(null);
        ObjectIdInfo objectIdInfo = createMockObjectIdInfo();
        ObjectIdGenerator<?> gen = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNull(gen);
    }

    @Test
    public void testObjectIdGeneratorInstance_DefaultGenerator() throws JsonMappingException {
        Annotated annotated = createMockAnnotated(ObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo = createMockObjectIdInfo();
        ObjectIdGenerator<?> gen = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(gen);
    }

    @Test
    public void testObjectIdGeneratorInstance_CustomGenerator() throws JsonMappingException {
        Annotated annotated = createMockAnnotated(MyObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo = createMockObjectIdInfo();
        ObjectIdGenerator<?> gen = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(gen);
        assertEquals(MyObjectIdGenerator.class, gen.getClass());
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonMappingException {
        Annotated annotated = createMockAnnotated(null);
        ObjectIdInfo objectIdInfo = createMockObjectIdInfo();
        ObjectIdResolver resolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNull(resolver);
    }

    @Test
    public void testObjectIdResolverInstance_NullResolverType() throws JsonMappingException {
        Annotated annotated = createMockAnnotated(null);
        ObjectIdInfo objectIdInfo = createMockObjectIdInfo();
        ObjectIdResolver resolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNull(resolver);
    }

    @Test
    public void testObjectIdResolverInstance_DefaultResolver() throws JsonMappingException {
        Annotated annotated = createMockAnnotated(ObjectIdResolver.class);
        ObjectIdInfo objectIdInfo = createMockObjectIdInfo();
        ObjectIdResolver resolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(resolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = createMockAnnotated(MyConverter.class);
        Object converterDef = MyConverter.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
        assertEquals(MyConverter.class, converter.getClass());
    }

    @Test
    public void testConverterInstance_NullConverterType() throws JsonMappingException {
        Annotated annotated = createMockAnnotated(null);
        Object converterDef = MyConverter.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        String msg = context.reportBadDefinition(javaType, "Bad Definition");
        assertEquals("Bad Definition", msg);
    }

    @Test
    public void testReportBadDefinition_Class() throws JsonMappingException {
        Class<?> javaType = String.class;
        String msg = context.reportBadDefinition(javaType, "Bad Definition");
        assertEquals("Bad Definition", msg);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition_InvalidType() throws JsonMappingException {
        JavaType javaType = context.getTypeFactory().constructType("InvalidType");
        context.reportBadDefinition(javaType, "Bad Definition");
    }

    private ObjectIdInfo createMockObjectIdInfo() {
        // todo: create mock ObjectIdInfo
        return null;
    }

    private Annotated createMockAnnotated(Class<?> clazz) {
        // todo: create mock Annotated
        return null;
    }

    private static class MyObjectIdGenerator implements ObjectIdGenerator<String> {
        @Override
        public String generateId(SerializerProvider serializers, Type idType) throws JsonProcessingException {
            return "";
        }
    }

    private static class MyConverter implements Converter<Object, Object> {
        @Override
        public Object convert(Object value, DeserializationContext ctxt) throws JsonMappingException, IOException {
            return null;
        }

        @Override
        public Object convertAndStore(ObjectValue source, DeserializationContext ctxt) throws JsonMappingException, IOException {
            return null;
        }

        @Override
        public void reset(DeserializationContext ctxt) throws JsonMappingException, IOException {

        }
    }

    private static enum MapperFeature {
        AUTO_DETECT_TYPES,
        USE_STATIC_TYPES,
        AUTO_CONVERT_NULLS,
        CAN_OVERLOAD_ACCESS_MODIFIERS,
        // ...
        }

    private static class EnumValue {
        private final Class<?> clazz;
        private final String value;

        EnumValue(Class<?> clazz, String value) {
            this.clazz = clazz;
            this.value = value;
        }

        public Class<?> getGeneratorType() {
            return clazz;
        }

        public String getName() {
            return value;
        }
    }
}