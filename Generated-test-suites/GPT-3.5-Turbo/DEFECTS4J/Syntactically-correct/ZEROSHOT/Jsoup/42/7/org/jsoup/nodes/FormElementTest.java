import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
   @Test
   public void testElements() {
      FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
      Elements elements = formElement.elements();
      assertNotNull(elements);
   }
   
   @Test
   public void testAddElement() {
      FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
      Element element = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "inputElement"));
      FormElement updatedFormElement = formElement.addElement(element);
      assertEquals(1, updatedFormElement.elements().size());
      assertEquals(element, updatedFormElement.elements().get(0));
   }
   
   @Test
   public void testSubmit() {
      FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes().put("action", "/submit"));
      Connection connection = formElement.submit();
      assertNotNull(connection);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testSubmitWithoutBaseUri() {
      FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes().put("action", "/submit"));
      formElement.submit();
   }
   
   @Test
   public void testFormData() {
      FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
      Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "inputElement").put("value", "123"));
      formElement.addElement(inputElement);
      
      List<Connection.KeyVal> formData = formElement.formData();
      assertNotNull(formData);
      assertEquals(1, formData.size());
      assertEquals("inputElement", formData.get(0).key());
      assertEquals("123", formData.get(0).value());
   }
   
   @Test
   public void testEquals() {
      FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
      FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
      
      assertTrue(formElement1.equals(formElement2));
      assertTrue(formElement2.equals(formElement1));
   }
}