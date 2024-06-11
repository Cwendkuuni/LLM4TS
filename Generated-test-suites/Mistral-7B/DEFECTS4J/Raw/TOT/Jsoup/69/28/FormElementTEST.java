import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class FormElementTest {

 private Element formElement;
 private Connection connection;

 @Before
 public void setup() {
 String html = "<html>" +
 "<body>" +
 "<form action='/' method='post'>" +
 "<input type='text' name='username'/>" +
 "<input type='password' name='password'/>" +
 "<select name='colors'>" +
 "<option value='red'>Red</option>" +
 "<option value='green' selected>Green</option>" +
 "<option value='blue'>Blue</option>" +
 "</select>" +
 "<input type='checkbox' name='likeCoffee' value='true'/>" +
 "<input type='radio' name='animal' value='dog' checked/>" +
 "</form>" +
 "</body>" +
 "</html>";
 ParseSetting.setDefaultparseHtml(true);
 Document doc = Jsoup.parseBodyFragment(html);
 formElement = doc.select("form").first();
 }

 @Test
 public void testElements() {
 Elements expectedElements = formElement.elements();
 Elements actualElements = formElement.elements();
 assertEquals(expectedElements.size(), actualElements.size());
 for (int i = 0; i < expectedElements.size(); i++) {
 assertEquals(expectedElements.get(i).tagName(), actualElements.get(i).tagName());
 }
 }

 @Test
 public void testSubmit() {
 List<Connection.KeyVal> formData = formElement.submit().data();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("username", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("password", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("colors", "green"));
 expectedFormData.add(HttpConnection.KeyVal.create("likeCoffee", "true"));
 expectedFormData.add(HttpConnection.KeyVal.create("animal", "dog"));
 assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseUri() {
 FormElement formElementWithoutBaseUri = new FormElement(new Tag("form"), "", new Attributes());
 try {
 formElementWithoutBaseUri.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 }
 }

 @Test
 public void testSubmitWithoutAction() {
 FormElement formElementWithoutAction = new FormElement(new Tag("form"), "", new Attributes().put("method", "post"));
 try {
 formElementWithoutAction.submit();
 } catch (IllegalArgumentException e) {
 fail("An exception should have been thrown as form does not have an action attribute.");
 }
 }
}