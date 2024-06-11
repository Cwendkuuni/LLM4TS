import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private FormElement formElement;
 private Element inputElement;
 private Element textareaElement;
 private Element selectElement;
 private Element disabledInputElement;
 private Element hiddenInputElement;

 @Before
 public void setup() {
 Parser parser = new Parser.MalformedEncodingDetector();
 String html = "<form><input name=\"inputName\" type=\"text\"/>" +
 " <input name=\"inputName\" type=\"text\" disabled/>" +
 " <input name=\"inputName\" type=\"text\" hidden/>" +
 " <input name=\"inputName\" type=\"text\" checked/>" +
 " <textarea name=\"textareaName\" rows=\"5\"></textarea>" +
 " <select name=\"selectName\"><option value=\"option1\">Option 1</option>" +
 " <option value=\"option2\" selected>Option 2</option>" +
 " </select></form>";
 formElement = (FormElement) parser.parse(html);

 inputElement = formElement.elements().first();
 textareaElement = (Element) formElement.elements().get(2);
 selectElement = (Element) formElement.elements().get(3);
 disabledInputElement = formElement.elements().get(1);
 hiddenInputElement = formElement.elements().get(4);
 }

 @Test
 public void testElements() {
 List<Element> elements = formElement.elements();
 assertEquals(5, elements.size());
 assertSame(formElement, elements.get(0).parent());
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("input", new Attributes().put("name", "newName"));
 formElement.addElement(newElement);
 assertEquals(6, formElement.elements().size());
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 assertEquals("POST", connection.method().name());
 }

 @Test
 public void testFormData_Input() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());

 Connection.KeyVal inputNameValue1 = formData.get(0);
 assertEquals("inputName", inputNameValue1.key());
 assertEquals("", inputNameValue1.val());

 Connection.KeyVal inputNameValue2 = formData.get(1);
 assertEquals("inputName", inputNameValue2.key());
 assertNull(inputNameValue2.val());

 Connection.KeyVal inputNameValue3 = formData.get(2);
 assertEquals("inputName", inputNameValue3.key());
 assertNotNull(inputNameValue3.val());
 assertEquals("", inputNameValue3.val());

 Connection.KeyVal inputNameValue4 = formData.get(3);
 assertEquals("inputName", inputNameValue4.key());
 assertNotNull(inputNameValue4.val());
 assertEquals("text", inputNameValue4.val());
 }

 @Test
 public void testFormData_Textarea() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());

 Connection.KeyVal textareaNameValue = formData.get(4);
 assertEquals("textareaName", textareaNameValue.key());
 assertNull(textareaNameValue.val());
 }

 @Test
 public void testFormData_Select() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());

 Connection.KeyVal selectNameValue1 = formData.get(5);
 assertEquals("selectName", selectNameValue1.key());
 assertEquals("option1", selectNameValue1.val());

 Connection.KeyVal selectNameValue2 = formData.get(6);
 assertEquals("selectName", selectNameValue2.key());
 assertEquals("option2", selectNameValue2.val());
 }

 @Test
 public void testFormData_CheckboxOrRadio() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());

 // checked input
 Connection.KeyVal inputNameValue3 = formData.get(2);
 assertEquals("inputName", inputNameValue3.key());
 assertEquals("on", inputNameValue3.val());

 // unchecked input
 Connection.KeyVal inputNameValueDisabled = formData.get(1);
 assertEquals("inputName", inputNameValueDisabled.key());
 assertNull(inputNameValueDisabled.val());
 }

 @Test
 public void testFormData_HiddenInput() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());

 Connection.KeyVal hiddenInputNameValue = formData.get(4);
 assertEquals("inputName", hiddenInputNameValue.key());
 assertNull(hiddenInputNameValue.val());
 }

 @Test
 public void testFormData_NoNameAttribute() {
 FormElement noNameFormElement = new FormElement(new Tag("form"), null, new Attributes());
 List<Connection.KeyVal> formData = noNameFormElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormData_InvalidType() {
 FormElement invalidTypeFormElement = new FormElement(new Tag("textarea"), null, new Attributes().put("name", "textareaName"));
 invalidTypeFormElement.setTagName("input");
 List<Connection.KeyVal> formData = invalidTypeFormElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormData_DisabledInput() {
 List<Connection.KeyVal> formData = disabledInputElement.formData();
 assertEquals(5, formData.size());
 assertFalse(formData.contains(new Connection.KeyVal("inputName", "")));
 }

 @Test
 public void testFormData_HiddenInput() {
 List<Connection.KeyVal> formData = hiddenInputElement.formData();
 assertEquals(5, formData.size());
 assertFalse(formData.contains(new Connection.KeyVal("inputName", "")));
 }
}