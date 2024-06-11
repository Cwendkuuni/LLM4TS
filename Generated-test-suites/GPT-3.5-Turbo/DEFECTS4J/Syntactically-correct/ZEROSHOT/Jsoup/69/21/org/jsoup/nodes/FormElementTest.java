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
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement returnedFormElement = formElement.addElement(element);
        assertEquals(formElement, returnedFormElement);
        assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithInputElement() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.attr("value", "inputValue");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("inputValue", keyVal.value());
    }

    // Add more tests for other methods...

}