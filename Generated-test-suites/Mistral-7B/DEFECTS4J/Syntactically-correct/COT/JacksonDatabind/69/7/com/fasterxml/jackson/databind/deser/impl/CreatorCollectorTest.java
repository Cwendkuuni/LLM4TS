package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.AnnotatedWithParams;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CreatorCollectorTest {

    private static final int COUNT = 5;
    private static final String TYPE_DESC = "typeDescription";
    private static final String TYPE_NAME = "typeName";
    private static final String CONSTRUCTOR_NAME = "constructorName";
    private static final String PARAMETER_NAME = "parameterName";
    private static final String IS_EXPLICIT = "isExplicit";
    private static final String IS_INJECTABLE = "isInjectable";
    private static final String IS_DELEGATE = "isDelegate";
    private static final String IS_PROPERTY_BASED = "isPropertyBased";
    private static final String IS_ARRAY_DELEGATE = "isArrayDelegate";

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private DeserializationConfig deserializationConfig;
    @Mock
    private AnnotationIntrospector annotationIntrospector;
    @Mock
    private BeanDescription beanDescription;
    @Mock
    private MapperConfig<?> mapperConfig;
    @Mock
    private AnnotatedClass annotatedClass;
    @Mock
    private AnnotatedMember annotatedMember;
    @Mock
    private AnnotatedWithParams creator;
    @Mock
    private CreatorProperty creatorProperty;
    @Mock
    private SettableBeanProperty settableBeanProperty;
    @Mock
    private BeanPropertyDefinition beanPropertyDefinition;
    @Mock
    private PropertyName propertyName;

    private CreatorCollector creatorCollector;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        creatorCollector = new CreatorCollector(beanDescription, mapperConfig);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        givenDefaultCreator();
        whenCreatingValueInstantiator();
        thenDefaultCreatorUsed();
    }

    @Test
    public void testStringCreator() throws Exception {
        givenStringCreator();
        whenCreatingValueInstantiator();
        thenStringCreatorUsed();
    }

    @Test
    public void testIntCreator() throws Exception {
        givenIntCreator();
        whenCreatingValueInstantiator();
        thenIntCreatorUsed();
    }

    @Test
    public void testLongCreator() throws Exception {
        givenLongCreator();
        whenCreatingValueInstantiator();
        thenLongCreatorUsed();
    }

    @Test
    public void testDoubleCreator() throws Exception {
        givenDoubleCreator();
        whenCreatingValueInstantiator();
        thenDoubleCreatorUsed();
    }

    @Test
    public void testBooleanCreator() throws Exception {
        givenBooleanCreator();
        whenCreatingValueInstantiator();
        thenBooleanCreatorUsed();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateDefaultCreator() throws Exception {
        givenDefaultCreator();
        givenDefaultCreator();
        creatorCollector.setDefaultCreator(creator);
        creatorCollector.setDefaultCreator(creator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateStringCreator() throws Exception {
        givenStringCreator();
        givenStringCreator();
        creatorCollector.addStringCreator(creator, false);
        creatorCollector.addStringCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateIntCreator() throws Exception {
        givenIntCreator();
        givenIntCreator();
        creatorCollector.addIntCreator(creator, false);
        creatorCollector.addIntCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateLongCreator() throws Exception {
        givenLongCreator();
        givenLongCreator();
        creatorCollector.addLongCreator(creator, false);
        creatorCollector.addLongCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateDoubleCreator() throws Exception {
        givenDoubleCreator();
        givenDoubleCreator();
        creatorCollector.addDoubleCreator(creator, false);
        creatorCollector.addDoubleCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateBooleanCreator() throws Exception {
        givenBooleanCreator();
        givenBooleanCreator();
        creatorCollector.addBooleanCreator(creator, false);
        creatorCollector.addBooleanCreator(creator, false);
    }

    @Test
    public void testDelegatingCreator() throws Exception {
        givenDelegatingCreator();
        whenCreatingValueInstantiator();
        thenDelegatingCreatorUsed();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateDelegatingCreator() throws Exception {
        givenDelegatingCreator();
        givenDelegatingCreator();
        creatorCollector.addDelegatingCreator(creator, false, new SettableBeanProperty[] { settableBeanProperty });
        creatorCollector.addDelegatingCreator(creator, false, new SettableBeanProperty[] { settableBeanProperty });
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        givenPropertyBasedCreator();
        whenCreatingValueInstantiator();
        thenPropertyBasedCreatorUsed();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatePropertyBasedCreator() throws Exception {
        givenPropertyBasedCreator();
        givenPropertyBasedCreator();
        creatorCollector.addPropertyCreator(creator, false, new SettableBeanProperty[] { settableBeanProperty });
        creatorCollector.addPropertyCreator(creator, false, new SettableBeanProperty[] { settableBeanProperty });
    }

    private void givenDefaultCreator() {
        givenCreator(creator, null, null, false, false, null, null, false, null, null, null, null, false, null);
    }

    private void givenStringCreator() {
        givenCreator(creator, propertyName, new Class[0], true, false, null, null, false, null, null, null, null, false, null);
    }

    private void givenIntCreator() {
        givenCreator(creator, propertyName, new Class[] { int.class }, true, false, null, null, false, null, null, null, null, false, null);
    }

    private void givenLongCreator() {
        givenCreator(creator, propertyName, new Class[] { long.class }, true, false, null, null, false, null, null, null, null, false, null);
    }

    private void givenDoubleCreator() {
        givenCreator(creator, propertyName, new Class[] { double.class }, true, false, null, null, false, null, null, null, null, false, null);
    }

    private void givenBooleanCreator() {
        givenCreator(creator, propertyName, new Class[] { boolean.class }, true, false, null, null, false, null, null, null, null, false, null);
    }

    private void givenDelegatingCreator() {
        givenCreator(creator, null, new Class[] { Collection.class }, false, false, null, null, true, settableBeanProperty, null, null, null, false, null);
    }

    private void givenPropertyBasedCreator() {
        givenCreator(creator, null, new Class[0], false, false, null, new SettableBeanProperty[] { settableBeanProperty }, false, null, null, null, null, false, null);
    }

    private void givenCreator(AnnotatedWithParams creator, PropertyName propertyName, Class<?>[] parameterTypes, boolean isExplicit, boolean isInjectable,
                             AnnotatedMember annotatedMember, BeanPropertyDefinition beanPropertyDefinition, boolean isDelegate,
                             SettableBeanProperty[] delegateArgs, SettableBeanProperty[] propertyBasedArgs, CreatorProperty[] creatorProperties,
                             boolean isArrayDelegate, String typeDescription, boolean isPropertyBased) {
        when(annotationIntrospector.findCreatorProperties(beanDescription)).thenReturn(creatorProperties);
        when(creatorProperties[0].getCreator()).thenReturn(creator);
        when(creator.getParameterAnnotations(0)).thenReturn(new Annotation[0]);
        when(creator.getParameterTypes()).thenReturn(parameterTypes);
        when(creator.getRawParameterType(0)).thenReturn(annotatedMember.getRawType());
        when(beanPropertyDefinition.getName()).thenReturn(propertyName);
        when(annotatedMember.getName()).thenReturn(PROPERTY_NAME);
        when(annotatedClass.getName()).thenReturn(TYPE_NAME);
        when(beanDescription.getType()).thenReturn(JavaType.construct(TYPE_NAME, new TypeFactory(objectMapper.getConfig()).findValueTypeCompatibleWith(JavaType.construct(Object.class))));
        when(beanDescription.hasCreatorAnnotations()).thenReturn(true);
        when(mapperConfig.canOverrideAccessModifiers()).thenReturn(true);
        when(mapperConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(false);
    }

    private void whenCreatingValueInstantiator() throws Exception {
        creatorCollector.setDefaultCreator(creator);
        creatorCollector.constructValueInstantiator(deserializationConfig);
    }

    private void thenDefaultCreatorUsed() throws Exception {
        verify(deserializationConfig).addValueInstantiator(eq(creatorCollector.constructValueInstantiator(deserializationConfig)));
        verifyNoMoreInteractions(deserializationConfig);
    }

    private void thenStringCreatorUsed() throws Exception {
        verify(deserializationConfig).addValueInstantiator(eq(creatorCollector.constructValueInstantiator(deserializationConfig)));
        verify(creatorCollector.constructValueInstantiator(deserializationConfig)).configureFromStringCreator(eq(creator));
        verifyNoMoreInteractions(deserializationConfig);
    }

    private void thenIntCreatorUsed() throws Exception {
        verify(deserializationConfig).addValueInstantiator(eq(creatorCollector.constructValueInstantiator(deserializationConfig)));
        verify(creatorCollector.constructValueInstantiator(deserializationConfig)).configureFromIntCreator(eq(creator));
        verifyNoMoreInteractions(deserializationConfig);
    }

    private void thenLongCreatorUsed() throws Exception {
        verify(deserializationConfig).addValueInstantiator(eq(creatorCollector.constructValueInstantiator(deserializationConfig)));
        verify(creatorCollector.constructValueInstantiator(deserializationConfig)).configureFromLongCreator(eq(creator));
        verifyNoMoreInteractions(deserializationConfig);
    }

    private void thenDoubleCreatorUsed() throws Exception {
        verify(deserializationConfig).addValueInstantiator(eq(creatorCollector.constructValueInstantiator(deserializationConfig)));
        verify(creatorCollector.constructValueInstantiator(deserializationConfig)).configureFromDoubleCreator(eq(creator));
        verifyNoMoreInteractions(deserializationConfig);
    }

    private void thenBooleanCreatorUsed() throws Exception {
        verify(deserializationConfig).addValueInstantiator(eq(creatorCollector.constructValueInstantiator(deserializationConfig)));
        verify(creatorCollector.constructValueInstantiator(deserializationConfig)).configureFromBooleanCreator(eq(creator));
        verifyNoMoreInteractions(deserializationConfig);
    }

    private void thenDelegatingCreatorUsed() throws Exception {
        verify(deserializationConfig).addValueInstantiator(eq(creatorCollector.constructValueInstantiator(deserializationConfig)));
        verify(creatorCollector.constructValueInstantiator(deserializationConfig)).configureFromArraySettings(eq(_creators[C_ARRAY_DELEGATE]), eq(JavaType.construct(TYPE_NAME, new TypeFactory(objectMapper.getConfig()).findValueTypeCompatibleWith(JavaType.construct(Collection.class)))), eq(_arrayDelegateArgs));
        verifyNoMoreInteractions(deserializationConfig);
    }

    private void thenPropertyBasedCreatorUsed() throws Exception {
        verify(deserializationConfig).addValueInstantiator(eq(creatorCollector.constructValueInstantiator(deserializationConfig)));
        verify(creatorCollector.constructValueInstantiator(deserializationConfig)).configureFromObjectSettings(_creators[C_DEFAULT], _creators[C_DELEGATE], null, _delegateArgs, _creators[C_PROPS], _propertyBasedArgs);
        verifyNoMoreInteractions(deserializationConfig);
    }
}