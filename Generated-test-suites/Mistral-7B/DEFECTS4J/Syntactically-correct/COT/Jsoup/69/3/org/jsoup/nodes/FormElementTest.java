import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final String BASE_URI = "http://example.com";

 @org.junit.Before
 public void setup() {
 Jsoup.shutdownHooksAdd(new Runnable() {
 @Override
 public void run() {
 Jsoup.shutdown();
 }
 });
 }

 /**
 * Test constructor.
 */
 @org.junit.Test
 public void testFormElementConstructor() {
 // given
 String html = "<form action='/' method='get'>" +
 " <input type='text' name='name' value='value'>" +
 "</form>";
 Tag tag = new Tag("form", null, new Attributes(){{
 put("action", "/");
 put("method", "get");
 }});

 // when
 FormElement formElement = new FormElement(tag, BASE_URI, new Attributes());

 // then
 assertNotNull(formElement);
 assertEquals("form", formElement.tagName());
 }

 /**
 * Test getElements() method.
 */
 @org.junit.Test
 public void testGetElements() {
 // given
 String html = "<form action='/' method='get'>" +
 " <input type='text' name='name1' value='value1'>" +
 " <input type='text' name='name2' value='value2'>" +
 " <select name='name3'>" +
 " <option value='option1'>Option 1</option>" +
 " <option value='option2' selected='selected'>Option 2</option>" +
 " </select>" +
 "</form>";
 FormElement formElement = Jsoup.parse(html).selectFirst("form");

 // when
 Elements elements = formElement.elements();

 // then
 assertEquals(3, elements.size());
 }

 /**
 * Test addElement() method.
 */
 @org.junit.Test
 public void testAddElement() {
 // given
 String html = "<form action='/' method='get'>" +
 " <input type='text' name='name' value='value'>" +
 "</form>";
 FormElement formElement = Jsoup.parse(html).selectFirst("form");
 Element input = Jsoup.parse("<input type='text' name='name2' value='value2'>").selectFirst("input");

 // when
 formElement.addElement(input);

 // then
 assertEquals(2, formElement.elements().size());
 }

 /**
 * Test submit() method with valid form data.
 */
 @org.junit.Test
 public void testSubmitWithValidFormData() {
 // given
 String html = "<form action='/' method='post'>" +
 " <input type='text' name='name' value='value'>" +
 "</form>";
 FormElement formElement = Jsoup.parse(html).selectFirst("form");

 // when
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();

 // then
 assertNotNull(connection);
 assertEquals(1, formData.size());
 assertEquals("name", formData.get(0).key());
 assertEquals("value", formData.get(0).val());
 }

 /**
 * Test submit() method with empty form data.
 */
 @org.junit.Test
 public void testSubmitWithEmptyFormData() {
 // given
 String html = "<form action='/' method='post'>" +
 " <input type='text' name='name'>" +
 "</form>";
 FormElement formElement = Jsoup.parse(html).selectFirst("form");

 // when, then
 assertThrows(IllegalArgumentException.class, ()-> formElement.submit());
 }

 /**
 * Test submit() method with no action URL.
 */
 @org.junit.Test
 public void testSubmitWithNoActionUrl() {
 // given
 String html = "<form>" +
 " <input type='text' name='name' value='value'>" +
 "</form>";
 FormElement formElement = Jsoup.parse(html).selectFirst("form");

 // when, then
 assertThrows(IllegalArgumentException.class, ()-> formElement.submit());
 }

 /**
 * Test formData() method with empty form controls.
 */
 @org.junit.Test
 public void testFormDataWithEmptyFormControls() {
 // given
 String html = "<form action='/' method='post'>" +
 " <input type='text' name='name'>" +
 "</form>";
 FormElement formElement = Jsoup.parse(html).selectFirst("form");

 // when
 List<Connection.KeyVal> formData = formElement.formData();

 // then
 assertEquals(0, formData.size());
 }

 /**
 * Test formData() method with disabled form control.
 */
 @org.junit.Test
 public void testFormDataWithDisabledFormControl() {
 // given
 String html = "<form action='/' method='post'>" +
 " <input type='text' name='name1' value='value1' disabled>" +
 " <input type='text' name='name2' value='value2'>" +
 "</form>";
 FormElement formElement = Jsoup.parse(html).selectFirst("form");

 // when
 List<Connection.KeyVal> formData = formElement.formData();

 // then
 assertEquals(1, formData.size());
 }

 /**
 * Test formData() method with select form control.
 */
 @org.junit.Test
 public void testFormDataWithSelectFormControl() {
 // given
 String html = "<form action='/' method='post'>" +
 " <select name='name'>" +
 " <option value='option1'>Option 1</option>" +
 " <option value='option2' selected='selected'>Option 2</option>" +
 " </select>" +
 "</form>";
 FormElement formElement = Jsoup.parse(html).selectFirst("form");

 // when
 List<Connection.KeyVal> formData = formElement.formData();

 // then
 assertEquals(1, formData.size());
 assertEquals("name", formData.get(0).key());
 assertEquals("option2", formData.get(0).val());
 }

 /**
 * Test formData() method with checkbox or radio form control.
 */
 @org.junit.Test
 public void testFormDataWithCheckboxOrRadioFormControl() {
 // given
 String html = "<form action='/' method='post'>" +
 " <input type='text' name='name' value='value'>" +
 " <label><input type='checkbox' name='checkbox' value='value' checked='checked'>" +
 " Checkbox</label>" +
 "</form>";
 FormElement formElement = Jsoup.parse(html).selectFirst("form");

 // when
 List<Connection.KeyVal> formData = formElement.formData();

 // then
 assertEquals(1, formData.size());
 assertEquals("name", formData.get(0).key());
 assertEquals("value", formData.get(0).val());
 }

}