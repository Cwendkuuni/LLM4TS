package org.jsoup.nodes;

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

public class FormElementTest {

 private Element formElement;
 private Element inputElement;
 private Element selectElement;
 private Element submitElement;

 @Before
 public void setUp() {
 Parser parser = new Parser.MalformedEncodingDetector();
 String html = "<form action=\"https://example.com\"><input name=\"input\" type=\"text\"><select name=\"select\"><option value=\"option1\">Option 1</option><option value=\"option2\" selected><Option 2</option></select><input type=\"submit\" name=\"submit\"></form>";
 formElement = parser.parse(html).selectFirst("form");
 inputElement = formElement.selectFirst("input[name=input]");
 selectElement = formElement.selectFirst("select[name=select]");
 submitElement = formElement.selectFirst("input[type=submit]");
 }

 @Test
 public void testCreateFormElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element element = new Element("input", new Attributes().put("type", "text").put("name", "test"));
 formElement.addElement(element);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitWithoutBaseUri() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 try {
 formElement.submit();
 } catch (IllegalStateException e) {
 throw new IllegalStateException("Should not be able to submit a form without a base URI", e);
 }
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());

 Connection.KeyVal inputKeyVal = formData.get(0);
 assertEquals("input", inputKeyVal.getName());
 assertEquals("", inputKeyVal.getValue());

 Connection.KeyVal selectKeyVal = formData.get(1);
 assertEquals("select", selectKeyVal.getName());
 assertEquals("option2", selectKeyVal.getValue());

 Connection.KeyVal submitKeyVal = formData.get(2);
 assertEquals("submit", submitKeyVal.getName());
 assertEquals("", submitKeyVal.getValue());
 }

 @Test
 public void testFormDataWithInput() {
 inputElement.val("test");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());

 Connection.KeyVal inputKeyVal = formData.get(0);
 assertEquals("input", inputKeyVal.getName());
 assertEquals("test", inputKeyVal.getValue());
 }

 @Test
 public void testFormDataWithSelect() {
 selectElement.select("option[value=option1]").first().attr("selected", "selected");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());

 Connection.KeyVal selectKeyVal = formData.get(1);
 assertEquals("select", selectKeyVal.getName());
 assertEquals("option1", selectKeyVal.getValue());
 }

 @Test
 public void testFormDataWithCheckbox() {
 inputElement = formElement.selectFirst("input[type=checkbox]");
 inputElement.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(4, formData.size());

 Connection.KeyVal inputKeyVal = formData.get(0);
 assertEquals("input", inputKeyVal.getName());
 assertEquals("", inputKeyVal.getValue());

 Connection.KeyVal checkboxKeyVal = formData.get(3);
 assertEquals("input", checkboxKeyVal.getName());
 assertEquals("on", checkboxKeyVal.getValue());
 }
}