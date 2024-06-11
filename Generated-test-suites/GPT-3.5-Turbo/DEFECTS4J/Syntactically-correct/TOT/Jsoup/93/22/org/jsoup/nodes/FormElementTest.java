import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        form = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = new Elements();
        form.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));
        form.addElement(new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes()));
        form.addElement(new Element(Tag.valueOf("select"), "http://example.com", new Attributes()));
        elements.add(new Element(Tag.valueOf("input"), "http://example.com", new Attributes()));
        elements.add(new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes()));
        elements.add(new Element(Tag.valueOf("select"), "http://example.com", new Attributes()));
        assertEquals(elements, form.elements());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element);
        assertTrue(form.elements().contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element);
        form.removeChild(element);
        assertFalse(form.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Element input = new Element(Tag.valueOf("input"), baseUri, new Attributes());
        input.attr("name", "username");
        input.val("testuser");
        form.addElement(input);

        Element checkbox = new Element(Tag.valueOf("input"), baseUri, new Attributes());
        checkbox.attr("name", "rememberMe");
        checkbox.attr("type", "checkbox");
        checkbox.attr("checked", "");
        form.addElement(checkbox);

        Connection connection = form.submit();
        assertEquals(baseUri, connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
        assertTrue(connection.request().data().has("username"));
        assertEquals("testuser", connection.request().data().get("username"));
        assertTrue(connection.request().data().has("rememberMe"));
        assertEquals("on", connection.request().data().get("rememberMe"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        form.submit();
    }

    @Test
    public void testFormData() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement form = new FormElement(tag, baseUri, attributes);

        Element input = new Element(Tag.valueOf("input"), baseUri, new Attributes());
        input.attr("name", "username");
        input.val("testuser");
        form.addElement(input);

        Element textarea = new Element(Tag.valueOf("textarea"), baseUri, new Attributes());
        textarea.attr("name", "description");
        textarea.text("This is a description");
        form.addElement(textarea);

        Element select = new Element(Tag.valueOf("select"), baseUri, new Attributes());
        select.attr("name", "country");
        select.appendChild(new Element(Tag.valueOf("option"), baseUri, new Attributes()).attr("selected", "").val("USA"));
        select.appendChild(new Element(Tag.valueOf("option"), baseUri, new Attributes()).val("Canada"));
        form.addElement(select);

        List<Connection.KeyVal> formData = form.formData();
        assertEquals(3, formData.size());

        Connection.KeyVal usernameKeyVal = HttpConnection.KeyVal.create("username", "testuser");
        assertTrue(formData.contains(usernameKeyVal));

        Connection.KeyVal descriptionKeyVal = HttpConnection.KeyVal.create("description", "This is a description");
        assertTrue(formData.contains(descriptionKeyVal));

        Connection.KeyVal countryKeyVal = HttpConnection.KeyVal.create("country", "USA");
        assertTrue(formData.contains(countryKeyVal));
    }

}