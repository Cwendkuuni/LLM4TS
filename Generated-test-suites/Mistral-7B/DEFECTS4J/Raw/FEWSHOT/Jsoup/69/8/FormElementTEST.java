package org.jsoup.nodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.parser.Tag;
import org.jsoup.select.ElementsFilter;
import org.jsoup.select.Select;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form><input type='text' name='name'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        assertNotNull(form);
    }

    @Test
    public void testGetElements() {
        String html = "<form><input type='text' name='name1'><input type='text' name='name2'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Elements elements = form.elements();
        assertEquals(2, elements.size());
    }

    @Test
    public void testAddElement() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Element input = new Element("input");
        input.attr("type", "text");
        input.attr("name", "name");
        form.addElement(input);
        Elements elements = form.elements();
        assertEquals(1, elements.size());
    }

    @Test
    public void testSubmitWithEmptyForm() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        assertNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        form.removeAttr("action");
        form.submit();
    }

    @Test
    public void testSubmitWithGetMethod() {
        String html = "<form action='http://example.com' method='get'><input type='text' name='name' value='value'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals("GET", connection.method().name());
        assertEquals("http://example.com", connection.url().toString());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }

    @Test
    public void testSubmitWithPostMethod() {
        String html = "<form action='http://example.com' method='post'><input type='text' name='name' value='value'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        assertNotNull(connection);
        assertEquals("POST", connection.method().name());
        assertEquals("http://example.com", connection.url().toString());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }

    @Test
    public void testFormDataEmptyForm() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSingleElement() {
        String html = "<form><input type='text' name='name1' value='value1'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name1", keyVal.key());
        assertEquals("value1", keyVal.value());
    }

    @Test
    public void testFormDataMultipleElements() {
        String html = "<form><input type='text' name='name1' value='value1'><input type='text' name='name2' value='value2'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(2, formData.size());
        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("name1", keyVal1.key());
        assertEquals("value1", keyVal1.value());
        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("name2", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }

    @Test
    public void testFormDataSelectElement() {
        String html = "<form><select name='name'><option value='value1'>Option 1</option><option selected value='value2'>Option 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value2", keyVal.value());
    }

    @Test
    public void testFormDataCheckboxOrRadioElement() {
        String html = "<form><input type='checkbox' name='name' value='value' checked><input type='radio' name='name' value='value' checked></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }

    @Test
    public void testFormDataDisabledElement() {
        String html = "<form><input type='text' name='name' value='value' disabled></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataNoNameAttribute() {
        String html = "<form><input type='text'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSelectMultipleElement() {
        String html = "<form><select name='name' multiple><option value='value1'>Option 1</option><option value='value2'>Option 2</option><option selected value='value3'>Option 3</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        List<String> values = new ArrayList<>();
        values.add("value3");
        assertEquals(values.toArray(new String[0]), keyVal.value().split("&"));
    }
}