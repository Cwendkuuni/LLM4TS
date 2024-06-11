package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
public class FormElementTest {

 private static final String BASE_URI = "http://example.com";
 private static final String TAG = "form";
 private static final String FORM_ACTION = "http://example.com/submit";
 private static final String FORM_METHOD = "POST";
 private static final String FORM_CONTROL_TAG = "input";
 private static final String FORM_CONTROL_NAME = "controlName";
 private static final String FORM_CONTROL_VALUE = "controlValue";
 private static final String FORM_CONTROL_DISABLED = "disabled";
 private static final String FORM_CONTROL_CHECKED = "checked";
 private static final String FORM_CONTROL_SELECTED = "selected";

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(PowerMockito.mock(Tag.class), BASE_URI, Mockito.mock(Attributes.class));
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(PowerMockito.mock(Tag.class), BASE_URI, Mockito.mock(Attributes.class));
 List<Element> elements = new ArrayList<>();
 elements.add(Mockito.mock(Element.class));
 PowerMockito.when(formElement.elements()).thenReturn(elements);
 List<Element> formControls = formElement.elements();
 assertNotNull(formControls);
 assertEquals(1, formControls.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(PowerMockito.mock(Tag.class), BASE_URI, Mockito.mock(Attributes.class));
 Element element = Mockito.mock(Element.class);
 formElement.addElement(element);
 List<Element> elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(element, elements.get(0));
 }

 @Test
 public void testRemoveChild() {
 FormElement formElement = new FormElement(PowerMockito.mock(Tag.class), BASE_URI, Mockito.mock(Attributes.class));
 Element element = Mockito.mock(Element.class);
 PowerMockito.when(formElement.elements()).thenReturn(Mockito.mock(Elements.class));
 PowerMockito.when(formElement.removeChild(element)).thenReturn(true);
 assertNull(formElement.elements().get(0));
 }

 @Test
 public void testSubmitWithValidFormData() {
 FormElement formElement = new FormElement(PowerMockito.mock(Tag.class), BASE_URI, Mockito.mock(Attributes.class));
 PowerMockito.when(formElement.elements()).thenReturn(Mockito.mock(Elements.class));

 PowerMockito.when(formElement.elements().get(0).tagName()).thenReturn(FORM_CONTROL_TAG);
 PowerMockito.when(formElement.elements().get(0).attr("name")).thenReturn(FORM_CONTROL_NAME);
 PowerMockito.when(formElement.elements().get(0).val()).thenReturn(FORM_CONTROL_VALUE);

 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create(FORM_CONTROL_NAME, FORM_CONTROL_VALUE));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testSubmitWithNoFormAction() {
 FormElement formElement = new FormElement(PowerMockito.mock(Tag.class), BASE_URI, Mockito.mock(Attributes.class));
 PowerMockito.when(formElement.elements()).thenReturn(Mockito.mock(Elements.class));
 PowerMockito.when(formElement.hasAttr("action")).thenReturn(false);
 try {
 formElement.submit();
 fail("IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitWithEmptyFormData() {
 FormElement formElement = new FormElement(PowerMockito.mock(Tag.class), BASE_URI, Mockito.mock(Attributes.class));
 PowerMockito.when(formElement.elements()).thenReturn(Mockito.mock(Elements.class));
 List<Element> elements = new ArrayList<>();
 elements.add(Mockito.mock(Element.class));
 PowerMockito.when(formElement.elements()).thenReturn(elements);
 PowerMockito.when(elements.get(0).tagName()).thenReturn(FORM_CONTROL_TAG);
 PowerMockito.when(elements.get(0).attr("name")).thenReturn(FORM_CONTROL_NAME);
 PowerMockito.when(elements.get(0).val()).thenReturn("");

 List<Connection.KeyVal> formData = formElement.submit().data();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = new FormElement(PowerMockito.mock(Tag.class), BASE_URI, Mockito.mock(Attributes.class));
 PowerMockito.when(formElement.elements()).thenReturn(Mockito.mock(Elements.class));

 PowerMockito.when(formElement.elements().get(0).tagName()).thenReturn(FORM_CONTROL_TAG);
 PowerMockito.when(formElement.elements().get(0).attr("name")).thenReturn(FORM_CONTROL_NAME);
 PowerMockito.when(formElement.elements().get(0).select("option[selected]")).thenReturn(Mockito.mock(Elements.class));
 PowerMockito.when(formElement.elements().get(0).select("option[selected]").get(0).val()).thenReturn("optionValue1");
 PowerMockito.when(formElement.elements().get(0).select("option")).thenReturn(Mockito.mock(Elements.class));
 PowerMockito.when(formElement.elements().get(0).select("option").get(0).val()).thenReturn("optionValue2");
 PowerMockito.when(formElement.elements().get(0).select("option[selected]").size()).thenReturn(1);

 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create(FORM_CONTROL_NAME, "optionValue1"));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataWithCheckboxOrRadioElement() {
 FormElement formElement = new FormElement(PowerMockito.mock(Tag.class), BASE_URI, Mockito.mock(Attributes.class));
 PowerMockito.when(formElement.elements()).thenReturn(Mockito.mock(Elements.class));

 PowerMockito.when(formElement.elements().get(0).tagName()).thenReturn(FORM_CONTROL_TAG);
 PowerMockito.when(formElement.elements().get(0).attr("name")).thenReturn(FORM_CONTROL_NAME);
 PowerMockito.when(formElement.elements().get(0).attr("type")).thenReturn("checkbox");
 PowerMockito.when(formElement.elements().get(0).hasAttr("checked")).thenReturn(true);

 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create(FORM_CONTROL_NAME, "on"));
 assertEquals(expectedFormData, formData);
 }
}