package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 // Given
 Tag tag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 FormElement formElement = new FormElement(tag, baseUri, attributes);

 // Then
 Assert.assertNotNull(formElement);
 Assert.assertNotNull(formElement.elements());
 }

 @Test
 public void testGetFormControls() {
 // Given
 Tag tag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 FormElement formElement = new FormElement(tag, baseUri, attributes);
 Element control1 = new Element("input", new Attributes().put("type", "text").put("name", "control1"));
 Element control2 = new Element("select", new Attributes().put("name", "control2"));
 formElement.addElement(control1);
 formElement.addElement(control2);

 // When
 Elements formControls = formElement.elements();

 // Then
 Assert.assertThat(formControls, is(notNullValue()));
 Assert.assertEquals(2, formControls.size());
 Assert.assertThat(formControls.get(0), is(control1));
 Assert.assertThat(formControls.get(1), is(control2));
 }

 @Test
 public void testSubmitForm() {
 // Given
 Tag formTag = new Tag("form", new Attributes().put("action", "http://example.com"));
 String baseUri = "http://example.com";
 Attributes formAttributes = new Attributes();
 FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "input1").put("value", "test"));
 formElement.addElement(textInput);

 // When
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();

 // Then
 Assert.assertNotNull(connection);
 Assert.assertNotNull(formData);
 Assert.assertEquals(1, formData.size());
 Assert.assertThat(formData.get(0).key, is("input1"));
 Assert.assertThat(formData.get(0).value, is("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithNoAction() {
 // Given
 Tag formTag = new Tag("form", new Attributes());
 String baseUri = "http://example.com";
 Attributes formAttributes = new Attributes();
 FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "input1").put("value", "test"));
 formElement.addElement(textInput);

 // When & Then
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 // Then
 Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 }
 }
}