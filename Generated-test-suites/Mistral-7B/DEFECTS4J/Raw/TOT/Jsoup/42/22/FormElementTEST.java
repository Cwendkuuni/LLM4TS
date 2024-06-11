import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

@Test
public void testFormElementCreation() {
FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
assertNotNull(formElement);
}

@Test
public void testGetElements() {
FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
assertNotNull(formElement.elements());
}

@Test
public void testAddElement() {
FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
Element inputElement = new Element("input", new Attributes().put("name", "username"));
formElement.addElement(inputElement);
assertTrue(formElement.elements().size() > 0);
assertTrue(formElement.elements().get(0).equals(inputElement));
}

@Test
public void testSubmitFormWithValidActionUrl() {
FormElement formElement = new FormElement(new Tag("form", "action", "/submit"), "", new Attributes());
Connection connection = formElement.submit();
assertNotNull(connection);
}

@Test(expected = IllegalArgumentException.class)
public void testSubmitFormWithoutActionUrl() {
FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
formElement.submit();
}

@Test
public void testFormDataWithTextInputs() {
FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
Element inputElement1 = new Element("input", new Attributes().put("name", "username").put("value", "John"));
Element inputElement2 = new Element("input", new Attributes().put("name", "password").put("value", "password123"));
formElement.addElement(inputElement1);
formElement.addElement(inputElement2);
List<Connection.KeyVal> formData = formElement.formData();
assertEquals(2, formData.size());
assertEquals("username=John", formData.get(0).toString());
assertEquals("password=password123", formData.get(1).toString());
}

@Test
public void testFormDataWithSelectElement() {
FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
Element selectElement = new Element("select", new Attributes());
Element optionElement1 = new Element("option", new Attributes().put("value", "apple"), new Attributes().put("selected", "selected"));
Element optionElement2 = new Element("option", new Attributes().put("value", "orange"));
selectElement.add(optionElement1);
selectElement.add(optionElement2);
formElement.addElement(selectElement);
List<Connection.KeyVal> formData = formElement.formData();
assertEquals(1, formData.size());
assertEquals("apple", formData.get(0).getValue());
}

@Test
public void testFormDataWithCheckboxOrRadioElements() {
FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
Element checkboxElement1 = new Element("input", new Attributes().put("name", "agreeToTerms").put("type", "checkbox"));
Element checkboxElement2 = new Element("input", new Attributes().put("name", "agreeToTerms").put("type", "checkbox"));
formElement.addElement(checkboxElement1);
formElement.addElement(checkboxElement2);
checkboxElement1.attr("checked", "checked");
List<Connection.KeyVal> formData = formElement.formData();
assertTrue(formData.size() > 0);
assertTrue(formData.stream().anyMatch(keyVal -> keyVal.getName().equals("agreeToTerms")));
}

@Test
public void testEquals() {
FormElement formElement1 = new FormElement(new Tag("form"), "", new Attributes());
FormElement formElement2 = new FormElement(new Tag("form"), "", new Attributes());
assertFalse(formElement1.equals(formElement2));
FormElement formElement3 = new FormElement(new Tag("form"), "", new Attributes());
formElement3.addElement(new Element("input", new Attributes().put("name", "username")));
formElement1.addElement(new Element("input", new Attributes().put("name", "username")));
assertTrue(formElement1.equals(formElement3));
}
}