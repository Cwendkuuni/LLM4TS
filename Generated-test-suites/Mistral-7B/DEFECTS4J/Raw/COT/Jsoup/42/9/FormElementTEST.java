import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.jsoup.nodes.*;
import org.jsoup.Connection.*;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

 private FormElement formElement;
 private Elements elements;
 private Tag tag;
 private Attributes attributes;
 private Connection connection;

 @Before
 public void setUp() {
 tag = new Tag("form");
 attributes = new Attributes();
 attributes.put("action", "http://www.example.com");
 formElement = new FormElement(tag, "baseUrl", attributes);
 elements = new Elements();
 elements.add(new InputElement("input1", "type1", "value1"));
 formElement.addElement(elements.get(0));
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 expectedElements.add(new InputElement("input1", "type1", "value1"));
 List<Element> actualElements = formElement.elements().list();
 assertEquals("Size of form elements should be equal.", expectedElements.size(), actualElements.size());
 for (int i = 0; i < expectedElements.size(); i++) {
 Element expectedElement = expectedElements.get(i);
 Element actualElement = actualElements.get(i);
 assertEquals("Form element at index [" + i + "] should be equal.", expectedElement, actualElement);
 }
 }

 @Test
 public void testSubmit() throws Exception {
 Connection expectedConnection = Jsoup.connect("http://www.example.com")
 .data(formData());
 Connection actualConnection = formElement.submit();
 assertEquals("Submitted connection should not be null.", notNullValue(), actualConnection);
 assertEquals("Submitted connection should be instance of Connection.", isA(Connection.class), actualConnection);
 assertEquals("Submitted connection should have same data as form.", expectedConnection.data(), actualConnection.data());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() throws Exception {
 formElement = new FormElement(new Tag("form"), "baseUrl", new Attributes());
 formElement.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormDataWithEmptyForm() {
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Form data size should be equal.", expectedFormData.size(), actualFormData.size());
 }

 @Test
 public void testFormDataWithInputElement() {
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(new Connection.KeyVal("input1", "value1"));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Form data size should be equal.", expectedFormData.size(), actualFormData.size());
 for (int i = 0; i < expectedFormData.size(); i++) {
 Connection.KeyVal expectedKeyVal = expectedFormData.get(i);
 Connection.KeyVal actualKeyVal = actualFormData.get(i);
 assertTrue("Form data at index [" + i + "] key should be equal.", expectedKeyVal.key().equals(actualKeyVal.key()));
 assertTrue("Form data at index [" + i + "] value should be equal.", expectedKeyVal.value().equals(actualKeyVal.value()));
 }
 }

 @Test
 public void testFormDataWithSelectElement() {
 Elements selectOptions = new Elements();
 selectOptions.add(new OptionElement("option1", "value1"));
 Element selectElement = new SelectElement("select1", selectOptions);
 formElement.addElement(selectElement);
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(new Connection.KeyVal("select1", "value1"));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Form data size should be equal.", expectedFormData.size(), actualFormData.size());
 for (int i = 0; i < expectedFormData.size(); i++) {
 Connection.KeyVal expectedKeyVal = expectedFormData.get(i);
 Connection.KeyVal actualKeyVal = actualFormData.get(i);
 assertTrue("Form data at index [" + i + "] key should be equal.", expectedKeyVal.key().equals(actualKeyVal.key()));
 assertTrue("Form data at index [" + i + "] value should be equal.", expectedKeyVal.value().equals(actualKeyVal.value()));
 }
 }

 @Test
 public void testFormDataWithCheckboxOrRadioElement() {
 Element checkboxElement = new InputElement("checkbox1", "checkbox", null);
 checkboxElement.attr("checked", "checked");
 formElement.addElement(checkboxElement);
 List<Connection.KeyVal> expectedFormData = new ArrayList<>();
 expectedFormData.add(new Connection.KeyVal("checkbox1", ""));
 List<Connection.KeyVal> actualFormData = formElement.formData();
 assertEquals("Form data size should be equal.", expectedFormData.size(), actualFormData.size());
 for (int i = 0; i < expectedFormData.size(); i++) {
 Connection.KeyVal expectedKeyVal = expectedFormData.get(i);
 Connection.KeyVal actualKeyVal = actualFormData.get(i);
 assertTrue("Form data at index [" + i + "] key should be equal.", expectedKeyVal.key().equals(actualKeyVal.key()));
 assertTrue("Form data at index [" + i + "] value should be equal.", expectedKeyVal.value().equals(actualKeyVal.value()));
 }
 }

}