import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.elements.Select;
import org.jsoup.nodes.elements.SubmitInput;
import org.jsoup.nodes.elements.TextInput;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html lang=\"en\"><head><meta charset=\"UTF-8\">" +
 " <title>Test Form</title></head><body>" +
 "<form action=\"https://test.com/submit\">" +
 "<input type=\"text\" name=\"username\" />" +
 "<input type=\"password\" name=\"password\" />" +
 "<select name=\"select\">" +
 "<option value=\"option1\">Option 1</option>" +
 "<option value=\"option2\" selected=\"selected\">Option 2</option>" +
 "</select>" +
 "<input type=\"submit\" value=\"Submit\" />" +
 "</form></body></html>";

 Parser parser = Jsoup.parser();
 formElement = parser.parseBodyFragment(html).getElementsByTag("form").first();
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 Element input = new TextInput("text", "Test");
 formElement.addElement(input);
 Elements elements = formElement.elements();
 assertEquals(4, elements.size());
 }

 @Test
 public void testSubmitWithValidFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 Connection connection = formElement.submit();
 assertEquals(3, formData.size());
 assertEquals("Test", formData.get(0).getKey());
 assertEquals("Test", formData.get(0).getValue());
 assertEquals("Test", connection.requestBody().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyFormAction() {
 FormElement emptyFormElement = new FormElement(new Tag("form"), "", new Attributes());
 try {
 emptyFormElement.submit();
 } catch (IllegalArgumentException iae) {
 assertEquals("Could not determine a form action URL for submit.", iae.getMessage());
 }
 }

 @Test
 public void testSubmitWithInvalidFormData() {
 formElement.removeChild(formElement.elements().get(2));
 List<Connection.KeyVal> formData = formElement.formData();
 Connection connection = formElement.submit();
 // This test assumes that Jsoup does not allow empty form submissions
 assertNull(connection);
 }
}