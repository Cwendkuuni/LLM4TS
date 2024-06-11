package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 // Test creating a new FormElement with valid arguments
 Tag tag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 FormElement formElement = new FormElement(tag, baseUri, attributes);

 // Test getting the elements() list is empty by default
 assertEquals(0, formElement.elements().size());
 }

 @Test
 public void testFormElementAddElement() {
 // Test adding an element to the FormElement
 Tag inputTag = new Tag("input");
 String inputName = "username";
 Attributes inputAttributes = new Attributes().put("name", inputName);
 Element inputElement = new Element(inputTag, "", inputAttributes);

 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.addElement(inputElement);

 // Test that the element has been added to the FormElement
 Element formElementChild = formElement.elements().first();
 assertEquals(inputElement, formElementChild);
 }

 @Test
 public void testFormElementRemoveElement() {
 // Test removing an element from the FormElement
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element inputElement = new Element(new Tag("input"), "", new Attributes());
 formElement.addElement(inputElement);
 formElement.removeChild(inputElement);

 // Test that the element has been removed from the FormElement
 assertEquals(0, formElement.elements().size());
 }

 @Test
 public void testFormElementSubmit() {
 // Test creating a Connection object from the FormElement's submit method
 FormElement formElement = createFormElementWithSubmitAction();
 Connection connection = formElement.submit();

 // Test that a Connection object is returned
 assertNotNull(connection);

 // Test that the Connection's method is set to GET or POST based on the form's method attribute
 Attributes formAttributes = formElement.attributes();
 String formMethod = formAttributes.get("method");
 Connection.Method connectionMethod = null;
 if (formMethod != null && formMethod.equalsIgnoreCase("post")) {
 connectionMethod = Connection.Method.POST;
 } else {
 connectionMethod = Connection.Method.GET;
 }
 assertEquals(connectionMethod, connection.method());
 }

 @Test
 public void testFormElementFormDataTextInputs() {
 // Test creating FormElement with text inputs and getting the form data
 FormElement formElement = createFormElementWithTextInputs();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());

 // Test that the form data contains the expected key-value pairs
 String input1Name = "input1";
 String input1Value = "value1";
 String input2Name = "input2";
 String input2Value = "value2";

 String expectedInput1Key = input1Name;
 String expectedInput1Value = input1Value;
 String expectedInput2Key = input2Name;
 String expectedInput2Value = input2Value;

 Connection.KeyVal keyVal1 = formData.get(0);
 assertEquals(expectedInput1Key, keyVal1.key());
 assertEquals(expectedInput1Value, keyVal1.val());

 Connection.KeyVal keyVal2 = formData.get(1);
 assertEquals(expectedInput2Key, keyVal2.key());
 assertEquals(expectedInput2Value, keyVal2.val());
 }

 @Test
 public void testFormElementFormDataSelectElement() {
 // Test creating FormElement with a select element and getting the form data
 FormElement formElement = createFormElementWithSelectElement();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());

 // Test that the form data contains the expected key-value pair
 String selectName = "select";
 String expectedOptionValue = "option2";

 Connection.KeyVal keyVal = formData.get(0);
 assertEquals(selectName, keyVal.key());
 assertEquals(expectedOptionValue, keyVal.val());
 }

 @Test
 public void testFormElementFormDataCheckboxOrRadio() {
 // Test creating FormElement with checkbox or radio elements and getting the form data
 FormElement formElement = createFormElementWithCheckboxOrRadio();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());

 // Test that the form data contains the expected key-value pair
 String checkboxOrRadioName = "checkboxOrRadio";

 Connection.KeyVal keyVal = formData.get(0);
 assertEquals(checkboxOrRadioName, keyVal.key());
 assertEquals("on", keyVal.val());
 }

 private FormElement createFormElementWithSubmitAction() {
 Parser parser = new Parser();
 String xml = "<form action='http://example.com/submit' method='post'>...</form>";
 Document document = parser.parse(xml);
 Element formElement = document.select("form").first();
 return (FormElement) formElement;
 }

 private FormElement createFormElementWithTextInputs() {
 Parser parser = new Parser();
 String xml = "<form>\n" +
 "  <input name='input1' type='text' value='value1'>\n" +
 "  <input name='input2' type='text' value='value2'>\n" +
 "  <input name='submit' type='submit'>\n" +
 "</form>";
 Document document = parser.parse(xml);
 Element formElement = document.select("form").first();
 return (FormElement) formElement;
 }

 private FormElement createFormElementWithSelectElement() {
 Parser parser = new Parser();
 String xml = "<form>\n" +
 "  <label for='select'>\n" +
 "    Select an option:\n" +
 "  </label>\n" +
 "  <select name='select'>\n" +
 "    <option value='option1'>Option 1</option>\n" +
 "    <option value='option2' selected>\n" +
 "      Option 2\n" +
 "    </option>\n" +
 "  </select>\n" +
 "  <input name='submit' type='submit'>\n" +
 "</form>";
 Document document = parser.parse(xml);
 Element formElement = document.select("form").first();
 return (FormElement) formElement;
 }

 private FormElement createFormElementWithCheckboxOrRadio() {
 Parser parser = new Parser();
 String xml = "<form>\n" +
 "  <input name='checkboxOrRadio' type='checkbox' checked>\n" +
 "  <input name='submit' type='submit'>\n" +
 "</form>";
 Document document = parser.parse(xml);
 Element formElement = document.select("form").first();
 return (FormElement) formElement;
 }
}