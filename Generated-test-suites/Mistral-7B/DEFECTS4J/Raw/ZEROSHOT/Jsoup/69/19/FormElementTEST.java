import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FormElementTest {

 private FormElement formElement;
 private Element inputElement;
 private Element selectElement;
 private Element checkboxElement;
 private Element radioElement;

 @BeforeEach
 void setup() {
 Parser parser = new Parser.MalformedHtmlDocumentParser();
 String html = "<html><body>" +
 " <form>" +
 " <input type='text' name='input'/>" +
 " <select name='select'>" +
 " <option value='option1'>Option 1</option>" +
 " <option value='option2' selected>Option 2</option>" +
 " </select>" +
 " <input type='checkbox' name='checkbox'/>" +
 " <input type='radio' name='radio' value='value1' checked/>" +
 " <input type='submit'/>" +
 " </form>" +
 " </body>" +
 "</html>";
 formElement = (FormElement) parser.parse(html).body().select("form").first();
 inputElement = formElement.elements().select("input[type='text']").first();
 selectElement = formElement.elements().select("select").first();
 checkboxElement = formElement.elements().select("input[type='checkbox']").first();
 radioElement = formElement.elements().select("input[type='radio']").first();
 }

 @Test
 void testCreateFormElement() {
 // Test creating a new FormElement
 Tag tag = new Tag("form");
 FormElement formElement = new FormElement(tag, "", new Attributes());
 assertEquals(tag.tagName(), formElement.tagName());
 }

 @Test
 void testElements() {
 // Test getting the list of form control elements
 Elements expectedElements = formElement.elements();
 List<Element> actualElements = new ArrayList<>();
 actualElements.add(inputElement);
 actualElements.add(selectElement);
 actualElements.add(checkboxElement);
 actualElements.add(radioElement);
 assertEquals(actualElements, expectedElements);
 }

 @Test
 void testAddElement() {
 // Test adding a form control element to the form
 Element textAreaElement = new Element(new Tag("textarea"));
 FormElement formElementAddElement = formElement.addElement(textAreaElement);
 Elements actualElements = formElementAddElement.elements();
 Element expectedElement = textAreaElement;
 assertEquals(actualElements.size(), expectedElements.size() + 1);
 assertEquals(actualElements.get(actualElements.size() - 1), expectedElement);
 }

 @Test
 void testSubmit() {
 // Test submitting the form
 Connection connection = formElement.submit();
 String expectedUrl = "http://localhost:8080/?input=&select=option2";
 assertEquals(expectedUrl, connection.url());
 }

 @Test
 void testFormData() {
 // Test getting form data
 List<Connection.KeyVal> expectedData = new ArrayList<>();
 expectedData.add(new Connection.KeyVal("input", ""));
 expectedData.add(new Connection.KeyVal("select", "option2"));
 List<Connection.KeyVal> actualData = formElement.formData();
 assertEquals(expectedData, actualData);
 }
}