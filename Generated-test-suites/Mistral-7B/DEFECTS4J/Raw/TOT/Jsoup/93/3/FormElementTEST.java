import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class FormElementTest {

 @Test
 public void testCreateFormElement() {
 FormElement formElement = new FormElement(new Tag("form"), "http://localhost:8080", new Attributes());
 Assert.assertEquals("form", formElement.tagName());
 }

 @Test
 public void testGetFormControls() {
 FormElement formElement = createFormElementWithControls();
 Elements formControls = formElement.elements();
 Assert.assertFalse(formControls.isEmpty());
 }

 @Test
 public void testAddFormControl() {
 FormElement formElement = createFormElement();
 Element inputElement = new Element("input", new Attributes().put("name", "username").put("type", "text"));
 formElement.addElement(inputElement);
 Elements formControls = formElement.elements();
 Assert.assertEquals(1, formControls.size());
 Assert.assertEquals(inputElement, formControls.get(0));
 }

 @Test
 public void testRemoveFormControl() {
 FormElement formElement = createFormElementWithControls();
 Element inputElement = formElement.elements().get(0);
 formElement.removeChild(inputElement);
 Elements formControls = formElement.elements();
 Assert.assertEquals(0, formControls.size());
 }

 @Test
 public void testSubmitForm() {
 FormElement formElement = createFormElementWithControls();
 Connection connection = formElement.submit();
 Assert.assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithoutAction() {
 FormElement formElement = new FormElement(new Tag("form"), new Attributes(), new Attributes().put("name", "form"));
 Assert.assertThrows(IllegalArgumentException.class, formElement::submit);
 }

 @Test
 public void testFormDataForCheckbox() {
 FormElement formElement = createFormElementWithCheckbox();
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Assert.assertEquals("checkbox", formData.get(0).key());
 Assert.assertTrue(formData.get(0).value().equalsIgnoreCase("on"));
 }

 @Test
 public void testFormDataForTextInput() {
 FormElement formElement = createFormElementWithTextInput();
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Assert.assertEquals("textInput", formData.get(0).key());
 Assert.assertEquals("value", formData.get(0).value());
 }

 @Test
 public void testFormDataForSelectInput() {
 FormElement formElement = createFormElementWithSelectInput();
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Assert.assertEquals("select", formData.get(0).key());
 Assert.assertEquals("optionValue", formData.get(0).value());
 }

 @Test
 public void testFormDataForMultipleSelectInput() {
 FormElement formElement = createFormElementWithMultipleSelectInput();
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(2, formData.size());
 Assert.assertEquals("select", formData.get(0).key());
 Assert.assertEquals("optionValue1", formData.get(0).value());
 Assert.assertEquals("select", formData.get(1).key());
 Assert.assertEquals("optionValue2", formData.get(1).value());
 }

 private FormElement createFormElement() {
 return new FormElement(new Tag("form"), new Attributes(), new Attributes());
 }

 private FormElement createFormElementWithControls() {
 Element inputElement1 = new Element("input", new Attributes().put("name", "input1"));
 Element inputElement2 = new Element("input", new Attributes().put("name", "input2"));
 FormElement formElement = new FormElement(new Tag("form"), new Attributes(), new Attributes());
 formElement.addElement(inputElement1);
 formElement.addElement(inputElement2);
 return formElement;
 }

 private FormElement createFormElementWithCheckbox() {
 Element checkboxElement = new Element("input", new Attributes().put("name", "checkbox").put("type", "checkbox"));
 FormElement formElement = new FormElement(new Tag("form"), new Attributes(), new Attributes());
 formElement.addElement(checkboxElement);
 return formElement;
 }

 private FormElement createFormElementWithTextInput() {
 Element textInputElement = new Element("input", new Attributes().put("name", "textInput").put("type", "text").put("value", "value"));
 FormElement formElement = new FormElement(new Tag("form"), new Attributes(), new Attributes());
 formElement.addElement(textInputElement);
 return formElement;
 }

 private FormElement createFormElementWithSelectInput() {
 Element selectElement = new Element("select", new Attributes().put("name", "select"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "optionValue1"));
 Element optionElement2 = new Element("option", new Attributes().put("value", "optionValue2"));
 selectElement.addElement(optionElement1);
 selectElement.addElement(optionElement2);
 selectElement.setAttribute("selected", "selected");
 FormElement formElement = new FormElement(new Tag("form"), new Attributes(), new Attributes());
 formElement.addElement(selectElement);
 return formElement;
 }

 private FormElement createFormElementWithMultipleSelectInput() {
 Element selectElement = new Element("select", new Attributes().put("name", "select"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "optionValue1"));
 Element optionElement2 = new Element("option", new Attributes().put("value", "optionValue2"));
 selectElement.addElement(optionElement1);
 Element optionElement3 = new Element("option", new Attributes().put("value", "optionValue3"));
 selectElement.addElement(optionElement3);
 FormElement formElement = new FormElement(new Tag("form"), new Attributes(), new Attributes());
 formElement.addElement(selectElement);
 return formElement;
 }
}