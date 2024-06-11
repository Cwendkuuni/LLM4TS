import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

  @Test
  public void testElements() {
    // Create a new FormElement object
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    // Add some form control elements to the form
    Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri());
    formElement.addElement(element1);

    Element element2 = new Element(Tag.valueOf("input"), formElement.baseUri());
    formElement.addElement(element2);

    // Test if the elements method returns the correct form controls
    Elements elements = formElement.elements();
    Assert.assertEquals(2, elements.size());
    Assert.assertTrue(elements.contains(element1));
    Assert.assertTrue(elements.contains(element2));
  }

  @Test
  public void testAddElement() {
    // Create a new FormElement object
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    // Create a form control element
    Element element = new Element(Tag.valueOf("input"), formElement.baseUri());

    // Add the form control element to the form
    formElement.addElement(element);

    // Test if the form control element is added correctly
    Elements elements = formElement.elements();
    Assert.assertEquals(1, elements.size());
    Assert.assertTrue(elements.contains(element));
  }

  @Test
  public void testRemoveChild() {
    // Create a new FormElement object
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    // Create a form control element
    Element element = new Element(Tag.valueOf("input"), formElement.baseUri());

    // Add the form control element to the form
    formElement.addElement(element);

    // Test if the removeChild method removes the form control element correctly
    formElement.removeChild(element);
    Elements elements = formElement.elements();
    Assert.assertEquals(0, elements.size());
  }

  @Test
  public void testSubmit() {
    // Create a new FormElement object
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    // Set the action attribute of the form
    formElement.attr("action", "http://example.com/submit");

    // Set the method attribute of the form
    formElement.attr("method", "POST");

    // Test if the submit method returns a connection object with the correct URL and method
    Connection connection = formElement.submit();
    Assert.assertEquals("http://example.com/submit", connection.request().url().toString());
    Assert.assertEquals(Connection.Method.POST, connection.request().method());
  }

  @Test
  public void testFormData() {
    // Create a new FormElement object
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

    // Create some form control elements
    Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri());
    element1.attr("name", "input1");
    element1.val("value1");
    formElement.addElement(element1);

    Element element2 = new Element(Tag.valueOf("select"), formElement.baseUri());
    Element option1 = new Element(Tag.valueOf("option"), formElement.baseUri());
    option1.attr("selected", "selected");
    option1.val("option1");
    element2.appendChild(option1);
    formElement.addElement(element2);

    Element element3 = new Element(Tag.valueOf("input"), formElement.baseUri());
    element3.attr("name", "input2");
    element3.attr("type", "checkbox");
    element3.attr("checked", "checked");
    formElement.addElement(element3);

    // Test if the formData method returns the correct form data
    List<Connection.KeyVal> formData = formElement.formData();
    Assert.assertEquals(3, formData.size());
    Assert.assertEquals("input1", formData.get(0).key());
    Assert.assertEquals("value1", formData.get(0).value());
    Assert.assertEquals("select", formData.get(1).key());
    Assert.assertEquals("option1", formData.get(1).value());
    Assert.assertEquals("input2", formData.get(2).key());
    Assert.assertEquals("on", formData.get(2).value());
  }

}