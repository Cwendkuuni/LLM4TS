import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String BASE_URI = "http://example.com";

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Assert.assertEquals("form", formElement.tagName());
 Assert.assertEquals(BASE_URI, formElement.baseUri());
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element element1 = new Element("input");
 Element element2 = new Element("textarea");
 formElement.addElement(element1);
 formElement.addElement(element2);

 List<Element> elements = formElement.elements().stream().map(e -> e).collect(Collectors.toList());

 Assert.assertEquals(2, elements.size());
 Assert.assertTrue(elements.contains(element1));
 Assert.assertTrue(elements.contains(element2));
 }

 @Test
 public void testSubmitEmptyForm() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Connection connection = formElement.submit();
 Assert.assertNotNull(connection);
 }

 @Test
 public void testSubmitFormWithActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form", "action", "/submit"), BASE_URI, new Attributes());
 Connection connection = formElement.submit();
 String action = connection.url();
 Assert.assertEquals("/submit", action);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithoutActionAttribute() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testFormDataEmptyForm() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertTrue(formData.isEmpty());
 }

 @Test
 public void testFormDataOneInput() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element input = new Element("input", new Attributes().put("name", "inputName").put("value", "inputValue"));
 formElement.addElement(input);
 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("inputName", keyVal.key());
 Assert.assertEquals("inputValue", keyVal.value());
 }

 @Test
 public void testFormDataMultipleInputs() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element input1 = new Element("input", new Attributes().put("name", "inputName1").put("value", "inputValue1"));
 Element input2 = new Element("input", new Attributes().put("name", "inputName2").put("value", "inputValue2"));
 formElement.addElement(input1);
 formElement.addElement(input2);
 List<Connection.KeyVal> formData = formElement.formData();

 Connection.KeyVal keyVal1 = formData.get(0);
 Connection.KeyVal keyVal2 = formData.get(1);

 Assert.assertEquals("inputName1", keyVal1.key());
 Assert.assertEquals("inputValue1", keyVal1.value());
 Assert.assertEquals("inputName2", keyVal2.key());
 Assert.assertEquals("inputValue2", keyVal2.value());
 }

 @Test
 public void testFormDataSelect() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element select = new Element("select");
 Element option1 = new Element("option", new Attributes().put("value", "option1").put("selected", "selected"));
 Element option2 = new Element("option", new Attributes().put("value", "option2"));
 select.addElement(option1);
 select.addElement(option2);
 formElement.addElement(select);

 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("select", keyVal.key());
 Assert.assertEquals("option1", keyVal.value());
 }

 @Test
 public void testFormDataCheckboxAndRadio() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element checkbox = new Element("input", new Attributes().put("name", "checkboxName").put("type", "checkbox").put("checked", "checked"));
 formElement.addElement(checkbox);

 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("checkboxName", keyVal.key());
 Assert.assertEquals("on", keyVal.value());

 formElement.removeChild(checkbox);
 checkbox.attr("checked", "false");
 formElement.addElement(checkbox);

 formData = formElement.formData();
 keyVal = formData.get(0);
 Assert.assertEquals("checkboxName", keyVal.key());
 Assert.assertEquals("", keyVal.value());
 }
}