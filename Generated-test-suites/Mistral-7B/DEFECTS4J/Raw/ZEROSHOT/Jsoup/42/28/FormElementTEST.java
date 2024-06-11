import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
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

public class FormElementTest {

 private Element formElement;
 private Connection connection;

 @Before
 public void setUp() {
 Parser parser = Jsoup.parser();
 Tag formTag = parser.parse("<form action='http://example.com'><input name='field1' type='text' value='value1'><select name='field2'><option value='option1'>Option 1</option><option value='option2' selected><option value='option3'>Option 3</option></select></form>").tags();
 formElement = new FormElement(formTag, "", new Attributes());
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 Element inputElement = elements.get(0);
 assertEquals("input", inputElement.tagName());
 Element selectElement = elements.get(1);
 assertEquals("select", selectElement.tagName());
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("input", new Attributes().put("type", "hidden").put("name", "field3").put("value", "value3"));
 formElement.addElement(newElement);
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(3, elements.size());
 }

 @Test
 public void testSubmit() {
 try {
 connection = formElement.submit();
 assertNotNull(connection);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(3, formData.size());

 Connection.KeyVal keyVal1 = formData.get(0);
 assertEquals("field1", keyVal1.key());
 assertEquals("value1", keyVal1.value());

 Connection.KeyVal keyVal2 = formData.get(1);
 assertEquals("field2", keyVal2.key());
 assertEquals("option2", keyVal2.value());

 Connection.KeyVal keyVal3 = formData.get(2);
 assertEquals("field3", keyVal3.key());
 assertNull(keyVal3.value());
 }

 @Test
 public void testHashCodeAndEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), "", new Attributes());
 FormElement formElement2 = new FormElement(formElement.tag(), "", formElement.attributes());
 assertEquals(formElement.hashCode(), formElement1.hashCode());
 assertEquals(formElement, formElement1);
 assertEquals(formElement, formElement2);
 }
}