package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A HTML Form Element provides ready access to the form fields/controls that are associated with it. It also allows a
 * form to easily be submitted.
 */
public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());

        Element element1 = new Element(Tag.valueOf("input"), "");
        formElement.addElement(element1);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element1));

        Element element2 = new Element(Tag.valueOf("input"), "");
        formElement.addElement(element2);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(element2));
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "");
        FormElement returnedFormElement = formElement.addElement(element);
        assertNotNull(returnedFormElement);
        assertEquals(formElement, returnedFormElement);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmitNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Jsoup.connect("").data(new ArrayList<>()).method(Connection.Method.GET), connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoBaseUri() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("action", "http://example.com");
        formElement.submit();
    }

    @Test
    public void testSubmitGetMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "GET");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Jsoup.connect("http://example.com").data(new ArrayList<>()).method(Connection.Method.GET), connection);
    }

    @Test
    public void testSubmitPostMethod() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Jsoup.connect("http://example.com").data(new ArrayList<>()).method(Connection.Method.POST), connection);
    }

    @Test
    public void testFormDataNoElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        Element element1 = new Element(Tag.valueOf("input"), "");
        element1.attr("name", "name1");
        element1.attr("type", "text");
        element1.val("value1");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), "");
        element2.attr("name", "name2");
        element2.attr("type", "checkbox");
        element2.attr("checked", "");
        element2.val("value2");
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("name1", "value1")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("name2", "value2")));
    }
}