import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setup() {
 String html = "<html lang=\"en\">" +
 " <head>" +
 " <meta charset=\"UTF-8\">" +
 " <title>Test Form</title>" +
 " </head>" +
 " <body>" +
 " <form action=\"https://test.com\" method=\"post\">" +
 " <label for=\"name\">Name:</label>" +
 " <input type=\"text\" id=\"name\" name=\"name\">" +
 " <label for=\"email\">Email:</label>" +
 " <input type=\"email\" id=\"email\" name=\"email\">" +
 " <label for=\"select\">Select:</label>" +
 " <select id=\"select\" name=\"select\">" +
 " <option value=\"option1\">Option 1</option>" +
 " <option value=\"option2\">Option 2</option>" +
 " <option selected value=\"option3\">Option 3</option>" +
 " </select>" +
 " <label for=\"checkbox\">Checkbox:</label>" +
 " <input type=\"checkbox\" id=\"checkbox\" name=\"checkbox\">" +
 " <label for=\"radio\">Radio:</label>" +
 " <input type=\"radio\" id=\"radio1\" name=\"radio\" value=\"value1\">" +
 " <input type=\"radio\" id=\"radio2\" name=\"radio\" value=\"value2\" checked>" +
 " <button type=\"submit\">Submit</button>" +
 " </form>" +
 " </body>" +
 " </html>";

 Parser parser = Jsoup.parser();
 Tag tag = parser.parseBodyFragment(html).selectFirst("form");
 formElement = new FormElement(tag, null, null);
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertEquals(6, elements.size());
 assertTrue(elements.contains(formElement.select("#name")));
 assertTrue(elements.contains(formElement.select("#email")));
 assertTrue(elements.contains(formElement.select("#select")));
 assertTrue(elements.contains(formElement.select("#checkbox")));
 assertTrue(elements.contains(formElement.select("#radio1")));
 assertTrue(elements.contains(formElement.select("#radio2")));
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("input", new Attributes().put("type", "hidden").put("name", "newElement"));
 formElement.addElement(newElement);
 Elements elements = formElement.elements();
 assertEquals(7, elements.size());
 assertTrue(elements.contains(newElement));
 }

 @Test
 public void testSubmit() throws Exception {
 Connection con = formElement.submit();
 List<Connection.KeyVal> formData = con.requestBody();
 assertEquals(6, formData.size());
 assertEquals("name", formData.get(0).key);
 assertEquals("", formData.get(0).val); // name input is empty by default
 assertEquals("email", formData.get(1).key);
 assertEquals("", formData.get(1).val); // email input is empty by default
 assertEquals("option3", formData.get(2).key); // selected option in select input
 assertEquals("", formData.get(2).val);
 assertEquals("option1", formData.get(3).key); // unselected option in select input
 assertEquals("", formData.get(3).val);
 assertEquals("checkbox", formData.get(4).key);
 assertEquals("false", formData.get(4).val); // checkbox not checked by default
 assertEquals("value2", formData.get(5).key); // checked radio button
 assertEquals("value1", formData.get(5).val); // unchecked radio button
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseUri() {
 formElement = new FormElement(Tag.valueOf("<form action=\"/submit\" method=\"post\">"), null, null);
 try {
 formElement.submit();
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}