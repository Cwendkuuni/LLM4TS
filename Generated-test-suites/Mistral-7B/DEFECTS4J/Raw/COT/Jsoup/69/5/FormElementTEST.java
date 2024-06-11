import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String BASE_URI = "http://test.com";
 private static final String FORM_HTML = "<form action=\"submit.html\" method=\"post\">" +
         "<input type=\"text\" name=\"username\" value=\"test\">" +
         "<input type=\"password\" name=\"password\" value=\"test\">" +
         "<select name=\"select\">" +
         "<option value=\"option1\" selected></option>" +
         "<option value=\"option2\">Option 2</option>" +
         "</select>" +
         "<input type=\"checkbox\" name=\"checkbox\" value=\"value1\">" +
         "<input type=\"checkbox\" name=\"checkbox\" value=\"value2\" checked>" +
         "</form>";

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Assert.assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 formElement.add(new Element("input", new Attributes().put("type", "text").put("name", "username")) {
 }).add(new Element("input", new Attributes().put("type", "password").put("name", "password")));
 Elements elements = formElement.elements();
 List<Element> formControls = new ArrayList<>(elements);
 Assert.assertEquals(2, formControls.size());
 Assert.assertThat(formControls.get(0).tagName(), is("input"));
 Assert.assertThat(formControls.get(1).tagName(), is("input"));
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 FormElement result = formElement.addElement(inputElement);
 Elements elements = result.elements();
 List<Element> formControls = new ArrayList<>(elements);
 Assert.assertEquals(1, formControls.size());
 Assert.assertThat(formControls.get(0), is(inputElement));
 }

 @Test
 public void testSubmit() throws Exception {
 FormElement formElement = Jsoup.parse(FORm_HTML, BASE_URI).select("form").first().asFormElement();
 Connection connection = formElement.submit();
 String action = connection.url();
 String expectedAction = "submit.html";
 Assert.assertEquals(expectedAction, action);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyFormData() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 formElement.add(new Element("input", new Attributes().put("type", "text").put("name", "username")));
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 formElement.submit();
 }

 @Test
 public void testFormDataWithInputElements() {
 FormElement formElement = Jsoup.parse(FORm_HTML, BASE_URI).select("form").first().asFormElement();
 List<Connection.KeyVal> formData = formElement.formData();
 String expectedUsername = "test";
 String expectedPassword = "test";
 String expectedSelectOption1 = "option1";
 String expectedSelectOption2 = "option2";
 String expectedCheckboxValue1 = "value1";
 String expectedCheckboxValue2 = "value2";

 for (Connection.KeyVal keyVal : formData) {
 String key = keyVal.key();
 String value = keyVal.value();
 if (key.equals("username")) {
 Assert.assertEquals(expectedUsername, value);
 } else if (key.equals("password")) {
 Assert.assertEquals(expectedPassword, value);
 } else if (key.startsWith("select")) {
 List<String> options = Arrays.asList("option1", "option2");
 String expectedOption = options.get(0);
 if (value.startsWith(expectedOption)) {
 Assert.assertEquals(expectedOption, key);
 return;
 }
 } else if (key.startsWith("checkbox")) {
 List<String> checkboxValues = Arrays.asList("value1", "value2");
 String expectedValue = checkboxValues.get(0);
 if (value.equals(expectedValue)) {
 Assert.assertEquals(expectedValue, key);
 return;
 }
 }
 }
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = Jsoup.parse(FORm_HTML, BASE_URI).select("form").first().asFormElement();
 Element selectElement = formElement.elements().first();
 selectElement.select("option[selected]").remove();
 selectElement.select("option:last").first().attr("selected", "selected");
 List<Connection.KeyVal> formData = formElement.formData();
 String expectedSelectOption2 = "Option 2";
 String expectedSelectKey = "select";

 for (Connection.KeyVal keyVal : formData) {
 String key = keyVal.key();
 String value = keyVal.value();
 if (key.equals(expectedSelectKey)) {
 List<String> options = Arrays.asList("option1", expectedSelectOption2);
 Assert.assertEquals(expectedSelectOption2, value);
 Assert.assertEquals(expectedSelectKey, key);
 return;
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataWithDisabledInputElement() {
 FormElement formElement = Jsoup.parse(FORm_HTML, BASE_URI).select("form").first().asFormElement();
 formElement.elements().first().attr("disabled", "disabled");
 List<Connection.KeyVal> formData = formElement.formData();
 }
}