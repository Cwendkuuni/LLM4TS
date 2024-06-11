import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class FormElementTest {

    private FormElement formElement;
    private Element inputElement;
    private Element selectElement;
    private Element checkboxElement;
    private Element radioElement;

    @Before
    public void setUp() {
        Tag formTag = Tag.valueOf("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(formTag, baseUri, attributes);

        Tag inputTag = Tag.valueOf("input");
        inputElement = new Element(inputTag, baseUri);
        inputElement.attr("name", "username");
        inputElement.attr("type", "text");
        inputElement.val("john_doe");

        Tag selectTag = Tag.valueOf("select");
        selectElement = new Element(selectTag, baseUri);
        selectElement.attr("name", "color");
        Tag optionTag1 = Tag.valueOf("option");
        Element optionElement1 = new Element(optionTag1, baseUri);
        optionElement1.attr("value", "red");
        optionElement1.text("Red");
        optionElement1.attr("selected", "");
        Tag optionTag2 = Tag.valueOf("option");
        Element optionElement2 = new Element(optionTag2, baseUri);
        optionElement2.attr("value", "blue");
        optionElement2.text("Blue");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);

        Tag checkboxTag = Tag.valueOf("input");
        checkboxElement = new Element(checkboxTag, baseUri);
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");

        Tag radioTag = Tag.valueOf("input");
        radioElement = new Element(radioTag, baseUri);
        radioElement.attr("name", "gender");
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "");
    }

    @Test
    public void testElements() {
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(radioElement);

        Elements elements = formElement.elements();

        assertTrue(elements.contains(inputElement));
        assertTrue(elements.contains(selectElement));
        assertTrue(elements.contains(checkboxElement));
        assertTrue(elements.contains(radioElement));
    }

    @Test
    public void testAddElement() {
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();
        assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testRemoveChild() {
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(radioElement);

        formElement.removeChild(inputElement);

        Elements elements = formElement.elements();
        assertFalse(elements.contains(inputElement));
        assertTrue(elements.contains(selectElement));
        assertTrue(elements.contains(checkboxElement));
        assertTrue(elements.contains(radioElement));
    }

    @Test
    public void testSubmit() {
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(radioElement);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.url().toString());
        assertTrue(connection.request().data().containsKey("username"));
        assertTrue(connection.request().data().containsValue("john_doe"));
        assertTrue(connection.request().data().containsKey("color"));
        assertTrue(connection.request().data().containsValue("red"));
        assertTrue(connection.request().data().containsKey("agree"));
        assertTrue(connection.request().data().containsValue("on"));
        assertTrue(connection.request().data().containsKey("gender"));
        assertTrue(connection.request().data().containsValue("on"));
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitNoAction() {
        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitEmptyAction() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testSubmitDisabledInput() {
        inputElement.attr("disabled", "");
        formElement.addElement(inputElement);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.url().toString());
        assertTrue(connection.request().data().isEmpty());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitSelectNoOptionSelected() {
        formElement.addElement(selectElement);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.url().toString());
        assertTrue(connection.request().data().containsKey("color"));
        assertTrue(connection.request().data().containsValue("red"));
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitSelectWithOptionSelected() {
        Element optionElement3 = new Element(Tag.valueOf("option"), "https://example.com");
        optionElement3.attr("value", "green");
        optionElement3.text("Green");
        selectElement.appendChild(optionElement3);
        formElement.addElement(selectElement);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.url().toString());
        assertTrue(connection.request().data().containsKey("color"));
        assertTrue(connection.request().data().containsValue("red"));
        assertFalse(connection.request().data().containsValue("green"));
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitCheckboxChecked() {
        formElement.addElement(checkboxElement);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.url().toString());
        assertTrue(connection.request().data().containsKey("agree"));
        assertTrue(connection.request().data().containsValue("on"));
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitCheckboxNotChecked() {
        checkboxElement.removeAttr("checked");
        formElement.addElement(checkboxElement);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.url().toString());
        assertFalse(connection.request().data().containsKey("agree"));
        assertFalse(connection.request().data().containsValue("on"));
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitRadioChecked() {
        formElement.addElement(radioElement);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.url().toString());
        assertTrue(connection.request().data().containsKey("gender"));
        assertTrue(connection.request().data().containsValue("on"));
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmitRadioNotChecked() {
        radioElement.removeAttr("checked");
        formElement.addElement(radioElement);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("https://example.com", connection.url().toString());
        assertFalse(connection.request().data().containsKey("gender"));
        assertFalse(connection.request().data().containsValue("on"));
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
        formElement.addElement(checkboxElement);
        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(4, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("username", "john_doe")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("color", "red")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("agree", "on")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("gender", "on")));
    }
}