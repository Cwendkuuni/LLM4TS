import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private static final FormElement TEST_FORM = new FormElement(new Tag("form"), "base-uri", new Attributes());

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 expectedElements.add(new Element("input"));
 expectedElements.add(new Element("textarea"));
 expectedElements.add(new Element("select"));
 expectedElements.add(new Element("button"));

 Assert.assertEquals(expectedElements, TEST_FORM.elements());
 }

 @Test
 public void testAddElement() {
 Element inputElement = new Element("input");
 FormElement formElement = new FormElement(new Tag("form"), "base-uri", new Attributes());
 formElement.addElement(inputElement);

 Elements formControls = formElement.elements();
 Assert.assertEquals(1, formControls.size());
 Assert.assertEquals(inputElement, formControls.get(0));
 }

 @Test
 public void testSubmitThrowsIllegalArgumentException() {
 FormElement formElement = new FormElement(new Tag("form"), "base-uri", new Attributes());

 try {
 formElement.submit();
 Assert.fail("Expected IllegalArgumentException when form has no action URL.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitWithActionUrl() {
 FormElement formElement = new FormElement(new Tag("form", "action", "http://example.com"), "base-uri", new Attributes());
 Connection connection = formElement.submit();
 // assert that connection has been created correctly
 }

 @Test
 public void testFormDataEmptyList() {
 FormElement formElement = new FormElement(new Tag("form"), "base-uri", new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataWithSingleElement() {
 FormElement formElement = new FormElement(new Tag("form"), "base-uri", new Attributes().put("name", "testName"));
 Element inputElement = new Element("input", new Attributes().put("name", "testName").put("value", "testValue"));
 formElement.addElement(inputElement);

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("testName", keyVal.key());
 Assert.assertEquals("testValue", keyVal.val());
 }

 @Test
 public void testFormDataWithMultipleElements() {
 FormElement formElement = new FormElement(new Tag("form"), "base-uri", new Attributes());
 Element inputElement1 = new Element("input", new Attributes().put("name", "testName1").put("value", "testValue1"));
 Element inputElement2 = new Element("input", new Attributes().put("name", "testName2").put("value", "testValue2"));
 formElement.addElement(inputElement1);
 formElement.addElement(inputElement2);

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(2, formData.size());

 Connection.KeyVal keyVal1 = formData.get(0);
 Assert.assertEquals("testName1", keyVal1.key());
 Assert.assertEquals("testValue1", keyVal1.val());

 Connection.KeyVal keyVal2 = formData.get(1);
 Assert.assertEquals("testName2", keyVal2.key());
 Assert.assertEquals("testValue2", keyVal2.val());
 }

 @Test
 public void testFormDataWithSelectElement() {
 FormElement formElement = new FormElement(new Tag("form"), "base-uri", new Attributes().put("name", "testName"));
 Element selectElement = new Element("select", new Attributes().put("name", "testName"));
 Element optionElement1 = new Element("option", new Attributes().put("value", "testValue1"));
 Element optionElement2 = new Element("option", new Attributes().put("value", "testValue2"));
 selectElement.add(optionElement1);
 selectElement.add(optionElement2);
 selectElement.setAttribute("selected", "selected");
 formElement.addElement(selectElement);

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("testName", keyVal.key());
 Assert.assertEquals("testValue1", keyVal.val());
 }

 @Test
 public void testFormDataWithCheckboxOrRadioElement() {
 FormElement formElement = new FormElement(new Tag("form"), "base-uri", new Attributes().put("name", "testName"));
 Element checkboxElement = new Element("input", new Attributes().put("name", "testName").put("type", "checkbox"));
 checkboxElement.setAttribute("checked", "checked");
 formElement.addElement(checkboxElement);

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 Assert.assertEquals("testName", keyVal.key());
 Assert.assertEquals("on", keyVal.val());
 }

 @Test
 public void testFormDataWithMultipleCheckboxOrRadioElements() {
 FormElement formElement = new FormElement(new Tag("form"), "base-uri", new Attributes().put("name", "testName"));
 Element checkboxElement1 = new Element("input", new Attributes().put("name", "testName").put("type", "checkbox"));
 checkboxElement1.setAttribute("checked", "checked");
 Element checkboxElement2 = new Element("input", new Attributes().put("name", "testName").put("type", "checkbox"));
 formElement.addElement(checkboxElement1);
 formElement.addElement(checkboxElement2);

 List<Connection.KeyVal> formData = formElement.formData();
 Assert.assertEquals(2, formData.size());

 Connection.KeyVal keyVal1 = formData.get(0);
 Assert.assertEquals("testName", keyVal1.key());
 Assert.assertEquals("on", keyVal1.val());

 Connection.KeyVal keyVal2 = formData.get(1);
 Assert.assertEquals("testName", keyVal2.key());
 Assert.assertEquals("on", keyVal2.val());
 }

}