package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PowerMockRunner.Silent
public class FormElementTest {

 private static final String BASE_URI = "http://example.com";
 private static final String FORM_HTML = "<form action='/' method='post'>\n" +
         "<input type='text' name='username'>\n" +
         "<input type='password' name='password'>\n" +
         "<select name='country'>\n" +
         "<option value='US'>United States</option>\n" +
         "<option value='CA' selected>Canada</option>\n" +
         "</select>\n" +
         "<input type='checkbox' name='agree' checked>\n" +
         "<input type='submit' value='Submit'>\n" +
         "</form>";

 @Test
 public void testCreateFormElement() {
 FormElement formElement = PowerMockito.spy(Jsoup.parse(BASE_URI, FORM_HTML, new AnnotatedClassLoader()).select("form").first());
 Tag tag = formElement.tag();
 String baseUri = formElement.baseUri();
 Attributes attributes = formElement.attributes();

 assertTrue(tag instanceof Tag);
 assertEquals("form", tag.tagName());
 assertEquals(BASE_URI, baseUri);
 assertEquals(11, formElement.children().size());
 assertEquals(2, formElement.elements().size());
 }

 @Test
 public void testGetFormControls() {
 FormElement formElement = PowerMockito.spy(Jsoup.parse(BASE_URI, FORM_HTML, new AnnotatedClassLoader()).select("form").first());
 List<Element> formControls = formElement.elements();

 assertTrue(formControls instanceof Elements);
 assertEquals(3, formControls.size());

 Element usernameInput = formControls.get(0);
 assertEquals("input", usernameInput.tagName());
 assertTrue(usernameInput.hasAttr("type"));
 assertEquals("text", usernameInput.attr("type"));

 Element passwordInput = formControls.get(1);
 assertEquals("input", passwordInput.tagName());
 assertTrue(passwordInput.hasAttr("type"));
 assertEquals("password", passwordInput.attr("type"));

 Element selectElement = formControls.get(2);
 assertEquals("select", selectElement.tagName());
 assertTrue(selectElement.hasAttr("name"));
 assertEquals("country", selectElement.attr("name"));
 }

 @Test
 public void testAddElement() {
 FormElement formElement = PowerMockito.spy(Jsoup.parse(BASE_URI, FORM_HTML, new AnnotatedClassLoader()).select("form").first());
 Element newElement = Mockito.mock(Element.class);

 formElement.addElement(newElement);
 assertTrue(formElement.elements().size() == 4);
 }

 @Test
 public void testDetermineFormActionUrl() {
 FormElement formElement = PowerMockito.spy(Jsoup.parse(BASE_URI, FORM_HTML, new AnnotatedClassLoader()).select("form").first());

 String formActionUrl = formElement.submit().url();
 assertEquals(BASE_URI, formActionUrl);
 }

 @Test
 public void testSubmitFormWithGETMethod() {
 FormElement formElement = PowerMockito.spy(Jsoup.parse(BASE_URI, FORM_HTML, new AnnotatedClassLoader()).select("form").first());

 Connection connection = formElement.submit();
 connection.method(Connection.Method.GET);
 List<Connection.KeyVal> formData = formElement.formData();

 assertTrue(connection instanceof Connection);
 assertEquals(Connection.Method.GET, connection.method());
 assertEquals(2, formData.size());

 String username = formData.get(0).key();
 assertEquals("username", username);
 String usernameValue = formData.get(0).val();
 assertNull(usernameValue);

 String password = formData.get(1).key();
 assertEquals("password", password);
 assertNull(formData.get(1).val());
 }

 @Test
 public void testSubmitFormWithPOSTMethod() {
 FormElement formElement = PowerMockito.spy(Jsoup.parse(BASE_URI, FORM_HTML, new AnnotatedClassLoader()).select("form").first());

 Connection connection = formElement.submit();
 connection.method(Connection.Method.POST);
 List<Connection.KeyVal> formData = formElement.formData();

 assertTrue(connection instanceof Connection);
 assertEquals(Connection.Method.POST, connection.method());
 assertEquals(3, formData.size());

 String username = formData.get(0).key();
 assertEquals("username", username);
 assertNull(formData.get(0).val());

 String password = formData.get(1).key();
 assertEquals("password", password);
 assertNull(formData.get(1).val());

 String country = formData.get(2).key();
 assertEquals("country", country);
 assertEquals("Canada", formData.get(2).val());
 }
}