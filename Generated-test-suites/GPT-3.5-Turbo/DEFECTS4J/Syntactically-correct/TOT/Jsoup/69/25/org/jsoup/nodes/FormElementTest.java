import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.Connection;

public class FormElementTest {

  private FormElement formElement;
  private Elements elements;
  
  @Before
  public void setup() {
    formElement = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    elements = new Elements();
  }
  
  @Test
  public void testElements() {
    assertNotNull(formElement.elements());
  }
  
  @Test
  public void testAddElement() {
    assertEquals(0, formElement.elements().size());
    
    formElement.addElement(new Element(Tag.input, "http://www.example.com", new Attributes()));
    
    assertEquals(1, formElement.elements().size());
  }
  
  @Test
  public void testSubmit() {
    Connection connection = formElement.submit();
    
    assertNotNull(connection);
    assertEquals("http://www.example.com", connection.request().url());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSubmitInvalidAction() {
    formElement.attr("action", "");
    formElement.submit();
  }
  
  @Test
  public void testFormData() {
    FormElement form = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    form.addElement(new Element(Tag.input, "", new Attributes().put("name", "username").put("value", "John")));
    
    List<Connection.KeyVal> data = form.formData();
    
    assertEquals(1, data.size());
    assertEquals("username", data.get(0).key());
    assertEquals("John", data.get(0).value());
  }
  
  @Test
  public void testFormDataDisabledElement() {
    FormElement form = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    Element input = new Element(Tag.input, "", new Attributes().put("name", "username").put("value", "John").put("disabled", ""));
    form.addElement(input);
    
    List<Connection.KeyVal> data = form.formData();
    
    assertEquals(0, data.size());
  }
  
  @Test
  public void testFormDataEmptyName() {
    FormElement form = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    form.addElement(new Element(Tag.input, "", new Attributes().put("name", "").put("value", "John")));
    
    List<Connection.KeyVal> data = form.formData();
    
    assertEquals(0, data.size());
  }
  
  @Test
  public void testFormDataSelect() {
    FormElement form = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    Element select = new Element(Tag.select, "", new Attributes().put("name", "countries"));
    Element option1 = new Element(Tag.option, "", new Attributes().put("value", "USA"));
    Element option2 = new Element(Tag.option, "", new Attributes().put("value", "UK").put("selected", ""));
    select.appendChild(option1);
    select.appendChild(option2);
    form.addElement(select);
    
    List<Connection.KeyVal> data = form.formData();
    
    assertEquals(1, data.size());
    assertEquals("countries", data.get(0).key());
    assertEquals("UK", data.get(0).value());
  }
  
  @Test
  public void testFormDataEmptySelect() {
    FormElement form = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    Element select = new Element(Tag.select, "", new Attributes().put("name", "countries"));
    form.addElement(select);
    
    List<Connection.KeyVal> data = form.formData();
    
    assertEquals(0, data.size());
  }
  
  @Test
  public void testFormDataCheckboxChecked() {
    FormElement form = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    Element checkbox = new Element(Tag.input, "", new Attributes().put("type", "checkbox").put("name", "agree").put("checked", ""));
    form.addElement(checkbox);
    
    List<Connection.KeyVal> data = form.formData();
    
    assertEquals(1, data.size());
    assertEquals("agree", data.get(0).key());
    assertEquals("on", data.get(0).value());
  }
  
  @Test
  public void testFormDataCheckboxUnchecked() {
    FormElement form = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    Element checkbox = new Element(Tag.input, "", new Attributes().put("type", "checkbox").put("name", "agree"));
    form.addElement(checkbox);
    
    List<Connection.KeyVal> data = form.formData();
    
    assertEquals(0, data.size());
  }
  
  @Test
  public void testFormDataRadioChecked() {
    FormElement form = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    Element radio = new Element(Tag.input, "", new Attributes().put("type", "radio").put("name", "gender").put("checked", "").put("value", "male"));
    form.addElement(radio);
    
    List<Connection.KeyVal> data = form.formData();
    
    assertEquals(1, data.size());
    assertEquals("gender", data.get(0).key());
    assertEquals("male", data.get(0).value());
  }
  
  @Test
  public void testFormDataRadioUnchecked() {
    FormElement form = new FormElement(Tag.form, "http://www.example.com", new Attributes());
    Element radio = new Element(Tag.input, "", new Attributes().put("type", "radio").put("name", "gender").put("value", "male"));
    form.addElement(radio);
    
    List<Connection.KeyVal> data = form.formData();
    
    assertEquals(0, data.size());
  }
}