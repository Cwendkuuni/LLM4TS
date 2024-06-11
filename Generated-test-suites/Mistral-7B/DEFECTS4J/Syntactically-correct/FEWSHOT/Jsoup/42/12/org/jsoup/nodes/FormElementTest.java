package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.SelectElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<html><body><form action='/submit' method='POST'><input name='name' type='text' value='value' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");
        Assert.assertNotNull(formElement);
    }

    @Test
    public void testFormElementElements() {
        String html = "<html><body><form action='/submit' method='POST'><input name='name1' type='text' value='value1' />" +
                "<input name='name2' type='text' value='value2' />" +
                "<select name='name3'><option value='option1' selected></option><option value='option2'></option></select>" +
                "<input type='checkbox' name='name4' checked /><input type='checkbox' name='name4' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");
        Elements elements = formElement.elements();
        List<String> expectedNames = Arrays.asList("name1", "name2", "name3", "name4", "name4");
        List<String> actualNames = new ArrayList<>();
        for (Element element : elements) {
            actualNames.add(element.attr("name"));
        }
        Assert.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testFormElementSubmit() {
        String html = "<html><body><form action='/submit' method='POST'><input name='name' type='text' value='value' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");
        Connection connection = formElement.submit();
        String expectedUrl = "http://submit";
        String expectedData = "name=value";
        Assert.assertEquals(expectedUrl, connection.url());
        List<Connection.KeyVal> formData = connection.requestBody();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals(expectedData, formData.get(0).toString());
    }

    @Test
    public void testFormElementSubmitWithMultipleInputs() {
        String html = "<html><body><form action='/submit' method='POST'>" +
                "<input name='name1' type='text' value='value1' /><input name='name2' type='text' value='value2' />" +
                "<select name='name3'><option value='option1' selected></option><option value='option2'></option></select>" +
                "<input type='checkbox' name='name4' checked /><input type='checkbox' name='name4' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");
        Connection connection = formElement.submit();
        String expectedUrl = "http://submit";
        List<Connection.KeyVal> expectedData = Arrays.asList(
                HttpConnection.KeyVal.create("name1", "value1"),
                HttpConnection.KeyVal.create("name2", "value2"),
                HttpConnection.KeyVal.create("name3", "option1")
        );
        List<Connection.KeyVal> actualData = connection.requestBody();
        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testFormElementSubmitWithNoAction() {
        String html = "<html><body><form method='POST'><input name='name' type='text' value='value' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");
        try {
            formElement.submit();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormElementSubmitWithInvalidMethod() {
        String html = "<html><body><form action='/submit' method='INVALID'><input name='name' type='text' value='value' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.body().selectFirst("form");
        try {
            formElement.submit();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid method 'INVALID'. Supported methods are: GET, POST", e.getMessage());
        }
    }

    @Test
    public void testFormElementEquals() {
        String html1 = "<html><body><form action='/submit1' method='POST'><input name='name' type='text' value='value' /></form></body></html>";
        String html2 = "<html><body><form action='/submit2' method='POST'><input name='name' type='text' value='value' /></form></body></html>";
        Document doc1 = Jsoup.parse(html1);
        Document doc2 = Jsoup.parse(html2);
        FormElement formElement1 = (FormElement) doc1.body().selectFirst("form");
        FormElement formElement2 = (FormElement) doc2.body().selectFirst("form");
        Assert.assertNotEquals(formElement1, formElement2);

        FormElement formElement3 = (FormElement) doc1.body().selectFirst("form");
        Assert.assertEquals(formElement1, formElement3);
    }
}