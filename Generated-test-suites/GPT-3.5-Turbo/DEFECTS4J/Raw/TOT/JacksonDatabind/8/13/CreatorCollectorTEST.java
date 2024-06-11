package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() {
        // Arrange
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams defaultCreator = new AnnotatedWithParams();
        
        // Act
        creatorCollector.setDefaultCreator(defaultCreator);
        
        // Assert
        assertSame(defaultCreator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }
    
    @Test
    public void testStringCreator() {
        // Arrange
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams();
        
        // Act
        creatorCollector.addStringCreator(stringCreator, true);
        
        // Assert
        assertSame(stringCreator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }
    
    @Test
    public void testIntCreator() {
        // Arrange
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams intCreator = new AnnotatedWithParams();
        
        // Act
        creatorCollector.addIntCreator(intCreator, true);
        
        // Assert
        assertSame(intCreator, creatorCollector._creators[CreatorCollector.C_INT]);
    }
    
    @Test
    public void testLongCreator() {
        // Arrange
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams longCreator = new AnnotatedWithParams();
        
        // Act
        creatorCollector.addLongCreator(longCreator, true);
        
        // Assert
        assertSame(longCreator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }
    
    @Test
    public void testDoubleCreator() {
        // Arrange
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams();
        
        // Act
        creatorCollector.addDoubleCreator(doubleCreator, true);
        
        // Assert
        assertSame(doubleCreator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }
    
    @Test
    public void testBooleanCreator() {
        // Arrange
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams();
        
        // Act
        creatorCollector.addBooleanCreator(booleanCreator, true);
        
        // Assert
        assertSame(booleanCreator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }
    
    @Test
    public void testDelegatingCreator() {
        // Arrange
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams delegatingCreator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[1];
        
        // Act
        creatorCollector.addDelegatingCreator(delegatingCreator, true, injectables);
        
        // Assert
        assertSame(delegatingCreator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertSame(injectables, creatorCollector._delegateArgs);
    }
    
    @Test
    public void testPropertyCreator() {
        // Arrange
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[1];
        
        // Act
        creatorCollector.addPropertyCreator(propertyCreator, true, properties);
        
        // Assert
        assertSame(propertyCreator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertSame(properties, creatorCollector._propertyBasedArgs);
    }
    
    @Test
    public void testIncompeteParameter() {
        // Arrange
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedParameter incompleteParameter = new AnnotatedParameter();
        
        // Act
        creatorCollector.addIncompeteParameter(incompleteParameter);
        
        // Assert
        assertSame(incompleteParameter, creatorCollector._incompleteParameter);
    }
}