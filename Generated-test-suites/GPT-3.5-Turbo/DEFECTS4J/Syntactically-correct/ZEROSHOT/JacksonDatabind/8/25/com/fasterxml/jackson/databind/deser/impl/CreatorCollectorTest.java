package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
  
  private CreatorCollector creatorCollector;
  
  @Before
  public void setup() {
    creatorCollector = new CreatorCollector(null, true);
  }
  
  @Test
  public void testSetDefaultCreator() {
    // Arrange
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Act
    creatorCollector.setDefaultCreator(creator);
    
    // Assert
    assertSame(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
  }
  
  @Test
  public void testAddStringCreator() {
    // Arrange
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Act
    creatorCollector.addStringCreator(creator, false);
    
    // Assert
    assertSame(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
  }
  
  @Test
  public void testAddIntCreator() {
    // Arrange
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Act
    creatorCollector.addIntCreator(creator, false);
    
    // Assert
    assertSame(creator, creatorCollector._creators[CreatorCollector.C_INT]);
  }
  
  @Test
  public void testAddLongCreator() {
    // Arrange
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Act
    creatorCollector.addLongCreator(creator, false);
    
    // Assert
    assertSame(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
  }
  
  @Test
  public void testAddDoubleCreator() {
    // Arrange
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Act
    creatorCollector.addDoubleCreator(creator, false);
    
    // Assert
    assertSame(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
  }
  
  @Test
  public void testAddBooleanCreator() {
    // Arrange
    AnnotatedWithParams creator = new AnnotatedWithParams();
    
    // Act
    creatorCollector.addBooleanCreator(creator, false);
    
    // Assert
    assertSame(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
  }
  
  @Test
  public void testAddDelegatingCreator() {
    // Arrange
    AnnotatedWithParams creator = new AnnotatedWithParams();
    CreatorProperty[] injectables = new CreatorProperty[0];
    
    // Act
    creatorCollector.addDelegatingCreator(creator, false, injectables);
    
    // Assert
    assertSame(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
    assertSame(injectables, creatorCollector._delegateArgs);
  }
  
  @Test
  public void testAddPropertyCreator() {
    // Arrange
    AnnotatedWithParams creator = new AnnotatedWithParams();
    CreatorProperty[] properties = new CreatorProperty[0];
    
    // Act
    creatorCollector.addPropertyCreator(creator, false, properties);
    
    // Assert
    assertSame(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
    assertSame(properties, creatorCollector._propertyBasedArgs);
  }
  
  @Test
  public void testAddIncompeteParameter() {
    // Arrange
    AnnotatedParameter parameter = new AnnotatedParameter();
    
    // Act
    creatorCollector.addIncompeteParameter(parameter);
    
    // Assert
    assertSame(parameter, creatorCollector._incompleteParameter);
  }

}