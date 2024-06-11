import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private FormElement form;

    @Before
    public void setUp() {
        form = new FormElement(Tag.valueOf("form"), "http://www.example.com/", new Attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        form.addElement(element);
        assertEquals(1, form.elements().size());
        assertTrue(form.elements().contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        form.addElement(element);
        form.removeChild(element);
        assertEquals(0, form.elements().size());
        assertFalse(form.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        String expectedAction = "http://www.example.com/action";
        form.attr("action", "action");
        Connection connection = form.submit();

        assertEquals(expectedAction, connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        form.submit();
    }

    @Test
    public void testFormData() {
        Element input = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        input.attr("name", "username");
        input.val("johnDoe");

        form.addElement(input);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("username", "johnDoe"));

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(expectedData, formData);
    }
}