package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 @Mock
 private Element tag;
 @Mock
 private Attributes attributes;
 @Mock
 private Connection connection;

 private FormElement formElement;

 @Before
 public void setUp() {
 formElement = new FormElement(tag, "", attributes);
 }

 @Test
 public void testFormElementCreation() {
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 }

 @Test
 public void testAddElement() {
 Element element = mock(Element.class);
 formElement.addElement(element);
 assertEquals(element, formElement.elements().get(0));
 }

 @Test
 public void testSubmit() {
 when(tag.tagName()).thenReturn("form");
 String baseUri = "http://test.com";
 when(tag.baseUri()).thenReturn(baseUri);
 when(tag.absUrl("action")).thenReturn("http://test.com/submit");

 Connection connectionResult = formElement.submit();
 assertEquals(Connection.Method.GET, connectionResult.method());
 assertEquals(baseUri, connectionResult.url());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 when(tag.tagName()).thenReturn("form");
 when(tag.baseUri()).thenReturn("http://test.com");
 when(tag.hasAttr("action")).thenReturn(false);
 formElement.submit();
 }

 @Test
 public void testFormData() {
 Element input1 = mock(Element.class);
 Element input2 = mock(Element.class);
 Element selectElement = mock(Element.class);
 Elements options = mock(Elements.class);

 when(input1.tagName()).thenReturn("input");
 when(input1.attr("name")).thenReturn("input1");
 when(input1.attr("type")).thenReturn("text");
 when(input1.val()).thenReturn("value1");
 when(input1.hasAttr("disabled")).thenReturn(false);

 when(input2.tagName()).thenReturn("input");
 when(input2.attr("name")).thenReturn("input2");
 when(input2.attr("type")).thenReturn("checkbox");
 when(input2.val()).thenReturn("value2");
 when(input2.hasAttr("checked")).thenReturn(true);

 when(selectElement.tagName()).thenReturn("select");
 when(selectElement.attr("name")).thenReturn("selectElement");
 when(selectElement.select("option[selected]")).thenReturn(options);
 when(options.size()).thenReturn(2);
 when(options.get(0).val()).thenReturn("option1Value");
 when(options.get(1).val()).thenReturn("option2Value");

 when(tag.select("input")).thenReturn(Arrays.asList(input1, input2));
 when(tag.select("select")).thenReturn(selectElement);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());
 assertEquals("input1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 assertEquals("input2", formData.get(1).key);
 assertEquals("value2", formData.get(1).value);
 assertEquals("selectElement", formData.get(2).key);
 assertEquals("option1Value", formData.get(2).value);
 assertEquals("option2Value", formData.get(3).value);
 }
}