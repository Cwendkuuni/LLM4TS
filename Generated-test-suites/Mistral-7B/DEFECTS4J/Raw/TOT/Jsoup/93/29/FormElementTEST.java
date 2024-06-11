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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private FormElement form;

 @Before
 public void setup() {
 String html = "<html><body>" +
 " <form action=\"http://test.com/submit\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" value=\"John Doe\">" +
 " <input type=\"submit\" name=\"submit\">" +
 " </form>" +
 "</body></html>";

 Parser parser = Jsoup.parser();
 Tag tag = parser.parseBodyFragment(html).select("form").first();
 form = new FormElement(tag, "http://test.com", new Attributes());
 }

 @Test
 public void testElements() {
 Elements elements = form.elements();
 assertNotNull(elements);
 assertEquals(1, elements.size());
 }

 @Test
 public void testAddElement() {
 Element input = new Element("input");
 input.attr("type", "text");
 input.attr("name", "password");
 form.addElement(input);
 assertEquals(2, form.elements().size());
 }

 @Test
 public void testSubmit() {
 Connection connection = form.submit();
 assertEquals("POST", connection.method().name());
 assertEquals("http://test.com/submit", connection.url());
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = form.formData();
 assertNotNull(formData);
 assertEquals(1, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("John Doe", formData.get(0).value());
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitWithoutAction() {
 FormElement formWithoutAction = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 formWithoutAction.submit();
 }

 @Test
 public void testFormWithSelectElement() {
 String html = "<html><body>" +
 " <form action=\"http://test.com/submit\" method=\"post\">" +
 " <select name=\"country\">" +
 " <option value=\"USA\">United States</option>" +
 " <option value=\"Canada\" selected>Canada</option>" +
 " </select>" +
 " <input type=\"submit\" name=\"submit\">" +
 " </form>" +
 "</body></html>";

 Parser parser = Jsoup.parser();
 Tag tag = parser.parseBodyFragment(html).select("form").first();
 FormElement formWithSelect = new FormElement(tag, "http://test.com", new Attributes());
 List<Connection.KeyVal> formData = formWithSelect.formData();
 assertEquals(1, formData.size());
 assertEquals("country", formData.get(0).key());
 assertEquals("Canada", formData.get(0).value());
 }

 @Test
 public void testFormWithCheckboxElement() {
 String html = "<html><body>" +
 " <form action=\"http://test.com/submit\" method=\"post\">" +
 " <input type=\"checkbox\" name=\"agree\" value=\"true\">" +
 " <input type=\"submit\" name=\"submit\">" +
 " </form>" +
 "</body></html>";

 Parser parser = Jsoup.parser();
 Tag tag = parser.parseBodyFragment(html).select("form").first();
 FormElement formWithCheckbox = new FormElement(tag, "http://test.com", new Attributes());
 formWithCheckbox.getElements().first().attr("checked", "checked");
 List<Connection.KeyVal> formData = formWithCheckbox.formData();
 assertEquals(1, formData.size());
 assertEquals("agree", formData.get(0).key());
 assertEquals("true", formData.get(0).value());
 }
}