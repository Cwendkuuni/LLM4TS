package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class CreatorCollectorTest {

  @Test
  public void testConstructValueInstantiator() {
    // create BeanDescription and DeserializationConfig mock objects
    BeanDescription beanDesc = mock(BeanDescription.class);
    DeserializationConfig config = mock(DeserializationConfig.class);
    
    // create CreatorCollector object
    CreatorCollector collector = new CreatorCollector(beanDesc, true);

    // set mock creators for testing
    AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
    AnnotatedWithParams stringCreator = mock(AnnotatedWithParams.class);
    AnnotatedWithParams intCreator = mock(AnnotatedWithParams.class);
    AnnotatedWithParams longCreator = mock(AnnotatedWithParams.class);
    AnnotatedWithParams doubleCreator = mock(AnnotatedWithParams.class);
    AnnotatedWithParams booleanCreator = mock(AnnotatedWithParams.class);
    AnnotatedWithParams delegateCreator = mock(AnnotatedWithParams.class);
    AnnotatedWithParams propsCreator = mock(AnnotatedWithParams.class);
    CreatorProperty[] delegateArgs = new CreatorProperty[] {mock(CreatorProperty.class)};
    CreatorProperty[] propertyArgs = new CreatorProperty[] {mock(CreatorProperty.class)};
    AnnotatedParameter incompleteParameter = mock(AnnotatedParameter.class);
    collector.setDefaultCreator(defaultCreator);
    collector.addStringCreator(stringCreator, true);
    collector.addIntCreator(intCreator, true);
    collector.addLongCreator(longCreator, true);
    collector.addDoubleCreator(doubleCreator, true);
    collector.addBooleanCreator(booleanCreator, true);
    collector.addDelegatingCreator(delegateCreator, true, delegateArgs);
    collector.addPropertyCreator(propsCreator, true, propertyArgs);
    collector.addIncompeteParameter(incompleteParameter);

    // call constructValueInstantiator method
    ValueInstantiator instantiator = collector.constructValueInstantiator(config);

    // check if the correct instance of ValueInstantiator is returned
    assertTrue(instantiator instanceof StdValueInstantiator);
    StdValueInstantiator stdInstantiator = (StdValueInstantiator) instantiator;

    // check if the correct values are set in the StdValueInstantiator object
    assertEquals(defaultCreator, stdInstantiator.getDefaultCreator());
    assertEquals(delegateCreator, stdInstantiator.getDelegatingCreator());
    assertArrayEquals(delegateArgs, stdInstantiator.getDelegateArgs());
    assertEquals(propsCreator, stdInstantiator.getPropertyBasedCreator());
    assertArrayEquals(propertyArgs, stdInstantiator.getPropertyBasedArgs());
    assertEquals(incompleteParameter, stdInstantiator.getIncompleteParameter());
  }
}