package org.jsoup.nodes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private FormElement formElement;
 private Element inputElement;
 private Element textAreaElement;
 private Element selectElement;

 @Before
 public void setUp() {
 formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 inputElement = new Element("input", new Attributes().put("type", "text").put("name", "inputName"));
 textAreaElement = new Element("textarea", new Attributes().put("name", "textAreaName"));
 selectElement = new Element("select", new Attributes().put("name", "selectName"));
 formElement.addElement(inputElement);
 formElement.addElement(textAreaElement);
 formElement.addElement(selectElement);
 }

 @Test
 public void testFormElements() {
 assertNotNull(formElement.elements());
 assertFalse(formElement.elements().isEmpty());
 }

 @Test
 public void testAddElement() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element elementToAdd = new Element("input", new Attributes().put("type", "text").put("name", "addName"));
 formElement1.addElement(elementToAdd);
 assertSame(formElement1, formElement1.addElement(elementToAdd));
 }

 @Test
 public void testGetElements() {
 assertSame(formElement, formElement.elements().get(0));
 }

 @Test
 public void testGetFormControlWithInputTypeText() {
 Element expectedInput = inputElement;
 Element actualInput = formElement.elements().stream()
 .filter(e -> e.tagName().equals("input"))
 .filter(e -> "text".equalsIgnoreCase(e.attr("type")))
 .findFirst()
 .orElse(null);
 assertSame(expectedInput, actualInput);
 }

 @Test
 public void testGetFormControlWithInputTypeTextAndName() {
 Element expectedInput = inputElement;
 Element actualInput = formElement.elements()
 .stream()
 .filter(e -> e.tagName().equals("input"))
 .filter(e -> "text".equalsIgnoreCase(e.attr("type")))
 .filter(e -> "inputName".equals(e.attr("name")))
 .findFirst()
 .orElse(null);
 assertSame(expectedInput, actualInput);
 }

 @Test
 public void testGetFormControlWithInputTypeTextAndNoName() {
 Element expectedNull = null;
 Element actualInput = formElement.elements()
 .stream()
 .filter(e -> e.tagName().equals("input"))
 .filter(e -> "text".equalsIgnoreCase(e.attr("type")))
 .filter(e -> "".equals(e.attr("name")))
 .findFirst()
 .orElse(null);
 assertSame(expectedNull, actualInput);
 }

 @Test
 public void testGetFormControlWithInputTypeTextAndMultipleElements() {
 Element expectedInput1 = inputElement;
 Element expectedInput2 = new Element("input", new Attributes().put("type", "text").put("name", "inputName2"));
 formElement.addElement(expectedInput2);
 List<Element> actualInputs = formElement.elements()
 .stream()
 .filter(e -> e.tagName().equals("input"))
 .filter(e -> "text".equalsIgnoreCase(e.attr("type")))
 .collect(Collectors.toList());
 List<Element> expectedInputs = new ArrayList<>();
 expectedInputs.add(expectedInput1);
 expectedInputs.add(expectedInput2);
 assertEquals(expectedInputs, actualInputs);
 }

 @Test
 public void testGetFormControlWithSelectElement() {
 Element expectedSelect = selectElement;
 Element actualSelect = formElement.elements()
 .stream()
 .filter(e -> e.tagName().equals("select"))
 .findFirst()
 .orElse(null);
 assertSame(expectedSelect, actualSelect);
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitWithoutActionAttribute() {
 formElement.submit();
 }

 @Test
 public void testFormDataWithInputTypeText() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("inputName", "inputValue"));
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataWithInputTypeTextAndName() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("inputName", "inputValue"));
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataWithInputTypeTextAndNoName() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 List<Connection.KeyVal> actualData = formElement.formData();
 assertFalse(expectedData.equals(actualData));
 }

 @Test
 public void testFormDataWithSelectElement() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("selectName", "optionValue1"));
 Element optionElement = new Element("option", new Attributes().put("value", "optionValue1"));
 selectElement.add(optionElement);
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataWithMultipleElements() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("inputName", "inputValue1"));
 expectedData.add(new Connection.KeyVal("textAreaName", "textValue"));
 expectedData.add(new Connection.KeyVal("selectName", "optionValue1"));
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataWithCheckboxOrRadio() {
 Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxName"));
 Element radioElement1 = new Element("input", new Attributes().put("type", "radio").put("name", "radioName").put("checked", "checked"));
 Element radioElement2 = new Element("input", new Attributes().put("type", "radio").put("name", "radioName"));
 formElement.addElement(checkboxElement);
 formElement.addElement(radioElement1);
 formElement.addElement(radioElement2);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("checkboxName", "true"));
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataWithEmptySelect() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 List<Connection.KeyVal> actualData = formElement.formData();
 assertFalse(expectedData.equals(actualData));
 }

 @After
 public void tearDown() {
 formElement = null;
 inputElement = null;
 textAreaElement = null;
 selectElement = null;
 }
}