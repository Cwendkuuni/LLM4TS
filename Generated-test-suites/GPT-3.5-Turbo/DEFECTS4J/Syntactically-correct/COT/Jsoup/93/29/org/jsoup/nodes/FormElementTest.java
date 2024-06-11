import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element);
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element);
        form.removeChild(element);
        Elements elements = form.elements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testSubmit() {
        Connection connection = form.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("name", "field1");
        element.val("value1");
        form.addElement(element);
        List<Connection.KeyVal> data = form.formData();
        assertEquals(1, data.size());
        Connection.KeyVal keyVal = data.get(0);
        assertEquals("field1", keyVal.key());
        assertEquals("value1", keyVal.value());
    }

}