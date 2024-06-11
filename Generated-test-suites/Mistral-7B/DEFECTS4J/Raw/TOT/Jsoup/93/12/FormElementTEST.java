import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testFormElementElements() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 formElement.addElement(textInput);
 Elements formControls = formElement.elements();
 assertFalse(formControls.isEmpty());
 assertTrue(formControls.contains(textInput));
 }

 @Test
 public void testFormElementRemoveChild() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 formElement.addElement(textInput);
 formElement.removeChild(textInput);
 assertFalse(formElement.elements().isEmpty());
 assertFalse(formElement.elements().contains(textInput));
 }

 @Test
 public void testFormElementSubmit() throws Exception {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "/submit"));
 Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 Element passwordInput = new Element("input", new Attributes().put("type", "password").put("name", "password"));
 formElement.addElement(textInput);
 formElement.addElement(passwordInput);
 Connection connection = formElement.submit();
 assertNotNull(connection);
 List<Connection.KeyVal> formData = formElement.formData();
 List<String> expectedFormData = new ArrayList<>(Arrays.asList(
 "username=", "password="
 ));
 assertEquals(expectedFormData.size(), formData.size());
 for (Connection.KeyVal keyVal : formData) {
 assertEquals(keyVal.key, expectedFormData.get(formData.indexOf(keyVal)));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitNoAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitEmptyAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", ""));
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitNullAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", null));
 formElement.submit();
 }
}