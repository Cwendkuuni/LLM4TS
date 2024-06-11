import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setup() {
 String html = "<html><body>" +
 " <form action=\"https://example.com\" method=\"get\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"submit\" value=\"Submit\" />" +
 " </form>" +
 "</body></html>";
 formElement = Jsoup.parse(html).body().selectFirst("form");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 Elements expectedElements = new Elements().add(new Element("input", new Attributes()));
 assertEquals(expectedElements, formElement.elements());
 }

 @Test
 public void testAddElement() {
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "password"));
 formElement.addElement(inputElement);
 assertEquals(new Elements().add(formElement).add(inputElement), formElement.elements());
 }

 @Test
 public void testSubmitWithEmptyActionUrl() {
 assertThrows(IllegalArgumentException.class, () -> formElement.submit());
 }

 @Test
 public void testSubmitWithValidActionUrl() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataWithTextInput() {
 List<Connection.KeyVal> expectedFormData = new ArrayList<Connection.KeyVal>() {{
 add(HttpConnection.KeyVal.create("username", ""));
 }};
 assertEquals(expectedFormData, formElement.formData());
 }

 @Test
 public void testFormDataWithSelectInput() {
 String html = "<html><body>" +
 " <form action=\"https://example.com\" method=\"get\">" +
 " <select name=\"color\">" +
 " <option value=\"red\">Red</option>" +
 " <option value=\"green\" selected>Green</option>" +
 " </select>" +
 " <input type=\"submit\" value=\"Submit\" />" +
 " </form>" +
 "</body></html>";
 Element formElementWithSelect = Jsoup.parse(html).body().selectFirst("form");
 List<Connection.KeyVal> expectedFormData = new ArrayList<Connection.KeyVal>() {{
 add(HttpConnection.KeyVal.create("color", "green"));
 }};
 assertEquals(expectedFormData, formElementWithSelect.formData());
 }

 @Test
 public void testFormDataWithCheckboxInput() {
 String html = "<html><body>" +
 " <form action=\"https://example.com\" method=\"get\">" +
 " <input type=\"checkbox\" name=\"agree\" checked />Agree" +
 " <input type=\"submit\" value=\"Submit\" />" +
 " </form>" +
 "</body></html>";
 Element formElementWithCheckbox = Jsoup.parse(html).body().selectFirst("form");
 List<Connection.KeyVal> expectedFormData = new ArrayList<Connection.KeyVal>() {{
 add(HttpConnection.KeyVal.create("agree", "on"));
 }};
 assertEquals(expectedFormData, formElementWithCheckbox.formData());
 }

 @Test
 public void testFormDataWithMultipleInputs() {
 String html = "<html><body>" +
 " <form action=\"https://example.com\" method=\"get\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <select name=\"color\">" +
 " <option value=\"red\">Red</option>" +
 " <option value=\"green\" selected>Green</option>" +
 " </select>" +
 " <input type=\"checkbox\" name=\"agree\" checked />Agree" +
 " <input type=\"submit\" value=\"Submit\" />" +
 " </form>" +
 "</body></html>";
 Element formElementWithMultipleInputs = Jsoup.parse(html).body().selectFirst("form");List<Connection.KeyVal> expectedFormData = new ArrayList<Connection.KeyVal>() {{
 add(HttpConnection.KeyVal.create("username", ""));
 add(HttpConnection.KeyVal.create("color", "green"));
 add(HttpConnection.KeyVal.create("agree", "on"));
 }};
 assertEquals(expectedFormData, formElementWithMultipleInputs.formData());
 }

 @Test
 public void testFormDataWithEmptyForm() {
 String html = "<html><body><form action=\"https://example.com\" method=\"get\"></form></body></html>";
 Element formElementWithEmptyForm = Jsoup.parse(html).body().selectFirst("form");
 List<Connection.KeyVal> expectedFormData = new ArrayList<Connection.KeyVal>();
 assertEquals(expectedFormData, formElementWithEmptyForm.formData());
 }

 @Test
 public void testFormDataWithDisabledInput() {
 String html = "<html><body>" +
 " <form action=\"https://example.com\" method=\"get\">" +
 " <input type=\"text\" name=\"username\" disabled />" +
 " <input type=\"submit\" value=\"Submit\" />" +
 " </form>" +
 "</body></html>";
 Element formElementWithDisabledInput = Jsoup.parse(html).body().selectFirst("form");
 List<Connection.KeyVal> expectedFormData = new ArrayList<Connection.KeyVal>();
 assertEquals(expectedFormData, formElementWithDisabledInput.formData());
 }

 @Test
 public void testFormDataWithNoNameAttribute() {
 String html = "<html><body>" +
 " <form action=\"https://example.com\" method=\"get\">" +
 " <input type=\"text\" />" +
 " <input type=\"submit\" value=\"Submit\" />" +
 " </form>" +
 "</body></html>";
 Element formElementWithNoNameAttribute = Jsoup.parse(html).body().selectFirst("form");
 assertFalse(formElementWithNoNameAttribute.formData().isEmpty());
 }

 @Test
 public void testFormDataWithMultipleInputsWithSameName() {
 String html = "<html><body>" +
 " <form action=\"https://example.com\" method=\"get\">" +
 " <input type=\"text\" name=\"username\" />" +
 " <input type=\"text\" name=\"username\" value=\"John\" />" +
 " <input type=\"submit\" value=\"Submit\" />" +
 " </form>" +
 "</body></html>";
 Element formElementWithMultipleInputsWithSameName = Jsoup.parse(html).body().selectFirst("form");
 List<Connection.KeyVal> expectedFormData = new ArrayList<Connection.KeyVal>() {{
 add(HttpConnection.KeyVal.create("username", ""));
 add(HttpConnection.KeyVal.create("username", "John"));
 }};
 assertEquals(expectedFormData, formElementWithMultipleInputsWithSameName.formData());
 }
}