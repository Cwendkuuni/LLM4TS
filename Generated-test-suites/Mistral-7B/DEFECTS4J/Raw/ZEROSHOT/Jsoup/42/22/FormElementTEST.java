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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private Parser parser;
 private Element formElement;

 @Before
 public void setup() {
 parser = Jsoup.parser();
 String html = "<html><body><form action=\"http://test.com\" method=\"POST\"><input name=\"name1\" type=\"text\"><input name=\"name2\" type=\"text\" checked><select name=\"name3\"><option value=\"value1\">Option 1</option><option selected value=\"value2\">Option 2</option></select></form></body></html>";
 formElement = parser.parse(html).body().selectFirst("form");
 }

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "inputName"));
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoActionUri() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 }
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(3, formData.size());

 String name1 = formData.get(0).key;
 assertEquals("name1", name1);
 String value1 = formData.get(0).val;
 assertNull(value1);

 String name2 = formData.get(1).key;
 assertEquals("name2", name2);
 String value2 = formData.get(1).val;
 assertNotNull(value2);

 String name3 = formData.get(2).key;
 assertEquals("name3", name3);
 String value3 = formData.get(2).val;
 assertEquals("value2", value3);
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 FormElement formElement2 = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 assertTrue(formElement1.equals(formElement2));

 FormElement formElement3 = new FormElement(new Tag("form"), "http://test.com", new Attributes().put("data", "testData"));
 assertFalse(formElement1.equals(formElement3));
 }
}