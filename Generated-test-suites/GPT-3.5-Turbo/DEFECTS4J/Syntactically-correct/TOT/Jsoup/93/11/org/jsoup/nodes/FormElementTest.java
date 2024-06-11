
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class FormElementTest {

    private FormElement form;

    @BeforeEach
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }


    @DisplayName("Test elements() method")
    @Test
    public void testElements() {
        Elements result = form.elements();
        assertNotNull(result);
        assertEquals(0, result.size());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element1);

        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());
        form.addElement(element2);

        result = form.elements();
        assertEquals(2, result.size());
        assertTrue(result.contains(element1));
        assertTrue(result.contains(element2));
    }


    @DisplayName("Test addElement() method")
    @Test
    public void testAddElement() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element1);
        Elements result = form.elements();
        assertEquals(1, result.size());
        assertTrue(result.contains(element1));
    }


    @DisplayName("Test removeChild() method")
    @Test
    public void testRemoveChild() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element1);

        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());
        form.addElement(element2);

        form.removeChild(element1);
        Elements result = form.elements();
        assertEquals(1, result.size());
        assertFalse(result.contains(element1));
        assertTrue(result.contains(element2));
    }


    @DisplayName("Test submit() method - with action attribute")
    @Test
    public void testSubmitWithActionAttribute() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("name", "username");
        element1.attr("value", "testuser");

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("name", "password");
        element2.attr("value", "testpass");

        form.addElement(element1);
        form.addElement(element2);

        Connection connection = form.submit();

        assertNotNull(connection);
        assertEquals("http://www.example.com/login", connection.request().url().toString());
        assertTrue(connection.request().data().containsKey("username"));
        assertTrue(connection.request().data().containsKey("password"));
        assertEquals("POST", connection.request().method().name());
    }
    

    @DisplayName("Test submit() method - without action attribute")
    @Test
    public void testSubmitWithoutActionAttribute() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("name", "username");
        element1.attr("value", "testuser");

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("name", "password");
        element2.attr("value", "testpass");

        form.addElement(element1);
        form.addElement(element2);

        Connection connection = form.submit();

        assertNotNull(connection);
        assertEquals("", connection.request().url().toString());
        assertTrue(connection.request().data().containsKey("username"));
        assertTrue(connection.request().data().containsKey("password"));
        assertEquals("POST", connection.request().method().name());
    }


    @DisplayName("Test formData() method")
    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("type", "text");
        element1.attr("name", "username");
        element1.attr("value", "testuser");

        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());
        element2.attr("name", "comment");
        element2.text("This is a test comment");

        Element element3 = new Element(Tag.valueOf("input"), "", new Attributes());
        element3.attr("type", "checkbox");
        element3.attr("name", "subscribe");
        element3.attr("value", "yes");
        element3.attr("checked", "");

        Element element4 = new Element(Tag.valueOf("select"), "", new Attributes());
        element4.attr("name", "country");

        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "us");
        option1.attr("selected", "");
        option1.text("United States");

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "ca");
        option2.text("Canada");

        element4.appendChild(option1);
        element4.appendChild(option2);

        form.addElement(element1);
        form.addElement(element2);
        form.addElement(element3);
        form.addElement(element4);

        List<Connection.KeyVal> data = form.formData();

        assertNotNull(data);
        assertEquals(4, data.size());

        assertEquals("username", data.get(0).key());
        assertEquals("testuser", data.get(0).value());

        assertEquals("comment", data.get(1).key());
        assertEquals("This is a test comment", data.get(1).value());

        assertEquals("subscribe", data.get(2).key());
        assertEquals("yes", data.get(2).value());

        assertEquals("country", data.get(3).key());
        assertEquals("us", data.get(3).value());
    }
}


