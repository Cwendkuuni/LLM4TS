package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    private static final String BASE_URI = "http://example.com";
    private static final String FORM_HTML = "<form action='/submit' method='post'>\n" +
            "<input name='input1' type='text' value='value1'>\n" +
            "<input name='input2' type='text' value='value2' disabled>\n" +
            "<select id='select1' name='select1'>\n" +
            "    <option value='option1' selected>Option 1</option>\n" +
            "    <option value='option2'>Option 2</option>\n" +
            "</select>\n" +
            "<input type='hidden' name='hidden1' value='hiddenValue1'>\n" +
            "</form>";
    private Element formElement;

    @Before
    public void setUp() {
        Document document = Jsoup.parse(FORM_HTML);
        formElement = document.select("form").first();
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(4, elements.size());
        assertTrue(elements.get(0) instanceof InputElement);
        assertTrue(elements.get(1) instanceof InputElement);
        assertTrue(elements.get(2) instanceof SelectElement);
        assertTrue(elements.get(3) instanceof InputElement);
    }

    @Test
    public void testAddElement() {
        InputElement input = new InputElement("input", null, null, new Attributes());
        formElement.addElement(input);
        Elements elements = formElement.elements();
        assertEquals(5, elements.size());
        assertSame(input, elements.get(4));
    }

    @Test
    public void testSubmit() {
        List<Connection.KeyVal> formData = formElement.submit().data();
        assertEquals(5, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("input1", keyVal1.key());
        assertEquals("value1", keyVal1.val());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("select1", keyVal2.key());
        assertEquals("option1", keyVal2.val());

        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("hidden1", keyVal3.key());
        assertNull(keyVal3.val());

        Connection.KeyVal keyVal4 = formData.get(3);
        assertNull(keyVal4);

        Connection.KeyVal keyVal5 = formData.get(4);
        assertNull(keyVal5);
    }

    @Test
    public void testFormDataWithDisabledInput() {
        List<Connection.KeyVal> formData = formElement.submit().data();
        assertEquals(3, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("input1", keyVal1.key());
        assertEquals("value1", keyVal1.val());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("select1", keyVal2.key());
        assertEquals("option1", keyVal2.val());

        Connection.KeyVal keyVal3 = formData.get(2);
        assertNull(keyVal3);
    }

    @Test
    public void testFormDataWithNoName() {
        List<Connection.KeyVal> formData = formElement.submit().data();
        assertEquals(4, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("input1", keyVal1.key());
        assertEquals("value1", keyVal1.val());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("select1", keyVal2.key());
        assertEquals("option1", keyVal2.val());

        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("hidden1", keyVal3.key());
        assertNull(keyVal3.val());

        Connection.KeyVal keyVal4 = formData.get(3);
        assertNull(keyVal4.key());
        assertNull(keyVal4.val());
    }

    @Test
    public void testFormDataWithMultipleSelects() {
        String formHtmlWithMultipleSelects = FORM_HTML +
                "<select id='select2' name='select2'>\n" +
                "    <option value='option3' selected>Option 3</option>\n" +
                "    <option value='option4'>Option 4</option>\n" +
                "</select>\n";

        Document documentWithMultipleSelects = Jsoup.parse(formHtmlWithMultipleSelects);
        FormElement formElementWithMultipleSelects = (FormElement) documentWithMultipleSelects.select("form").first();
        List<Connection.KeyVal> formData = formElementWithMultipleSelects.submit().data();
        assertEquals(7, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("input1", keyVal1.key());
        assertEquals("value1", keyVal1.val());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("select1", keyVal2.key());
        assertEquals("option1", keyVal2.val());

        Connection.KeyVal keyVal3 = formData.get(2);
        assertEquals("hidden1", keyVal3.key());
        assertNull(keyVal3.val());

        Connection.KeyVal keyVal4 = formData.get(3);
        assertEquals("select2", keyVal4.key());
        assertEquals("option3", keyVal4.val());

        Connection.KeyVal keyVal5 = formData.get(4);
        assertNull(keyVal5);

        Connection.KeyVal keyVal6 = formData.get(5);
        assertNull(keyVal6.key());
        assertNull(keyVal6.val());

        Connection.KeyVal keyVal7 = formData.get(6);
        assertNull(keyVal7.key());
        assertNull(keyVal7.val());
    }

    @Test
    public void testSubmitWithNullFormElement() {
        assertNull(FormElement.class.cast(null).submit());
    }
}