import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String BASE_URI = "http://test.com";
 private static final String FORM_HTML = "<form action='/' method='post'>\n" +
         "  <input type='text' name='username'>\n" +
         "  <select name='country'>\n" +
         "    <option value='US'>United States</option>\n" +
         "    <option value='CA' selected>Canada</option>\n" +
         "  </select>\n" +
         "  <input type='checkbox' name='agree' checked>\n" +
         "  <input type='submit'>\n" +
         "</form>";

 @Test
 public void testFormElementCreation() {
  Tag formTag = new Tag("form");
  Attributes formAttrs = new Attributes();
  formAttrs.put("action", "");
  formAttrs.put("method", "post");
  FormElement formElement = new FormElement(formTag, BASE_URI, formAttrs);
  Assert.assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
  Tag formTag = new Tag("form");
  Attributes formAttrs = new Attributes();
  formAttrs.put("action", "");
  FormElement formElement = new FormElement(formTag, BASE_URI, formAttrs);

  Element textInput = new Tag("input");
  textInput.tagName("input");
  textInput.attributes().put("type", "text");
  textInput.attributes().put("name", "username");
  formElement.addElement(textInput);

  Element selectElement = new Tag("select");
  selectElement.tagName("select");
  Elements options = new Elements();
  options.add(new Tag("option").text("United States").attributes().put("value", "US"));
  options.add(new Tag("option").selected(true).text("Canada").attributes().put("value", "CA"));
  selectElement.children(options);
  formElement.addElement(selectElement);

  Element checkboxElement = new Tag("input");
  checkboxElement.tagName("input");
  checkboxElement.attributes().put("type", "checkbox");
  checkboxElement.attributes().put("name", "agree");
  checkboxElement.attributes().put("checked", "checked");
  formElement.addElement(checkboxElement);

  Element submitElement = new Tag("input");
  submitElement.tagName("input");
  submitElement.attributes().put("type", "submit");
  formElement.addElement(submitElement);

  Elements formControls = formElement.elements();
  Assert.assertEquals(4, formControls.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testSubmitWithoutAction() {
  Tag formTag = new Tag("form");
  Attributes formAttrs = new Attributes();
  FormElement formElement = new FormElement(formTag, BASE_URI, formAttrs);
  formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidAction() {
  Tag formTag = new Tag("form");
  Attributes formAttrs = new Attributes();
  formAttrs.put("action", "");
  FormElement formElement = new FormElement(formTag, BASE_URI, formAttrs);
  formElement.submit();
 }

 @Test
 public void testFormDataEmptyForm() {
  Tag formTag = new Tag("form");
  Attributes formAttrs = new Attributes();
  FormElement formElement = new FormElement(formTag, BASE_URI, formAttrs);
  List<Connection.KeyVal> formData = formElement.formData();
  Assert.assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataSelectElement() {
  Tag formTag = new Tag("form");
  Attributes formAttrs = new Attributes();
  formAttrs.put("action", "");
  FormElement formElement = new FormElement(formTag, BASE_URI, formAttrs);

  Element selectElement = new Tag("select");
  Elements options = new Elements();
  options.add(new Tag("option").text("United States").attributes().put("value", "US"));
  selectElement.children(options);
  formElement.addElement(selectElement);

  List<Connection.KeyVal> formData = formElement.formData();
  Assert.assertEquals(0, formData.size());

  options = selectElement.select("option[selected]");
  for (Element option : options) {
    formElement.addElement(option);
  }
  formData = formElement.formData();
  Assert.assertEquals(1, formData.size());
  Assert.assertEquals("country", formData.get(0).key);
  Assert.assertEquals("CA", formData.get(0).value);
 }

 @Test
 public void testFormDataCheckboxElement() {
  Tag formTag = new Tag("form");
  Attributes formAttrs = new Attributes();
  formAttrs.put("action", "");
  FormElement formElement = new FormElement(formTag, BASE_URI, formAttrs);

  Element checkboxElement = new Tag("input");
  checkboxElement.tagName("input");
  checkboxElement.attributes().put("type", "checkbox");
  checkboxElement.attributes().put("name", "agree");
  formElement.addElement(checkboxElement);

  List<Connection.KeyVal> formData = formElement.formData();
  Assert.assertEquals(0, formData.size());

  checkboxElement.attributes().put("checked", "checked");
  formData = formElement.formData();
  Assert.assertEquals(1, formData.size());
  Assert.assertEquals("agree", formData.get(0).key);
  Assert.assertEquals("", formData.get(0).value);
 }

 @Test
 public void testFormDataSubmitElement() {
  Tag formTag = new Tag("form");
  Attributes formAttrs = new Attributes();
  formAttrs.put("action", "");
  FormElement formElement = new FormElement(formTag, BASE_URI, formAttrs);

  Element textInput = new Tag("input");
  textInput.tagName("input");
  textInput.attributes().put("type", "text");
  textInput.attributes().put("name", "username");
  formElement.addElement(textInput);

  Element submitElement = new Tag("input");
  submitElement.tagName("input");
  submitElement.attributes().put("type", "submit");
  formElement.addElement(submitElement);

  List<Connection.KeyVal> formData = formElement.formData();
  Assert.assertEquals(0, formData.size());

  textInput.val("test");
  formData = formElement.formData();
  Assert.assertEquals(1, formData.size());
  Assert.assertEquals("username", formData.get(0).key);
  Assert.assertEquals("test", formData.get(0).value);
 }

 @Test
 public void testFormDataMultipleElements() {
  Tag formTag = new Tag("form");
  Attributes formAttrs = new Attributes();
  formAttrs.put("action", "");
  FormElement formElement = new FormElement(formTag, BASE_URI, formAttrs);

  Element textInput1 = new Tag("input");
  textInput1.tagName("input");
  textInput1.attributes().put("type", "text");
  textInput1.attributes().put("name", "username1");
  formElement.addElement(textInput1);

  Element textInput2 = new Tag("input");
  textInput2.tagName("input");
  textInput2.attributes().put("type", "text");
  textInput2.attributes().put("name", "username2");
  formElement.addElement(textInput2);

  Element selectElement = new Tag("select");
  selectElement.tagName("select");
  Elements options = new Elements();
  options.add(new Tag("option").text("United States").attributes().put("value", "US"));
  selectElement.children(options);
  formElement.addElement(selectElement);

  Element checkboxElement1 = new Tag("input");
  checkboxElement1.tagName("input");
  checkboxElement1.attributes().put("type", "checkbox");
  checkboxElement1.attributes().put("name", "agree1");
  formElement.addElement(checkboxElement1);

  Element checkboxElement2 = new Tag("input");
  checkboxElement2.tagName("input");
  checkboxElement2.attributes().put("type", "checkbox");
  checkboxElement2.attributes().put("name", "agree2");
  formElement.addElement(checkboxElement2);

  Element submitElement = new Tag("input");
  submitElement.tagName("input");
  submitElement.attributes().put("type", "submit");
  formElement.addElement(submitElement);

  textInput1.val("test1");
  textInput2.val("test2");
  selectElement.select("option[value='CA']").first().attributes().put("selected", "selected");
  checkboxElement1.attributes().put("checked", "checked");

  List<Connection.KeyVal> formData = formElement.formData();
  Assert.assertEquals(5, formData.size());
  Assert.assertEquals("username1", formData.get(0).key);
  Assert.assertEquals("test1", formData.get(0).value);
  Assert.assertEquals("username2", formData.get(1).key);
  Assert.assertEquals("test2", formData.get(1).value);
  Assert.assertEquals("country", formData.get(2).key);
  Assert.assertEquals("CA", formData.get(2).value);
  Assert.assertEquals("agree1", formData.get(3).key);
  Assert.assertEquals("", formData.get(3).value);
  Assert.assertEquals("agree2", formData.get(4).key);
  Assert.assertEquals("", formData.get(4).value);
 }

}