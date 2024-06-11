import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

 private FormElement formElement;
 private Element inputElement;
 private Element textAreaElement;
 private Element selectElement;

 @Before
 public void setup() {
 Parser parser = new Parser.MalformedHtmlParser();
 String html = "<form><input type='text' name='textInput' /><textarea name='textArea'></textarea><select name='selectElement'><option value='option1'>Option 1</option><option value='option2' selected>Option 2</option></select></form>";
 FormElement form = (FormElement) parser.parse(html);
 formElement = form;
 inputElement = form.elements().get(0);
 textAreaElement = form.elements().get(1);
 selectElement = form.elements().get(2);
 }

 @Test
 public void testElements() {
 List<Element> expectedElements = Arrays.asList(inputElement, textAreaElement, selectElement);
 List<Element> actualElements = formElement.elements();
 Assert.assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("input", new Attributes().put("type", "hidden").put("name", "hiddenInput"));
 formElement.addElement(newElement);
 List<Element> actualElements = formElement.elements();
 List<Element> expectedElements = Arrays.asList(inputElement, textAreaElement, selectElement, newElement);
 Assert.assertEquals(expectedElements, actualElements);
 }

 @Test
 public void testSubmitWithCorrectURL() throws Exception {
 Connection connection = formElement.submit();
 String expectedURL = "http://example.com/form-handler.html";
 String actualURL = connection.url();
 Assert.assertEquals(expectedURL, actualURL);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyActionURL() {
 formElement.submit();
 }

 @Test
 public void testFormDataForTextInput() {
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("textInput", ""),
 new Connection.KeyVal(":text", ""));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 Assert.assertEquals(expectedFormData, actualFormData);
 }

 @Test
 public void testFormDataForTextArea() {
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("textArea", ""),
 new Connection.KeyVal(":textarea", ""));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 Assert.assertEquals(expectedFormData, actualFormData);
 }

 @Test
 public void testFormDataForSelectElementWithOneOptionSelected() {
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("selectElement", "option1"));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 Assert.assertEquals(expectedFormData, actualFormData);
 }

 @Test
 public void testFormDataForSelectElementWithMultipleOptionsSelected() {
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("selectElement", "option2"));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 Assert.assertEquals(expectedFormData, actualFormData);
 }

 @Test
 public void testFormDataForCheckboxOrRadioWithChecked() {
 Element checkboxOrRadio = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxOrRadio"));
 checkboxOrRadio.attr("checked", "checked");
 formElement.addElement(checkboxOrRadio);
 List<Connection.KeyVal> expectedFormData = Arrays.asList(
 new Connection.KeyVal("checkboxOrRadio", ""));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 Assert.assertEquals(expectedFormData, actualFormData);
 }

 @Test
 public void testFormDataForCheckboxOrRadioWithoutChecked() {
 Element checkboxOrRadio = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxOrRadio"));
 formElement.addElement(checkboxOrRadio);
 List<Connection.KeyVal> actualFormData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = Arrays.asList();
 Assert.assertEquals(expectedFormData, actualFormData);
 }
}