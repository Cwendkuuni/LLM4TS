import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Attributes attributes = new Attributes();
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        FormElement newFormElement = formElement.addElement(new Element(Tag.valueOf("input"), "http://example.com"));
        assertEquals(1, newFormElement.elements().size());
    }

    @Test
    public void testSubmit() {
        Connection con = formElement.submit();
        assertEquals("http://example.com", con.request().url().toString());
        assertEquals(Connection.Method.GET, con.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testEquals() {
        FormElement sameFormElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(formElement, sameFormElement);

        FormElement differentFormElement = new FormElement(Tag.valueOf("form"), "http://different.com", new Attributes());
        assertNotEquals(formElement, differentFormElement);
    }

}