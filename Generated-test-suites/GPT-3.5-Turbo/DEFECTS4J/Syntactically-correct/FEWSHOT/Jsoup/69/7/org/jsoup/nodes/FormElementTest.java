package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class FormElementTest {
    private FormElement formElement;
    
    @Before
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmitWithAction() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals(formElement.formData(), connection.request().data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement1.attr("value", "option1");
        optionElement1.attr("selected");
        Element optionElement2 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement2.attr("value", "option2");
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        formElement.addElement(selectElement);
        Element inputElement1 = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement1.attr("type", "checkbox");
        inputElement1.attr("name", "checkbox");
        inputElement1.attr("checked");
        inputElement1.val("checkbox1");
        Element inputElement2 = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement2.attr("type", "checkbox");
        inputElement2.attr("name", "checkbox");
        inputElement2.val("checkbox2");
        Element inputElement3 = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement3.attr("type", "checkbox");
        inputElement3.attr("name", "checkbox");
        inputElement3.removeAttr("checked");
        inputElement3.val("checkbox3");
        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);
        formElement.addElement(inputElement3);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(4, formData.size());
        assertEquals("checkbox", formData.get(0).key());
        assertEquals("checkbox1", formData.get(0).value());
        assertEquals("checkbox", formData.get(1).key());
        assertEquals("checkbox2", formData.get(1).value());
        assertEquals("checkbox", formData.get(2).key());
        assertEquals("on", formData.get(2).value());
        assertEquals(selectElement.attr("name"), formData.get(3).key());
        assertEquals(optionElement1.attr("value"), formData.get(3).value());
    }
}