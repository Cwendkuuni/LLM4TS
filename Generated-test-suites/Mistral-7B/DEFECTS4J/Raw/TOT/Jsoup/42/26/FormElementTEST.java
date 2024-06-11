import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private ParseSetup setup;

 @Before
 public void setup() {
 setup = new ParseSetup.UrlResolver() {
 @Override
 public String resolveUrl(String url) {
 return "http://localhost";
 }
 };
 Jsoup.shutdownHooksAdd(setup);
 }

 @Test
 public void testCreateFormElement() {
 String html = "<form action=\"/\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"submit\" name=\"submit\" />" +
 "</form>";

 Document doc = Jsoup.parse(html, setup);
 FormElement form = doc.selectFirst("form");

 assertEquals("form", form.tagName());
 assertEquals("", form.tag().classes().toString());
 assertNotNull(form.elements());
 assertNull(form.attributes().getClass());
 }

 @Test
 public void testEmptyFormElement() {
 String html = "<form></form>";

 Document doc = Jsoup.parse(html, setup);
 FormElement form = doc.selectFirst("form");

 assertEquals("form", form.tagName());
 assertEquals("", form.tag().classes().toString());
 assertNotNull(form.elements());
 }

 @Test
 public void testFormWithNoActionAttribute() {
 String html = "<form>" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"submit\" name=\"submit\" />" +
 "</form>";

 Document doc = Jsoup.parse(html, setup);
 FormElement form = doc.selectFirst("form");

 assertNull(form.absUrl("action"));
 }

 @Test
 public void testFormWithInvalidActionAttribute() {
 String html = "<form action=\"javascript:alert('Hello')\" >" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"submit\" name=\"submit\" />" +
 "</form>";

 Document doc = Jsoup.parse(html, setup);
 FormElement form = doc.selectFirst("form");

 assertNull(form.absUrl("action"));
 }

 @Test
 public void testFormWithSubmitAttribute() {
 String html = "<form method=\"POST\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"submit\" name=\"submit\" />" +
 "</form>";

 Document doc = Jsoup.parse(html, setup);
 FormElement form = doc.selectFirst("form");

 assertEquals(Connection.Method.POST, form.method());
 }

 @Test
 public void testFormWithGetAttribute() {
 String html = "<form method=\"GET\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"submit\" name=\"submit\" />" +
 "</form>";

 Document doc = Jsoup.parse(html, setup);
 FormElement form = doc.selectFirst("form");

 assertEquals(Connection.Method.GET, form.method());
 }

 @Test
 public void testFormWithMultipleControls() {
 String html = "<form action=\"/\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"text\" name=\"password\" />" +
 " <input type=\"submit\" name=\"submit\" />" +
 "</form>";

 Document doc = Jsoup.parse(html, setup);
 FormElement form = doc.selectFirst("form");

 Elements controls = form.elements();
 assertEquals(3, controls.size());
 }

 @Test
 public void testFormSubmission() {
 String html = "<form action=\"/\">" +
 " <input type=\"text\" name=\"username\" value=\"john\" />" +
 " <input type=\"hidden\" name=\"password\" value=\"password\" />" +
 " <input type=\"submit\" name=\"submit\" />" +
 "</form>";

 Document doc = Jsoup.parse(html, setup);
 FormElement form = doc.selectFirst("form");
 Connection con = form.submit();
 assertNotNull(con);
 List<Connection.KeyVal> data = form.formData();
 assertEquals(2, data.size());
 assertEquals("username", data.get(0).key);
 assertEquals("john", data.get(0).val);
 assertEquals("password", data.get(1).key);
 assertEquals("password", data.get(1).val);
 }

 @Test
 public void testFormSubmissionWithMultipleSelects() {
 String html = "<form action=\"/\">" +
 " <select name=\"fruits[]\">" +
 " <option value=\"apple\">Apple</option>" +
 " <option value=\"banana\" selected>Banana</option>" +
 " </select>" +
 " <select name=\"colors[]\">" +
 " <option value=\"red\" selected>Red</option>" +
 " <option value=\"green\">Green</option>" +
 " </select>" +
 " <input type=\"submit\" name=\"submit\" />" +
 "</form>";

 Document doc = Jsoup.parse(html, setup);
 FormElement form = doc.selectFirst("form");
 Connection con = form.submit();
 assertNotNull(con);
 List<Connection.KeyVal> data = form.formData();
 assertEquals(4, data.size());
 assertEquals("fruits[]", data.get(0).key);
 assertEquals("banana", data.get(0).val);
 assertEquals("colors[]", data.get(1).key);
 assertEquals("red", data.get(1).val);
 assertNull(data.get(2));
 assertNull(data.get(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormSubmissionWithoutBaseURI() {
 String html = "<form action=\"/\">" +
 " <input type=\"text\" name=\"username\" value=\"john\" />" +
 " <input type=\"submit\" name=\"submit\" />" +
 "</form>";

 Document doc = Jsoup.parse(html); // no baseURI set
 FormElement form = doc.selectFirst("form");
 try {
 form.submit();
 } catch (IllegalArgumentException e) {
 throw new AssertionError("Should not reach here", e);
 }
 }
}