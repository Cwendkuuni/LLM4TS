import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class FormElementTest {

 private FormElement formElement;
 private Connection connection;
 private Element formControlElement;

 @Before
 public void setUp() {
 formControlElement = mock(Element.class);
 connection = mock(Connection.class);
 formElement = new FormElement(Tag.FORM, "", new Attributes());
 }

 @Test
 public void testCreateNewFormElement() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetFormControlsAssociatedWithThisForm() {
 List<Element> formControls = Arrays.asList(formControlElement);
 formElement.addElement(formControlElement);
 List<Element> elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(formControlElement, elements.get(0));
 }

 @Test
 public void testAddFormControlElementToForm() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 Element formControlElement = mock(Element.class);
 formElement.addElement(formControlElement);
 List<Element> elements = formElement.elements();
 assertEquals(1, elements.size());
 assertEquals(formControlElement, elements.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyActionUrl() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 formElement.submit();
 }

 @Test
 public void testSubmitForm() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 formElement.addElement(formControlElement);
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataWithCheckboxAndRadio() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 Element checkboxElement = mock(Element.class);
 Element radioElement1 = mock(Element.class);
 Element radioElement2 = mock(Element.class);

 formElement.addElement(checkboxElement);
 formElement.addElement(radioElement1);
 formElement.addElement(radioElement2);
 checkboxElement.hasAttr(Mockito.eq("checked"));
 radioElement1.hasAttr(Mockito.eq("checked"));
 radioElement2.hasAttr(Mockito.eq("name"));
 radioElement2.hasAttr(Mockito.eq("value"));

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());

 String checkboxName = checkboxElement.attr("name");
 assertEquals(checkboxName, formData.get(0).key);
 String checkboxValue = checkboxElement.attr("value");
 assertEquals(checkboxValue, formData.get(0).value);

 String radioName1 = radioElement1.attr("name");
 assertEquals(radioName1, formData.get(1).key);
 String radioValue1 = radioElement1.attr("value");
 assertEquals(radioValue1, formData.get(1).value);

 String radioName2 = radioElement2.attr("name");
 assertEquals(radioName2, formData.get(2).key);
 }

 @Test
 public void testFormDataWithSelect() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 Element selectElement = mock(Element.class);
 Element optionElement1 = mock(Element.class);
 Element optionElement2 = mock(Element.class);
 Element optionElement3 = mock(Element.class);

 formElement.addElement(selectElement);
 selectElement.select("option[selected]").add(optionElement1);
 selectElement.select("option[selected]").add(optionElement2);
 selectElement.select("option").add(optionElement3);
 optionElement1.val("Option1");
 optionElement2.val("Option2");
 optionElement3.val("Option3");

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());

 String selectName = selectElement.attr("name");
 assertEquals(selectName, formData.get(0).key);
 String option1Value = optionElement1.val();
 assertEquals(option1Value, formData.get(1).value);
 String option2Value = optionElement2.val();
 assertEquals(option2Value, formData.get(2).value);
 }

 @Test
 public void testFormDataWithNoSelectedOption() {
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
 Element selectElement = mock(Element.class);
 Element optionElement1 = mock(Element.class);
 Element optionElement2 = mock(Element.class);

 formElement.addElement(selectElement);
 selectElement.select("option[selected]").add(optionElement1);
 selectElement.select("option").add(optionElement2);
 optionElement1.val("Option1");
 optionElement2.val("Option2");

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(2, formData.size());

 String selectName = selectElement.attr("name");
 assertEquals(selectName, formData.get(0).key);
 String option1Value = optionElement1.val();
 assertEquals(option1Value, formData.get(0).value);
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(Tag.FORM, "", new Attributes());
 FormElement formElement2 = new FormElement(Tag.FORM, "", new Attributes());
 assertEquals(formElement1, formElement1);
 assertEquals(formElement1, formElement2);
 FormElement formElement3 = new FormElement(Tag.FORM, "", new Attributes("data-test", "value"));
 assertNotEquals(formElement1, formElement3);
 }
}