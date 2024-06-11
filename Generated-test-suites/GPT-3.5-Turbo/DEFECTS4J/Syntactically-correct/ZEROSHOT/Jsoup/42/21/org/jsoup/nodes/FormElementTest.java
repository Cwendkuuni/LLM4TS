import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FormElementTest {
  
  private FormElement formElement;
  
  @Before
  public void setup() {
    Tag tag = Tag.valueOf("form");
    String baseUri = "http://example.com";
    Attributes attributes = new Attributes();
    formElement = new FormElement(tag, baseUri, attributes);
  }
  
  @Test
  public void testElements() {
    Elements elements = formElement.elements();
    assertNotNull(elements);
  }
  
  @Test
  public void testAddElement() {
    Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
    FormElement result = formElement.addElement(element);
    assertEquals(result, formElement);
  }
  
  @Test
  public void testSubmit() {
    Connection connection = formElement.submit();
    assertNotNull(connection);
  }
  
  @Test
  public void testFormData() {
    List<Connection.KeyVal> data = formElement.formData();
    assertEquals(0, data.size());
  }
  
  @Test
  public void testEquals() {
    FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    assertTrue(formElement.equals(formElement2));
  }
}