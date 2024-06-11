package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        // Test creating a FormElement with valid tag, baseUri, and attributes
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "https://example.com/submit");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // Check if the FormElement has been created correctly
        Assert.assertEquals("form", formElement.tagName());
        Assert.assertEquals(baseUri, formElement.baseUri());
        Assert.assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testElementsMethod() {
        // Test getting the list of form control elements associated with a FormElement
        String html = "<html><head></head><body><form action=\"/submit\" method=\"post\">" +
                "<input type=\"text\" name=\"username\" />" +
                "<input type=\"password\" name=\"password\" />" +
                "</form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = (FormElement) document.select("form").first();

        // Check if the list of form control elements is correct
        List<Element> elements = formElement.elements().all();
        Assert.assertEquals(2, elements.size());
        Assert.assertTrue(elements.get(0) instanceof InputElement);
        Assert.assertTrue(elements.get(1) instanceof InputElement);
    }

    @Test
    public void testAddElementMethod() {
        // Test adding a form control element to a FormElement
        String html = "<html><head></head><body>" +
                "<form action=\"/submit\" method=\"post\">" +
                "</form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = (FormElement) document.select("form").first();

        // Create a new input element
        InputElement inputElement = new InputElement(new Tag("input"), null, new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "newInput");

        // Add the input element to the form element
        formElement.addElement(inputElement);

        // Check if the input element has been added to the form element
        List<Element> elements = formElement.elements().all();
        Assert.assertEquals(1, elements.size() + formElement.elements().size());
        Assert.assertTrue(elements.get(elements.size() - 1) == inputElement);
    }

    @Test
    public void testSubmitMethod() {
        // Test the submit method, which prepares a Connection object for submitting a form with the given form values
        String html = "<html><head></head><body>" +
                "<form action=\"/submit\" method=\"post\">" +
                "<input type=\"text\" name=\"username\" value=\"test\" />" +
                "<input type=\"password\" name=\"password\" value=\"test123\" />" +
                "</form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = (FormElement) document.select("form").first();

        Connection connection = formElement.submit();

        // Check if the Connection object is prepared correctly
        Assert.assertEquals(Connection.Method.POST, connection.method());
        List<Connection.KeyVal> formData = connection.data();
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("username", formData.get(0).getKey().trim());
        Assert.assertEquals("test", formData.get(0).getValue());
        Assert.assertEquals("password", formData.get(1).getKey().trim());
        Assert.assertEquals("test123", formData.get(1).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethod_NoActionUrl() {
        // Test the submit method with no action URL
        String html = "<html><head></head><body>" +
                "<form action=\"\" method=\"post\">" +
                "</form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = (FormElement) document.select("form").first();

        formElement.submit();
    }

    @Test
    public void testFormDataMethod() {
        // Test the formData method, which returns a copy of the data that this form will submit
        String html = "<html><head></head><body>" +
                "<form action=\"/submit\" method=\"post\">" +
                "<input type=\"text\" name=\"username\" value=\"test\" />" +
                "<input type=\"password\" name=\"password\" value=\"test123\" />" +
                "<select name=\"selectElement\">" +
                "<option value=\"option1\" selected></option>" +
                "</select>" +
                "</form></body></html>";
        Document document = Jsoup.parse(html);
        FormElement formElement = (FormElement) document.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        // Check if the form data is correct
        Assert.assertEquals(3, formData.size());
        Assert.assertEquals("username", formData.get(0).getKey().trim());
        Assert.assertEquals("test", formData.get(0).getValue());
        Assert.assertEquals("password", formData.get(1).getKey().trim());
        Assert.assertEquals("test123", formData.get(1).getValue());
        Assert.assertEquals("selectElement", formData.get(2).getKey().trim());
        Assert.assertEquals("option1", formData.get(2).getValue());
    }
}