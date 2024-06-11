package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private Element formElement;
 private Connection connection;

 @Before
 public void setUp() {
 String html = "<html><head></head><body>" +
 " <form action='/' method='post'>" +
 " <input type='text' name='name' value='John Doe' />" +
 " <input type='hidden' name='age' value='30' />" +
 " <select name='country'>" +
 " <option value='USA'>USA</option>" +
 " <option selected value='Canada'>Canada</option>" +
 " </select>" +
 " <input type='checkbox' name='hobby' value='reading' checked />" +
 " <input type='checkbox' name='hobby' value='swimming' />" +
 " <input type='submit' name='submit' value='Submit' />" +
 " </form>" +
 " </body></html>";
 formElement = Jsoup.parse(html).body().selectFirst("form");
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(5, elements.size());
 }

 @Test
 public void testAddElement() {
 Element input = Jsoup.parse("<input type='text' name='newName' value='Mary' />").body();
 formElement.addElement(input);
 Elements elements = formElement.elements();
 assertEquals(6, elements.size());
 }

 @Test
 public void testSubmitNormalCase() throws Exception {
 List<Connection.KeyVal> formData = formElement.formData();
 Connection connection = formElement.submit();
 assertEquals(6, formData.size());
 assertEquals("John Doe", formData.get(0).getValue());
 assertEquals("name", formData.get(0).getName());
 assertEquals("30", formData.get(1).getValue());
 assertEquals("age", formData.get(1).getName());
 assertEquals("Canada", formData.get(2).getValue());
 assertEquals("country", formData.get(2).getName());
 assertEquals("reading", formData.get(3).getValue());
 assertEquals("hobby", formData.get(3).getName());
 assertEquals("Swimming", formData.get(4).getValue());
 assertEquals("hobby", formData.get(4).getName());
 assertEquals("Mary", formData.get(5).getValue());
 assertEquals("newName", formData.get(5).getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitNoBaseURI() {
 formElement = Jsoup.parse("<form action='/' method='post'></form>").body();
 assertThrows(IllegalArgumentException.class, () -> formElement.submit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitNoAction() {
 formElement = Jsoup.parse("<form></form>").body();
 assertThrows(IllegalArgumentException.class, () -> formElement.submit());
 }

 @Test
 public void testFormDataEmptyForm() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(0, formData.size());
 }
}