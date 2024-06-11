import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<html><body><form><input type='text' name='username' /><input type='submit' name='submit' /></form></body></html>");
 }

 @Test
 public void testFormElementCreation() {
 FormElement form = doc.select("form").first();
 assertNotNull(form);
 }

 @Test
 public void testElementsMethod() {
 FormElement form = doc.select("form").first();
 Elements elements = form.elements();
 assertNotNull(elements);
 assertEquals(2, elements.size());
 }

 @Test
 public void testAddElementMethod() {
 FormElement form = doc.select("form").first();
 Element input = doc.createElement("input");
 input.attr("type", "text");
 input.attr("name", "password");
 form.addElement(input);
 Elements elements = form.elements();
 assertEquals(3, elements.size());
 }

 @Test
 public void testSubmitMethodWithValidFormData() throws Exception {
 FormElement form = doc.select("form").first();
 Connection connection = form.submit();
 List<Connection.KeyVal> data = form.formData();
 assertEquals(2, data.size());
 assertEquals("username", data.get(0).key());
 assertEquals("", data.get(0).val());
 assertEquals("submit", data.get(1).key());
 assertEquals("", data.get(1).val());
 }

 @Test
 public void testSubmitMethodWithEmptyFormData() throws Exception {
 FormElement form = doc.select("form").first();
 form.removeAttr("action");
 Connection connection = form.submit();
 assertThrows(IllegalArgumentException.class, () -> form.submit());
 }

 @Test
 public void testFormDataMethodWithSelectElement() throws Exception {
 FormElement form = doc.parse("<html><body><form><select name='color'><option value='red' selected>Red</option><option value='green'>Green</option></select><input type='submit' name='submit' /></form></body></html>").select("form").first();
 List<Connection.KeyVal> data = form.formData();
 assertEquals(2, data.size());
 assertEquals("color", data.get(0).key());
 assertEquals("red", data.get(0).val());
 assertEquals("submit", data.get(1).key());
 assertEquals("", data.get(1).val());
 }

 @Test
 public void testFormDataMethodWithCheckboxElement() throws Exception {
 FormElement form = doc.parse("<html><body><form><input type='checkbox' name='agree' value='yes' checked /></form></body></html>").select("form").first();
 List<Connection.KeyVal> data = form.formData();
 assertEquals(1, data.size());
 assertEquals("agree", data.get(0).key());
 assertEquals("yes", data.get(0).val());
 }

 @Test
 public void testFormDataMethodWithMultipleCheckboxElements() throws Exception {
 FormElement form = doc.parse("<html><body><form><input type='checkbox' name='gender' value='male' checked /><input type='checkbox' name='gender' value='female' /></form></body></html>").select("form").first();
 List<Connection.KeyVal> data = form.formData();
 assertEquals(2, data.size());
 assertEquals("gender", data.get(0).key());
 assertEquals("male", data.get(0).val());
 assertEquals("gender", data.get(1).key());
 assertEquals("female", data.get(1).val());
 }

 @Test
 public void testEqualsMethod() {
 FormElement form1 = doc.select("form").first();
 FormElement form2 = doc.select("form").first();
 assertTrue(form1.equals(form2));
 }

 @Test
 public void testEqualsMethodDifferentInstances() {
 FormElement form1 = doc.select("form").first();
 FormElement form2 = doc.select("form").get(1);
 assertFalse(form1.equals(form2));
 }

 @Test
 public void testEqualsMethodSameInstance() {
 FormElement form = doc.select("form").first();
 assertTrue(form.equals(form));
 }
}