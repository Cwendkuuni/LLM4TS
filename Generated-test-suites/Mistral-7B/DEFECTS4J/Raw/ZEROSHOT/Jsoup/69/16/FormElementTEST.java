import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 FormElement formElement = new FormElement(new Tag("form"), "http://localhost:8080/", new Attributes());
 assertNotNull(formElement);
 }

 @Test
 public void testGetElements() {
 FormElement formElement = new FormElement(new Tag("form"), "http://localhost:8080/", new Attributes());
 Element element1 = new Element("input");
 Element element2 = new Element("textarea");
 formElement.addElement(element1);
 formElement.addElement(element2);

 assertEquals(2, formElement.elements().size());
 }

 @Test
 public void testSubmit() {
 FormElement formElement = new FormElement(new Tag("form"), "http://localhost:8080/", new Attributes());
 String baseUri = "http://localhost:8080/";
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
 FormElement formElement = new FormElement(new Tag("form"), "http://localhost:8080/", new Attributes());
 Element input1 = new Element("input");
 input1.attr("name", "name1");
 input1.attr("value", "value1");
 Element input2 = new Element("input");
 input2.attr("name", "name2");
 input2.attr("value", "value2");
 Element selectElement = new Element("select");
 Element option1 = new Element("option").text("option1");
 option1.attr("value", "value1");
 selectElement.add(option1);
 Element option2 = new Element("option").text("option2");
 option2.attr("value", "value2");
 selectElement.add(option2);
 selectElement.attr("name", "name3");
 selectElement.attr("selected", "selected");
 formElement.addElement(input1);
 formElement.addElement(input2);
 formElement.addElement(selectElement);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 assertEquals("name1", formData.get(0).key);
 assertEquals("value1", formData.get(0).value);
 assertEquals("name2", formData.get(1).key);
 assertEquals("value2", formData.get(1).value);
 assertEquals("name3", formData.get(2).key);
 // selected option value
 assertEquals("value1", formData.get(2).value);
 }

 @Test
 public void testFormDataEmpty() {
 FormElement formElement = new FormElement(new Tag("form"), "http://localhost:8080/", new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertNull(formData);
 }
}