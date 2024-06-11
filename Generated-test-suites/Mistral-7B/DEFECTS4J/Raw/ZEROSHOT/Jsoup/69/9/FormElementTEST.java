import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private Element formElement;
 private Connection connection;

 @Before
 public void setup() {
 Parser parser = new Parser.MalformedHtmlParser();
 String html = "<html><body>" +
 "    <form method=\"get\" action=\"https://example.com/\">" +
 "        <input type=\"text\" name=\"username\" value=\"test\">" +
 "        <input type=\"text\" name=\"password\" value=\"password\">" +
 "        <select name=\"country\">" +
 "            <option value=\"USA\">USA</option>" +
 "            <option value=\"Canada\" selected>Canada</option>" +
 "        </select>" +
 "        <input type=\"checkbox\" name=\"agree\" checked>" +
 "        <input type=\"submit\" value=\"Submit\">" +
 "    </form>" +
 "    <div id=\"otherElement\"></div>" +
 "    <script src=\"script.js\"></script>" +
 "</body></html>";

 Document doc = parser.parse(html);
 Tag formTag = doc.selectFirst("form");
 formElement = new FormElement(formTag, "", new Attributes());
 }

 @Test
 public void testElements() {
 List<Element> expectedElements = new ArrayList<>();
 expectedElements.add(new Element(new Tag("input", new Attributes().put("type", "text").put("name", "username").put("value", "test"))));
 expectedElements.add(new Element(new Tag("input", new Attributes().put("type", "text").put("name", "password").put("value", "password"))));
 expectedElements.add(new Element(new Tag("select", new Attributes().put("name", "country"))));
 expectedElements.add(new Element(new Tag("option", new Attributes().put("value", "USA"))));
 expectedElements.add(new Element(new Tag("option", new Attributes().put("value", "Canada").put("selected", "selected"))));
 expectedElements.add(new Element(new Tag("input", new Attributes().put("type", "checkbox").put("name", "agree").put("checked", "checked"))));

 List<Element> actualElements = formElement.elements();
 assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testSubmit() throws Exception {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("username", "test"));
 expectedData.add(new Connection.KeyVal("password", "password"));
 expectedData.add(new Connection.KeyVal("country", "Canada"));
 expectedData.add(new Connection.KeyVal("agree", "on"));

 connection = formElement.submit();
 assertNotNull(connection);
 List<Connection.KeyVal> actualData = connection.data();
 assertEquals(expectedData, actualData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 formElement = new FormElement(new Tag("form", new Attributes()), "", new Attributes());
 formElement.submit();
 }
}