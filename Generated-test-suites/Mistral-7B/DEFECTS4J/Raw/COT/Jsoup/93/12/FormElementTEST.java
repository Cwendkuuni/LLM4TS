package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormElementTest {

 private FormElement formElement;
 private Connection connection;
 private Element inputElement;
 private Element selectElement;
 private Element checkboxElement;

 @Before
 public void setup() {
 formElement = new FormElement(Tag.form("form"), "baseUrl", new Attributes());
 connection = mock(Connection.class);
 inputElement = mock(Element.class);
 selectElement = mock(Element.class);
 checkboxElement = mock(Element.class);
 }

 @Test
 public void testFormElementConstructor() {
 FormElement formElement = new FormElement(Tag.form("form"), "baseUrl", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 }

 @Test
 public void testAddElementMethod() {
 formElement.addElement(inputElement);
 assertEquals(1, formElement.elements().size());
 }

 @Test
 public void testSubmitMethodValidFormActionUrl() throws Exception {
 when(formElement.submit().execute().statusCode()).thenReturn(200);
 Connection submittedForm = formElement.submit();
 assertNotNull(submittedForm);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodInvalidFormActionUrl() throws Exception {
 when(formElement.submit().execute().statusCode()).thenThrow(new IOException());
 assertThrows(IllegalArgumentException.class, formElement::submit);
 }

 @Test
 public void testFormDataMethodEmptyForm() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodSingleInputElement() {
 inputElement.setAttribute("name", "inputName");
 inputElement.setAttribute("value", "inputValue");
 formElement.addElement(inputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("inputName", keyVal.key());
 assertEquals("inputValue", keyVal.value());
 }

 @Test
 public void testFormDataMethodSingleSelectElement() {
 String optionValue = "optionValue";
 selectElement.setAttribute("name", "selectName");
 Elements options = new Elements(new Element("option", new Attributes(), optionValue));
 selectElement.addChildren(options);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("selectName", keyVal.key());
 assertEquals(optionValue, keyVal.value());
 }

 @Test
 public void testFormDataMethodMultipleInputElements() {
 String inputValue1 = "inputValue1";
 String inputValue2 = "inputValue2";
 inputElement.setAttribute("name", "inputName1");
 inputElement.setAttribute("value", inputValue1);
 formElement.addElement(inputElement);
 inputElement = mock(Element.class);
 inputElement.setAttribute("name", "inputName2");
 inputElement.setAttribute("value", inputValue2);
 formElement.addElement(inputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(2, formData.size());
 Connection.KeyVal keyVal1 = formData.get(0);
 assertEquals("inputName1", keyVal1.key());
 assertEquals(inputValue1, keyVal1.value());
 Connection.KeyVal keyVal2 = formData.get(1);
 assertEquals("inputName2", keyVal2.key());
 assertEquals(inputValue2, keyVal2.value());
 }

 @Test
 public void testFormDataMethodSingleCheckboxOrRadioElementUnchecked() {
 checkboxElement.setAttribute("name", "checkboxName");
 checkboxElement.setAttribute("type", "checkbox");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodSingleCheckboxOrRadioElementChecked() {
 checkboxElement.setAttribute("name", "checkboxName");
 checkboxElement.setAttribute("type", "checkbox");
 checkboxElement.setAttribute("checked", "checked");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("checkboxName", keyVal.key());
 assertEquals("on", keyVal.value());
 }

 @Test
 public void testFormDataMethodMultipleCheckboxOrRadioElementsUnchecked() {
 checkboxElement.setAttribute("name", "checkboxName1");
 checkboxElement.setAttribute("type", "checkbox");
 formElement.addElement(checkboxElement);
 checkboxElement = mock(Element.class);
 checkboxElement.setAttribute("name", "checkboxName2");
 checkboxElement.setAttribute("type", "checkbox");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodMultipleCheckboxOrRadioElementsChecked() {
 checkboxElement.setAttribute("name", "checkboxName1");
 checkboxElement.setAttribute("type", "checkbox");
 checkboxElement.setAttribute("checked", "checked");
 formElement.addElement(checkboxElement);
 checkboxElement = mock(Element.class);
 checkboxElement.setAttribute("name", "checkboxName2");
 checkboxElement.setAttribute("type", "checkbox");
 checkboxElement.setAttribute("checked", "checked");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(2, formData.size());
 Connection.KeyVal keyVal1 = formData.get(0);
 assertEquals("checkboxName1", keyVal1.key());
 assertEquals("on", keyVal1.value());
 Connection.KeyVal keyVal2 = formData.get(1);
 assertEquals("checkboxName2", keyVal2.key());
 assertEquals("on", keyVal2.value());
 }

 @Test
 public void testFormDataMethodMixedInputElements() {
 String inputValue1 = "inputValue1";
 String inputValue2 = "inputValue2";
 String selectOptionValue = "optionValue";
 inputElement.setAttribute("name", "inputName1");
 inputElement.setAttribute("value", inputValue1);
 formElement.addElement(inputElement);
 selectElement.setAttribute("name", "selectName");
 Elements options = new Elements(new Element("option", new Attributes(), selectOptionValue));
 selectElement.addChildren(options);
 formElement.addElement(selectElement);
 checkboxElement.setAttribute("name", "checkboxName");
 checkboxElement.setAttribute("type", "checkbox");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(3, formData.size());
 Connection.KeyVal keyVal1 = formData.get(0);
 assertEquals("inputName1", keyVal1.key());
 assertEquals(inputValue1, keyVal1.value());
 Connection.KeyVal keyVal2 = formData.get(1);
 assertEquals("selectName", keyVal2.key());
 assertEquals(selectOptionValue, keyVal2.value());
 Connection.KeyVal keyVal3 = formData.get(2);
 assertEquals("checkboxName", keyVal3.key());
 assertEquals("on", keyVal3.value());
 }
}