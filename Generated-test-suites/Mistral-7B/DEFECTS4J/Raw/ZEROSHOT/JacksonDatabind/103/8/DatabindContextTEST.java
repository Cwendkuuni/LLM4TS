package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.None;

/**
 * Test class for {@link DatabindContext}
 */
public class DatabindContextTest {

    private DeserializationContext context;
    private SerializerProvider provider;
    private MapperConfig<ObjectMapper> config;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        config = new MapperConfig<ObjectMapper>(ObjectMapper.getDefaultInstance());
        mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
        provider = mapper.getSerializerProvider();
    }

    /**
     * Test for {@link DatabindContext#getConfig()}
     */
    @Test
    public void testGetConfig() {
        assertEquals(config, context.getConfig());
    }

    /**
     * Test for {@link DatabindContext#getAnnotationIntrospector()}
     */
    @Test
    public void testGetAnnotationIntrospector() {
        assertEquals(config.getDefaultAnnotatedClassInfoResolver(), context.getAnnotationIntrospector());
    }

    /**
     * Test for {@link DatabindContext#isEnabled(MapperFeature)}
     */
    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_INTROSPECTION_ANNOTATIONS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS_AND_SETTERS));
    }

    /**
     * Test for {@link DatabindContext#canOverrideAccessModifiers()}
     */
    @Test
    public void testCanOverrideAccessModifiers() {
        assertFalse(context.canOverrideAccessModifiers());
    }

    /**
     * Test for {@link DatabindContext#getActiveView()}
     */
    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    /**
     * Test for {@link DatabindContext#getLocale()}
     */
    @Test
    public void testGetLocale() {
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    /**
     * Test for {@link DatabindContext#getTimeZone()}
     */
    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    /**
     * Test for {@link DatabindContext#getDefaultPropertyFormat(Class)}
     */
    @Test
    public void testgetDefaultPropertyFormat() {
        assertEquals(null, context.getDefaultPropertyFormat(Object.class));
    }

    /**
     * Test for {@link DatabindContext#getAttribute(Object)}
     */
    @Test
    public void testGetAttribute() {
        context.setAttribute("foo", "bar");
        assertEquals("bar", context.getAttribute("foo"));
        assertNull(context.getAttribute("baz"));
    }

    /**
     * Test for {@link DatabindContext#setAttribute(Object, Object)}
     */
    @Test
    public void testSetAttribute() {
        context.setAttribute("foo", "bar");
        assertEquals("bar", context.getAttribute("foo"));

        context.setAttribute("foo", 123);
        assertEquals(123, context.getAttribute("foo"));
    }

    /**
     * Test for {@link DatabindContext#constructType(Type)}
     */
    @Test
    public void testConstructType() {
        JavaType javaType = context.getTypeFactory().constructType(String[].class);
        assertEquals(String[].class, javaType.getRawClass());
    }

    /**
     * Test for {@link DatabindContext#constructSpecializedType(JavaType, Class)}
     */
    @Test
    public void testConstructSpecializedType() {
        JavaType javaType = context.getTypeFactory().constructType(List.class);
        JavaType specializedType = context.constructSpecializedType(javaType, ArrayList.class);
        assertEquals(ArrayList.class, specializedType.getRawClass());
        assertEquals(List.class, specializedType.getType());
    }

    /**
     * Test for {@link DatabindContext#resolveSubType(JavaType, String)}
     */
    @Test
    public void testResolveSubType() {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        JavaType resolvedType = context.resolveSubType(javaType, "java.lang.String");
        assertEquals(String.class, resolvedType.getRawClass());
    }

    /**
     * Test for {@link DatabindContext#invalidTypeIdException(JavaType, String, String)}
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() {
        JavaType javaType = context.getTypeFactory().constructType(Object.class);
        context.invalidTypeIdException(javaType, "foo", "Not a base type");
    }

    /**
     * Test for {@link DatabindContext#getTypeFactory()}
     */
    @Test
    public void testgetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertEquals(String[].class, typeFactory.constructType(String[].class));
    }

    /**
     * Test for {@link DatabindContext#objectIdGeneratorInstance(Annotated, ObjectIdInfo)}
     */
    @Test
    public void testObjectIdGeneratorInstance() {
        Annotated annotated = new AnnotatedClass(String.class, null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, new JsonTypeName(Id.NAME), null, null);
        ObjectIdGenerator<?> generated = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(generated);
    }

    /**
     * Test for {@link DatabindContext#objectIdResolverInstance(Annotated, ObjectIdInfo)}
     */
    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = new AnnotatedClass(String.class, null, null);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, null, null, null);
        ObjectIdResolver generated = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(generated);
    }

    /**
     * Test for {@link DatabindContext#converterInstance(Annotated, Object)}
     */
    @Test
    public void testConverterInstance() {
        Annotated annotated = new AnnotatedClass(String[].class, null, null);
        Converter<String[], Integer[]> converter = new Converter<String[], Integer[]>() {
            @Override
            public Integer[] convert(String[] src, Converter<?> converterProperty) {
                return new Integer[src.length];
            }

            @Override
            public String[] reverseConvert(Integer[] src, Converter<?> converterProperty) {
                return new String[src.length];
            }
        };
        context.setAnnotationIntrospector(new AnnotationIntrospector() {
            @Override
            public Converter<Object, Object> findConverter(Annotated annotated, JavaType srcType, JavaType targType) {
                return converter;
            }
        });

        Converter<Object, Object> converter = context.converterInstance(annotated, converter);
        assertSame(converter, converter);
    }

    /**
     * Test for {@link DatabindContext#reportBadDefinition(JavaType, String)}
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() {
        JavaType javaType = context.getTypeFactory().constructType(new Class<?>[] { String.class, String.class });
        context.reportBadDefinition(javaType, "Invalid type definition");
    }

    /**
     * Test for {@link DatabindContext#reportBadDefinition(Class, String)}
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionClass() {
        context.reportBadDefinition(String[].class, "Invalid class definition");
    }
}

class AnnotatedClass implements Annotated {

    private final Class<?> clazz;
    private final JsonFilter filter;
    private final JsonTypeName typeName;

    public AnnotatedClass(Class<?> clazz, JsonFilter filter, JsonTypeName typeName) {
        this.clazz = clazz;
        this.filter = filter;
        this.typeName = typeName;
    }

    @Override
    public Class<?> getRawClass() {
        return clazz;
    }

    @Override
    public JsonFilter getFilterProvider() {
        return filter;
    }

    @Override
    public JsonTypeName getTypeId() {
        return typeName;
    }

    @Override
    public Object getAnnotation(Class<?> annotations) {
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getAnnotationsByType(Class<? extends Annotation> annotationType) {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotationsByType(Class<? extends Annotation> annotationType) {
        return new Annotation[0];
    }

    @Override
    public String toString() {
        return clazz.getName();
    }
}