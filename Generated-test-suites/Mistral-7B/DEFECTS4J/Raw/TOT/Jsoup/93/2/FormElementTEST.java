import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

 private FormElement form;

 @Before
 public void setup() {
 String html = "<html><head></head><body><form action='/' method='POST' >" +
 " <input name='text' type='text' />" +
 " <select name='select' ><option value='option1'>Option 1</option>" +
 " <option value='option2' selected>Option 2</option></select>" +
 " <input name='check' type='checkbox' checked />" +
 " <input name='radio' type='radio' checked />" +
 " <input name='radio' type='radio' />" +
 " </form></body></html>";
 document = Jsoup.parse(html);
 form = document.select("form").first();
 }

 @Test
 public void testFormElementCreation() {
 FormElement expectedForm = form;
 assertNotNull(expectedForm);
 Equals expectedEq = new Equals();
 assertEquals(expectedEq, expectedForm);
 }

 @Test
 public void testElementsMethod() {
 Elements elements = form.elements();
 assertEquals(5, elements.size());
 for (Element el : elements) {
 if (el.tagName().equals("input")) {
 assertNotNull(el.attr("name"));
 }
 }
 }

 @Test
 public void testAddElementMethod() {
 Element newInput = new Element(new Tag("input"));
 newInput.attr("name", "newInput");
 FormElement formWithNewInput = form.addElement(newInput);
 Elements elements = formWithNewInput.elements();
 assertEquals(6, elements.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitMethodWithoutActionAttribute() {
 form.submit();
 }

 @Test
 public void testFormDataMethodTextInput() {
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(5, formData.size());
 Connection.KeyVal textInput = formData.get(0);
 assertEquals("text", textInput.key);
 assertNull(textInput.value);
 }

 @Test
 public void testFormDataMethodSelectInput() {
 List<Connection.KeyVal> formData = form.formData();
 Connection.KeyVal selectInput = formData.get(2);
 assertEquals("select", selectInput.key);
 assertEquals("option2", selectInput.value);
 }

 @Test
 public void testFormDataMethodCheckboxInput() {
 List<Connection.KeyVal> formData = form.formData();
 Connection.KeyVal checkboxInput = formData.get(3);
 assertEquals("check", checkboxInput.key);
 assertEquals("on", checkboxInput.value);
 }

 @Test
 public void testFormDataMethodRadioInput() {
 List<Connection.KeyVal> formData = form.formData();
 Connection.KeyVal radioInput1 = formData.get(4);
 assertEquals("radio", radioInput1.key);
 assertNull(radioInput1.value);
 }
}