package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    private static final String TEST_HTML = "<form action=\"https://example.com/\">" +
            "<input type=\"text\" name=\"field1\" value=\"value1\">" +
            "<input type=\"text\" name=\"field2\" value=\"value2\">" +
            "<select name=\"field3\">" +
            "<option value=\"option1\" selected>Option 1</option>" +
            "<option value=\"option2\">Option 2</option>" +
            "</select>" +
            "<input type=\"checkbox\" name=\"field4\" value=\"value4\">" +
            "<input type=\"checkbox\" name=\"field4\" value=\"value4\" checked>" +
            "</form>";

    private FormElement formElement;

    @Before
    public void setUp() {
        FormParser parser = new FormParser();
        Document doc = parser.parseInput(TEST_HTML, "src/test/resources");
        formElement = (FormElement) doc.select("form").first();
    }

    @Test
    public void testElements() {
        assertEquals(5, formElement.elements().size());
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element("input");
        formElement.addElement(inputElement);
        assertEquals(6, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertTrue(connection.method().name().equalsIgnoreCase("POST") || connection.method().name().equalsIgnoreCase("GET"));
        assertEquals("https://example.com/", connection.url());
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(5, formData.size());
        List<String> names = new ArrayList<>(Arrays.asList("field1", "field2", "field3", "field4", "field4"));
        for (int i = 0; i < formData.size(); i++) {
            assertEquals(names.get(i), formData.get(i).key());
        }
        List<String> values = new ArrayList<>(Arrays.asList("value1", "value2", "option1", "value4", "value4"));
        for (int i = 0; i < formData.size(); i++) {
            assertEquals(values.get(i), formData.get(i).value());
        }
    }

    @Test
    public void testSubmitWithDisabledInput() {
        FormElement formElementWithDisabledInput = new FormElement(new Tag("form"), null, new Attributes());
        Element inputElementWithDisabledAttribute = new Element("input");
        inputElementWithDisabledAttribute.attributes().put("disabled", "disabled");
        formElementWithDisabledInput.addElement(inputElementWithDisabledAttribute);
        Connection connection = formElementWithDisabledInput.submit();
        assertNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formElementWithoutBaseUri = new FormElement(new Tag("form"), null, new Attributes());
        Connection connection = formElementWithoutBaseUri.submit();
        throw new IllegalArgumentException("Could not determine a form action URL for submit.");
    }
}