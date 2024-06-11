import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private Document doc;

 @Before
 public void setUp() {
 String html = "<html lang=\"en\"><head><meta charset=\"UTF-8\"/><title>Test Form</title></head>" +
 "<body>" +
 "<form action=\"/submit\" method=\"post\">" +
 "<input type=\"text\" name=\"username\" value=\"test\">" +
 "<input type=\"text\" name=\"password\" value=\"password\">" +
 "<select name=\"country\">" +
 "<option value=\"USA\">USA</option>" +
 "<option value=\"CA\" selected>Canada</option>" +
 "</select>" +
 "<input type=\"submit\" value=\"Submit\">" +
 "</form>" +
 "</body></html>";
 doc = Jsoup.parse(html);
 }

 @Test
 public void testFormElementCreation() {
 FormElement form = (FormElement) doc.selectFirst("form");
 assertNotNull(form);
 }

 @Test
 public void testElementsMethod() {
 FormElement form = (FormElement) doc.selectFirst("form");
 Elements elements = form.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement form = (FormElement) doc.selectFirst("form");
 Element input = new Elements(doc.parse("<input type=\"text\" name=\"newInput\" value=\"test\"/>")).first();
 form.addElement(input);
 Elements elements = form.elements();
 assertEquals(4, elements.size());
 }

 @Test
 public void testSubmitMethod() throws Exception {
 FormElement form = (FormElement) doc.selectFirst("form");
 Connection connection = form.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithoutBaseUri() {
 FormElement form = (FormElement) doc.selectFirst("form");
 form.submit();
 }

 @Test
 public void testFormDataMethod() {
 FormElement form = (FormElement) doc.selectFirst("form");
 List<Connection.KeyVal> formData = form.formData();
 assertNotNull(formData);
 assertEquals(5, formData.size());
 assertEquals("test", formData.get(0).key);
 assertEquals("test", formData.get(0).value);
 assertEquals("password", formData.get(1).key);
 assertEquals("password", formData.get(1).value);
 assertEquals("Canada", formData.get(2).key);
 assertEquals("CA", formData.get(2).value);
 assertNull(formData.get(3).key);
 assertNull(formData.get(3).value);
 assertNull(formData.get(4).key);
 assertNull(formData.get(4).value);
 }

 @Test
 public void testFormDataMethodWithEmptyForm() {
 FormElement emptyForm = new FormElement(new Tag("form"), null, new Attributes());
 List<Connection.KeyVal> formData = emptyForm.formData();
 assertNotNull(formData);
 assertEquals(0, formData.size());
 }
}