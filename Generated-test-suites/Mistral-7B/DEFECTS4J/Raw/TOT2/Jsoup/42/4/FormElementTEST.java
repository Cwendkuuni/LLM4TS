package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.Connection;
import org.jsoup.nodes.*;
import org.junit.Test;

public class FormElementTest {

   @Test
   public void testElementsMethod() {
      Tag formTag = new Tag("form");
      Attributes formAttributes = new Attributes();
      FormElement formElement = new FormElement(formTag, "", formAttributes);
      Element inputElement = new Element("input");
      Element textAreaElement = new Element("textarea");
      formElement.addElement(inputElement);
      formElement.addElement(textAreaElement);

      Elements formControls = formElement.elements();

      assertEquals(2, formControls.size());
      assertTrue(formControls.contains(inputElement));
      assertTrue(formControls.contains(textAreaElement));
   }

   @Test
   public void testAddElementMethod() {
      Tag formTag = new Tag("form");
      Attributes formAttributes = new Attributes();
      FormElement formElement = new FormElement(formTag, "", formAttributes);

      Element inputElement = new Element("input");
      FormElement formElementWithInput = formElement.addElement(inputElement);

      assertEquals(1, formElementWithInput.elements().size());
      assertTrue(formElementWithInput.elements().contains(inputElement));
   }

   @Test
   public void testSubmitMethod() {
      Tag formTag = new Tag("form");
      Attributes formAttributes = new Attributes();
      FormElement formElement = new FormElement(formTag, "", formAttributes);

      Connection connection = formElement.submit();
      assertNotNull(connection);
   }

   @Test
   public void testFormDataMethod() {
      Tag formTag = new Tag("form");
      Attributes formAttributes = new Attributes();

      // input element
      Element inputElement = new Element("input");
      inputElement.attr("name", "name1");
      inputElement.attr("value", "value1");
      formAttributes.put("name", "formName");
      formAttributes.put("action", "http://example.com");

      // select element
      Element selectElement = new Element("select");
      Element optionElement1 = new Element("option");
      optionElement1.attr("value", "value2");
      optionElement1.attr("selected", "selected");
      Element optionElement2 = new Element("option");
      optionElement2.attr("value", "value3");
      selectElement.add(optionElement1);
      selectElement.add(optionElement2);

      FormElement formElement = new FormElement(formTag, "", formAttributes);
      formElement.addElement(inputElement);
      formElement.addElement(selectElement);

      List<Connection.KeyVal> formData = formElement.formData();
      assertNotNull(formData);
      assertEquals(2, formData.size());
      assertEquals("name1", formData.get(0).key);
      assertEquals("value1", formData.get(0).value);
      assertEquals("formName", formData.get(1).key);
      assertEquals("value2", formData.get(1).value);
   }
}