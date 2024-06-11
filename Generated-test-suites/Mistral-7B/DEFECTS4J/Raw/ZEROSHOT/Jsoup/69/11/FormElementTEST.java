import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormElementTest {

    private static final String HTML_FORM_STRING = "<form action=\"http://test.com\" method=\"post\">" +
            "<input type=\"text\" name=\"inputName\" value=\"inputValue\">" +
            "<select name=\"selectName\">" +
            "<option selected value=\"optionValue1\">Option 1</option>" +
            "<option value=\"optionValue2\">Option 2</option>" +
            "</select>" +
            "<input type=\"checkbox\" name=\"checkboxName\" value=\"checkboxValue\" checked>" +
            "</form>";

    private Element formElement;

    @Before
    public void setUp() {
        formElement = Jsoup.parseBodyFragment(HTML_FORM_STRING).getElementsByTag("form").first();
    }

    @Test
    public void testFormElementCreation() {
        assertNotNull(formElement);
        assertInstanceOf(FormElement.class, formElement);
    }

    @Test
    public void testElementsMethod() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(5, elements.size());
    }

    @Test
    public void testAddElementMethod() {
        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "addedName");
        inputElement.attr("value", "addedValue");

        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertEquals(6, elements.size());
        assertNotNull(elements.get(5));
        assertEquals("input", elements.get(5).tagName());
        assertEquals("addedName", elements.get(5).attr("name"));
        assertEquals("addedValue", elements.get(5).attr("value"));
    }

    @Test
    public void testSubmitMethod() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("POST", connection.method().name());
        assertEquals("http://test.com", connection.url().toString());
    }

    @Test
    public void testFormDataMethod() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(7, formData.size()); // 5 original + 2 select + 1 checkbox

        for (Connection.KeyVal keyVal : formData) {
            String name = keyVal.key();
            String value = keyVal.value();

            if (name.equals("inputName")) {
                assertEquals("inputValue", value);
            } else if (name.equals("selectName")) {
                assertEquals("optionValue1", value);
            } else if (name.equals("checkboxName")) {
                assertEquals("checkboxValue", value);
            }
        }
    }
}