package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertNull(formElement.elements());
    }

    @Test
    public void testFormElementWithElements() {
        Tag tag = new Tag("form");
        Element inputElement = new Element("input", "", new Attributes().put("name", "inputName"));
        FormElement formElement = new FormElement(tag, "", new Attributes());
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertTrue(elements.size() > 0);
        assertSame(inputElement, elements.get(0));
    }

    @Test
    public void testFormElementSubmitWithNoAction() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertNull(formElement.submit().execute().body());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithNullBaseUri() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, null, new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormElementSubmit() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form", "action", baseUri);
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element inputElement = new Element("input", "", new Attributes().put("name", "inputName").put("value", "inputValue"));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("inputValue", keyVal.value());

        Connection connection = formElement.submit();
        assertNotNull(connection.request());
        assertNotNull(connection.response());
        assertNotNull(connection.body());
    }

    @Test
    public void testFormElementSubmitWithEmptyAction() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form", "action", "");
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element inputElement = new Element("input", "", new Attributes().put("name", "inputName").put("value", "inputValue"));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("inputValue", keyVal.value());

        Connection connection = formElement.submit();
        assertNull(connection.request());
        assertNull(connection.response());
        assertNull(connection.body());
    }

    @Test
    public void testFormElementSubmitWithActionAndMethodGet() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form", "action", "/api/submit");
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes().put("method", "GET"));

        Element inputElement = new Element("input", "", new Attributes().put("name", "inputName").put("value", "inputValue"));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("inputValue", keyVal.value());

        Connection connection = formElement.submit();
        assertNotNull(connection.request());
        assertNotNull(connection.response());
        assertNull(connection.body());
    }

    @Test
    public void testFormElementSubmitWithActionAndMethodPost() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form", "action", "/api/submit");
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes().put("method", "POST"));

        Element inputElement = new Element("input", "", new Attributes().put("name", "inputName").put("value", "inputValue"));
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("inputName", keyVal.key());
        assertEquals("inputValue", keyVal.value());

        Connection connection = formElement.submit();
        assertNotNull(connection.request());
        assertNotNull(connection.response());
        assertNotNull(connection.body());
    }

    @Test
    public void testFormElementSubmitWithSelectElement() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form", "action", "/api/submit");

        Tag selectTag = new Tag("select", "name", "selectName");
        Element optionTag1 = new Element("option", "", new Attributes().put("value", "optionValue1"));
        Element optionTag2 = new Element("option", "", new Attributes().put("value", "optionValue2").put("selected", "selected"));
        selectTag.add(optionTag1);
        selectTag.add(optionTag2);

        Element inputElement = new Element("input", "", new Attributes().put("type", "hidden").put("name", "_csrf").put("value", "csrfValue"));

        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.addElement(selectTag);
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        Connection.KeyVal selectKeyVal = formData.get(0);
        assertEquals("selectName", selectKeyVal.key());
        assertEquals("optionValue2", selectKeyVal.value());
        Connection.KeyVal inputKeyVal = formData.get(1);
        assertEquals("_csrf", inputKeyVal.key());
        assertEquals("csrfValue", inputKeyVal.value());

        Connection connection = formElement.submit();
        assertNotNull(connection.request());
        assertNotNull(connection.response());
        assertNotNull(connection.body());
    }

    @Test
    public void testFormElementSubmitWithCheckboxOrRadioElement() {
        String baseUri = "http://example.com";
        Tag formTag = new Tag("form", "action", "/api/submit");

        Element checkboxElement = new Element("input", "", new Attributes().put("name", "checkboxName").put("type", "checkbox").put("checked", "checked"));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("checkboxName", keyVal.key());
        assertTrue(keyVal.value().equals("on"));

        Connection connection = formElement.submit();
        assertNotNull(connection.request());
        assertNotNull(connection.response());
        assertNotNull(connection.body());
    }
}