import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("name", "input1"));
        FormElement updatedForm = form.addElement(element);
        assertEquals(1, updatedForm.elements().size());
        assertTrue(updatedForm.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes().put("action", ""));
        Connection connection = form.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoActionUrl() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        form.submit();
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes()
                .put("name", "input1")
                .put("value", "value1"));
        Element element2 = new Element(Tag.valueOf("select"), "http://example.com", new Attributes()
                .put("name", "select1"));
        Element option = new Element(Tag.valueOf("option"), "http://example.com", new Attributes()
                .put("selected", ""));
        option.appendText("option1");
        element2.appendChild(option);
        form.addElement(element1);
        form.addElement(element2);

        List<Connection.KeyVal> formData = form.formData();
        assertEquals(2, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("select1", formData.get(1).key());
        assertEquals("option1", formData.get(1).value());
    }
}