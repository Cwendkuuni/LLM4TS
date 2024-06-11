package org.jsoup.nodes.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.jsoup.helper.Utils.newDocument;
import static org.jsoup.parser.ParseSetting.*;

@RunWith(JUnit4.class)
public class FormElementTest {

 private Parser parser;

 @Before
 public void setup() {
 parser = new Parser.Builder().addSettings(formParsing()).build();
 }

 /**
 * Test constructor FormElement
 */
 @Test
 public void testFormElementConstructor() {
 // given
 String html = "<form><input type='text' name='name'></form>";
 Tag tag = parser.parseInput(html, "").selectFirst("form");

 // when
 FormElement formElement = new FormElement(tag, "http://localhost", new Attributes());

 // then
 assertNotNull(formElement);
 assertEquals("FORM", formElement.tagName());
 assertNotNull(formElement.elements());
 }

 /**
 * Test method elements
 */
 @Test
 public void testElements() {
 // given
 String html = "<form><input type='text' name='name'><input type='text' name='lastName'></form>";
 Tag tag = parser.parseInput(html, "").selectFirst("form");
 FormElement formElement = new FormElement(tag, "http://localhost", new Attributes());

 // when
 Elements elements = formElement.elements();

 // then
 assertEquals(2, elements.size());
 }

 /**
 * Test method addElement
 */
 @Test
 public void testAddElement() {
 // given
 String html = "<form><input type='text' name='name'></form>";
 Tag formTag = parser.parseInput(html, "").selectFirst("form");
 FormElement formElement = new FormElement(formTag, "http://localhost", new Attributes());
 String htmlElement = "<input type='text' name='lastName'>";
 Tag inputTag = parser.parseInput(htmlElement, "").selectFirst("input");

 // when
 FormElement inputElement = new FormElement(inputTag, "http://localhost", new Attributes());
 formElement.addElement(inputElement);

 // then
 assertEquals(2, formElement.elements().size());
 }

 /**
 * Test method removeChild
 */
 @Test
 public void testRemoveChild() {
 // given
 String html = "<form><input type='text' name='name'><input type='text' name='lastName'></form>";
 Tag formTag = parser.parseInput(html, "").selectFirst("form");
 FormElement formElement = new FormElement(formTag, "http://localhost", new Attributes());
 FormElement inputElement = formElement.elements().get(1);
 formElement.removeChild(inputElement);

 // then
 assertEquals(1, formElement.elements().size());
 }

 /**
 * Test method submit
 * Given: a valid form
 */
 @Test
 public void testSubmitValidForm() {
 // given
 String html = "<form action='http://localhost' method='post'><input type='text' name='name' value='test'></form>";
 Tag formTag = parser.parseInput(html, "").selectFirst("form");
 FormElement formElement = new FormElement(formTag, "http://localhost", new Attributes());

 // when
 Connection connection = formElement.submit();

 // then
 assertNotNull(connection);
 assertEquals(HttpConnection.Response.StatusCode.OK, connection.statusCode());
 }

 /**
 * Test method submit
 * Given: an invalid form without action attribute
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithoutActionAttribute() {
 // given
 String html = "<form method='post'><input type='text' name='name' value='test'></form>";
 Tag formTag = parser.parseInput(html, "").selectFirst("form");
 FormElement formElement = new FormElement(formTag, "http://localhost", new Attributes());

 // when
 Connection connection = formElement.submit();
 }

 /**
 * Test method formData
 * Given: a form with one text input
 */
 @Test
 public void testFormDataOneTextInput() {
 // given
 String html = "<form action='http://localhost' method='post'><input type='text' name='name' value='test'></form>";
 Tag formTag = parser.parseInput(html, "").selectFirst("form");
 FormElement formElement = new FormElement(formTag, "http://localhost", new Attributes());
 List<Connection.KeyVal> keyValues = formElement.formData();

 // when
 // then
 assertEquals(1, keyValues.size());
 assertEquals("name", keyValues.get(0).key());
 assertEquals("test", keyValues.get(0).value());
 }

 /**
 * Test method formData
 * Given: a form with one select input
 */
 @Test
 public void testFormDataOneSelectInput() {
 // given
 String html = "<form action='http://localhost' method='post'><select name='name'><option value='test1'>Test 1</option><option value='test2'>Test 2</option></select></form>";
 Tag formTag = parser.parseInput(html, "").selectFirst("form");
 FormElement formElement = new FormElement(formTag, "http://localhost", new Attributes());
 List<Connection.KeyVal> keyValues = formElement.formData();

 // when
 // then
 assertEquals(1, keyValues.size());
 assertEquals("name", keyValues.get(0).key());
 assertEquals("test1", keyValues.get(0).value());
 }

 /**
 * Test method formData
 * Given: a form with one checkbox input
 */
 @Test
 public void testFormDataOneCheckboxInput() {
 // given
 String html = "<form action='http://localhost' method='post'><input type='checkbox' name='name' value='test'></form>";
 Tag formTag = parser.parseInput(html, "").selectFirst("form");
 FormElement formElement = new FormElement(formTag, "http://localhost", new Attributes());
 formElement.elements().get(0).attr("checked", "checked");
 List<Connection.KeyVal> keyValues = formElement.formData();

 // when
 // then
 assertTrue(keyValues.contains(HttpConnection.KeyVal.create("name", "test")));
 }

 /**
 * Test method formData
 * Given: a form with multiple inputs
 */
 @Test
 public void testFormDataMultipleInputs() {
 // given
 String html = "<form action='http://localhost' method='post'><input type='text' name='name' value='test1'><input type='text' name='lastName' value='test2'><input type='checkbox' name='agree' value='agree'></form>";
 Tag formTag = parser.parseInput(html, "").selectFirst("form");
 FormElement formElement = new FormElement(formTag, "http://localhost", new Attributes());
 List<Connection.KeyVal> keyValues = formElement.formData();

 // when
 // then
 assertEquals(3, keyValues.size());
 assertTrue(keyValues.contains(HttpConnection.KeyVal.create("name", "test1")));
 assertTrue(keyValues.contains(HttpConnection.KeyVal.create("lastName", "test2")));
 assertTrue(keyValues.contains(HttpConnection.KeyVal.create("agree", "agree")));
 }
}