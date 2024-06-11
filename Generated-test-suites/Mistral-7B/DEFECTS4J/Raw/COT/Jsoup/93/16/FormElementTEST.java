package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

 private static final String TEST_URL = "http://test.com";

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_URL, new Attributes());
 Assert.assertEquals("form", formElement.tagName());
 }

 @Test
 public void testGetElements() {
 FormElement formElement = createFormElementWithChildren("form", TEST_URL, new Attributes());
 Elements elements = formElement.elements();
 Assert.assertNotNull(elements);
 Assert.assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_URL, new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "test"));
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 Assert.assertEquals(1, elements.size());
 Assert.assertEquals(inputElement, elements.first());
 }

 @Test
 public void testRemoveElement() {
 FormElement formElement = createFormElementWithChildren("form", TEST_URL, new Attributes());
 Element inputElement = formElement.elements().first();
 formElement.removeChild(inputElement);
 Elements elements = formElement.elements();
 Assert.assertEquals(2, elements.size());
 Assert.assertNotEquals(inputElement, elements.first());
 }

 @Test
 public void testSubmitWithValidAction() throws Exception {
 FormElement formElement = createFormElementWithChildren("form", TEST_URL + "/submit", new Attributes().put("action", TEST_URL + "/submit"));
 Connection connection = formElement.submit();
 Assert.assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidAction() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), TEST_URL, new Attributes());
 // Absolute action URL should not be empty
 Assert.assertThrows(IllegalArgumentException.class, formElement::submit);
 }

 @Test
 public void testFormDataForSelect() {
 FormElement formElement = createFormElementWithSelect("form", TEST_URL, new Attributes().put("action", TEST_URL + "/submit"));
 Element selectElement = new Element("select", new Attributes().put("name", "testSelect"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "option1").put("selected", "true"));
 Element optionElement2 = new Element("option", new Attributes().put("value", "option2"));
 selectElement.add(optionElement1);
 selectElement.add(optionElement2);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(2, formData.size());
 Assert.assertEquals("testSelect", formData.get(0).key);
 Assert.assertEquals("option1", formData.get(0).value);
 }

 @Test
 public void testFormDataForCheckboxRadioWithChecked() {
 FormElement formElement = createFormElementWithCheckbox("form", TEST_URL, new Attributes().put("action", TEST_URL + "/submit"));
 Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "testCheckbox").put("checked", "true"));
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Assert.assertEquals("testCheckbox", formData.get(0).key);
 Assert.assertEquals("on", formData.get(0).value);
 }

 @Test
 public void testFormDataForCheckboxRadioWithoutChecked() {
 FormElement formElement = createFormElementWithCheckbox("form", TEST_URL, new Attributes().put("action", TEST_URL + "/submit"));
 Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "testCheckbox"));
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataForTextInput() {
 FormElement formElement = createFormElementWithTextInput("form", TEST_URL, new Attributes().put("action", TEST_URL + "/submit"));
 Element textInputElement = new Element("input", new Attributes().put("type", "text").put("name", "testTextInput").put("value", "testValue"));
 formElement.addElement(textInputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Assert.assertEquals("testTextInput", formData.get(0).key);
 Assert.assertEquals("testValue", formData.get(0).value);
 }

 @Test
 public void testFormDataForTextInputEmpty() {
 FormElement formElement = createFormElementWithTextInput("form", TEST_URL, new Attributes().put("action", TEST_URL + "/submit"));
 Element textInputElement = new Element("input", new Attributes().put("type", "text").put("name", "testTextInput"));
 formElement.addElement(textInputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 }

 private FormElement createFormElementWithChildren(String tag, String baseUrl, Attributes attributes) {
 FormElement formElement = new FormElement(new Tag(tag), baseUrl, attributes);
 Element inputElement1 = new Element("input", new Attributes().put("type", "text").put("name", "testInput1"));
 Element inputElement2 = new Element("input", new Attributes().put("type", "text").put("name", "testInput2"));
 Element selectElement = new Element("select", new Attributes().put("name", "testSelect"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "option1"));
 Element optionElement2 = new Element("option", new Attributes().put("value", "option2"));
 selectElement.add(optionElement1);
 selectElement.add(optionElement2);
 formElement.addElement(inputElement1);
 formElement.addElement(inputElement2);
 formElement.addElement(selectElement);
 return formElement;
 }

 private FormElement createFormElementWithSelect(String tag, String baseUrl, Attributes attributes) {
 FormElement formElement = new FormElement(new Tag(tag), baseUrl, attributes);
 Element selectElement = new Element("select", new Attributes().put("name", "testSelect"));
 formElement.addElement(selectElement);
 return formElement;
 }

 private FormElement createFormElementWithCheckbox(String tag, String baseUrl, Attributes attributes) {
 FormElement formElement = new FormElement(new Tag(tag), baseUrl, attributes);
 Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "testCheckbox"));
 formElement.addElement(checkboxElement);
 return formElement;
 }

 private FormElement createFormElementWithTextInput(String tag, String baseUrl, Attributes attributes) {
 FormElement formElement = new FormElement(new Tag(tag), baseUrl, attributes);
 Element textInputElement = new Element("input", new Attributes().put("type", "text").put("name", "testTextInput"));
 formElement.addElement(textInputElement);
 return formElement;
 }
}