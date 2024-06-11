package com.fasterxml.jackson.databind.deser.impl;// import statements go here

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        BeanDescription beanDesc = new BeanDescription();
        creatorCollector = new CreatorCollector(beanDesc, true);
    }

    

    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        creatorCollector = new CreatorCollector(new BeanDescription(), true);
        assertNull(creatorCollector.getDefaultCreator());
    }

    // Test setDefaultCreator method
    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(creator);
        assertSame(creator, creatorCollector.getDefaultCreator());
    }

    // Test addStringCreator method
    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    // Test addIntCreator method
    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    // Test addLongCreator method
    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    // Test addDoubleCreator method
    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    // Test addBooleanCreator method
    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    // Test addDelegatingCreator method
    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[5];
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertSame(injectables, creatorCollector._delegateArgs);
    }

    // Test addPropertyCreator method
    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertSame(properties, creatorCollector._propertyBasedArgs);
    }

    // Test hasDefaultCreator method
    @Test
    public void testHasDefaultCreator() {
        assertFalse(creatorCollector.hasDefaultCreator());
    }

    // Test fixAccess method
    @Test
    public void testFixAccess() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector._fixAccess(creator);
        verify(member).getClass();
        verifyNoMoreInteractions(member);
    }

    // Test verifyNonDup method with no existing creator
    @Test
    public void testVerifyNonDupNoExistingCreator() {
        AnnotatedWithParams newOne = mock(AnnotatedWithParams.class);
        creatorCollector.verifyNonDup(newOne, CreatorCollector.C_STRING, false);
    }

    // Test verifyNonDup method with existing non-explicit creator
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNonDupExistingNonExplicitCreator() {
        AnnotatedWithParams oldOne = mock(AnnotatedWithParams.class);
        AnnotatedWithParams newOne = mock(AnnotatedWithParams.class);
        creatorCollector.verifyNonDup(newOne, CreatorCollector.C_STRING, false);
    }

    // Test verifyNonDup method with existing explicit creator
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNonDupExistingExplicitCreator() {
        AnnotatedWithParams oldOne = mock(AnnotatedWithParams.class);
        AnnotatedWithParams newOne = mock(AnnotatedWithParams.class);
        creatorCollector.verifyNonDup(newOne, CreatorCollector.C_STRING, true);
    }

    // Test verifyNonDup method with existing non-explicit creator of different class
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNonDupExistingNonExplicitCreatorDifferentClass() {
        AnnotatedWithParams oldOne = mock(AnnotatedWithParams.class);
        AnnotatedWithParams newOne = mock(AnnotatedWithParams.class);
        creatorCollector.verifyNonDup(newOne, CreatorCollector.C_INT, false);
    }

    // Test verifyNonDup method with existing explicit creator of same class
    @Test
    public void testVerifyNonDupExistingExplicitCreatorSameClass() {
        AnnotatedWithParams oldOne = mock(AnnotatedWithParams.class);
        AnnotatedWithParams newOne = mock(AnnotatedWithParams.class);
        creatorCollector.verifyNonDup(newOne, CreatorCollector.C_STRING, true);
    }

    
}