import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.helper.Validate;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class FormElementTest {

 private static final String BASE_URI = "http://example.com";
 private static final String FORM_HTML = "<form action=\"/submit\" method=\"post\">" +
         "<input type=\"text\" name=\"username\" value=\"test\"/>" +
         "<input type=\"password\" name=\"password\" value=\"test\"/>" +
         "<select name=\"country\">" +
         "<option value=\"US\"> United States </option>" +
         "<option value=\"CA\" selected> Canada </option>" +
         "</select>" +
         "<input type=\"submit\" name=\"submit\" value=\"Submit\"/>" +
         "</form>";

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Assert.assertThat(formElement.elements(), is(instanceOf(Elements.class)));
 }

 @Test
 public void testElementsMethodReturnsEmptyListWhenNoElements() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 List<Element> emptyElementsList = formElement.elements().all();
 Assert.assertThat(emptyElementsList, is(empty()));
 }

 @Test
 public void testElementsMethodReturnsListWithAddedElement() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Element inputElement = new Elements(new Tag("input")).first();
 formElement.addElement(inputElement);
 List<Element> elementsList = formElement.elements().all();
 Assert.assertThat(elementsList, hasSize(1));
 Assert.assertThat(elementsList.get(0), is(equalTo(inputElement)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodThrowsExceptionWhenActionUriCannotBeDetermined() {
 FormElement formElement = new FormElement(new Tag("form"), null, new Attributes());
 formElement.submit();
 }

 @Test
 public void testSubmitMethodWithValidActionUri() {
 FormElement formElement = new FormElement(new Tag("form"), BASE_URI, new Attributes());
 Connection connection = formElement.submit();
 Assert.assertThat(connection.url(), is(equalTo(BASE_URI + "/submit")));
 }

 @Test
 public void testFormDataMethodWithValidFormControls() {
 FormElement formElement = Element.parse(FORMDATA_HTML, Parser.htmlParser()).select("form").first().select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertThat(formData, hasSize(3));
 Assert.assertThat(formData.get(0).key(), is(equalTo("username")));
 Assert.assertThat(formData.get(0).value(), is(equalTo("test")));
 Assert.assertThat(formData.get(1).key(), is(equalTo("password")));
 Assert.assertThat(formData.get(1).value(), is(equalTo("test")));
 Assert.assertThat(formData.get(2).key(), is(equalTo("country")));
 Assert.assertThat(formData.get(2).value(), is(equalTo("CA")));
 }

 @Test
 public void testFormDataMethodWithEmptySelect() {
 FormElement formElement = Element.parse(FORMDATA_HTML, Parser.htmlParser()).select("form").first().select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertThat(formData, hasSize(2));
 }

 @Test
 public void testFormDataMethodWithDisabledInput() {
 String FORM_HTML_WITH_DISABLED_INPUT = FORM_HTML.replace("value=\"test\"", "disabled=\"disabled\"");
 FormElement formElement = Element.parse(FORM_HTML_WITH_DISABLED_INPUT, Parser.htmlParser()).select("form").first().select("form").first();
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertThat(formData, hasSize(1));
 }
}