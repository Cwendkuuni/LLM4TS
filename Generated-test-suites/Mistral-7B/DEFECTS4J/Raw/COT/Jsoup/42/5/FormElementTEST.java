import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FormElementTest {

 private FormElement formElement;
 private Element inputElement;
 private Element selectElement;
 private Element formWithNoAction;

 @Before
 public void setUp() {
 // Create FormElement instance with input and select elements
 Tag inputTag = new Tag("input");
 inputTag.put("name", "nameInput");
 inputTag.put("type", "text");
 inputElement = new Element(inputTag);

 Tag selectTag = new Tag("select");
 selectTag.put("name", "nameSelect");
 selectElement = new Element(selectTag);
 selectElement.appendElement(new Element("option").text("Option 1"));
 selectElement.appendElement(new Element("option").text("Option 2").attr("selected", "true"));

 formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.addElement(inputElement);
 formElement.addElement(selectElement);

 // Create FormElement instance with no action attribute
 Tag formTagNoAction = new Tag("form");
 formWithNoAction = new FormElement(formTagNoAction, "", new Attributes());
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements(inputElement, selectElement);
 assertEquals(expectedElements, formElement.elements());
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("input").attr("name", "newElement");
 formElement.addElement(newElement);
 Elements expectedElements = new Elements(inputElement, selectElement, newElement);
 assertEquals(expectedElements, formElement.elements());
 }

 @Test
 public void testSubmitWithAction() throws Exception {
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("nameInput", "inputValue"));
 expectedFormData.add(HttpConnection.KeyVal.create("nameSelect", "Option 1"));

 Connection connection = formElement.submit();
 List<Connection.KeyVal> actualFormData = connection.data();
 assertEquals(expectedFormData, actualFormData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 formWithNoAction.submit();
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test
 public void testFormDataForInputElement() {
 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal inputElementFormData = null;
 for (Connection.KeyVal data : formData) {
 if (data.key.equals("nameInput")) {
 inputElementFormData = data;
 break;
 }
 }
 assertEquals("inputValue", inputElementFormData.value);
 }

 @Test
 public void testFormDataForSelectElement() {
 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal selectElementFormData = null;
 for (Connection.KeyVal data : formData) {
 if (data.key.equals("nameSelect")) {
 selectElementFormData = data;
 break;
 }
 }
 assertEquals("Option 1", selectElementFormData.value);
 }

 @Test
 public void testFormDataForCheckboxOrRadio() {
 Element checkboxElement = new Element("input").attr("name", "nameCheckbox").attr("type", "checkbox").attr("checked", "true");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal checkboxFormData = null;
 for (Connection.KeyVal data : formData) {
 if (data.key.equals("nameCheckbox")) {
 checkboxFormData = data;
 break;
 }
 }
 assertEquals(true, checkboxFormData.value.equals("on"));
 }
}