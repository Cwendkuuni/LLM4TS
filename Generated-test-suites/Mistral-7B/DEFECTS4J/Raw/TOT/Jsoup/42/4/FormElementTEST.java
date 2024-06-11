import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

private FormElement formElement;

@Before
public void setup() {
String html = "<html lang=\"en\">" +
"<head>" +
"<meta charset=\"UTF-8\">" +
"<title>Test Form Element</title>" +
"</head>" +
"<body>" +
"<form action=\"/submit\" method=\"post\">" +
"<label for=\"name\">Name:</label>" +
"<input type=\"text\" name=\"name\" id=\"name\">" +
"<br>" +
"<label for=\"email\">Email:</label>" +
"<input type=\"email\" name=\"email\" id=\"email\">" +
"<br>" +
"<label for=\"select\">Select:</label>" +
"<select name=\"select\">" +
"<option value=\"option1\">Option 1</option>" +
"<option value=\"option2\" selected>Option 2</option>" +
"<option value=\"option3\">Option 3</option>" +
"</select>" +
"<br>" +
"<label for=\"checkbox\">Checkbox:</label>" +
"<input type=\"checkbox\" name=\"checkbox\" id=\"checkbox\">" +
"<br>" +
"<label for=\"radio\">Radio:</label>" +
"<input type=\"radio\" name=\"radio\" id=\"radio1\" checked>" +
"<input type=\"radio\" name=\"radio\" id=\"radio2\">" +
"<br>" +
"<input type=\"submit\" value=\"Submit\">" +
"</form>" +
"</body>" +
"</html>";

Parser parser = Jsoup.parser();
Document document = parser.parse(html);
formElement = document.body().selectFirst("form");
}

@Test
public void testFormElementCreation() {
FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
assertNotNull(formElement);
}

@Test
public void testElements() {
Elements elements = formElement.elements();
assertNotNull(elements);
assertEquals(6, elements.size());
}

@Test
public void testAddElement() {
Element input = new Element("input", new Attributes().put("type", "hidden").put("name", "hiddenInput").put("value", "testValue"));
FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
formElement.addElement(input);
Elements elements = formElement.elements();
assertEquals(1, elements.size());
assertEquals(input, elements.get(0));
}

@Test
public void testSubmit() {
Connection connection = formElement.submit();
assertNotNull(connection);
assertEquals("/submit", connection.url());
}

@Test
public void testFormData() {
List<Connection.KeyVal> formData = formElement.formData();
assertEquals(5, formData.size());
assertEquals("name", formData.get(0).getKey());
assertEquals("", formData.get(0).getValue());
assertEquals("email", formData.get(1).getKey());
assertEquals("", formData.get(1).getValue());
assertEquals("option2", formData.get(2).getKey());
assertEquals("Option 2", formData.get(2).getValue());
assertEquals("checkbox", formData.get(3).getKey());
assertEquals("true", formData.get(3).getValue());
assertEquals("radio2", formData.get(4).getKey());
assertEquals("", formData.get(4).getValue());
}

@Test
public void testEmptyFormElement() {
FormElement emptyFormElement = new FormElement(Tag.FORM, "", new Attributes());
List<Connection.KeyVal> formData = emptyFormElement.formData();
assertEquals(0, formData.size());
}

@Test(expected = IllegalArgumentException.class)
public void testEmptyActionUrl() {
FormElement formElement = new FormElement(Tag.FORM, "", new Attributes().put("action", ""));
formElement.submit();
}
}