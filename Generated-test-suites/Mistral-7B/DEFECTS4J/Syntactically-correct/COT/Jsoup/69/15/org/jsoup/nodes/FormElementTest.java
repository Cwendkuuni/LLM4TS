import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 Tag tag = new Tag("form");
 Attributes attributes = new Attributes();
 attributes.put("action", "http://test.com/submit");
 formElement = new FormElement(tag, "", attributes);
 }

 /**
 * Test method for {@link FormElement#FormElement(Tag, String, Attributes)}
 */
 @Test
 public void testFormElementConstructor() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 /**
 * Test method for {@link FormElement#elements()}
 */
 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertTrue(elements.isEmpty());
 }

 /**
 * Test method for {@link FormElement#addElement(Element)}
 */
 @Test
 public void testAddElement() {
 Element input = new Elements(new Tag("input")).first();
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.addElement(input);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(input, elements.get(0));
 }

 /**
 * Test method for {@link FormElement#submit()} with valid form action
 */
 @Test
 public void testSubmitWithValidFormAction() {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 /**
 * Test method for {@link FormElement#submit()} with invalid form action
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidFormAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", ""));
 formElement.submit();
 }

 /**
 * Test method for {@link FormElement#formData()} with empty form
 */
 @Test
 public void testFormDataWithEmptyForm() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertTrue(formData.isEmpty());
 }

 /**
 * Test method for {@link FormElement#formData()} with select tag
 */
 @Test
 public void testFormDataWithSelectTag() {
 Element select = new Elements(new Tag("select"))
         .attr("name", "test")
         .add(new Elements(new Tag("option").val("Option 1")))
         .add(new Elements(new Tag("option").val("Option 2")))
         .first();
 formElement.addElement(select);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("test", formData.get(0).key);
 assertEquals("Option 1", formData.get(0).value);
 assertEquals("test", formData.get(1).key);
 assertEquals("Option 2", formData.get(1).value);
 }

 /**
 * Test method for {@link FormElement#formData()} with checkbox tag
 */
 @Test
 public void testFormDataWithCheckboxTag() {
 Element checkbox = new Elements(new Tag("input").attr("type", "checkbox")).attr("name", "test").first();
 formElement.addElement(checkbox);
 checkbox.attr("checked", "true");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 assertEquals("test", formData.get(0).key);
 assertEquals("on", formData.get(0).value);
 }

 /**
 * Test method for {@link FormElement#formData()} with multiple checkboxes
 */
 @Test
 public void testFormDataWithMultipleCheckboxes() {
 Element checkbox1 = new Elements(new Tag("input").attr("type", "checkbox").attr("name", "test1")).first();
 Element checkbox2 = new Elements(new Tag("input").attr("type", "checkbox").attr("name", "test1")).first();
 formElement.addElement(checkbox1);
 formElement.addElement(checkbox2);
 checkbox1.attr("checked", "true");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("test1", formData.get(0).key);
 assertEquals("on", formData.get(0).value);
 assertEquals("test1", formData.get(1).key);
 assertEquals("on", formData.get(1).value);
 }

 /**
 * Test method for {@link FormElement#formData()} with multiple select options
 */
 @Test
 public void testFormDataWithMultipleSelectOptions() {
 Element select = new Elements(new Tag("select").attr("name", "test"))
         .add(new Elements(new Tag("option").val("Option 1")))
         .add(new Elements(new Tag("option").attr("selected", "true").val("Option 2")))
         .add(new Elements(new Tag("option").val("Option 3")))
         .first();
 formElement.addElement(select);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 assertEquals("test", formData.get(0).key);
 assertEquals("Option 2", formData.get(0).value);
 assertEquals("test", formData.get(1).key);
 assertEquals("Option 3", formData.get(1).value);
 }
}