import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import org.junit.Test;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertSame(formElement, elements.parent());
 }

 @Test
 public void testAddElement() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element inputElement = new Elements(new Tag("input")).first();
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(1, elements.size());
 assertSame(inputElement, elements.first());
 }

 @Test
 public void testSubmit() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("action", "http://example.com"));
 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithEmptyAction() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormData() {
 FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
 Element inputElement1 = new Elements(new Tag("input")).first();
 inputElement1.attr("name", "input1");
 inputElement1.attr("type", "text");
 inputElement1.attr("value", "value1");
 formElement.addElement(inputElement1);

 Element selectElement = new Elements(new Tag("select"))
 .attr("name", "select1")
 .add(new Elements(new Tag("option").attr("value", "option1")))
 .add(new Elements(new Tag("option").attr("value", "option2")))
 .first();
 formElement.addElement(selectElement);
 formElement.select("input[name='select1'] option:checked").first().attr("selected", true);
 formElement.addElement(new Elements(new Tag("input")).attr("type", "submit").first());

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 assertEquals("input1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 assertEquals("select1", formData.get(1).key);
 assertEquals("option1", formData.get(1).value);
 assertEquals("", formData.get(2).key); // empty text fields have empty names
 assertNull(formData.get(2).value);
 }

 @Test
 public void testEquals() {
 FormElement formElement1 = new FormElement(new Tag("form"), "", new Attributes());
 FormElement formElement2 = new FormElement(new Tag("form"), "", new Attributes());
 assertFalse(formElement1.equals(formElement2));

 FormElement formElement3 = new FormElement(new Tag("form"), "", new Attributes());
 formElement3.addElement(new Elements(new Tag("input")).first());
 assertFalse(formElement1.equals(formElement3));

 FormElement formElement4 = new FormElement(new Tag("form"), "", new Attributes());
 formElement4.addElement(new Elements(new Tag("input")).first());
 Element inputElement = new Elements(new Tag("input")).first();
 formElement4.addElement(inputElement);
 assertSame(inputElement, formElement4.elements().first());
 assertTrue(formElement1.equals(formElement4));
}

}