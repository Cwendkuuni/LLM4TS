import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FormElementTest {

 private Element formElement;
 private Element inputElement;
 private Element selectElement;
 private Element checkboxElement;

 @Before
 public void setUp() {
 Parser parser = new Parser.Malformed();
 Tag formTag = parser.parse("<form action='' method='post'><input name='input' type='text' /><select name='select'><option value='option1'>Option 1</option><option value='option2' selected='selected'>Option 2</option></select><input type='checkbox' name='checkbox' /></form>").tags();
 Tag inputTag = parser.parse("<input type='text' name='input' /><input type='hidden' name='hidden' value='hiddenValue' />").tags().get(0);
 Tag selectTag = parser.parse("<select name='select'><option value='option1'>Option 1</option><option value='option2' selected='selected'>Option 2</option></select>").tags().get(0);
 Tag checkboxTag = parser.parse("<input type='checkbox' name='checkbox' />").tags().get(0);

 formElement = new FormElement(formTag, "", new Attributes());
 inputElement = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 inputElement.attributes(new Attributes().put("name", "input").put("type", "text"));
 formElement.addElement(inputElement);

 selectElement = new Element("select") {
 @Override
 public String tagName() {
 return "select";
 }
 };
 formElement.addElement(selectElement);

 selectElement.addElement(new Element("option") {
 @Override
 public String tagName() {
 return "option";
 }
 }.attributes(new Attributes().put("value", "option1")));
 selectElement.addElement(new Element("option") {
 @Override
 public String tagName() {
 return "option";
 }
 }.attributes(new Attributes().put("value", "option2").put("selected", "selected")));

 checkboxElement = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 }.attributes(new Attributes().put("type", "checkbox").put("name", "checkbox"));
 formElement.addElement(checkboxElement);
 }

 @Test
 public void testElements() {
 List<Element> elements = formElement.elements();
 assertEquals("size of elements returned from FormElement should match number of elements in the form", 4, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element elementToAdd = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 }.attributes(new Attributes().put("name", "input").put("type", "text"));
 formElement.addElement(elementToAdd);
 assertEquals("size of elements returned from FormElement after adding an element should be one more than previous size", 1, formElement.elements().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 try {
 formElement.submit();
 fail("Expected an IllegalArgumentException to be thrown when trying to submit a form with no action URL");
 } catch (IllegalArgumentException e) {
 // passing
 }
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertEquals("base URI of connection should match base URI of form", formElement.baseUri(), connection.baseUri());
 assertEquals("method of connection should match method of form", Connection.Method.POST, connection.method());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals("size of form data list returned from FormElement should match number of form controls in the form", 3, formData.size());
 assertEquals("input name and value in form data should match input element in form", "input", formData.get(0).key());
 assertEquals("input value in form data should match input element value", "", formData.get(0).value());
 assertEquals("select name and value in form data should match select element and selected option", "select", formData.get(1).key());
 assertEquals("select value in form data should match selected option value", "option2", formData.get(1).value());
 assertEquals("checkbox name and value in form data should match checkbox element", "checkbox", formData.get(2).key());
 assertEquals("checkbox value in form data should be 'on' if checkbox is not checked, otherwise empty string", "", formData.get(2).value());
 assertTrue("checkbox value in form data should be empty string if checkbox is checked", formElement.elements().get(2).hasAttr("checked"));
 assertEquals("", formData.get(2).value());
 }
}