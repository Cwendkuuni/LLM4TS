import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private Element formElement;
 private Connection connection;

 @Before
 public void setUp() {
 Parser parser = Jsoup.parse("" +
 "<html><body>" +
 "<form action='/submit' method='post'>" +
 "<input type='text' name='name' value='John'>" +
 "<input type='text' name='age' value='30'>" +
 "<select name='gender'>" +
 "<option value='male'>Male</option>" +
 "<option value='female' selected>Female</option>" +
 "</select>" +
 "<input type='submit'>" +
 "</form>" +
 "</body></html>");

 formElement = (FormElement) parser.selectFirst("form");
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElementCopy = formElement.clone();
 Element newElement = new Element("input", new Attributes().put("name", "newName").put("type", "text").put("value", "newValue"));
 formElementCopy.addElement(newElement);
 assertEquals(4, formElementCopy.elements().size());
 }

 @Test
 public void testSubmit() {
 List<Connection.KeyVal> formData = formElement.submit().data();
 assertEquals(6, formData.size());
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(6, formData.size());
 assertEquals("name", formData.get(0).key);
 assertEquals("John", formData.get(0).value);
 assertEquals("age", formData.get(1).key);
 assertEquals("30", formData.get(1).value);
 assertEquals("gender", formData.get(2).key);
 assertEquals("female", formData.get(2).value);
 }

 @Test
 public void testSubmitWithEmptyAction() {
 try {
 FormElement emptyFormElement = new FormElement(new Tag("form"), "", new Attributes());
 connection = emptyFormElement.submit();
 fail("IllegalArgumentException expected");
 } catch(IllegalArgumentException e) {
 // passing
 assertTrue(e.getMessage().contains("Could not determine a form action URL for submit"));
 }
 }

 @Test
 public void testSubmitWithGetMethod() {
 FormElement formElementGet = new FormElement(new Tag("form"), "", new Attributes().put("method", "get"));
 connection = formElementGet.submit();
 fail("IllegalArgumentException expected");
 }
}