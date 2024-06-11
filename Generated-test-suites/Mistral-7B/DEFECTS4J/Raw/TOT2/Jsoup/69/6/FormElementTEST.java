package org.jsoup.nodes;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Elements elements = formElement.elements();
 assertNotNull(elements);
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element element = new Element("input");
 formElement.addElement(element);
 Elements elements = formElement.elements();
 assertTrue(elements.size() > 0);
 }

 @Test
 public void testSubmitMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 formElement.addElement(new Element("input", new Attributes().put("name", "testName").put("value", "testValue")));
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("testName", formData.get(0).key);
 assertEquals("testValue", formData.get(0).value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementConstructionWithNullBaseUri() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementConstructionWithNullTag() {
 FormElement formElement = new FormElement(null, "baseUri", new Attributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementConstructionWithNullAttributes() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementConstructionWithEmptyTagName() {
 FormElement formElement = new FormElement(new Tag("", "baseUri", new Attributes()), "baseUri", new Attributes());
 }

 @Test
 public void testEmptyFormElementsMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Elements elements = formElement.elements();
 assertEquals(0, elements.size());
 }

 @Test
 public void testFormControlWithEmptyNameMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element input = new Element("input", new Attributes());
 formElement.addElement(input);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormControlWithEmptyValueMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element input = new Element("input", new Attributes().put("name", "testName"));
 formElement.addElement(input);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testInvalidFormControlMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element invalidControl = new Element("invalidControl", new Attributes());
 formElement.addElement(invalidControl);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testSubmitWithEmptyBaseUriMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Connection connection = formElement.submit();
 assertNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidActionURL() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormDataWithEmptyFormMethod() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }
}