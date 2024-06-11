package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private static final String TEST_BASE_URI = "http://test.com";
 private static final String TEST_ACTION_URI = "http://test.com/submit";

 @Test
 public void testFormElementConstructor() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 assertEquals("form", formElement.tagName());
 assertEquals(TEST_BASE_URI, formElement.baseUri());
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 Elements elements = formElement.elements();
 assertEquals(0, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 Element inputElement = new Elements(new Tag("input")).first();
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(inputElement, elements.first());
 }

 @Test
 public void testRemoveChildMethod() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 Element inputElement = new Elements(new Tag("input")).first();
 formElement.addElement(inputElement);
 formElement.removeChild(inputElement);
 Elements elements = formElement.elements();
 assertEquals(0, elements.size());
 }

 @Test
 public void testSubmitMethodWithValidUrl() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 formElement.addElement(new Elements(new Tag("input")).first());
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithoutBaseUri() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 formElement.addElement(new Elements(new Tag("input")).first());
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithoutActionUrl() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormDataMethodWithSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 Element selectElement = new Elements(new Tag("select"))
         .attr("name", "test")
         .add(new Elements(new Tag("option").attr("value", "one")))
         .add(new Elements(new Tag("option").attr("selected", "selected")))
         .add(new Elements(new Tag("option").attr("value", "three")))
         .first();
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key());
 assertEquals("two", formData.get(0).value());
 }

 @Test
 public void testFormDataMethodWithSelectElementNoSelectedOption() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 Element selectElement = new Elements(new Tag("select"))
         .attr("name", "test")
         .add(new Elements(new Tag("option").attr("value", "one")))
         .add(new Elements(new Tag("option").attr("value", "two")))
         .first();
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodWithCheckboxElement() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 Element checkboxElement = new Elements(new Tag("input"))
         .attr("type", "checkbox")
         .attr("name", "test")
         .attr("checked", "checked")
         .first();
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key());
 assertEquals("on", formData.get(0).value());
 }

 @Test
 public void testFormDataMethodWithTextField() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 Element textFieldElement = new Elements(new Tag("input"))
         .attr("type", "text")
         .attr("name", "test")
         .attr("value", "testValue")
         .first();
 formElement.addElement(textFieldElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key());
 assertEquals("testValue", formData.get(0).value());
 }

 @Test
 public void testFormDataMethodWithMixedElements() {
 FormElement formElement = new FormElement(new Tag("form"), TEST_BASE_URI, new Attributes());
 formElement.addElement(new Elements(new Tag("input"))
         .attr("type", "checkbox")
         .attr("name", "testCheckbox")
         .attr("checked", "checked")
         .first());
 formElement.addElement(new Elements(new Tag("select"))
         .attr("name", "testSelect")
         .add(new Elements(new Tag("option").attr("value", "one")))
         .add(new Elements(new Tag("option").attr("selected", "selected")))
         .first());
 formElement.addElement(new Elements(new Tag("input"))
         .attr("type", "text")
         .attr("name", "testTextField")
         .attr("value", "testValue")
         .first());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 assertEquals("testCheckbox", formData.get(0).key());
 assertEquals("on", formData.get(0).value());
 assertEquals("testSelect", formData.get(1).key());
 assertEquals("two", formData.get(1).value());
 assertEquals("testTextField", formData.get(2).key());
 assertEquals("testValue", formData.get(2).value());
 }
}