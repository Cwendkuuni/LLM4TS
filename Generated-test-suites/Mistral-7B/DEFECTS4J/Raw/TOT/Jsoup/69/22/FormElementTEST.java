import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

 private Document doc;

 @Rule
 public ExpectedException expectedEx = ExpectedException.none();

 @Before
 public void setUp() {
 String html = "<html lang=\"en\"><body>\n" +
 "  <form method=\"get\" action=\"/\">\n" +
 "    <input type=\"text\" name=\"username\" value=\"John Doe\">\n" +
 "    <input type=\"password\" name=\"password\" value=\"password123\">\n" +
 "    <select name=\"color\">\n" +
 "      <option value=\"red\" selected>Red</option>\n" +
 "      <option value=\"blue\">Blue</option>\n" +
 "    </select>\n" +
 "    <input type=\"checkbox\" name=\"agree\" value=\"yes\" checked>\n" +
 "    <input type=\"checkbox\" name=\"agree\" value=\"no\">\n" +
 "    <input type=\"submit\" value=\"Submit\">\n" +
 "  </form>\n" +
 "  <div>\n" +
 "  </div>\n" +
 " </html>";
 doc = Jsoup.parse(html);
 }

 // Test case for elements() method
 @org.junit.Test
 public void testFormElementElements() {
 FormElement form = (FormElement) doc.select("form").first();
 List<Element> formElements = form.elements();
 assertEquals(6, formElements.size());
 assertEquals("input[type=\"text\"]", formElements.get(0).tag().name());
 assertEquals("input[type=\"password\"]", formElements.get(1).tag().name());
 assertEquals("select", formElements.get(2).tag().name());
 assertEquals("input[type=\"checkbox\"]:checked", formElements.get(3).tag().name());
 assertEquals("input[type=\"checkbox\"]:not(:checked)", formElements.get(4).tag().name());
 assertEquals("input[type=\"submit\"]", formElements.get(5).tag().name());
 }

 // Test case for addElement() method
 @org.junit.Test
 public void testFormElementAddElement() {
 FormElement form = (FormElement) doc.select("form").first();
 Element input = new Element("input", new Attributes().put("type", "text").put("name", "newElement").put("value", "newValue"));
 form.addElement(input);
 assertEquals(7, form.elements().size());
 assertEquals("input[name=\"newElement\"]", form.elements().get(6).tag().name());
 }

 // Test case for submit() method
 @org.junit.Test
 public void testFormElementSubmit() {
 FormElement form = (FormElement) doc.select("form").first();
 Connection connection = form.submit();
 assertEquals("GET", connection.method().name());
 assertEquals("/", connection.url().toString());
 List<Connection.KeyVal> formData = connection.requestBody();
 assertEquals(5, formData.size());
 assertEquals("username=John%20Doe", formData.get(0).key());
 assertEquals("password=password123", formData.get(1).key());
 assertEquals("color=red", formData.get(2).key());
 assertEquals("agree=yes", formData.get(3).key());
 assertEquals("", formData.get(4).key());
 }

 // Test case for formData() method with empty form
 @org.junit.Test
 public void testFormElementFormDataEmptyForm() {
 FormElement form = (FormElement) doc.select("form[action=\"\"]").first();
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(0, formData.size());
 }

 // Test case for formData() method with disabled input
 @org.junit.Test
 public void testFormElementFormDataDisabledInput() {
 FormElement form = (FormElement) doc.select("form").first();
 Element input = form.elements().get(0);
 input.attr("disabled", "disabled");
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(4, formData.size());
 // disabled input is not included in form data
 assertFalse(formData.contains(new Connection.KeyVal("username", "John Doe")));
 }

 // Test case for formData() method with select tag
 @org.junit.Test
 public void testFormElementFormDataSelectTag() {
 FormElement form = (FormElement) doc.select("form").first();
 Element select = form.elements().get(2);
 select.children().get(1).attr("selected", "selected");
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(4, formData.size());
 assertEquals("color=blue", formData.get(2).key());
 }

 // Test case for formData() method with checkbox tags
 @org.junit.Test
 public void testFormElementFormDataCheckboxTags() {
 FormElement form = (FormElement) doc.select("form").first();
 Element checkbox1 = form.elements().get(3);
 checkbox1.attr("checked", "checked");
 Element checkbox2 = form.elements().get(4);
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(6, formData.size());
 assertEquals("agree=yes", formData.get(4).key());
 assertEquals("agree=no", formData.get(5).key());
 }

 // Test case for submit() method with empty form action
 @org.junit.Test
 public void testFormElementSubmitEmptyAction() {
 FormElement form = new FormElement(new Tag("form"), "", new Attributes());
 expectedEx.expect(IllegalArgumentException.class);
 form.submit();
 }

 // Test case for submit() method with form action containing invalid URL
 @org.junit.Test
 public void testFormElementSubmitInvalidUrl() {
 FormElement form = (FormElement) doc.select("form").first();
 form.attr("action", "javascript:alert('Hello')");
 expectedEx.expect(IllegalArgumentException.class);
 form.submit();
 }
}