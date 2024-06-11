package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

   private CreatorCollector creatorCollector;

   @Before
   public void setUp() {
      creatorCollector = new CreatorCollector(null, null);
   }

   @Test
   public void testSetDefaultCreator() {
      creatorCollector.setDefaultCreator(null);
      assertTrue(creatorCollector.hasDefaultCreator());
   }

   @Test
   public void testAddStringCreator() {
      creatorCollector.addStringCreator(null, true);
      assertTrue(creatorCollector.hasPropertyBasedCreator());
   }

   @Test
   public void testAddIntCreator() {
      creatorCollector.addIntCreator(null, true);
      assertTrue(creatorCollector.hasPropertyBasedCreator());
   }

   @Test
   public void testAddLongCreator() {
      creatorCollector.addLongCreator(null, true);
      assertTrue(creatorCollector.hasPropertyBasedCreator());
   }

   @Test
   public void testAddDoubleCreator() {
      creatorCollector.addDoubleCreator(null, true);
      assertTrue(creatorCollector.hasPropertyBasedCreator());
   }

   @Test
   public void testAddBooleanCreator() {
      creatorCollector.addBooleanCreator(null, true);
      assertTrue(creatorCollector.hasPropertyBasedCreator());
   }

   @Test
   public void testAddDelegatingCreator() {
      creatorCollector.addDelegatingCreator(null, true, null);
      assertTrue(creatorCollector.hasDelegatingCreator());
   }

   @Test
   public void testAddPropertyCreator() {
      creatorCollector.addPropertyCreator(null, true, null);
      assertTrue(creatorCollector.hasPropertyBasedCreator());
   }

   @Test
   public void testAddIncompeteParameter() {
      creatorCollector.addIncompeteParameter(null);
      assertNotNull(creatorCollector._incompleteParameter);
   }
}