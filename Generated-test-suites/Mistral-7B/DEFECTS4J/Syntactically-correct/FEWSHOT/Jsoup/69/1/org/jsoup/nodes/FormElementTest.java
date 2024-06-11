package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class FormElementTest {

    private Element form;
    private Element inputText;
    private Element inputHidden;
    private Element select;
    private Element option1;
    private Element option2;
    private Element checkbox;
    private Element radio;

    @Before
    public void setup() {
        String html = "<html><body>" +
                "<form action='http://test.com' method='POST'>" +
                "<input type='text' name='inputText' value='testText'>" +
                "<input type='hidden' name='inputHidden' value='testHidden'>" +
                "<select name='select'>" +
                "<option value='option1'>Option 1</option>" +
                "<option selected value='option2'>Option 2</option>" +
                "</select>" +
                "<input type='checkbox' name='checkbox' value='checkedValue' checked>" +
                "<input type='radio' name='radio' value='radioValue' checked>" +
                "</form>" +
                "</body></html>";

        Document doc = Jsoup.parse(html);
        form = doc.selectFirst("form");
        inputText = form.selectFirst("input[type=text]");
        inputHidden = form.selectFirst("input[type=hidden]");
        select = form.selectFirst("select");
        option1 = select.selectFirst("option[value=option1]");
        option2 = select.selectFirst("option[value=option2]");
        checkbox = form.selectFirst("input[type=checkbox]");
        radio = form.selectFirst("input[type=radio]");
    }

    @Test
    public void testFormElementConstruction() {
        FormElement formElement = (FormElement) form;
        assertNotNull(formElement.elements());
    }

    @Test
    public void testFormElementElements() {
        FormElement formElement = (FormElement) form;
        assertEquals(6, formElement.elements().size());
        assertEquals(inputText, formElement.elements().get(0));
        assertEquals(inputHidden, formElement.elements().get(1));
        assertEquals(select, formElement.elements().get(2));
        assertEquals(checkbox, formElement.elements().get(3));
        assertEquals(radio, formElement.elements().get(4));
    }

    @Test
    public void testFormElementAddElement() {
        FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());
        Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "textInput").put("value", "test"));
        formElement.addElement(textInput);

        assertEquals(1, formElement.elements().size());
        assertEquals(textInput, formElement.elements().get(0));
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        Connection connection = ((FormElement) form).submit();
        assertNotNull(connection);
        assertEquals("POST", connection.method());
        assertEquals("http://test.com", connection.url());
    }

    @Test
    public void testFormElementFormData() {
        FormElement formElement = (FormElement) form;
        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(6, formData.size());

        assertEquals("inputText", formData.get(0).key);
        assertEquals("testText", formData.get(0).value);

        assertEquals("inputHidden", formData.get(1).key);
        assertEquals("testHidden", formData.get(1).value);

        assertEquals("select", formData.get(2).key);
        assertEquals("option2", formData.get(2).value);

        assertEquals("checkbox", formData.get(3).key);
        assertTrue(formData.get(3).value.equals("checkedValue") || formData.get(3).value.equals("on"));

        assertEquals("radio", formData.get(4).key);
        assertTrue(formData.get(4).value.equals("radioValue") || formData.get(4).value.equals("on"));

        assertEquals("select", formData.get(5).key);
        assertEquals("option1", formData.get(5).value);
    }

    @Test
    public void testFormElementSubmitWithCustomAttributes() throws Exception {
        FormElement formElement = new FormElement(Tag.FORM, "", new Attributes().put("id", "myForm").put("class", "myClass"));
        Element textInput = new Element("input", new Attributes().put("type", "text").put("name", "textInput").put("value", "test").put("id", "myInput"));
        formElement.addElement(textInput);

        Connection connection = formElement.submit();
        assertEquals("POST", connection.method());
        assertEquals("http://test.com", connection.url());
        assertEquals("myForm", connection.header("X-JSoup-Form-ID"));
        assertEquals("myClass", connection.header("X-JSoup-Form-Class"));
        assertEquals("myInput", connection.data().get("textInput"));
    }
}