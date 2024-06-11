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
        Element element = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        form.addElement(element);

        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        form.addElement(element);

        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));

        form.removeChild(element);

        elements = form.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testSubmitWithAbsoluteActionUrl() {
        form.attr("action", "https://www.example.com");
        form.attr("method", "post");

        Connection connection = form.submit();

        assertEquals("https://www.example.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionUrl() {
        form.attr("method", "post");

        form.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), form.baseUri(), new Attributes());
        inputElement.attr("name", "name");
        inputElement.attr("type", "text");
        inputElement.val("John");

        Element selectElement = new Element(Tag.valueOf("select"), form.baseUri(), new Attributes());
        selectElement.attr("name", "gender");
        Element optionElement = new Element(Tag.valueOf("option"), form.baseUri(), new Attributes());
        optionElement.attr("selected", "");
        optionElement.val("Male");
        selectElement.appendChild(optionElement);

        form.addElement(inputElement);
        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(2, formData.size());

        Connection.KeyVal nameKeyVal = formData.get(0);
        assertEquals("name", nameKeyVal.key());
        assertEquals("John", nameKeyVal.value());

        Connection.KeyVal genderKeyVal = formData.get(1);
        assertEquals("gender", genderKeyVal.key());
        assertEquals("Male", genderKeyVal.value());
    }
}