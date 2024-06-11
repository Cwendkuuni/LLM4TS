import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private Document document;

 @Before
 public void setup() {
 document = Jsoup.parse("<html><body><form action='/' method='post'><input type='text' name='username' /><input type='submit' name='submit' value='Submit' /></form></body></html>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = document.selectFirst("form");
 assertNotNull(formElement);
 }

 @Test
 public void testEmptyFormElement() {
 FormElement formElement = document.selectFirst("form[action='']");
 assertNull(formElement);
 }

 @Test
 public void testFormElementMethod() {
 FormElement formElement = document.selectFirst("form");
 assertEquals(Connection.Method.POST, formElement.submit().method());
 }

 @Test
 public void testFormElementElements() {
 FormElement formElement = document.selectFirst("form");
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(2, elements.size());
 }

 @Test
 public void testFormElementAddElement() {
 FormElement formElement = document.selectFirst("form");
 Element inputElement = new Element("input");
 inputElement.attr("type", "hidden");
 inputElement.attr("name", "hiddenInput");
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testFormElementRemoveElement() {
 FormElement formElement = document.selectFirst("form");
 Element inputElement = formElement.elements().get(0);
 formElement.removeChild(inputElement);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 }

 @Test
 public void testFormElementSubmitWithData() {
 FormElement formElement = document.selectFirst("form");
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("username", formData.get(0).key());
 assertNull(formData.get(0).val()); // checkboxes/radios won't be set in submit() method
 assertEquals("Submit", formData.get(1).key());
 assertEquals("Submit", formData.get(1).val());
 }
}