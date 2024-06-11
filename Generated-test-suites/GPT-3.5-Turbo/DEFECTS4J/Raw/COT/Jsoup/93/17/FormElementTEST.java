
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
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Elements elements = formElement.elements();
    assertNotNull(elements);
  }
  
  @Test
  public void testAddElement() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com");
    FormElement result = formElement.addElement(inputElement);
    assertNotNull(result);
    Elements elements = formElement.elements();
    assertEquals(1, elements.size());
    assertEquals(inputElement, elements.get(0));
  }
  
  @Test
  public void testRemoveChild() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com");
    FormElement result = formElement.addElement(inputElement);
    assertNotNull(result);
    Elements elements = formElement.elements();
    assertEquals(1, elements.size());
    formElement.removeChild(inputElement);
    assertEquals(0, elements.size());
  }
  
  @Test
  public void testSubmit() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Connection connection = formElement.submit();
    assertNotNull(connection);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSubmitWithEmptyAction() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    formElement.attr("action", "");
    formElement.submit();
  }
  
  @Test
  public void testFormData() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertTrue(data.isEmpty());
  }
  
  @Test
  public void testFormDataWithInputElement() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com");
    inputElement.attr("name", "inputName");
    inputElement.val("inputValue");
    formElement.addElement(inputElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertEquals(1, data.size());
    Connection.KeyVal keyVal = data.get(0);
    assertEquals("inputName", keyVal.key());
    assertEquals("inputValue", keyVal.value());
  }
  
  @Test
  public void testFormDataWithDisabledInputElement() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element inputElement = new Element(Tag.valueOf("input"), "http://www.example.com");
    inputElement.attr("name", "inputName");
    inputElement.attr("disabled", "");
    inputElement.val("inputValue");
    formElement.addElement(inputElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertTrue(data.isEmpty());
  }
  
  @Test
  public void testFormDataWithSelectElementNoOptionSelected() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com");
    selectElement.attr("name", "selectName");
    formElement.addElement(selectElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertTrue(data.isEmpty());
  }
  
  @Test
  public void testFormDataWithSelectElementOptionSelected() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com");
    selectElement.attr("name", "selectName");
    Element optionElement = new Element(Tag.valueOf("option"), "http://www.example.com");
    optionElement.attr("selected", "");
    optionElement.val("optionValue");
    selectElement.appendChild(optionElement);
    formElement.addElement(selectElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertEquals(1, data.size());
    Connection.KeyVal keyVal = data.get(0);
    assertEquals("selectName", keyVal.key());
    assertEquals("optionValue", keyVal.value());
  }
  
  @Test
  public void testFormDataWithSelectElementMultipleOptionsSelected() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element selectElement = new Element(Tag.valueOf("select"), "http://www.example.com");
    selectElement.attr("name", "selectName");
    Element optionElement1 = new Element(Tag.valueOf("option"), "http://www.example.com");
    optionElement1.attr("selected", "");
    optionElement1.val("optionValue1");
    selectElement.appendChild(optionElement1);
    Element optionElement2 = new Element(Tag.valueOf("option"), "http://www.example.com");
    optionElement2.attr("selected", "");
    optionElement2.val("optionValue2");
    selectElement.appendChild(optionElement2);
    formElement.addElement(selectElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertEquals(2, data.size());
    Connection.KeyVal keyVal1 = data.get(0);
    assertEquals("selectName", keyVal1.key());
    assertEquals("optionValue1", keyVal1.value());
    Connection.KeyVal keyVal2 = data.get(1);
    assertEquals("selectName", keyVal2.key());
    assertEquals("optionValue2", keyVal2.value());
  }
  
  @Test
  public void testFormDataWithCheckboxElementChecked() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element checkboxElement = new Element(Tag.valueOf("input"), "http://www.example.com");
    checkboxElement.attr("type", "checkbox");
    checkboxElement.attr("name", "checkboxName");
    checkboxElement.attr("checked", "");
    checkboxElement.val("checkboxValue");
    formElement.addElement(checkboxElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertEquals(1, data.size());
    Connection.KeyVal keyVal = data.get(0);
    assertEquals("checkboxName", keyVal.key());
    assertEquals("checkboxValue", keyVal.value());
  }
  
  @Test
  public void testFormDataWithCheckboxElementUnchecked() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element checkboxElement = new Element(Tag.valueOf("input"), "http://www.example.com");
    checkboxElement.attr("type", "checkbox");
    checkboxElement.attr("name", "checkboxName");
    checkboxElement.val("checkboxValue");
    formElement.addElement(checkboxElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertTrue(data.isEmpty());
  }
  
  @Test
  public void testFormDataWithRadioButtonElementChecked() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element radioElement = new Element(Tag.valueOf("input"), "http://www.example.com");
    radioElement.attr("type", "radio");
    radioElement.attr("name", "radioName");
    radioElement.attr("checked", "");
    radioElement.val("radioValue");
    formElement.addElement(radioElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertEquals(1, data.size());
    Connection.KeyVal keyVal = data.get(0);
    assertEquals("radioName", keyVal.key());
    assertEquals("radioValue", keyVal.value());
  }
  
  @Test
  public void testFormDataWithRadioButtonElementUnchecked() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element radioElement = new Element(Tag.valueOf("input"), "http://www.example.com");
    radioElement.attr("type", "radio");
    radioElement.attr("name", "radioName");
    radioElement.val("radioValue");
    formElement.addElement(radioElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertTrue(data.isEmpty());
  }
  
  @Test
  public void testFormDataWithOtherElement() {
    FormElement formElement = new FormElement(Tag.valueOf("form"), "http://www.example.com", new Attributes());
    Element otherElement = new Element(Tag.valueOf("div"), "http://www.example.com");
    otherElement.attr("name", "otherName");
    otherElement.val("otherValue");
    formElement.addElement(otherElement);
    List<Connection.KeyVal> data = formElement.formData();
    assertNotNull(data);
    assertEquals(1, data.size());
    Connection.KeyVal keyVal = data.get(0);
    assertEquals("otherName", keyVal.key());
    assertEquals("otherValue", keyVal.value());
  }
}

