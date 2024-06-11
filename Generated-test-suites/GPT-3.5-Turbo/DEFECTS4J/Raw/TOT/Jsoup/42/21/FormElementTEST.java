import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.FormElement;
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
        String baseUri = "https://www.example.com";
        formElement = new FormElement(tag, baseUri, new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        
        // Check that initially no elements are associated with the form
        Assert.assertEquals(0, elements.size());

        // Add a form control element
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));

        // Check that the form control element has been added to the form
        Assert.assertEquals(1, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());

        // Add the form control element to the form
        formElement.addElement(element);

        // Check that the form control element has been added to the form
        Assert.assertTrue(formElement.elements().contains(element));
    }

    @Test
    public void testSubmit() throws IllegalArgumentException {
        // Test with no action attribute set - should throw IllegalArgumentException
        try {
            Connection connection = formElement.submit();
            Assert.fail("Expected IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }

        // Test with action attribute set to an empty string - should throw IllegalArgumentException
        formElement.attributes().put("action", "");
        try {
            Connection connection = formElement.submit();
            Assert.fail("Expected IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }

        // Test with action attribute set to a valid URL
        String validActionURL = "https://www.example.com/form-action";
        formElement.attributes().put("action", validActionURL);
        Connection connection = formElement.submit();

        // Check that the connection object is created with the correct action URL and method
        Assert.assertEquals(validActionURL, connection.request().url().toString());
        Assert.assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        // Add form control elements of different types
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "input");
        inputElement.val("input-value");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "select");
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("value", "1");
        optionElement.text("Option 1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        checkboxElement.val("checkbox-value");
        formElement.addElement(checkboxElement);

        // Get the data that the form submits
        List<Connection.KeyVal> formData = formElement.formData();

        // Check that the data contains the correct key-value pairs
        Assert.assertEquals(3, formData.size());
        Assert.assertEquals("input", formData.get(0).key());
        Assert.assertEquals("input-value", formData.get(0).value());
        Assert.assertEquals("select", formData.get(1).key());
        Assert.assertEquals("1", formData.get(1).value());
        Assert.assertEquals("checkbox", formData.get(2).key());
        Assert.assertEquals("checkbox-value", formData.get(2).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());

        // Check that two empty form elements are equal
        Assert.assertEquals(formElement, formElement2);

        // Add a form control element to formElement
        formElement.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));

        // Check that two form elements with different number of form control elements are not equal
        Assert.assertNotEquals(formElement, formElement2);

        // Add the same form control element to formElement2
        formElement2.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));

        // Check that two form elements with the same number of form control elements are equal
        Assert.assertEquals(formElement, formElement2);
    }
}