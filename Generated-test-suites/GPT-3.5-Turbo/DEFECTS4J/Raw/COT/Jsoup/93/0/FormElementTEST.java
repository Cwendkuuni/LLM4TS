import org.jsoup.*;
import org.jsoup.nodes.*;
import org.junit.*;
import org.junit.runner.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class FormElementTest {

  @Test
  public void testElements() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    assertNotNull(form.elements());
  }

  @Test
  public void testAddElement() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element element = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
    form.addElement(element);
    assertTrue(form.elements().contains(element));
  }

  @Test
  public void testSubmit() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Connection connection = form.submit();
    assertNotNull(connection);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSubmitWithInvalidAction() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    form.attr("action", ""); // Set an empty action to trigger IllegalArgumentException
    form.submit();
  }

  @Test
  public void testFormData() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    List<Connection.KeyVal> formData = form.formData();
    assertNotNull(formData);
  }

  @Test
  public void testFormDataWithCheckbox() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element checkbox = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
    checkbox.attr("type", "checkbox");
    checkbox.attr("name", "checkbox");
    checkbox.attr("checked", "");
    form.addElement(checkbox);
    List<Connection.KeyVal> formData = form.formData();
    assertNotNull(formData);
    assertEquals(1, formData.size());
    Connection.KeyVal keyVal = formData.get(0);
    assertEquals("checkbox", keyVal.key());
    assertEquals("on", keyVal.value());
  }

  @Test
  public void testFormDataWithRadio() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element radio = new Element(Tag.valueOf("input"), "http://www.example.com", new Attributes());
    radio.attr("type", "radio");
    radio.attr("name", "radio");
    radio.attr("checked", "");
    form.addElement(radio);
    List<Connection.KeyVal> formData = form.formData();
    assertNotNull(formData);
    assertEquals(1, formData.size());
    Connection.KeyVal keyVal = formData.get(0);
    assertEquals("radio", keyVal.key());
    assertEquals("on", keyVal.value());
  }

  @Test
  public void testFormDataWithSelect() {
    FormElement form = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element select = new Element(Tag.valueOf("select"), "http://www.example.com", new Attributes());
    Element option = new Element(Tag.valueOf("option"), "http://www.example.com", new Attributes());
    option.attr("selected", "");
    option.text("Option 1");
    select.appendChild(option);
    form.addElement(select);
    List<Connection.KeyVal> formData = form.formData();
    assertNotNull(formData);
    assertEquals(1, formData.size());
    Connection.KeyVal keyVal = formData.get(0);
    assertEquals("select", keyVal.key());
    assertEquals("Option 1", keyVal.value());
  }

}