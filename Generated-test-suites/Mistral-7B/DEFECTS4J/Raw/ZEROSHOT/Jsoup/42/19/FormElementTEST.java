import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import org.junit.Test;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 // given
 String html = "<form action=\"/test\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <select name=\"country\">" +
 " <option value=\"usa\">United States</option>" +
 " <option value=\"canada\" selected>Canada</option>" +
 " </select>" +
 " <input type=\"submit\" name=\"submit\" value=\"Submit\" />" +
 "</form>";

 // when
 Document doc = Jsoup.parse(html);
 Element formElement = doc.selectFirst("form");

 // then
 assertNotNull(formElement);
 assertSame(formElement.tagName(), "form");
 }

 @Test
 public void testFormElementGetElements() {
 // given
 String html = "<form action=\"/test\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <select name=\"country\">" +
 " <option value=\"usa\">United States</option>" +
 " <option value=\"canada\" selected>Canada</option>" +
 " </select>" +
 " <input type=\"submit\" name=\"submit\" value=\"Submit\" />" +
 "</form>";

 // when
 Document doc = Jsoup.parse(html);
 Element formElement = doc.selectFirst("form");
 Elements elements = formElement.elements();

 // then
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testFormElementAddElement() {
 // given
 String html = "<form action=\"/test\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" />" +
 "</form>";

 // when
 Document doc = Jsoup.parse(html);
 Element formElement = doc.selectFirst("form");

 Element inputElement = new Element("input");
 inputElement.attr("type", "hidden");
 inputElement.attr("name", "hiddenInput");
 inputElement.attr("value", "testValue");

 // when
 formElement.addElement(inputElement);

 // then
 Elements elements = formElement.elements();
 assertEquals(4, elements.size());
 }

 @Test
 public void testFormElementSubmit() {
 // given
 String html = "<form action=\"/test\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" value=\"testUser\" />" +
 " <input type=\"hidden\" name=\"hiddenInput\" value=\"testValue\" />" +
 " <select name=\"country\">" +
 " <option value=\"usa\">United States</option>" +
 " <option value=\"canada\" selected>Canada</option>" +
 " </select>" +
 " </form>";

 // when
 Document doc = Jsoup.parse(html);
 Element formElement = doc.selectFirst("form");
 Connection connection = formElement.submit();

 // then
 assertNotNull(connection);
 assertEquals(Connection.Method.POST, connection.method());
 List<Connection.KeyVal> formData = connection.requestBody();
 assertEquals(3, formData.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testFormElementSubmitWithoutBaseUri() {
 // given
 String html = "<form action=\"/test\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" />" +
 "</form>";

 // when
 Document doc = Jsoup.parse(html);
 Element formElement = doc.selectFirst("form");
 formElement.submit();
 }
}