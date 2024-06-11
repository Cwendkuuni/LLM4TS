package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.core.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import org.junit.*;
import org.mockito.*;

public class CreatorCollectorTest {

    @Before
    public void setUp() {
        // Set up mocks
        MapperConfig<?> configMock = mock(MapperConfig.class);
        BeanDescription beanDescriptionMock = mock(BeanDescription.class);
        JavaType typeMock = mock(JavaType.class);
        JavaType delegateTypeMock = mock(JavaType.class);
        JavaType arrayDelegateTypeMock = mock(JavaType.class);
        AnnotatedMember memberMock = mock(AnnotatedMember.class);
        AnnotatedWithParams creator1Mock = mock(AnnotatedWithParams.class);
        AnnotatedWithParams creator2Mock = mock(AnnotatedWithParams.class);
        AnnotatedParameter parameterMock = mock(AnnotatedParameter.class);
        SettableBeanProperty property1Mock = mock(SettableBeanProperty.class);
        SettableBeanProperty property2Mock = mock(SettableBeanProperty.class);
        SettableBeanProperty[] propertiesMock = new SettableBeanProperty[] { property1Mock, property2Mock };
        CreatorProperty[] injectablesMock = new CreatorProperty[] { property1Mock };
        CreatorProperty[] injectables2Mock = new CreatorProperty[] { property2Mock };
        SettableBeanProperty[] injectablesArrayMock = new SettableBeanProperty[] {};
        SettableBeanProperty[] argsMock = new SettableBeanProperty[] { property1Mock };
        SettableBeanProperty[] argsArrayMock = new SettableBeanProperty[] { property1Mock, property2Mock };
        AnnotatedWithParams defaultCreatorMock = mock(AnnotatedWithParams.class);

        when(configMock.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(false);
        when(configMock.canOverrideAccessModifiers()).thenReturn(false);
        when(beanDescriptionMock.getType()).thenReturn(typeMock);
        when(typeMock.getRawClass()).thenReturn(Object.class);
        when(delegateTypeMock.isCollectionLikeType()).thenReturn(false);
        when(arrayDelegateTypeMock.isCollectionLikeType()).thenReturn(true);
        when(creator1Mock.isAnnotationPresent(JsonCreator.class)).thenReturn(true);
        when(creator1Mock.getParameterTypes()).thenReturn(new Class[] { String.class });
        when(creator1Mock.getRawParameterType(0)).thenReturn(String.class);
        when(creator2Mock.isAnnotationPresent(JsonCreator.class)).thenReturn(true);
        when(creator2Mock.getParameterTypes()).thenReturn(new Class[] { int.class });
        when(creator2Mock.getRawParameterType(0)).thenReturn(int.class);
        when(memberMock.getAnnotated()).thenReturn(creator1Mock);
        when(memberMock.getName()).thenReturn("constructor1");
        when(creator1Mock.getParameter(0)).thenReturn(memberMock);
        when(parameterMock.getParameterName()).thenReturn("p");
        when(property1Mock.getName()).thenReturn("prop1");
        when(property1Mock.getMember()).thenReturn(memberMock);
        when(property1Mock.getMemberName()).thenReturn("property1");
        when(property1Mock.getInjectableValueId()).thenReturn(null);
    }

    @Test
    public void testCreatorCollector_DefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        AnnotatedWithParams creatorMock = mock(AnnotatedWithParams.class);
        when(creatorMock.isAnnotationPresent(JsonCreator.class)).thenReturn(false);
        when(creatorMock.getParameterTypes()).thenReturn(new Class[] {});
        when(creatorMock.getRawParameterType(0)).thenReturn(Object.class);

        creatorCollector.setDefaultCreator(creatorMock);
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(null);

        assertNotNull(instantiator);

        // TODO: Verify that the default creator is used by the instantiator
    }

    @Test
    public void testCreatorCollector_StringCreator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addStringCreator(creator1Mock, false);
        assertEquals(creator1Mock, creatorCollector.getCreator(C_STRING));

        // TODO: Verify that the String creator is used by the instantiator
    }

    @Test
    public void testCreatorCollector_IntCreator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addIntCreator(creator2Mock, false);
        assertEquals(creator2Mock, creatorCollector.getCreator(C_INT));

        // TODO: Verify that the int creator is used by the instantiator
    }

    @Test
    public void testCreatorCollector_LongCreator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addLongCreator(creator1Mock, false);
        assertEquals(creator1Mock, creatorCollector.getCreator(C_LONG));

        // TODO: Verify that the long creator is used by the instantiator
    }

    @Test
    public void testCreatorCollector_DoubleCreator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addDoubleCreator(creator1Mock, false);
        assertEquals(creator1Mock, creatorCollector.getCreator(C_DOUBLE));

        // TODO: Verify that the double creator is used by the instantiator
    }

    @Test
    public void testCreatorCollector_BooleanCreator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addBooleanCreator(creator1Mock, false);
        assertEquals(creator1Mock, creatorCollector.getCreator(C_BOOLEAN));

        // TODO: Verify that the boolean creator is used by the instantiator
    }

    @Test
    public void testCreatorCollector_DelegateCreator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addDelegatingCreator(creator1Mock, false, argsMock);
        assertEquals(creator1Mock, creatorCollector.getCreator(C_DELEGATE));

        // TODO: Verify that the delegate creator is used by the instantiator
    }

    @Test
    public void testCreatorCollector_ArrayDelegateCreator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addDelegatingCreator(creator1Mock, false, injectablesArrayMock);
        assertEquals(creator1Mock, creatorCollector.getCreator(C_ARRAY_DELEGATE));

        // TODO: Verify that the array delegate creator is used by the instantiator
    }

    @Test
    public void testCreatorCollector_PropertyBasedCreator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addPropertyCreator(creator1Mock, false, propertiesMock);
        assertEquals(creator1Mock, creatorCollector.getCreator(C_PROPS));

        // TODO: Verify that the property-based creator is used by the instantiator
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollector_DuplicateCreator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addStringCreator(creator1Mock, false);
        creatorCollector.addStringCreator(creator1Mock, false);

        // TODO: Verify that an IllegalArgumentException is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollector_ExplicitCreatorVsDefault() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addStringCreator(creator1Mock, false);
        creatorCollector.setDefaultCreator(creator1Mock);

        // TODO: Verify that an IllegalArgumentException is thrown
    }

    @Test
    public void testCreatorCollector_IncompleteParameter() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        creatorCollector.addIncompeteParameter(parameterMock);

        // TODO: Verify that the incomplete parameter is accessible
    }

    @Test
    public void testCreatorCollector_GetCreator_InvalidIndex() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescriptionMock, configMock);

        // TODO: Verify that a NullPointerException is thrown when getting a creator with an invalid index
    }

    @After
    public void tearDown() {
        reset(beanDescriptionMock, configMock, typeMock, creator1Mock, creator2Mock, memberMock, property1Mock, property2Mock);
    }
}