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

import static org.junit.Assert.*;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 " <form action=\"/submit\" method=\"post\">" +
 " <label for=\"name\">Name:</label>" +
 " <input type=\"text\" name=\"name\" id=\"name\">" +
 " <label for=\"age\">Age:</label>" +
 " <input type=\"number\" name=\"age\" id=\"age\">" +
 " <label for=\"gender\">Gender:</label>" +
 " <select name=\"gender\">" +
 " <option value=\"male\" selected>Male</option>" +
 " <option value=\"female\">Female</option>" +
 " </select>" +
 " <label for=\"hobbies\">Hobbies:</label>" +
 " <input type=\"checkbox\" name=\"hobbies[]\" value=\"football\">Football" +
 " <input type=\"checkbox\" name=\"hobbies[]\" value=\"basketball\">Basketball" +
 " <input type=\"hidden\" name=\"csrfToken\" value=\"abc123\">" +
 " <button type=\"submit\">Submit</button>" +
 " </form>" +
 "</body></html>";

 Document doc = Jsoup.parse(html);
 formElement = doc.selectFirst("form");
 }

 @Test
 public void testElements() {
 // Check if elements() returns an instance of Elements
 assertTrue(formElement.elements() instanceof Elements);

 // Check if formElement contains any elements
 assertTrue(formElement.elements().size() > 0);
 }

 @Test
 public void testAddElement() {
 // Create a new text input element
 Element newInput = new Element("input");
 newInput.attr("type", "text");
 newInput.attr("name", "newElement");
 newInput.attr("id", "newElement");

 // Add the new input to the form and check if it's present
 formElement.addElement(newInput);
 assertEquals(formElement.elements().size(), 11);

 // Perform a submission and check if the new input is sent
 List<Connection.KeyVal> formData = formElement.formData();
 assertTrue(formData.contains(new Connection.KeyVal("newElement", "")));
 }

 @Test
 public void testSubmit() {
 // Set up a connection and check if it correctly submits the form data
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();

 // Check if all form data is included in the request
 List<Connection.KeyVal> expectedFormData = formElement.formData();
 assertEquals(expectedFormData.size(), formData.size());
 for (Connection.KeyVal keyVal : expectedFormData) {
 assertTrue(formData.contains(keyVal));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 // Test the submit method when there's no 'action' attribute
 FormElement noActionForm = new FormElement(new Tag("form"), "", new Attributes());
 try {
 noActionForm.submit();
 fail("Expected an IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoMethod() {
 // Test the submit method when there's no 'method' attribute
 FormElement noMethodForm = new FormElement(new Tag("form"), "", new Attributes().put("action", "/submit"));
 try {
 noMethodForm.submit();
 fail("Expected an IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }
}