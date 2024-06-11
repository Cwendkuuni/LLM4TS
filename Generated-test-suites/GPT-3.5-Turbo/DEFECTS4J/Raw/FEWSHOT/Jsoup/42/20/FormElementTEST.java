package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element1);
        assertEquals(1, elements.size());
        assertEquals(element1, elements.get(0));

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element2);
        assertEquals(2, elements.size());
        assertEquals(element2, elements.get(1));
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element1);
        assertEquals(1, form.elements().size());
        assertEquals(element1, form.elements().get(0));

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(element2);
        assertEquals(2, form.elements().size());
        assertEquals(element2, form.elements().get(1));
    }
    
    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Connection con = form.submit();
        assertNotNull(con);
        assertEquals("http://example.com", con.request().url().toString());
        assertEquals(Connection.Method.GET, con.request().method());
        assertEquals(0, con.request().data().size());

        form.attr("action", "/submit");
        form.attr("method", "POST");
        form.addElement(new Element(Tag.valueOf("input"), "", new Attributes("name", "name1", "value", "value1")));
        form.addElement(new Element(Tag.valueOf("input"), "", new Attributes("name", "name2", "value", "value2")));

        con = form.submit();
        assertNotNull(con);
        assertEquals("http://example.com/submit", con.request().url().toString());
        assertEquals(Connection.Method.POST, con.request().method());
        assertEquals(2, con.request().data().size());
        assertEquals("value1", con.request().data().get("name1"));
        assertEquals("value2", con.request().data().get("name2"));
    }
    
    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        List<Connection.KeyVal> data = form.formData();
        assertNotNull(data);
        assertEquals(0, data.size());

        Element input1 = new Element(Tag.valueOf("input"), "", new Attributes("name", "name1", "value", "value1"));
        Element input2 = new Element(Tag.valueOf("input"), "", new Attributes("name", "name2", "value", "value2"));
        form.addElement(input1);
        form.addElement(input2);

        data = form.formData();
        assertNotNull(data);
        assertEquals(2, data.size());
        assertEquals("name1", data.get(0).key());
        assertEquals("value1", data.get(0).value());
        assertEquals("name2", data.get(1).key());
        assertEquals("value2", data.get(1).value());

        input1.attr("name", "");
        data = form.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("name2", data.get(0).key());
        assertEquals("value2", data.get(0).value());
    }

    @Test
    public void testEquals() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement form3 = new FormElement(Tag.valueOf("form"), "http://example.com/submit", new Attributes());

        assertEquals(form1, form2);
        assertNotEquals(form1, form3);
        assertNotEquals(form2, form3);
    }
}