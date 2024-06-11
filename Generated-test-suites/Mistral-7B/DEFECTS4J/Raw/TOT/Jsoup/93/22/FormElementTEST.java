import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html lang=\"en\"><body>" +
 " <form action=\"/\">" +
 " <input name=\"username\" type=\"text\"/>" +
 " <input name=\"password\" type=\"password\"/>" +
 " <select name=\"country\">" +
 " <option value=\"US\">United States</option>" +
 " <option value=\"CA\">Canada</option>" +
 " <option selected value=\"MX\">Mexico</option>" +
 " </select>" +
 " <input name=\"agreeTerms\" type=\"checkbox\" checked/> Agree to Terms" +
 " <button type=\"submit\">Submit</button>" +
 " </form>" +
 "</body></html>";
 formElement = Jsoup.parse(html).select("form").first().as(FormElement.class);
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertEquals(5, elements.size());
 assertInstanceOf(Element.class, elements.get(0));
 assertInstanceOf(Element.class, elements.get(1));
 assertInstanceOf(Element.class, elements.get(2));
 assertInstanceof(Element.class, elements.get(3));
 assertInstanceOf(TextNode.class, elements.get(4));
 }

 @Test
 public void testAddElement() {
 Element newInput = new Element(Tag.INPUT, null, new Attributes()
 .put("name", "newInput")
 .put("type", "text"));
 formElement.addElement(newInput);
 Elements elements = formElement.elements();
 assertEquals(6, elements.size());
 assertInstanceOf(Element.class, elements.get(5));
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertEquals(Connection.Method.GET, connection.method());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(6, formData.size());
 List<String> keys = new ArrayList<>(formData.size());
 for (Connection.KeyVal pair : formData) {
 keys.add(pair.key());
 }
 assertArrayEquals(new String[]{"username", "password", "country", "agreeTerms", "action", ""}, keys.toArray(new String[0]));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitIllegalArgumentException() {
 formElement.submit();
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testFormDataEmptySelect() {
 Element emptySelect = new Element(Tag.SELECT, null, new Attributes().put("name", "emptySelect"));
 formElement.addElement(emptySelect);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(7, formData.size());
 List<Connection.KeyVal> emptySelectData = formData.subList(5, formData.size());
 assertTrue(emptySelectData.isEmpty());
 }

 @Test
 public void testFormDataCheckboxNotChecked() {
 Element checkbox = new Element(Tag.INPUT, null, new Attributes().put("name", "checkbox").put("type", "checkbox"));
 formElement.addElement(checkbox);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(7, formData.size());
 assertTrue(formData.get(5).value().isEmpty());
 }

 @Test
 public void testFormDataCheckboxChecked() {
 Element checkbox = new Element(Tag.INPUT, null, new Attributes().put("name", "checkbox").put("type", "checkbox").put("checked", "checked"));
 formElement.addElement(checkbox);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(8, formData.size());
 assertEquals("on", formData.get(7).value());
 }
}