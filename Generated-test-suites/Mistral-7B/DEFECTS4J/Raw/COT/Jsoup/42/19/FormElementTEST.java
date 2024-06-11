import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private FormElement formElement;

 @Test
 public void testFormElementConstruction() {
 Tag tag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 formElement = new FormElement(tag, baseUri, attributes);

 Assert.assertNotNull(formElement);
 Assert.assertEquals("form", formElement.tagName());
 }

 @Test
 public void testElements() {
 Tag formTag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 formElement = new FormElement(formTag, baseUri, attributes);
 Element inputElement = new Element("input");
 formElement.elements().add(inputElement);

 List<Element> elements = formElement.elements();

 Assert.assertNotNull(elements);
 Assert.assertEquals(1, elements.size());
 Assert.assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testAddElement() {
 Tag formTag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 formElement = new FormElement(formTag, baseUri, attributes);
 Element inputElement = new Element("input");
 formElement.addElement(inputElement);

 List<Element> elements = formElement.elements();

 Assert.assertNotNull(elements);
 Assert.assertEquals(1, elements.size());
 Assert.assertEquals(inputElement, elements.get(0));
 }

 @Test
 public void testSubmitWithValidActionUrl() throws Exception {
 Tag formTag = new Tag("form", "action", "http://example.com/submit");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 formElement = new FormElement(formTag, baseUri, attributes);

 Connection connection = formElement.submit();
 Assert.assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidActionUrl() throws Exception {
 Tag formTag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 formElement = new FormElement(formTag, baseUri, attributes);
 formElement.submit();
 }

 @Test
 public void testFormData() {
 Tag formTag = new Tag("form");
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 formElement = new FormElement(formTag, baseUri, attributes);

 Element inputElement1 = new Element("input", "name1", "value1");
 formElement.elements().add(inputElement1);
 Element inputElement2 = new Element("input", "name2", "value2");
 formElement.elements().add(inputElement2);
 Element selectElement = new Element("select", "selectName");
 Element optionElement1 = new Element("option", null, "optionValue1");
 selectElement.elements().add(optionElement1);
 formElement.elements().add(selectElement);
 Element checkboxElement1 = new Element("input", "checkboxName", null);
 checkboxElement1.setAttribute("checked", "checked");
 formElement.elements().add(checkboxElement1);

 List<Connection.KeyVal> formData = formElement.formData();
 List<String> expectedFormData = new ArrayList<String>() {{
 add("name1=value1");
 add("name2=value2");
 add("selectName=optionValue1");
 add("checkboxName=checkboxName");
 }};

 for (Connection.KeyVal data : formData) {
 String keyVal = data.key() + "=" + data.value();
 Assert.assertEquals(expectedFormData.get(expectedFormData.size() - 1), keyVal);
 expectedFormData.remove(expectedFormData.size() - 1);
 }

 Assert.assertEquals(0, expectedFormData.size());
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 FormElement formElement2 = new FormElement(new Tag("form"), "http://example.com", new Attributes());

 Assert.assertEquals(formElement1, formElement2);
 }

 @Test
 public void testNotEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 FormElement formElement2 = new FormElement(new Tag("form"), "http://example.com", new Attributes());
 FormElement formElement3 = new FormElement(new Tag("div"), "http://example.com", new Attributes());

 Assert.assertNotEquals(formElement1, formElement2);
 Assert.assertNotEquals(formElement1, formElement3);
 }
}