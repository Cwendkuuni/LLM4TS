import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FormElementTest {

 private Element formElement;

 @Before
 public void setUp() {
 String html = "<html><body>" +
 " <form method=\"post\" action=\"/\">" +
 " <input type=\"text\" name=\"username\" value=\"John\">" +
 " <input type=\"password\" name=\"password\" value=\"password\">" +
 " <input type=\"submit\" value=\"Submit\">" +
 " </form>" +
 "</body></html>";

 Parser parser = Jsoup.parser();
 formElement = parser.parseBodyFragment(html);
 }

 @Test
 public void testElements() {
 FormElement form = (FormElement) formElement;
 Elements elements = form.elements();
 assertEquals(2, elements.size());
 }

 @Test
 public void testSubmit() {
 FormElement form = (FormElement) formElement;
 Connection connection = form.submit();
 assertEquals("POST", connection.method().name());
 List<Connection.KeyVal> formData = form.formData();
 assertEquals(2, formData.size());
 assertEquals("username", formData.get(0).key());
 assertEquals("John", formData.get(0).value());
 assertEquals("password", formData.get(1).key());
 assertEquals("password", formData.get(1).value());
 }

 @Test
 public void testEmptyForm() {
 String html = "<html><body>" +
 " <form method=\"post\" action=\"/\">" +
 " </form>" +
 "</body></html>";

 Parser parser = Jsoup.parser();
 Element emptyForm = parser.parseBodyFragment(html);
 FormElement form = (FormElement) emptyForm;
 assertEquals(0, form.elements().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingActionAttribute() {
 String html = "<html><body>" +
 " <form>" +
 " </form>" +
 "</body></html>";

 Parser parser = Jsoup.parser();
 Element missingActionForm = parser.parseBodyFragment(html);
 FormElement form = (FormElement) missingActionForm;
 form.submit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingBaseUri() {
 String html = "<html>" +
 " <head></head>" +
 " <body>" +
 " <form method=\"post\" action=\"/\">" +
 " </body>" +
 "</html>";

 Parser parser = Jsoup.parser();
 Element missingBaseUriForm = parser.parse(html);
 FormElement form = (FormElement) missingBaseUriForm;
 form.submit();
 }

 @Test
 public void testDisableFormElement() {
 String html = "<html><body>" +
 " <form method=\"post\" action=\"/\">" +
 " <input type=\"text\" name=\"username\" value=\"John\" disabled>" +
 " </form>" +
 "</body></html>";

 Parser parser = Jsoup.parser();
 Element disabledFormElement = parser.parseBodyFragment(html);
 FormElement form = (FormElement) disabledFormElement;
 assertEquals(0, form.elements().size());
 }
}