import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
  
  private FormElement formElement;
  
  @Before
  public void setup() {
    Tag tag = Tag.valueOf("form");
    String baseUri = "https://example.com";
    Attributes attributes = new Attributes();
    
    formElement = new FormElement(tag, baseUri, attributes);
  }
  
  @Test
  public void testElements() {
    Elements elements = formElement.elements();
    assertNotNull(elements);
    assertEquals(0, elements.size());
  }
  
  @Test
  public void testAddElement() {
    Element element = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
    FormElement result = formElement.addElement(element);
    assertNotNull(result);
    assertEquals(1, result.elements().size());
    assertEquals(element, result.elements().first());
  }
  
  @Test
  public void testSubmit() {
    Connection connection = formElement.submit();
    assertNotNull(connection);
  }
  
  @Test
  public void testFormData() {
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(0, formData.size());
  }
  
  @Test
  public void testEquals() {
    FormElement anotherFormElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
    assertTrue(formElement.equals(anotherFormElement));
  }
  
}