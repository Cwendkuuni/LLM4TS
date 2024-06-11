import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setUp() {
 Parser parser = new Parser.MalformedEncodingDetector();
 String html = "<form action=\"http://test.com\"><input type=\"text\" name=\"input1\"/><input type=\"text\" name=\"input2\"/>" +
         "<select name=\"selectElement\"><option value=\"option1\">Option 1</option><option value=\"option2\">Option 2</option></select>" +
         "<input type=\"submit\"/></form>";
 formElement = parser.parseBodyFragment(html).getElementsByTag("form").first();
 }

 @Test
 public void testFormElementConstructor() {
 Tag tag = formElement.tag();
 assertNotNull(tag);
 assertEquals("form", tag.tagName());
 }

 @Test
 public void testElementsMethod() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 Element input = formElement.getElementsByTag("input").get(0);
 formElement.addElement(input);
 Elements newElements = formElement.elements();
 assertEquals(4, newElements.size());
 }

 @Test
 public void testSubmitMethodNormalCase() throws Exception {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodInvalidActionUrl() {
 formElement.setAttrs(new Attributes().put("action", ""));
 assertThrows(IllegalArgumentException.class, () -> formElement.submit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodNullElement() {
 assertThrows(IllegalArgumentException.class, FormElement::new, "FormElement constructor should fail with null element.");
 }

 @Test
 public void testFormDataMethodNormalCase() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(3, formData.size());
 }

 @Test
 public void testFormDataMethodEmptyForm() {
 Element emptyForm = Parser.parseBodyFragment("<form></form>").getElementsByTag("form").first();
 List<Connection.KeyVal> formData = emptyForm.formData();
 assertNotNull(formData);
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethodSelectElement() {
 List<Connection.KeyVal> formData = formElement.formData();
 String selectElementName = "selectElement";
 Connection.KeyVal selectElementValue = null;
 for (Connection.KeyVal keyVal : formData) {
 if (selectElementName.equals(keyVal.key())) {
 selectElementValue = keyVal;
 break;
 }
 }
 assertNotNull(selectElementValue);
 assertEquals("option1", selectElementValue.value());
 }
}