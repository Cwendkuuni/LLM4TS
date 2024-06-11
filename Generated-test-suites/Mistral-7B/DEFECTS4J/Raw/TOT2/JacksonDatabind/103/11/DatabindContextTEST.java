import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DatabindContextTest {

    private DatabindContext context;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
    }

    // Test for getConfig()
    @Test
    public void testGetConfig() {
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        MapperConfig<?> config = context.getConfig();
        assertEquals(annotationIntrospector, config.getAnnotationIntrospector());
    }

    // Test for isEnabled()
    @Test
    public void testIsEnabled() {
        boolean enabled = context.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_FOR_SUBTYPES);
        assertFalse(enabled);

        MapperConfig<?> config = new MapperConfig.Builder()
                .disable(MapperFeature.AUTO_DETECT_DERIVED_FOR_SUBTYPES)
                .build();
        context = new JacksonAnnotationIntrospectorContext(config);
        enabled = context.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_FOR_SUBTYPES);
        assertFalse(enabled);

        config = new MapperConfig.Builder()
                .enable(MapperFeature.AUTO_DETECT_DERIVED_FOR_SUBTYPES)
                .build();
        context = new JacksonAnnotationIntrospectorContext(config);
        enabled = context.isEnabled(MapperFeature.AUTO_DETECT_DERIVED_FOR_SUBTYPES);
        assertTrue(enabled);
    }

    // Test for getAttribute()
    @Test
    public void testGetAttribute() {
        Object attr = context.setAttribute("testKey", "testValue").getAttribute("testKey");
        assertEquals("testValue", attr);
    }

    // Test for constructType()
    @Test
    public void testConstructType() throws JsonMappingException {
        Type type = new TypeFactory().constructType(String.class);
        JavaType javaType = context.constructType(type);
        assertEquals(String.class, javaType.getRawClass());
    }

    // Test for resolveSubType()
    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "java.lang.StringBuffer");
        assertEquals(StringBuffer.class, subType.getRawClass());
    }

    // Test for invalidTypeIdException()
    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(baseType, "java.lang.Integer");
    }

    // Test for getTypeFactory()
    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = context.getTypeFactory();
        assertEquals(typeFactory.getClass(), context.getTypeFactory().getClass());
    }

    // Test for objectIdGeneratorInstance()
    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        AnnotatedClass annotatedClass = AnnotatedClass.construct(String.class, null);
        ObjectIdGenerator<?> idGenerator = context.objectIdGeneratorInstance(annotatedClass, null);
        assertNotNull(idGenerator);
    }

    // Test for objectIdResolverInstance()
    @Test
    public void testObjectIdResolverInstance() {
        AnnotatedClass annotatedClass = AnnotatedClass.construct(String.class, null);
        ObjectIdResolver idResolver = context.objectIdResolverInstance(annotatedClass, null);
        assertNotNull(idResolver);
    }

    // Test for converterInstance()
    @Test
    public void testConverterInstance() throws JsonMappingException {
        AnnotatedClass annotatedClass = AnnotatedClass.construct(String.class, null);
        Converter<String,Integer> converter = context.converterInstance(annotatedClass, null);
        assertNull(converter);
    }

    // Test for getActiveView()
    @Test
    public void testGetActiveView() {
        assertNull(context.getActiveView());
    }

    // Test for getLocale()
    @Test
    public void testGetLocale() {
        Locale locale = context.getLocale();
        assertEquals(Locale.getDefault(), locale);
    }

    // Test for getTimeZone()
    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = context.getTimeZone();
        assertEquals(TimeZone.getDefault(), timeZone);
    }

    // Test for getDefaultPropertyFormat()
    @Test
    public void testGetDefaultPropertyFormat() {
        JsonFormat.Value defaultFormat = context.getDefaultPropertyFormat(Object.class);
        assertNull(defaultFormat);
    }
}