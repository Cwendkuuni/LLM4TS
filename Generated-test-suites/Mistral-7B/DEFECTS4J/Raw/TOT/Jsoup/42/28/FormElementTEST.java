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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class FormElementTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<html><body>" +
 "<form action=\"/submit\">" +
 "<input type=\"text\" name=\"username\" value=\"John\">" +
 "<input type=\"hidden\" name=\"csrf\" value=\"abc123\">" +
 "<input type=\"submit\" value=\"Submit\">" +
 "</form>" +
 "</body></html>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement form = (FormElement) doc.select("form").first();
 assertNotNull(form);
 }

 @Test
 public void testFormElementGetElements() {
 FormElement form = (FormElement) doc.select("form").first();
 Elements elements = form.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testFormElementAddElement() {
 FormElement form = (FormElement) doc.select("form").first();
 Element input = doc.createElement("input");
 input.attr("type", "text");
 input.attr("name", "password");
 form.addElement(input);
 Elements elements = form.elements();
 assertEquals(4, elements.size());
 assertSame(input, elements.get(3));
 }

 @Test
 public void testFormElementSubmit() {
 FormElement form = (FormElement) doc.select("form").first();
 Connection connection = form.submit();
 assertNotNull(connection);
 assertEquals("/submit", connection.url().toString());
 }

 @Test
 public void testFormElementFormData() {
 FormElement form = (FormElement) doc.select("form").first();
 List<Connection.KeyVal> formData = form.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>(Arrays.asList(
 new Connection.KeyVal("username", "John"),
 new Connection.KeyVal("csrf", "abc123")
 ));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormElementFormDataHiddenInput() {
 FormElement form = (FormElement) doc.select("form").first();
 Element hiddenInput = form.select("input[name=csrf]").first();
 form.elements().remove(hiddenInput);
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(1, formData.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitWithoutBaseUri() {
 FormElement form = doc.createElement("form");
 try {
 form.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 }
 }

 @Test
 public void testFormElementSubmitWithInvalidActionUrl() {
 FormElement form = doc.createElement("form");
 form.baseUri("http://example.com");
 form.attr("action", "");
 try {
 form.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit.", e.getMessage());
 }
 }
}