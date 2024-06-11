import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private FormElement formElement;
 private Element form;
 private Connection connection;

 @Before
 public void setUp() {
 Parser parser = new Parser.MalformedEncodingDetector();
 String html = "<html>" +
         "<body>" +
         "<form action='/' method='post'>" +
         "<input type='text' name='username' value='test'>" +
         "<input type='hidden' name='password' value='password'>" +
         "<select name='country'>" +
         "<option value='USA'>USA</option>" +
         "<option value='Canada' selected>Canada</option>" +
         "<option value='Mexico'>Mexico</option>" +
         "</select>" +
         "<input type='submit' name='submit' value='Submit'>" +
         "</form>" +
         "</body>" +
         "</html>";
 form = parser.parseBodyFragment(html);
 formElement = new FormElement(form.tag(), "", new Attributes());
 }

 @Test
 public void testFormElementConstructor() {
 Tag tag = form.tag();
 assertEquals("form", tag.tagName());
 }

 @Test
 public void testElementsMethod() {
 Elements expectedElements = Arrays.asList(
         form.select("input[type='text']"),
         form.select("input[type='hidden']"),
         form.select("select")
 ).iterator().asList();
 assertEquals(expectedElements, formElement.elements().iterator().asList());
 }

 @Test
 public void testAddElementMethod() {
 Element input = form.select("input[type='text']").first();
 formElement.addElement(input);
 Elements elementsAfterAdd = formElement.elements();
 assertEquals(elementsAfterAdd.size(), 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWhenNoActionAttribute() {
 formElement.submit();
 }

 @Test
 public void testFormDataMethod() {
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(Connection.KeyVal.create("username", "test"));
 expectedFormData.add(Connection.KeyVal.create("password", "password"));
 expectedFormData.add(Connection.KeyVal.create("country", "Canada"));
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testSubmitMethod() throws Exception {
 connection = formElement.submit();
 assertEquals("POST", connection.method().name());
 assertEquals("/", connection.url().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWhenFormActionURLCannotBeDetermined() {
 form.removeAttr("action");
 formElement = new FormElement(form.tag(), "", new Attributes());
 formElement.submit();
 }
}