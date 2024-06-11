import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.FormElement;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

 private static final String BASE_URI = "http://example.com";
 private static final String FORM_HTML = "<html><body><form action='/' method='post'><input name='input1' type='text' value='value1'><input type='submit' name='submit'></form></body></html>";

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), BASE_URI, new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = parseFormElement(FORMDATA_HTML);
 Elements elements = formElement.elements();
 assertEquals(2, elements.size());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), BASE_URI, new Attributes());
 Element inputElement = new Element("input", new Attributes().put("name", "input1").put("type", "text").put("value", "value1"));
 formElement.addElement(inputElement);
 Equals(inputElement, formElement.getFirstChild());
 }

 @Test
 public void testRemoveElement() {
 FormElement formElement = parseFormDataWithChild("input1", "value1", null);
 FormElement child = formElement.getFirstChild();
 formElement.removeChild(child);
 Assert.assertNull(formElement.getFirstChild());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), BASE_URI, new Attributes().put("name", "form1"));
 try {
 formElement.submit();
 fail("Expected an IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoName() {
 FormElement formElement = new Element("form", new Attributes().put("action", "/"));
 try {
 formElement.submit();
 fail("Expected an IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testSubmitWithEmptyFormData() {
 FormElement formElement = parseFormElement(FORMDATA_HTML);
 List<Connection.KeyVal> formData = formElement.formData();
 assertTrue(formData.isEmpty());
 }

 @Test
 public void testSubmitWithSingleInputText() {
 FormElement formElement = parseFormElement(FORMDATA_HTML);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("input1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 }

 @Test
 public void testSubmitWithSingleInputHidden() {
 String FORM_HTML_HIDDEN = "<html><body><form action='/' method='post'><input name='input1' type='hidden' value='value1'><input type='submit' name='submit'></form></body></html>";
 FormElement formElement = parseFormElement(FORM_HTML_HIDDEN);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("input1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 }

 @Test
 public void testSubmitWithSingleSelect() {
 String FORM_HTML_SELECT = "<html><body><form action='/' method='post'><select name='input1'><option value='option1'>Option 1</option><option value='option2' selected>Option 2</option></select><input type='submit' name='submit'></form></body></html>";
 FormElement formElement = parseFormElement(FORM_HTML_SELECT);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("input1", formData.get(0).key);
 assertEquals("option2", formData.get(0).value);
 }

 @Test
 public void testSubmitWithMultipleInputs() {
 String FORM_HTML_MULTIPLE = "<html><body><form action='/' method='post'><input name='input1' type='text' value='value1'><input name='input2' type='text' value='value2'><input type='submit' name='submit'></form></body></html>";
 FormElement formElement = parseFormElement(FORM_HTML_MULTIPLE);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("input1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 assertEquals("input2", formData.get(1).key);
 assertEquals("value2", formData.get(1).value);
 }

 @Test
 public void testSubmitWithMultipleInputsAndHidden() {
 String FORM_HTML_MULTIPLE_HIDDEN = "<html><body><form action='/' method='post'><input name='input1' type='text' value='value1'><input name='input2' type='text' value='value2'><input name='input3' type='hidden' value='value3'><input type='submit' name='submit'></form></body></html>";
 FormElement formElement = parseFormElement(FORM_HTML_MULTIPLE_HIDDEN);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 assertEquals("input1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 assertEquals("input2", formData.get(1).key);
 assertEquals("value2", formData.get(1).value);
 assertEquals("input3", formData.get(2).key);
 assertEquals("value3", formData.get(2).value);
 }

 @Test
 public void testSubmitWithDisabledInput() {
 String FORM_HTML_DISABLED = "<html><body><form action='/' method='post'><input name='input1' type='text' value='value1' disabled><input type='submit' name='submit'></form></body></html>";
 FormElement formElement = parseFormElement(FORM_HTML_DISABLED);
 List<Connection.KeyVal> formData = formElement.formData();
 assertTrue(formData.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitWithNullFormData() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), BASE_URI, new Attributes());
 try {
 formElement.submit(null);
 fail("Expected an IllegalStateException.");
 } catch (IllegalStateException e) {
 // pass
 }
 }

 private static FormElement parseFormElement(String html) {
 return Jsoup.parseBodyFragments(html).first().selectFirst("form");
 }

 private static FormElement parseFormDataWithChild(String... params) {
 String inputTag = "<input %s %s>";
 String name = params[0];
 String value = (params.length > 1) ? params[1] : "";
 String attr = String.format(inputTag, "name='%s'", name, value.isEmpty() ? "" : " value='%s'", value);
 return new FormElement(new Tag("form", new Attributes()), BASE_URI, new Attributes()) {
 @Override
 protected void addChild(Node node) {
 super.addChild(node);
 elements.add(node);
 }
 }.addChild(new Element("input", String.format(inputTag, "type='%s'", "text")));
 }

}