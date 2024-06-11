package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    private static final CreatorCollector cc;
    private static final BeanDescription beanDesc;
    private static final boolean canFixAccess;

    static {
        NopAnnotationIntrospector introspector = new NopAnnotationIntrospector();
        try {
            Method constructor = CreatorCollector.class.getDeclaredConstructor(BeanDescription.class, boolean.class);
            constructor.setAccessible(true);
            cc = (CreatorCollector) constructor.newInstance(introspector.findBeanDescription(Object[].class), false);
            beanDesc = introspector.findBeanDescription(Object[].class);
            canFixAccess = true;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Before
    public void setUp() {
        // Set up the CreatorCollector instance for testing
    }

    @Test
    public void testConstructValueInstantiator() {
        ValueInstantiator instantiator = cc.constructValueInstantiator(null);
        assertNotNull(instantiator);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructValueInstantiator_NullBeanDescription() {
        cc.constructValueInstantiator(null);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.setDefaultCreator(creator);
        assertEquals(creator, cc._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testSetDefaultCreator_NullCreator() {
        cc.setDefaultCreator(null);
        assertNull(cc._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addStringCreator(creator, false);
        assertEquals(creator, cc._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddStringCreator_Explicit() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addStringCreator(creator, true);
        assertEquals(creator, cc._creators[CreatorCollector.C_STRING]);
        assertTrue(cc._explicitCreators > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddStringCreator_Duplicate() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addStringCreator(creator, false);
        cc.addStringCreator(creator, false);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addIntCreator(creator, false);
        assertEquals(creator, cc._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddIntCreator_Explicit() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addIntCreator(creator, true);
        assertEquals(creator, cc._creators[CreatorCollector.C_INT]);
        assertTrue(cc._explicitCreators > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIntCreator_Duplicate() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addIntCreator(creator, false);
        cc.addIntCreator(creator, false);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addLongCreator(creator, false);
        assertEquals(creator, cc._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddLongCreator_Explicit() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addLongCreator(creator, true);
        assertEquals(creator, cc._creators[CreatorCollector.C_LONG]);
        assertTrue(cc._explicitCreators > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLongCreator_Duplicate() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addLongCreator(creator, false);
        cc.addLongCreator(creator, false);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addDoubleCreator(creator, false);
        assertEquals(creator, cc._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddDoubleCreator_Explicit() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addDoubleCreator(creator, true);
        assertEquals(creator, cc._creators[CreatorCollector.C_DOUBLE]);
        assertTrue(cc._explicitCreators > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDoubleCreator_Duplicate() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addDoubleCreator(creator, false);
        cc.addDoubleCreator(creator, false);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addBooleanCreator(creator, false);
        assertEquals(creator, cc._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddBooleanCreator_Explicit() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addBooleanCreator(creator, true);
        assertEquals(creator, cc._creators[CreatorCollector.C_BOOLEAN]);
        assertTrue(cc._explicitCreators > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBooleanCreator_Duplicate() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        cc.addBooleanCreator(creator, false);
        cc.addBooleanCreator(creator, false);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        cc.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, cc._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, cc._delegateArgs);
    }

    @Test
    public void testAddDelegatingCreator_Explicit() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        cc.addDelegatingCreator(creator, true, injectables);
        assertEquals(creator, cc._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, cc._delegateArgs);
        assertTrue(cc._explicitCreators > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDelegatingCreator_Duplicate() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        cc.addDelegatingCreator(creator, false, injectables);
        cc.addDelegatingCreator(creator, false, injectables);
    }

    @Test
    public void testAddPropertyCreator() {
        BeanPropertyDefinition propertyDefinition = mock(BeanPropertyDefinition.class);
        CreatorProperty[] properties = new CreatorProperty[1];
        properties[0] = new CreatorProperty(propertyDefinition, null);
        cc.addPropertyCreator(null, false, properties);
        assertEquals(properties, cc._propertyBasedArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyCreator_NullProperties() {
        cc.addPropertyCreator(null, false, (CreatorProperty[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyCreator_DuplicateName() {
        BeanPropertyDefinition propertyDefinition1 = mock(BeanPropertyDefinition.class);
        CreatorProperty property1 = new CreatorProperty(propertyDefinition1, null);
        BeanPropertyDefinition propertyDefinition2 = mock(BeanPropertyDefinition.class);
        CreatorProperty property2 = new CreatorProperty(propertyDefinition2, null);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = property1;
        properties[1] = property2;
        cc.addPropertyCreator(null, false, properties);
    }

    @Test
    public void testAddIncompleteParameter() {
        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        cc.addIncompleteParameter(parameter);
        assertEquals(parameter, cc._incompleteParameter);
    }

    @Test
    public void testCanFixAccess() {
        assertFalse(cc._canFixAccess);
        BeanDescription beanDescWithCanFixAccess = new BeanDescription(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false);
        CreatorCollector ccWithCanFixAccess = new CreatorCollector(beanDescWithCanFixAccess, true);
        assertTrue(ccWithCanFixAccess._canFixAccess);
    }
}