import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setup() {
 Tag tag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 formElement = new FormElement(tag, baseUri, attributes);
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 expectedElements.add(new Element("input", new Attributes()));
 expectedElements.add(new Element("textarea", new Attributes()));
 expectedElements.add(new Element("select", new Attributes()));
 expectedElements.add(new Element("button", new Attributes()));

 Elements actualElements = formElement.elements();
 assertEquals("Size of elements should be equal", expectedElements.size(), actualElements.size());
 for (int i = 0; i < expectedElements.size(); i++) {
 Element expectedElement = expectedElements.get(i);
 Element actualElement = actualElements.get(i);
 assertEquals("Element type should be equal", expectedElement.tagName(), actualElement.tagName());
 }
 }

 @Test
 public void testAddElement() {
 Element inputElement = new Element("input", new Attributes());
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals("Size of elements should be equal", 1, elements.size());
 Element actualInputElement = elements.get(0);
 assertEquals("Input element should be equal", inputElement, actualInputElement);
 }

 @Test
 public void testSubmitWithValidActionUrl() throws Exception {
 Connection connection = formElement.submit();
 String actionUrl = connection.url();
 String expectedActionUrl = "http://example.com";
 assertEquals("Action URL should be equal", expectedActionUrl, actionUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyActionUrl() {
 formElement.setAttr("action", "");
 try {
 formElement.submit();
 fail("IllegalArgumentException expected but not thrown.");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testFormDataEmptyForm() {
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Size of form data should be equal", expectedFormData.size(), actualFormData.size());
 }

 @Test
 public void testFormDataWithOneInputElement() {
 Element inputElement = new Element("input", new Attributes().put("name", "inputName").put("value", "inputValue"));
 formElement.addElement(inputElement);
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("inputName", "inputValue"));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Size of form data should be equal", expectedFormData.size(), actualFormData.size());
 for (int i = 0; i < expectedFormData.size(); i++) {
 Connection.KeyVal expectedEntry = expectedFormData.get(i);
 Connection.KeyVal actualEntry = actualFormData.get(i);
 assertEquals("Form data entries should be equal", expectedEntry, actualEntry);
 }
 }

 @Test
 public void testFormDataWithOneSelectElement() {
 Element selectElement = new Element("select", new Attributes().put("name", "selectName"));
 Element optionElement = new Element("option", new Attributes().put("value", "optionValue"));
 selectElement.addElement(optionElement);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("selectName", "optionValue"));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Size of form data should be equal", expectedFormData.size(), actualFormData.size());
 for (int i = 0; i < expectedFormData.size(); i++) {
 Connection.KeyVal expectedEntry = expectedFormData.get(i);
 Connection.KeyVal actualEntry = actualFormData.get(i);
 assertEquals("Form data entries should be equal", expectedEntry, actualEntry);
 }
 }

 @Test
 public void testFormDataWithOneCheckboxOrRadioElement() {
 Element checkboxOrRadioElement = new Element("input", new Attributes().put("name", "checkboxOrRadioName").put("type", "checkbox"));
 formElement.addElement(checkboxOrRadioElement);
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("checkboxOrRadioName", ""));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Size of form data should be equal", expectedFormData.size(), actualFormData.size());
 for (int i = 0; i < expectedFormData.size(); i++) {
 Connection.KeyVal expectedEntry = expectedFormData.get(i);
 Connection.KeyVal actualEntry = actualFormData.get(i);
 assertEquals("Form data entries should be equal", expectedEntry, actualEntry);
 }
 checkboxOrRadioElement.setAttr("checked", "checked");
 List<Connection.KeyVal> expectedFormDataWithChecked = new ArrayList<>();
 expectedFormDataWithChecked.add(HttpConnection.KeyVal.create("checkboxOrRadioName", ""));
 expectedFormDataWithChecked.add(HttpConnection.KeyVal.create("checkboxOrRadioName", ""));
 List<Connection.KeyVal> actualFormDataWithChecked = formElement.formData();
 assertEquals("Size of form data should be equal", expectedFormDataWithChecked.size(), actualFormDataWithChecked.size());
 for (int i = 0; i < expectedFormDataWithChecked.size(); i++) {
 Connection.KeyVal expectedEntry = expectedFormDataWithChecked.get(i);
 Connection.KeyVal actualEntry = actualFormDataWithChecked.get(i);
 assertEquals("Form data entries should be equal", expectedEntry, actualEntry);
 }
 }

 @Test
 public void testFormDataWithMultipleInputElements() {
 Element inputElement1 = new Element("input", new Attributes().put("name", "inputName1").put("value", "inputValue1"));
 formElement.addElement(inputElement1);
 Element inputElement2 = new Element("input", new Attributes().put("name", "inputName2").put("value", "inputValue2"));
 formElement.addElement(inputElement2);
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("inputName1", "inputValue1"));
 expectedFormData.add(HttpConnection.KeyVal.create("inputName2", "inputValue2"));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Size of form data should be equal", expectedFormData.size(), actualFormData.size());
 for (int i = 0; i < expectedFormData.size(); i++) {
 Connection.KeyVal expectedEntry = expectedFormData.get(i);
 Connection.KeyVal actualEntry = actualFormData.get(i);
 assertEquals("Form data entries should be equal", expectedEntry, actualEntry);
 }
 }

 @Test
 public void testFormDataWithMultipleSelectElements() {
 Element selectElement1 = new Element("select", new Attributes().put("name", "selectName1"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "optionValue1"));
 selectElement1.addElement(optionElement1);
 Element selectElement2 = new Element("select", new Attributes().put("name", "selectName2"));
 Element optionElement2 = new Element("option", new Attributes().put("value", "optionValue2"));
 selectElement2.addElement(optionElement2);
 formElement.addElement(selectElement1);
 formElement.addElement(selectElement2);
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("selectName1", "optionValue1"));
 expectedFormData.add(HttpConnection.KeyVal.create("selectName2", "optionValue2"));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Size of form data should be equal", expectedFormData.size(), actualFormData.size());
 for (int i = 0; i < expectedFormData.size(); i++) {
 Connection.KeyVal expectedEntry = expectedFormData.get(i);
 Connection.KeyVal actualEntry = actualFormData.get(i);
 assertEquals("Form data entries should be equal", expectedEntry, actualEntry);
 }
 }

 @Test
 public void testFormDataWithMultipleCheckboxOrRadioElements() {
 Element checkboxOrRadioElement1 = new Element("input", new Attributes().put("name", "checkboxOrRadioName1").put("type", "checkbox"));
 formElement.addElement(checkboxOrRadioElement1);
 Element checkboxOrRadioElement2 = new Element("input", new Attributes().put("name", "checkboxOrRadioName2").put("type", "checkbox"));
 formElement.addElement(checkboxOrRadioElement2);
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("checkboxOrRadioName1", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("checkboxOrRadioName2", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("checkboxOrRadioName1", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("checkboxOrRadioName2", ""));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Size of form data should be equal", expectedFormData.size(), actualFormData.size());
 for (int i = 0; i < expectedFormData.size(); i++) {
 Connection.KeyVal expectedEntry = expectedFormData.get(i);
 Connection.KeyVal actualEntry = actualFormData.get(i);
 assertEquals("Form data entries should be equal", expectedEntry, actualEntry);
 }
 checkboxOrRadioElement1.setAttr("checked", "checked");
 List<Connection.KeyVal> expectedFormDataWithChecked1 = new ArrayList<>();
 expectedFormDataWithChecked1.add(HttpConnection.KeyVal.create("checkboxOrRadioName1", ""));
 expectedFormDataWithChecked1.add(HttpConnection.KeyVal.create("checkboxOrRadioName1", ""));
 expectedFormDataWithChecked1.add(HttpConnection.KeyVal.create("checkboxOrRadioName2", ""));
 expectedFormDataWithChecked1.add(HttpConnection.KeyVal.create("checkboxOrRadioName2", ""));
 List<Connection.KeyVal> actualFormDataWithChecked = formElement.formData();
 assertEquals("Size of form data should be equal", expectedFormDataWithChecked.size(), actualFormDataWithChecked.size());
 for (int i = 0; i < expectedFormDataWithChecked.size(); i++) {
 Connection.KeyVal expectedEntry = expectedFormDataWithChecked.get(i);
 Connection.KeyVal actualEntry = actualFormDataWithChecked.get(i);
 assertEquals("Form data entries should be equal", expectedEntry, actualEntry);
 }
 }
}