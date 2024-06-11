package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private Element formElement;
 private Element formControlElement;
 private Element selectElement;
 private Element inputElement;

 @Before
 public void setUp() {
 Parser parser = new Parser();

 Tag formTag = new Tag("form");
 Attributes formAttributes = new Attributes();
 formAttributes.put("action", "/test");
 formAttributes.put("method", "get");
 formElement = new FormElement(formTag, "", formAttributes);

 Tag inputTag = new Tag("input");
 Attributes inputAttributes = new Attributes();
 inputAttributes.put("name", "inputName");
 inputAttributes.put("type", "text");
 inputAttributes.put("value", "inputValue");
 inputElement = new Element(inputTag, "", inputAttributes);

 Tag selectTag = new Tag("select");
 Attributes selectAttributes = new Attributes();
 selectAttributes.put("name", "selectName");
 selectElement = new Element(selectTag, "", selectAttributes);

 Element optionTag1 = new Tag("option");
 Attributes optionAttributes1 = new Attributes();
 optionAttributes1.put("value", "optionValue1");
 Element optionElement1 = new Element(optionTag1, "", optionAttributes1);
 selectElement.appendText("Option 1");
 selectElement.appendChild(optionElement1);

 Element optionTag2 = new Tag("option");
 Attributes optionAttributes2 = new Attributes();
 optionAttributes2.put("value", "optionValue2");
 Element optionElement2 = new Element(optionTag2, "", optionAttributes2);
 selectElement.appendText("Option 2");
 selectElement.appendChild(optionElement2);

 formControlElement = new Element("div", "", new Attributes().put("id", "formControlId"));
 formControlElement.appendChild(inputElement);
 formControlElement.appendChild(selectElement);
 formElement.addElement(formControlElement);
 }

 @Test
 public void testFormElementCreation() {
 assertNotNull(formElement);
 assertEquals("form", formElement.tagName());
 }

 @Test
 public void testElementsMethod() {
 Elements elements = formElement.elements();
 assertEquals(2, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElementCopy = new FormElement(new Tag("form"), "", new Attributes());
 Element newElement = new Element("div", "", new Attributes().put("id", "newElementId"));
 formElementCopy.addElement(newElement);
 Elements elements = formElementCopy.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testSubmitMethod() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithNoActionAttribute() {
 FormElement formElementNoAction = new FormElement(new Tag("form"), "", new Attributes());
 formElementNoAction.submit();
 }

 @Test
 public void testFormDataMethod() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());

 assertEquals("inputName", formData.get(0).key);
 assertEquals("inputValue", formData.get(0).val);

 assertEquals("selectName", formData.get(1).key);
 assertEquals("optionValue1", formData.get(1).val);
 }

 @Test
 public void testFormDataMethodWithDisabledInput() {
 inputElement.attributes().put("disabled", "");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertNull(formData.get(0).val);
 }

 @Test
 public void testFormDataMethodWithUnselectedSelect() {
 selectElement.select("option[selected]").remove();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodWithUncheckedCheckboxOrRadio() {
 inputElement.attributes().put("type", "checkbox");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());

 inputElement.attributes().put("type", "radio");
 formControlElement.appendText("Radio Group");
 Element radioGroup = formControlElement.appendElement("input");
 radioGroup.attributes().put("name", "radioName");
 radioGroup.attributes().put("type", "radio");
 radioGroup.attributes().put("value", "radioValue");
 formControlElement.appendChild(radioGroup);
 formElement.addElement(formControlElement);

 List<Connection.KeyVal> formDataCopy = formElement.formData();
 assertEquals(0, formDataCopy.size());

 radioGroup.attributes().put("checked", "");
 List<Connection.KeyVal> formDataChecked = formElement.formData();
 assertEquals(1, formDataChecked.size());
 assertEquals("radioName", formDataChecked.get(0).key);
 assertEquals("radioValue", formDataChecked.get(0).val);
 }

 @Test
 public void testFormDataMethodWithSelectedSelect() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());

 selectElement.select("option[selected]").first().attributes().put("selected", "");
 selectElement.select("option[value=optionValue2]").first().attributes().put("selected", "");
 List<Connection.KeyVal> formDataWithSelectedOption = formElement.formData();
 assertEquals(1, formDataWithSelectedOption.size());
 assertEquals("selectName", formDataWithSelectedOption.get(0).key);
 assertEquals("optionValue2", formDataWithSelectedOption.get(0).val);
 }

 @Test
 public void testRemoveChildMethod() {
 FormElement formElementCopy = new FormElement(new Tag("form"), "", new Attributes());
 formElementCopy.addElement(inputElement);
 formElementCopy.addElement(selectElement);
 formElementCopy.removeChild(inputElement);
 Elements elements = formElementCopy.elements();
 assertEquals(1, elements.size());
 }
}