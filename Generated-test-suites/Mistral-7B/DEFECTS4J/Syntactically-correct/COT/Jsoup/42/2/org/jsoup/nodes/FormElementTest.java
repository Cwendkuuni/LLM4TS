import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 Tag tag = new Tag("form");
 Attributes attributes = new Attributes();
 formElement = new FormElement(tag, "", attributes);
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 Element element1 = new Element("input");
 expectedElements.add(element1);
 formElement.addElement(element1);
 assertEquals(expectedElements, formElement.elements());
 }

 @Test
 public void testSubmit() {
String baseUri = "http://example.com";
 Connection connection = formElement.submit();
 String action = "http://example.com/submit";
 assertEquals(action, connection.url().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.removeAttr("action");
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 Element inputElement = new Element("input");
 inputElement.attr("name", "inputName");
 inputElement.attr("value", "inputValue");
 formElement.addElement(inputElement);

 expectedData.add(HttpConnection.KeyVal.create("inputName", "inputValue"));
 assertEquals(expectedData, formElement.formData());
 }

 @Test
 public void testFormDataWithSelectElement() {
 Element selectElement = new Element("select");
 Element optionElement1 = new Element("option").attr("value", "optionValue1").text("Option 1");
 Element optionElement2 = new Element("option").attr("value", "optionValue2").text("Option 2");
 selectElement.add(optionElement1);
 selectElement.add(optionElement2);
 selectElement.attr("name", "selectName");
 formElement.addElement(selectElement);

 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("selectName", "optionValue1"));
 formElement.addElement(optionElement1); // mark the first option as selected
 expectedData.add(HttpConnection.KeyVal.create("selectName", "optionValue2"));

 assertEquals(expectedData, formElement.formData());
 }

 @Test
 public void testFormDataWithCheckboxOrRadioElement() {
 Element checkboxElement = new Element("input").attr("type", "checkbox").attr("name", "checkboxName").attr("checked", "checked");
 Element radioElement1 = new Element("input").attr("type", "radio").attr("name", "radioName").attr("value", "radioValue1");
 Element radioElement2 = new Element("input").attr("type", "radio").attr("name", "radioName").attr("value", "radioValue2");
 formElement.addElement(checkboxElement);
 formElement.addElement(radioElement1);
 formElement.addElement(radioElement2);

 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("checkboxName", "true"));
 expectedData.add(HttpConnection.KeyVal.create("radioName", "radioValue1"));

 assertEquals(expectedData, formElement.formData());
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), "", new Attributes());
 FormElement formElement2 = new FormElement(new Tag("form"), "", new Attributes());
 Assert.assertTrue(formElement1.equals(formElement2));

 formElement1.addElement(new Element("input"));
 Assert.assertFalse(formElement1.equals(formElement2));
 }
}