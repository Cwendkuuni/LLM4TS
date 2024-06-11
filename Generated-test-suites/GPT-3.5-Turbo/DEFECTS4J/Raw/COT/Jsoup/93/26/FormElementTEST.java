
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
  
  @Test
  public void testElements() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
    Elements elements = formElement.elements();
    assertNotNull(elements);
    assertEquals(0, elements.size());
  }

  @Test
  public void testAddElement() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
    Element element = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
    FormElement result = formElement.addElement(element);
    assertNotNull(result);
    assertEquals(1, formElement.elements().size());
  }

  @Test
  public void testSubmit() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
    Connection connection = formElement.submit();
    assertNotNull(connection);
    assertEquals("https://example.com", connection.request().url().toString());
    assertEquals(Connection.Method.POST, connection.request().method());
  }

  @Test
  public void testFormData() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(0, formData.size());
  }
}
