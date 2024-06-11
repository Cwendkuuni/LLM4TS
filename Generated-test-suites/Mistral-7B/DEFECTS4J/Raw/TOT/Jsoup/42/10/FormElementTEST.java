import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseException;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setup() throws ParseException {
 String html = "<html><body><form action=\"http://example.com\" method=\"post\"><input type=\"text\" name=\"username\" value=\"test\"><input type=\"text\" name=\"password\" value=\"password\"><select name=\"country\"><option selected value=\"US\">United States</option><option value=\"CA\">Canada</option></select><input type=\"submit\" name=\"submit\"></form></body></html>";
 formElement = Jsoup.parse(html).body().getElementsByTag("form").first();
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(Tag.FORM, null, formElement.attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "newElement"));
 FormElement formElement = new FormElement(Tag.FORM, null, formElement.attributes());
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(4, elements.size());
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 List<Connection.KeyVal> formData = formElement.formData();
 List<String> expectedFormData = Arrays.asList("username=test", "password=password", "country=US", "submit=submit");
 assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseURI() {
 FormElement formElement = new FormElement(Tag.FORM, null, new Attributes().put("action", ""));
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 FormElement formElement = new FormElement(Tag.FORM, null, new Attributes().put("action", ""));
 formElement.submit();
 }
}