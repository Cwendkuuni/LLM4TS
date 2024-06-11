package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 "  <form action='/submit' method='post'>" +
 "    <input type='text' name='username'>" +
 "    <input type='password' name='password'>" +
 "    <select name='country'>" +
 "      <option value='US'>United States</option>" +
 "      <option value='CA'>Canada</option>" +
 "    </select>" +
 "    <input type='submit'>" +
 "  </form>" +
 "</body></html>";

 Document doc = Jsoup.parse(html);
 Tag formTag = doc.selectFirst("form");
 formElement = new FormElement(formTag, null, null);
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 Element input1 = elements.get(0);
 assertEquals("input", input1.tagName());
 Element select = elements.get(1);
 assertEquals("select", select.tagName());
 Element submit = elements.get(2);
 assertEquals("input", submit.tagName());
 assertEquals("submit", submit.attribute("type"));
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 assertEquals("/submit", connection.url().toString());
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(4, formData.size());
 Connection.KeyVal username = null;
 Connection.KeyVal password = null;
 Connection.KeyVal country = null;
 for (Connection.KeyVal keyVal : formData) {
 switch (keyVal.key()) {
 case "username":
 username = keyVal;
 break;
 case "password":
 password = keyVal;
 break;
 case "country":
 country = keyVal;
 break;
 }
 }
 assertNotNull(username);
 assertEquals("username", username.key());
 assertEquals("", username.value());
 assertNotNull(password);
 assertEquals("password", password.key());
 assertNull(password.value());
 assertNotNull(country);
 assertEquals("country", country.key());
 assertEquals("US", country.value());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 FormElement noActionForm = new FormElement(new Tag("form"), null, null);
 assertThrows(IllegalArgumentException.class, () -> noActionForm.submit());
 }

 @Test
 public void testAddElement() {
 Element addedElement = new Element("input", new Attributes().put("type", "hidden").put("name", "token"));
 formElement.addElement(addedElement);
 Elements elements = formElement.elements();
 assertEquals(4, elements.size());
 }
}