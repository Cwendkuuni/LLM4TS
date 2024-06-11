package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.FormElement;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String TEST_BASE_URI = "http://test.com";
 private static final String TEST_FORM_ACTION = "http://test.com/form";

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), TEST_BASE_URI, new Attributes());
 Assert.assertNotNull(formElement);
 }

 @Test
 public void testGetFormControls() {
 FormElement formElement = createFormElement();
 Elements formControls = formElement.elements();
 Assert.assertNotNull(formControls);
 Assert.assertEquals(0, formControls.size());

 formElement.addElement(createInputElement("text", "testInput"));
 formElement.addElement(createSelectElement("select", "testSelect"));
 formControls = formElement.elements();
 Assert.assertNotNull(formControls);
 Assert.assertEquals(2, formControls.size());
 }

 @Test
 public void testSubmitForm() {
 FormElement formElement = createFormElement();
 formElement.addElement(createInputElement("text", "testInput"));
 formElement.addElement(createSelectElement("select", "testSelect"));

 Connection connection = formElement.submit();
 Assert.assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithEmptyActionUrl() {
 FormElement formElement = createFormElement();
 formElement.addElement(createInputElement("text", "testInput"));

 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 }
 }

 @Test
 public void testFormDataEmptyForm() {
 FormElement formElement = createFormElement();
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertNotNull(formData);
 Assert.assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataSingleInput() {
 FormElement formElement = createFormElement();
 formElement.addElement(createInputElement("text", "testInput", "Test Value"));
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertNotNull(formData);
 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("testInput", keyVal.key());
 Assert.assertEquals("Test Value", keyVal.value());
 }

 @Test
 public void testFormDataSingleSelect() {
 FormElement formElement = createFormElement();
 formElement.addElement(createSelectElement("select", "testSelect", "Option 1", "Option 1 Value"));
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertNotNull(formData);
 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("testSelect", keyVal.key());
 Assert.assertEquals("Option 1 Value", keyVal.value());
 }

 @Test
 public void testFormDataMultipleInputs() {
 FormElement formElement = createFormElement();
 formElement.addElement(createInputElement("text", "testInput1", "Test Value 1"));
 formElement.addElement(createInputElement("text", "testInput2", "Test Value 2"));
 formElement.addElement(createSelectElement("select", "testSelect", "Option 1", "Option 1 Value"));
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertNotNull(formData);
 Assert.assertEquals(3, formData.size());
 }

 @Test
 public void testFormDataMultipleSelects() {
 FormElement formElement = createFormElement();
 formElement.addElement(createSelectElement("select", "testSelect1", "Option 1", "Option 1 Value"));
 formElement.addElement(createSelectElement("select", "testSelect2", "Option 2", "Option 2 Value"));
 formElement.addElement(createInputElement("text", "testInput", "Test Value"));
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertNotNull(formData);
 Assert.assertEquals(3, formData.size());
 }

 private Element createFormElement() {
 return new FormElement(new Tag("form", new Attributes()), TEST_BASE_URI, new Attributes());
 }

 private Element createInputElement(String type, String name, String value) {
 return new Element("input", new Attributes().put("type", type).put("name", name).put("value", value));
 }

 private Element createSelectElement(String tagName, String name, String... options) {
 Element selectElement = new Element(tagName, new Attributes().put("name", name));
 for (String option : options) {
 Element optionElement = new Element("option", new Attributes().put("value", option));
 optionElement.text(option);
 selectElement.appendText(optionElement);
 }
 return selectElement;
 }

}