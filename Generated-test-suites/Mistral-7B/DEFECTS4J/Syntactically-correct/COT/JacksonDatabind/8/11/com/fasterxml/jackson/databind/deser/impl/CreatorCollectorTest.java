package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.AnnotatedWithParams;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private DeserializationConfig config;
    private BeanDescription beanDescription;

    @Before
    public void setUp() {
        config = mock(DeserializationConfig.class);
        beanDescription = mock(BeanDescription.class);
        creatorCollector = new CreatorCollector(beanDescription, true);
    }

    @Test
    public void testConstructValueInstantiator_NoCustomCreators() throws IOException {
        JavaType type = Mockito.mock(JavaType.class);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertTrue(inst instanceof StdValueInstantiator);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(creator);
        AnnotatedWithParams defaultCreator = creatorCollector.getCreator(CreatorCollector.C_DEFAULT);
        assertEquals(creator, defaultCreator);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, false);
        AnnotatedWithParams stringCreator = creatorCollector.getCreator(CreatorCollector.C_STRING);
        assertEquals(creator, stringCreator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateStringCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, false);
        creatorCollector.addStringCreator(creator, false);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(creator, false);
        AnnotatedWithParams intCreator = creatorCollector.getCreator(CreatorCollector.C_INT);
        assertEquals(creator, intCreator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateIntCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(creator, false);
        creatorCollector.addIntCreator(creator, false);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(creator, false);
        AnnotatedWithParams longCreator = creatorCollector.getCreator(CreatorCollector.C_LONG);
        assertEquals(creator, longCreator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateLongCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(creator, false);
        creatorCollector.addLongCreator(creator, false);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(creator, false);
        AnnotatedWithParams doubleCreator = creatorCollector.getCreator(CreatorCollector.C_DOUBLE);
        assertEquals(creator, doubleCreator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateDoubleCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(creator, false);
        creatorCollector.addDoubleCreator(creator, false);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(creator, false);
        AnnotatedWithParams booleanCreator = creatorCollector.getCreator(CreatorCollector.C_BOOLEAN);
        assertEquals(creator, booleanCreator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateBooleanCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(creator, false);
        creatorCollector.addBooleanCreator(creator, false);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        CreatorProperty[] delegateArgs = creatorCollector.getDelegatingCreatorArgs();
        assertEquals(injectables.length, delegateArgs.length);
        for (int i = 0; i < injectables.length; i++) {
            assertEquals(injectables[i], delegateArgs[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateDelegatingCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        creatorCollector.addDelegatingCreator(creator, false, injectables);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = mock(CreatorProperty.class);
        properties[1] = mock(CreatorProperty.class);
        creatorCollector.addPropertyCreator(creator, false, properties);
        CreatorProperty[] propertyBasedArgs = creatorCollector.getPropertyBasedArgs();
        assertEquals(properties.length, propertyBasedArgs.length);
        for (int i = 0; i < properties.length; i++) {
            assertEquals(properties[i], propertyBasedArgs[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicatePropertyCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = mock(CreatorProperty.class);
        properties[1] = mock(CreatorProperty.class);
        creatorCollector.addPropertyCreator(creator, false, properties);
        creatorCollector.addPropertyCreator(creator, false, properties);
    }

    @Test
    public void testAddIncompleteParameter() {
        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        creatorCollector.addIncompleteParameter(parameter);
        AnnotatedParameter incompleteParameter = creatorCollector.getIncompleteParameter();
        assertEquals(parameter, incompleteParameter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateIncompleteParameter() {
        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        creatorCollector.addIncompleteParameter(parameter);
        creatorCollector.addIncompleteParameter(parameter);
    }

    @Test
    public void testGetCreator_DefaultCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(creator);
        AnnotatedWithParams result = creatorCollector.getCreator(CreatorCollector.C_DEFAULT);
        assertEquals(creator, result);
    }

    @Test
    public void testGetCreator_StringCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, false);
        AnnotatedWithParams result = creatorCollector.getCreator(CreatorCollector.C_STRING);
        assertEquals(creator, result);
    }

    @Test
    public void testGetCreator_IntCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(creator, false);
        AnnotatedWithParams result = creatorCollector.getCreator(CreatorCollector.C_INT);
        assertEquals(creator, result);
    }

    @Test
    public void testGetCreator_LongCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(creator, false);
        AnnotatedWithParams result = creatorCollector.getCreator(CreatorCollector.C_LONG);
        assertEquals(creator, result);
    }

    @Test
    public void testGetCreator_DoubleCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(creator, false);
        AnnotatedWithParams result = creatorCollector.getCreator(CreatorCollector.C_DOUBLE);
        assertEquals(creator, result);
    }

    @Test
    public void testGetCreator_BooleanCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(creator, false);
        AnnotatedWithParams result = creatorCollector.getCreator(CreatorCollector.C_BOOLEAN);
        assertEquals(creator, result);
    }

    @Test
    public void testGetCreator_DelegateCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        AnnotatedWithParams result = creatorCollector.getCreator(CreatorCollector.C_DELEGATE);
        assertEquals(creator, result);
    }

    @Test
    public void testGetCreator_PropertyBasedCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = mock(CreatorProperty.class);
        creatorCollector.addPropertyCreator(creator, false, properties);
        AnnotatedWithParams result = creatorCollector.getCreator(CreatorCollector.C_PROPS);
        assertEquals(creator, result);
    }

    @Test
    public void testGetDelegatingCreatorArgs() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[2];
        injectables[0] = mock(CreatorProperty.class);
        injectables[1] = mock(CreatorProperty.class);
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        CreatorProperty[] result = creatorCollector.getDelegatingCreatorArgs();
        assertEquals(injectables.length, result.length);
        for (int i = 0; i < injectables.length; i++) {
            assertEquals(injectables[i], result[i]);
        }
    }

    @Test
    public void testGetPropertyBasedArgs() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = mock(CreatorProperty.class);
        properties[1] = mock(CreatorProperty.class);
        creatorCollector.addPropertyCreator(creator, false, properties);
        CreatorProperty[] result = creatorCollector.getPropertyBasedArgs();
        assertEquals(properties.length, result.length);
        for (int i = 0; i < properties.length; i++) {
            assertEquals(properties[i], result[i]);
        }
    }

    @Test
    public void testGetIncompleteParameter() {
        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        creatorCollector.addIncompleteParameter(parameter);
        AnnotatedParameter result = creatorCollector.getIncompleteParameter();
        assertEquals(parameter, result);
    }

    @Test
    public void testConstructValueInstantiator_WithCustomCreators() throws IOException {
        JavaType delegateType = mock(JavaType.class);
        AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] delegateInjectables = new CreatorProperty[1];
        delegateInjectables[0] = mock(CreatorProperty.class);
        creatorCollector.addDelegatingCreator(delegateCreator, true, delegateInjectables);

        JavaType type = Mockito.mock(JavaType.class);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertTrue(inst instanceof StdValueInstantiator);
        when(inst.getClass()).thenReturn(StdValueInstantiator.class);
        when(inst.getDelegateInstance()).thenReturn(delegateCreator);
        when(delegateCreator.getGenericParameterType(0)).thenReturn(delegateType);
        when(delegateType.getRawClass()).thenReturn(Mockito.mock(Class.class));

        assertEquals(delegateCreator, creatorCollector.getCreator(CreatorCollector.C_DELEGATE));
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructValueInstantiator_WithNullType() throws IOException {
        creatorCollector.constructValueInstantiator(config);
        fail("Expected IllegalStateException");
    }
}