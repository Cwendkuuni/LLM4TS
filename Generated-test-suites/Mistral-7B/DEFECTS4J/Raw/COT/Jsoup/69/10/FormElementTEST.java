import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

 private FormElement formElement;

 @Before
 public void setUp() {
 Tag tag = new Tag("form");
 Attributes attributes = new Attributes();
 formElement = new FormElement(tag, null, attributes);
 }

 @Test
 public void testElements() {
 Elements expectedElements = new Elements();
 Element element = new Element("input");
 expectedElements.add(element);
 formElement.addElement(element);
 assertEquals(expectedElements, formElement.elements());
 }

 @Test
 public void testSubmitIllegalArgumentException() {
 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitValid() {
 Tag tag = new Tag("form");
 tag.put("action", "http://example.com");
 Attributes attributes = new Attributes();
 formElement = new FormElement(tag, null, attributes);
 formElement.addElement(new Element("input", new Attributes().put("name", "username").put("value", "test")));
 formElement.addElement(new Element("input", new Attributes().put("name", "password").put("type", "password").put("value", "testpassword")));

 Connection connection = formElement.submit();
 assertNotNull(connection);
 }

 @Test
 public void testFormDataEmptyList() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormDataSingleElement() {
 formElement.addElement(new Element("input", new Attributes().put("name", "username").put("value", "test")));
 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal expectedKeyVal = new Connection.KeyVal("username", "test");
 assertEquals(1, formData.size());
 assertEquals(expectedKeyVal, formData.get(0));
 }

 @Test
 public void testFormDataMultipleElements() {
 formElement.addElement(new Element("input", new Attributes().put("name", "username").put("value", "test")));
 formElement.addElement(new Element("input", new Attributes().put("name", "password").put("type", "password").put("value", "testpassword")));
 formElement.addElement(new Element("select", new Attributes().put("name", "color").put("selectedIndex", "1")))
 .addElement(new Element("option", new Attributes().put("value", "red")));
 List<Connection.KeyVal> formData = formElement.formData();
 Connection.KeyVal usernameKeyVal = new Connection.KeyVal("username", "test");
 Connection.KeyVal passwordKeyVal = new Connection.KeyVal("password", "testpassword");
 Connection.KeyVal colorKeyVal = new Connection.KeyVal("color", "red");
 assertEquals(3, formData.size());
 assertEquals(usernameKeyVal, formData.get(0));
 assertEquals(passwordKeyVal, formData.get(1));
 assertEquals(colorKeyVal, formData.get(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitNullBaseUri() {
 formElement.submit();
 }

 @Test
 public void testSubmitMissingAction() {
 formElement = new FormElement(new Tag("form"), null, null);
 formElement.addElement(new Element("input", new Attributes().put("name", "username").put("value", "test")));
 formElement.addElement(new Element("input", new Attributes().put("name", "password").put("type", "password").put("value", "testpassword")));
 try {
 formElement.submit();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSubmitActionEmpty() {
 formElement = new FormElement(new Tag("form"), null, null);
 formElement.put("action", "");
 formElement.addElement(new Element("input", new Attributes().put("name", "username").put("value", "test")));
 formElement.addElement(new Element("input", new Attributes().put("name", "password").put("type", "password").put("value", "testpassword")));
 Connection connection = formElement.submit();
 assertEquals("", connection.url());
 }

 @Test
 public void testSubmitActionWithQueryString() {
 formElement = new FormElement(new Tag("form"), null, null);
 formElement.put("action", "http://example.com?param=value");
 formElement.addElement(new Element("input", new Attributes().put("name", "username").put("value", "test")));
 formElement.addElement(new Element("input", new Attributes().put("name", "password").put("type", "password").put("value", "testpassword")));
 Connection connection = formElement.submit();
 assertEquals("http://example.com?param=value", connection.url());
 }
}