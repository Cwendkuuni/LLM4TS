package org.junit.test;

import org.junit.Assert;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

// Test cases for Attribute constructor
@Test
public void testAttributeConstruction() {
// Given
String key = "testKey";
String value = "testValue";

// When
Attribute attribute = new Attribute(key, value);

// Then
Assert.assertEquals(key, attribute.getKey());
Assert.assertEquals(value, attribute.getValue());
}

// Test cases for getKey() method
@Test
public void testGetKey() {
// Given
Attribute attribute = new Attribute("testKey", "testValue");

// When
String key = attribute.getKey();

// Then
Assert.assertEquals("testKey", key);
}

// Test cases for setValue(String val) method
@Test
public void testSetValue() {
// Given
Attribute attribute = new Attribute("testKey", "testValue");

// When
String newValue = "newTestValue";
String oldValue = attribute.setValue(newValue);

// Then
Assert.assertEquals(newValue, attribute.getValue());
Assert.assertNotNull(oldValue);
Assert.assertNotEquals(newValue, oldValue);
}

// Test cases for null Attribute instance
@Test(expected = IllegalArgumentException.class)
public void testSetValueWithNullAttribute() {
// Given
Attribute attribute = null;

// When
String newValue = "newTestValue";

// Then
attribute.setValue(newValue);
}

// Test cases for null value
@Test(expected = IllegalArgumentException.class)
public void testSetValueWithNullValue() {
// Given
Attribute attribute = new Attribute("testKey", "testValue");

// When
String newValue = null;

// Then
attribute.setValue(newValue);
}

// Test cases for empty value
@Test(expected = IllegalArgumentException.class)
public void testSetValueWithEmptyValue() {
// Given
Attribute attribute = new Attribute("testKey", "testValue");

// When
String newValue = "";

// Then
attribute.setValue(newValue);
}

// Test cases for getValue() method
@Test
public void testGetValue() {
// Given
Attribute attribute = new Attribute("testKey", "testValue");

// When
String value = attribute.getValue();

// Then
Assert.assertEquals("testValue", value);
}

// Test cases for clone() method
@Test
public void testClone() {
// Given
Attribute attribute = new Attribute("testKey", "testValue");

// When
Attribute clonedAttribute = attribute.clone();

// Then
Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
}

// Test cases for createFromEncoded(String unencodedKey, String encodedValue) method
@Test
public void testCreateFromEncoded() {
// Given
String unencodedKey = "testKey";
String encodedValue = "testValue%20%22%22";

// When
Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

// Then
Assert.assertEquals(unencodedKey, attribute.getKey());
Assert.assertEquals("test Value \"\"", attribute.getValue());
}

// Test cases for isDataAttribute() method
@Test
public void testIsDataAttribute() {
// Given
Attribute attribute = new Attribute("testDataKey", "testValue");

// When
boolean isDataAttribute = attribute.isDataAttribute();

// Then
Assert.assertTrue(isDataAttribute);
}

// Test cases for isDataAttribute(String key) method
@Test
public void testIsDataAttributeWithNonDataAttribute() {
// Given
Attribute attribute = new Attribute("testKey", "testValue");

// When
boolean isDataAttribute = attribute.isDataAttribute();

// Then
Assert.assertFalse(isDataAttribute);
}

// Test cases for shouldCollapseAttribute() method with HTML output settings
@Test
public void testShouldCollapseAttribute() {
// Given
Document.OutputSettings settings = new Document.OutputSettings();
settings.syntax(Document.OutputSettings.Syntax.html);

// When
boolean shouldCollapse = Attribute.shouldCollapseAttribute(settings);

// Then
Assert.assertTrue(shouldCollapse);
}

// Test cases for shouldCollapseAttribute(String key, String value, Document.OutputSettings out) method with boolean attribute
@Test
public void testShouldCollapseAttributeWithBooleanAttribute() {
// Given
Document.OutputSettings settings = new Document.OutputSettings();
settings.syntax(Document.OutputSettings.Syntax.html);

// When
boolean shouldCollapse = Attribute.shouldCollapseAttribute("checked", null, settings);

// Then
Assert.assertTrue(shouldCollapse);
}

// Test cases for shouldCollapseAttribute(String key, String value, Document.OutputSettings out) method with non-boolean attribute
@Test
public void testShouldCollapseAttributeWithNonBooleanAttribute() {
// Given
Document.OutputSettings settings = new Document.OutputSettings();
settings.syntax(Document.OutputSettings.Syntax.html);

// When
boolean shouldCollapse = Attribute.shouldCollapseAttribute("testKey", "testValue", settings);

// Then
Assert.assertFalse(shouldCollapse);
}

// Test cases for shouldCollapseAttribute(Document.OutputSettings out) method with null settings
@Test(expected = IllegalArgumentException.class)
public void testShouldCollapseAttributeWithNullSettings() {
// Given
Document.OutputSettings settings = null;

// When
Attribute.shouldCollapseAttribute(settings);

// Then
// This should throw an IllegalArgumentException
}
}