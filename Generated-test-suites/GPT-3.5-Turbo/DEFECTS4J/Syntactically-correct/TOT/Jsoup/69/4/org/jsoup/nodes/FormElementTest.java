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
        formElement = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());

        Element inputElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        formElement.addElement(inputElement);

        elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(inputElement));

        Element buttonElement = new Element(Tag.valueOf("button"), "https://example.com", new Attributes());
        formElement.addElement(buttonElement);

        elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(2, elements.size());
        assertTrue(elements.contains(buttonElement));
    }

    @Test
    public void testSubmit() {
        // Test case for form with action URL
        formElement.attributes().put("action", "/submit");
        formElement.attributes().put("method", "POST");

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("https://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());

        // Test case for form with base URI
        formElement.attributes().remove("action");

        connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("https://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        // Test case for empty form elements
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(0, data.size());

        // Test case for non-submittable form element
        Element nonSubmittableElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        nonSubmittableElement.attributes().put("type", "hidden");
        nonSubmittableElement.attributes().put("name", "hiddenField");
        formElement.addElement(nonSubmittableElement);

        data = formElement.formData();
        assertNotNull(data);
        assertEquals(0, data.size());

        // Test case for disabled form element
        Element disabledElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        disabledElement.attributes().put("type", "text");
        disabledElement.attributes().put("name", "disabledField");
        disabledElement.attributes().put("disabled", "");
        formElement.addElement(disabledElement);

        data = formElement.formData();
        assertNotNull(data);
        assertEquals(0, data.size());

        // Test case for select with selected options
        Element selectElement = new Element(Tag.valueOf("select"), "https://example.com", new Attributes());
        selectElement.attributes().put("name", "selectField");

        Element option1 = new Element(Tag.valueOf("option"), "https://example.com", new Attributes());
        option1.attr("selected", "");
        option1.text("Option 1");

        Element option2 = new Element(Tag.valueOf("option"), "https://example.com", new Attributes());
        option2.attr("selected", "");
        option2.text("Option 2");

        Element option3 = new Element(Tag.valueOf("option"), "https://example.com", new Attributes());
        option3.attr("selected", "");
        option3.text("Option 3");

        selectElement.appendChild(option1);
        selectElement.appendChild(option2);
        selectElement.appendChild(option3);

        formElement.addElement(selectElement);

        data = formElement.formData();
        assertNotNull(data);
        assertEquals(3, data.size());
        assertTrue(data.contains(HttpConnection.KeyVal.create("selectField", "Option 1")));
        assertTrue(data.contains(HttpConnection.KeyVal.create("selectField", "Option 2")));
        assertTrue(data.contains(HttpConnection.KeyVal.create("selectField", "Option 3")));

        // Test case for select without selected options
        selectElement.select("option[selected]").removeAttr("selected");

        data = formElement.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertTrue(data.contains(HttpConnection.KeyVal.create("selectField", "Option 1")));

        // Test case for checkbox
        Element checkboxElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        checkboxElement.attributes().put("type", "checkbox");
        checkboxElement.attributes().put("name", "checkboxField");
        checkboxElement.attributes().put("checked", "");

        formElement.addElement(checkboxElement);

        data = formElement.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertTrue(data.contains(HttpConnection.KeyVal.create("checkboxField", "on")));

        // Test case for radio button
        Element radioButton = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        radioButton.attributes().put("type", "radio");
        radioButton.attributes().put("name", "radioField");
        radioButton.attributes().put("checked", "");

        formElement.addElement(radioButton);

        data = formElement.formData();
        assertNotNull(data);
        assertEquals(1, data.size());
        assertTrue(data.contains(HttpConnection.KeyVal.create("radioField", "on")));

        // Test case for other form control elements
        Element textElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        textElement.attributes().put("type", "text");
        textElement.attributes().put("name", "textField");
        textElement.attributes().put("value", "Input Value");

        formElement.addElement(textElement);

        data = formElement.formData();
        assertNotNull(data);
        assertEquals(2, data.size());
        assertTrue(data.contains(HttpConnection.KeyVal.create("textField", "Input Value")));
    }
}