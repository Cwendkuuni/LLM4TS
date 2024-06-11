package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
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
 FormElement formElement = createFormElement(formTag, null, null);
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = createFormElement(new Tag("form"), null, null);
 Element inputElement = new Element("input", null, null);
 formElement.addElement(inputElement);
 List<Element> elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = createFormElement(new Tag("form"), null, null);
 Element inputElement = new Element("input", null, null);
 formElement.addElement(inputElement);
 assertEquals(inputElement, formElement.addElement(inputElement));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodNoActionAttribute() {
 FormElement formElement = createFormElement(new Tag("form"), null, null);
 formElement.submit();
 }

 @Test
 public void testSubmitMethodValidActionAttribute() {
 FormElement formElement = createFormElement(new Tag("form", "action", "http://example.com"), null, null);
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataMethodNoNameAttribute() {
 FormElement formElement = createFormElement(new Element("input", null, null), null, null);
 List<Connection.KeyVal> formData = formElement.formData();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFormDataMethodSimpleElement() {
 FormElement formElement = createFormElement(new Element("input", "name", "value"), null, null);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("name", formData.get(0).key);
 assertEquals("value", formData.get(0).val);
 }

 @Test
 public void testFormDataMethodSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), null, null);
 Element selectElement = new Element("select", null, null);
 Element optionElement1 = new Element("option", null, "Option 1");
 Element optionElement2 = new Element("option", null, "Option 2");
 selectElement.add(optionElement1);
 selectElement.add(optionElement2);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertFalse(formData.isEmpty());
 assertEquals("1", formData.get(0).val);
 assertEquals("Option 1", formData.get(0).key);
 }

 @Test
 public void testFormDataMethodCheckboxOrRadioElement() {
 FormElement formElement = new FormElement(new Element("input", "groupName", ""), null, null);
 Element checkboxElement = new Element("input", "checkBoxName", null);
 checkboxElement.attr("checked", "checked");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("checkBoxName", formData.get(0).key);
 assertEquals("", formData.get(0).val);
 }

 @Test
 public void testEqualsMethod() {
 FormElement formElement1 = createFormElement(new Tag("form"), null, null);
 FormElement formElement2 = createFormElement(new Tag("form"), null, null);
 FormElement formElement3 = formElement1;
 FormElement formElement4 = new FormElement(new Tag("form"), null, null);
 assertEquals(formElement3, formElement3);
 assertEquals(formElement1, formElement1);
 assertNotEquals(formElement1, formElement2);
 assertNotEquals(formElement2, formElement3);
 assertNotEquals(formElement1, formElement4);
 }
}