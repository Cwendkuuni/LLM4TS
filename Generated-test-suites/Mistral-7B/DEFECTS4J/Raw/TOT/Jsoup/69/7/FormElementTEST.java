import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private FormElement form;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 " <form action='http://example.com' method='get'>" +
 " <input type='text' name='username' value='test'>" +
 " <input type='text' name='password' value='password'>" +
 " <select name='select'>" +
 " <option value='option1'>Option 1</option>" +
 " <option value='option2' selected>Option 2</option>" +
 " </select>" +
 " <input type='submit' value='Submit'>" +
 " </form>" +
 "</body></html>";
 form = Jsoup.parse(html).body().selectFirst(FormElement.class);
 }

 @Test
 public void testElements() {
 Elements elements = form.elements();
 assertEquals(3, elements.size());
 assertInstanceOf(Element.class, elements.get(0));
 assertInstanceOf(Element.class, elements.get(1));
 assertInstanceOf(Element.class, elements.get(2));
 }

 @Test
 public void testAddElement() {
 Element input = new Element("input");
 input.attr("type", "text");
 input.attr("name", "newElement");
 form.addElement(input);
 Elements elements = form.elements();
 assertEquals(4, elements.size());
 }

 @Test
 public void testSubmitWithNoAction() {
 try {
 form.submit();
 fail("Should have thrown IllegalArgumentException for no action URL");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSubmitWithInvalidMethod() {
 try {
 form.submit().method(Connection.Method.PUT);
 fail("Should have thrown UnsupportedOperationException for PUT method");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testSubmitWithValidData() {
 Connection connection = form.submit();
 List<Connection.KeyVal> formData = connection.data();
 assertEquals(5, formData.size());
 assertEquals("test", formData.get(0).key);
 assertEquals("test", formData.get(0).val);
 assertEquals("password", formData.get(1).key);
 assertEquals("password", formData.get(1).val);
 assertEquals("option2", formData.get(2).key);
 assertEquals("Option 2", formData.get(2).val);
 }

 @Test
 public void testSubmitWithDisabledElements() {
 Element disabledInput = form.selectFirst("input[disabled]");
 form.submit();
 List<Connection.KeyVal> formData = connection.data();
 assertEquals(3, formData.size());
 assertNull(formData.stream().filter(kv -> kv.key.equals("username")).findFirst().get());
 assertNull(formData.stream().filter(kv -> kv.key.equals("password")).findFirst().get());
 assertNotNull(formData.stream().filter(kv -> kv.key.equals("disabledInputName")).findFirst().get());
 }
}