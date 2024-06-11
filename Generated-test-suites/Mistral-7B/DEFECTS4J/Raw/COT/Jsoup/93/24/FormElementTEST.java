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
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

 private static final String BASE_URL = "http://example.com";
 private static final Tag FORM_TAG = new Tag("form");

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URL, new Attributes());
 Assert.assertEquals("form", formElement.tagName());
 Assert.assertEquals(BASE_URL, formElement.baseUri());
 }

 @Test
 public void testGetFormControls() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URL, new Attributes());
 Elements elements = formElement.elements();

 // Add some form controls
 Element textInput = new Elements(new Tag("input")).first();
 textInput.attributes().put("type", "text");
 textInput.attributes().put("name", "username");
 formElement.addElement(textInput);

 Element passwordInput = new Elements(new Tag("input")).first();
 passwordInput.attributes().put("type", "password");
 passwordInput.attributes().put("name", "password");
 formElement.addElement(passwordInput);

 Element submitButton = new Elements(new Tag("input")).first();
 submitButton.attributes().put("type", "submit");
 formElement.addElement(submitButton);

 // Check that we get the correct number of form controls
 Assert.assertEquals(3, elements.size());
 }

 @Test
 public void testSubmitFormThrowsExceptionWhenNoActionAttribute() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URL, new Attributes());
 try {
 formElement.submit();
 Failure.fail("Expected IllegalArgumentException when submitting form without 'action' attribute.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSubmitForm() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URL.concat("/submit"), new Attributes());
 formElement.addElement(new Elements(new Tag("input")).first());
 formElement.addElement(new Elements(new Tag("input")).first());
 formElement.addElement(new Elements(new Tag("input")).first());
 formElement.addElement(new Elements(new Tag("input")).first());

 Connection connection = formElement.submit();
 // Perform some assertions on the connection
 // ...
 }

 @Test
 public void testFormDataEmptyForm() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URL, new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertTrue(formData.isEmpty());
 }

 @Test
 public void testFormDataSingleTextInput() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URL, new Attributes());
 Element textInput = new Elements(new Tag("input")).first();
 textInput.attributes().put("type", "text");
 textInput.attributes().put("name", "username");
 formElement.addElement(textInput);
 textInput.text("testuser");

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 HttpConnection.KeyVal.create("username", "testuser")
 );
 Assert.assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataMultipleInputs() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URL, new Attributes());
 Element textInput1 = new Elements(new Tag("input")).first();
 textInput1.attributes().put("type", "text");
 textInput1.attributes().put("name", "username");
 formElement.addElement(textInput1);
 textInput1.text("testuser1");

 Element textInput2 = new Elements(new Tag("input")).first();
 textInput2.attributes().put("type", "text");
 textInput2.attributes().put("name", "password");
 formElement.addElement(textInput2);
 textInput2.text("testpassword1");

 Element checkbox = new Elements(new Tag("input")).first();
 checkbox.attributes().put("type", "checkbox");
 checkbox.attributes().put("name", "acceptTerms");
 formElement.addElement(checkbox);
 checkbox.attr("checked", "checked");

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 HttpConnection.KeyVal.create("username", "testuser1"),
 HttpConnection.KeyVal.create("password", "testpassword1"),
 HttpConnection.KeyVal.create("acceptTerms", "on")
 );
 Assert.assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataSelectElement() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URL, new Attributes());
 Element select = new Elements(new Tag("select")).first();
 select.attributes().put("name", "favoriteColor");
 formElement.addElement(select);

 Element option1 = new Elements(new Tag("option")).first();
 option1.text("Red");
 option1.attr("value", "red");
 select.addElement(option1);
 option1.attr("selected", "selected");

 Element option2 = new Elements(new Tag("option")).first();
 option2.text("Blue");
 option2.attr("value", "blue");
 select.addElement(option2);

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 HttpConnection.KeyVal.create("favoriteColor", "red")
 );
 Assert.assertEquals(expectedFormData, formData);
 }

 @Test
 public void testFormDataDisabledInput() {
 FormElement formElement = new FormElement(FORM_TAG, BASE_URL, new Attributes());
 Element textInput = new Elements(new Tag("input")).first();
 textInput.attributes().put("type", "text");
 textInput.attributes().put("name", "username");
 textInput.attributes().put("disabled", "disabled");
 formElement.addElement(textInput);

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertTrue(formData.isEmpty());
 }
}