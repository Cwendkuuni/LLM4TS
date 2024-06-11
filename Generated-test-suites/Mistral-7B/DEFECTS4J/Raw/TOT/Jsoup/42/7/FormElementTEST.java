import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setup() {
 String html = "<html><head></head><body>" +
 " <form action=\"http://example.com\" method=\"get\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"text\" name=\"password\" />" +
 " <select name=\"country\">" +
 " <option value=\"USA\">United States</option>" +
 " <option value=\"CA\">Canada</option>" +
 " </select>" +
 " <input type=\"submit\" value=\"Submit\" />" +
 " </form>" +
 "</body></html>";
 formElement = Jsoup.parse(html).body().select("form").first();
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 assertNotNull(elements.get(0).attr("name"));
 assertEquals("input", elements.get(0).tagName());
 assertEquals("username", elements.get(0).attr("name"));
 }

 @Test
 public void testAddElement() {
 Element input = new Element("input");
 input.attr("type", "hidden");
 input.attr("name", "token");
 formElement.addElement(input);
 Elements elementsAfterAdd = formElement.elements();
 assertEquals(4, elementsAfterAdd.size());
 assertEquals("hidden", elementsAfterAdd.get(3).tagName());
 assertEquals("token", elementsAfterAdd.get(3).attr("name"));
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertEquals("GET", connection.method().name());
 assertEquals("http://example.com", connection.url().toString());
 List<Connection.KeyVal> formData = connection.data();
 assertEquals(4, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("", formData.get(0).val()); // input fields are empty by default
 assertEquals("CA", formData.get(1).val()); // Canada is selected in the dropdown
 assertEquals("password", formData.get(2).key());
 assertEquals("", formData.get(2).val()); // input fields are empty by default
 assertEquals("USA", formData.get(3).key()); // United States is the default option
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithMissingAction() {
 FormElement formElementWithMissingAction = new FormElement(new Tag("form"), null, new Attributes());
 try {
 formElementWithMissingAction.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 }
 }
}