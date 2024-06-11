package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
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

 @Before
 public void setup() {
 String html = "<html><body>" +
 " <form action=\"/submit\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"text\" name=\"password\" />" +
 " <select name=\"option\">" +
 " <option value=\"option1\">Option 1</option>" +
 " <option value=\"option2\" selected>Option 2</option>" +
 " </select>" +
 " <input type=\"submit\" />" +
 " </form>" +
 "</body></html>";

 Document doc = Jsoup.parse(html);
 formElement = (FormElement) doc.body().select("form").first();
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("input");
 newElement.attr("type", "hidden");
 newElement.attr("name", "newElement");
 formElement.addElement(newElement);
 Elements elementsAfterAdd = formElement.elements();
 assertEquals(4, elementsAfterAdd.size());
 assertEquals(newElement, elementsAfterAdd.get(3));
 }

 @Test
 public void testSubmit() throws Exception {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 List<Connection.KeyVal> formData = connection.requestBody();
 assertNotNull(formData);
 assertEquals(4, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("", formData.get(0).value()); // text inputs are empty by default
 assertEquals("password", formData.get(1).key());
 assertEquals("", formData.get(1).value());
 assertEquals("option2", formData.get(2).key());
 assertEquals("Option 2", formData.get(2).value());
 assertEquals("option1", formData.get(3).key());
 assertEquals("Option 1", formData.get(3).value());
 }

 @Test
 public void testSubmitWithDisabledInput() throws Exception {
 formElement.child(1).attr("disabled", "disabled");
 Connection connection = formElement.submit();
 assertNotNull(connection);
 List<Connection.KeyVal> formData = connection.requestBody();
 assertNotNull(formData);
 assertEquals(3, formData.size());
 assertNull(formData.get(1)); // disabled input is not included in form data
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 FormElement formWithoutAction = new FormElement(new Tag("form"), "", new Attributes());
 Connection connection = formWithoutAction.submit();
 }
}