import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String BASE_URI = "http://example.com";
 private static final String FORM_HTML = "<form action='/submit' method='post' name='myForm'> " +
         "<input type='text' name='username' value='test'> " +
         "<input type='hidden' name='hidden' value='hiddenValue'> " +
         "<textarea name='message'>Test message</textarea> " +
         "<select name='select'> " +
         "<option value='option1'>Option 1</option> " +
         "<option value='option2' selected>Option 2</option> " +
         "</select> " +
         "<input type='checkbox' name='checkbox' checked> " +
         "<input type='checkbox' name='checkbox' unchecked> " +
         "</form>";

 @Test
 public void testFormElementConstructor() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), BASE_URI, null);
 Assert.assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 FormElement formElement = parseFormElement(FORMDATA);
 Elements elements = formElement.elements();
 Assert.assertNotNull(elements);
 Assert.assertEquals(5, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes()), BASE_URI, null);
 Element inputElement = new Element(new Tag("input", new Attributes().put("type", "text").put("name", "textfield")), BASE_URI);
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 Assert.assertEquals(1, elements.size());
 Assert.assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testSubmitMethodWithValidUrl() {
 FormElement formElement = parseFormElement(FORMDATA);
 Connection connection = formElement.submit();
 Assert.assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithEmptyActionUrl() {
 FormElement formElement = new FormElement(new Tag("form", new Attributes().put("action", "").put("method", "POST")), BASE_URI, null);
 formElement.submit();
 }

 @Test
 public void testFormDataMethodWithValidElements() {
 FormElement formElement = parseFormElement(FORMDATA);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertNotNull(formData);
 Assert.assertEquals(5, formData.size());
 }

 @Test
 public void testFormDataMethodWithSelectElement() {
 FormElement formElement = parseFormElement(FORMDATA);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals("option2", formData.get(3).getKey());
 Assert.assertEquals("Option 2", formData.get(3).getValue());
 }

 @Test
 public void testFormDataMethodWithCheckboxElements() {
 FormElement formElement = parseFormElement(FORMDATA);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals("checkbox", formData.get(4).getKey());
 Assert.assertEquals("true", formData.get(4).getValue());
 }

 @Test
 public void testFormDataMethodWithUncheckedCheckboxElement() {
 FormElement formElement = parseFormElement(FORMDATA);
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals("checkbox", formData.get(5).getKey());
 Assert.assertEquals("", formData.get(5).getValue());
 }

 private FormElement parseFormElement(String formData) {
 Element formElementTag = new Tag("form", new Attributes().put("name", "myForm"));
 return new FormElement(formElementTag, BASE_URI, null);
 }

 private static final String FORMDATA = "<html><body>" + FORM_HTML + "</body></html>";

}