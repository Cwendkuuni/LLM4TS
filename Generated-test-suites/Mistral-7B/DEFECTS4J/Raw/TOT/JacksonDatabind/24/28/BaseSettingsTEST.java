package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseSettingsTest {

    private ObjectMapper objectMapper;
    private ClassIntrospector classIntrospector;
    private AnnotationIntrospector annotationIntrospector;
    private VisibilityChecker<?> visibilityChecker;
    private PropertyNamingStrategy propertyNamingStrategy;
    private TypeFactory typeFactory;
    private TypeResolverBuilder<?> typeResolverBuilder;
    private DateFormat dateFormat;
    private HandlerInstantiator handlerInstantiator;
    private BaseSettings baseSettings;

    @Before
    public void setup() {
        classIntrospector = new ClassIntrospector();
        annotationIntrospector = new AnnotationIntrospector();
        visibilityChecker = new VisibilityChecker.Default();
        propertyNamingStrategy = PropertyNamingStrategy.STANDARD_ENGLISH_LOW_CAMEL_CASE;
        typeFactory = new TypeFactory();
        typeResolverBuilder = new SimpleTypeResolverBuilder();
        dateFormat = new StdDateFormat();
        handlerInstantiator = new HandlerInstantiator();
        objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(annotationIntrospector);
        objectMapper.setClassIntrospector(classIntrospector);
        objectMapper.setVisibilityChecker(visibilityChecker);
        objectMapper.setPropertyNamingStrategy(propertyNamingStrategy);
        objectMapper.setTypeFactory(typeFactory);
        objectMapper.setTypeResolverBuilder(typeResolverBuilder);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
                typeResolverBuilder, dateFormat, handlerInstantiator, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.US_STANDARD);
    }

    @Test
    public void testBaseSettingsGetters() throws JsonMappingException, IOException {
        assertNotNull(baseSettings.getClassIntrospector());
        assertNotNull(baseSettings.getAnnotationIntrospector());
        assertNotNull(baseSettings.getVisibilityChecker());
        assertNotNull(baseSettings.getPropertyNamingStrategy());
        assertNotNull(baseSettings.getTypeFactory());
        assertNotNull(baseSettings.getTypeResolverBuilder());
        assertNotNull(baseSettings.getDateFormat());
        assertNotNull(baseSettings.getHandlerInstantiator());
        assertEquals(Locale.getDefault(), baseSettings.getLocale());
        assertEquals(TimeZone.getDefault(), baseSettings.getTimeZone());
        assertEquals(Base64Variant.US_STANDARD, baseSettings.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithGetter() throws JsonMappingException, IOException, ParseException {
        DateFormat newDateFormat = new StdDateFormat();
        TimeZone newTimeZone = TimeZone.getTimeZone("Europe/London");
        BaseSettings newBaseSettings = baseSettings.withDateFormat(newDateFormat);
        assertEquals(newDateFormat, newBaseSettings.getDateFormat());
        assertEquals(TimeZone.getDefault(), newBaseSettings.getTimeZone());

        newBaseSettings = newBaseSettings.withTimeZone(newTimeZone);
        assertEquals(newDateFormat, newBaseSettings.getDateFormat());
        assertNotSame(TimeZone.getDefault(), newBaseSettings.getTimeZone());
        assertEquals(newTimeZone, newBaseSettings.getTimeZone());
    }

    @Test
    public void testBaseSettingsWithClassIntrospector() throws JsonMappingException, IOException {
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(newClassIntrospector);
        assertNotSame(classIntrospector, newClassIntrospector);
        assertEquals(newClassIntrospector, newBaseSettings.getClassIntrospector());
    }

    @Test
    public void testBaseSettingsWithAnnotationIntrospector() throws JsonMappingException, IOException {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newBaseSettings = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);
        assertNotSame(annotationIntrospector, newAnnotationIntrospector);
        assertEquals(newAnnotationIntrospector, newBaseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testBaseSettingsWithVisibilityChecker() throws JsonMappingException, IOException {
        VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker.Std();
        BaseSettings newBaseSettings = baseSettings.withVisibilityChecker(newVisibilityChecker);
        assertNotSame(visibilityChecker, newVisibilityChecker);
        assertEquals(newVisibilityChecker, newBaseSettings.getVisibilityChecker());
    }

    @Test
    public void testBaseSettingsWithPropertyNamingStrategy() throws JsonMappingException, IOException {
        PropertyNamingStrategy newPropertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE;
        BaseSettings newBaseSettings = baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategy);
        assertNotSame(propertyNamingStrategy, newPropertyNamingStrategy);
        assertEquals(newPropertyNamingStrategy, newBaseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testBaseSettingsWithTypeFactory() throws JsonMappingException, IOException {
        TypeFactory newTypeFactory = new TypeFactory();
        BaseSettings newBaseSettings = baseSettings.withTypeFactory(newTypeFactory);
        assertNotSame(typeFactory, newTypeFactory);
        assertEquals(newTypeFactory, newBaseSettings.getTypeFactory());
    }

    @Test
    public void testBaseSettingsWithTypeResolverBuilder() throws JsonMappingException, IOException {
        TypeResolverBuilder<?> newTypeResolverBuilder = new SimpleTypeResolverBuilder();
        BaseSettings newBaseSettings = baseSettings.withTypeResolverBuilder(newTypeResolverBuilder);
        assertNotSame(typeResolverBuilder, newTypeResolverBuilder);
        assertEquals(newTypeResolverBuilder, newBaseSettings.getTypeResolverBuilder());
    }

    @Test
    public void testBaseSettingsWithDateFormat() throws JsonMappingException, IOException {
        DateFormat newDateFormat = new StdDateFormat();
        BaseSettings newBaseSettings = baseSettings.withDateFormat(newDateFormat);
        assertNotSame(dateFormat, newDateFormat);
        assertEquals(newDateFormat, newBaseSettings.getDateFormat());
    }

    @Test
    public void testBaseSettingsWithHandlerInstantiator() throws JsonMappingException, IOException {
        HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();
        BaseSettings newBaseSettings = baseSettings.withHandlerInstantiator(newHandlerInstantiator);
        assertNotSame(handlerInstantiator, newHandlerInstantiator);
        assertEquals(newHandlerInstantiator, newBaseSettings.getHandlerInstantiator());
    }

    @Test
    public void testBaseSettingsWithLocale() throws JsonMappingException, IOException {
        Locale newLocale = new Locale("es", "ES");
        BaseSettings newBaseSettings = baseSettings.with(newLocale);
        assertNotSame(Locale.getDefault(), newLocale);
        assertEquals(newLocale, newBaseSettings.getLocale());
    }

    @Test
    public void testBaseSettingsWithTimeZone() throws JsonMappingException, IOException {
        TimeZone newTimeZone = TimeZone.getTimeZone("US/Eastern");
        BaseSettings newBaseSettings = baseSettings.with(newTimeZone);
        assertNotSame(TimeZone.getDefault(), newTimeZone);
        assertEquals(newTimeZone, newBaseSettings.getTimeZone());
    }

    @Test
    public void testBaseSettingsWithBase64Variant() throws JsonMappingException, IOException {
        Base64Variant newBase64Variant = Base64Variant.JAPAN;
        BaseSettings newBaseSettings = baseSettings.with(newBase64Variant);
        assertNotSame(Base64Variant.US_STANDARD, newBase64Variant);
        assertEquals(newBase64Variant, newBaseSettings.getBase64Variant());
    }

    @Test
    public void testBaseSettingsSerialization() throws JsonMappingException, IOException {
        BaseSettings baseSettingsToSerialize = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
                typeResolverBuilder, dateFormat, handlerInstantiator, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.US_STANDARD);

        String serializedBaseSettings = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(baseSettingsToSerialize);

        BaseSettings deserializedBaseSettings = objectMapper.readValue(serializedBaseSettings, BaseSettings.class);

        assertEquals(baseSettingsToSerialize, deserializedBaseSettings);
    }
}