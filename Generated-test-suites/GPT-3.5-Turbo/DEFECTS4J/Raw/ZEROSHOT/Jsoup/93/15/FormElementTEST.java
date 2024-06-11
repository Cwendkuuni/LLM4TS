import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
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
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement newFormElement = formElement.addElement(element);
        Elements elements = newFormElement.elements();
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertEquals(element, elements.first());
    }
    
    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        FormElement formElementWithoutAction = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElementWithoutAction.submit();
    }
    
    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element1.attr("name", "name1");
        element1.val("value1");
        Element element2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element2.attr("name", "name2");
        element2.val("value2");

        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("name1", keyVal1.key());
        assertEquals("value1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("name2", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }

    @Test
    public void testFormDataWithDisabledInput() {
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element1.attr("name", "name1");
        element1.attr("disabled", "disabled");
        formElement.addElement(element1);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataWithSelect() {
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        select.attr("name", "name");
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("selected", "selected");
        option1.val("value1");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.val("value2");
        select.appendChild(option1);
        select.appendChild(option2);

        formElement.addElement(select);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value1", keyVal.value());
    }

    @Test
    public void testFormDataWithCheckbox() {
        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "name");
        checkbox.attr("checked", "checked");

        formElement.addElement(checkbox);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataWithRadio() {
        Element radio = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radio.attr("type", "radio");
        radio.attr("name", "name");
        radio.attr("checked", "checked");

        formElement.addElement(radio);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataWithValue() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element.attr("name", "name");
        element.val("value");

        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }
}