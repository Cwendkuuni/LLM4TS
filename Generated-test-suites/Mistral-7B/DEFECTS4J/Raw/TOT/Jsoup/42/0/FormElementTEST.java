import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.helper.HttpConnection;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setUp() {
 String html = "" +
 " <html>\n" +
 " <head>\n" +
 " </head>\n" +
 " <body>\n" +
 " <form action=\"http://example.com/\">\n" +
 " <label for=\"input1\">Input 1:</label>\n" +
 " <input id=\"input1\" name=\"input1\" type=\"text\"/>\n" +
 " <label for=\"checkbox\">Checkbox:</label>\n" +
 " <input id=\"checkbox\" name=\"checkbox\" type=\"checkbox\" checked/>\n" +
 " <label for=\"select\">Select:</label>\n" +
 " <select id=\"select\" name=\"select\">\n" +
 " <option value=\"option1\">Option 1</option>\n" +
 " <option value=\"option2\" selected>Option 2</option>\n" +
 " </select>\n" +
 " <button type=\"submit\">Submit</button>\n" +
 " </form>\n" +
 " </body>\n" +
 " </html>\n";

 Document document = Jsoup.parse(html);
 formElement = document.select("form").first();
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 assertEquals("form", formElement.tagName());
 }

 @Test
 public void testFormControlsAreEmptyInitially() {
 assertEquals(0, formElement.elements().size());
 }

 @Test
 public void testAddingFormControlElement() {
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "inputName"));
 formElement.addElement(inputElement);
 assertEquals(1, formElement.elements().size());
 }

 @Test
 public void testFormSubmission() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 List<Connection.KeyVal> formData = formElement.formData();
 // Check if the expected form data is present
 Connection.KeyVal input1 = new Connection.KeyVal("input1", ""); // empty input
 Connection.KeyVal checkbox = new Connection.KeyVal("checkbox", "on"); // checked checkbox
 Connection.KeyVal selectOption2 = new Connection.KeyVal("select", "option2"); // selected option
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(input1);
 expectedFormData.add(checkbox);
 expectedFormData.add(selectOption2);
 assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormSubmissionWithMissingActionAttribute() {
 FormElement formElementWithoutAction = new FormElement(Tag.FORM, "", new Attributes());
 formElementWithoutAction.addElement(new Element("input", new Attributes().put("type", "text").put("name", "inputName")));
 try {
 formElementWithoutAction.submit();
 fail("Expected IllegalArgumentException with missing action attribute.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testFormSubmissionWithInvalidMethod() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes().put("method", "InvalidMethod"));
 formElement.addElement(new Element("input", new Attributes().put("type", "text").put("name", "inputName")));
 try {
 formElement.submit();
 fail("Expected IllegalArgumentException with invalid method.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }
}