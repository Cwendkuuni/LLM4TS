package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private FormElement createFormElement(Tag tag, String baseUri, Attributes attributes) {
 return new FormElement(tag, baseUri, attributes);
 }

 @Test
 public void testFormElementConstructor() {
 Tag formTag = new Tag("form");
 FormElement formElement = createFormElement(formTag, "", new Attributes());
 Assert.assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = createFormElement(new Tag("form"), "", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 formElement.addElement(textInput);
 Elements elements = formElement.elements();
 Assert.assertEquals(1, elements.size());
 Assert.assertTrue(elements.contains(textInput));
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = createFormElement(new Tag("form"), "", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 FormElement result = formElement.addElement(textInput);
 Assert.assertSame(result, formElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodEmptyAction() {
 FormElement formElement = createFormElement(new Tag("form"), "", new Attributes());
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitMethodNullElements() {
 FormElement formElement = createFormElement(new Tag("form"), "", new Attributes());
 formElement.elements().clear();
 try {
 formElement.submit();
 } catch (IllegalStateException e) {
 Assert.assertEquals("Form cannot be submitted with no form controls.", e.getMessage());
 }
 }

 @Test
 public void testFormDataMethodEmptyForm() {
 FormElement formElement = createFormElement(new Tag("form"), "", new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodSingleTextInput() {
 FormElement formElement = createFormElement(new Tag("form"), "", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 formElement.addElement(textInput);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("username", keyVal.key());
 Assert.assertEquals("", keyVal.value());
 }

 @Test
 public void testFormDataMethodMultipleTextInput() {
 FormElement formElement = createFormElement(new Tag("form"), "", new Attributes());
 Element textInput1 = new Element("input", new Attributes().put("type", "text").put("name", "username1"));
 Element textInput2 = new Element("input", new Attributes().put("type", "text").put("name", "username2"));
 formElement.addElement(textInput1);
 formElement.addElement(textInput2);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(2, formData.size());
 Connection.KeyVal keyVal1 = formData.get(0);
 Assert.assertEquals("username1", keyVal1.key());
 Assert.assertEquals("", keyVal1.value());
 Connection.KeyVal keyVal2 = formData.get(1);
 Assert.assertEquals("username2", keyVal2.key());
 Assert.assertEquals("", keyVal2.value());
 }

 @Test
 public void testFormDataMethodSelectElement() {
 FormElement formElement = createFormElement(new Tag("form"), "", new Attributes());
 Element selectElement = new Element("select", new Attributes().put("name", "selection"));
 Element option1 = new Element("option", new Attributes().put("value", "Option1").put("selected", "selected"));
 Element option2 = new Element("option", new Attributes().put("value", "Option2"));
 selectElement.addElement(option1);
 selectElement.addElement(option2);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("selection", keyVal.key());
 Assert.assertEquals("Option1", keyVal.value());
 }

 @Test
 public void testFormDataMethodCheckboxOrRadio() {
 FormElement formElement = createFormElement(new Tag("form"), "", new Attributes());
 Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "option1").put("checked", "checked"));
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("option1", keyVal.key());
 Assert.assertEquals("true", keyVal.value());
 }
}