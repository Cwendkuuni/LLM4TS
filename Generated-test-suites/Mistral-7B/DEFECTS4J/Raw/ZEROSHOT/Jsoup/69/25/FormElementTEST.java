import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Element formElement;
 private Element inputElement;
 private Element selectElement;
 private Element formWithInputAndSelect;

 @Before
 public void setUp() {
 Parser parser = Jsoup.parser();

 Tag formTag = parser.parse("<form action=\"http://example.com\"/>").tag("");
 Attributes formAttributes = formTag.attributes();
 formAttributes.put("method", "get");
 formElement = new Element("form", formAttributes, null);

 inputElement = parser.parse("<input type=\"text\" name=\"input\" value=\"value\"/>").tag();
 formElement.add(inputElement);

 selectElement = parser.parse("<select name=\"select\">" +
         "<option value=\"option1\">Option 1</option>" +
         "<option selected value=\"option2\">Option 2</option>" +
         "</select>").tag();
 formElement.add(selectElement);

 formWithInputAndSelect = parser.parse("<form action=\"http://example.com\"" +
         " method=\"post\">" +
         "<input type=\"text\" name=\"input\" value=\"value\"/>" +
         "<select name=\"select\">" +
         "<option value=\"option1\">Option 1</option>" +
         "<option selected value=\"option2\">Option 2</option>" +
         "</select>" +
         "</form>").tag();
 }

 @Test
 public void testFormElementConstruction() {
 assertNotNull(formElement);
 assertEquals("form", formElement.tagName());
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertEquals(2, elements.size());
 assertEquals(inputElement, elements.get(0));
 assertEquals(selectElement, elements.get(1));
 }

 @Test
 public void testAddElement() {
 FormElement formElementWithInput = (FormElement) formElement.clone();
 formElementWithInput.add(inputElement);
 Equals equals = new Equals();
 assertEquals(formElement, equals.equals(formElementWithInput));
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 assertEquals("GET", connection.method().name());
 assertEquals("http://example.com", connection.url().toString());
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());

 Connection.KeyVal inputKeyVal = formData.get(0);
 assertEquals("input", inputKeyVal.key());
 assertEquals("value", inputKeyVal.value());

 Connection.KeyVal selectKeyVal = formData.get(1);
 assertEquals("select", selectKeyVal.key());
 assertEquals("option2", selectKeyVal.value());
 }

 @Test
 public void testFormDataWithDisabledInput() {
 inputElement.attributes().put("disabled", "disabled");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("select", formData.get(0).key());
 assertEquals("option2", formData.get(0).value());
 }

 @Test
 public void testFormDataWithInputWithoutName() {
 inputElement = parser.parse("<input type=\"text\" value=\"value\"/>").tag();
 formElement.add(inputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataWithSelectWithoutSelectedOption() {
 selectElement = parser.parse("<select name=\"select\"><option value=\"option1\">Option 1</option></select>").tag();
 formElement.add(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataWithFormWithoutBaseUri() {
 formWithInputAndSelect.attributes().remove("action");
 List<Connection.KeyVal> formData = formWithInputAndSelect.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 assertEquals("", formData.get(0).key());
 assertEquals("", formData.get(0).value());
 }
}