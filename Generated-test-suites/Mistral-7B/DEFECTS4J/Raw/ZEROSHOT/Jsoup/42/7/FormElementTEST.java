import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParsingMode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private static final String HTML_FILE_CONTENT = "" +
         "<html lang=\"en\">" +
         "<head></head>" +
         "<body>" +
         "<form action=\"test.html\">" +
         "<input type=\"text\" name=\"name\" value=\"John\">" +
         "<input type=\"text\" name=\"age\" value=\"30\">" +
         "<select name=\"country\">" +
         "<option value=\"USA\">USA</option>" +
         "<option value=\"Canada\" selected>Canada</option>" +
         "</select>" +
         "<input type=\"checkbox\" name=\"agree\" value=\"yes\" checked>" +
         "<input type=\"submit\" value=\"Submit\">" +
         "</form>" +
         "</body>" +
         "</html>";

 private Element formElement;

 @Before
 public void setUp() throws Exception {
 Document doc = Jsoup.parse(HTML_FILE_CONTENT, ParsingMode.HTML_PARSE);
 formElement = doc.selectFirst("form");
 }

 @Test
 public void testCreateFormElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element input = new Elements(new Tag("input")).first();
 formElement.addElement(input);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(input, elements.first());
 }

 @Test
 public void testSubmit() throws Exception {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormData() throws Exception {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());

 // check form data for text inputs
 assertEquals("name", formData.get(0).key);
 assertEquals("John", formData.get(0).value);

 assertEquals("age", formData.get(1).key);
 assertEquals("30", formData.get(1).value);

 // check form data for select input
 assertEquals("country", formData.get(2).key);
 assertEquals("Canada", formData.get(2).value);

 // check form data for checkbox input
 assertEquals("agree", formData.get(3).key);
 assertEquals("yes", formData.get(3).value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyActionUrl() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", ""));
 formElement.submit();
 }
}