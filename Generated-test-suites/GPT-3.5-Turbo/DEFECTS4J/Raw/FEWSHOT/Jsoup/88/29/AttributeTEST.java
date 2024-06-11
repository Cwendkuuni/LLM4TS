package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.helper.Validate;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

public class AttributeTest {
	
	@Test
	public void testAttributeKey() {
		Attribute attribute = new Attribute("key", "value");
		assertEquals("key", attribute.getKey());
		
		attribute.setKey("newKey");
		assertEquals("newKey", attribute.getKey());
	}
	
	@Test
	public void testAttributeValue() {
		Attribute attribute = new Attribute("key", "value");
		assertEquals("value", attribute.getValue());
		
		attribute.setValue("newValue");
		assertEquals("newValue", attribute.getValue());
	}
	
	@Test
	public void testHtmlRepresentation() throws IOException {
		Attribute attribute = new Attribute("key", "value");
		assertEquals("key=\"value\"", attribute.html());
	}
	
	@Test
	public void testToStringMethod() throws IOException {
		Attribute attribute = new Attribute("key", "value");
		assertEquals("key=\"value\"", attribute.toString());
	}
	
	@Test
	public void testCreateFromEncoded() {
		Attribute attribute = Attribute.createFromEncoded("key", "value");
		assertEquals("key", attribute.getKey());
		assertEquals("value", attribute.getValue());
	}
	
	@Test
	public void testIsDataAttribute() {
		assertTrue(Attribute.isDataAttribute("data-key"));
		assertFalse(Attribute.isDataAttribute("key"));
	}
	
	@Test
	public void testShouldCollapseAttribute() {
		Document.OutputSettings out = new Document.OutputSettings();
		assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
		assertFalse(Attribute.shouldCollapseAttribute("key", "value", out));
	}
	
	@Test
	public void testIsBooleanAttribute() {
		assertTrue(Attribute.isBooleanAttribute("allowfullscreen"));
		assertFalse(Attribute.isBooleanAttribute("key"));
	}

	@Test
	public void testEqualsMethod() {
		Attribute attribute1 = new Attribute("key", "value");
		Attribute attribute2 = new Attribute("key", "value");
		assertTrue(attribute1.equals(attribute2));
		
		attribute2.setKey("newKey");
		assertFalse(attribute1.equals(attribute2));
	}
	
	@Test
	public void testHashCode() {
		Attribute attribute = new Attribute("key", "value");
		int hashCode = attribute.hashCode();
		assertEquals(hashCode, attribute.hashCode());
	}
	
	@Test
	public void testCloneMethod() {
		Attribute attribute1 = new Attribute("key", "value");
		Attribute attribute2 = attribute1.clone();
		assertEquals(attribute1.getKey(), attribute2.getKey());
		assertEquals(attribute1.getValue(), attribute2.getValue());
	}
}