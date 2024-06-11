
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

  @Test
  public void testElements() {
    Tag tag = Tag.valueOf("form");
    String baseUri = "http://example.com";
    Attributes attributes = new Attributes();
    FormElement formElement = new FormElement(tag, baseUri, attributes);
    Elements elements = formElement.elements();
    assertNotNull(elements);
    assertEquals(0, elements.size());
  }

  @Test
  public void testAddElement() {
    Tag tag = Tag.valueOf("form");
    String baseUri = "http://example.com";
    Attributes attributes = new Attributes();
    FormElement formElement = new FormElement(tag, baseUri, attributes);
    Element element = new Element(Tag.valueOf("input"), baseUri);
    formElement.addElement(element);
    Elements elements = formElement.elements();
    assertNotNull(elements);
    assertEquals(1, elements.size());
    assertEquals(element, elements.get(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSubmitWithNoBaseUri() {
    Tag tag = Tag.valueOf("form");
    Attributes attributes = new Attributes();
    FormElement formElement = new FormElement(tag, null, attributes);
    formElement.submit();
  }

  @Test
  public void testSubmitWithAction() {
    Tag tag = Tag.valueOf("form");
    Attributes attributes = new Attributes();
    attributes.put("action", "http://example.com/submit");
    FormElement formElement = new FormElement(tag, "http://example.com", attributes);
    Connection connection = formElement.submit();
    assertNotNull(connection);
    assertEquals("POST", connection.request().method());
    assertEquals("http://example.com/submit", connection.request().url().toString());
  }

  @Test
  public void testSubmitWithoutAction() {
    Tag tag = Tag.valueOf("form");
    Attributes attributes = new Attributes();
    FormElement formElement = new FormElement(tag, "http://example.com", attributes);
    Connection connection = formElement.submit();
    assertNotNull(connection);
    assertEquals("GET", connection.request().method());
    assertEquals("http://example.com", connection.request().url().toString());
  }

  @Test
  public void testFormData() {
    Tag tag = Tag.valueOf("form");
    String baseUri = "http://example.com";
    Attributes attributes = new Attributes();
    FormElement formElement = new FormElement(tag, baseUri, attributes);
    Element element1 = new Element(Tag.valueOf("input"), baseUri);
    element1.attr("name", "username");
    element1.val("John Doe");
    formElement.addElement(element1);
    Element element2 = new Element(Tag.valueOf("input"), baseUri);
    element2.attr("name", "password");
    element2.val("password123");
    formElement.addElement(element2);
    
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(2, formData.size());
    assertEquals("username", formData.get(0).key());
    assertEquals("John Doe", formData.get(0).value());
    assertEquals("password", formData.get(1).key());
    assertEquals("password123", formData.get(1).value());
}
}

