import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        // Test case 1: Checking if empty form contains zero elements
        Assert.assertEquals(0, formElement.elements().size());

        // Test case 2: Adding a form control element and checking if the form contains one element
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(inputElement);
        Assert.assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        // Test case 1: Submitting a form without action URL, it should throw an IllegalArgumentException
        try {
            formElement.submit();
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Test passed, IllegalArgumentException is expected
        }

        // Test case 2: Submitting a form with action URL, it should return a valid Connection object
        formElement.attributes().put("action", "/submit-form");
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        // Test case 1: Testing with an empty form, it should return an empty list
        List<Connection.KeyVal> data = formElement.formData();
        Assert.assertEquals(0, data.size());

        // Test case 2: Adding a valid form control element and checking if the form data contains the added element
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attributes().put("name", "username");
        inputElement.attributes().put("value", "John Doe");
        formElement.addElement(inputElement);
        data = formElement.formData();
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("username", data.get(0).key());
        Assert.assertEquals("John Doe", data.get(0).value());

        // Test case 3: Adding a disabled form control element, it should be skipped in form data
        Element disabledElement = new Element(Tag.valueOf("input"), "", new Attributes());
        disabledElement.attributes().put("name", "password");
        disabledElement.attributes().put("value", "123456");
        disabledElement.attributes().put("disabled", "");
        formElement.addElement(disabledElement);
        data = formElement.formData();
        Assert.assertEquals(1, data.size()); // The disabled element should not be included in the form data
    }
}