package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class AttributeTest {
	
	private Attribute attribute1;
	private Attribute attribute2;
	private Attribute attribute3;
	private Attribute attribute4;
	private Attribute attribute5;
	private Attributes parent;
	
	@Before
	public void setup() {
		attribute1 = new Attribute("key1", "value1");
		attribute2 = new Attribute("key2", "value2");
		attribute3 = new Attribute("key3", "value3");
		attribute4 = new Attribute("key4", "value4");
		attribute5 = new Attribute("key5", "value5");
		parent = new Attributes();
	}

	@Test
	public void testGetKey() {
		assertEquals("key1", attribute1.getKey());
		assertEquals("key2", attribute2.getKey());
		assertEquals("key3", attribute3.getKey());
		assertEquals("key4", attribute4.getKey());
		assertEquals("key5", attribute5.getKey());
	}

	@Test
	public void testSetKey() {
		attribute1.setKey("newKey1");
		assertEquals("newKey1", attribute1.getKey());
		
		attribute2.setKey("newKey2");
		assertEquals("newKey2", attribute2.getKey());
		
		attribute3.setKey("newKey3");
		assertEquals("newKey3", attribute3.getKey());
		
		attribute4.setKey("newKey4");
		assertEquals("newKey4", attribute4.getKey());
		
		attribute5.setKey("newKey5");
		assertEquals("newKey5", attribute5.getKey());
	}

	@Test
	public void testGetValue() {
		assertEquals("value1", attribute1.getValue());
		assertEquals("value2", attribute2.getValue());
		assertEquals("value3", attribute3.getValue());
		assertEquals("value4", attribute4.getValue());
		assertEquals("value5", attribute5.getValue());
	}

	@Test
	public void testSetValue() {
		assertEquals("value1", attribute1.setValue("newValue1"));
		assertEquals("newValue1", attribute1.getValue());
		
		assertEquals("value2", attribute2.setValue("newValue2"));
		assertEquals("newValue2", attribute2.getValue());
		
		assertEquals("value3", attribute3.setValue("newValue3"));
		assertEquals("newValue3", attribute3.getValue());
		
		assertEquals("value4", attribute4.setValue("newValue4"));
		assertEquals("newValue4", attribute4.getValue());
		
		assertEquals("value5", attribute5.setValue("newValue5"));
		assertEquals("newValue5", attribute5.getValue());
	}

	@Test
	public void testHtml() {
		assertEquals("key1=\"value1\"", attribute1.html());
		assertEquals("key2=\"value2\"", attribute2.html());
		assertEquals("key3=\"value3\"", attribute3.html());
		assertEquals("key4=\"value4\"", attribute4.html());
		assertEquals("key5=\"value5\"", attribute5.html());
	}

	@Test
	public void testClone() {
		Attribute clonedAttribute = attribute1.clone();
		assertEquals(attribute1.getKey(), clonedAttribute.getKey());
		assertEquals(attribute1.getValue(), clonedAttribute.getValue());
		
		clonedAttribute = attribute2.clone();
		assertEquals(attribute2.getKey(), clonedAttribute.getKey());
		assertEquals(attribute2.getValue(), clonedAttribute.getValue());
		
		clonedAttribute = attribute3.clone();
		assertEquals(attribute3.getKey(), clonedAttribute.getKey());
		assertEquals(attribute3.getValue(), clonedAttribute.getValue());
		
		clonedAttribute = attribute4.clone();
		assertEquals(attribute4.getKey(), clonedAttribute.getKey());
		assertEquals(attribute4.getValue(), clonedAttribute.getValue());
		
		clonedAttribute = attribute5.clone();
		assertEquals(attribute5.getKey(), clonedAttribute.getKey());
		assertEquals(attribute5.getValue(), clonedAttribute.getValue());
	}

	@Test
	public void testEquals() {
		assertFalse(attribute1.equals(attribute2));
		assertFalse(attribute2.equals(attribute3));
		assertFalse(attribute3.equals(attribute4));
		assertFalse(attribute4.equals(attribute5));
		
		Attribute attribute1Copy = new Attribute("key1", "value1");
		assertEquals(attribute1, attribute1Copy);
	}

	@Test
	public void testHashCode() {
		assertEquals(attribute1.hashCode(), attribute1.hashCode());
		assertEquals(attribute2.hashCode(), attribute2.hashCode());
		assertEquals(attribute3.hashCode(), attribute3.hashCode());
		assertEquals(attribute4.hashCode(), attribute4.hashCode());
		assertEquals(attribute5.hashCode(), attribute5.hashCode());
	}

}