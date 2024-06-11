import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parparser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setUp() {
 String html = "<html><body><form action=\"http://example.com\"><input name=\"name\" type=\"text\"/><select name=\"select\"><option value=\"option1\">Option 1</option><option value=\"option2\" selected>Option 2</option></select><input type=\"checkbox\" name=\"checkbox\"/><input type=\"radio\" name=\"radio\" checked/></form></body></html>";
 Document document = Jsoup.parse(html);
 formElement = document.body().selectFirst("form");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 Elements expectedElements = new Elements();
 expectedElements.add(new Element("input", new Attributes().put("name", "name").put("type", "text")));
 expectedElements.add(new Element("select", new Attributes().put("name", "select")));
 expectedElements.add(new Element("option", new Attributes().put("value", "option1")));
 expectedElements.add(new Element("option", new Attributes().put("value", "option2").put("selected", "selected")));
 expectedElements.add(new Element("input", new Attributes().put("type", "checkbox").put("name", "checkbox")));
 expectedElements.add(new Element("input", new Attributes().put("type", "radio").put("name", "radio").put("checked", "checked")));

 Elements actualElements = formElement.elements();
 assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("input", new Attributes().put("name", "newName").put("type", "text"));
 formElement.addElement(newElement);

 Elements expectedElements = new Elements();
 expectedElements.addAll(formElement.elements());
 expectedElements.add(newElement);

 Elements actualElements = formElement.elements();
 assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutBaseUri() {
 FormElement formElementWithoutBaseUri = new FormElement(new Tag("form"), null, new Attributes());
 formElementWithoutBaseUri.submit();
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("name", ""));
 expectedData.add(new Connection.KeyVal("select", "option2"));
 expectedData.add(new Connection.KeyVal("checkbox", ""));
 expectedData.add(new Connection.KeyVal("radio", ""));

 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataForSelect() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("name", "option2"));

 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataForCheckboxOrRadio() {
 FormElement formElementWithCheckboxOrRadio = new Elements(Parparser.parse("<html><body><form><input type=\"checkbox\" name=\"name\"/></form></body></html>").first().select("form")).get(0);
 List<Connection.KeyVal> actualData = formElementWithCheckboxOrRadio.formData();
 assertNull(actualData);
 }

 @Test
 public void testFormDataForMultipleCheckboxOrRadio() {
 FormElement formElementWithMultipleCheckboxOrRadio = new Elements(Parparser.parse("<html><body><form><input type=\"checkbox\" name=\"name\" checked/><input type=\"checkbox\" name=\"name\"/></form></body></html>").first().select("form")).get(0);
 List<Connection.KeyVal> actualData = formElementWithMultipleCheckboxOrRadio.formData();
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("name", ""));
 expectedData.add(new Connection.KeyVal("name", ""));
 assertEquals(expectedData, actualData);
 }
}