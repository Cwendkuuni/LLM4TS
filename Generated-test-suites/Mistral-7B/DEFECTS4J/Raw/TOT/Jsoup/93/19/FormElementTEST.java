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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setup() {
 String html = "<html><body>" +
 " <form action=\"/submit\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" value=\"John\"/>" +
 " <input type=\"text\" name=\"password\" value=\"Password123\"/>" +
 " <select name=\"select\">" +
 " <option value=\"Option1\">Option 1</option>" +
 " <option value=\"Option2\" selected>Option 2</option>" +
 " </select>" +
 " <input type=\"submit\" value=\"Submit\"/>" +
 " </form>" +
 "</body></html>";
 formElement = Jsoup.parse(html).body().selectFirst("form");
 }

 @Test
 public void testFormElementCreation() {
 assertNotNull(formElement);
 assertEquals(Tag.FORM.name(), formElement.tagName());
 }

 @Test
 public void testElementsMethod() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement newFormElement = new FormElement(new Tag("form"), "", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "newElement"));
 newFormElement.addElement(inputElement);
 Elements newElements = newFormElement.elements();
 assertNotNull(newElements);
 assertEquals(2, newElements.size());
 }

 @Test
 public void testSubmitMethod() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithoutActionAttribute() {
 FormElement formElementWithoutAction = new FormElement(new Tag("form"), "", new Attributes());
 try {
 formElementWithoutAction.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 }
 }

 @Test
 public void testFormDataMethod() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(5, formData.size());

 // check the data for each form control
 assertEquals("username", formData.get(0).key);
 assertEquals("John", formData.get(0).value);
 assertEquals("password", formData.get(1).key);
 assertEquals("Password123", formData.get(1).value);
 assertEquals("select", formData.get(2).key);
 assertEquals("Option2", formData.get(2).value);
 assertEquals("submit", formData.get(3).key);
 assertEquals("Submit", formData.get(3).value);
 assertEquals("newElement", formData.get(4).key);
 assertEquals(null, formData.get(4).value);
 }
}