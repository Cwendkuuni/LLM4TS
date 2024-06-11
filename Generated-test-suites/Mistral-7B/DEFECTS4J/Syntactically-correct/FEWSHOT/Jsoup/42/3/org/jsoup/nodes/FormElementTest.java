package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form><input name='input1' type='text' value='value1'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Element input = form.elements().first();

        assertEquals("form", form.tagName());
        assertEquals("input", input.tagName());
        assertEquals("value1", input.val());
        assertEquals("input1", input.attr("name"));
    }

    @Test
    public void testFormElementWithNoName() {
        String html = "<form><input type='text'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Element input = form.elements().size() > 0 ? form.elements().get(0) : null;

        assertNull(input);
    }

    @Test
    public void testFormElementWithNoInputs() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();

        assertTrue(form.elements().isEmpty());
    }

    @Test
    public void testFormElementWithMultipleInputs() {
        String html = "<form><input name='input1' type='text' value='value1'><input name='input2' type='text' value='value2'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();

        List<Element> inputs = form.elements();
        assertEquals(2, inputs.size());
        Element input1 = inputs.get(0);
        Element input2 = inputs.get(1);

        assertEquals("input", input1.tagName());
        assertEquals("input", input2.tagName());
        assertEquals("value1", input1.val());
        assertEquals("value2", input2.val());
        assertEquals("input1", input1.attr("name"));
        assertEquals("input2", input2.attr("name"));
    }

    @Test
    public void testFormElementWithSelectElement() {
        String html = "<form><select name='select'><option value='option1'>Option 1</option><option value='option2'>Option 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Element select = form.elements().first();

        assertEquals("select", select.tagName());
        assertEquals(2, select.select("option").size());
        Element option1 = select.select("option:first-child");
        Element option2 = select.select("option:last-child");

        assertEquals("Option 1", option1.text());
        assertEquals("option1", option1.attr("value"));
        assertEquals("Option 2", option2.text());
        assertEquals("option2", option2.attr("value"));
    }

    @Test
    public void testFormElementWithCheckboxes() {
        String html = "<form><input type='checkbox' name='checkbox' value='value'>Checkbox</form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Element checkbox = form.elements().first();

        assertTrue(checkbox.hasAttr("checked"));
        assertEquals("checkbox", checkbox.attr("type"));
        assertEquals("value", checkbox.val());
        assertEquals("checkbox", checkbox.attr("name"));

        checkbox.removeAttr("checked");
        assertFalse(checkbox.hasAttr("checked"));
    }

    @Test
    public void testFormElementWithMultipleCheckboxes() {
        String html = "<form><input type='checkbox' name='checkbox1' value='value1'>Checkbox 1<input type='checkbox' name='checkbox2' value='value2'>Checkbox 2</form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Element checkbox1 = form.elements().get(0);
        Element checkbox2 = form.elements().get(1);

        assertTrue(checkbox1.hasAttr("checked"));
        assertTrue(checkbox2.hasAttr("checked"));

        checkbox1.removeAttr("checked");
        assertFalse(checkbox1.hasAttr("checked"));

        checkbox2.removeAttr("checked");
        assertFalse(checkbox2.hasAttr("checked"));
    }

    @Test
    public void testFormElementSubmit() {
        String html = "<form action='http://example.com' method='post'><input name='input' type='text' value='value'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();

        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();

        assertEquals("POST", connection.method().name());
        assertEquals("http://example.com", connection.url().toString());
        assertEquals(1, data.size());
        Connection.KeyVal keyVal = data.get(0);
        assertEquals("input", keyVal.key());
        assertEquals("value", keyVal.value());
    }

    @Test
    public void testFormElementWithMultipleInputsSubmit() {
        String html = "<form action='http://example.com' method='post'><input name='input1' type='text' value='value1'><input name='input2' type='text' value='value2'></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();

        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();

        assertEquals(2, data.size());
        Connection.KeyVal keyVal1 = data.get(0);
        Connection.KeyVal keyVal2 = data.get(1);

        assertEquals("input1", keyVal1.key());
        assertEquals("value1", keyVal1.value());
        assertEquals("input2", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }

    @Test
    public void testFormElementWithSelectSubmit() {
        String html = "<form action='http://example.com' method='post'><select name='select'><option value='option1'>Option 1</option><option value='option2'>Option 2</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Element select = form.elements().first();
        Elements options = select.select("option[selected]");

        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();

        assertEquals(1, data.size());
        Connection.KeyVal keyVal = data.get(0);
        assertEquals("select", keyVal.key());
        String selectedOptionValue = options.size() > 0 ? options.get(0).val() : null;
        assertEquals(selectedOptionValue, keyVal.value());
    }

    @Test
    public void testFormElementWithMultipleSelectsSubmit() {
        String html = "<form action='http://example.com' method='post'><select name='select1'><option value='option1'>Option 1</option><option value='option2'>Option 2</option></select><select name='select2'><option value='option3'>Option 3</option><option value='option4'>Option 4</option></select></form>";
        Document doc = Jsoup.parse(html);
        FormElement form = doc.select("form").first();
        Element select1 = form.elements().get(0);
        Element select2 = form.elements().get(1);
        Elements options1 = select1.select("option[selected]");
        Elements options2 = select2.select("option[selected]");

        Connection connection = form.submit();
        List<Connection.KeyVal> data = form.formData();

        assertEquals(2, data.size());
        Connection.KeyVal keyVal1 = data.get(0);
        Connection.KeyVal keyVal2 = data.get(1);

        assertEquals("select1", keyVal1.key());
        String selectedOptionValue1 = options1.size() > 0 ? options1.get(0).val() : null;
        assertEquals(selectedOptionValue1, keyVal1.value());

        assertEquals("select2", keyVal2.key());
        String selectedOptionValue2 = options2.size() > 0 ? options2.get(0).val() : null;
        assertEquals(selectedOptionValue2, keyVal2.value());
    }
}