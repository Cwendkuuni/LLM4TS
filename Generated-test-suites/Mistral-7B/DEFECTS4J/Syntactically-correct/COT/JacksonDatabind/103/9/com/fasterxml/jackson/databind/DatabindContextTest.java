package com.example.test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

    @InjectMocks
    private DatabindContext databindContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetConfig() {
        assertSame(mapperConfig, databindContext.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertSame(annotationIntrospector, databindContext.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled_MapperFeature() {
        assertTrue(databindContext.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(databindContext.isEnabled(MapperFeature.STRICT_DATABIND));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        assertTrue(databindContext.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertNull(databindContext.getActiveView());
    }

    @Test
    public void testGetLocale() {
        Locale locale = Locale.US;
        when(mapperConfig.getLocale()).thenReturn(locale);
        assertSame(locale, databindContext.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        when(mapperConfig.getTimeZone()).thenReturn(timeZone);
        assertSame(timeZone, databindContext.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat_Class() {
        Class<String> baseType = String.class;
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        when(mapperConfig.getDefaultPropertyFormat(baseType)).thenReturn(jsonFormat);
        assertSame(jsonFormat, databindContext.getDefaultPropertyFormat(baseType));
    }

    @Test
    public void testConstructType() {
        Type type = String[].class;
        JavaType javaType = mock(JavaType.class);
        when(typeFactory.constructType(type)).thenReturn(javaType);
        JavaType result = databindContext.constructType(type);
        assertSame(javaType, result);
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = mock(JavaType.class);
        Class<String> subclass = String.class;
        JavaType result = databindContext.constructSpecializedType(baseType, subclass);
        assertSame(baseType, result);

        // Test when the baseType is not the same as the subclass
        JavaType baseType2 = mock(JavaType.class);
        when(baseType.getRawClass()).thenReturn(Object.class);
        JavaType result2 = databindContext.constructSpecializedType(baseType, subclass);
        assertNull(result2);
    }

    @Test(expected = JsonMappingException.class)
    public void testResolveSubType_InvalidTypeId() {
        JavaType baseType = mock(JavaType.class);
        String typeId = "InvalidTypeId";
        throw new JsonMappingException("Error");
        databindContext.resolveSubType(baseType, typeId);
    }

    @Test
    public void testReportBadDefinition() {
        Type type = Integer[].class;
        String msg = "Message";
        Object result = databindContext.reportBadDefinition(type, msg);
        assertInstanceOf(JsonMappingException.class, result);
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> result = databindContext.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNull(result);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdResolver result = databindContext.objectIdResolverInstance(annotated, objectIdInfo);
        assertNull(result);
    }

    @Test
    public void testConverterInstance() {
        Annotated annotated = mock(Annotated.class);
        Object converterDef = new JsonDeserialize.With();
        Converter<Object, Object> result = databindContext.converterInstance(annotated, converterDef);
        assertNull(result);

        // Test when converterDef is a Converter instance
        Converter<Object, Object> converter = mock(Converter.class);
        result = databindContext.converterInstance(annotated, converter);
        assertSame(converter, result);
    }

    @Test
    public void testReportBadDefinition_Class() {
        Type type = Integer[].class;
        String msg = "Message";
        Object result = databindContext.reportBadDefinition(type, msg);
        assertInstanceOf(JsonMappingException.class, result);
    }
}