package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[0]);

        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(annotatedWithParams);
        assertSame(annotatedWithParams, creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[1]);

        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creatorCollector.addStringCreator(annotatedWithParams, false);
        assertSame(annotatedWithParams, creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[2]);

        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creatorCollector.addIntCreator(annotatedWithParams, false);
        assertSame(annotatedWithParams, creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[3]);

        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creatorCollector.addLongCreator(annotatedWithParams, false);
        assertSame(annotatedWithParams, creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[4]);

        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(annotatedWithParams, false);
        assertSame(annotatedWithParams, creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[5]);

        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(annotatedWithParams, false);
        assertSame(annotatedWithParams, creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[6]);

        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        CreatorProperty[] creatorProperties = new CreatorProperty[0];
        creatorCollector.addDelegatingCreator(annotatedWithParams, false, creatorProperties);
        assertSame(annotatedWithParams, creatorCollector._creators[6]);

        assertSame(creatorProperties, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._creators[7]);

        AnnotatedWithParams annotatedWithParams = new AnnotatedWithParams();
        CreatorProperty[] creatorProperties = new CreatorProperty[0];
        creatorCollector.addPropertyCreator(annotatedWithParams, false, creatorProperties);
        assertSame(annotatedWithParams, creatorCollector._creators[7]);

        assertSame(creatorProperties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        assertNull(creatorCollector._incompleteParameter);

        AnnotatedParameter annotatedParameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(annotatedParameter);
        assertSame(annotatedParameter, creatorCollector._incompleteParameter);
    }
}

class AnnotatedWithParams {}
class AnnotatedParameter {}