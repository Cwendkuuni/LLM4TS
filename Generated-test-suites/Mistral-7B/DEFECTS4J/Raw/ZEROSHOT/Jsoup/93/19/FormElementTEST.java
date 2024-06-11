package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;

import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

 @Before
 public void setup() {
 ParseSetup.setupBase("src/test/resources");
 }

 @Test
 public void testFormElementCreation() {
 // Create a new FormElement with an input tag
 Tag formTag = new Tag("form");
 Attributes formAttributes = new Attributes();
 formAttributes.put("action", "http://test.com");
 formAttributes.put("method", "POST");
 FormElement formElement = new FormElement(formTag, "", formAttributes);

 // Verify that the FormElement was created correctly
 assertEquals("form", formElement.tagName());
 assertEquals("http://test.com", formElement.baseUri());
 assertNotNull(formElement.elements());
 }

 @Test
 public void testFormElementEmpty() {
 // Create a new FormElement with an empty tag
 Tag formTag = new Tag("form");
 FormElement formElement = new FormElement(formTag, "", new Attributes());

 // Verify that the FormElement was created correctly
 assertEquals("form", formElement.tagName());
 assertNull(formElement.baseUri());
 assertNotNull(formElement.elements());
 assertFalse(formElement.elements().size() > 0);
 }

 @Test
 public void testFormElementWithElement() {
 // Create a new FormElement with an input tag as a child
 Tag formTag = new Tag("form");
 Attributes formAttributes = new Attributes();
 formAttributes.put("action", "http://test.com");
 formAttributes.put("method", "POST");
 Element inputElement = new Element("input");
 inputElement.attr("type", "text");
 inputElement.attr("name", "username");
 formElement = new FormElement(formTag, "", formAttributes);
 formElement.addElement(inputElement);

 // Verify that the FormElement and its child Element were created correctly
 assertEquals("form", formElement.tagName());
 assertEquals("http://test.com", formElement.baseUri());
 assertNotNull(formElement.elements());
 assertFalse(formElement.elements().size() > 0);
 assertNotNull(formElement.elements().get(0));
 assertEquals("input", formElement.elements().get(0).tagName());
 assertEquals("text", formElement.elements().get(0).attr("type"));
 assertEquals("username", formElement.elements().get(0).attr("name"));
 }

 @Test
 public void testFormElementWithMultipleElements() {
 // Create a new FormElement with multiple input tags as children
 Tag formTag = new Tag("form");
 Attributes formAttributes = new Attributes();
 formAttributes.put("action", "http://test.com");
 formAttributes.put("method", "POST");

 Element input1 = new Element("input");
 input1.attr("type", "text");
 input1.attr("name", "username");
 Element input2 = new Element("input");
 input2.attr("type", "password");
 input2.attr("name", "password");

 formElement = new FormElement(formTag, "", formAttributes);
 formElement.addElement(input1);
 formElement.addElement(input2);

 // Verify that the FormElement and its child Elements were created correctly
 assertEquals("form", formElement.tagName());
 assertEquals("http://test.com", formElement.baseUri());
 assertNotNull(formElement.elements());
 assertFalse(formElement.elements().size() > 0);
 assertNotNull(formElement.elements().get(0));
 assertNotNull(formElement.elements().get(1));
 assertEquals("input", formElement.elements().get(0).tagName());
 assertEquals("text", formElement.elements().get(0).attr("type"));
 assertEquals("username", formElement.elements().get(0).attr("name"));
 assertEquals("input", formElement.elements().get(1).tagName());
 assertEquals("password", formElement.elements().get(1).attr("type"));
 assertEquals("password", formElement.elements().get(1).attr("name"));
 }

 @Test
 public void testFormElementRemoveElement() {
 // Create a new FormElement with multiple input tags as children
 Tag formTag = new Tag("form");
 Attributes formAttributes = new Attributes();
 formAttributes.put("action", "http://test.com");
 formAttributes.put("method", "POST");

 Element input1 = new Element("input");
 input1.attr("type", "text");
 input1.attr("name", "username");
 Element input2 = new Element("input");
 input2.attr("type", "password");
 input2.attr("name", "password");

 formElement = new FormElement(formTag, "", formAttributes);
 formElement.addElement(input1);
 formElement.addElement(input2);
 formElement.removeChild(input1);

 // Verify that the FormElement and its child Elements were removed correctly
 assertEquals("form", formElement.tagName());
 assertEquals("http://test.com", formElement.baseUri());
 assertNotNull(formElement.elements());
 assertFalse(formElement.elements().size() > 0);
 assertNull(formElement.elements().get(0));
 assertNotNull(formElement.elements().get(1));
 assertEquals("input", formElement.elements().get(1).tagName());
 assertEquals("password", formElement.elements().get(1).attr("type"));
 assertEquals("password", formElement.elements().get(1).attr("name"));
 }

 @Test
 public void testFormElementSubmit() {
 // Create a new FormElement with multiple input tags as children
 Tag formTag = new Tag("form");
 Attributes formAttributes = new Attributes();
 formAttributes.put("action", "http://test.com");
 formAttributes.put("method", "POST");

 Element input1 = new Element("input");
 input1.attr("type", "text");
 input1.attr("name", "username");
 Element input2 = new Element("input");
 input2.attr("type", "password");
 input2.attr("name", "password");

 formElement = new FormElement(formTag, "", formAttributes);
 formElement.addElement(input1);
 formElement.addElement(input2);

 Connection connection = formElement.submit();

 // Verify that the Connection was created correctly
 assertEquals(HttpURLConnection.HTTP_OK, connection.response().statusCode());
 List<Connection.KeyVal> formData = formElement.formData();

 // Verify that the form data was created correctly
 assertEquals(2, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals(input1.val(), formData.get(0).val());
 assertEquals("password", formData.get(1).key());
 assertEquals(input2.val(), formData.get(1).val());
 }
}