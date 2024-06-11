import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html lang=\"en\"><body>" +
 " <form action=\"/submit\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" value=\"John\"/>" +
 " <input type=\"text\" name=\"password\" value=\"password123\"/>" +
 " <input type=\"submit\" value=\"Submit\"/>" +
 " </form>" +
 "</body></html>";
 formElement = Jsoup.parse(html).getElementsByTag("form").first().getElementsByTag("form").first();
 }

 @Test
 public void testFormElementCreation() {
 // Test FormElement creation
 FormElement expectedFormElement = new FormElement(new Tag("form"), "/", new Attributes());
 assertEquals(expectedFormElement.tagName(), formElement.tagName());
 assertEquals(expectedFormElement.attributes().toString(), formElement.attributes().toString());
 }

 @Test
 public void testGetElements() {
 // Test getting form controls
 Elements expectedElements = new Elements();
 expectedElements.add(new Element("input", new Attributes().put("type", "text").put("name", "username").put("value", "John")))
 .add(new Element("input", new Attributes().put("type", "text").put("name", "password").put("value", "password123")));
 assertEquals(expectedElements.toString(), formElement.elements().toString());
 }

 @Test
 public void testAddElement() {
 // Test adding a form control
 Element newElement = new Element("input", new Attributes().put("type", "hidden").put("name", "csrfToken").put("value", "tokenValue"));
 FormElement expectedFormElementWithNewElement = new FormElement(formElement.tag(), formElement.baseUri(), formElement.attributes())
 .addElement(newElement);
 Element newAddedElement = expectedFormElementWithNewElement.elements().get(formElement.elements().size() + 1);
 assertEquals(newAddedElement.tagName(), "input");
 assertEquals(newAddedElement.attributes().get("type"), "hidden");
 assertEquals(newAddedElement.attributes().get("name"), "csrfToken");
 assertEquals(newAddedElement.attributes().get("value"), "tokenValue");
 }

 @Test
 public void testRemoveChild() {
 // Test removing a form control
 FormElement expectedFormElementWithoutFirstElement = new FormElement(formElement.tag(), formElement.baseUri(), formElement.attributes())
 .removeChild(formElement.elements().get(0));
 Element removedElement = expectedFormElementWithoutFirstElement.elements().get(0);
 assertNull(removedElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 // Test submit without an action attribute
 FormElement formElementWithoutAction = new FormElement(new Tag("form"), "/", new Attributes());
 assertThrows(IllegalArgumentException.class, formElementWithoutAction::submit);
 }

 @Test
 public void testSubmit() {
 // Test form submission
 Connection connection = formElement.submit();
 assertEquals("POST", connection.method().name());
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(new Connection.KeyVal("username", "John"));
 expectedFormData.add(new Connection.KeyVal("password", "password123"));
 assertEquals(expectedFormData, connection.data());
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitWithDisabledFormControl() {
 // Test form submission with a disabled form control
 formElement.getElements().get(0).attributes().put("disabled", "disabled");
 assertThrows(IllegalStateException.class, formElement::submit);
 }

 @Test
 public void testFormDataEmptyForm() {
 // Test form data for empty form
 List<Connection.KeyVal> expectedFormDataEmptyForm = new ArrayList<>();
 assertEquals(expectedFormDataEmptyForm, formElement.formData());
 }

 @Test
 public void testFormDataContainsSelectElementWithSingleSelectedOption() {
 // Test form data for a form with a select element and a single selected option
 Element selectElement = formElement.getElements().get(0).getElementsByTag("select").first();
 selectElement.select("option[selected]").first().attributes().put("selected", "");
 Element optionElement = selectElement.getElementsByTag("option").first();
 assertEquals("username", optionElement.attributes().get("name"));
 assertEquals("John", optionElement.val());
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(new Connection.KeyVal("username", "John"));
 assertEquals(expectedFormData, formElement.formData());
 }

 @Test
 public void testFormDataContainsSelectElementWithMultipleSelectedOptions() {
 // Test form data for a form with a select element and multiple selected options
 Element selectElement = formElement.getElements().get(0).getElementsByTag("select").first();
 List<Element> options = selectElement.select("option[selected]");
 options.get(0).attributes().put("selected", "");
 options.get(1).attributes().put("selected", "");
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(new Connection.KeyVal("username", "John,Jane"));
 assertEquals(expectedFormData, formElement.formData());
 }

 @Test
 public void testFormDataContainsCheckboxOrRadioElement() {
 // Test form data for a form with a checkbox or radio element
 Element checkboxOrRadioElement = formElement.getElements().get(0).getElementsByTag("input").get(1);
 checkboxOrRadioElement.attributes().put("checked", "checked");
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(new Connection.KeyVal("username", "on"));
 assertEquals(expectedFormData, formElement.formData());
 }
}