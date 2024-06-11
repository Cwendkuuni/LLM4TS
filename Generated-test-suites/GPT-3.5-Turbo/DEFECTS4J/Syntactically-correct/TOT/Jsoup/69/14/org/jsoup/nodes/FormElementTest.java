import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        Elements elements = new Elements();
        elements.add(new Element(Tag.valueOf("input"), formElement.baseUri()));
        formElement.addElement(new Element(Tag.valueOf("textarea"), formElement.baseUri()));

        assertEquals(elements, formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri());
        FormElement result = formElement.addElement(element1);

        assertTrue(formElement.elements().contains(element1));
        assertEquals(formElement, result);
    }

    @Test
    public void testSubmit() {
        // Test case for successful connection preparation
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "POST");
        formElement.addElement(new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "username").val("testUser"));
        formElement.addElement(new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "password").val("testPassword"));

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(2, connection.request().postDataCharset());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(formData, connection.request().requestBody().params());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionUrl() {
        // Test case for IllegalArgumentException when absolute action URL cannot be determined
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "username").val("testUser");
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "allowCookies").val("true").attr("type", "checkbox");
        checkboxElement.attr("checked", "");

        formElement.addElement(inputElement);
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("username", "testUser"));
        expectedData.add(HttpConnection.KeyVal.create("allowCookies", "true"));

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataSkipDisabledInputs() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "username").val("testUser");
        Element disabledInputElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "password").val("testPassword").attr("disabled", "");

        formElement.addElement(inputElement);
        formElement.addElement(disabledInputElement);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("username", "testUser"));

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataForSelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri()).attr("name", "country");
        selectElement.appendChild(new Element(Tag.valueOf("option"), formElement.baseUri()).attr("value", "USA").attr("selected", "").text("United States"));
        selectElement.appendChild(new Element(Tag.valueOf("option"), formElement.baseUri()).attr("value", "UK").text("United Kingdom"));

        formElement.addElement(selectElement);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("country", "USA"));

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataForEmptySelectElement() {
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri()).attr("name", "country");

        formElement.addElement(selectElement);

        List<Connection.KeyVal> expectedData = new ArrayList<>();

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataForCheckboxInput() {
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "enableNotifications").attr("type", "checkbox").attr("checked", "");
        Element otherCheckboxElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "newsletter").attr("type", "checkbox");

        formElement.addElement(checkboxElement);
        formElement.addElement(otherCheckboxElement);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("enableNotifications", "on"));

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataForRadioInput() {
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "gender").attr("type", "radio").attr("checked", "").val("male");
        Element otherRadioElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "gender").attr("type", "radio").val("female");

        formElement.addElement(radioElement);
        formElement.addElement(otherRadioElement);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("gender", "male"));

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(expectedData, formData);
    }

    @Test
    public void testFormDataForOtherInputTypes() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "username").val("testUser");
        Element passwordElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "password").val("testPassword").attr("type", "password");
        Element emailElement = new Element(Tag.valueOf("input"), formElement.baseUri()).attr("name", "email").val("test@example.com").attr("type", "email");

        formElement.addElement(inputElement);
        formElement.addElement(passwordElement);
        formElement.addElement(emailElement);

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("username", "testUser"));
        expectedData.add(HttpConnection.KeyVal.create("password", "testPassword"));
        expectedData.add(HttpConnection.KeyVal.create("email", "test@example.com"));

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(expectedData, formData);
    }
}