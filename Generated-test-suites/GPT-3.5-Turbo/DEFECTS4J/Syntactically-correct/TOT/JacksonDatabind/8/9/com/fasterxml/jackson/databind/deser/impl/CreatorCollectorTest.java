package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
        assertEquals(1, creatorCollector._explicitCreators & (1 << CreatorCollector.C_STRING));   
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, false);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
        assertEquals(0, creatorCollector._explicitCreators & (1 << CreatorCollector.C_INT));
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, true);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
        assertEquals(1, creatorCollector._explicitCreators & (1 << CreatorCollector.C_LONG));
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, true);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
        assertEquals(1, creatorCollector._explicitCreators & (1 << CreatorCollector.C_DOUBLE));
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, false);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
        assertEquals(0, creatorCollector._explicitCreators & (1 << CreatorCollector.C_BOOLEAN));
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[0];
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(1, creatorCollector._explicitCreators & (1 << CreatorCollector.C_DELEGATE));
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[0];
        creatorCollector.addPropertyCreator(creator, false, properties);
        
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(0, creatorCollector._explicitCreators & (1 << CreatorCollector.C_PROPS));
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }
}