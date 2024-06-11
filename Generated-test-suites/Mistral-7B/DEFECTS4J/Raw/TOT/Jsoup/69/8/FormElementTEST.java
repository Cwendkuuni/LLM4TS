import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

 private static final String HTML = "" +
         "<html lang=\"en\">\n" +
         "<head>\n" +
         "<meta charset=\"UTF-8\">\n" +
         "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
         "<title>FormElement Test</title>\n" +
         "</head>\n" +
         "<body>\n" +
         "<form action=\"/submit\" method=\"post\">\n" +
         "<label for=\"name\">Name:</label>\n" +
         "<input type=\"text\" id=\"name\" name=\"name\">\n" +
         "<label for=\"age\">Age:</label>\n" +
         "<select id=\"age\" name=\"age\">\n" +
         "<option value=\"18\">18</option>\n" +
         "<option value=\"21\" selected>21</option>\n" +
         "</select>\n" +
         "<label for=\"gender\">Gender:</label>\n" +
         "<input type=\"radio\" id=\"male\" name=\"gender\" value=\"male\" checked>\n" +
         "<label for=\"male\">Male</label>\n" +
         "<input type=\"radio\" id=\"female\" name=\"gender\" value=\"female\">\n" +
         "<label for=\"female\">Female</label>\n" +
         "<button type=\"submit\">Submit</button>\n" +
         "</form>\n" +
         "</body>\n" +
         "</html>";

 private Element formElement;

 @Before
 public void setUp() {
 Parser parser = new Parser.Builder().parseSetting(ParseSetting.OMIT_TAG_NAME_SPACE).build();
 Document document = parser.parse(HTML);
 formElement = document.select("form").first();
 }

 @Test
 public void testElements() {
 Elements elements = formElement.elements();
 assertEquals(3, elements.size());
 assertNotNull(elements.get(0).tagName());
 assertNotNull(elements.get(1).attr("name"));
 assertNotNull(elements.get(2).attr("type"));
 }

 @Test
 public void testSubmit() {
 Connection connection = formElement.submit();
 assertEquals(formElement.absUrl("action"), connection.url().toString());
 assertEquals(Connection.Method.POST, connection.method());
 }

 @Test
 public void testFormData() {
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 assertEquals("name", formData.get(0).key());
 assertEquals("", formData.get(0).val());
 assertEquals("age", formData.get(1).key());
 assertEquals("21", formData.get(1).val());
 assertEquals("gender", formData.get(2).key());
 assertEquals("male", formData.get(2).val());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubmitWithInvalidAction() {
 FormElement invalidFormElement = new FormElement(Tag.FORM, "", new Attributes());
 Connection connection = invalidFormElement.submit();
 fail("An IllegalArgumentException is expected.");
 }
}