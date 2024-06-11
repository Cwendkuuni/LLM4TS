import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", new Attributes());
 assertEquals("FORM", formElement.tagName());
 assertNotNull(formElement.elements());
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", new Attributes());
 List<Element> elements = new ArrayList<>();
 elements.add(Mockito.mock(Element.class));
 formElement.elements = elements;

 assertEquals(elements, formElement.elements());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", new Attributes());
 Element element = Mockito.mock(Element.class);
 formElement.addElement(element);
 assertEquals(element, formElement.elements().get(0));
 }

 @Test
 public void testSubmitWithValidUrl() throws Exception {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "https://test.com", new Attributes());
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyActionUrl() throws Exception {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", new Attributes());
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNullActionUrl() throws Exception {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), null, new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormDataWhenFormHasNoControls() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertNull(formData);
 }

 @Test
 public void testFormDataWhenFormHasSelectControl() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", new Attributes());
 Element selectElement = Mockito.mock(Element.class);
 Element optionElement = Mockito.mock(Element.class);
 Mockito.when(selectElement.attr("name")).thenReturn("selectName");
 Mockito.when(selectElement.tagName()).thenReturn("select");
 Mockito.when(selectElement.select("option[selected]")).thenReturn(new ArrayList<Element>() {{
 add(optionElement);
 }});
 Mockito.when(optionElement.val()).thenReturn("optionValue");
 Elements elements = new Elements();
 elements.add(selectElement);
 formElement.elements = elements;

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("selectName", formData.get(0).key);
 assertEquals("optionValue", formData.get(0).value);
 }

 @Test
 public void testFormDataWhenFormHasCheckboxControl() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", new Attributes());
 Element checkboxElement = Mockito.mock(Element.class);
 Mockito.when(checkboxElement.attr("name")).thenReturn("checkboxName");
 Mockito.when(checkboxElement.attr("type")).thenReturn("checkbox");
 Elements elements = new Elements();
 elements.add(checkboxElement);
 formElement.elements = elements;

 Mockito.when(checkboxElement.hasAttr("checked")).thenReturn(true);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("checkboxName", formData.get(0).key);
 assertEquals("", formData.get(0).value);
 }

 @Test
 public void testFormDataWhenFormHasMultipeCheckboxControls() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", new Attributes());
 Element checkboxElement1 = Mockito.mock(Element.class);
 Element checkboxElement2 = Mockito.mock(Element.class);
 Mockito.when(checkboxElement1.attr("name")).thenReturn("checkboxName");
 Mockito.when(checkboxElement1.attr("type")).thenReturn("checkbox");
 Mockito.when(checkboxElement2.attr("name")).thenReturn("checkboxName");
 Mockito.when(checkboxElement2.attr("type")).thenReturn("checkbox");
 Mockito.when(checkboxElement1.hasAttr("checked")).thenReturn(true);
 Mockito.when(checkboxElement2.hasAttr("checked")).thenReturn(false);
 Elements elements = new Elements();
 elements.add(checkboxElement1);
 elements.add(checkboxElement2);
 formElement.elements = elements;

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("checkboxName", formData.get(0).key);
 assertEquals("", formData.get(0).value);
 assertEquals("checkboxName", formData.get(1).key);
 assertEquals("", formData.get(1).value);
 }

 @Test
 public void testFormDataWhenFormHasTextInputControl() {
 FormElement formElement = new FormElement(Mockito.mock(Tag.class), "", new Attributes());
 Element textInputElement = Mockito.mock(Element.class);
 Mockito.when(textInputElement.attr("name")).thenReturn("textInputName");
 Mockito.when(textInputElement.attr("type")).thenReturn("text");
 Elements elements = new Elements();
 elements.add(textInputElement);
 formElement.elements = elements;

 Mockito.when(textInputElement.val()).thenReturn("textInputValue");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("textInputName", formData.get(0).key);
 assertEquals("textInputValue", formData.get(0).value);
 }
}