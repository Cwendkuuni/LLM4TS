package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

@RunWith(MockitoJUnitRunner.class)
public class DatabindContextTest {

    @Test
    public void testGetConfig() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);
    }

    @Test
    public void testIsEnabled_MapperFeature() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        assertTrue(context.isEnabled(MapperFeature.DEFAULT_INTENTS));
        assertTrue(context.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS));
        assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
    }

    @Test
    public void testCanOverrideAccessModifiers() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        assertFalse(context.canOverrideAccessModifiers());

        MapperFactoryBuilder builderWithAccessModifiers = new MapperFactoryBuilder()
                .withSerializerModifier(new SerializerModifier() {
                    @Override
                    public void modifySerializer(SerializerConfig config, BeanDescription beanDesc, SerializerSerializerModifier.SerializerModifierContext context) throws SerializerProvider. serializationProviderConfigException {
                        config.setVisibilityChecker(config.getDefaultVisibilityChecker().withModifiers(javax.annotation.Override));
                    }
                });
        DatabindContext contextWithAccessModifiers = builderWithAccessModifiers.getDeserializationContext();
        assertTrue(contextWithAccessModifiers.canOverrideAccessModifiers());
    }

    @Test
    public void testGetActiveView() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        assertNull(context.getDefaultPropertyFormat(Object.class));
    }

    @Test
    public void testGetAttribute() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        context.setAttribute("test", "test value");
        assertEquals("test value", context.getAttribute("test"));
        assertNull(context.getAttribute("nonexistent"));
    }

    @Test
    public void testConstructType() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        JavaType javaType = context.getTypeFactory().constructType(String.class);
        assertEquals(String.class, javaType.getRawClass());

        JavaType javaTypeWithGenericType = context.getTypeFactory().constructType(List.class
                .getTypeParameters()[0].getType());
        assertEquals(Type.class, javaTypeWithGenericType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();

        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType subType = context.resolveSubType(baseType, "StringBuilder");
        assertEquals(StringBuilder.class, subType.getRawClass());

        JavaType baseTypeWithGenericType = context.getTypeFactory().constructType(List.class
                .getTypeParameters()[0].getType());
        JavaType subTypeWithGenericType = context.resolveSubType(baseTypeWithGenericType,
                "java.util.ArrayList");
        assertEquals(java.util.ArrayList.class, subTypeWithGenericType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        JavaType baseType = context.getTypeFactory().constructType(String.class);
        JavaType invalidSubType = context.getTypeFactory().constructType(int.class);
        throw context.invalidTypeIdException(baseType, "java.lang.Integer", "");
    }

    @Test
    public void testGetTypeFactory() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testObjectIdGeneratorInstance() throws JsonMappingException {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();

        AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws JsonMappingException {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();

        AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);
        ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotatedClass, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws JsonMappingException {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();

        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        Object converterDef = Mockito.mock(Converter.class);
        Converter<Object, Object> converter = context.converterInstance(annotatedMember, converterDef);
        assertNotNull(converter);
    }

    @Test(expected = JsonMappingException.class)
    public void testReportBadDefinition() throws JsonMappingException {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();
        JavaType javaType = builder.getFactory().constructType(String.class);
        throw context.reportBadDefinition(javaType, "Bad Type!");
    }

    @Test
    public void testFormat() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        String message = "Message: {0}";
        String result = context._format(message, "Test");
        assertEquals("Message: Test", result);
    }

    @Test
    public void testTruncate() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        String longString = "This is a very long string that needs to be truncated";
        String result = context._truncate(longString);
        assertTrue(result.contains(longString.subString(0, 500)));
        assertTrue(result.contains("[...]"));
    }

    @Test
    public void testQuotedString() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        String string = "This is a string that needs to be quoted";
        String result = context._quotedString(string);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
    }

    @Test
    public void testColonConcat() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        String base = "Base";
        String extra = "Extra";
        String result = context._colonConcat(base, extra);
        assertEquals(base + ": " + extra, result);
    }

    @Test
    public void testDesc() {
        DatabindContext context = Mockito.mock(DatabindContext.class);
        String description = "This is a description that needs to be truncated";
        String result = context._desc(description);
        assertEquals(description, result);
    }

    @Test
    public void testInvalidDefinitionException_withClass() throws JsonMappingException {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();

        Class<?> clazz = Object.class;
        try {
            context.reportBadDefinition(clazz, "Invalid Definition!");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testInvalidDefinitionException_withType() throws JsonMappingException {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        DatabindContext context = builder.getDeserializationContext();

        JavaType javaType = context.getTypeFactory().constructType(String.class);
        try {
            context.reportBadDefinition(javaType, "Invalid Definition!");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}