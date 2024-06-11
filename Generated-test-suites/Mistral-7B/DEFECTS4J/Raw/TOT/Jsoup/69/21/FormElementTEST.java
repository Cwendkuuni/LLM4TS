package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private FormElement form;

 @Before
 public void setUp() {
 String html = "<html><form action=\"test.html\" method=\"post\"><input type=\"text\" name=\"name\"/><input type=\"submit\"/></form></html>";
 form = Jsoup.parseBodyFragment(html).selectFirst(FormElement.class);
 }

 @Test
 public void testFormElements() {
 // Test getting form elements
 Elements elements = form.elements();
 assertEquals(2, elements.size());

 // Test adding a new form element
 Element input = Jsoup.parseBodyFragment("<input type=\"text\" name=\"age\"/>").selectFirst(Element.class);
 form.addElement(input);
 elements = form.elements();
 assertEquals(3, elements.size());
 assertNotNull(elements.get(2));
 assertEquals("input", elements.get(2).tagName());
 }

 @Test
 public void testFormSubmit() {
 // Test submission with no form data
 Connection connection = form.submit();
 assertNull(connection.dataMap());

 // Test submission with form data
 form.child(0).attr("value", "John Doe");
 form.child(1).attr("value", "42");
 connection = form.submit();
 List<Connection.KeyVal> formData = connection.dataMap();
 assertEquals(2, formData.size());
 assertEquals("name", formData.get(0).key);
 assertEquals("John Doe", formData.get(0).val);
 assertEquals("age", formData.get(1).key);
 assertEquals("42", formData.get(1).val);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormSubmitWithoutBaseUri() {
 // Test submission without base URI, which should throw an exception
 form = Jsoup.parseBodyFragment("<form action=\"test.html\"></form>").selectFirst(FormElement.class);
 form.submit();
 }

 @Test
 public void testFormSubmitWithInvalidMethod() {
 // Test submission with an invalid method
 form.attr("method", "PUT");
 Connection connection = form.submit();
 assertNull(connection.dataMap());
 }
}