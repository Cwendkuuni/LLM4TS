import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetFormControls() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element control1 = new Element("input", new Attributes().put("type", "text").put("name", "control1"));
 Element control2 = new Element("input", new Attributes().put("type", "text").put("name", "control2"));
 formElement.addElement(control1);
 formElement.addElement(control2);

 List<Element> formControls = formElement.elements();
 assertEquals(2, formControls.size());
 assertEquals(control1, formControls.get(0));
 assertEquals(control2, formControls.get(1));
 }

 @Test
 public void testSubmitForm() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "http://example.com"));
 Element control1 = new Element("input", new Attributes().put("type", "text").put("name", "control1").put("value", "value1"));
 Element control2 = new Element("input", new Attributes().put("type", "text").put("name", "control2").put("value", "value2"));
 formElement.addElement(control1);
 formElement.addElement(control2);

 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithoutAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormData() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element control1 = new Element("input", new Attributes().put("type", "text").put("name", "control1").put("value", "value1"));
 Element control2 = new Element("input", new Attributes().put("type", "text").put("name", "control2").put("value", "value2"));
 formElement.addElement(control1);
 formElement.addElement(control2);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("control1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 assertEquals("control2", formData.get(1).key);
 assertEquals("value2", formData.get(1).value);
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element selectElement = new Element("select", new Attributes().put("name", "control"));
 Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", "selected"));
 Element option2 = new Element("option", new Attributes().put("value", "option2"));
 selectElement.addElement(option1);
 selectElement.addElement(option2);
 formElement.addElement(selectElement);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("control", formData.get(0).key);
 assertEquals("option1", formData.get(0).value);
 }

 @Test
 public void testFormDataWithCheckboxOrRadioElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "control").put("checked", "checked"));
 formElement.addElement(checkboxElement);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("control", formData.get(0).key);
 assertEquals("on", formData.get(0).value);
 }

 @Test
 public void testFormDataWithEmptyNameAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element control = new Element("input", new Attributes());
 formElement.addElement(control);

 List<Connection.KeyVal> formData = formElement.formData();
 assertNull(formData);
 }
}