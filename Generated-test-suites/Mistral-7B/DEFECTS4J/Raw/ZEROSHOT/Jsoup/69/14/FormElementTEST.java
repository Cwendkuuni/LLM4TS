package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElements() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "name"));
 formElement.addElement(textInput);
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(1, elements.size());
 assertEquals(textInput, elements.get(0));
 }

 @Test
 public void testSubmit() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "name"));
 formElement.addElement(textInput);
 formElement.textInput().val("value");
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidActionUrl() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormData() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "name"));
 formElement.addElement(textInput);
 textInput.val("value");
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("name", keyVal.key());
 assertEquals("value", keyVal.val());
 }

 @Test
 public void testFormDataWithMultipleInputs() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element textInput1 = new Element("input", new Attributes().put("type", "text").put("name", "name1"));
 Element textInput2 = new Element("input", new Attributes().put("type", "text").put("name", "name2"));
 formElement.addElement(textInput1);
 formElement.addElement(textInput2);
 textInput1.val("value1");
 textInput2.val("value2");
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(2, formData.size());
 Connection.KeyVal keyVal1 = formData.get(0);
 assertEquals("name1", keyVal1.key());
 assertEquals("value1", keyVal1.val());
 Connection.KeyVal keyVal2 = formData.get(1);
 assertEquals("name2", keyVal2.key());
 assertEquals("value2", keyVal2.val());
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element selectElement = new Element("select", new Attributes().put("name", "name"));
 Element option1 = new Element("option", Arrays.asList("value1", "Option 1"));
 Element option2 = new Element("option", Arrays.asList("value2", "Option 2"));
 selectElement.add(option1);
 selectElement.add(option2);
 selectElement.select("option[selected]").get(0).select("option").first().attr("selected", "selected");
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("name", keyVal.key());
 assertEquals("value1", keyVal.val());
 }

 @Test
 public void testFormDataWithCheckboxOrRadioElement() {
 FormElement formElement = new FormElement(new Tag("form"), "baseUri", new Attributes());
 Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "name"));
 formElement.addElement(checkboxElement);
 checkboxElement.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("name", keyVal.key());
 assertEquals("on", keyVal.val());
 }
}