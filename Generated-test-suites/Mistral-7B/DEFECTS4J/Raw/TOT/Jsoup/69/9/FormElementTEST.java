package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup. Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private FormElement form;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 " <form action=\"https://example.com\" method=\"post\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"password\" name=\"password\" />" +
 " <select name=\"select\">" +
 " <option value=\"option1\">Option 1</option>" +
 " <option value=\"option2\">Option 2</option>" +
 " <option selected value=\"option3\">Option 3</option>" +
 " </select>" +
 " <input type=\"submit\" />" +
 " </form>" +
 "</body></html>";
 form = Jsoup.parse(html).body().select("form").first().as(FormElement.class);
 }

 @Test
 public void testElements() {
 Elements expectedControls = form.select("input, select");
 assertEquals(expectedControls.size(), form.elements().size());
 assertEquals(expectedControls, form.elements());
 }

 @Test
 public void testAddElement() {
 Element input = Jsoup.parse("<input type='text' name='textField' />").body().first();
 FormElement formWithInput = form.addElement(input);
 Elements expectedControls = formWithInput.elements();
 assertEquals(expectedControls.size(), 2);
 assertEquals(expectedControls.get(0), form.get(0));
 assertEquals(expectedControls.get(1), input);
 }

 @Test
 public void testSubmitWithValidFormData() {
 Connection connection = form.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(Connection.KeyVal.create("username", ""));
 expectedFormData.add(Connection.KeyVal.create("password", ""));
 expectedFormData.add(Connection.KeyVal.create("select", "option3"));
 assertEquals(expectedFormData, formData);
 }

 @Test
 public void testSubmitWithEmptyFormData() {
 FormElement emptyForm = new FormElement(Tag.FORM, "", new Attributes());
 Connection connection = emptyForm.submit();
 List<Connection.KeyVal> formData = connection.requestBody();
 assertNull(formData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidFormData() {
 FormElement formWithoutAction = new FormElement(Tag.FORM, "", new Attributes());
 formWithoutAction.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidMethod() {
 FormElement formWithInvalidMethod = new FormElement(Tag.FORM, "", new Attributes(){{set("method", "invalid");}});
 formWithInvalidMethod.submit();
 }
}