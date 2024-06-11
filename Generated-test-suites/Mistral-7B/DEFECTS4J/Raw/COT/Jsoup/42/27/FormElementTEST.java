import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Elements;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setup() {
 Tag formTag = new Tag("form");
 Attributes formAttributes = new Attributes().put("action", "http://test.com/form");
 formElement = new FormElement(formTag, "", formAttributes);
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 Element inputElement = new Element("input", new Attributes().put("name", "username").put("type", "text"));
 expectedElements.add(inputElement);
 formElement.addElement(inputElement);
 List<Element> actualElements = formElement.elements();
 assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testSubmit_ValidForm() {
 Connection connection = formElement.submit();
 String actionUrl = connection.url();
 assertEquals("http://test.com/form", actionUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmit_EmptyActionUrl() {
 formElement = new FormElement(new Tag("form"), "", new Attributes().put("", ""));
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmit_NullFormData() {
 formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "http://test.com/form"));
 formElement.submit();
 }

 @Test
 public void testFormData_SelectElement() {
 formElement.addElement(new Element("select", new Attributes().put("name", "colors")))
 .addElement(new Element("option", new Attributes().put("value", "red").put("selected", "selected")))
 .addElement(new Element("option", new Attributes().put("value", "green")));

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("colors", "red"));
 expectedFormData.add(HttpConnection.KeyVal.create("colors", "green"));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormData_CheckboxOrRadioElement() {
 formElement.addElement(new Element("input", new Attributes().put("name", "color").put("type", "checkbox").put("checked", "checked")))
 .addElement(new Element("input", new Attributes().put("name", "color").put("type", "radio").put("checked", "checked")));

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("color", "on"));
 expectedFormData.add(HttpConnection.KeyVal.create("color", "on"));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormData_OtherElement() {
 formElement.addElement(new Element("input", new Attributes().put("name", "username").put("type", "text")))
 .addElement(new Element("textarea", new Attributes().put("name", "message")))
 .addElement(new Element("input", new Attributes().put("name", "submit").put("type", "submit")));

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("username", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("message", ""));
 expectedFormData.add(HttpConnection.KeyVal.create("submit", ""));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), "", new Attributes().put("action", "http://test.com/form1"));
 FormElement formElement2 = new FormElement(new Tag("form"), "", new Attributes().put("action", "http://test.com/form2"));
 Assert.assertNotEquals(formElement1, formElement2);

 FormElement formElement3 = new FormElement(formElement.tag(), formElement.baseUri(), formElement.attributes());
 Assert.assertEquals(formElement, formElement3);
 }
}