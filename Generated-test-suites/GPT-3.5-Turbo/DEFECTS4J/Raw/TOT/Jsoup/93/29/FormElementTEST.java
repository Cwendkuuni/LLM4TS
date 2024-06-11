import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(
                Tag.valueOf("form"),
                "http://example.com",
                new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull("Elements should not be null", elements);
        assertTrue("Elements should be empty", elements.isEmpty());

        Element inputElement = new Element(
                Tag.valueOf("input"),
                formElement.baseUri(),
                new Attributes());
        formElement.addElement(inputElement);
        assertFalse("Elements should not be empty after adding an element", elements.isEmpty());
        assertEquals("Elements should contain the added element", inputElement, elements.first());
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element(
                Tag.valueOf("input"),
                formElement.baseUri(),
                new Attributes());

        FormElement returnedFormElement = formElement.addElement(inputElement);
        assertSame("Returned form element should be the same as the original form element", formElement, returnedFormElement);
        assertTrue("Elements should not be empty after adding an element", formElement.elements().size() > 0);
        assertEquals("Elements should contain the added element", inputElement, formElement.elements().first());
    }

    @Test
    public void testRemoveChild() {
        Element inputElement = new Element(
                Tag.valueOf("input"),
                formElement.baseUri(),
                new Attributes());
        formElement.addElement(inputElement);

        formElement.removeChild(inputElement);
        assertTrue("Elements should be empty after removing the added element", formElement.elements().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutActionAttribute() {
        formElement.submit();
    }

    @Test
    public void testSubmitWithEmptyMethodAttribute() {
        formElement.attr("action", "http://example.com");

        Connection connection = formElement.submit();
        assertEquals("Connection URL should be equal to the form action attribute", "http://example.com", connection.url().toString());
        assertEquals("Connection method should be GET by default", Connection.Method.GET, connection.method());
    }

    @Test
    public void testSubmitWithMethodAttribute() {
        formElement.attr("action", "http://example.com");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();
        assertEquals("Connection URL should be equal to the form action attribute", "http://example.com", connection.url().toString());
        assertEquals("Connection method should be POST", Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(
                Tag.valueOf("input"),
                formElement.baseUri(),
                new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.attr("type", "text");
        inputElement.val("inputValue");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals("Form data list should have a size of 1", 1, formData.size());
        assertEquals("Form data key should match the name attribute of the input element", "inputName", formData.get(0).key());
        assertEquals("Form data value should macth the value of the input element", "inputValue", formData.get(0).value());
    }

}