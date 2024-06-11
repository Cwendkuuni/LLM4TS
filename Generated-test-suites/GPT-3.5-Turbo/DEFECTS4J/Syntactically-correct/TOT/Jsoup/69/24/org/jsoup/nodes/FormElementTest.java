import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();

        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();

        Assert.assertNotNull("Form controls should not be null", elements);
        Assert.assertTrue("Form controls should be empty", elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());

        FormElement updatedForm = formElement.addElement(element);

        Assert.assertEquals("Form element should have one added element", 1, updatedForm.elements().size());
        Assert.assertEquals("Added element should be the same as the provided element", element, updatedForm.elements().get(0));
    }

    @Test
    public void testSubmit_noActionUrl() {
        Connection connection = formElement.submit();

        Assert.assertNull("Connection should be null if no action URL is set", connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_invalidActionUrl() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "");

        FormElement invalidForm = new FormElement(tag, baseUri, attributes);
        invalidForm.submit();
    }

    @Test
    public void testSubmit_validActionUrl_getMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "/form-submit");
        attributes.put("method", "GET");

        FormElement getForm = new FormElement(tag, baseUri, attributes);
        Connection connection = getForm.submit();

        Assert.assertNotNull("Connection should not be null for a valid action URL and GET method", connection);
        Assert.assertEquals("Connection URL should match the form action URL", "https://www.example.com/form-submit", connection.request().url().toString());
        Assert.assertEquals("Method should be GET", Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testSubmit_validActionUrl_postMethod() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "/form-submit");
        attributes.put("method", "POST");

        FormElement postForm = new FormElement(tag, baseUri, attributes);
        Connection connection = postForm.submit();

        Assert.assertNotNull("Connection should not be null for a valid action URL and POST method", connection);
        Assert.assertEquals("Connection URL should match the form action URL", "https://www.example.com/form-submit", connection.request().url().toString());
        Assert.assertEquals("Method should be POST", Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData_noElements() {
        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull("Form data should not be null", formData);
        Assert.assertTrue("Form data should be empty", formData.isEmpty());
    }

    @Test
    public void testFormData_withElements() {
        Tag inputTag = Tag.valueOf("input");
        String inputBaseUri = formElement.baseUri();
        Attributes inputAttributes = new Attributes();
        inputAttributes.put("name", "inputName");
        inputAttributes.put("value", "inputValue");

        Element inputElement = new Element(inputTag, inputBaseUri, inputAttributes);
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertNotNull("Form data should not be null", formData);
        Assert.assertEquals("Form data should have one entry", 1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals("Form data key should match the element name", "inputName", keyVal.key());
        Assert.assertEquals("Form data value should match the element value", "inputValue", keyVal.value());
    }

}