import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.helper.Elements;
import org.jsoup.helper.HttpConnection;
import static org.junit.Assert.*;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 Tag tag = new Tag("form");
 Attributes attributes = new Attributes();
 formElement = new FormElement(tag, "http://test.com", attributes);
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 Element element = new Element("input");
 expectedElements.add(element);
 formElement.addElement(element);
 Equals equal = new Equals();
 assertEquals(expectedElements, formElement.elements());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element element = new Element("input");
 formElement.addElement(element);
 Equals equal = new Equals();
 assertEquals(element, formElement.elements().get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithoutAction() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormDataEmptyForm() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataSimpleForm() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement = new Element("input");
 inputElement.attr("name", "testName");
 inputElement.attr("type", "text");
 inputElement.attr("value", "testValue");
 formElement.addElement(inputElement);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("testName", "testValue"));
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element selectElement = new Element("select");
 Element optionElement = new Element("option");
 optionElement.attr("selected", "true");
 optionElement.val("testValue");
 selectElement.add(optionElement);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("testName", "testValue"));
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataCheckboxOrRadio() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element checkboxElement = new Element("input");
 checkboxElement.attr("name", "testName");
 checkboxElement.attr("type", "checkbox");
 checkboxElement.attr("checked", "true");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("testName", "on"));
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testFormDataMultipleElements() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement1 = new Element("input");
 inputElement1.attr("name", "testName1");
 inputElement1.attr("type", "text");
 inputElement1.attr("value", "testValue1");
 formElement.addElement(inputElement1);
 Element inputElement2 = new Element("input");
 inputElement2.attr("name", "testName2");
 inputElement2.attr("type", "text");
 inputElement2.attr("value", "testValue2");
 formElement.addElement(inputElement2);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("testName1", "testValue1"));
 expectedData.add(HttpConnection.KeyVal.create("testName2", "testValue2"));
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataWithNullName() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement = new Element("input");
 inputElement.attr("name", null);
 inputElement.attr("type", "text");
 inputElement.attr("value", "testValue");
 formElement.addElement(inputElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataWithEmptyName() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement = new Element("input");
 inputElement.attr("name", "");
 inputElement.attr("type", "text");
 inputElement.attr("value", "testValue");
 formElement.addElement(inputElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataWithEmptyValue() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement = new Element("input");
 inputElement.attr("name", "testName");
 inputElement.attr("type", "text");
 inputElement.attr("value", "");
 formElement.addElement(inputElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataWithNullValue() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 Element inputElement = new Element("input");
 inputElement.attr("name", "testName");
 inputElement.attr("type", "text");
 inputElement.attr("value", null);
 formElement.addElement(inputElement);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataWithEmptyForm() {
 FormElement formElement = new FormElement(new Tag("form"), "http://test.com", new Attributes());
 List<Connection.KeyVal> actualData = formElement.formData();
 }
}