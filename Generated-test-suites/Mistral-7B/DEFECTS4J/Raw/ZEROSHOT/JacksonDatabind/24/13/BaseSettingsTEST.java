package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.time.ZoneId;
import java.util.Locale;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest extends MockObjectTestCase {

private BaseSettings baseSettings;

@Override
protected void setUp() throws Exception {
super.setUp();

ClassIntrospector classIntrospectorMock = mock(ClassIntrospector.class, ClassImposteriser.INSTANCE);
AnnotationIntrospector annotationIntrospectorMock = mock(AnnotationIntrospector.class, ClassImposteriser.INSTANCE);
VisibilityChecker<?> visibilityCheckerMock = mock(VisibilityChecker.class, ClassImposteriser.INSTANCE);
PropertyNamingStrategy propertyNamingStrategyMock = mock(PropertyNamingStrategy.class, ClassImposteriser.INSTANCE);
TypeFactory typeFactoryMock = mock(TypeFactory.class, ClassImposteriser.INSTANCE);
TypeResolverBuilder<?> typeResolverBuilderMock = mock(TypeResolverBuilder.class, ClassImposteriser.INSTANCE);
DateFormat dateFormatMock = new StdDateFormat();
HandlerInstantiator handlerInstantiatorMock = mock(HandlerInstantiator.class, ClassImposteriser.INSTANCE);
Locale localeMock = mock(Locale.class, ClassImposteriser.INSTANCE);
TimeZone timeZoneMock = mock(TimeZone.class, ClassImposteriser.INSTANCE);
Base64Variant defaultBase64 = Base64Variant.STANDARD;

baseSettings = new BaseSettings(classIntrospectorMock, annotationIntrospectorMock, visibilityCheckerMock, propertyNamingStrategyMock, typeFactoryMock,
        typeResolverBuilderMock, dateFormatMock, handlerInstantiatorMock, localeMock, timeZoneMock, defaultBase64);
}

@Test
public void testGetClassIntrospector() {
assertEquals(classIntrospectorMock, baseSettings.getClassIntrospector());
}

@Test
public void testGetAnnotationIntrospector() {
assertEquals(annotationIntrospectorMock, baseSettings.getAnnotationIntrospector());
}

@Test
public void testGetVisibilityChecker() {
assertEquals(visibilityCheckerMock, baseSettings.getVisibilityChecker());
}

@Test
public void testGetPropertyNamingStrategy() {
assertEquals(propertyNamingStrategyMock, baseSettings.getPropertyNamingStrategy());
}

@Test
public void testGetTypeFactory() {
assertEquals(typeFactoryMock, baseSettings.getTypeFactory());
}

@Test
public void testGetTypeResolverBuilder() {
assertEquals(typeResolverBuilderMock, baseSettings.getTypeResolverBuilder());
}

@Test
public void testGetDateFormat() throws JsonProcessingException {
assertEquals(dateFormatMock, baseSettings.getDateFormat());
}

@Test
public void testGetHandlerInstantiator() {
assertEquals(handlerInstantiatorMock, baseSettings.getHandlerInstantiator());
}

@Test
public void testGetLocale() {
assertEquals(localeMock, baseSettings.getLocale());
}

@Test
public void testGetTimeZone() {
assertEquals(timeZoneMock, baseSettings.getTimeZone());
}

@Test
public void testGetBase64Variant() {
assertEquals(defaultBase64, baseSettings.getBase64Variant());
}

@Test
public void testWithClassIntrospector() {
BaseSettings newBaseSettings = baseSettings.withClassIntrospector(classIntrospectorMock);
assertEquals(classIntrospectorMock, newBaseSettings.getClassIntrospector());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithAnnotationIntrospector() {
BaseSettings newBaseSettings = baseSettings.withAnnotationIntrospector(annotationIntrospectorMock);
assertEquals(annotationIntrospectorMock, newBaseSettings.getAnnotationIntrospector());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithAnnotationIntrospectorPair() {
AnnotationIntrospectorPair annotationIntrospectorPairMock = mock(AnnotationIntrospectorPair.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.withAnnotationIntrospector(annotationIntrospectorPairMock);
assertEquals(annotationIntrospectorPairMock.getFirst(), newBaseSettings.getAnnotationIntrospector());
assertEquals(annotationIntrospectorPairMock.getSecond(), newBaseSettings.getClassIntrospector());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithVisibilityChecker() {
VisibilityChecker<?> newVisibilityCheckerMock = mock(VisibilityChecker.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.withVisibilityChecker(newVisibilityCheckerMock);
assertEquals(newVisibilityCheckerMock, newBaseSettings.getVisibilityChecker());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithPropertyNamingStrategy() {
PropertyNamingStrategy newPropertyNamingStrategyMock = mock(PropertyNamingStrategy.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategyMock);
assertEquals(newPropertyNamingStrategyMock, newBaseSettings.getPropertyNamingStrategy());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithTypeFactory() {
TypeFactory newTypeFactoryMock = mock(TypeFactory.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.withTypeFactory(newTypeFactoryMock);
assertEquals(newTypeFactoryMock, newBaseSettings.getTypeFactory());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithTypeResolverBuilder() {
TypeResolverBuilder<?> newTypeResolverBuilderMock = mock(TypeResolverBuilder.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.withTypeResolverBuilder(newTypeResolverBuilderMock);
assertEquals(newTypeResolverBuilderMock, newBaseSettings.getTypeResolverBuilder());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithDateFormat() {
DateFormat newDateFormatMock = mock(DateFormat.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.withDateFormat(newDateFormatMock);
assertEquals(newDateFormatMock, newBaseSettings.getDateFormat());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithHandlerInstantiator() {
HandlerInstantiator newHandlerInstantiatorMock = mock(HandlerInstantiator.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.withHandlerInstantiator(newHandlerInstantiatorMock);
assertEquals(newHandlerInstantiatorMock, newBaseSettings.getHandlerInstantiator());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithLocale() {
Locale newLocaleMock = mock(Locale.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.with(newLocaleMock);
assertEquals(newLocaleMock, newBaseSettings.getLocale());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithTimeZone() {
TimeZone newTimeZoneMock = mock(TimeZone.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.with(newTimeZoneMock);
assertEquals(newTimeZoneMock, newBaseSettings.getTimeZone());
assertEquals(baseSettings, newBaseSettings);
}

@Test
public void testWithDateFormatAndTimeZone() {
DateFormat newDateFormatMock = mock(DateFormat.class, ClassImposteriser.INSTANCE);
TimeZone newTimeZoneMock = mock(TimeZone.class, ClassImposteriser.INSTANCE);

BaseSettings newBaseSettings = baseSettings.with(newTimeZoneMock);
assertEquals(newTimeZoneMock, newBaseSettings.getTimeZone());
assertEquals(newDateFormatMock, newBaseSettings.getDateFormat());

BaseSettings newBaseSettingsWithDateFormat = newBaseSettings.withDateFormat(newDateFormatMock);
assertEquals(newDateFormatMock, newBaseSettingsWithDateFormat.getDateFormat());
assertEquals(newTimeZoneMock, newBaseSettingsWithDateFormat.getTimeZone());
assertEquals(newBaseSettings, newBaseSettingsWithDateFormat);
}

@Test
public void testWithBase64Variant() {
Base64Variant newBase64Variant = Base64Variant.US_ASCII;

BaseSettings newBaseSettings = baseSettings.with(newBase64Variant);
assertEquals(newBase64Variant, newBaseSettings.getBase64Variant());
assertEquals(baseSettings, newBaseSettings);
}
}