package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 FormElement formElement = Mockito.mock(FormElement.class);
 Elements elementsMock = Mockito.mock(Elements.class);
 Connection connectionMock = Mockito.mock(Connection.class);

 @Test
 public void testFormElementConstructor() {
 // Test the FormElement constructor
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 // Test the elements() method
 when(formElement.elements()).thenReturn(elementsMock);
 List<Element> elements = new ArrayList<>();
 elements.add(Mockito.mock(Element.class));
 elementsMock.addAll(elements);
 assertEquals(elements, formElement.elements().all());
 }

 @Test
 public void testAddElementMethod() {
 // Test the addElement() method
 when(elementsMock.size()).thenReturn(0);
 Element elementMock = Mockito.mock(Element.class);
 formElement.addElement(elementMock);
 verify(elementsMock).add(elementMock);
 }

 @Test
 public void testRemoveChildMethod() {
 // Test the removeChild() method
 Node nodeMock = Mockito.mock(Node.class);
 formElement.removeChild(nodeMock);
 verify(elementsMock).remove(nodeMock);
 }

 @Test
 public void testSubmitMethod() {
 // Test the submit() method
 String baseUri = "https://example.com";
 String action = "https://example.com/submit";
 when(formElement.hasAttr("action")).thenReturn(true);
 when(formElement.absUrl("action")).thenReturn(action);
 when(formElement.attr("method")).thenReturn("GET");
 when(Jsoup.connect(action).data(Mockito.anyList())).thenReturn(connectionMock);

 Connection connection = formElement.submit();
 assertEquals(connectionMock, connection);
 }

 @Test
 public void testFormDataMethodEmptyForm() {
 // Test the formData() method for an empty form
 when(formElement.elements()).thenReturn(elementsMock);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodSingleInput() {
 // Test the formData() method for a form with a single input
 Element inputMock = Mockito.mock(Element.class);
 inputMock.attr("name", "inputName");
 inputMock.attr("type", "text");
 inputMock.val("testInputValue");
 when(formElement.elements()).thenReturn(elementsMock);
 elementsMock.add(inputMock);

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(Connection.KeyVal.create("inputName", "testInputValue"));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataMethodMultipleInputs() {
 // Test the formData() method for a form with multiple inputs
 Element input1Mock = Mockito.mock(Element.class);
 input1Mock.attr("name", "inputName1");
 input1Mock.attr("type", "text");
 input1Mock.val("testInputValue1");
 Element input2Mock = Mockito.mock(Element.class);
 input2Mock.attr("name", "inputName2");
 input2Mock.attr("type", "text");
 input2Mock.val("testInputValue2");
 when(formElement.elements()).thenReturn(elementsMock);
 elementsMock.addAll(Arrays.asList(input1Mock, input2Mock));

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.addAll(Arrays.asList(
 Connection.KeyVal.create("inputName1", "testInputValue1"),
 Connection.KeyVal.create("inputName2", "testInputValue2")
 ));
 assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithNoBaseURI() {
 // Test the submit() method with no base URI
 formElement.submit();
 }

 @Test
 public void testFormDataMethodWithSelectTag() {
 // Test the formData() method with a form containing a select tag
 Element selectMock = Mockito.mock(Element.class);
 selectMock.tagName("select");
 Element option1Mock = Mockito.mock(Element.class);
 option1Mock.tagName("option");
 option1Mock.attr("value", "optionValue1");
 option1Mock.attr("selected", "selected");
 Element option2Mock = Mockito.mock(Element.class);
 option2Mock.tagName("option");
 option2Mock.attr("value", "optionValue2");
 when(formElement.elements()).thenReturn(elementsMock);
 elementsMock.addAll(Arrays.asList(selectMock, option1Mock, option2Mock));

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.addAll(Arrays.asList(
 Connection.KeyVal.create("name_of_select_tag", "optionValue1"),
 Connection.KeyVal.create("name_of_select_tag", "optionValue2")
 ));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataMethodWithCheckboxOrRadio() {
 // Test the formData() method with a form containing a checkbox or radio
 Element checkboxMock = Mockito.mock(Element.class);
 checkboxMock.tagName("input");
 checkboxMock.attr("type", "checkbox");
 checkboxMock.attr("name", "checkboxName");
 checkboxMock.attr("checked", "checked");
 when(formElement.elements()).thenReturn(elementsMock);
 elementsMock.add(checkboxMock);

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(Connection.KeyVal.create("checkboxName", "on"));
 assertEquals(expectedFormData, formData);
 }
}