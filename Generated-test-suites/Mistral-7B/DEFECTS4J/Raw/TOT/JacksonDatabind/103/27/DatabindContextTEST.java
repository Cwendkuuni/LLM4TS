package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.Locale;

public class DatabindContextTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = JsonMapper.builder()
                .findAndRegisterModules()
                .build();
    }

    @Test
    public void testGetConfig() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);

        // test getting a simple feature
        assertTrue(config.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));

        // test getting a complex feature
        Class<?> customAnnotationClass = CustomMapperConfig.class;
        Object customAnnotationInstance = new CustomMapperConfig();
        config = new MapperFactory(new AnnotationIntrospector() {})
                .builder()
                .annotationIntrospector(new StubAnnotationIntrospector(customAnnotationClass, customAnnotationInstance))
                .build();
        config = context.getConfig();
        assertSame(customAnnotationInstance, config.getAnnotationIntrospector());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();

        // test introspection of a simple class with no annotations
        JavaType javaType = objectMapper.getTypeFactory().constructType(SimpleClass.class);
        Annotated annotated = AnnotatedClass.construct(javaType, null, null);
        assertNull(annotationIntrospector.findAnnotationInstance(annotated));

        // test introspection of a class with an annotation
        javaType = objectMapper.getTypeFactory().constructType(AnnotatedClass.class);
        annotated = AnnotatedClass.construct(javaType, null, null);
        assertNotNull(annotationIntrospector.findAnnotationInstance(annotated));
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();

        // test enabling/disabling a simple feature
        context = context.withFeature(MapperFeature.AUTO_DETECT_CREATORS, false);
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        context = context.withFeature(MapperFeature.AUTO_DETECT_CREATORS, true);
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));

        // test enabling/disabling a complex feature
        context = context.withAnnotationIntrospector(new CustomAnnotationIntrospector());
        assertNotNull(context.getAnnotationIntrospector());
        context = context.withAnnotationIntrospector(null);
        assertNull(context.getAnnotationIntrospector());
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();

        // test getting a simple value
        assertFalse(context.canOverrideAccessModifiers());

        // test setting and getting a value
        context = context.withOverrideAccessModifiers(true);
        assertTrue(context.canOverrideAccessModifiers());
        context = context.withOverrideAccessModifiers(false);
        assertFalse(context.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        assertNull(context.getActiveView());

        Class<?> viewClass = SimpleView.class;
        context = context.withActiveView(viewClass);
        assertEquals(viewClass, context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();

        // test getting a simple value
        assertEquals(Locale.getDefault(), context.getLocale());

        // test setting and getting a value
        Locale locale = Locale.US;
        context = context.withLocale(locale);
        assertEquals(locale, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();

        // test getting a simple value
        assertEquals(TimeZone.getDefault(), context.getTimeZone());

        // test setting and getting a value
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        context = context.withTimeZone(timeZone);
        assertEquals(timeZone, context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();

        // test getting a simple value
        JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(String.class);
        assertNotNull(defaultPropertyFormat);

        // test setting and getting a value
        JsonFormat.Value customDefaultPropertyFormat = new JsonFormat.Value();
        context = context.withDefaultPropertyFormat(String.class, customDefaultPropertyFormat);
        assertEquals(customDefaultPropertyFormat, context.getDefaultPropertyFormat(String.class));
    }

    @Test
    public void testGetAttribute() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();

        // test getting a simple value
        Object attribute = context.getAttribute("testKey");
        assertNull(attribute);

        // test setting and getting a value
        Object attributeValue = "testValue";
        context = context.setAttribute("testKey", attributeValue);
        assertEquals(attributeValue, context.getAttribute("testKey"));
    }

    @Test
    public void testSetAttribute() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();

        // test setting and getting a value
        Object attributeValue = "testValue";
        context = context.setAttribute("testKey", attributeValue);
        assertEquals(attributeValue, context.getAttribute("testKey"));

        // test removing a value
        context = context.setAttribute("testKey", null);
        assertNull(context.getAttribute("testKey"));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        JavaType baseType = context.getTypeFactory().constructType(SimpleClass.class);
        String typeId = "com.example.test.AnotherClass";
        context.invalidTypeIdException(baseType, typeId, null);
    }

    @Test
    public void testConstructType() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();

        // test simple constructor
        Type type = String.class;
        JavaType javaType = context.constructType(type);
        assertEquals(String.class, javaType.getRawClass());

        // test constructor with a generic type
        type = new TypeToken<List<String>>() {}.getType();
        javaType = context.constructType(type);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getParameterTypes().nextParameterType());

        // test constructor with a type variable
        type = new TypeToken<Map<String, ?>>() {}.getType();
        javaType = context.constructType(type);
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getParameterTypes().get(0));
        assertEquals(Object.class, javaType.getParameterTypes().get(1));
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        JavaType baseType = context.getTypeFactory().constructType(SimpleClass.class);

        // test simple resolution
        String subClass = SimpleSubClass.class.getName();
        JavaType javaType = context.resolveSubType(baseType, subClass);
        assertEquals(SimpleSubClass.class, javaType.getRawClass());

        // test resolution with a generic type
        subClass = "java.util.List<" + SimpleSubClass.class.getName() + ">";
        javaType = context.resolveSubType(baseType, subClass);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(SimpleSubClass.class, javaType.getParameterTypes().nextParameterType());

        // test resolution with a type variable
        subClass = "java.util.Map<String,?" + SimpleSubClass.class.getName() + ">";
        javaType = context.resolveSubType(baseType, subClass);
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getParameterTypes().get(0));
        assertEquals(SimpleSubClass.class, javaType.getParameterTypes().get(1));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdExceptionWithMessage() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        JavaType baseType = context.getTypeFactory().constructType(SimpleClass.class);
        String typeId = "com.example.test.AnotherClass";
        context.invalidTypeIdException(baseType, typeId, "Subclass mismatch");
    }

    @Test
    public void testGetTypeFactory() {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();

        // test getting a simple type
        Type type = String.class;
        JavaType javaType = typeFactory.constructType(type);
        assertEquals(String.class, javaType.getRawClass());

        // test getting a complex type
        type = new TypeToken<List<String>>() {}.getType();
        javaType = typeFactory.constructType(type);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getParameterTypes().nextParameterType());

        // test getting a type variable
        type = new TypeToken<Map<String, ?>>() {}.getType();
        javaType = typeFactory.constructType(type);
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getParameterTypes().get(0));
        assertEquals(Object.class, javaType.getParameterTypes().get(1));
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        Annotated annotated = AnnotatedClass.construct(String.class, null, null);

        // test simple instantiation
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, null);
        assertNotNull(objectIdGenerator);

        // test instantiation with a custom generator
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        objectIdInfo.setGenerator(CustomObjectIdGenerator.class);
        objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertInstanceOf(CustomObjectIdGenerator.class, objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        Annotated annotated = AnnotatedClass.construct(String.class, null, null);

        // test simple instantiation
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, null);
        assertNotNull(objectIdResolver);

        // test instantiation with a custom resolver
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        objectIdInfo.setResolver(CustomObjectIdResolver.class);
        objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertInstanceOf(CustomObjectIdResolver.class, objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        Annotated annotated = AnnotatedClass.construct(String.class, null, null);

        // test simple instantiation
        Converter<Object, Object> converter = context.converterInstance(annotated, null);
        assertNull(converter);

        // test instantiation with a custom converter
        Converter<Object, Object> customConverter = new CustomConverter();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        objectIdInfo.setConverter(CustomConverter.class);
        converter = context.converterInstance(annotated, objectIdInfo);
        assertInstanceOf(CustomConverter.class, converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        JavaType javaType = context.getTypeFactory().constructType(InvalidClass.class);
        String msg = "Invalid class definition";
        context.reportBadDefinition(javaType, msg);
    }

    @Test
    public void testReportBadDefinitionWithClass() throws JsonMappingException {
        DatabindContext context = objectMapper.getSerializerProvider().getDeserializationContext();
        Class<?> clazz = InvalidClass.class;
        String msg = "Invalid class definition";
        context.reportBadDefinition(clazz, msg);
    }
}