package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 String html = "<html><body><form action=\"http://example.com\"><input type=\"text\" name=\"username\"><input type=\"submit\"></form></body></html>";
 formElement = Jsoup.parse(html).body().select("form").first().forms().first();
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 expectedElements.add(formElement.select("input").first());
 expectedElements.add(formElement.select("textarea").first());
 expectedElements.add(formElement.select("select").first());

 assertEquals("Size should match", 3, formElement.elements().size());
 Elements actualElements = formElement.elements();
 assertTrue("Lists should be equal", actualElements.equals(expectedElements));
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("input", new Attributes().put("type", "hidden").put("name", "newField"));
 formElement.addElement(newElement);

 Elements actualElements = formElement.elements();
 Element expectedElement = formElement.select("input[name=newField]").first();

 assertTrue("New element should be present", actualElements.size() == 4);
 assertTrue("New element should match expected", actualElements.get(3).equals(expectedElement));
 }

 @Test
 public void testRemoveChild() {
 Element childToRemove = formElement.select("input").first();
 formElement.removeChild(childToRemove);

 Elements actualElements = formElement.elements();
 assertFalse("Child element should no longer be present", actualElements.contains(childToRemove));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitNullBaseUri() {
 FormElement formElementNullBaseUri = new FormElement(new Tag("form"), null, new Attributes());
 formElementNullBaseUri.submit();
 }

 @Test
 public void testSubmitValidForm() {
 Connection connection = formElement.submit();
 String url = connection.url();
 String expectedUrl = "http://example.com";

 assertEquals("Submitted URL should match expected", expectedUrl, url);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitEmptyAction() {
 FormElement formElementEmptyAction = new FormElement(new Tag("form"), "", new Attributes().put("action", ""));
 formElementEmptyAction.submit();
 }

 @Test
 public void testFormDataValidForm() {
 List<Connection.KeyVal> formData = formElement.formData();
 String username = "testUser";

 // Set user name input value
 formElement.select("input[name=username]").first().val(username);

 String[] expectedFormData = new String[]{"username=testUser"};
 for (Connection.KeyVal data : formData) {
 String keyValStr = data.key + "=" + data.val;
 assertArrayEquals("Form data should match expected", expectedFormData, new String[]{keyValStr});
 }
 }

 @Test
 public void testFormDataEmptyForm() {
 List<Connection.KeyVal> formData = formElement.formData();
 String[] expectedFormData = new String[0];

 assertArrayEquals("Form data should be empty when form is empty", expectedFormData, formData.toArray(expectedFormData));
 }

 @Test
 public void testFormDataDisabledFormControl() {
 formElement.select("input[name=username]").first().attr("disabled", "disabled");

 List<Connection.KeyVal> formData = formElement.formData();
 String[] expectedFormData = new String[0];

 assertArrayEquals("Form data should be empty when form control is disabled", expectedFormData, formData.toArray(expectedFormData));
 }
}