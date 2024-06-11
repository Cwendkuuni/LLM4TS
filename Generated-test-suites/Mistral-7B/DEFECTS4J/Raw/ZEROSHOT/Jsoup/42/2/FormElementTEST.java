import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParsingMode;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 // Given
 String html = "<form action='/submit' method='post' name='myForm'><input type='text' name='username'><input type='submit' name='submit'></form>";
 Document doc = Jsoup.parse(html, ParsingMode.HTML_PARSE);
 Element formElement = doc.selectFirst("form");

 // When
 FormElement form = new FormElement((Tag) formElement.tag(), "/baseUrl", formElement.attributes());

 // Then
 assertEquals("form", form.tagName());
 assertEquals("/baseUrl", form.baseUri());
 Assert.assertNotNull(form.elements());
 }

 @Test
 public void testGetFormControls() {
 // Given
 String html = "<form action='/submit' method='post' name='myForm'><input type='text' name='username'><input type='password' name='password'><input type='submit' name='submit'></form>";
 Document doc = Jsoup.parse(html, ParsingMode.HTML_PARSE);
 Element formElement = doc.selectFirst("form");
 FormElement form = new FormElement((Tag) formElement.tag(), "/baseUrl", formElement.attributes());

 // When
 Elements controls = form.elements();

 // Then
 assertEquals(3, controls.size());
 // Check first control is text input
 assertEquals("input", controls.get(0).tagName());
 Attribute nameAttr = controls.get(0).attr("name");
 assertEquals("username", nameAttr.getValue());
 // Check second control is password input
 assertEquals("input", controls.get(1).tagName());
 assertEquals("password", controls.get(1).attr("type"));
 // Check third control is submit button
 assertEquals("input", controls.get(2).tagName());
 assertEquals("submit", controls.get(2).attr("type"));
 }

 @Test
 public void testSubmitForm() {
 // Given
 String html = "<form action='/submit' method='post' name='myForm'><input type='text' name='username' value='test'><input type='submit' name='submit'></form>";
 Document doc = Jsoup.parse(html, ParsingMode.HTML_PARSE);
 Element formElement = doc.selectFirst("form");
 FormElement form = new FormElement((Tag) formElement.tag(), "/baseUrl", formElement.attributes());

 // When
 Connection con = form.submit();
 List<Connection.KeyVal> formData = form.formData();

 // Then
 assertEquals(Connection.Method.POST, con.method());
 assertEquals(2, formData.size());
 // Check form data contains username=test
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("username", keyVal.key());
 assertEquals("test", keyVal.val());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithNoAction() {
 // Given
 String html = "<form name='myForm'><input type='text' name='username' value='test'><input type='submit' name='submit'></form>";
 Document doc = Jsoup.parse(html, ParsingMode.HTML_PARSE);
 Element formElement = doc.selectFirst("form");
 FormElement form = new FormElement((Tag) formElement.tag(), "/baseUrl", formElement.attributes());

 // When
 form.submit();

 // Then
 // Exception should be thrown
 }
}