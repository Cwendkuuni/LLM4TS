import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSettings;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private Element formElement;
 private Element inputElement;
 private Element selectElement;
 private Element textareaElement;

 @Before
 public void setUp() {
 ParseSettings parseSettings = new ParseSettings.Builder().build();
 String html = "<form action=\"/\"><input name=\"input\" type=\"text\"/><select name=\"select\"><option value=\"option1\">Option 1</option><option value=\"option2\" selected>Option 2</option></select><textarea name=\"textarea\">Textarea</textarea></form>";
 formElement = Jsoup.parse(html, parseSettings).body().selectFirst("form");
 inputElement = formElement.selectFirst("input");
 selectElement = formElement.selectFirst("select");
 textareaElement = formElement.selectFirst("textarea");
 }

 @Test
 public void testFormElementConstructor() {
 Tag tag = formElement.tag();
 assertEquals("form", tag.tagName());
 assertEquals("/", formElement.baseUri());
 Attributes attributes = formElement.attributes();
 assertNotNull(attributes);
 assertFalse(attributes.hasKey("action"));
 }

 @Test
 public void testElementsMethod() {
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 assertTrue(elements.get(0) instanceof FormElement);
 assertTrue(elements.get(1) instanceof Element);
 assertTrue(elements.get(2) instanceof Element);
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElementCopy = new FormElement(new Tag("form"), "", new Attributes());
 Element elementToAdd = new Element("input", new Attributes().put("name", "newInput"));
 formElementCopy.addElement(elementToAdd);
 Elements elements = formElementCopy.elements();
 assertEquals(2, elements.size());
 }

 @Test
 public void testSubmitMethod() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 assertTrue(connection.method() == Connection.Method.GET);
 String action = formElement.attr("action");
 assertEquals("/", action);
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitMethodWithoutActionAttribute() {
 FormElement formElementWithoutAction = new FormElement(new Tag("form"), "", new Attributes());
 formElementWithoutAction.submit();
 }

 @Test
 public void testFormDataMethod() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 Connection.KeyVal inputData = formData.get(0);
 assertEquals("input", inputData.key());
 assertEquals("", inputData.value());
 Connection.KeyVal selectData1 = formData.get(1);
 assertEquals("select", selectData1.key());
 assertEquals("option1", selectData1.value());
 Connection.KeyVal selectData2 = formData.get(2);
 assertEquals("select", selectData2.key());
 assertEquals("option2", selectData2.value());
 }

 @Test
 public void testFormDataMethodWithTextarea() {
 FormElement formElementWithTextarea = new FormElement(new Tag("form"), "", new Attributes().put("action", "/"));
 textareaElement.text("Textarea value");
 List<Connection.KeyVal> formData = formElementWithTextarea.formData();
 assertEquals(4, formData.size());
 Connection.KeyVal textareaData = formData.get(3);
 assertEquals("textarea", textareaData.key());
 assertEquals("Textarea value", textareaData.value());
 }
}