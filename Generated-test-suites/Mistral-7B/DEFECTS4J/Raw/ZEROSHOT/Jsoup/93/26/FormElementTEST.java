package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setup() {
 Tag tag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("action", "/submit");
 FormElement formElement = new FormElement(tag, baseUri, attributes);
 this.formElement = formElement;
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(0, elements.size());

 Element inputElement = Jsoup.parse("<form><input type=\"text\"></form>").selectFirst("input");
 formElement.add(inputElement);
 elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(inputElement.tagName(), elements.get(0).tagName());
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 assertEquals("/submit", connection.url());
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertTrue(formData.isEmpty());

 Element inputElement = Jsoup.parse("<form><input type=\"text\" name=\"username\" value=\"test\"></form>").selectFirst("input");
 formElement.add(inputElement);
 formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("test", formData.get(0).value());
 }

 @Test
 public void testFormDataWithSelectElement() {
 Element form = Jsoup.parse("<form><select name=\"color\"><option value=\"red\">Red</option><option value=\"green\">Green</option></select></form>").selectFirst("form");
 FormElement formElement = (FormElement) form;
 formElement.add(form.selectFirst("option[value=green]"));
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("color", formData.get(0).key());
 assertEquals("green", formData.get(0).value());
 }

 @Test
 public void testFormDataWithCheckboxElement() {
 Element form = Jsoup.parse("<form><input type=\"checkbox\" name=\"agree\" value=\"true\">Agree</form>").selectFirst("form");
 FormElement formElement = (FormElement) form;
 formElement.add(form.selectFirst("input[type=checkbox]"));
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("agree", formData.get(0).key());
 assertEquals("true", formData.get(0).value());
 }

 @Test
 public void testFormDataWithMultipleElements() {
 Element form = Jsoup.parse("<form><input type=\"text\" name=\"username\" value=\"test1\"><input type=\"text\" name=\"username\" value=\"test2\"><input type=\"checkbox\" name=\"agree\" value=\"true\"><select name=\"color\"><option value=\"red\">Red</option><option value=\"green\">Green</option></select></form>").selectFirst("form");
 FormElement formElement = (FormElement) form;
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("test1", formData.get(0).value());
 assertEquals("username", formData.get(1).key());
 assertEquals("test2", formData.get(1).value());
 assertEquals("agree", formData.get(2).key());
 assertEquals("true", formData.get(2).value());
 assertEquals("color", formData.get(3).key());
 assertNull(formData.get(3).value());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 String message = e.getMessage();
 assertTrue(message.contains("Could not determine a form action URL for submit. Ensure you set a base URI when parsing."));
 }
 }
}