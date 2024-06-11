package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String TEST_HTML = "<html><head></head><body><form action='http://test.com'>" +
         "<input type='text' name='username' value='test' />" +
         "<select name='select'>" +
         "<option value='option1' selected='selected'>Option 1</option>" +
         "<option value='option2'>Option 2</option>" +
         "</select>" +
         "<input type='checkbox' name='checkbox' value='checked' checked='checked' />" +
         "</form></body></html>";

 @Test
 public void testFormElementConstructor() {
 FormElement formElement = Jsoup.parse(TEST_HTML).select("form").first().as(FormElement.class);
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = Jsoup.parse(TEST_HTML).select("form").first().as(FormElement.class);
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = Jsoup.parse(TEST_HTML).select("form").first().as(FormElement.class);
 Element inputElement = new Element("input");
 inputElement.attr("type", "text");
 inputElement.attr("name", "newInput");
 formElement.addElement(inputElement);
 Elements newElements = formElement.elements();
 assertEquals(4, newElements.size());
 }

 @Test
 public void testRemoveChildMethod() {
 FormElement formElement = Jsoup.parse(TEST_HTML).select("form").first().as(FormElement.class);
 Element inputElement = formElement.elements().get(0);
 formElement.removeChild(inputElement);
 Elements newElements = formElement.elements();
 assertEquals(2, newElements.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithEmptyActionAttribute() {
 FormElement formElement = new Element("form", new Attributes().put("", ""));
 try {
 formElement.submit();
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitMethodWithValidActionAttribute() {
 FormElement formElement = Jsoup.parse(TEST_HTML).select("form").first().as(FormElement.class);
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataMethodWithEmptyFormElement() {
 FormElement formElement = new Element("form");
 try {
 formElement.formData();
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormDataMethodWithValidFormElement() {
 FormElement formElement = Jsoup.parse(TEST_HTML).select("form").first().as(FormElement.class);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 }

 @Test
 public void testFormDataMethodWithSelectElement() {
 FormElement formElement = Jsoup.parse(TEST_HTML).select("form").first().as(FormElement.class);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("option1", formData.get(0).val());
 assertEquals("username", formData.get(1).key);
 }

 @Test
 public void testFormDataMethodWithCheckboxElement() {
 FormElement formElement = Jsoup.parse(TEST_HTML).select("form").first().as(FormElement.class);
 Element checkboxElement = formElement.elements().get(2);
 checkboxElement.attr("checked", "false");
 formElement.removeChild(checkboxElement);
 checkboxElement.attr("checked", "true");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertFalse(formData.get(2).val().equals("checked"));
 checkboxElement.attr("checked", "false");
 formElement.removeChild(checkboxElement);
 formData = formElement.formData();
 assertTrue(formData.isEmpty());
 }

}