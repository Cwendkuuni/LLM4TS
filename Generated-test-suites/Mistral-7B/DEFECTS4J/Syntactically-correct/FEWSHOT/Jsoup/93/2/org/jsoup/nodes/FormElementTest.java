package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        tagTest("form", new Attributes().put("action", "http://example.com"), new FormElement("form"));
        tagTest("form", new Attributes().put("action", "http://example.com").put("method", "POST"), new FormElement("form"));
    }

    @Test
    public void testFormElementGetElements() {
        String html = "<form>\n" +
                "    <input type=\"text\" name=\"name1\" value=\"value1\">\n" +
                "    <input type=\"text\" name=\"name2\" value=\"value2\">\n" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Elements elements = formElement.elements();

        Assert.assertEquals(2, elements.size());
        Assert.assertNotNull(elements.get(0).attr("name"));
        Assert.assertEquals("value1", elements.get(0).val());
        Assert.assertNotNull(elements.get(1).attr("name"));
        Assert.assertEquals("value2", elements.get(1).val());
    }

    @Test
    public void testFormElementAddElement() {
        String html = "<form>\n" +
                "    <input type=\"text\" name=\"name1\" value=\"value1\">\n" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();

        Element input = new Element("input");
        input.attr("type", "text");
        input.attr("name", "name2");
        input.attr("value", "value2");

        formElement.addElement(input);

        Elements elements = formElement.elements();
        Assert.assertEquals(3, elements.size());
        Assert.assertNotNull(elements.get(0).attr("name"));
        Assert.assertEquals("value1", elements.get(0).val());
        Assert.assertNotNull(elements.get(1).attr("name"));
        Assert.assertEquals("value2", elements.get(2).val());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitNullBaseUri() {
        String html = "<form action=\"http://example.com\"></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        formElement.submit();
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        String html = "<form action=\"http://example.com\">\n" +
                "    <input type=\"text\" name=\"name1\" value=\"value1\">\n" +
                "    <input type=\"text\" name=\"name2\" value=\"value2\">\n" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.data();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("name1", formData.get(0).key());
        Assert.assertEquals("value1", formData.get(0).value());
        Assert.assertEquals("name2", formData.get(1).key());
        Assert.assertEquals("value2", formData.get(1).value());
    }

    @Test
    public void testFormElementFormDataEmptyForm() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormElementFormDataSingleElement() {
        String html = "<form>\n" +
                "    <input type=\"text\" name=\"name\" value=\"value\">\n" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("name", formData.get(0).key());
        Assert.assertEquals("value", formData.get(0).value());
    }

    @Test
    public void testFormElementFormDataMultipleElements() {
        String html = "<form>\n" +
                "    <input type=\"text\" name=\"name1\" value=\"value1\">\n" +
                "    <input type=\"text\" name=\"name2\" value=\"value2\">\n" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("name1", formData.get(0).key());
        Assert.assertEquals("value1", formData.get(0).value());
        Assert.assertEquals("name2", formData.get(1).key());
        Assert.assertEquals("value2", formData.get(1).value());
    }

    @Test
    public void testFormElementFormDataSelectElement() {
        String html = "<form>\n" +
                "    <select name=\"name\">\n" +
                "        <option value=\"value1\">Value 1</option>\n" +
                "        <option value=\"value2\" selected>\n" +
                "            Value 2\n" +
                "        </option>\n" +
                "    </select>\n" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("name", formData.get(0).key());
        Assert.assertEquals("value2", formData.get(0).value());
    }

    @Test
    public void testFormElementFormDataCheckboxAndRadio() {
        String html = "<form>\n" +
                "    <input type=\"checkbox\" name=\"name\" value=\"value\">\n" +
                "    <input type=\"radio\" name=\"name\" value=\"value\" checked>\n" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("name", formData.get(0).key());
        Assert.assertEquals("value", formData.get(0).value());
    }

    @Test
    public void testFormElementFormDataDisabledInput() {
        String html = "<form>\n" +
                "    <input type=\"text\" name=\"name\" value=\"value\" disabled>\n" +
                "</form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(0, formData.size());
    }

    private void tagTest(String tagName, Attributes attributes, Class<?> expectedClass) {
        String html = "<" + tagName;
        if (attributes != null) {
            html += " " + attributes.toString();
        }
        html += ">";
        Document doc = Jsoup.parse(html);
        Element element = doc.selectFirst(tagName);
        Assert.assertEquals(expectedClass, element.getClass());
    }
}