import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<html><body>" +
 "<form action='/submit' method='post'>" +
 "<input type='text' name='username' value='John'>" +
 "<input type='text' name='password' value='password'>" +
 "<select name='country'>" +
 "<option value='us'>United States</option>" +
 "<option value='ca' selected>Canada</option>" +
 "</select>" +
 "<input type='submit' value='Submit'>" +
 "</form>" +
 "</body></html>");
 }

 @Test
 public void testElements() {
 FormElement form = doc.selectFirst("form");
 List<Element> elements = form.elements();
 assertEquals(3, elements.size());
 assertTrue(elements.contains(form.select("#username")));
 assertTrue(elements.contains(form.select("#password")));
 assertTrue(elements.contains(form.select("#country")));
 }

 @Test
 public void testAddElement() {
 FormElement form = doc.selectFirst("form");
 Element newElement = doc.createElement("input");
 newElement.attr("type", "hidden");
 newElement.attr("name", "newField");
 form.addElement(newElement);
 assertEquals(4, form.elements().size());
 }

 @Test
 public void testSubmit() {
 FormElement form = doc.selectFirst("form");
 Connection connection = form.submit();
 assertEquals("/submit", connection.url().toString());
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(3, formData.size());
 assertEquals("John", formData.get(0).key());
 assertEquals("John", formData.get(0).val());
 assertEquals("password", formData.get(1).key());
 assertEquals("password", formData.get(1).val());
 assertEquals("ca", formData.get(2).key());
 assertEquals("Canada", formData.get(2).val());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitNoBaseUri() {
 FormElement form = doc.selectFirst("form");
 form.submit(); // throw exception if baseUri not set
 }

 @Test
 public void testSubmitNoAction() {
 FormElement form = doc.selectFirst("form[action='']");
 try {
 form.submit();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}