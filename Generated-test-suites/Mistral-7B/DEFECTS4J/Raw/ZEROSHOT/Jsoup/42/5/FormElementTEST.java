import org.junit.Before;
import org.junit.Test;
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
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setup() {
 String html = "<html><body>" +
 " <form action='/' method='post'>" +
 " <input name='input1' type='text' value='value1'>" +
 " <input name='input2' type='hidden' value='value2'>" +
 " <select name='select1'>" +
 " <option value='option1' selected>" +
 " Option 1" +
 " </option>" +
 " <option value='option2'>" +
 " Option 2" +
 " </option>" +
 " </select>" +
 " <input name='input3' type='checkbox' value='value3' checked>" +
 " <input name='input4' type='checkbox' value='value4'>" +
 " <input type='submit' value='Submit'>" +
 " </form>" +
 " </body>" +
 "</html>";

 Parser parser = Jsoup.parseBodyFragment(html);
 formElement = parser.select("form").first();
 }

 @Test
 public void testElements() {
 // Test getting form elements
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());

 // Test getting input elements
 Element input1 = elements.get(0);
 Equals expectInput1 = new Equals("input1", "value1");
 assertEquals(expectInput1, input1.attributes().get("name"));
 assertEquals("text", input1.tagName());

 Element input2 = elements.get(1);
 Equals expectInput2 = new Equals("input2", "value2");
 assertEquals(expectInput2, input1.attributes().get("name"));
 assertEquals("hidden", input2.tagName());

 // Test getting select element
 Element select = elements.get(2);
 assertEquals("select", select.tagName());

 // Test getting checkbox elements
 Element checkbox1 = elements.get(3);
 assertEquals("checkbox", checkbox1.tagName());
 assertTrue(checkbox1.hasAttr("checked"));

 Element checkbox2 = elements.get(4);
 assertEquals("checkbox", checkbox2.tagName());
 assertFalse(checkbox2.hasAttr("checked"));
 }

 @Test
 public void testSubmit() {
 // Test form submission
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("input1", "value1"),
 new Connection.KeyVal("input2", "value2"),
 new Connection.KeyVal("select1", "option1"),
 new Connection.KeyVal("input3", "value3"),
 new Connection.KeyVal("input3", "value3") // checkbox with checked attributes is sent twice
 );

 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertEquals(expectedFormData, formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseUri() {
 // Test submission without base URI
 FormElement formElementWithoutBaseUri = new FormElement(new Tag("form"), null, null);
 formElementWithoutBaseUri.addElement(new Elements.Element("input", new Attributes()));
 List<Connection.KeyVal> formData = formElementWithoutBaseUri.submit().requestBody();
 }
}