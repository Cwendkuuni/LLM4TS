import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private String html;
 private Document document;

 @Before
 public void setUp() {
 html = "<html><head></head><body>" +
 "<form action=\"https://www.example.com\" method=\"post\">" +
 "<input type=\"text\" name=\"username\" value=\"test\">" +
 "<input type=\"password\" name=\"password\" value=\"test\">" +
 "<select name=\"mySelect\">" +
 "<option value=\"option1\">Option 1</option>" +
 "<option value=\"option2\" selected>Option 2</option>" +
 "</select>" +
 "<input type=\"checkbox\" name=\"agree\" value=\"yes\"> Agree" +
 "<input type=\"checkbox\" name=\"disagree\" value=\"no\"> Disagree" +
 "<input type=\"submit\" value=\"Submit\">" +
 "</form>" +
 "</body></html>";
 document = Jsoup.parse(html);
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = (FormElement) document.select("form").first();
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = (FormElement) document.select("form").first();
 assertEquals(5, formElement.elements().size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = (FormElement) document.select("form").first();
 Element inputElement = new Element("input", new Attributes().put("type", "hidden").put("name", "token").put("value", "12345"));
 formElement.addElement(inputElement);
 assertEquals(6, formElement.elements().size());
 }

 @Test
 public void testSubmitWithValidFormData() {
 FormElement formElement = (FormElement) document.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 Connection connection = formElement.submit();
 assertEquals(Connection.Method.POST, connection.method());
 assertEquals("https://www.example.com", connection.url());
 assertEquals(6, formData.size());
 for (int i = 0; i < formData.size(); i++) {
 Connection.KeyVal keyVal = formData.get(i);
 System.out.println("Key: " + keyVal.key());
 System.out.println("Value: " + keyVal.value());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitWithoutActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 try {
 formElement.submit();
 fail("Expected IllegalStateException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormDataWithSelectTag() {
 FormElement formElement = (FormElement) document.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(6, formData.size());
 assertEquals("option2", formData.get(3).key());
 assertEquals("Option 2", formData.get(3).value());
 }

 @Test
 public void testFormDataWithCheckboxTags() {
 FormElement formElement = (FormElement) document.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(8, formData.size());
 assertTrue(formData.get(6).key().contains("agree"));
 assertTrue(formData.get(7).key().contains("disagree"));
 }
}