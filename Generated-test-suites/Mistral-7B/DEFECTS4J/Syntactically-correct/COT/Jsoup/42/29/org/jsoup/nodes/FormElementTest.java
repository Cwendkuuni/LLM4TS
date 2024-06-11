import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.FormElement;
import org.jsoup.helper.HttpConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for FormElement class
 */
public class FormElementTest {

    /**
     * Test constructor FormElement
     */
    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertNotNull(formElement);
    }

    /**
     * Test method elements
     */
    @Test
    public void testElements() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = new Element("input");
        formElement.addElement(element);

        Elements expectedElements = new Elements();
        expectedElements.add(element);

        Elements actualElements = formElement.elements();

        Assert.assertEquals(expectedElements, actualElements);
    }

    /**
     * Test method addElement
     */
    @Test
    public void testAddElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element element = new Element("input");
        formElement.addElement(element);

        Assert.assertEquals(formElement, formElement.addElement(element));
    }

    /**
     * Test method submit
     */
    @Test
    public void testSubmit() throws Exception {
        Tag tag = new Tag("form");
        tag.put("action", "http://example.com");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element inputElement = new Element("input");
        inputElement.attr("name", "test");
        inputElement.attr("value", "testValue");
        formElement.addElement(inputElement);

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertEquals(Connection.Method.GET, connection.method());
        Assert.assertEquals("testValue", formData.get(0).getValue());
    }

    /**
     * Test method formData
     */
    @Test
    public void testFormData() {
        Tag tag = new Tag("form");
        tag.put("action", "http://example.com");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element inputElement1 = new Element("input");
        inputElement1.attr("name", "test1");
        inputElement1.attr("value", "testValue1");
        formElement.addElement(inputElement1);

        Element inputElement2 = new Element("input");
        inputElement2.attr("name", "test2");
        inputElement2.attr("value", "testValue2");
        formElement.addElement(inputElement2);

        Element selectElement = new Element("select");
        Element optionElement1 = new Element("option");
        optionElement1.attr("value", "testOption1");
        optionElement1.text("Test Option 1");
        selectElement.add(optionElement1);

        Element optionElement2 = new Element("option");
        optionElement2.attr("value", "testOption2");
        optionElement2.text("Test Option 2");
        selectElement.add(optionElement2);
        selectElement.attr("name", "testSelect");
        formElement.addElement(selectElement);

        // checkbox and radio
        Element checkboxElement = new Element("input");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("name", "testCheckbox");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);

        Element radioElement1 = new Element("input");
        radioElement1.attr("type", "radio");
        radioElement1.attr("name", "testRadio");
        radioElement1.attr("checked", "checked");
        radioElement1.attr("value", "testValueRadio1");
        formElement.addElement(radioElement1);

        Element radioElement2 = new Element("input");
        radioElement2.attr("type", "radio");
        radioElement2.attr("name", "testRadio");
        radioElement2.attr("value", "testValueRadio2");
        formElement.addElement(radioElement2);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("test1", "testValue1"));
        expectedFormData.add(HttpConnection.KeyVal.create("test2", "testValue2"));
        expectedFormData.add(HttpConnection.KeyVal.create("testSelect", "testOption1"));
        expectedFormData.add(HttpConnection.KeyVal.create("testCheckbox", ""));
        expectedFormData.add(HttpConnection.KeyVal.create("testRadio", "testValueRadio1"));

        List<Connection.KeyVal> actualFormData = formElement.formData();

        Assert.assertEquals(expectedFormData, actualFormData);
    }

    /**
     * Test method equals
     */
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("form");
        String baseUri1 = "http://example.com";
        Attributes attributes1 = new Attributes();

        FormElement formElement1 = new FormElement(tag1, baseUri1, attributes1);

        Tag tag2 = new Tag("form");
        String baseUri2 = "http://example.com";
        Attributes attributes2 = new Attributes();

        FormElement formElement2 = new FormElement(tag2, baseUri2, attributes2);

        Assert.assertFalse(formElement1.equals(formElement2));

        Tag tag3 = new Tag("form");
        String baseUri3 = "http://example.com";
        Attributes attributes3 = new Attributes();

        FormElement formElement3 = new FormElement(tag3, baseUri3, attributes3);

        formElement1.setAttribute("test", "testValue");
        formElement3.setAttribute("test", "testValue");

        Assert.assertTrue(formElement1.equals(formElement3));
    }
}