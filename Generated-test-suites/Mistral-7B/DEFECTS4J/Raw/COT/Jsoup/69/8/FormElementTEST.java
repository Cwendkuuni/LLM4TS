import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setup() {
 Tag tag = new Tag("form");
 Attributes attributes = new Attributes();
 formElement = new FormElement(tag, "", attributes);
 }

 @Test
 public void testFormElementConstruction() {
 assertNotNull(formElement);
 }

 @Test
 public void testElementsMethod() {
 Elements elements = formElement.elements();
 assertNotNull(elements);
 }

 @Test
 public void testAddElementMethod() {
 Element inputElement = new Element("input");
 formElement.addElement(inputElement);
 assertEquals(1, formElement.elements().size());
 }

 @Test
 public void testSubmitMethod_ValidFormActionUrl_GETRequest() throws Exception {
 Connection connection = formElement.submit();
 assertNotNull(connection);
 assertEquals(Connection.Method.GET, connection.method());
 }

 @Test
 public void testSubmitMethod_ValidFormActionUrl_POSTRequest() throws Exception {
 FormElement formElementWithPostMethod = new FormElement(new Tag("form"), "", new Attributes().put("method", "post"));
 Connection connection = formElementWithPostMethod.submit();
 assertNotNull(connection);
 assertEquals(Connection.Method.POST, connection.method());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethod_InvalidFormActionUrl() throws Exception {
 FormElement formElementWithEmptyAction = new FormElement(new Tag("form"), "", new Attributes().put("action", ""));
 assertThrows(IllegalArgumentException.class, () -> formElementWithEmptyAction.submit());
 }

 @Test
 public void testFormDataMethod_NoFormControls() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertNotNull(formData);
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethod_SingleInputElement() {
 Element inputElement = new Element("input");
 inputElement.attr("name", "testInput");
 inputElement.attr("value", "testValue");
 formElement.addElement(inputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("testInput", keyVal.key());
 assertEquals("testValue", keyVal.value());
 }

 @Test
 public void testFormDataMethod_MultipleInputElements() {
 Element inputElement1 = new Element("input");
 inputElement1.attr("name", "testInput1");
 inputElement1.attr("value", "testValue1");
 Element inputElement2 = new Element("input");
 inputElement2.attr("name", "testInput2");
 inputElement2.attr("value", "testValue2");
 formElement.addElement(inputElement1);
 formElement.addElement(inputElement2);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());
 }

 @Test
 public void testFormDataMethod_SelectElement() {
 Element selectElement = new Element("select");
 Element optionElement1 = new Element("option");
 optionElement1.attr("value", "optionValue1");
 optionElement1.attr("selected", "selected");
 Element optionElement2 = new Element("option");
 optionElement2.attr("value", "optionValue2");
 selectElement.add(optionElement1);
 selectElement.add(optionElement2);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("select", keyVal.key());
 assertEquals("optionValue1", keyVal.value());
 }

 @Test
 public void testFormDataMethod_CheckboxElement() {
 Element checkboxElement = new Element("input");
 checkboxElement.attr("name", "testCheckbox");
 checkboxElement.attr("type", "checkbox");
 checkboxElement.attr("checked", "checked");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("testCheckbox", keyVal.key());
 assertEquals("on", keyVal.value());
 }

 @Test
 public void testFormDataMethod_RadioElement() {
 Element radioElement1 = new Element("input");
 radioElement1.attr("name", "testRadio");
 radioElement1.attr("type", "radio");
 radioElement1.attr("checked", "checked");
 Element radioElement2 = new Element("input");
 radioElement2.attr("name", "testRadio");
 radioElement2.attr("type", "radio");
 formElement.addElement(radioElement1);
 formElement.addElement(radioElement2);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals("testRadio", keyVal.key());
 assertEquals("on", keyVal.value());
 }

 @Test
 public void testFormDataMethod_DisabledInputElement() {
 Element inputElement = new Element("input");
 inputElement.attr("name", "testInput");
 inputElement.attr("value", "testValue");
 inputElement.attr("disabled", "disabled");
 formElement.addElement(inputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataMethod_FormControlWithNoName() {
 Element inputElement = new Element("input");
 formElement.addElement(inputElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }
}