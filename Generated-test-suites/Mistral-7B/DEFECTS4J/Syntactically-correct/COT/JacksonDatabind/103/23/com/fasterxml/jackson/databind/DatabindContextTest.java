package com.example.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultInitTracker;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class DatabindContextTest {

    private DatabindContext databindContext;
    private MapperConfig<?> mapperConfig;
    private AnnotationIntrospector annotationIntrospector;
    private TypeFactory typeFactory;

    @Before
    public void setup() {
        mapperConfig = mock(MapperConfig.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
        typeFactory = mock(TypeFactory.class);
        databindContext = spy(new DatabindContext() {
            @Override
            public MapperConfig<?> getConfig() {
                return mapperConfig;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return annotationIntrospector;
            }

            @Override
            public TypeFactory getTypeFactory() {
                return typeFactory;
            }
        });
    }

    @Test
    public void testGetConfig() {
        assertEquals(mapperConfig, databindContext.getConfig());
    }

    @Test
    public void testIsEnabled_isEnabled() {
        when(mapperConfig.isEnabled(MapperFeature.AUTO_DETECT_CREATORS)).thenReturn(true);
        assertTrue(databindContext.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    @Test
    public void testIsEnabled_isDisabled() {
        when(mapperConfig.isEnabled(MapperFeature.AUTO_DETECT_CREATORS)).thenReturn(false);
        assertFalse(databindContext.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        when(mapperConfig.canOverrideAccessModifiers()).thenReturn(true);
        assertTrue(databindContext.canOverrideAccessModifiers());
    }

    @Test
    public void testCanOverrideAccessModifiers_false() {
        when(mapperConfig.canOverrideAccessModifiers()).thenReturn(false);
        assertFalse(databindContext.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        assertEquals(null, databindContext.getActiveView());
    }

    @Test
    public void testGetLocale() {
        when(mapperConfig.getLocale()).thenReturn(Locale.US);
        assertEquals(Locale.US, databindContext.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        when(mapperConfig.getTimeZone()).thenReturn(TimeZone.getDefault());
        assertEquals(TimeZone.getDefault(), databindContext.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        Class<?> baseType = Object.class;
        JavaType javaType = mock(JavaType.class);
        when(typeFactory.constructType(baseType)).thenReturn(javaType);
        when(annotationIntrospector.getDefaultPropertyFormat(javaType)).thenReturn(new JsonFormat.Value());
        assertEquals(new JsonFormat.Value(), databindContext.getDefaultPropertyFormat(baseType));
    }

    @Test
    public void testGetAttribute() {
        Object key = new Object();
        Object value = new Object();
        databindContext.setAttribute(key, value);
        assertEquals(value, databindContext.getAttribute(key));
    }

    @Test
    public void testGetAttribute_null() {
        assertNull(databindContext.getAttribute(null));
    }

    @Test
    public void testConstructType() {
        Type type = new Type() {
        };
        JavaType javaType = mock(JavaType.class);
        when(typeFactory.constructType(type)).thenReturn(javaType);
        assertEquals(javaType, databindContext.constructType(type));
    }

    @Test(expected = JsonMappingException.class)
    public void testConstructType_invalidTypeIdException() {
        JavaType baseType = mock(JavaType.class);
        String typeId = "invalidTypeId";
        when(databindContext.getTypeFactory()).thenThrow(new JsonMappingException(""));
        databindContext.resolveSubType(baseType, typeId);
    }

    @Test
    public void testHandleUnknownTypeId_null() {
        JavaType baseType = mock(JavaType.class);
        String typeId = null;
        doThrow(new JsonMappingException("")).when(databindContext).invalidTypeIdException(baseType, typeId, "");
        assertNull(databindContext.handleUnknownTypeId(baseType, typeId));
    }

    @Test
    public void testHandleUnknownTypeId_emptyString() {
        JavaType baseType = mock(JavaType.class);
        String typeId = "";
        doThrow(new JsonMappingException("")).when(databindContext).invalidTypeIdException(baseType, typeId, "");
        assertNull(databindContext.handleUnknownTypeId(baseType, typeId));
    }

    @Test
    public void testHandleUnknownTypeId_String() {
        JavaType baseType = mock(JavaType.class);
        String typeId = "String";
        when(databindContext.getTypeFactory()).thenReturn(typeFactory);
        when(typeFactory.constructType(TypeFactory.Default.STRING_TYPE)).thenReturn(baseType);
        assertEquals(baseType, databindContext.handleUnknownTypeId(baseType, typeId));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId_invalidTypeId() {
        JavaType baseType = mock(JavaType.class);
        String typeId = "InvalidTypeId";
        doThrow(new JsonMappingException("")).when(databindContext).invalidTypeIdException(baseType, typeId, "");
        databindContext.handleUnknownTypeId(baseType, typeId);
    }

    @Test
    public void testInvalidTypeIdException() {
        JavaType baseType = mock(JavaType.class);
        String typeId = "TypeId";
        String extraDesc = "Not a subtype";
        JsonMappingException jsonMappingException = new JsonMappingException("Description");
        when(databindContext.getTypeFactory()).thenThrow(jsonMappingException);
        assertThrows(JsonMappingException.class, () -> databindContext.invalidTypeIdException(baseType, typeId, extraDesc));
    }

    @Test
    public void testGetTypeFactory() {
        assertEquals(typeFactory, databindContext.getTypeFactory());
    }

    @Test
    public void testObjectIdGeneratorInstance() {
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = mock(ObjectIdGenerator.class);
        when(objectIdGenerator.forScope(objectIdInfo.getScope())).thenReturn(objectIdGenerator);
        when(annotationIntrospector.getObjectIdGenerator(annotated)).thenReturn(objectIdGenerator);
        ObjectIdGenerator<?> result = databindContext.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertEquals(objectIdGenerator, result);
    }

    @Test
    public void testObjectIdResolverInstance() {
        Annotated annotated = mock(Annotated.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = mock(ObjectIdResolver.class);
        when(objectIdResolver).thenReturn(objectIdResolver);
        assertEquals(objectIdResolver, databindContext.objectIdResolverInstance(annotated, objectIdInfo));
    }

    @Test
    public void testConverterInstance() {
        Annotated annotated = mock(Annotated.class);
        Object converterDef = new JsonProperty.Deserialize();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.getClass()).thenReturn(StdDeserializer.EmptyStringDeserializer.class);
        when(annotationIntrospector.findConverter(annotated, converterDef)).thenReturn(converterDef);
        when(annotationIntrospector.findConverter(annotated, converterDef.getClass())).thenReturn(converter);
        Converter<Object, Object> result = databindContext.converterInstance(annotated, converterDef);
        assertEquals(converter, result);
    }

    @Test
    public void testReportBadDefinition() {
        Class<?> type = Foo.class;
        String msg = "Message";
        JsonMappingException jsonMappingException = new JsonMappingException("Description");
        when(databindContext.getTypeFactory()).thenThrow(jsonMappingException);
        assertEquals(jsonMappingException, databindContext.reportBadDefinition(type, msg));
    }

    @Test
    public void testReportBadDefinition_withExceptionMessage() {
        Class<?> type = Foo.class;
        String msg = "Message %s";
        JsonMappingException jsonMappingException = new JsonMappingException("Description %s", "error");
        when(databindContext.getTypeFactory()).thenThrow(jsonMappingException);
        assertEquals(jsonMappingException, databindContext.reportBadDefinition(type, msg, "error"));
    }

    @Test
    public void testReportBadDefinition_withExceptionMessageAndExceptionCause() {
        Class<?> type = Foo.class;
        String msg = "Message %s";
        Throwable cause = new IOException("Cause");
        JsonMappingException jsonMappingException = new JsonMappingException("Description %s", "error", cause);
        when(databindContext.getTypeFactory()).thenThrow(jsonMappingException);
        assertEquals(jsonMappingException, databindContext.reportBadDefinition(type, msg, "error", cause));
    }

    @Test
    public void testFormat() {
        String msg = "Message %s %s %s";
        Object msgArg1 = "arg1";
        Object msgArg2 = "arg2";
        Object msgArg3 = "arg3";
        assertEquals(_format(msg, msgArg1, msgArg2, msgArg3), "Message arg1 arg2 arg3");
    }

    @Test
    public void testTruncate() {
        String desc = "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription";
        assertEquals(_truncate(desc), "DescriptionDescription...DescriptionDescription");
    }

    @Test
    public void testQuotedString() {
        String desc = "Description";
        assertEquals(_quotedString(desc), "\"Description\"");
    }

    @Test
    public void testColonConcat() {
        String msgBase = "Message";
        String extra = " Extra message";
        assertEquals(_colonConcat(msgBase, extra), "Message Extra message");
    }

    @Test
    public void testDesc() {
        String desc = "Description";
        assertEquals(_desc(desc), "Description");
    }

    private static class Foo {}
}