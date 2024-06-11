import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String BASE_URI = "http://example.com";

 @Test
 public void testFormElementConstructor() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 assertTrue(formElement instanceof FormElement);
 }

 @Test
 public void testElementsMethodReturnsEmptyListInitially() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 assertTrue(formElement.elements().isEmpty());
 }

 @Test
 public void testAddElementMethodAddsElementToFormControlList() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element inputElement = new Element("input");
 formElement.addElement(inputElement);
 List<Element> formControls = formElement.elements();
 assertTrue(formControls.size() == 1);
 assertTrue(formControls.contains(inputElement));
 }

 @Test
 public void testRemoveChildMethodRemovesNodeFromFormControlList() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element inputElement = new Element("input");
 formElement.addElement(inputElement);
 formElement.removeChild(inputElement);
 List<Element> formControls = formElement.elements();
 assertTrue(formControls.isEmpty());
 }

 @Test
 public void testSubmitMethodThrowsIllegalArgumentExceptionWhenNoActionUrl() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitMethodReturnsConnectionObject() {
 FormElement formElement = new FormElement(new Tag("form", "action", "/submit"), BASE_URI, new Attributes());
 Connection connection = formElement.submit();
 assertTrue(connection instanceof Connection);
 }

 @Test
 public void testFormDataMethodReturnsEmptyListInitially() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertTrue(formData.isEmpty());
 }

 @Test
 public void testFormDataMethodReturnsKeyValueListForFormControls() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element inputElement1 = new Element("input", "name1", null, "value1");
 Element inputElement2 = new Element("input", "name2", null, "value2");
 formElement.addElement(inputElement1);
 formElement.addElement(inputElement2);
 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("name1", "value1"));
 expectedFormData.add(HttpConnection.KeyVal.create("name2", "value2"));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataMethodHandlesSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element selectElement = new Element("select");
 Element optionElement1 = new Element("option", null, null, "Option 1");
 Element optionElement2 = new Element("option", null, null, "Option 2");
 selectElement.add(optionElement1);
 selectElement.add(optionElement2);
 selectElement.addAttr("name", "selectName");
 formElement.addElement(selectElement);
 optionElement1.setAttr("selected", "selected");
 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("selectName", "Option 1"));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataMethodHandlesCheckboxOrRadioElement() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element checkboxElement = new Element("input", null, null, null);
 checkboxElement.setTag("input");
 checkboxElement.setAttr("type", "checkbox");
 checkboxElement.setAttr("name", "checkboxName");
 Element radioElement1 = new Element("input", null, null, null);
 radioElement1.setTag("input");
 radioElement1.setAttr("type", "radio");
 radioElement1.setAttr("name", "radioName");
 radioElement1.setAttr("id", "radioId1");
 Element labelElement1 = new Element("label", null, null, "Label 1");
 labelElement1.addAttr("for", "radioId1");
 formElement.addElement(checkboxElement);
 formElement.addElement(radioElement1);
 formElement.addElement(labelElement1);
 checkboxElement.setAttr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("checkboxName", "on"));
 assertEquals(expectedFormData, formData);
 }
}