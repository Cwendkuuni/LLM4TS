package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class CreatorCollectorTest {
	
	private CreatorCollector creatorCollector;
	private BeanDescription beanDescription;
	private boolean canFixAccess;
	
	@Before
	public void setUp() {
		beanDescription = mock(BeanDescription.class);
		canFixAccess = true;
		creatorCollector = new CreatorCollector(beanDescription, canFixAccess);
	}
	
	@Test
	public void testSetDefaultCreator() {
		creatorCollector.setDefaultCreator(null);
		assertEquals(null, creatorCollector.setDefaultCreator(null));
	}
	
	@Test
	public void testAddStringCreator() {
		creatorCollector.addStringCreator(null, false);
		assertEquals(null, creatorCollector.addStringCreator(null, false));
	}
	
	@Test
	public void testAddIntCreator() {
		creatorCollector.addIntCreator(null, false);
		assertEquals(null, creatorCollector.addIntCreator(null, false));
	}
	
	@Test
	public void testAddLongCreator() {
		creatorCollector.addLongCreator(null, false);
		assertEquals(null, creatorCollector.addLongCreator(null, false));
	}
	
	@Test
	public void testAddDoubleCreator() {
		creatorCollector.addDoubleCreator(null, false);
		assertEquals(null, creatorCollector.addDoubleCreator(null, false));
	}
	
	@Test
	public void testAddBooleanCreator() {
		creatorCollector.addBooleanCreator(null, false);
		assertEquals(null, creatorCollector.addBooleanCreator(null, false));
	}
	
	@Test
	public void testAddDelegatingCreator() {
		CreatorProperty[] injectables = {};
		creatorCollector.addDelegatingCreator(null, false, injectables);
		assertEquals(null, creatorCollector.addDelegatingCreator(null, false, injectables));
	}
	
	@Test
	public void testAddPropertyCreator() {
		CreatorProperty[] properties = {};
		creatorCollector.addPropertyCreator(null, false, properties);
		assertEquals(null, creatorCollector.addPropertyCreator(null, false, properties));
	}
	
	@Test
	public void testAddIncompeteParameter() {
		creatorCollector.addIncompeteParameter(null);
		assertEquals(null, creatorCollector.addIncompeteParameter(null));
	}
	
	@Test
	public void testHasDefaultCreator() {
		boolean result = creatorCollector.hasDefaultCreator();
		assertEquals(false, result);
	}
	
	@Test
	public void testConstructValueInstantiator() {
		DeserializationConfig config = mock(DeserializationConfig.class);
		ValueInstantiator result = creatorCollector.constructValueInstantiator(config);
		assertEquals(null, result);
	}
}