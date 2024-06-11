import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private FormElement formElement;
 private Element inputElement;
 private Element selectElement;
 private Element textareaElement;

 @Before
 public void setup() {
 Parser parser = new Parser.MalformedEncodingDetector();
 String html = "<form><input type='text' name='input'/>" +
 "<select name='select'><option value='option1'>Option 1</option>" +
 "<option value='option2' selected>Option 2</option></select>" +
 "<textarea name='textarea'>Hello World</textarea></form>";
 formElement = (FormElement) parser.parse(html);

 inputElement = new Element("input", new Attributes().put("type", "text").put("name", "input"));
 formElement.addElement(inputElement);

 selectElement = new Element("select", new Attributes().put("name", "select"));
 formElement.addElement(selectElement);

 textareaElement = new Element("textarea", new Attributes().put("name", "textarea"));
 formElement.addElement(textareaElement);
 }

 @Test
 public void testElements() {
 List<Element> elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElementCopy = new FormElement(new Tag("form"), "", new Attributes());
 formElementCopy.addElement(inputElement);
 List<Element> elements = formElementCopy.elements();
 assertNotNull(elements);
 assertEquals(1, elements.size());
 assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testSubmit_GetFormData_SimpleForm() {
 // Simple form with text input and select element
 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("input", ""),
 new Connection.KeyVal("select", "option2")
 );
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testSubmit_GetFormData_Textarea() {
 // Form with textarea
 textareaElement.text("Test Textarea");
 formElement = (FormElement) formElement.cloneNode(true); // Clone form element to test getFormData on
 list = formElement.formData();
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("textarea", "Test Textarea")
 );
 assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmit_IllegalArgumentException_NoAction() {
 formElement.removeAttr("action");
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmit_IllegalArgumentException_InvalidMethod() {
 formElement.attr("method", "invalidMethod");
 formElement.submit();
 }
}