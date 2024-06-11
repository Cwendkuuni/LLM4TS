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

public class FormElementTest {

 private Element formElement;
 private Connection connection;

 @Before
 public void setUp() throws Exception {
 String html = "<html><head></head><body>" +
 "   <form method=\"post\" action=\"https://test.com\">" +
 "       <input type=\"text\" name=\"username\" />" +
 "       <input type=\"password\" name=\"password\" />" +
 "       <select name=\"country\">" +
 "           <option value=\"USA\">USA</option>" +
 "       </select>" +
 "       <input type=\"submit\" value=\"Submit\" />" +
 "   </form>" +
 "</body></html>";

 Parser parser = Jsoup.parser();
 Document doc = parser.parse(html);
 formElement = doc.select("form").first();
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 Element inputElement = new Element("input");
 inputElement.attr("type", "text");
 inputElement.attr("name", "newElement");
 formElement.addElement(inputElement);

 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(4, elements.size());
 }

 @Test
 public void testSubmit() throws Exception {
 connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitInvalidActionUrl() {
 formElement = new Element("form", new Attributes().put("action", ""));
 formElement.submit();
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(3, formData.size());

 for (Connection.KeyVal pair : formData) {
 System.out.println(pair.key + "=" + pair.val);
 }
 }

 @Test
 public void testFormDataWithSelectElement() {
 Element selectElement = formElement.select("select").first();
 selectElement.select("option[selected]").remove();
 selectElement.select("option:first").first().attr("selected", "selected");

 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(3, formData.size());

 for (Connection.KeyVal pair : formData) {
 if (pair.key.equals("country")) {
 assertEquals("USA", pair.val);
 }
 }
 }

 @Test
 public void testFormDataWithCheckboxElement() {
 Element checkboxElement = new Element("input");
 checkboxElement.attr("type", "checkbox");
 checkboxElement.attr("name", "agree");
 formElement.addElement(checkboxElement);
 checkboxElement.attr("checked", "checked");

 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(4, formData.size());

 for (Connection.KeyVal pair : formData) {
 if (pair.key.equals("agree")) {
 assertNotNull(pair.val);
 assertEquals("on", pair.val);
 }
 }
 }

 @Test
 public void testFormDataWithRadioElement() {
 Element radioGroupElement = new Element("input");
 radioGroupElement.attr("type", "radio");
 radioGroupElement.attr("name", "gender");
 Element radioMaleElement = new Element("input");
 radioMaleElement.attr("type", "radio");
 radioMaleElement.attr("name", "gender");
 radioMaleElement.attr("value", "male");
 radioMaleElement.attr("id", "male");
 Element radioFemaleElement = new Element("input");
 radioFemaleElement.attr("type", "radio");
 radioFemaleElement.attr("name", "gender");
 radioFemaleElement.attr("value", "female");
 radioFemaleElement.attr("id", "female");
 Element labelMaleElement = new Element("label");
 labelMaleElement.attr("for", "male");
 labelMaleElement.text("Male");
 Element labelFemaleElement = new Element("label");
 labelFemaleElement.attr("for", "female");
 labelFemaleElement.text("Female");
 radioMaleElement.appendText(labelMaleElement);
 radioFemaleElement.appendText(labelFemaleElement);
 formElement.addElement(radioGroupElement);
 formElement.addElement(radioMaleElement);
 formElement.addElement(radioFemaleElement);

 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(3, formData.size());

 for (Connection.KeyVal pair : formData) {
 if (pair.key.equals("gender")) {
 assertNotNull(pair.val);
 assertEquals("male", pair.val);
 }
 }
 }

 @Test
 public void testEquals() {
 FormElement formElement2 = new FormElement(new Tag("form"), "", new Attributes());
 assertFalse(formElement.equals(formElement2));

 FormElement formElement3 = new FormElement(tag(), "", new Attributes().putAll(formElement.attributes()));
 assertTrue(formElement.equals(formElement3));
 }
}