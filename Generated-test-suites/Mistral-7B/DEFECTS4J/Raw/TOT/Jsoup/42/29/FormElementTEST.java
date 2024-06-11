import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private Element document;
 private Element formElement;

 @Before
 public void setup() {
 ParseSetup.setDefaultParseTimeout(0);
 document = Jsoup.parse("<html><body><form name='testForm'><input name='test' type='text' value='testValue'><input type='submit'></form></body></html>");
 formElement = document.selectFirst("form");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethodReturnsEmptyListByDefault() {
 Elements elements = formElement.elements();
 assertEquals(0, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 Element input = document.createElement("input");
 input.setAttribute("name", "testInput");
 input.setAttribute("type", "text");
 formElement.addElement(input);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 }

 @Test
 public void testSubmitMethodThrowsIllegalArgumentExceptionWhenNoActionAttribute() {
 assertThrows(IllegalArgumentException.class, formElement::submit);
 }

 @Test
 public void testSubmitMethodReturnsConnectionObject() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataMethodReturnsEmptyListByDefault() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodReturnsListWithCorrectValues() {
 formElement.addElement(new Element("input") {{
 setAttribute("name", "test");
 setAttribute("type", "text");
 setAttribute("value", "testValue");
 }} );
 List<Connection.KeyVal> formData = formElement.formData();
 List<String> expected = Arrays.asList("test=testValue");
 assertEquals(expected, formData);
 }

 @Test
 public void testFormDataMethodHandlesMultipleElements() {
 formElement.addElement(new Element("input") {{
 setAttribute("name", "test1");
 setAttribute("type", "text");
 setAttribute("value", "testValue1");
 }} );
 formElement.addElement(new Element("input") {{
 setAttribute("name", "test2");
 setAttribute("type", "text");
 setAttribute("value", "testValue2");
 }} );
 List<Connection.KeyVal> formData = formElement.formData();
 List<String> expected = Arrays.asList("test1=testValue1", "test2=testValue2");
 assertEquals(expected, formData);
 }

 @Test
 public void testFormDataMethodHandlesSelectElement() {
 Element select = document.createElement("select");
 select.setAttribute("name", "testSelect");
 Element option1 = document.createElement("option");
 option1.setText("Option 1");
 select.appendChild(option1);
 Element option2 = document.createElement("option");
 option2.setText("Option 2");
 option2.setAttribute("selected", "selected");
 select.appendChild(option2);
 formElement.addElement(select);
 List<Connection.KeyVal> formData = formElement.formData();
 List<String> expected = Arrays.asList("testSelect=Option+2");
 assertEquals(expected, formData);
 }

 @Test
 public void testFormDataMethodHandlesCheckboxOrRadioElement() {
 Element checkbox = document.createElement("input");
 checkbox.setAttribute("name", "testCheckbox");
 checkbox.setAttribute("type", "checkbox");
 checkbox.setAttribute("checked", "checked");
 formElement.addElement(checkbox);
 List<Connection.KeyVal> formData = formElement.formData();
 List<String> expected = Arrays.asList("testCheckbox=on");
 assertEquals(expected, formData);
 }

 @Test
 public void testFormDataMethodHandlesMultipleCheckboxOrRadioElements() {
 Element checkbox1 = document.createElement("input");
 checkbox1.setAttribute("name", "testCheckbox1");
 checkbox1.setAttribute("type", "checkbox");
 checkbox1.setAttribute("checked", "checked");
 formElement.addElement(checkbox1);
 Element checkbox2 = document.createElement("input");
 checkbox2.setAttribute("name", "testCheckbox2");
 checkbox2.setAttribute("type", "checkbox");
 formElement.addElement(checkbox2);
 List<Connection.KeyVal> formData = formElement.formData();
 List<String> expected = Arrays.asList("testCheckbox1=on", "testCheckbox2=on");
 assertEquals(expected, formData);
 }

 @Test
 public void testFormDataMethodHandlesEmptyInputElement() {
 Element input = document.createElement("input");
 input.setAttribute("name", "testEmptyInput");
 formElement.addElement(input);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }
}