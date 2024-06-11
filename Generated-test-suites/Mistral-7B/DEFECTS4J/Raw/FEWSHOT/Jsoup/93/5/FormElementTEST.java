package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag formTag = new Tag("form");
        Attributes formAttrs = new Attributes();
        formAttrs.put("action", "http://test.com");
        formAttrs.put("method", "get");
        FormElement formElement = new FormElement(formTag, null, formAttrs);

        assertEquals("form", formElement.tagName());
        assertEquals("http://test.com", formElement.absUrl("action"));
        assertEquals("get", formElement.attr("method").toLowerCase());

        Document doc = Jsoup.parse("<form action=\"http://test.com\" method=\"get\"></form>");
        FormElement formElementFromDoc = doc.select("form").first();
        assertEquals(formElement, formElementFromDoc);
    }

    @Test
    public void testFormElementGetElements() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());
        Element textInput = new Elements(new Tag("input")).first();
        textInput.attr("type", "text");
        textInput.attr("name", "username");
        formElement.addElement(textInput);

        Element passwordInput = new Elements(new Tag("input")).first();
        passwordInput.attr("type", "password");
        passwordInput.attr("name", "password");
        formElement.addElement(passwordInput);

        Elements formControls = formElement.elements();
        assertEquals(2, formControls.size());
        assertEquals(textInput, formControls.get(0));
        assertEquals(passwordInput, formControls.get(1));
    }

    @Test
    public void testFormElementRemoveElement() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());
        Element textInput = new Elements(new Tag("input")).first();
        textInput.attr("type", "text");
        textInput.attr("name", "username");
        formElement.addElement(textInput);

        Element passwordInput = new Elements(new Tag("input")).first();
        passwordInput.attr("type", "password");
        passwordInput.attr("name", "password");
        formElement.addElement(passwordInput);

        Element removedElement = formElement.elements().get(0);
        formElement.removeChild(removedElement);

        assertEquals(1, formElement.elements().size());
        assertFalse(formElement.elements().contains(removedElement));
    }

    @Test
    public void testFormElementSubmit() throws Exception {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());

        Element textInput = new Elements(new Tag("input"))
                .attr("type", "text")
                .attr("name", "username")
                .attr("value", "testUser")
                .first();
        Element passwordInput = new Elements(new Tag("input"))
                .attr("type", "password")
                .attr("name", "password")
                .attr("value", "testPassword")
                .first();

        formElement.addElement(textInput);
        formElement.addElement(passwordInput);

        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.requestBody();

        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testUser", formData.get(0).value());
        assertEquals("password", formData.get(1).key());
        assertEquals("testPassword", formData.get(1).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormElementSubmitWithNoActionUrl() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());

        formElement.submit();
    }

    @Test
    public void testFormElementFormDataForSingleTextInput() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());

        Element textInput = new Elements(new Tag("input"))
                .attr("type", "text")
                .attr("name", "username")
                .attr("value", "testUser")
                .first();
        formElement.addElement(textInput);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testUser", formData.get(0).value());
    }

    @Test
    public void testFormElementFormDataForSingleSelectElement() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());

        Element selectElement = new Elements(new Tag("select"))
                .attr("name", "colors")
                .first();
        Element optionRed = new Elements(new Tag("option"))
                .attr("value", "red")
                .attr("selected", "selected")
                .first();
        Element optionBlue = new Elements(new Tag("option"))
                .attr("value", "blue")
                .first();
        selectElement.addElement(optionRed);
        selectElement.addElement(optionBlue);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("colors", formData.get(0).key());
        assertEquals("red", formData.get(0).value());
    }

    @Test
    public void testFormElementFormDataForMultipleElements() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());

        Element textInput1 = new Elements(new Tag("input"))
                .attr("type", "text")
                .attr("name", "username1")
                .attr("value", "testUser1")
                .first();
        Element textInput2 = new Elements(new Tag("input"))
                .attr("type", "text")
                .attr("name", "username2")
                .attr("value", "testUser2")
                .first();
        Element selectElement = new Elements(new Tag("select"))
                .attr("name", "colors")
                .first();
        Element optionRed = new Elements(new Tag("option"))
                .attr("value", "red")
                .attr("selected", "selected")
                .first();
        Element optionBlue = new Elements(new Tag("option"))
                .attr("value", "blue")
                .first();
        selectElement.addElement(optionRed);
        selectElement.addElement(optionBlue);
        formElement.addElement(textInput1);
        formElement.addElement(textInput2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(3, formData.size());
        assertEquals("username1", formData.get(0).key());
        assertEquals("testUser1", formData.get(0).value());
        assertEquals("username2", formData.get(1).key());
        assertEquals("testUser2", formData.get(1).value());
        assertEquals("colors", formData.get(2).key());
        assertEquals("red", formData.get(2).value());
    }

    @Test
    public void testFormElementFormDataForMultipleSelectElements() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());

        Element selectElement1 = new Elements(new Tag("select"))
                .attr("name", "colors1")
                .first();
        Element optionRed1 = new Elements(new Tag("option"))
                .attr("value", "red")
                .attr("selected", "selected")
                .first();
        Element optionBlue1 = new Elements(new Tag("option"))
                .attr("value", "blue")
                .first();
        selectElement1.addElement(optionRed1);
        selectElement1.addElement(optionBlue1);
        formElement.addElement(selectElement1);

        Element selectElement2 = new Elements(new Tag("select"))
                .attr("name", "colors2")
                .first();
        Element optionRed2 = new Elements(new Tag("option"))
                .attr("value", "red")
                .attr("selected", "selected")
                .first();
        Element optionBlue2 = new Elements(new Tag("option"))
                .attr("value", "blue")
                .first();
        selectElement2.addElement(optionRed2);
        selectElement2.addElement(optionBlue2);
        formElement.addElement(selectElement2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(4, formData.size());
        assertEquals("colors1", formData.get(0).key());
        assertEquals("red", formData.get(0).value());
        assertEquals("colors2", formData.get(1).key());
        assertEquals("red", formData.get(1).value());
    }

    @Test
    public void testFormElementFormDataForMultipleCheckboxes() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, null, new Attributes());

        Element checkboxElement1 = new Elements(new Tag("input"))
                .attr("type", "checkbox")
                .attr("name", "colors")
                .attr("value", "red")
                .attr("checked", "checked")
                .first();
        Element checkboxElement2 = new Elements(new Tag("input"))
                .attr("type", "checkbox")
                .attr("name", "colors")
                .attr("value", "blue")
                .first();
        formElement.addElement(checkboxElement1);
        formElement.addElement(checkboxElement2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                HttpConnection.KeyVal.create("colors", "red"),
                HttpConnection.KeyVal.create("colors", "blue"));
        assertEquals(expectedFormData, formData);
    }
}