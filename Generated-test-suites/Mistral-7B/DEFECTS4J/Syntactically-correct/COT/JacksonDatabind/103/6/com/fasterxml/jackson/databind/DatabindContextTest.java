package com.example.junit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;
import com.fasterxml.jackson.databind.util.ObjectIdResolver;

/**
 * Test class for {@link DatabindContext}
 */
public class DatabindContextTest {

    private DatabindContext context;
    private MapperConfig<ObjectMapper> config;
    private HandlerInstantiator handlerInstantiator;
    private AnnotationIntrospector annotationIntrospector;
    private TypeFactory typeFactory;

    @Before
    public void setUp() {
        handlerInstantiator = mock(HandlerInstantiator.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
        annotationIntrospector = spy(annotationIntrospector);
        config = new MapperConfig<ObjectMapper>() {{
            setAnnotationIntrospector(annotationIntrospector);
            setHandlerInstantiator(handlerInstantiator);
        }};
        context = spy(new BaseDatabindContext() {{
            setConfig(config);
            setAnnotationIntrospector(annotationIntrospector);
            setTypeFactory(typeFactory = new SimpleTypeFactory());
        }});
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetConfig() {
        assertSame(config, context.getConfig());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertSame(annotationIntrospector, context.getAnnotationIntrospector());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS).notDefault());
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
        Locale locale = Locale.US;
        when(config.getLocale()).thenReturn(locale);
        assertSame(locale, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        when(config.getTimeZone()).thenReturn(timeZone);
        assertSame(timeZone, context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        assertNull(context.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testSetAttribute() {
        Object attribute = "test";
        context.setAttribute("test", attribute);
        assertSame(attribute, context.getAttribute("test"));
    }

    @Test
    public void testConstructType() {
        Type type = Object.class;
        JavaType javaType = context.constructType(type);
        assertNotNull(javaType);
        assertEquals(Object.class, javaType.getRawClass());
    }

    @Test
    public void testConstructSpecializedType() {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        Class<?> subclass = StringBuffer.class;
        JavaType specializedType = context.constructSpecializedType(baseType, subclass);
        assertNotNull(specializedType);
        assertEquals(StringBuffer.class, specializedType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String subClass = "java.lang.StringBuffer";
        JavaType resolvedSubType = context.resolveSubType(baseType, subClass);
        assertNotNull(resolvedSubType);
        assertEquals(StringBuffer.class, resolvedSubType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        String typeId = "InvalidTypeId";
        context.invalidTypeIdException(baseType, typeId, "test");
    }

    @Test
    public void testGetTypeFactory() {
        assertSame(typeFactory, context.getTypeFactory());
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo() {{
            setGeneratorType(ObjectIdGenerator.Identity.class);
        }};
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertInstanceOf(ObjectIdGenerator.Identity.class, objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo() {{
            setResolverType(ObjectIdResolver.Std.class);
        }};
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertInstanceOf(ObjectIdResolver.Std.class, objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        Annotated annotated = mock(Annotated.class);
        Object converterDef = JsonAutoDetect.None.class;
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);

        converterDef = JsonSubTypes.With.class;
        converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);

        converterDef = JsonTypeInfo.Id.NAME.class;
        converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);

        converterDef = JsonTypeInfo.Id.BASE_CLASS.class;
        converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);

        converterDef = JsonTypeInfo.Id.VALUE.class;
        converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);

        converterDef = String.class;
        converter = context.converterInstance(annotated, converterDef);
        assertNull(converter);

        converterDef = StringToIntegerConverter.class;
        converter = context.converterInstance(annotated, converterDef);
        assertInstanceOf(StringToIntegerConverter.class, converter);
    }

    @Test
    public void testReportBadDefinition() throws JsonMappingException {
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        String msg = "test message";
        String result = context.reportBadDefinition(javaType, msg);
        assertTrue(result.contains(msg));
    }
}

class BaseDatabindContext extends DatabindContext {
    // empty
}

class SimpleTypeFactory extends TypeFactory {
    // empty
}

class StringToIntegerConverter extends Converter<String, Integer> {
    // empty
}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StringBuffer.class, name = "StringBuffer")
})
class TestClass {
    // empty
}