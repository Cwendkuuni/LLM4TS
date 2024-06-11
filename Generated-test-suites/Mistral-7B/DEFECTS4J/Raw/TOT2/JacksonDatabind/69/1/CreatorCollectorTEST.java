package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

 private CreatorCollector collector;
 private BeanDescription beanDescription;
 private MapperFactoryConfig config;

 @Before
 public void setUp() throws Exception {
 beanDescription = new BeanDescription();
 beanDescription.setName("TestBean");
 beanDescription.addProperty("property1", String.class);
 beanDescription.addProperty("property2", Integer.class);
 beanDescription.addProperty("property3", Long.class);
 config = new MapperFactoryConfig();
 collector = new CreatorCollector(beanDescription, config);
 }

 @Test
 public void testDefaultCreator() throws Exception {
 JavaType type = beanDescription.getType();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 Object instance = instantiator.createValueInstance(null);
 assertNotNull(instance);
 assertTrue(instance instanceof Object);
 }

 @Test
 public void testStringCreator() throws Exception {
 AnnotatedWithParams creator = AnnotatedWithParams.construct("TestBean", String[].class, new String[]{"test"});
 collector.addStringCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 Object instance = instantiator.createValueInstance(new Object[]{"test"});
 assertNotNull(instance);
 assertTrue(instance instanceof Object);
 assertEquals("test", instance);
 }

 @Test
 public void testIntCreator() throws Exception {
 AnnotatedWithParams creator = AnnotatedWithParams.construct("TestBean", int[].class, new int[]{1});
 collector.addIntCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 Object instance = instantiator.createValueInstance(new Object[]{1});
 assertNotNull(instance);
 assertTrue(instance instanceof Object);
 assertEquals(1, instance);
 }

 @Test
 public void testLongCreator() throws Exception {
 AnnotatedWithParams creator = AnnotatedWithParams.construct("TestBean", long[].class, new long[]{1L});
 collector.addLongCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 Object instance = instantiator.createValueInstance(new Object[]{1L});
 assertNotNull(instance);
 assertTrue(instance instanceof Object);
 assertEquals(1L, instance);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 AnnotatedWithParams creator = AnnotatedWithParams.construct("TestBean", boolean[].class, new boolean[]{true});
 collector.addBooleanCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 Object instance = instantiator.createValueInstance(new Object[]{true});
 assertNotNull(instance);
 assertTrue(instance instanceof Object);
 assertEquals(true, instance);
 }

 @Test
 public void testDelegateCreator() throws Exception {
 AnnotatedWithParams creator = AnnotatedWithParams.construct(TestDelegate.class, new Class[0]);
 SettableBeanProperty[] injectables = new SettableBeanProperty[1];
 injectables[0] = new SettableBeanProperty("delegate", String.class);
 collector.addDelegatingCreator(creator, false, injectables);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 Object instance = instantiator.createValueInstance(new Object[]{"test"});
 assertNotNull(instance);
 assertTrue(instance instanceof TestDelegate);
 assertEquals("test", instance.getDelegate());
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 AnnotatedWithParams creator = AnnotatedWithParams.construct("TestPropertyBased", TestPropertyBased.class, new Class[0]);
 SettableBeanProperty[] properties = new SettableBeanProperty[2];
 properties[0] = new SettableBeanProperty("property1", String.class);
 properties[1] = new SettableBeanProperty("property2", Integer.class);
 collector.addPropertyCreator(creator, false, properties);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 Object instance = instantiator.createValueInstance(new Object[]{"test", 1});
 assertNotNull(instance);
 assertTrue(instance instanceof TestPropertyBased);
 assertEquals("test", instance.getProperty1());
 assertEquals(1, instance.getProperty2());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 AnnotatedWithParams creator = AnnotatedWithParams.construct("TestBean", String[].class, new String[]{"test"});
 collector.addStringCreator(creator, false);
 collector.addStringCreator(creator, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 // should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIncompatibleCreatorTypes() throws Exception {
 AnnotatedWithParams creator1 = AnnotatedWithParams.construct("TestBean1", String[].class, new String[]{"test"});
 collector.addStringCreator(creator1, false);
 AnnotatedWithParams creator2 = AnnotatedWithParams.construct("TestBean2", Integer[].class, new Integer[]{1});
 collector.addIntCreator(creator2, false);
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 // should throw an IllegalArgumentException
 }

 @Test(expected = IllegalStateException.class)
 public void testUnknownCreatorType() throws Exception {
 AnnotatedWithParams creator = AnnotatedWithParams.construct("TestBean", String[].class, new String[]{"test"});
 collector.addCreator(creator, new Object[0].getClass());
 // should throw an IllegalStateException
 }

}