package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 private FormElement formElement;
 private Element mockElement;
 private Connection mockConnection;
 private Attributes mockAttributes;
 private Tag mockTag;
 private Elements mockElements;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockTag = mock(Tag.class);
 mockAttributes = mock(Attributes.class);
 mockElement = mock(Element.class);
 mockConnection = mock(Connection.class);
 mockElements = mock(Elements.class);
 formElement = new FormElement(mockTag, "baseUri", mockAttributes);
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 expectedElements.add(mockElement);
 when(formElement.elements()).thenReturn(expectedElements);
 assertSame(expectedElements, formElement.elements());
 }

 @Test
 public void testAddElement() {
 Element elementToAdd = mock(Element.class);
 formElement.addElement(elementToAdd);
 verify(mockElements).add(elementToAdd);
 }

 @Test
 public void testRemoveChild() {
 Node nodeToRemove = mock(Node.class);
 formElement.removeChild(nodeToRemove);
 verify(mockSuper).removeChild(nodeToRemove);
 verify(mockElements).remove(nodeToRemove);
 }

 @Test
 public void testSubmit() throws Exception {
 String actionUrl = "http://example.com";
 when(formElement.absUrl("action")).thenReturn(actionUrl);
 String baseUri = "baseUri";
 when(formElement.baseUri()).thenReturn(baseUri);
when(mockConnection.data(anyList())).thenReturn(mockConnection);
 Connection connection = formElement.submit();
 assertEquals(actionUrl, connection.url());
 assertEquals(Connection.Method.GET, connection.method());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseUri() {
 when(formElement.hasAttr("action")).thenReturn(true);
 when(formElement.absUrl("action")).thenThrow(new IllegalArgumentException());
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyFormData() {
 when(formElement.elements()).thenReturn(mockElements);
 when(mockElements.size()).thenReturn(0);
 try {
 formElement.submit();
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormData() {
 Element selectElement = mock(Element.class);
 Element optionElement = mock(Element.class);
 Element checkboxElement = mock(Element.class);
 formElement.elements().add(selectElement);
 formElement.elements().add(checkboxElement);

 when(selectElement.tagName()).thenReturn("select");
 when(selectElement.select("option[selected]")).thenReturn(new Elements(optionElement));
 when(optionElement.val()).thenReturn("value1");
 when(selectElement.attr("name")).thenReturn("nameSelect");

 when(checkboxElement.tagName()).thenReturn("input");
 when(checkboxElement.attr("type")).thenReturn("checkbox");
 when(checkboxElement.attr("name")).thenReturn("nameCheckbox");
 when(checkboxElement.hasAttr("checked")).thenReturn(true);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 assertEquals("nameSelect", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 assertEquals("nameCheckbox", formData.get(1).key);
 assertEquals("on", formData.get(1).value);
 assertEquals("nameCheckbox", formData.get(2).key);
 assertEquals("on", formData.get(2).value);
 }
}