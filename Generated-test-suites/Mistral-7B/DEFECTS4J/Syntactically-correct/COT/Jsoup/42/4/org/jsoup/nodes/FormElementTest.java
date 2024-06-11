import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.helper.Elements;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private FormElement formElement;

 @Test
 public void testFormElementConstructor() {
 Tag tag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 formElement = new FormElement(tag, baseUri, attributes);

 Assert.assertEquals("form", formElement.tagName());
 Assert.assertEquals(baseUri, formElement.baseUri());
 }

 @Test
 public void testElements() {
 Tag formTag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 formElement = new FormElement(formTag, baseUri, attributes);

 Element inputElement = new Element("input");
 inputElement.attr("name", "inputName");
 formElement.add(inputElement);

 Elements elements = formElement.elements();
 List<Element> expectedElements = new ArrayList<>();
 expectedElements.add(inputElement);

 Assert.assertEquals(expectedElements, elements);
 }

 @Test
 public void testSubmitInvalidFormAction() {
 formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());

 try {
 formElement.submit();
 Validate.fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitNullFormData() {
 formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 formElement.add(new Element("input", new Attributes().put("name", "inputName")));

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 }

 @Test
 public void testSubmitFormDataSameNameMultipleValues() {
 formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());

 Element inputElement1 = new Element("input", new Attributes().put("name", "inputName").put("value", "value1"));
 formElement.add(inputElement1);

 Element inputElement2 = new Element("input", new Attributes().put("name", "inputName").put("value", "value2"));
 formElement.add(inputElement2);

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("inputName", "value1"));
 expectedFormData.add(HttpConnection.KeyVal.create("inputName", "value2"));

 Assert.assertEquals(expectedFormData, formData);
 }

 @Test
 public void testSubmitFormDataSameNameSingleValue() {
 formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());

 Element inputElement = new Element("input", new Attributes().put("name", "inputName").put("value", "value1"));
 formElement.add(inputElement);

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("inputName", "value1"));

 Assert.assertEquals(expectedFormData, formData);
 }

 @Test
 public void testSubmitFormDataEmptyName() {
 formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());

 Element inputElement = new Element("input", new Attributes().put("name", ""));
 formElement.add(inputElement);
 formElement.add(new Element("input", new Attributes().put("name", "inputName").put("value", "value1")));

 List<Connection.KeyVal> formData = formElement.formData();
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(HttpConnection.KeyVal.create("inputName", "value1"));

 Assert.assertEquals(expectedFormData, formData);
 }

 @Test
 public void testSubmitFormDataMissingName() {
 formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 formElement.add(new Element("input", new Attributes().put("value", "value1")));

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 }

 @Test
 public void testSubmitFormDataNullValue() {
 formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 formElement.add(new Element("input", new Attributes().put("name", "inputName").put("value", null)));

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 }

 @Test
 public void testSubmitFormDataEmptyValue() {
 formElement = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 formElement.add(new Element("input", new Attributes().put("name", "inputName").put("value", "")));

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 FormElement formElement2 = new FormElement(new Tag("form"), "http://example.com", new Attributes());

 Assert.assertTrue(formElement1.equals(formElement2));
 }

 @Test
 public void testEqualsDifferentTags() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 FormElement formElement2 = new FormElement(new Tag("div"), "http://example.com", new Attributes());

 Assert.assertFalse(formElement1.equals(formElement2));
 }

 @Test
 public void testEqualsDifferentBaseUri() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 FormElement formElement2 = new FormElement(new Tag("form"), "http://example.org", new Attributes());

 Assert.assertFalse(formElement1.equals(formElement2));
 }

 @Test
 public void testEqualsDifferentAttributes() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://example.com", new Attributes().put("id", "form1"));
 FormElement formElement2 = new FormElement(new Tag("form"), "http://example.com", new Attributes().put("id", "form2"));

 Assert.assertFalse(formElement1.equals(formElement2));
 }

 @Test
 public void testEqualsSameElementsButDifferentOrder() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element inputElement1 = new Element("input", new Attributes().put("name", "inputName1").put("value", "value1"));
 Element inputElement2 = new Element("input", new Attributes().put("name", "inputName2").put("value", "value2"));
 formElement1.add(inputElement2);
 formElement1.add(inputElement1);

 FormElement formElement2 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 formElement2.add(inputElement1);
 formElement2.add(inputElement2);

 Assert.assertFalse(formElement1.equals(formElement2));
 }

 @Test
 public void testEqualsSameElementsSameOrder() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 Element inputElement1 = new Element("input", new Attributes().put("name", "inputName1").put("value", "value1"));
 Element inputElement2 = new Element("input", new Attributes().put("name", "inputName2").put("value", "value2"));
 formElement1.add(inputElement1);
 formElement1.add(inputElement2);

 FormElement formElement2 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 formElement2.add(inputElement1);
 formElement2.add(inputElement2);

 Assert.assertTrue(formElement1.equals(formElement2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitNullForm() {
 FormElement formElement = null;
 formElement.submit();
 }
}