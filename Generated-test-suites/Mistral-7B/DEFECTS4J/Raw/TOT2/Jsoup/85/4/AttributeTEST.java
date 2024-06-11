package org.junit.test;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

@Test
public void testAttributeCreation() {
    String key = "testKey";
    String value = "testValue";
    Attribute attribute = new Attribute(key, value);
    assertNotNull(attribute);
    assertEquals(key, attribute.getKey());
    assertEquals(value, attribute.getValue());
}

@Test
public void testAttributeCreationWithParent() {
    String key = "testKey";
    String value = "testValue";
    Attribute attribute = new Attribute(key, value, null);
    assertNotNull(attribute);
    assertEquals(key, attribute.getKey());
    assertEquals(value, attribute.getValue());

    Attributes parent = new Attributes();
    attribute = new Attribute(key, value, parent);
    assertNotNull(attribute);
    assertEquals(key, attribute.getKey());
    assertEquals(value, attribute.getValue());
    assertEquals(attribute, parent.get(key));
}

@Test
public void testGetKey() {
    String key = "testKey";
    String value = "testValue";
    Attribute attribute = new Attribute(key, value, null);
    assertEquals(key, attribute.getKey());
}

@Test
public void testSetKey() {
    String initialKey = "initialKey";
    String value = "testValue";
    Attribute attribute = new Attribute(initialKey, value, null);
    String expectedKey = "newKey";
    attribute.setKey(expectedKey);
    assertEquals(expectedKey, attribute.getKey());
}

@Test
public void testGetValue() {
    String key = "testKey";
    String value = "testValue";
    Attribute attribute = new Attribute(key, value, null);
    assertEquals(value, attribute.getValue());
}

@Test
public void testSetValue() {
    String key = "testKey";
    String initialValue = "initialValue";
    String newValue = "newValue";
    Attribute attribute = new Attribute(key, initialValue, null);
    assertEquals(initialValue, attribute.getValue());
    attribute.setValue(newValue);
    assertEquals(newValue, attribute.getValue());
}

// Add more test cases as needed for other methods and edge cases
}

// Test end