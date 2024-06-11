import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

 private static final String BASE_URI = "http://example.com";

 @Test
 public void testFormElementConstructor() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Assert.assertEquals("form", formElement.tagName());
 Assert.assertEquals(BASE_URI, formElement.baseUri());
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Elements elements = formElement.elements();
 Assert.assertTrue(elements.isEmpty());

 Element inputElement = new Element("input", new Attributes().put("type", "text"));
 formElement.add(inputElement);
 elements = formElement.elements();
 Assert.assertEquals(1, elements.size());
 Assert.assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text"));
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();

 Assert.assertEquals(1, elements.size());
 Assert.assertEquals(inputElement, elements.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitInvalidBaseUri() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormDataEmptyForm() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertTrue(formData.isEmpty());
 }

 @Test
 public void testFormDataSingleInputElement() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "inputName").put("value", "inputValue"));
 formElement.add(inputElement);
 List<Connection.KeyVal> formData = formElement.formData();

 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("inputName", keyVal.key());
 Assert.assertEquals("inputValue", keyVal.value());
 }

 @Test
 public void testFormDataMultipleInputElements() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element inputElement1 = new Element("input", new Attributes().put("type", "text").put("name", "inputName1").put("value", "inputValue1"));
 formElement.add(inputElement1);
 Element inputElement2 = new Element("input", new Attributes().put("type", "text").put("name", "inputName2").put("value", "inputValue2"));
 formElement.add(inputElement2);
 List<Connection.KeyVal> formData = formElement.formData();

 Assert.assertEquals(2, formData.size());
 Connection.KeyVal keyVal1 = formData.get(0);
 Assert.assertEquals("inputName1", keyVal1.key());
 Assert.assertEquals("inputValue1", keyVal1.value());
 Connection.KeyVal keyVal2 = formData.get(1);
 Assert.assertEquals("inputName2", keyVal2.key());
 Assert.assertEquals("inputValue2", keyVal2.value());
 }

 @Test
 public void testFormDataSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element selectElement = new Element("select", new Attributes().put("name", "selectName"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "optionValue1").put("selected", "selected"));
 Element optionElement2 = new Element("option", new Attributes().put("value", "optionValue2"));
 selectElement.add(optionElement1);
 selectElement.add(optionElement2);
 formElement.add(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();

 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("selectName", keyVal.key());
 Assert.assertEquals("optionValue1", keyVal.value());
 }

 @Test
 public void testFormDataCheckboxOrRadioElement() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element checkboxOrRadioElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxOrRadioName").put("value", "checkboxOrRadioValue"));
 formElement.add(checkboxOrRadioElement);
 List<Connection.KeyVal> formData = formElement.formData();

 Assert.assertTrue(formData.isEmpty());

 checkboxOrRadioElement.attr("checked", "checked");
 formData = formElement.formData();

 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("checkboxOrRadioName", keyVal.key());
 Assert.assertEquals("checkboxOrRadioValue", keyVal.value());
 }

 @Test
 public void testFormDataFormWithEmptyAndNullValues() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element inputElement1 = new Element("input", new Attributes().put("type", "text").put("name", "inputName1"));
 formElement.add(inputElement1);
 Element inputElement2 = new Element("input", new Attributes().put("type", "text").put("name", "inputName2"));
 inputElement2.attr("value", "");
 formElement.add(inputElement2);
 Element inputElement3 = new Element("input", new Attributes().put("type", "text").put("name", "inputName3"));
 formElement.add(inputElement3);

 List<Connection.KeyVal> formData = formElement.formData();

 Assert.assertEquals(3, formData.size());
 Connection.KeyVal keyVal1 = formData.get(0);
 Assert.assertEquals("inputName1", keyVal1.key());
 Assert.assertNull(keyVal1.value());
 Connection.KeyVal keyVal2 = formData.get(1);
 Assert.assertEquals("inputName2", keyVal2.key());
 Assert.assertNull(keyVal2.value());
 Connection.KeyVal keyVal3 = formData.get(2);
 Assert.assertEquals("inputName3", keyVal3.key());
 Assert.assertNull(keyVal3.value());
 }
}