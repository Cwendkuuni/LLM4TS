package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
  
  private CreatorCollector creatorCollector;
  
  @Before
  public void setUp() {
    creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
  }
  
  @Test
  public void testSetDefaultCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.setDefaultCreator(creator);
    AnnotatedWithParams[] creators = creatorCollector.getCreators();
    assertEquals(creator, creators[0]);
  }
  
  @Test
  public void testAddStringCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addStringCreator(creator, true);
    AnnotatedWithParams[] creators = creatorCollector.getCreators();
    assertEquals(creator, creators[1]);
  }
  
  @Test
  public void testAddIntCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addIntCreator(creator, true);
    AnnotatedWithParams[] creators = creatorCollector.getCreators();
    assertEquals(creator, creators[2]);
  }
  
  @Test
  public void testAddLongCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addLongCreator(creator, true);
    AnnotatedWithParams[] creators = creatorCollector.getCreators();
    assertEquals(creator, creators[3]);
  }
  
  @Test
  public void testAddDoubleCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addDoubleCreator(creator, true);
    AnnotatedWithParams[] creators = creatorCollector.getCreators();
    assertEquals(creator, creators[4]);
  }
  
  @Test
  public void testAddBooleanCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.addBooleanCreator(creator, true);
    AnnotatedWithParams[] creators = creatorCollector.getCreators();
    assertEquals(creator, creators[5]);
  }
  
  @Test
  public void testAddDelegatingCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] injectables = new SettableBeanProperty[1];
    creatorCollector.addDelegatingCreator(creator, true, injectables);
    AnnotatedWithParams[] creators = creatorCollector.getCreators();
    assertEquals(creator, creators[6]);
    assertEquals(injectables, creatorCollector.getDelegateArgs());
  }
  
  @Test
  public void testAddPropertyCreator() {
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] properties = new SettableBeanProperty[1];
    creatorCollector.addPropertyCreator(creator, true, properties);
    AnnotatedWithParams[] creators = creatorCollector.getCreators();
    assertEquals(creator, creators[7]);
    assertEquals(properties, creatorCollector.getPropertyBasedArgs());
  }
  
  @Test
  public void testAddIncompeteParameter() {
    AnnotatedParameter parameter = new AnnotatedParameter();
    creatorCollector.addIncompeteParameter(parameter);
    assertEquals(parameter, creatorCollector.getIncompleteParameter());
  }
  
  @Test
  public void testHasDefaultCreator() {
    assertFalse(creatorCollector.hasDefaultCreator());
    AnnotatedWithParams creator = new AnnotatedWithParams();
    creatorCollector.setDefaultCreator(creator);
    assertTrue(creatorCollector.hasDefaultCreator());
  }
  
  @Test
  public void testHasDelegatingCreator() {
    assertFalse(creatorCollector.hasDelegatingCreator());
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] injectables = new SettableBeanProperty[1];
    creatorCollector.addDelegatingCreator(creator, true, injectables);
    assertTrue(creatorCollector.hasDelegatingCreator());
  }
  
  @Test
  public void testHasPropertyBasedCreator() {
    assertFalse(creatorCollector.hasPropertyBasedCreator());
    AnnotatedWithParams creator = new AnnotatedWithParams();
    SettableBeanProperty[] properties = new SettableBeanProperty[1];
    creatorCollector.addPropertyCreator(creator, true, properties);
    assertTrue(creatorCollector.hasPropertyBasedCreator());
  }
}