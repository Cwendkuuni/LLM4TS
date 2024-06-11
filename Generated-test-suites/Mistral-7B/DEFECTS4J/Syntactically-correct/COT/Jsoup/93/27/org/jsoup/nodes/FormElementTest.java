package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;

import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

 FormElement formElement; // FormElement object to be tested
 Element inputElement; // Input element to be used in tests
 Element selectElement; // Select element to be used in tests
 Element formElementWithNoAction; // FormElement object with no action attribute

 @Before
 public void setUp() {
 ParseSetup parseSetup = new ParseSetup.Builder().build();
 String html = "<form><input name=\"inputName\" type=\"text\"><select name=\"selectName\"><option value=\"option1\">Option 1</option><option value=\"option2\">Option 2</option></select></form>";
 Document doc = Jsoup.parse(html, parseSetup);
 formElement = (FormElement) doc.selectFirst("form");
 inputElement = doc.selectFirst("input[name=inputName]");
 selectElement = doc.selectFirst("select[name=selectName]");
 formElementWithNoAction = (FormElement) doc.selectFirst("form[action='']");
 }

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 Elements expectedElements = new Elements().add(inputElement).add(selectElement);
 assertEquals(expectedElements, formElement.elements());
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElementWithInput = formElement.addElement(inputElement);
 assertEquals(formElementWithInput, formElement.addElement(inputElement));
 assertEquals(2, formElementWithInput.elements().size());
 }

 @Test
 public void testRemoveChildMethod() {
 FormElement formElementWithInputAndSelect = formElement.addElement(inputElement).addElement(selectElement);
 Element removedElement = formElementWithInputAndSelect.elements().get(0);
 formElementWithInputAndSelect.removeChild(removedElement);
 assertEquals(1, formElementWithInputAndSelect.elements().size());
 }

 @Test
 public void testSubmitMethodWithValidAction() throws Exception {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithNoAction() {
 formElementWithNoAction.submit();
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testFormDataMethod() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("inputName", ""));
 expectedData.add(HttpConnection.KeyVal.create("selectName", "option1"));
 assertEquals(expectedData, formElement.formData());
 }

 @Test
 public void testFormDataMethodWithNoFormControls() {
 FormElement formElementWithNoFormControls = new FormElement(new Tag("form"), "", new Attributes());
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 assertEquals(expectedData, formElementWithNoFormControls.formData());
 }

 @Test
 public void testFormDataMethodWithDisabledInput() {
 inputElement.attr("disabled", "disabled");
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 assertEquals(expectedData, formElement.formData());
 }

 @Test
 public void testFormDataMethodWithMultipleSelectOptions() {
 selectElement.select("option[selected]").remove();
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("selectName", "option2"));
 assertEquals(expectedData, formElement.formData());
 }

 @Test
 public void testFormDataMethodWithCheckboxOrRadio() {
 Element checkboxElement = new Element("input", new Attributes().put("name", "checkboxName").put("type", "checkbox"));
 checkboxElement.attr("checked", "checked");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("checkboxName", "on"));
 assertEquals(expectedData, formElement.formData());
 }

 @Test
 public void testFormDataMethodWithMultipleCheckboxesOrRadios() {
 Element checkboxElement1 = new Element("input", new Attributes().put("name", "checkboxName1").put("type", "checkbox"));
 checkboxElement1.attr("checked", "checked");
 Element checkboxElement2 = new Element("input", new Attributes().put("name", "checkboxName2").put("type", "checkbox"));
 formElement.addElement(checkboxElement1).addElement(checkboxElement2);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("checkboxName1", "on"));
 expectedData.add(HttpConnection.KeyVal.create("checkboxName2", ""));
 assertEquals(expectedData, formElement.formData());
 }
}