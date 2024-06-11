package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunner.ClassloaderInjection(AnnotatedClassLoader.class)
public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element element1 = mock(Element.class);
 Element element2 = mock(Element.class);
 PowerMockito.when(element1.tagName()).thenReturn("input");
 PowerMockito.when(element2.tagName()).thenReturn("select");
 List<Element> elements = Arrays.asList(element1, element2);
 PowerMockito.when(formElement.elements()).thenReturn(new Elements(elements));

 List<Element> actualElements = formElement.elements().all();
 assertEquals(2, actualElements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element element = mock(Element.class);
 PowerMockito.when(element.tagName()).thenReturn("input");
 formElement.addElement(element);
 List<Element> actualElements = formElement.elements().all();
 assertEquals(1, actualElements.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddDuplicateElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element element = mock(Element.class);
 PowerMockito.when(element.tagName()).thenReturn("input");
 formElement.addElement(element);
 formElement.addElement(element);
 }

 @Test
 public void testSubmit() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.addElement(mock(Element.class));
 formElement.addElement(mock(Element.class));

 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseUri() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.addElement(mock(Element.class));
 formElement.addElement(mock(Element.class));
 PowerMockito.mockStatic(Jsoup.class);
 PowerMockito.when(Jsoup.connect(Mockito.anyString(), Mockito.any(Connection.Method.class)))
 .thenThrow(new IllegalArgumentException("Could not determine a form action URL for submit."));

 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormData() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element input1 = mock(Element.class);
 Element input2 = mock(Element.class);
 Element input3 = mock(Element.class);
 PowerMockito.when(input1.tagName()).thenReturn("input");
 PowerMockito.when(input1.attr("name")).thenReturn("name1");
 PowerMockito.when(input1.val()).thenReturn("value1");
 PowerMockito.when(input2.tagName()).thenReturn("select");
 PowerMockito.when(input2.attr("name")).thenReturn("name2");
 PowerMockito.when(input2.select("option[selected]")).thenReturn(new Elements(mock(Element.class)));
 PowerMockito.when(input3.tagName()).thenReturn("input");
 PowerMockito.when(input3.attr("name")).thenReturn("name3");
 PowerMockito.when(input3.attr("type")).thenReturn("checkbox");
 PowerMockito.when(input3.hasAttr("checked")).thenReturn(true);
 List<Element> elements = Arrays.asList(input1, input2, input3);
 PowerMockito.when(formElement.elements()).thenReturn(new Elements(elements));

 List<Connection.KeyVal> actualFormData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("name1", "value1"),
 new Connection.KeyVal("name2", ""),
 new Connection.KeyVal("name3", "")
 );
 assertEquals(expectedFormData, actualFormData);
 }
}