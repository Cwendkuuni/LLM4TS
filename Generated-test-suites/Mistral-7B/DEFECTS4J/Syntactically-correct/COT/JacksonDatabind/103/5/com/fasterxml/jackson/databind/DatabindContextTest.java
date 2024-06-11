import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for DatabindContext
 */
public class DatabindContextTest {

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @Mock
    private MapperConfig<?> mapperConfig;

    @Mock
    private HandlerInstantiator handlerInstantiator;

    @Mock
    private TypeFactory typeFactory;

    @Mock
    private ObjectReader objectReader;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DatabindContext databindContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mapperConfig.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
        when(mapperConfig.getHandlerInstantiator()).thenReturn(handlerInstantiator);
        when(mapperConfig.getTypeFactory()).thenReturn(typeFactory);
        when(handlerInstantiator.objectIdGeneratorInstance(mapperConfig, mock(Annotated.class), mock(ObjectIdInfo.class))).thenReturn(mock(ObjectIdGenerator.class));
        when(handlerInstantiator.resolverIdGeneratorInstance(mapperConfig, mock(Annotated.class), mock(ObjectIdInfo.class))).thenReturn(mock(ObjectIdResolver.class));
        when(handlerInstantiator.converterInstance(mapperConfig, mock(Annotated.class), mock(Class.class))).thenReturn(mock(Converter.class));
    }

    /**
     * Test method for getConfig()
     *
     * @throws JsonMappingException expected
     * @throws JsonProcessingException expected
     */
    @Test
    public void testGetConfig() throws JsonMappingException, JsonProcessingException {
        assertEquals(mapperConfig, databindContext.getConfig());
    }

    /**
     * Test method for getAnnotationIntrospector()
     */
    @Test
    public void testGetAnnotationIntrospector() {
        assertEquals(annotationIntrospector, databindContext.getAnnotationIntrospector());
    }

    /**
     * Test method for isEnabled(MapperFeature feature)
     */
    @Test
    public void testIsEnabled() {
        assertNull(databindContext.isEnabled(null));
        when(mapperConfig.isEnabled(MapperFeature.AUTO_DETECT_TYPES)).thenReturn(true);
        assertTrue(databindContext.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
        when(mapperConfig.isEnabled(MapperFeature.AUTO_DETECT_TYPES)).thenReturn(false);
        assertFalse(databindContext.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
    }

    /**
     * Test method for canOverrideAccessModifiers()
     */
    @Test
    public void testCanOverrideAccessModifiers() {
        assertFalse(databindContext.canOverrideAccessModifiers());
    }

    /**
     * Test method for getActiveView()
     */
    @Test
    public void testGetActiveView() {
        assertNull(databindContext.getActiveView());
    }

    /**
     * Test method for getLocale()
     */
    @Test
    public void testGetLocale() {
        assertEquals(Locale.getDefault(), databindContext.getLocale());
    }

    /**
     * Test method for getTimeZone()
     */
    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getDefault(), databindContext.getTimeZone());
    }

    /**
     * Test method for getDefaultPropertyFormat(Class<?> baseType)
     */
    @Test
    public void testGetDefaultPropertyFormat() {
        assertNull(databindContext.getDefaultPropertyFormat(null));
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        when(objectIdInfo.getDefaultPropertyFormat()).thenReturn(mock(JsonFormat.Value.class));
        assertEquals(mock(JsonFormat.Value.class), databindContext.getDefaultPropertyFormat(Object.class));
    }

    /**
     * Test method for getAttribute(Object key)
     */
    @Test
    public void testGetAttribute() {
        assertNull(databindContext.getAttribute(null));
        Object key = new Object();
        when(objectMapper.getSerializerConfig()).thenReturn(mapperConfig);
        when(mapperConfig.getAttribute(key)).thenReturn("Value");
        assertEquals("Value", databindContext.getAttribute(key));
    }

    /**
     * Test method for setAttribute(Object key, Object value)
     */
    @Test
    public void testSetAttribute() {
        Object key = new Object();
        Object value = new Object();
        databindContext.setAttribute(key, value);
        verify(mapperConfig).setAttribute(key, value);
    }

    /**
     * Test method for constructType(Type type)
     *
     * @throws JsonMappingException expected
     */
    @Test(expected = JsonMappingException.class)
    public void testConstructTypeNullType() throws JsonMappingException {
        databindContext.constructType(null);
    }

    /**
     * Test method for constructType(Type type)
     */
    @Test
    public void testConstructType() {
        Type type = new TypeToken<Integer>() {}.getType();
        assertEquals(typeFactory.constructType(type), databindContext.constructType(type));
    }

    /**
     * Test method for constructSpecializedType(JavaType baseType, Class<?> subclass)
     */
    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = mock(JavaType.class);
        Class<?> subclass = String.class;
        when(baseType.getRawClass()).thenReturn(Object.class);
        assertEquals(typeFactory.constructSpecializedType(baseType, subclass), databindContext.constructSpecializedType(baseType, subclass));
    }

    /**
     * Test method for resolveSubType(JavaType baseType, String subClass)
     *
     * @throws JsonMappingException expected
     */
    @Test(expected = JsonMappingException.class)
    public void testResolveSubTypeNullBaseType() throws JsonMappingException {
        databindContext.resolveSubType(null, "java.lang.String");
    }

    /**
     * Test method for resolveSubType(JavaType baseType, String subClass)
     */
    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getRawClass()).thenReturn(Object.class);
        when(typeFactory.constructFromCanonical("java.lang.String")).thenReturn(mock(JavaType.class));
        assertEquals(mock(JavaType.class), databindContext.resolveSubType(baseType, "java.lang.String"));
    }

    /**
     * Test method for invalidTypeIdException(JavaType baseType, String typeId, String extraDesc)
     */
    @Test
    public void testInvalidTypeIdException() {
        JavaType baseType = mock(JavaType.class);
        String typeId = "java.lang.String";
        String extraDesc = "Not a subtype";
        when(baseType.getRawClass()).thenReturn(Object.class);
        verify(databindContext).invalidTypeIdException(baseType, typeId, extraDesc);
    }

    /**
     * Test method for getTypeFactory()
     */
    @Test
    public void testGetTypeFactory() {
        assertEquals(typeFactory, databindContext.getTypeFactory());
    }

    /**
     * Test method for objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
     */
    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        assertEquals(mock(ObjectIdGenerator.class), databindContext.objectIdGeneratorInstance(annotated, objectIdInfo));
    }

    /**
     * Test method for objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
     */
    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        assertEquals(mock(ObjectIdResolver.class), databindContext.objectIdResolverInstance(annotated, objectIdInfo));
    }

    /**
     * Test method for converterInstance(Annotated annotated, Object converterDef)
     *
     * @throws JsonMappingException expected
     */
    @Test(expected = JsonMappingException.class)
    public void testConverterInstanceNullAnnotated() throws JsonMappingException {
        databindContext.converterInstance(null, mock(Object.class));
    }

    /**
     * Test method for converterInstance(Annotated annotated, Object converterDef)
     */
    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        Object converterDef = mock(Object.class);
        assertEquals(mock(Converter.class), databindContext.converterInstance(annotated, converterDef));
    }

    /**
     * Test method for reportBadDefinition(JavaType type, String msg)
     *
     * @throws JsonMappingException expected
     */
    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinitionNullType() throws JsonMappingException {
        databindContext.reportBadDefinition(null, "Message");
    }

    /**
     * Test method for reportBadDefinition(JavaType type, String msg)
     */
    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType type = mock(JavaType.class);
        when(type.getRawClass()).thenReturn(Object.class);
        String msg = "Message";
        assertEquals(reportBadDefinition(type, msg), databindContext.reportBadDefinition(type, msg));
    }

    /**
     * Test method for reportBadDefinition(Class<?> type, String msg)
     */
    @Test
    public void testReportBadDefinitionClass() throws JsonMappingException {
        Class<?> type = String.class;
        String msg = "Message";
        assertEquals(reportBadDefinition(type, msg), databindContext.reportBadDefinition(type, msg));
    }

    /**
     * Helper method to create reportBadDefinition with mocks
     *
     * @param type type
     * @param msg message
     * @return JsonMappingException
     */
    private JsonMappingException reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
        when(mapperConfig.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
        when(mapperConfig.getTypeFactory()).thenReturn(typeFactory);
        when(annotationIntrospector.findSerializerAnnotation(type)).thenReturn(new JsonAutoDetect.MethodPropertyAnnotation(null, null, new JsonAutoDetect.MethodPropertyAnn(null)));
        return databindContext.reportBadDefinition(type, msg);
    }
}