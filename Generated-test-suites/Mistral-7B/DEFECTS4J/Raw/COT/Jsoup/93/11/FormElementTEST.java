package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import org.junit.Test;

public class FormElementTest {

 private static final String HTML_TEMPLATE = "<html><body>" +
         "<form action=\"test-action\" method=\"post\">" +
         "<input name=\"input1\" type=\"text\" value=\"value1\">" +
         "<input name=\"input2\" type=\"hidden\" value=\"value2\">" +
         "<select name=\"select\">" +
         " <option value=\"option1\" selected>Option 1</option>" +
         " <option value=\"option2\">Option 2</option>" +
         "</select>" +
         "<input name=\"checkbox\" type=\"checkbox\">" +
         "<input name=\"radio\" type=\"radio\" checked>" +
         "<input name=\"disabled\" type=\"text\" disabled>" +
         "</form>" +
         "</body></html>";

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new Parser().parseBodyFragment(HTML_TEMPLATE).getElementsByTag("form").first().asFormElement();
 assertNotNull(formElement);
 }

 @Test
 public void testElements() {
 FormElement formElement = new Parser().parseBodyFragment(HTML_TEMPLATE).getElementsByTag("form").first().asFormElement();
 Elements elements = formElement.elements();
 assertEquals(6, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new Parser().parseBodyFragment(HTML_TEMPLATE).getElementsByTag("form").first().asFormElement();
 Element newElement = new Element("input", new Attributes().attr("type", "submit"));
 formElement.addElement(newElement);
 Element addedElement = formElement.elements().get(6);
 assertEquals(newElement.tagName(), addedElement.tagName());
 }

 @Test
 public void testSubmitWithValidAction() {
 FormElement formElement = new Parser().parseBodyFragment(HTML_TEMPLATE).getElementsByTag("form").first().asFormElement();
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidAction() {
 FormElement formElement = new Parser().parseBodyFragment("<form name=\"form\"></form>").getElementsByTag("form").first().asFormElement();
 assertThrows(IllegalArgumentException.class, formElement::submit);
 }

 @Test
 public void testFormDataBasic() {
 FormElement formElement = new Parser().parseBodyFragment(HTML_TEMPLATE).getElementsByTag("form").first().asFormElement();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(6, formData.size());
 assertEquals("input1", formData.get(0).key);
 assertEquals("value1", formData.get(0).val);
 assertEquals("select", formData.get(1).key);
 assertEquals("option1", formData.get(1).val);
 assertNull(formData.get(2).val);
 assertFalse(formData.get(3).val.isEmpty());
 assertEquals("checkbox", formData.get(4).key);
 assertEquals("", formData.get(4).val);
 assertTrue(formData.get(5).val.equalsIgnoreCase("on"));
 }

 @Test
 public void testFormDataHiddenInput() {
 FormElement formElement = new Parser().parseBodyFragment(HTML_TEMPLATE).getElementsByTag("form").first().asFormElement();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(7, formData.size());
 assertEquals("input2", formData.get(6).key);
 assertEquals("value2", formData.get(6).val);
 }

 @Test
 public void testFormDataSelectInput() {
 FormElement formElement = new Parser().parseBodyFragment(HTML_TEMPLATE).getElementsByTag("form").first().asFormElement();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(7, formData.size());
 assertEquals("select", formData.get(1).key);
 assertEquals("option1", formData.get(1).val);
 }

 @Test
 public void testFormDataCheckboxInput() {
 FormElement formElement = new Parser().parseBodyFragment(HTML_TEMPLATE).getElementsByTag("form").first().asFormElement();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(7, formData.size());
 assertFalse(formData.get(4).val.isEmpty());
 }

 @Test
 public void testFormDataDisabledInput() {
 FormElement formElement = new Parser().parseBodyFragment(HTML_TEMPLATE).getElementsByTag("form").first().asFormElement();
 formElement.getElements().get(5).remove();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());
 assertNull(formData.get(4).key);
 assertNull(formData.get(4).val);
 }
}