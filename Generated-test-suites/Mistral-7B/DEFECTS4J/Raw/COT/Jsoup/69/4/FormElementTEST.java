import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 private FormElement formElement;
 private Element element;
 private List<Connection.KeyVal> keyValues;
 private Connection connection;

 @Before
 public void setup() {
 formElement = new FormElement(mock(Tag.class), "", new Attributes());
 element = mock(Element.class);
 keyValues = new ArrayList<>();
 connection = mock(Connection.class);
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 expectedElements.add(element);
 assertSame(expectedElements, formElement.elements());
 }

 @Test
 public void testAddElement() {
 formElement.addElement(element);
 assertSame(formElement, formElement.addElement(element));
 assertEquals(1, formElement.elements().size());
 }

 @Test
 public void testSubmit() {
 String action = "http://test.com";
 when(formElement.baseUri()).thenReturn(action);
 when(formElement.formData()).thenReturn(keyValues);
 when(connection.requestBody()).thenReturn(keyValues.toString().getBytes());
 Connection actualConnection = formElement.submit();
 assertEquals(connection, actualConnection);
 }

 @Test
 public void testFormData() {
 Element inputElement = mock(Element.class);
 when(inputElement.attr("name")).thenReturn("inputName");
 when(inputElement.attr("type")).thenReturn("text");
 when(inputElement.val()).thenReturn("inputValue");
 formElement.addElement(inputElement);
 List<Connection.KeyVal> expectedKeyValues = new ArrayList<>();
 expectedKeyValues.add(new Connection.KeyVal("inputName", "inputValue"));
 assertEquals(expectedKeyValues, formElement.formData());
 }

 @Test
 public void testFormDataForSelectElement() {
 Element selectElement = mock(Element.class);
 Element optionElement = mock(Element.class);
 when(selectElement.tagName()).thenReturn("select");
 when(selectElement.select("option[selected]")).thenReturn(new Elements(optionElement));
 when(optionElement.val()).thenReturn("optionValue");
 when(selectElement.attr("name")).thenReturn("selectName");
 formElement.addElement(selectElement);
 List<Connection.KeyVal> expectedKeyValues = new ArrayList<>();
 expectedKeyValues.add(new Connection.KeyVal("selectName", "optionValue"));
 assertEquals(expectedKeyValues, formElement.formData());
 }

 @Test
 public void testFormDataForCheckboxOrRadioElement() {
 Element checkboxElement = mock(Element.class);
 when(checkboxElement.attr("name")).thenReturn("checkboxName");
 when(checkboxElement.attr("type")).thenReturn("checkbox");
 when(checkboxElement.hasAttr("checked")).thenReturn(true);
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> expectedKeyValues = new ArrayList<>();
 expectedKeyValues.add(new Connection.KeyVal("checkboxName", "on"));
 assertEquals(expectedKeyValues, formElement.formData());
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitWithNoActionURL() {
 when(formElement.hasAttr("action")).thenReturn(false);
 formElement.submit();
 fail("Expected IllegalStateException to be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyActionURL() {
 when(formElement.hasAttr("action")).thenReturn(true);
 when(formElement.absUrl("action")).thenReturn("");
 formElement.submit();
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testSubmitWithInvalidMethod() {
 when(formElement.hasAttr("method")).thenReturn("invalidMethod");
 formElement.submit();
 fail("Expected IllegalArgumentException to be thrown");
 }
}