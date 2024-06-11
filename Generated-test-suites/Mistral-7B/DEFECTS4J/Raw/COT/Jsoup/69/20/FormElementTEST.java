import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

private static final String TEST_HTML = "" +
        "<html>" +
        "<body>" +
        "<form method=\"get\">" +
        "<input name=\"testInput\" type=\"text\">" +
        "<select name=\"testSelect\">" +
        "<option value=\"option1\">Option 1</option>" +
        "<option value=\"option2\" selected>Option 2</option>" +
        "</select>" +
        "<input name=\"testCheckbox\" type=\"checkbox\">" +
        "<input name=\"testRadio\" type=\"radio\" checked>" +
        "<input name=\"testDisabledInput\" type=\"text\" disabled>" +
        "</form>" +
        "</body>" +
        "</html>";

@Before
public void setup() {
 ParseSetup.setDefaultParseErrorReporter(new ParseErrorReporter() {
 @Override
 public void error(String message, Location location) {
 throw new RuntimeException(message);
 }
 });
}

@Test
public void testConstructors() {
 Tag tag = new Tag("form");
 FormElement formElement = new FormElement(tag, "http://test.com", Attributes.empty());
 assertNotNull(formElement);
}

@Test
public void testElements() {
 FormElement formElement = parseHtml(TEST_HTML).body().select("form").first().FormElement();
 Elements elements = formElement.elements();
 assertNotNull(elements);
 assertEquals(5, elements.size());
}

@Test
public void testAddElement() {
 FormElement formElement = parseHtml(TEST_HTML).body().select("form").first().FormElement();
 Element inputElement = parseHtml(TEST_HTML).body().select("input[name=\"testInput\"]").first();
 formElement.addElement(inputElement);
 Elements elements = formElement.elements();
 assertEquals(6, elements.size());
 assertEquals(inputElement, elements.get(5));
}

@Test
public void testSubmitWithGetMethod() throws Exception {
 FormElement formElement = parseHtml(TEST_HTML).body().select("form").first().FormElement();
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());
 assertEquals("testInput", formData.get(0).key());
 assertEquals("", formData.get(0).value());
 assertEquals("testSelect", formData.get(1).key());
 assertEquals("option2", formData.get(1).value());
 assertEquals("testCheckbox", formData.get(2).key());
 assertEquals("", formData.get(2).value());
 assertEquals("testRadio", formData.get(3).key());
 assertEquals("on", formData.get(3).value());
 assertEquals("testDisabledInput", formData.get(4).key());
 assertEquals("", formData.get(4).value());
 String response = connection.execute().body();
 assertNull(response);
}

@Test
public void testSubmitWithPostMethod() throws Exception {
 FormElement formElement = parseHtml(TEST_HTML).body().select("form").first().FormElement();
 formElement.attr("method", "post");
 Connection connection = formElement.submit();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(5, formData.size());
 assertEquals("testInput", formData.get(0).key());
 assertEquals("", formData.get(0).value());
 assertEquals("testSelect", formData.get(1).key());
 assertEquals("option2", formData.get(1).value());
 assertEquals("testCheckbox", formData.get(2).key());
 assertEquals("", formData.get(2).value());
 assertEquals("testRadio", formData.get(3).key());
 assertEquals("on", formData.get(3).value());
 assertEquals("testDisabledInput", formData.get(4).key());
 assertEquals("", formData.get(4).value());
 String response = connection.execute().body();
 assertNotNull(response);
}

@Test(expected = IllegalArgumentException.class)
public void testSubmitWithoutBaseUri() {
 FormElement formElement = new FormElement(new Tag("form"), null, Attributes.empty());
 formElement.submit();
}

@Test
public void testSubmitWithEmptyFormData() {
 FormElement formElement = parseHtml("<html><body><form></form></body></html>").body().select("form").first().FormElement();
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
}

@Test
public void testSubmitWithNullFormData() {
 FormElement formElement = parseHtml("<html><body><form></form></body></html>").body().select("form").first().FormElement();
 formElement.elements().clear();
 List<Connection.KeyVal> formData = null;
 try {
 formElement.submit();
 } catch (IllegalArgumentException e) {
 formData = Arrays.asList((Connection.KeyVal[]) e.getCauses()[0].getCause());
 }
 assertEquals(0, formData.size());
}
}