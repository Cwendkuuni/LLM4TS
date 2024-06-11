package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {
   private CreatorCollector creatorCollector;
   
   @Before
   public void setup() {
      BeanDescription beanDesc = null;
      boolean canFixAccess = false;
      creatorCollector = new CreatorCollector(beanDesc, canFixAccess);
   }
   
   @Test
   public void testSetDefaultCreator() {
      AnnotatedWithParams creator = null;
      creatorCollector.setDefaultCreator(creator);
      Assert.assertEquals(creator, creatorCollector._creators[0]);
   }
   
   @Test
   public void testAddStringCreator() {
      AnnotatedWithParams creator = null;
      boolean explicit = false;
      creatorCollector.addStringCreator(creator, explicit);
      Assert.assertEquals(creator, creatorCollector._creators[1]);
   }
   
   @Test
   public void testAddIntCreator() {
      AnnotatedWithParams creator = null;
      boolean explicit = false;
      creatorCollector.addIntCreator(creator, explicit);
      Assert.assertEquals(creator, creatorCollector._creators[2]);
   }
   
   @Test
   public void testAddLongCreator() {
      AnnotatedWithParams creator = null;
      boolean explicit = false;
      creatorCollector.addLongCreator(creator, explicit);
      Assert.assertEquals(creator, creatorCollector._creators[3]);
   }
   
   @Test
   public void testAddDoubleCreator() {
      AnnotatedWithParams creator = null;
      boolean explicit = false;
      creatorCollector.addDoubleCreator(creator, explicit);
      Assert.assertEquals(creator, creatorCollector._creators[4]);
   }
   
   @Test
   public void testAddBooleanCreator() {
      AnnotatedWithParams creator = null;
      boolean explicit = false;
      creatorCollector.addBooleanCreator(creator, explicit);
      Assert.assertEquals(creator, creatorCollector._creators[5]);
   }
   
   @Test
   public void testAddDelegatingCreator() {
      AnnotatedWithParams creator = null;
      boolean explicit = false;
      CreatorProperty[] injectables = null;
      creatorCollector.addDelegatingCreator(creator, explicit, injectables);
      Assert.assertEquals(creator, creatorCollector._creators[6]);
      Assert.assertEquals(injectables, creatorCollector._delegateArgs);
   }
   
   @Test
   public void testAddPropertyCreator() {
      AnnotatedWithParams creator = null;
      boolean explicit = false;
      CreatorProperty[] properties = null;
      creatorCollector.addPropertyCreator(creator, explicit, properties);
      Assert.assertEquals(creator, creatorCollector._creators[7]);
      Assert.assertEquals(properties, creatorCollector._propertyBasedArgs);
   }
   
   @Test
   public void testAddIncompeteParameter() {
      AnnotatedParameter parameter = null;
      creatorCollector.addIncompeteParameter(parameter);
      Assert.assertEquals(parameter, creatorCollector._incompleteParameter);
   }
   
   @Test
   public void testHasDefaultCreator() {
      boolean expected = false;
      boolean result = creatorCollector.hasDefaultCreator();
      Assert.assertEquals(expected, result);
   }
}