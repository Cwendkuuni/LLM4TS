package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 private static final String BASE_URI = "http://test.com";
 private static final String FORM_TAG = "form";
 private static final String INPUT_TAG = "input";
 private static final String SELECT_TAG = "select";
 private static final String OPTION_TAG = "option";

 @Test
 public void testFormElementCreation() {
 FormElement form = new FormElement(new Tag(FORM_TAG), BASE_URI, new Attributes());
 assertNotNull(form);
 }

 @Test
 public void testGetFormControls() {
 FormElement form = createFormWithElements();
 Elements formControls = form.elements();
 assertNotNull(formControls);
 assertTrue(formControls.size() > 0);
 }

 @Test
 public void testAddElement() {
 FormElement form = createFormWithElements();
 Element input = createInputElement();
 form.addElement(input);
 Elements formControls = form.elements();
 assertEquals(2, formControls.size());
 assertEquals(input, formControls.get(1));
 }

 @Test
 public void testRemoveChild() {
 FormElement form = createFormWithElements();
 Element input = createInputElement();
 form.addElement(input);
 form.removeChild(input);
 Elements formControls = form.elements();
 assertEquals(0, formControls.size());
 }

 @Test
 public void testSubmit() {
 FormElement form = createFormWithElements();
 Connection connection = form.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNullAction() {
 FormElement form = new FormElement(new Tag(FORM_TAG), null, new Attributes());
 try {
 form.submit();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormDataEmptyForm() {
 FormElement form = new FormElement(new Tag(FORM_TAG), BASE_URI, new Attributes());
 List<Connection.KeyVal> formData = form.formData();
 assertTrue(formData.isEmpty());
 }

 @Test
 public void testFormDataWithInput() {
 FormElement form = createFormWithInput();
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(1, formData.size());
 assertEquals("inputName", formData.get(0).key);
 assertEquals("inputValue", formData.get(0).value);
 }

 @Test
 public void testFormDataWithSelect() {
 FormElement form = createFormWithSelect();
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(1, formData.size());
 assertEquals("selectName", formData.get(0).key);
 assertEquals("selectedOptionValue", formData.get(0).value);
 }

 @Test
 public void testFormDataWithCheckbox() {
 FormElement form = createFormWithCheckbox();
 List<Connection.KeyVal> formData = form.formData();
 assertTrue(formData.isEmpty());
 }

 @Test
 public void testFormDataWithCheckedCheckbox() {
 FormElement form = createFormWithCheckedCheckbox();
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(1, formData.size());
 assertEquals("checkboxName", formData.get(0).key);
 assertEquals("checkboxValue", formData.get(0).value);
 }

 @Test
 public void testFormDataWithMultipleInputs() {
 FormElement form = createFormWithMultipleInputs();
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(3, formData.size());
 // order is undefined
 assertFalse(formData.get(0).key.equals("input1Name"));
 assertFalse(formData.get(0).key.equals("input2Name"));
 assertFalse(formData.get(0).key.equals("input3Name"));
 // values match expected
 assertEquals("input1Value", formData.get(1).value);
 assertEquals("input2Value", formData.get(3).value);
 assertEquals("input3Value", formData.get(2).value);
 }

 private FormElement createFormWithElements() {
 FormElement form = new FormElement(new Tag(FORM_TAG), BASE_URI, new Attributes());
 Element input = createInputElement();
 Element select = createSelectElement();
 form.addElement(input);
 form.addElement(select);
 return form;
 }

 private Element createInputElement() {
 Element input = new Element(new Tag(INPUT_TAG), BASE_URI, new Attributes().put("name", "inputName")
 .put("value", "inputValue").put("type", "text"));
 return input;
 }

 private Element createSelectElement() {
 Element select = new Element(new Tag(SELECT_TAG), BASE_URI, new Attributes().put("name", "selectName"));
 Element option = new Element(new Tag(OPTION_TAG), BASE_URI, new Attributes().put("value", "selectedOptionValue"));
 select.addElement(option);
 return select;
 }

 private Element createCheckboxElement() {
 Element checkbox = new Element(new Tag(INPUT_TAG), BASE_URI, new Attributes()
 .put("name", "checkboxName").put("type", "checkbox").put("value", "checkboxValue"));
 return checkbox;
 }

 private FormElement createFormWithCheckbox() {
 FormElement form = new FormElement(new Tag(FORM_TAG), BASE_URI, new Attributes());
 Element checkbox = createCheckboxElement();
 form.addElement(checkbox);
 return form;
 }

 private FormElement createFormWithCheckedCheckbox() {
 FormElement form = new FormElement(new Tag(FORM_TAG), BASE_URI, new Attributes());
 Element checkbox = createCheckboxElement();
 checkbox.attributes().put("checked", "");
 form.addElement(checkbox);
 return form;
 }

 private FormElement createFormWithMultipleInputs() {
 FormElement form = new FormElement(new Tag(FORM_TAG), BASE_URI, new Attributes());
 Element input1 = createInputElement();
 input1.attributes().put("name", "input1Name");
 Element input2 = createInputElement();
 input2.attributes().put("name", "input2Name");
 Element input3 = createInputElement();
 input3.attributes().put("name", "input3Name");
 form.addElement(input1);
 form.addElement(input2);
 form.addElement(input3);
 return form;
 }
}