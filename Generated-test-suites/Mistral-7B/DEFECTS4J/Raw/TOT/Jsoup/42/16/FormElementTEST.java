import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private Document document;

 @Before
 public void setup() {
 String html = "<html><body><form action=\"/\"><input name=\"input1\" type=\"text\"><input name=\"input2\" type=\"hidden\"><select name=\"input3\"><option value=\"option1\">Option 1</option><option selected value=\"option2\">Option 2</option></select><input name=\"input4\" type=\"checkbox\"><input name=\"input5\" type=\"checkbox\"><input name=\"input6\" type=\"submit\"></form></body></html>";
 document = Jsoup.parse(html);
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = document.select("form").first();
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = document.select("form").first();
 List<Element> elements = formElement.elements().select("input, select");
 assertEquals(5, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = document.select("form").first();
 Element newElement = new Parser().parseFromString("<input name=\"newElement\" type=\"text\"/>", "").first();
 formElement.addElement(newElement);
 List<Element> elements = formElement.elements().select("input");
 assertEquals(6, elements.size());
 assertEquals(newElement, elements.get(5));
 }

 @Test
 public void testSubmitWithValidFormData() {
 FormElement formElement = document.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 Connection connection = formElement.submit();
 assertEquals(Connection.Method.GET, connection.method()); // the test assumes that the form uses GET method by default
 assertEquals(Arrays.asList(new Connection.KeyVal("input1", ""), new Connection.KeyVal("input2", ""),
 new Connection.KeyVal("input3", "option2"), new Connection.KeyVal("input4", ""), new Connection.KeyVal("input5", ""),
 new Connection.KeyVal("input6", "")), formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseURI() {
 FormElement formElement = document.select("form").first();
 formElement.submit();
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = document.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(Arrays.asList(new Connection.KeyVal("input1", ""), new Connection.KeyVal("input2", ""),
 new Connection.KeyVal("input3", "option2"), new Connection.KeyVal("input4", ""), new Connection.KeyVal("input5", ""),
 new Connection.KeyVal("input6", "")), formData);
 }

 @Test
 public void testFormDataWithCheckboxElement() {
 FormElement formElement = document.select("form").first();
 Element checkboxElement1 = formElement.select("input[name=input4]").first();
 checkboxElement1.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(Arrays.asList(new Connection.KeyVal("input1", ""), new Connection.KeyVal("input2", ""),
 new Connection.KeyVal("input3", "option2"), new Connection.KeyVal("input4", "true"),
 new Connection.KeyVal("input5", ""), new Connection.KeyVal("input6", "")), formData);

 FormElement formElement2 = document.parse("<html><body><form action=\"/\"><input name=\"input1\" type=\"text\"><input name=\"input2\" type=\"hidden\"><input name=\"input4\" type=\"checkbox\"><input name=\"input5\" type=\"checkbox\"><input name=\"input6\" type=\"submit\"></form></body></html>").select("form").first();
 List<Connection.KeyVal> formData2 = formElement2.formData();
 assertEquals(Arrays.asList(new Connection.KeyVal("input1", ""), new Connection.KeyVal("input2", ""),
 new Connection.KeyVal("input3", ""), new Connection.KeyVal("input4", ""), new Connection.KeyVal("input5", ""),
 new Connection.KeyVal("input6", "")), formData2);
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = document.select("form").first();
 FormElement formElement2 = document.select("form").first();
 assertEquals(formElement1, formElement2);
 }

 @Test
 public void testSubmitWithInvalidFormData() {
 FormElement formElement = document.select("form").first();
 formElement.removeAttr("action");
 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testFormDataWithEmptyForm() {
 FormElement formElement = document.parse("<html><body><form action=\"/\"></form></body></html>").select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertNull(formData);
 }
}