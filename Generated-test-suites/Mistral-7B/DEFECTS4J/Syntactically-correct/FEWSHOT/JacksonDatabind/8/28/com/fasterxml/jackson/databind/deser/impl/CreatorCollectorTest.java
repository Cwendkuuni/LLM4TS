package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 BeanDescription beanDesc;
 CreatorCollector collector;
 AnnotatedWithParams creatorDefault, creatorString, creatorInt, creatorLong, creatorDouble, creatorBoolean, creatorDelegate, creatorDelegateWithArgs, creatorProperties, creatorDelegateWithProperties;
 AnnotatedParameter incompleteParameter;

 @Before
 public void setup() {
 beanDesc = new BeanDescription(null);
 collector = new CreatorCollector(beanDesc, true);
 }

 @Test
 public void testConstructValueInstantiator() {
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertNotNull(inst);
 }

 @Test
 public void testDefaultCreator() {
 creatorDefault = mock(AnnotatedWithParams.class);
 collector.setDefaultCreator(creatorDefault);
 assertTrue(collector.hasDefaultCreator());
}

 @Test
 public void testSetStringCreator() {
 creatorString = mock(AnnotatedWithParams.class);
 collector.addStringCreator(creatorString, false);
 collector.addStringCreator(creatorString, true);
 assertTrue(collector.hasCreator(AnnotatedWithParams.class, "String"));
}

 @Test
 public void testSetIntCreator() {
 creatorInt = mock(AnnotatedWithParams.class);
 collector.addIntCreator(creatorInt, false);
 collector.addIntCreator(creatorInt, true);
 assertTrue(collector.hasCreator(AnnotatedWithParams.class, "int"));
}

 @Test
 public void testSetLongCreator() {
 creatorLong = mock(AnnotatedWithParams.class);
 collector.addLongCreator(creatorLong, false);
 collector.addLongCreator(creatorLong, true);
 assertTrue(collector.hasCreator(AnnotatedWithParams.class, "long"));
}

 @Test
 public void testSetDoubleCreator() {
 creatorDouble = mock(AnnotatedWithParams.class);
 collector.addDoubleCreator(creatorDouble, false);
 collector.addDoubleCreator(creatorDouble, true);
 assertTrue(collector.hasCreator(AnnotatedWithParams.class, "double"));
}

 @Test
 public void testSetBooleanCreator() {
 creatorBoolean = mock(AnnotatedWithParams.class);
 collector.addBooleanCreator(creatorBoolean, false);
 collector.addBooleanCreator(creatorBoolean, true);
 assertTrue(collector.hasCreator(AnnotatedWithParams.class, "boolean"));
}

 @Test
 public void testSetDelegatingCreator() {
 creatorDelegate = mock(AnnotatedWithParams.class);
 CreatorProperty[] delegateArgs = new CreatorProperty[1];
 CreatorProperty delegateArg = mock(CreatorProperty.class);
 delegateArgs[0] = delegateArg;
 collector.addDelegatingCreator(creatorDelegate, false, delegateArgs);
 assertTrue(collector.hasCreator(AnnotatedWithParams.class, "delegate"));
}

 @Test
 public void testSetPropertyCreator() {
 creatorProperties = mock(AnnotatedWithParams.class);
 CreatorProperty[] propertyBasedArgs = new CreatorProperty[3];
 CreatorProperty property = mock(CreatorProperty.class);
 propertyBasedArgs[0] = property;
 CreatorProperty[] properties = new CreatorProperty[2];
 properties[0] = property;
 properties[1] = property;
 collector.addPropertyCreator(creatorProperties, false, properties);
 assertTrue(collector.hasCreator(AnnotatedWithParams.class, "property-based"));
}

 @Test
 public void testSetDelegateWithPropertyCreator() {
 creatorDelegateWithArgs = mock(AnnotatedWithParams.class);
 creatorDelegateWithProperties = mock(AnnotatedWithParams.class);
 CreatorProperty[] delegateArgs = new CreatorProperty[1];
 CreatorProperty delegateArg = mock(CreatorProperty.class);
 delegateArgs[0] = delegateArg;
 CreatorProperty[] propertyBasedArgs = new CreatorProperty[3];
 CreatorProperty property = mock(CreatorProperty.class);
 propertyBasedArgs[0] = property;
 CreatorProperty[] properties = new CreatorProperty[2];
 properties[0] = property;
 properties[1] = property;
 collector.addDelegatingCreator(creatorDelegateWithArgs, false, delegateArgs);
 collector.addPropertyCreator(creatorDelegateWithProperties, false, properties);
 assertTrue(collector.hasCreator(AnnotatedWithParams.class, "delegate-property"));
}

 @Test(expected = IllegalArgumentException.class)
 public void testSetDuplicateStringCreator() {
 creatorString = mock(AnnotatedWithParams.class);
 collector.addStringCreator(creatorString, false);
 collector.addStringCreator(creatorString, false);
}

 @Test(expected = IllegalArgumentException.class)
 public void testSetDuplicateIntCreator() {
 creatorInt = mock(AnnotatedWithParams.class);
 collector.addIntCreator(creatorInt, false);
 collector.addIntCreator(creatorInt, false);
}

 @Test(expected = IllegalArgumentException.class)
 public void testSetDuplicateLongCreator() {
 creatorLong = mock(AnnotatedWithParams.class);
 collector.addLongCreator(creatorLong, false);
 collector.addLongCreator(creatorLong, false);
}

 @Test(expected = IllegalArgumentException.class)
 public void testSetDuplicateDoubleCreator() {
 creatorDouble = mock(AnnotatedWithParams.class);
 collector.addDoubleCreator(creatorDouble, false);
 collector.addDoubleCreator(creatorDouble, false);
}

 @Test(expected = IllegalArgumentException.class)
 public void testSetDuplicateBooleanCreator() {
 creatorBoolean = mock(AnnotatedWithParams.class);
 collector.addBooleanCreator(creatorBoolean, false);
 collector.addBooleanCreator(creatorBoolean, false);
}

 @Test(expected = IllegalArgumentException.class)
 public void testSetDuplicateDelegateCreator() {
 creatorDelegate = mock(AnnotatedWithParams.class);
 CreatorProperty[] delegateArgs = new CreatorProperty[1];
 CreatorProperty delegateArg = mock(CreatorProperty.class);
 delegateArgs[0] = delegateArg;
 collector.addDelegatingCreator(creatorDelegate, false, delegateArgs);
 collector.addDelegatingCreator(creatorDelegate, false, delegateArgs);
}

 @Test(expected = IllegalArgumentException.class)
 public void testSetDuplicatePropertyCreator() {
 creatorProperties = mock(AnnotatedWithParams.class);
 CreatorProperty[] propertyBasedArgs = new CreatorProperty[3];
 CreatorProperty property = mock(CreatorProperty.class);
 CreatorProperty[] properties = new CreatorProperty[2];
 properties[0] = property;
 properties[1] = property;
 collector.addPropertyCreator(creatorProperties, false, properties);
 collector.addPropertyCreator(creatorProperties, false, properties);
}

 @Test
 public void testAddIncompleteParameter() {
 incompleteParameter = mock(AnnotatedParameter.class);
 collector.addIncompleteParameter(incompleteParameter);
 assertNotNull(collector.getIncompleteParameter());
}
}