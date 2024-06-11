import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private static final Parser parser = new Parser.Builder().build();
 private static final ParseSetup parseSetup = new ParseSetup.Builder().build();

 @Before
 public void setup() {
 parseSetup.setBaseUrl("http://example.com");
 }

 @Test
 public void testFormElementCreation() {
 String html = "<form>\n" +
 "  <input type=\"text\" name=\"username\" />\n" +
 "  <input type=\"password\" name=\"password\" />\n" +
 "  <select name=\"country\">\n" +
 "    <option value=\"USA\">USA</option>\n" +
 "    <option value=\"Canada\" selected>Canada</option>\n" +
 "  </select>\n" +
 "  <input type=\"submit\" name=\"submit\" value=\"Submit\" />\n" +
 "  <input type=\"hidden\" name=\"csrf\" value=\"abc123\" />\n" +
 "</form>";

 Element formElement = parser.parse(html, parseSetup).first();
 FormElement form = (FormElement) formElement;

 assertEquals("form", form.tagName());
 assertEquals(1, form.elements().size());
 }

 @Test
 public void testEmptyFormElementCreation() {
 String html = "<form></form>";
 FormElement form = parser.parse(html, parseSetup).first().asFormElement();
 assertEquals(0, form.elements().size());
 }

 @Test
 public void testFormElementGetElements() {
 String html = "<form>\n" +
 "  <input type=\"text\" name=\"username\" />\n" +
 "  <input type=\"password\" name=\"password\" />\n" +
 "  <select name=\"country\">\n" +
 "    <option value=\"USA\">USA</option>\n" +
 "    <option value=\"Canada\" selected>Canada</option>\n" +
 "  </select>\n" +
 "  <input type=\"submit\" name=\"submit\" value=\"Submit\" />\n" +
 "  <input type=\"hidden\" name=\"csrf\" value=\"abc123\" />\n" +
 "  <input type=\"text\" name=\"search\" />\n" +
 "  <input type=\"text\" name=\"search\" />\n" +
 "</form>";

 FormElement form = parser.parse(html, parseSetup).first().asFormElement();

 Element textInput1 = form.elements().get(0);
 Element textInput2 = form.elements().get(1);
 Element selectElement = form.elements().get(2);
 Element submitButton = form.elements().get(4);

 assertEquals("input", textInput1.tagName());
 assertEquals("text", textInput1.attr("type"));
 assertEquals("username", textInput1.attr("name"));

 assertEquals("select", selectElement.tagName());
 List<Element> options = selectElement.select("option[selected]");
 assertEquals(1, options.size());
 Element option = options.get(0);
 assertEquals("option", option.tagName());
 assertEquals("Canada", option.text());
 assertEquals("USA", selectElement.select("option:first-child").text());

 assertEquals("input", submitButton.tagName());
 assertEquals("submit", submitButton.attr("type"));
 assertEquals("submit", submitButton.attr("name"));
 }

 @Test
 public void testFormElementAddElement() {
 String html = "<form>\n" +
 "  <input type=\"text\" name=\"username\" />\n" +
 "  <input type=\"password\" name=\"password\" />\n" +
 "</form>";

 FormElement form = parser.parse(html, parseSetup).first().asFormElement();
 Element textInput = new Element("input");
 textInput.attr("type", "text");
 textInput.attr("name", "email");
 form.addElement(textInput);

 assertEquals(3, form.elements().size());
 }

 @Test
 public void testFormElementSubmit() {
 String html = "<form action=\"http://example.com/login\" method=\"post\">\n" +
 "  <input type=\"text\" name=\"username\" />\n" +
 "  <input type=\"password\" name=\"password\" />\n" +
 "  <select name=\"country\">\n" +
 "    <option value=\"USA\">USA</option>\n" +
 "    <option value=\"Canada\" selected>Canada</option>\n" +
 "  </select>\n" +
 "  <input type=\"submit\" name=\"submit\" value=\"Submit\" />\n" +
 "  <input type=\"hidden\" name=\"csrf\" value=\"abc123\" />\n" +
 "</form>";

 FormElement form = parser.parse(html, parseSetup).first().asFormElement();
 Connection connection = form.submit();

 assertEquals("http://example.com/login", connection.url());
 assertEquals(Connection.Method.POST, connection.method());

 List<Connection.KeyVal> formData = form.formData();
 assertEquals(6, formData.size());
 assertEquals("username", formData.get(0).key);
 assertEquals("", formData.get(0).val); // no value for text input
 assertEquals("password", formData.get(1).key);
 assertEquals("", formData.get(1).val); // no value for password input
 assertEquals("Canada", formData.get(2).val);
 assertEquals("", formData.get(2).key); // name not set for selected option
 assertEquals("csrf", formData.get(3).key);
 assertEquals("abc123", formData.get(3).val);
 assertEquals("submit", formData.get(4).key);
 assertEquals("", formData.get(4).val);
 assertEquals("country", formData.get(5).key);
 assertEquals("Canada", formData.get(5).val);
 }

 @Test(expected = IllegalStateException.class)
 public void testFormElementSubmitWithoutBaseUri() {
 String html = "<form action=\"http://example.com/login\" method=\"post\">\n" +
 "  <input type=\"text\" name=\"username\" />\n" +
 "  <input type=\"password\" name=\"password\" />\n" +
 "</form>";

 FormElement form = parser.parse(html).first().asFormElement();
 form.submit();
 }
}