package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for FormElement
 */
public class FormElementTest {

    /**
     * Test constructor
     */
    @Test
    public void testFormElementConstructor() {
        String html = "<form action='/' method='post'><input name='test'/></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        Assert.assertEquals("form", formElement.tagName());
    }

    /**
     * Test method elements
     */
    @Test
    public void testElementsMethod() {
        String html = "<form action='/' method='post'><input name='test1'/><input name='test2'/></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        List<Element> elements = formElement.elements();
        Assert.assertEquals(2, elements.size());
    }

    /**
     * Test method addElement
     */
    @Test
    public void testAddElementMethod() {
        String html1 = "<form action='/' method='post'></form>";
        Document doc1 = Jsoup.parse(html1);
        FormElement formElement1 = new FormElement(doc1.selectFirst("form"), "", new Attributes());

        String html2 = "<input name='test' type='text'/>";
        Document doc2 = Jsoup.parse(html2);
        Element element = doc2.selectFirst("input");

        formElement1.addElement(element);
        List<Element> elements = formElement1.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("input", elements.get(0).tagName());
    }

    /**
     * Test method submit
     */
    @Test
    public void testSubmitMethod() {
        String html = "<form action='/' method='post'><input name='test' value='testValue'/></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("test", formData.get(0).key());
        Assert.assertEquals("testValue", formData.get(0).value());
        // Add more assertions based on the response from the submission
    }

    /**
     * Test method formData
     */
    @Test
    public void testFormDataMethod() {
        String html = "<form action='/' method='post'><input name='test1' value='testValue1'/><input name='test2' value='testValue2'/></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("test1", formData.get(0).key());
        Assert.assertEquals("testValue1", formData.get(0).value());
        Assert.assertEquals("test2", formData.get(1).key());
        Assert.assertEquals("testValue2", formData.get(1).value());
    }

    /**
     * Test edge cases and exceptions
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithoutBaseURI() {
        String html = "<form action='/' method='post'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodEmptyAction() {
        String html = "<form action='' method='post'></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodNullForm() {
        Document doc = null;
        FormElement formElement = new FormElement(null, "", new Attributes());
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodNullFormAction() {
        String html = "<form></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = new FormElement(doc.selectFirst("form"), "", new Attributes());
        formElement.submit();
    }
}