import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private static final String BASE_URI = "http://example.com";
 private static final String TAG_NAME = "form";
 private static final String ATTRIBUTE_ACTION = "action";
 private static final String ATTRIBUTE_METHOD = "method";
 private static final String ATTRIBUTE_NAME = "name";
 private static final String ATTRIBUTE_TYPE = "type";
 private static final String ATTRIBUTE_DISABLED = "disabled";
 private static final String ATTRIBUTE_CHECKED = "checked";
 private static final String VALUE_EMPTY = "";
 private static final String VALUE_STRING = "test";
 private static final String VALUE_LIST = "list";
 private static final String TYPE_SELECT = "select";
 private static final String TYPE_CHECKBOX = "checkbox";
 private static final String TYPE_RADIO = "radio";

 @Test
 public void testFormElementConstructor() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testFormElementElements() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertFalse(elements.isEmpty());
 }

 @Test
 public void testFormElementAddElement() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 Element element = new Element("input", new Attributes().put("type", "text").put("name", "test"));
 formElement.addElement(element);
 assertEquals(formElement.elements().size(), 1);
 }

 @Test
 public void testFormElementRemoveChild() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 Element childElement = new Element("input", new Attributes().put("type", "text").put("name", "test"));
 formElement.addElement(childElement);
 formElement.removeChild(childElement);
 assertEquals(formElement.elements().size(), 0);
 }

 @Test
 public void testFormElementSubmitThrowsIllegalArgumentException() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 assertThrows(IllegalArgumentException.class, () -> formElement.submit());
 }

 @Test
 public void testFormElementSubmitWithBaseUri() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes().put("action", "/path/to/submit"));
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormElementSubmitWithMethodGet() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes().put("action", "/path/to/submit").put("method", "GET"));
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormElementSubmitWithMethodPost() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes().put("action", "/path/to/submit").put("method", "POST"));
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormElementFormDataEmptyList() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertFalse(formData.isEmpty());
 }

 @Test
 public void testFormElementFormDataWithSelectTag() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes().put("action", "/path/to/submit"));
 Element selectElement = new Element("select", new Attributes().put("name", "test"));
 formElement.addElement(selectElement);
 Element optionOne = new Element("option", new Attributes().put("value", "option1"));
 optionOne.val(VALUE_STRING);
 selectElement.addElement(optionOne);
 Element optionTwo = new Element("option", new Attributes().put("selected", "selected").put("value", "option2"));
 optionTwo.val(VALUE_STRING);
 selectElement.addElement(optionTwo);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(formData.size(), 1);
 assertEquals(formData.get(0).key, ATTRIBUTE_NAME);
 assertEquals(formData.get(0).value, VALUE_STRING);
 }

 @Test
 public void testFormElementFormDataWithSelectTagNoSelectedOption() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes().put("action", "/path/to/submit"));
 Element selectElement = new Element("select", new Attributes().put("name", "test"));
 formElement.addElement(selectElement);
 Element optionOne = new Element("option", new Attributes().put("value", "option1"));
 optionOne.val(VALUE_STRING);
 selectElement.addElement(optionOne);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(formData.size(), 0);
 }

 @Test
 public void testFormElementFormDataWithCheckboxOrRadioTag() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes().put("action", "/path/to/submit"));
 Element checkboxElement = new Element("input", new Attributes().put("type", TYPE_CHECKBOX).put("name", "test"));
 formElement.addElement(checkboxElement);
 checkboxElement.attr("checked", "checked");
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(formData.size(), 1);
 assertEquals(formData.get(0).key, ATTRIBUTE_NAME);
 assertEquals(formData.get(0).value, "on");
 }

 @Test
 public void testFormElementFormDataWithTextInputTag() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes().put("action", "/path/to/submit"));
 Element textInputElement = new Element("input", new Attributes().put("type", "text").put("name", "test").put("value", VALUE_STRING));
 formElement.addElement(textInputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(formData.size(), 1);
 assertEquals(formData.get(0).key, ATTRIBUTE_NAME);
 assertEquals(formData.get(0).value, VALUE_STRING);
 }

 @Test
 public void testFormElementFormDataWithDisabledInput() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes().put("action", "/path/to/submit"));
 Element textInputElement = new Element("input", new Attributes().put("type", "text").put("name", "test").put("disabled", "disabled"));
 formElement.addElement(textInputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(formData.size(), 0);
 }
}