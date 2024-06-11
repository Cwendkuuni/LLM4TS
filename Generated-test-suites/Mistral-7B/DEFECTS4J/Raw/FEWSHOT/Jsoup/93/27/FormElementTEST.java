package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form action=\"/test\"> <input type=\"text\" name=\"name\" value=\"John\"/> <input type=\"submit\" name=\"submit\"/> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        assertEquals("form", form.tag().name());
        assertEquals("/test", form.absUrl("action"));
    }

    @Test
    public void testElements() {
        String html = "<form action=\"/test\"> <input type=\"text\" name=\"name1\" value=\"John1\"/> <input type=\"text\" name=\"name2\" value=\"John2\"/> <input type=\"submit\" name=\"submit\"/> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Element> elements = form.elements().select("input");
        assertEquals(3, elements.size());
        Element name1 = elements.get(0);
        Element name2 = elements.get(1);
        Element submit = elements.get(2);
        assertEquals("name1", name1.attr("name"));
        assertEquals("John1", name1.val());
        assertEquals("name2", name2.attr("name"));
        assertEquals("John2", name2.val());
        assertEquals("submit", submit.tagName());
        assertNull(submit.val());
    }

    @Test
    public void testAddElement() {
        String html = "<form action=\"/test\"> <input type=\"text\" name=\"name1\" value=\"John1\"/> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Element input = new Element("input", new Attributes().put("type", "text")
                .put("name", "name2").put("value", "John2"));
        form.addElement(input);
        List<Element> elements = form.elements().select("input");
        assertEquals(2, elements.size());
        Element name1 = elements.get(0);
        Element name2 = elements.get(1);
        assertEquals("name1", name1.attr("name"));
        assertEquals("John1", name1.val());
        assertEquals("name2", name2.attr("name"));
        assertEquals("John2", name2.val());
    }

    @Test
    public void testRemoveElement() {
        String html = "<form action=\"/test\"> <input type=\"text\" name=\"name1\" value=\"John1\"/> <input type=\"text\" name=\"name2\" value=\"John2\"/> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Element name1 = form.selectFirst("input[name=name1]");
        form.removeChild(name1);
        List<Element> elements = form.elements().select("input");
        assertEquals(1, elements.size());
        Element name2 = elements.get(0);
        assertEquals("name2", name2.attr("name"));
        assertEquals("John2", name2.val());
    }

    @Test
    public void testSubmit() throws Exception {
        String html = "<form action=\"/test\"> <input type=\"text\" name=\"name\" value=\"John\"/> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        Connection connection = form.submit();
        assertEquals("/test", connection.url().toString());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("John", keyVal.value());
    }

    @Test
    public void testSelectElementWithSelectedOption() {
        String html = "<form action=\"/test\"> <select name=\"name\"> <option value=\"John\" selected>John</option> </select> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("John", keyVal.value());
    }

    @Test
    public void testSelectElementWithNoSelectedOption() {
        String html = "<form action=\"/test\"> <select name=\"name\"> <option value=\"John\">John</option> </select> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testCheckboxOrRadioWithChecked() {
        String html = "<form action=\"/test\"> <input type=\"checkbox\" name=\"name\" value=\"John\" checked/> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("John", keyVal.value());
    }

    @Test
    public void testCheckboxOrRadioWithoutChecked() {
        String html = "<form action=\"/test\"> <input type=\"checkbox\" name=\"name\" value=\"John\"/> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormWithSubmitAndOtherInputs() {
        String html = "<form action=\"/test\"> <input type=\"submit\" name=\"submit\"/> <input type=\"text\" name=\"name1\" value=\"John1\"/> <input type=\"text\" name=\"name2\" value=\"John2\"/> </form>";
        Document doc = Jsoup.parse(html);
        FormElement form = (FormElement) doc.selectFirst("form");
        List<Element> elements = form.elements().select("input");
        assertEquals(3, elements.size());
        Element submit = elements.get(0);
        assertEquals("submit", submit.tagName());
        assertNull(submit.val());
        Element name1 = elements.get(1);
        assertEquals("name1", name1.attr("name"));
        assertEquals("John1", name1.val());
        Element name2 = elements.get(2);
        assertEquals("name2", name2.attr("name"));
        assertEquals("John2", name2.val());
        List<Connection.KeyVal> formData = form.formData();
        assertEquals(2, formData.size());
        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("name1", keyVal1.key());
        assertEquals("John1", keyVal1.value());
        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("name2", keyVal2.key());
        assertEquals("John2", keyVal2.value());
    }
}