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

 private FormElement formElement;
 private Connection connection;

 @Before
 public void setUp() {
 String html = "<html><head></head><body><form action='/submit' method='post'><input type='text' name='inputName' /><input type='submit' /></form></body></html>";
 formElement = Jsoup.parseBodyFragment(html).selectFirst("form");
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(0, elements.size());

 Element input = new Element("input", new Attributes().put("type", "text").put("name", "inputName"));
 formElement.addElement(input);
 elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(1, elements.size());
 assertEquals(input, elements.first());
 }

 @Test
 public void testSubmit() throws Exception {
 List<Connection.KeyVal> formData = formElement.formData();
 connection = formElement.submit();
 assertEquals("/submit", connection.url().toString());
 assertEquals("POST", connection.method().name());

 List<Connection.KeyVal> submittedFormData = connection.requestBody();
 assertEquals(1, submittedFormData.size());
 assertEquals("inputName", submittedFormData.get(0).key());
 assertNull(submittedFormData.get(0).value());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseURI() {
 FormElement formElementWithoutBaseURI = new FormElement(Tag.FORM, "", new Attributes());
 try {
 formElementWithoutBaseURI.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 throw e;
 }
 }
}