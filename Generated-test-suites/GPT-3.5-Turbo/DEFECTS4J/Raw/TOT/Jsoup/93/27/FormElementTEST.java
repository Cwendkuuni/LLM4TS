import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class FormElementTest {
  
  private FormElement formElement;

  @Before
  public void setUp() {
    formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
  }
  
  @Test
  public void testElements_emptyForm_noElementsReturned() {
    Elements elements = formElement.elements();
    assertNotNull(elements);
    assertEquals(0, elements.size());
  }
  
  @Test
  public void testAddElement_addFormControl_newElementAdded() {
    Element inputElement = new Element(Tag.valueOf("input"), "");
    formElement.addElement(inputElement);
    Elements elements = formElement.elements();
    assertNotNull(elements);
    assertEquals(1, elements.size());
    assertTrue(elements.contains(inputElement));
  }
  
  @Test
  public void testRemoveChild_removeElementFromForm_elementRemoved() {
    Element inputElement = new Element(Tag.valueOf("input"), "");
    formElement.addElement(inputElement);
    formElement.removeChild(inputElement);
    Elements elements = formElement.elements();
    assertNotNull(elements);
    assertEquals(0, elements.size());
    assertFalse(elements.contains(inputElement));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSubmit_missingActionAttribute_throwIllegalArgumentException() {
    formElement.submit();
  }
  
  @Test
  public void testSubmit_validForm_submitConnectionPrepared() {
    Element inputElement = new Element(Tag.valueOf("input"), "");
    inputElement.attr("type", "text");
    inputElement.attr("name", "username");
    inputElement.val("testuser");
    formElement.addElement(inputElement);
    Connection connection = formElement.submit();
    assertNotNull(connection);
    assertEquals("POST", connection.request().method());
    assertEquals("", connection.request().data());
    assertTrue(connection.request().header("User-Agent").contains("Mozilla"));
    assertTrue(connection.request().header("Content-Type").contains("application/x-www-form-urlencoded"));
    assertTrue(connection.request().url().contains("action"));
  }
  
  @Test
  public void testFormData_noFormControls_emptyFormDataReturned() {
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(0, formData.size());
  }
  
  @Test
  public void testFormData_selectFormControl_noSelectedOptions_defaultOptionAddedToFormData() {
    Element selectElement = new Element(Tag.valueOf("select"), "");
    selectElement.attr("name", "gender");
    Element optionElement = new Element(Tag.valueOf("option"), "");
    optionElement.attr("value", "male");
    selectElement.appendChild(optionElement);
    formElement.addElement(selectElement);
    
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(1, formData.size());
    
    Connection.KeyVal keyVal = formData.get(0);
    assertEquals("gender", keyVal.key());
    assertEquals("male", keyVal.value());
  }
  
  @Test
  public void testFormData_selectFormControl_selectedOptionPresent_selectedOptionAddedToFormData() {
    Element selectElement = new Element(Tag.valueOf("select"), "");
    selectElement.attr("name", "gender");
    Element optionElement1 = new Element(Tag.valueOf("option"), "");
    optionElement1.attr("value", "male");
    selectElement.appendChild(optionElement1);
    
    Element optionElement2 = new Element(Tag.valueOf("option"), "");
    optionElement2.attr("value", "female");
    optionElement2.attr("selected", "");
    selectElement.appendChild(optionElement2);
    
    Element optionElement3 = new Element(Tag.valueOf("option"), "");
    optionElement3.attr("value", "other");
    selectElement.appendChild(optionElement3);
    
    formElement.addElement(selectElement);
    
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(1, formData.size());
    
    Connection.KeyVal keyVal = formData.get(0);
    assertEquals("gender", keyVal.key());
    assertEquals("female", keyVal.value());
  }
  
  @Test
  public void testFormData_checkboxFormControl_checked_checkboxValueAddedToFormData() {
    Element checkboxElement = new Element(Tag.valueOf("input"), "");
    checkboxElement.attr("type", "checkbox");
    checkboxElement.attr("name", "agree");
    checkboxElement.attr("checked", "");
    formElement.addElement(checkboxElement);
    
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(1, formData.size());
    
    Connection.KeyVal keyVal = formData.get(0);
    assertEquals("agree", keyVal.key());
    assertEquals("on", keyVal.value());
  }
  
  @Test
  public void testFormData_checkboxFormControl_unchecked_noDataAddedToFormData() {
    Element checkboxElement = new Element(Tag.valueOf("input"), "");
    checkboxElement.attr("type", "checkbox");
    checkboxElement.attr("name", "agree");
    formElement.addElement(checkboxElement);
    
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(0, formData.size());
  }
  
  @Test
  public void testFormData_radioFormControl_checked_radioValueAddedToFormData() {
    Element radioElement = new Element(Tag.valueOf("input"), "");
    radioElement.attr("type", "radio");
    radioElement.attr("name", "gender");
    radioElement.attr("value", "male");
    radioElement.attr("checked", "");
    formElement.addElement(radioElement);
    
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(1, formData.size());
    
    Connection.KeyVal keyVal = formData.get(0);
    assertEquals("gender", keyVal.key());
    assertEquals("male", keyVal.value());
  }
  
  @Test
  public void testFormData_radioFormControl_unchecked_noDataAddedToFormData() {
    Element radioElement = new Element(Tag.valueOf("input"), "");
    radioElement.attr("type", "radio");
    radioElement.attr("name", "gender");
    radioElement.attr("value", "male");
    formElement.addElement(radioElement);
    
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(0, formData.size());
  }
  
  @Test
  public void testFormData_textFormControl_textValueAddedToFormData() {
    Element inputElement = new Element(Tag.valueOf("input"), "");
    inputElement.attr("type", "text");
    inputElement.attr("name", "username");
    inputElement.val("testuser");
    formElement.addElement(inputElement);
    
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(1, formData.size());
    
    Connection.KeyVal keyVal = formData.get(0);
    assertEquals("username", keyVal.key());
    assertEquals("testuser", keyVal.value());
  }
  
  @Test
  public void testFormData_disabledFormControl_noDataAddedToFormData() {
    Element inputElement = new Element(Tag.valueOf("input"), "");
    inputElement.attr("type", "text");
    inputElement.attr("name", "username");
    inputElement.attr("disabled", "");
    inputElement.val("testuser");
    formElement.addElement(inputElement);
    
    List<Connection.KeyVal> formData = formElement.formData();
    assertNotNull(formData);
    assertEquals(0, formData.size());
  }
}