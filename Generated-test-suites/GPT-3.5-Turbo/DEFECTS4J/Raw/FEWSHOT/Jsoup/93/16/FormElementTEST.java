import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
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
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();

        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertTrue(elements.isEmpty());
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
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmit() {
        Connection connection = form.submit();

        assertEquals("http://www.example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        form.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.val("inputValue");

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "selectName");
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.val("optionValue");
        selectElement.appendChild(optionElement);

        form.addElement(inputElement);
        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(2, formData.size());

        Connection.KeyVal inputKeyVal = formData.get(0);
        assertEquals("inputName", inputKeyVal.key());
        assertEquals("inputValue", inputKeyVal.value());

        Connection.KeyVal selectKeyVal = formData.get(1);
        assertEquals("selectName", selectKeyVal.key());
        assertEquals("optionValue", selectKeyVal.value());
    }
}