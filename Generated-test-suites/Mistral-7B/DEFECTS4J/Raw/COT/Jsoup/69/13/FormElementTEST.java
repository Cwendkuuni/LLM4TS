import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String BASE_URI = "http://example.com";
 private static final String FORM_HTML = "<html>" +
         "<body>" +
         "<form action='/submit' method='post'>" +
         "<input type='text' name='input1'>" +
         "<input type='text' name='input2' disabled>" +
         "<select name='select1'>" +
         "<option value='option1'>Option 1</option>" +
         "<option value='option2' selected>Option 2</option>" +
         "</select>" +
         "<input type='checkbox' name='checkbox' checked>" +
         "<input type='hidden' name='hidden' value='hidden'>" +
         "</form>" +
         "</body>" +
         "</html>";

 @Test
 public void testFormElementCreation() {
 Element formElement = Jsoup.parse(FORM_HTML, BASE_URI).body().selectFirst("form");
 FormElement formElementInstance = new FormElement(formElement.tag(), BASE_URI, formElement.attributes());

 Assert.assertEquals(formElementInstance.elements().size(), 5);
 }

 @Test
 public void testElements() {
 FormElement formElement = new FormElement(new Tag("form", null, new Attributes()), BASE_URI, new Attributes());
 formElement.add(new Element("input", new Attributes().put("name", "input1"))).add(new Element("input", new Attributes().put("name", "input2").put("disabled", "disabled")));
 formElement.add(new Element("select", new Attributes().put("name", "select1"))).add(new Element("option", new Attributes().put("value", "option1")));
 formElement.add(new Element("input", new Attributes().put("type", "checkbox").put("name", "checkbox").put("checked", "checked")));
 formElement.add(new Element("input", new Attributes().put("type", "hidden").put("name", "hidden").put("value", "hidden")));

 Assert.assertEquals(formElement.elements().size(), 5);
 }

 @Test
 public void testSubmitWithValidActionUrl() {
 FormElement formElement = new FormElement(new Tag("form", null, new Attributes().put("action", "/submit")), BASE_URI, new Attributes());
 Connection connection = formElement.submit();
 Assert.assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidActionUrl() {
 FormElement formElement = new FormElement(new Tag("form", null, new Attributes()), BASE_URI, new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormData() {
 FormElement formElement = new FormElement(new Tag("form", null, new Attributes()), BASE_URI, new Attributes());
 formElement.add(new Element("input", new Attributes().put("name", "input1"))).add(new Element("input", new Attributes().put("name", "input2").put("disabled", "disabled")));
 formElement.add(new Element("select", new Attributes().put("name", "select1"))).add(new Element("option", new Attributes().put("value", "option1")));
 formElement.add(new Element("input", new Attributes().put("type", "checkbox").put("name", "checkbox").put("checked", "checked")));
 formElement.add(new Element("input", new Attributes().put("type", "hidden").put("name", "hidden").put("value", "hidden")));

 List<Connection.KeyVal> formData = formElement.formData();

 List<String> expectedFormData = new ArrayList<>();
 expectedFormData.add("input1= ");
 expectedFormData.add("select1=option1");
 expectedFormData.add("checkbox=on");

 for (Connection.KeyVal keyVal : formData) {
 expectedFormData.add(keyVal.key + "=" + keyVal.val);
 }

 Assert.assertEquals(expectedFormData.toString(), formData.toString());
 }

 @Test
 public void testFormDataEmptyList() {
 FormElement formElement = new FormElement(new Tag("form", null, new Attributes()), BASE_URI, new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(formData.size(), 0);
 }
}