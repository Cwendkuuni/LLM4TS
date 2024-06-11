package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

 private static final String HTML = "<html>\n" +
         "<body>\n" +
         "<form action=\"http://test.com/\">\n" +
         "<input type=\"text\" name=\"username\" value=\"John\">\n" +
         "<select name=\"gender\">\n" +
         "<option value=\"Male\" selected>\nMale\n</option>\n" +
         "<option value=\"Female\">Female\n</option>\n" +
         "</select>\n" +
         "<input type=\"submit\">\n" +
         "</form>\n" +
         "</body>\n" +
         "</html>";

 private Element formElement;
 private Connection connection;
 private Parser parser;

 @Before
 public void setUp() {
 parser = new Parser(ParseSetting.classicParser());
 connection = mock(Connection.class);
 formElement = parser.parse(HTML).select("form").first();
 }

 @Test
 public void testConstructor() {
 Tag tag = formElement.tag();
 Attributes attributes = formElement.attributes();

 FormElement formElement = new FormElement(tag, "baseUri", attributes);

 assertEquals(tag, formElement.tag());
 assertEquals("baseUri", formElement.baseUri());
 assertSame(attributes, formElement.attributes());
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();

 assertEquals(2, elements.size());

 Element input = elements.get(0);
 assertEquals("input", input.tag().name());
 assertFalse(input.hasAttr("disabled"));

 Element select = elements.get(1);
 assertEquals("select", select.tag().name());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(Tag.FORM, "baseUri", Attributes.EMPTY_ATTRIBUTES);
 Element input = parser.parse("<input type=\"text\" name=\"username\" value=\"John\">").first();

 formElement.addElement(input);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 assertSame(input, elements.get(0));
 }

 @Test
 public void testRemoveChild() {
 FormElement formElement = new FormElement(Tag.FORM, "baseUri", Attributes.EMPTY_ATTRIBUTES);
 Element input = parser.parse("<input type=\"text\" name=\"username\" value=\"John\">").first();
 formElement.addElement(input);
 Element select = parser.parse("<select name=\"gender\">\n<option value=\"Male\">Male\n</option>\n<option value=\"Female\">Female\n</option>\n</select>").first();
 formElement.addElement(select);
 assertEquals(2, formElement.elements().size());
 formElement.removeChild(input);
 assertEquals(1, formElement.elements().size());
 assertFalse(formElement.elements().contains(input));
 }

 @Test
 public void testSubmit() {
 when(connection.data(anyList())).thenReturn(connection);

 FormElement formElement = new FormElement(Tag.FORM, "baseUri", Attributes.EMPTY_ATTRIBUTES);
 formElement.addElement(parser.parse("<input type=\"text\" name=\"username\" value=\"John\">").first());
 formElement.addElement(parser.parse("<input type=\"text\" name=\"password\" value=\"Password\">").first());
 formElement.addElement(parser.parse("<select name=\"gender\">\n<option value=\"Male\">Male\n</option>\n<option value=\"Female\">Female\n</option>\n</select>").first());
 formElement.addElement(parser.parse("<input type=\"submit\">").first());

 Connection result = formElement.submit();
 verify(connection).data(argThat(list -> checkFormData(list)));
 assertEquals(connection, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyBaseUri() {
 FormElement formElement = new FormElement(Tag.FORM, "", Attributes.EMPTY_ATTRIBUTES);
 formElement.submit();
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());

 assertEquals("username", formData.get(0).key());
 assertEquals("John", formData.get(0).value());

 assertEquals("password", formData.get(1).key());
 assertNull(formData.get(1).value());

 assertEquals("gender", formData.get(2).key());
 assertEquals("Male", formData.get(2).value());
 }

 @Test
 public void testFormDataWithDisabledInput() {
 FormElement formElement = new FormElement(Tag.FORM, "baseUri", Attributes.EMPTY_ATTRIBUTES);
 formElement.addElement(parser.parse("<input type=\"text\" name=\"username\" value=\"John\" disabled>").first());
 formElement.addElement(parser.parse("<input type=\"text\" name=\"password\" value=\"Password\">").first());
 formElement.addElement(parser.parse("<select name=\"gender\">\n<option value=\"Male\">Male\n</option>\n<option value=\"Female\">Female\n</option>\n</select>").first());
 formElement.addElement(parser.parse("<input type=\"submit\">").first());

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertFalse(formData.get(0).key().equals("username"));
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = new FormElement(Tag.FORM, "baseUri", Attributes.EMPTY_ATTRIBUTES);
 formElement.addElement(parser.parse("<input type=\"text\" name=\"username\" value=\"John\">").first());
 formElement.addElement(parser.parse("<select name=\"gender\">\n<option value=\"Male\">Male\n</option>\n<option value=\"Female\">Female\n</option>\n</select>").first());
 formElement.addElement(parser.parse("<input type=\"submit\">").first());

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("Male", formData.get(2).value());
 }

 @Test
 public void testFormDataWithMultipleSelectedOptions() {
 FormElement formElement = new FormElement(Tag.FORM, "baseUri", Attributes.EMPTY_ATTRIBUTES);
 formElement.addElement(parser.parse("<input type=\"text\" name=\"username\" value=\"John\">").first());
 formElement.addElement(parser.parse("<select name=\"gender\">\n<option value=\"Male\" selected>\nMale\n</option>\n<option value=\"Female\" selected>\nFemale\n</option>\n</select>").first());
 formElement.addElement(parser.parse("<input type=\"submit\">").first());

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("Female", formData.get(2).value());
 }

 @Test
 public void testFormDataWithCheckedCheckboxOrRadio() {
 FormElement formElement = new FormElement(Tag.FORM, "baseUri", Attributes.EMPTY_ATTRIBUTES);
 formElement.addElement(parser.parse("<input type=\"text\" name=\"username\" value=\"John\">").first());
 formElement.addElement(parser.parse("<input type=\"checkbox\" name=\"agree\" value=\"true\" checked>").first());
 formElement.addElement(parser.parse("<input type=\"submit\">").first());

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("true", formData.get(2).value());
 }

 private boolean checkFormData(List<Connection.KeyVal> formData) {
 for (Connection.KeyVal keyVal : formData) {
 if (keyVal.key() == null || keyVal.key().isEmpty() || keyVal.value() == null) {
 return false;
 }
 }
 return true;
 }

}