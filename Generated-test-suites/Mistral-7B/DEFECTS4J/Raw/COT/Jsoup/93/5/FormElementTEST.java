package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetting;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test for FormElement class
 */
public class FormElementTest {

    @org.junit.BeforeClass
    public static void init() {
        Parser.setDefaultParseTimeout(0);
        ParseSetting setting = new ParseSetting.Builder().build();
        Parser.setDefaultParser(setting);
    }

    // Test methods signatures

    /**
     * Test constructor FormElement
     */
    @org.junit.Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertNotNull(formElement);
    }

    /**
     * Test method elements
     */
    @org.junit.Test
    public void testElements() {
        String html = "<html><body><form><input type='text' name='name' /><input type='text' name='age' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
    }

    /**
     * Test method addElement
     */
    @org.junit.Test
    public void testAddElement() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Element input = new Element("input");
        input.attr("name", "name");
        input.attr("type", "text");
        formElement.addElement(input);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
    }

    /**
     * Test method removeChild
     */
    @org.junit.Test
    public void testRemoveChild() {
        String html = "<html><body><form><input type='text' name='name' /><input type='text' name='age' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Element inputName = formElement.selectFirst("input[name='name']");
        formElement.removeChild(inputName);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
    }

    /**
     * Test method submit
     */
    @org.junit.Test
    public void testSubmit() throws Exception {
        String html = "<html><body><form action='http://example.com' method='POST'><input type='text' name='name' value='John' /><input type='text' name='age' value='30' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals("http://example.com", connection.url().toString());
        assertEquals(2, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
        assertEquals("age", formData.get(1).key());
        assertEquals("30", formData.get(1).value());
    }

    /**
     * Test method formData
     */
    @org.junit.Test
    public void testFormData() {
        String html = "<html><body><form><input type='text' name='name' value='John' /><input type='text' name='age' value='30' /><input type='text' name='gender' value='Male' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
        assertEquals("name", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
        assertEquals("age", formData.get(1).key());
        assertEquals("30", formData.get(1).value());
        assertEquals("gender", formData.get(2).key());
        assertEquals("Male", formData.get(2).value());
    }

    // Test edge cases and exceptions

    /**
     * Test method elements - null form element
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testElementsNullFormElement() {
        FormElement formElement = null;
        Elements elements = formElement.elements();
    }

    /**
     * Test method submit - null form element
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testSubmitNullFormElement() {
        FormElement formElement = null;
        formElement.submit();
    }

    /**
     * Test method submit - empty form element
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSubmitEmptyFormElement() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.submit();
    }

    /**
     * Test method submit - form with missing 'action' attribute
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSubmitMissingActionAttribute() {
        String html = "<html><body><form method='POST'><input type='text' name='name' value='John' /><input type='text' name='age' value='30' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.submit();
    }

    /**
     * Test method formData - null form element
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testFormDataNullFormElement() {
        FormElement formElement = null;
        formElement.formData();
    }

    /**
     * Test method formData - empty form element
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormDataEmptyFormElement() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.formData();
    }

    /**
     * Test method formData - form control with missing 'name' attribute
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormDataMissingNameAttribute() {
        String html = "<html><body><form><input type='text' /><input type='text' name='age' value='30' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.formData();
    }

    /**
     * Test method formData - form control with empty value
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormDataEmptyValue() {
        String html = "<html><body><form><input type='text' name='name' value='' /><input type='text' name='age' value='30' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.formData();
    }

    /**
     * Test method formData - form control with null value
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testFormDataNullValue() {
        String html = "<html><body><form><input type='text' name='name' value=null /><input type='text' name='age' value='30' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.formData();
    }

    /**
     * Test method formData - select form control with no selected option
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormDataNoSelectedOption() {
        String html = "<html><body><form><select name='gender'><option value='Male'>Male</option><option value='Female'>Female</option></select></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.formData();
    }

    /**
     * Test method formData - checkbox or radio with no checked attribute
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormDataNoCheckedAttribute() {
        String html = "<html><body><form><input type='checkbox' name='acceptTerms' /><button type='submit'>Submit</button></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        formElement.formData();
    }

}