package org.jsoup.nodes.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseException;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 @Test
 public void testFormElementCreation() throws ParseException {
 // Given a valid HTML form element
 String html = "<form action='/' method='post'>\n" +
 " <input type='text' name='username'>\n" +
 " <input type='password' name='password'>\n" +
 " <select name='country'>\n" +
 " <option value='USA'>USA</option>\n" +
 " <option value='Canada' selected>Canada</option>\n" +
 " </select>\n" +
 " <input type='submit' value='Login'>\n" +
 " </form>";
 Element formElement = Jsoup.parseBodyFragment(html).getElementsByTag("form").first();

 // When creating a new FormElement instance
 FormElement form = new FormElement(formElement.tag(), formElement.baseUri(), formElement.attributes());

 // Then the FormElement instance should have the expected properties
 assertEquals("form", form.tag().name());
 assertEquals("", form.id());
 assertEquals("", form.className());
 assertEquals("", form.text());
 assertNotNull(form.elements());
 assertEquals(3, form.elements().size());
 assertEquals("POST", form.attr("method"));
 assertEquals("", form.attr("action"));
 }

 @Test
 public void testAddElement() throws ParseException {
 // Given a FormElement instance
 FormElement form = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "username"));

 // When adding an element to the FormElement
 form.addElement(inputElement);

 // Then the Element should be present in the FormElement's elements list
 assertEquals(1, form.elements().size());
 assertEquals(inputElement, form.elements().first());
 }

 @Test
 public void testFormDataEmptyForm() throws Exception {
 // Given a FormElement instance with no form controls
 FormElement form = new FormElement(new Tag("form", new Attributes()), "", new Attributes());

 // When calling formData() method
 List<Connection.KeyVal> formData = form.formData();

 // Then an empty list should be returned
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataTextInput() throws Exception {
 // Given a FormElement instance with a text input form control
 FormElement form = new FormElement(new Tag("form", new Attributes()), "", new Attributes()) {
 {
 addElement(new Element("input", new Attributes()
 .put("type", "text")
 .put("name", "username")));
 addElement(new Element("input", new Attributes()
 .put("type", "submit")
 .put("value", "Login")));
 }
 };
 Element textInput = form.elements().get(0);
 textInput.val("testuser");

 // When calling formData() method
 List<Connection.KeyVal> formData = form.formData();

 // Then the text input's value should be present in the list
 assertEquals(1, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("testuser", formData.get(0).value());
 }

 @Test
 public void testFormDataSelectInput() throws Exception {
 // Given a FormElement instance with a select input form control
 FormElement form = new FormElement(new Tag("form", new Attributes()), "", new Attributes()) {
 {
 addElement(new Element("input", new Attributes()
 .put("type", "hidden")
 .put("name", "_method")
 .put("value", "patch")));
 addElement(new Element("select", new Attributes()
 .put("name", "country")
 .put("size", "1")) {
 {
 addElement(new Element("option", new Attributes()) {
 {
 setText("USA");
 setSelected(true);
 }
 });
 addElement(new Element("option", new Attributes()) {
 {
 setText("Canada");
 }
 });
 }
 });
 addElement(new Element("input", new Attributes()
 .put("type", "submit")
 .put("value", "Update")));
 }
 };
 Element selectInput = form.elements().get(1);

 // When calling formData() method
 List<Connection.KeyVal> formData = form.formData();

 // Then the selected option's value should be present in the list
 assertEquals(1, formData.size());
 assertEquals("country", formData.get(0).key());
 assertEquals("USA", formData.get(0).value());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() throws Exception {
 // Given a FormElement instance with no action attribute
 FormElement form = new FormElement(new Tag("form", new Attributes()), "", new Attributes());

 // When calling submit() method
 Connection connection = form.submit();

 // Then an IllegalArgumentException should be thrown
 }

 @Test
 public void testSubmitWithAction() throws Exception {
 // Given a FormElement instance with an action attribute
 FormElement form = new FormElement(new Tag("form", new Attributes().put("action", "/login")), "", new Attributes());

 // When calling submit() method
 Connection connection = form.submit();

 // Then a Connection instance should be returned
 assertNotNull(connection);
 }
}