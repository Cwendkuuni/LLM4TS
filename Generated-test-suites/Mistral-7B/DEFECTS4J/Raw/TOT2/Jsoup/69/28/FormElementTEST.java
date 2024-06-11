package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 private FormElement formElement;
 private Element inputElement;
 private Element selectElement;
 private Element textAreaElement;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 Tag formTag = new Tag("form");
 Tag inputTag = new Tag("input");
 inputTag.put("type", "text");
 inputTag.put("name", "username");

 Tag selectTag = new Tag("select");
 selectTag.put("name", "country");

 Tag textAreaTag = new Tag("textarea");
 textAreaTag.put("name", "message");
 textAreaTag.put("rows", "5");

 formElement = new FormElement(formTag, "", new Attributes());
 inputElement = new Element(inputTag, "", new Attributes());
 selectElement = new Element(selectTag, "", new Attributes());
 textAreaElement = new Element(textAreaTag, "", new Attributes());

 formElement.addElement(inputElement);
 formElement.addElement(selectElement);
 formElement.addElement(textAreaElement);
 }

 @Test
 public void testElements() {
 List<Element> elements = formElement.elements();
 assertEquals(3, elements.size());
 assertEquals(inputElement, elements.get(0));
 assertEquals(selectElement, elements.get(1));
 assertEquals(textAreaElement, elements.get(2));
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element(new Tag("button"), "", new Attributes());
 FormElement formElementWithButton = formElement.addElement(newElement);
 List<Element> elements = formElementWithButton.elements();
 assertEquals(4, elements.size());
 assertEquals(newElement, elements.get(3));
 }

 @Test
 public void testSubmitFormWithNoActionUrl() {
 try {
 formElement.submit();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSubmitFormWithEmptyActionUrl() {
 String baseUri = "http://example.com";
 FormElement formElementWithActionUrl = new FormElement(new Tag("form"), baseUri, new Attributes());
 formElementWithActionUrl.attributes().put("action", "");
 try {
 formElementWithActionUrl.submit();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSubmitFormWithValidActionUrl() {
 String baseUri = "http://example.com";
 FormElement formElementWithActionUrl = new FormElement(new Tag("form"), baseUri, new Attributes());
 formElementWithActionUrl.attributes().put("action", "/path/to/submit");
 Connection connection = formElementWithActionUrl.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataWithNoFormControls() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataWithOneTextInput() {
 String inputValue = "John Doe";
 inputElement.attributes().put("value", inputValue);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("username=" + inputValue, formData.get(0).value());
 }

 @Test
 public void testFormDataWithOneSelectElement() {
 String selectOptionValue = "USA";
 selectElement.select("option[value=" + Select.valueAttribute(Select.value("USA")) + "]").first().attributes().put("selected", "selected");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("country=" + selectOptionValue, formData.get(0).value());
 }

 @Test
 public void testFormDataWithOneTextArea() {
 String textAreaValue = "Hello World!";
 textAreaElement.attributes().put("value", textAreaValue);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("message=" + textAreaValue, formData.get(0).value());
 }

 @Test
 public void testFormDataWithOneInputAndOneSelect() {
 String inputValue = "John Doe";
 inputElement.attributes().put("value", inputValue);
 String selectOptionValue = "USA";
 selectElement.select("option[value=" + Select.valueAttribute(Select.value("USA")) + "]").first().attributes().put("selected", "selected");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 List<String> expectedValues = Arrays.asList("username=" + inputValue, "country=" + selectOptionValue);
 for (Connection.KeyVal keyVal : formData) {
 List<String> keyValValues = Arrays.asList(keyVal.key(), keyVal.value());
 assertEquals(expectedValues, keyValValues);
 }
 }

 @Test
 public void testFormDataWithOneTextAreaAndOneInput() {
 String textAreaValue = "Hello World!";
 textAreaElement.attributes().put("value", textAreaValue);
 String inputValue = "John Doe";
 inputElement.attributes().put("value", inputValue);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 List<String> expectedValues = Arrays.asList("message=" + textAreaValue, "username=" + inputValue);
 for (Connection.KeyVal keyVal : formData) {
 List<String> keyValValues = Arrays.asList(keyVal.key(), keyVal.value());
 assertEquals(expectedValues, keyValValues);
 }
 }

 @Test
 public void testFormDataWithTextInputAndSelectWithMultipleOptions() {
 String inputValue = "John Doe";
 inputElement.attributes().put("value", inputValue);
 String selectOptionValue1 = "USA";
 selectElement.select("option[value=" + Select.valueAttribute(Select.value("USA")) + "]").first().attributes().put("selected", "selected");
 String selectOptionValue2 = "Canada";
 selectElement.select("option[value=" + Select.valueAttribute(Select.value("Canada")) + "]").first().attributes().put("selected", "selected");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 List<String> expectedValues = Arrays.asList("username=" + inputValue, "country=" + selectOptionValue1, "country=" + selectOptionValue2);
 for (Connection.KeyVal keyVal : formData) {
 List<String> keyValValues = Arrays.asList(keyVal.key(), keyVal.value());
 assertEquals(expectedValues, keyValValues);
 }
 }
}