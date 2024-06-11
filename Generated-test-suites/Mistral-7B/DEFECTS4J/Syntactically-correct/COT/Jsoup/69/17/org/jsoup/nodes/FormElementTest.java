import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.Jsoup;

public class FormElementTest {

 private static final String TEST_HTML = "" +
         "<html><head></head><body>" +
         "<form action=\"/\">" +
         "<input name=\"input1\" type=\"text\">" +
         "<select name=\"select1\">" +
         "<option value=\"option1\">Option 1</option>" +
         "<option value=\"option2\" selected>Option 2</option>" +
         "</select>" +
         "<input name=\"input2\" type=\"checkbox\">" +
         "<input name=\"input3\" type=\"radio\" checked>" +
         "<input name=\"input4\" type=\"hidden\" value=\"hiddenValue\">" +
         "</form>" +
         "</body></html>";

 @Test
 public void testConstructFormElement() {
 Document doc = Jsoup.parse(TEST_HTML);
 FormElement formElement = (FormElement) doc.select("form").first();
 Assert.assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 Document doc = Jsoup.parse(TEST_HTML);
 FormElement formElement = (FormElement) doc.select("form").first();
 Elements elements = formElement.elements();
 Assert.assertNotNull(elements);
 Assert.assertEquals(5, elements.size());
 }

 @Test
 public void testAddElement() {
 Document doc = Jsoup.parse("<html><body><form></form></body></html>");
 FormElement formElement = (FormElement) doc.select("form").first();
 Element input = doc.createElement("input");
 input.attr("name", "input1");
 formElement.addElement(input);
 Elements elements = formElement.elements();
 Assert.assertEquals(1, elements.size());
 Assert.assertEquals("input1", elements.first().attr("name"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 // expected
 String message = e.getMessage();
 String expected = "Could not determine a form action URL for submit. Ensure you set a base URI when parsing.";
 Truth.assertThat(message).isEqualTo(expected);
 }
 }

 @Test
 public void testFormDataNormalCase() {
 Document doc = Jsoup.parse(TEST_HTML);
 FormElement formElement = (FormElement) doc.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 // the order of key-val pair may not be same as the test case, so we just check the count and the key-val pair exists or not
 Assert.assertEquals(5, formData.size());
 Truth.assertThat(formData.get(0).name()).isEqualTo("input1");
 Truth.assertThat(formData.get(1).name()).isEqualTo("select1");
 Truth.assertThat(formData.get(2).name()).isEqualTo("input2");
 Truth.assertThat(formData.get(3).name()).isEqualTo("input3");
 Truth.assertThat(formData.get(4).name()).isEqualTo("input4");
 }

 @Test
 public void testFormDataSelectCase() {
 Document doc = Jsoup.parse(TEST_HTML);
 FormElement formElement = (FormElement) doc.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 Truth.assertThat(formData.get(1).name()).isEqualTo("select1");
 Truth.assertThat(formData.get(1).value()).isEqualTo("option2");
 }

 @Test
 public void testFormDataCheckboxCase() {
 Document doc = Jsoup.parse("<html><body><form><input name=\"checkbox1\" type=\"checkbox\"></form></body></html>");
 FormElement formElement = (FormElement) doc.select("form").first();
 formElement.addElement(new Element("input", new Attributes().put("name", "checkbox2").put("type", "checkbox")));
 formElement.addElement(new Element("input", new Attributes().put("name", "checkbox3").put("type", "checkbox")));
 formElement.addElement(new Element("input", new Attributes().put("name", "checkbox4").put("type", "checkbox").put("checked", "checked")));
 List<Connection.KeyVal> formData = formElement.formData();
 Truth.assertThat(formData.size()).isEqualTo(5);
 Truth.assertThat(formData.get(3).name()).isEqualTo("checkbox4");
 Truth.assertThat(formData.get(3).value()).isEqualTo("on");
 }

 @Test
 public void testFormDataRadioCase() {
 Document doc = Jsoup.parse("<html><body><form><input name=\"radio1\" type=\"radio\" checked>\n" +
 "<input name=\"radio2\" type=\"radio\"></form></body></html>");
 FormElement formElement = (FormElement) doc.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 Truth.assertThat(formData.get(1).name()).isEqualTo("radio1");
 Truth.assertThat(formData.get(1).value()).isEqualTo("on");
 }

 @Test
 public void testFormDataHiddenCase() {
 Document doc = Jsoup.parse("<html><body><form><input name=\"hiddenInput\" type=\"hidden\" value=\"hiddenValue\"></form></body></html>");
 FormElement formElement = (FormElement) doc.select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 Truth.assertThat(formData.get(0).name()).isEqualTo("hiddenInput");
 Truth.assertThat(formData.get(0).value()).isEqualTo("hiddenValue");
 }
}