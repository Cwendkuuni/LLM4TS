import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private Document doc;

 @Before
 public void setup() {
 doc = Jsoup.parse("<html><body>" +
 "<form action='/submit' method='post'>" +
 "<input type='text' name='textField' value='initialValue'>" +
 "<select name='selectField'>" +
 "<option value='option1'>Option 1</option>" +
 "<option selected value='option2'>Option 2</option>" +
 "</select>" +
 "<input type='checkbox' name='checkBox' value='checkValue'>" +
 "<input type='hidden' name='hiddenField' value='hiddenValue'>" +
 "</form>" +
 "</body></html>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement formElement = doc.body().select("form").first();
 assertNotNull(formElement);
 }

 @Test
 public void testGetFormControls() {
 FormElement formElement = doc.body().select("form").first();
 Elements formControls = formElement.elements();
 assertEquals(4, formControls.size());
 }

 @Test
 public void testAddFormControl() {
 FormElement formElement = doc.body().select("form").first();
 Element textField = new Element("input");
 textField.tagName("input");
 textField.attr("type", "text");
 textField.attr("name", "newTextField");
 textField.attr("value", "newValue");
 formElement.addElement(textField);
 Elements formControls = formElement.elements();
 assertEquals(5, formControls.size());
 }

 @Test
 public void testSubmitFormWithValidData() {
 FormElement formElement = doc.body().select("form").first();
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());
 assertEquals("initialValue", formData.get(0).key);
 assertEquals("initialValue", formData.get(0).val);
 assertEquals("option2", formData.get(1).key);
 assertEquals("Option 2", formData.get(1).val);
 assertNull(formData.get(2).key);
 assertNull(formData.get(2).val);
 assertEquals("checkValue", formData.get(3).key);
 assertEquals("checkValue", formData.get(3).val);
 assertEquals("hiddenValue", formData.get(4).key);
 assertEquals("hiddenValue", formData.get(4).val);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitFormWithNullAction() {
 FormElement formElement = new Element("form");
 formElement.attr("name", "formName");
 formElement.removeAttr("action");
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testSubmitFormWithEmptyAction() {
 FormElement formElement = new Element("form");
 formElement.attr("name", "formName");
 formElement.attr("action", "");
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
 assertNotNull(e);
 }
 }
}