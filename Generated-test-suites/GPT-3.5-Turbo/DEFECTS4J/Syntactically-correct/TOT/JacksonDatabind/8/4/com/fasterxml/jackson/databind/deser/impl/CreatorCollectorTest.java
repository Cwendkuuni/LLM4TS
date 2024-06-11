package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        DeserializationConfig config = new DeserializationConfig();
        
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        
        assertNotNull(valueInstantiator);
        assertEquals("com.fasterxml.jackson.databind.deser.std.StdValueInstantiator", valueInstantiator.getClass().getName());
    }
    
    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        
        creatorCollector.setDefaultCreator(creator);
        
        assertNotNull(creatorCollector._creators[0]);
        assertEquals(creator, creatorCollector._creators[0]);
    }
    
    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        
        creatorCollector.addStringCreator(creator, false);
        
        assertNotNull(creatorCollector._creators[1]);
        assertEquals(creator, creatorCollector._creators[1]);
    }
    
    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        
        creatorCollector.addIntCreator(creator, false);
        
        assertNotNull(creatorCollector._creators[2]);
        assertEquals(creator, creatorCollector._creators[2]);
    }
    
    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        
        creatorCollector.addLongCreator(creator, false);
        
        assertNotNull(creatorCollector._creators[3]);
        assertEquals(creator, creatorCollector._creators[3]);
    }
    
    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        
        creatorCollector.addDoubleCreator(creator, false);
        
        assertNotNull(creatorCollector._creators[4]);
        assertEquals(creator, creatorCollector._creators[4]);
    }
    
    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        
        creatorCollector.addBooleanCreator(creator, false);
        
        assertNotNull(creatorCollector._creators[5]);
        assertEquals(creator, creatorCollector._creators[5]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[1];
        
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        
        assertNotNull(creatorCollector._creators[6]);
        assertEquals(creator, creatorCollector._creators[6]);
        assertNotNull(creatorCollector._delegateArgs);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }
    
    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[1];
        
        creatorCollector.addPropertyCreator(creator, false, properties);
        
        assertNotNull(creatorCollector._creators[7]);
        assertEquals(creator, creatorCollector._creators[7]);
        assertNotNull(creatorCollector._propertyBasedArgs);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedParameter parameter = new AnnotatedParameter();
        
        creatorCollector.addIncompeteParameter(parameter);
        
        assertNotNull(creatorCollector._incompleteParameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

}