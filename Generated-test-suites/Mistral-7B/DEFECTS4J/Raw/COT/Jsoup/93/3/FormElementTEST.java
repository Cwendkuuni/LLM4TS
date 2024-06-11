package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String TEST_HTML = "''<form action='http://example.com'> " +
         "<input type='text' name='username' /> " +
         "<select name='colors'> " +
         "<option value='red' selected>Red</option>" +
         "<option value='green'>Green</option>" +
         "<option value='blue'>Blue</option>" +
         "</select>" +
         "<input type='checkbox' name='agree' checked /> " +
         "<input type='submit' value='Submit' /> " +
         "</form>'";

 @Test
 public void testFormElementCreation() {
 FormElement formElement = parseFormElement(TEST_HTML);
 assertTrue(formElement instanceof FormElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = parseFormElement(TEST_HTML);
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element textInput = new Elements(new Tag("input")).first();
 textInput.attributes().put("type", "text");
 textInput.attributes().put("name", "username");
 formElement.addElement(textInput);
 assertEquals(1, formElement.elements().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormData() {
 FormElement formElement = parseFormElement(TEST_HTML);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 assertEquals("username", formData.get(0).key);
 assertEquals("", formData.get(0).value);
 assertEquals("colors", formData.get(1).key);
 assertEquals("red", formData.get(1).value);
 assertEquals("agree", formData.get(2).key);
 assertEquals("on", formData.get(2).value);
 }

 @Test
 public void testFormDataWithEmptyInput() {
 FormElement formElement = parseFormElement(TEST_HTML);
 Elements inputs = formElement.elements().select("input[name='username']");
 inputs.first().val("");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 assertEquals("username", formData.get(0).key);
 assertEquals("", formData.get(0).value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataWithDisabledInput() {
 FormElement formElement = parseFormElement(TEST_HTML);
 Elements inputs = formElement.elements().select("input[name='username']");
 inputs.first().attributes().put("disabled", "");
 List<Connection.KeyVal> formData = formElement.formData();
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = parseFormElement(TEST_HTML);
 Elements selectElements = formElement.elements().select("select[name='colors']");
 Elements options = selectElements.first().select("option");
 options.last().attributes().put("selected", "");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 assertEquals("colors", formData.get(1).key);
 assertEquals("blue", formData.get(1).value);
 }

 @Test
 public void testFormDataWithCheckboxOrRadio() {
 FormElement formElement = parseFormElement(TEST_HTML);
 Elements checkboxOrRadioElements = formElement.elements().select("input[type='checkbox'][name='agree']");
 checkboxOrRadioElements.first().attributes().remove("checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 }

 @Test
 public void testSubmit() throws Exception {
 FormElement formElement = parseFormElement(TEST_HTML);
 Connection connection = formElement.submit();
 String response = connection.body();
 assertEquals("Submit", connection.response().statusCode());
 }

 private static FormElement parseFormElement(String html) {
 Parser parser = new Parser.malformedHtmlParser();
 Document doc = parser.parseBodyFragment(html);
 FormElement formElement = (FormElement) doc.get(0);
 return formElement;
 }
}