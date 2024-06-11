package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag formTag = Parser.parse("<form action='/form' method='post'><input name='foo' type='text' /></form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());

        assertEquals("form", formElement.tag().name());
        assertEquals("/form", formElement.attr("action"));
        assertEquals("post", formElement.attr("method"));
        assertNull(formElement.html());
    }

    @Test
    public void testFormElementElements() {
        Tag formTag = Parser.parse("<form action='/form' method='post'><input name='foo' type='text' /><input name='bar' type='text' /></form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());
        Elements elements = formElement.elements();

        assertEquals(2, elements.size());
        assertEquals("input", elements.get(0).tag().name());
        assertEquals("input", elements.get(1).tag().name());
    }

    @Test
    public void testFormElementAddElement() {
        Tag formTag = Parser.parse("<form action='/form' method='post'></form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());
        Element inputElement = Parser.parse("<input name='foo' type='text' />").parseBodyFragment();

        FormElement result = formElement.addElement(inputElement);

        assertEquals(1, formElement.elements().size());
        assertEquals("input", formElement.elements().get(0).tag().name());
        assertEquals("foo", formElement.elements().get(0).attr("name"));
    }

    @Test
    public void testFormElementRemoveChild() {
        Tag formTag = Parser.parse("<form action='/form' method='post'><input name='foo' type='text' /></form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());
        Element inputElement = formElement.elements().get(0);

        formElement.removeChild(inputElement);

        assertTrue(formElement.children().isEmpty());
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        Tag formTag = Parser.parse("<form name='testForm' action='http://example.com/submit' method='post'> " +
                "<input name='foo' type='text' value='fooValue' />" +
                "<input name='bar' type='text' value='barValue' />" +
                "</form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();

        assertEquals(2, formData.size());
        assertEquals("foo", formData.get(0).key());
        assertEquals("fooValue", formData.get(0).value());
        assertEquals("bar", formData.get(1).key());
        assertEquals("barValue", formData.get(1).value());
    }

    @Test
    public void testFormElementSubmitWithEmptyForm() throws Exception {
        Tag formTag = Parser.parse("<form name='testForm' action='http://example.com/submit' method='post'></form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();

        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormElementSubmitWithDisabledInput() throws Exception {
        Tag formTag = Parser.parse("<form name='testForm' action='http://example.com/submit' method='post'> " +
                "<input name='foo' type='text' value='fooValue' disabled />" +
                "</form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();

        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormElementSubmitWithSelectElement() throws Exception {
        Tag formTag = Parser.parse("<form name='testForm' action='http://example.com/submit' method='post'> " +
                "<select name='foo'> " +
                "<option value='option1'>Option 1</option>" +
                "<option value='option2' selected>Option 2</option>" +
                "</select>" +
                "</form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();

        assertEquals(1, formData.size());
        assertEquals("foo", formData.get(0).key());
        assertEquals("option2", formData.get(0).value());
    }

    @Test
    public void testFormElementSubmitWithCheckboxOrRadioElement() throws Exception {
        Tag formTag = Parser.parse("<form name='testForm' action='http://example.com/submit' method='post'> " +
                "<input name='foo' type='checkbox' checked='checked' />" +
                "</form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();

        assertEquals(1, formData.size());
        assertEquals("foo", formData.get(0).key());
        assertEquals("on", formData.get(0).value());

        formElement = new FormElement(formTag, "http://base", new Attributes().put("name", "testForm2"));
        formElement.elements().first().removeAttr("checked");
        connection = formElement.submit();
        formData = connection.requestBody();

        assertTrue(formData.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithInvalidActionUrl() throws Exception {
        Tag formTag = Parser.parse("<form name='testForm' action='' method='post'> " +
                "</form>").tags().get(0);
        FormElement formElement = new FormElement(formTag, "http://base", new Attributes());

        formElement.submit();

        // The exception should be thrown before reaching this point
    }
}