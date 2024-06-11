package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(creator);

        // assert that the creator is set as the default creator
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, false);

        // assert that the creator is added to the correct index
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(creator, false);

        // assert that the creator is added to the correct index
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(creator, false);

        // assert that the creator is added to the correct index
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(creator, false);

        // assert that the creator is added to the correct index
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(creator, false);

        // assert that the creator is added to the correct index
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[0];
        creatorCollector.addDelegatingCreator(creator, false, injectables);

        // assert that the creator is added to the correct index
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        // assert that the injectables are correctly assigned
        assertSame(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[0];
        creatorCollector.addPropertyCreator(creator, false, properties);

        // assert that the creator is added to the correct index
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        // assert that the properties are correctly assigned
        assertSame(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        creatorCollector.addIncompeteParameter(parameter);

        // assert that the parameter is correctly assigned
        assertSame(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);

        assertFalse(creatorCollector.hasDefaultCreator());

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(creator);
        
        assertTrue(creatorCollector.hasDefaultCreator());
    }
}