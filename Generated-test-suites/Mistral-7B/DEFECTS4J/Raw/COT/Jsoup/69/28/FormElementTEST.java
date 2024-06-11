import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormElementTest {

 FormElement formElement;
 Element element;
 Attributes attributes;
 Tag tag;

 @Before
 public void setUp() {
 tag = mock(Tag.class);
 attributes = new Attributes();
 element = new Element("element") {
 @Override
 public Tag tag() {
 return tag;
 }
 };
 formElement = new FormElement(tag, "baseUri", attributes);
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 expectedElements.add(element);
 assertEquals(expectedElements, formElement.elements());
 }

 @Test
 public void testAddElement() {
 Element newElement = new Element("newElement") {
 @Override
 public Tag tag() {
 return mock(Tag.class);
 }
 };
 formElement.addElement(newElement);
 Elements expectedElements = new Elements();
 expectedElements.add(element);
 expectedElements.add(newElement);
 assertEquals(expectedElements, formElement.elements());
 }

 @Test
 public void testSubmit() {
 String baseUri = "baseUri";
 String action = "action";
 Connection connection = mock(Connection.class);
 try {
 when(tag.tagName()).thenReturn("form");
 when(tag.hasAttr("action")).thenReturn(true);
 when(tag.absUrl("action")).thenReturn(action);
 when(Jsoup.connect(action).data(formData()).method(Connection.Method.GET)).thenReturn(connection);

 FormElement formElement = new FormElement(tag, baseUri, attributes);
 Connection result = formElement.submit();
 assertEquals(connection, result);
 } catch (IllegalArgumentException e) {
 fail("Expected submit method to not throw an IllegalArgumentException.");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithNoAction() {
 when(tag.tagName()).thenReturn("form");
 when(tag.hasAttr("action")).thenThrow(new RuntimeException());
 FormElement formElement = new FormElement(tag, "baseUri", attributes);
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 when(tag.tagName()).thenReturn("form");
 when(tag.hasAttr("action")).thenReturn(true);
 when(tag.absUrl("action")).thenReturn("");
 FormElement formElement = new FormElement(tag, "baseUri", attributes);
 formElement.submit();
 }

 @Test
 public void testFormData() {
 ArrayList<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("name1", "value1"));
 expectedData.add(HttpConnection.KeyVal.create("name2", "value2"));
 FormElement formElement = setupFormElementWithElements(expectedData);
 List<Connection.KeyVal> result = formElement.formData();
 assertEquals(expectedData, result);
 }

 @Test
 public void testFormDataWithNoFormControlls() {
 FormElement formElement = new FormElement(mock(Tag.class), "baseUri", attributes);
 List<Connection.KeyVal> result = formElement.formData();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFormDataWithDisabledFormControl() {
 Element disabledFormControl = new Element("disabledFormControl") {
 @Override
 public Tag tag() {
 return mock(Tag.class);
 }
 };
 disabledFormControl.attributes().put("disabled", "disabled");
 FormElement formElement = setupFormElementWithElement(disabledFormControl);
 List<Connection.KeyVal> result = formElement.formData();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFormDataWithNoNameAttribute() {
 Element elementWithNoNameAttribute = new Element("elementWithNoNameAttribute") {
 @Override
 public Tag tag() {
 return mock(Tag.class);
 }
 };
 FormElement formElement = setupFormElementWithElement(elementWithNoNameAttribute);
 List<Connection.KeyVal> result = formElement.formData();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFormDataWithSelectElement() {
 Element selectElement = new Element("selectElement") {
 @Override
 public Tag tag() {
 return tag;
 }
 };
 Element option1 = new Element("option1") {
 @Override
 public Tag tag() {
 return mock(Tag.class);
 }
 };
 Element option2 = new Element("option2") {
 @Override
 public Tag tag() {
 return mock(Tag.class);
 }
 };
 selectElement.select("option").add(option1);
 selectElement.select("option").add(option2);
 selectElement.attributes().put("name", "selectName");
 FormElement formElement = setupFormElementWithElement(selectElement);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("selectName", option1.val()));
 expectedData.add(HttpConnection.KeyVal.create("selectName", option2.val()));
 List<Connection.KeyVal> result = formElement.formData();
 assertEquals(expectedData, result);
 }

 @Test
 public void testFormDataWithCheckboxOrRadioElement() {
 Element checkboxOrRadioElement = new Element("checkboxOrRadioElement") {
 @Override
 public Tag tag() {
 return mock(Tag.class);
 }
 };
 checkboxOrRadioElement.attributes().put("name", "checkboxOrRadioName");
 checkboxOrRadioElement.attributes().put("type", "checkbox");
 checkboxOrRadioElement.attributes().put("checked", "checked");
 FormElement formElement = setupFormElementWithElement(checkboxOrRadioElement);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("checkboxOrRadioName", "on"));
 List<Connection.KeyVal> result = formElement.formData();
 assertEquals(expectedData, result);
 }

 @Test
 public void testFormDataWithMultipleCheckboxOrRadioElements() {
 Element checkboxOrRadioElement1 = new Element("checkboxOrRadioElement1") {
 @Override
 public Tag tag() {
 return mock(Tag.class);
 }
 };
 checkboxOrRadioElement1.attributes().put("name", "checkboxOrRadioName");
 checkboxOrRadioElement1.attributes().put("type", "checkbox");
 checkboxOrRadioElement1.attributes().put("checked", "checked");
 Element checkboxOrRadioElement2 = new Element("checkboxOrRadioElement2") {
 @Override
 public Tag tag() {
 return mock(Tag.class);
 }
 };
 checkboxOrRadioElement2.attributes().put("name", "checkboxOrRadioName");
 checkboxOrRadioElement2.attributes().put("type", "checkbox");
 FormElement formElement = setupFormElementWithElements(checkboxOrRadioElement1, checkboxOrRadioElement2);
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(HttpConnection.KeyVal.create("checkboxOrRadioName", "on"));
 expectedData.add(HttpConnection.KeyVal.create("checkboxOrRadioName", "on"));
 List<Connection.KeyVal> result = formElement.formData();
 assertEquals(expectedData, result);
 }

 private FormElement setupFormElementWithElement(Element element) {
 FormElement formElement = new FormElement(tag, "baseUri", attributes);
 formElement.addElement(element);
 return formElement;
 }

 private FormElement setupFormElementWithElements(List<Element> elements) {
 FormElement formElement = new FormElement(tag, "baseUri", attributes);
 for (Element el : elements) {
 formElement.addElement(el);
 }
 return formElement;
 }
}