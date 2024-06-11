package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        // Given
        Tag formTag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();

        // When
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // Then
        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertNotNull(formElement.elements());
    }

    @Test
    public void testFormElementGetElements() {
        // Given
        Tag formTag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();

        Tag inputTag1 = new Tag("input");
        Tag inputTag2 = new Tag("input");
        Elements elements = new Elements();
        elements.add(inputTag1);
        elements.add(inputTag2);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.elements = elements;

        // When & Then
        assertEquals(elements.size(), formElement.elements().size());
        assertEquals(inputTag1, formElement.elements().get(0));
        assertEquals(inputTag2, formElement.elements().get(1));
    }

    @Test
    public void testFormElementAddElement() {
        // Given
        Tag formTag = new Tag("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Tag inputTag = new Tag("input");

        // When
        FormElement result = formElement.addElement(inputTag);

        // Then
        assertEquals(formElement, result);
        assertEquals(1, formElement.elements().size());
        assertEquals(inputTag, formElement.elements().get(0));
    }

    @Test
    public void testFormElementSubmit() {
        // Given
        String baseUri = "http://www.example.com";
        String formAction = "http://www.example.com/submit";
        String formData = "param1=value1&param2=value2";

        Tag formTag = new Tag("form", new Attributes().put("action", formAction).put("method", "post"));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element input1 = new Element("input", new Attributes().put("name", "param1").put("value", "value1"));
        Element input2 = new Element("input", new Attributes().put("name", "param2").put("value", "value2"));
        formElement.elements().add(input1);
        formElement.elements().add(input2);

        // When
        Connection connection = formElement.submit();

        // Then
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals(formAction, connection.url());
        List<Connection.KeyVal> formDataList = connection.data();
        assertEquals(2, formDataList.size());
        assertEquals("param1", formDataList.get(0).key());
        assertEquals("value1", formDataList.get(0).val());
        assertEquals("param2", formDataList.get(1).key());
        assertEquals("value2", formDataList.get(1).val());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithoutBaseUri() {
        // Given
        Tag formTag = new Tag("form");
        new FormElement(formTag, null, new Attributes());

        // When & Then (throws an exception)
    }

    @Test
    public void testFormElementFormData() {
        // Given
        String baseUri = "http://www.example.com";
        String formAction = "http://www.example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", formAction).put("method", "post"));

        Element input1 = new Element("input", new Attributes().put("name", "param1").put("value", "value1").put("type", "text"));
        Element input2 = new Element("select", new Attributes().put("name", "param2"));
        Element option1 = new Element("option", null);
        option1.val("valueA");
        option1.text("Option A");
        Element option2 = new Element("option", null);
        option2.val("valueB");
        option2.text("Option B");
        input2.elements().add(option1);
        input2.elements().add(option2);

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.elements().add(input1);
        formElement.elements().add(input2);

        // When
        List<Connection.KeyVal> formData = formElement.formData();

        // Then
        assertEquals(4, formData.size());
        assertEquals("param1", formData.get(0).key());
        assertEquals("value1", formData.get(0).val());
        assertEquals("param2", formData.get(1).key());
        assertEquals("valueA", formData.get(2).val());
        assertEquals("Option A", formData.get(2).key());
        assertEquals("param2", formData.get(3).key());
        assertEquals("valueB", formData.get(3).val());
        assertEquals("Option B", formData.get(3).key());
    }

    @Test
    public void testFormElementWithNoAction() {
        // Given
        String baseUri = "http://www.example.com";
        Tag formTag = new Tag("form");

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        // When & Then
        assertEquals(0, formElement.elements().size());
        assertNull(formElement.submit());
    }

    @Test
    public void testFormElementWithDisabledInput() {
        // Given
        String baseUri = "http://www.example.com";
        String formAction = "http://www.example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", formAction).put("method", "post"));

        Element input1 = new Element("input", new Attributes().put("name", "param1").put("value", "value1").put("type", "text").put("disabled", "disabled"));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.elements().add(input1);

        // Then
        assertEquals(0, formElement.elements().size());
        assertNull(formElement.submit());
    }

    @Test
    public void testFormElementWithMissingNameInput() {
        // Given
        String baseUri = "http://www.example.com";
        String formAction = "http://www.example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", formAction).put("method", "post"));

        Element input1 = new Element("input", new Attributes().put("type", "text"));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.elements().add(input1);

        // Then
        assertEquals(0, formElement.elements().size());
        assertNull(formElement.submit());
    }

    @Before
    public void setUp() {
        ParseSetup.setDefaultParser(Jsoup.parser());
    }
}