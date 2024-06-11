package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseUtil;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><input type='text' name='name' /></form>");
        FormElement form = (FormElement) doc.select("form").first();
        assertEquals("form", form.tagName());
        assertEquals("/test", form.attr("action"));
        assertEquals("post", form.attr("method"));
        assertEquals("testForm", form.attr("name"));
    }

    @Test
    public void testGetElements() {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><input type='text' name='name1' /><input type='text' name='name2' /><select name='selectName'><option value='option1'>Option 1</option><option value='option2' selected><option value='option3'>Option 3</option></select></form>");
        FormElement form = (FormElement) doc.select("form").first();
        Elements elements = form.elements();
        assertEquals(3, elements.size());
        assertEquals("input", elements.get(0).tagName());
        assertEquals("input[name='name1']", elements.get(0).toString());
        assertEquals("select", elements.get(1).tagName());
        assertEquals("select[name='selectName']", elements.get(1).toString());
        assertEquals("option", elements.get(2).tagName());
        assertEquals("option[value='option2']", elements.get(2).toString());
    }

    @Test
    public void testAddElement() {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'></form>");
        FormElement form = (FormElement) doc.select("form").first();
        Element input = ParseUtil.parseTag("input", null, new Attributes().put("type", "text").put("name", "name"));
        form.addElement(input);
        Elements elements = form.elements();
        assertEquals(1, elements.size());
        assertEquals("input", elements.get(0).tagName());
        assertEquals("input[name='name']", elements.get(0).toString());
    }

    @Test
    public void testSubmit() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><input type='text' name='name' value='John Doe' /></form>");
        FormElement form = (FormElement) doc.select("form").first();
        Connection con = form.submit();
        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("name", "John Doe"));
        List<Connection.KeyVal> actualData = con.requestBody();
        assertSame(expectedData, actualData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() throws Exception {
        Document doc = Jsoup.parse("<form action='' name='testForm' method='post'><input type='text' name='name' value='John Doe' /></form>");
        FormElement form = (FormElement) doc.select("form").first();
        form.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNullForm() {
        FormElement form = null;
        try {
            form.submit();
        } catch (IllegalArgumentException e) {
            assertNull(form);
            throw e;
        }
    }

    @Test
    public void testFormData() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><input type='text' name='name1' value='John Doe' /><select name='selectName'><option value='option1'>Option 1</option><option value='option2' selected><option value='option3'>Option 3</option></select></form>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> expectedData = Arrays.asList(
                new Connection.KeyVal("name1", "John Doe"),
                new Connection.KeyVal("selectName", "option2"));
        List<Connection.KeyVal> actualData = form.formData();
        assertSame(expectedData, actualData);
    }

    @Test
    public void testFormDataWithNoFormControls() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'></form>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> actualData = form.formData();
        assertTrue(actualData.isEmpty());
    }

    @Test
    public void testFormDataWithEmptyForm() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'></form>");
        FormElement form = (FormElement) doc.select("form").first();
        Element input = ParseUtil.parseTag("input", null, new Attributes().put("type", "text").put("name", "name").put("value", ""));
        form.addElement(input);
        List<Connection.KeyVal> actualData = form.formData();
        assertTrue(actualData.isEmpty());
    }

    @Test
    public void testFormDataWithDisabledFormControl() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><input type='text' name='name' disabled /></form>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> actualData = form.formData();
        assertTrue(actualData.isEmpty());
    }

    @Test
    public void testFormDataWithNoNameFormControl() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><input type='text' value='John Doe' /></form>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> actualData = form.formData();
        assertTrue(actualData.isEmpty());
    }

    @Test
    public void testFormDataWithNoValueFormControl() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><input type='text' name='name' /></form>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> actualData = form.formData();
        assertTrue(actualData.isEmpty());
    }

    @Test
    public void testFormDataWithSelectAndSingleValue() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><select name='selectName'><option value='option1'>Option 1</option><option value='option2' selected><option value='option3'>Option 3</option></select></form>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> expectedData = Arrays.asList(new Connection.KeyVal("selectName", "option2"));
        List<Connection.KeyVal> actualData = form.formData();
        assertSame(expectedData, actualData);
    }

    @Test
    public void testFormDataWithSelectAndMultipleValues() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><select name='selectName' multiple><option value='option1'>Option 1</option><option value='option2' selected><option value='option3'>Option 3</option></select></form>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> expectedData = Arrays.asList(new Connection.KeyVal("selectName", "option2"), new Connection.KeyVal("selectName", "option1"));
        List<Connection.KeyVal> actualData = form.formData();
        assertSame(expectedData, actualData);
    }

    @Test
    public void testFormDataWithCheckboxAndChecked() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><input type='checkbox' name='checkboxName' checked /></form>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> expectedData = Arrays.asList(new Connection.KeyVal("checkboxName", ""));
        List<Connection.KeyVal> actualData = form.formData();
        assertSame(expectedData, actualData);
    }

    @Test
    public void testFormDataWithCheckboxAndUnchecked() throws Exception {
        Document doc = Jsoup.parse("<form action='/test' name='testForm' method='post'><input type='checkbox' name='checkboxName' /></form>");
        FormElement form = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> actualData = form.formData();
        assertTrue(actualData.isEmpty());

        Element checkbox = ParseUtil.parseTag("input", null, new Attributes().put("type", "checkbox").put("name", "checkboxName").put("checked", "checked"));
        form.addElement(checkbox);
        List<Connection.KeyVal> expectedData = Arrays.asList(new Connection.KeyVal("checkboxName", ""));
        List<Connection.KeyVal> actualData2 = form.formData();
        assertSame(expectedData, actualData2);
    }
}