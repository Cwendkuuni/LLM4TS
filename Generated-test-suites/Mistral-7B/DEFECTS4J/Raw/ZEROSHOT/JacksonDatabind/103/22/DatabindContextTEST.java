package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

public class DatabindContextTest {

    @org.junit.Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        this.databindContext = mapper.getDeserializationConfig().getContext();
        this.objectMapper = mapper;
    }

    private DatabindContext databindContext;
    private ObjectMapper objectMapper;

    @org.junit.Test
    public void testGetConfig() {
        MapperConfig<?> config = databindContext.getConfig();
        assertNotNull(config);
    }

    @org.junit.Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = databindContext.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);
    }

    @org.junit.Test
    public void testIsEnabled() {
        assertTrue(databindContext.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        assertFalse(databindContext.isEnabled(MapperFeature.CAN_OVERLOAD_ACCESS_MODIFIERS));

        MapperConfig<ObjectMapper> config = new MapperFactoryBuilder().disable(MapperFeature.AUTO_DETECT_TYPES).build();
        DatabindContext context = config.getContext();
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
    }

    @org.junit.Test
    public void testCanOverrideAccessModifiers() {
        assertTrue(databindContext.canOverrideAccessModifiers());
        MapperConfig<ObjectMapper> config = new MapperFactoryBuilder().build();
        DatabindContext context = config.getContext();
        assertFalse(context.canOverrideAccessModifiers());
    }

    @org.junit.Test
    public void testGetActiveView() {
        // Abstract class, can't test directly
    }

    @org.junit.Test
    public void testGetLocale() {
        Locale locale = databindContext.getLocale();
        assertEquals("en_US", locale.toString());
    }

    @org.junit.Test
    public void testGetTimeZone() {
        TimeZone timeZone = databindContext.getTimeZone();
        assertEquals("America/New_York", timeZone.getID());
    }

    @org.junit.Test
    public void testGetDefaultPropertyFormat() {
        JavaType javaType = databindContext.getTypeFactory().constructType(String.class);
        JsonFormat.Value format = databindContext.getDefaultPropertyFormat(javaType);
        assertEquals(JsonFormat.Value.DATE_TIME, format);
    }

    @org.junit.Test
    public void testGetAttribute() {
        Object attribute = databindContext.getAttribute(JsonInclude.Include.ALWAYS);
        assertNotNull(attribute);
        assertTrue(attribute instanceof JsonInclude.Include);
    }

    @org.junit.Test
    public void testSetAttribute() {
        databindContext.setAttribute(JsonInclude.Include.ALWAYS, null);
        Object attribute = databindContext.getAttribute(JsonInclude.Include.ALWAYS);
        assertNotNull(attribute);
        assertTrue(attribute instanceof JsonInclude.Include);

        databindContext.setAttribute(JsonInclude.Include.ALWAYS, new Object());
        Object attribute2 = databindContext.getAttribute(JsonInclude.Include.ALWAYS);
        assertNotNull(attribute2);
        assertNotEquals(attribute, attribute2);
    }

    @org.junit.Test
    public void testConstructType() throws IOException {
        Type type = new TypeToken<String>() {}.getType();
        JavaType javaType = databindContext.constructType(type);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @org.junit.Test
    public void testConstructSpecializedType() throws IOException {
        JavaType baseType = databindContext.getTypeFactory().constructType(String.class);
        Class<String> subclass = String.class;
        JavaType javaType = databindContext.constructSpecializedType(baseType, subclass);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @org.junit.Test
    public void testResolveSubType() throws IOException {
        JavaType baseType = databindContext.getTypeFactory().constructType(String.class);
        String subClass = "java.lang.String";
        JavaType javaType = databindContext.resolveSubType(baseType, subClass);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());

        JavaType javaType2 = databindContext.resolveSubType(baseType, "java.lang.Object");
        assertNull(javaType2);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws IOException {
        JavaType baseType = databindContext.getTypeFactory().constructType(String.class);
        String typeId = "java.lang.Integer";
        databindContext.invalidTypeIdException(baseType, typeId, null);
    }

    @org.junit.Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = databindContext.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @org.junit.Test
    public void testObjectIdGeneratorInstance() throws IOException {
        ObjectReader reader = objectMapper.readerForClass(MyClass.class);
        MyClass annotated = new MyClass();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(reader.getConfig(), annotated);
        ObjectIdGenerator<?> objectIdGenerator = databindContext.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @org.junit.Test
    public void testObjectIdResolverInstance() throws IOException {
        ObjectReader reader = objectMapper.readerForClass(MyClass.class);
        MyClass annotated = new MyClass();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(reader.getConfig(), annotated);
        ObjectIdResolver objectIdResolver = databindContext.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @org.junit.Test
    public void testConverterInstance() throws IOException {
        ObjectReader reader = objectMapper.readerForClass(MyClass.class);
        MyClass annotated = new MyClass();
        Object converterDef = MyConverter.class;
        Converter<Object, Object> converter = databindContext.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @org.junit.Test
    public void testReportBadDefinition() throws IOException {
        JavaType javaType = databindContext.getTypeFactory().constructType(new TypeToken<MyClass>() {}.getType());
        String msg = "bad definition";
        MyClass obj = databindContext.reportBadDefinition(javaType, msg);
        fail("Expected JsonMappingException to be thrown");
    }

    @org.junit.Test
    public void testReportBadDefinitionString() throws IOException {
        Class<MyClass> type = MyClass.class;
        String msg = "bad definition";
        MyClass obj = (MyClass) databindContext.reportBadDefinition(type, msg);
        fail("Expected JsonMappingException to be thrown");
    }

    static class MyClass {
        @JsonProperty("name")
        private String name;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private int id;
    }

    static class MyConverter {
        @JsonAdapter(MyJsonAdapter.class)
        @JsonProperty("converter")
        public static Converter<Object, Object> converter() {
            return null;
        }
    }

    static class MyJsonAdapter extends StdSerializer<MyClass> {
        public MyJsonAdapter() {
            this(null);
        }

        public MyJsonAdapter(Class<MyClass> t) {
            this(null);
        }

        public MyJsonAdapter(Class<MyClass> t, JsonSerializer.ValueInclusion incl) {
            this();
        }

        public MyJsonAdapter(Class<MyClass> t, JsonSerializer.NullValueProvider nullProvider) {
            this();
        }

        public MyJsonAdapter(Class<MyClass> t, JsonSerializer.WithDefaults provider) {
            this();
        }

        public MyJsonAdapter(Class<MyClass> t, JsonSerializer.UnwrappedTreeCreator unwrappedTreeCreator) {
            this();
        }

        public MyJsonAdapter(Class<MyClass> t, JsonSerializerFactory serFactory) {
            this();
        }

        public MyJsonAdapter(Class<MyClass> t, ObjectIdGenerator idGen) {
            this();
        }

        public MyJsonAdapter(Class<MyClass> t, ObjectIdResolver idResolver) {
            this();
        }

        @Override
        public void serialize(MyClass src, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
        }

        @Override
        public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}