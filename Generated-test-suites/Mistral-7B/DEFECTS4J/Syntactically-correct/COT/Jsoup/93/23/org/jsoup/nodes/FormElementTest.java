package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        String html = "<form><input type=\"text\" name=\"username\"></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        assertNotNull(formElement);
        assertNotNull(formElement.elements());
    }

    @Test
    public void testElements() {
        String html = "<form><input type=\"text\" name=\"username\"><input type=\"password\" name=\"password\"></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        Element usernameInput = elements.get(0);
        assertEquals("input", usernameInput.tagName());
        String usernameName = usernameInput.attr("name");
        assertEquals("username", usernameName);
        Element passwordInput = elements.get(1);
        assertEquals("input", passwordInput.tagName());
        String passwordName = passwordInput.attr("name");
        assertEquals("password", passwordName);
    }

    @Test
    public void testAddElement() {
        String html = "<form><input type=\"text\" name=\"username\"></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        Element input = new Element("input", new Attributes().put("type", "text").put("name", "password"));
        formElement.addElement(input);
        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        Element passwordInput = elements.get(1);
        assertEquals("input", passwordInput.tagName());
        String passwordName = passwordInput.attr("name");
        assertEquals("password", passwordName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        String html = "<form></form>";
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmit() {
        String html = "<form action=\"http://localhost:8080/login\" method=\"post\"><input type=\"text\" name=\"username\"><input type=\"password\" name=\"password\"></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://localhost:8080/login", connection.url());
        assertEquals(Connection.Method.POST, connection.method());
    }

    @Test
    public void testFormDataEmptyForm() {
        String html = "<form></form>";
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSingleInput() {
        String html = "<form><input type=\"text\" name=\"username\"></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("", keyVal.value());
    }

    @Test
    public void testFormDataMultipleInputs() {
        String html = "<form><input type=\"text\" name=\"username\"><input type=\"password\" name=\"password\"></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        Connection.KeyVal usernameKeyVal = null;
        Connection.KeyVal passwordKeyVal = null;
        for (Connection.KeyVal keyVal : formData) {
            if (keyVal.key().equals("username")) {
                usernameKeyVal = keyVal;
            } else if (keyVal.key().equals("password")) {
                passwordKeyVal = keyVal;
            }
        }
        assertEquals("username", usernameKeyVal.key());
        assertEquals("", usernameKeyVal.value());
        assertEquals("password", passwordKeyVal.key());
        assertEquals("", passwordKeyVal.value());
    }

    @Test
    public void testFormDataSelectWithSingleOption() {
        String html = "<form><select name=\"country\">" +
                "<option value=\"USA\">United States</option>" +
                "</select></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("country", keyVal.key());
        assertEquals("USA", keyVal.value());
    }

    @Test
    public void testFormDataSelectWithMultipleOptions() {
        String html = "<form><select name=\"country\">" +
                "<option value=\"USA\">United States</option>" +
                "<option value=\"Canada\">Canada</option>" +
                "</select></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = null;
        for (Connection.KeyVal connectionKeyVal : formData) {
            if (connectionKeyVal.key().equals("country")) {
                keyVal = connectionKeyVal;
            }
        }
        assertEquals("country", keyVal.key());
        assertTrue(keyVal.value().startsWith("Canada") || keyVal.value().startsWith("USA"));
    }

    @Test
    public void testFormDataCheckboxUnchecked() {
        String html = "<form><input type=\"checkbox\" name=\"agree\"></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataCheckboxChecked() {
        String html = "<form><input type=\"checkbox\" name=\"agree\" checked></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("agree", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataRadioUnchecked() {
        String html = "<form><input type=\"radio\" name=\"gender\" value=\"male\"></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataRadioChecked() {
        String html = "<form><input type=\"radio\" name=\"gender\" value=\"male\" checked></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("gender", keyVal.key());
        assertEquals("male", keyVal.value());
    }

    @Test
    public void testFormDataMixedInputTypes() {
        String html = "<form><input type=\"text\" name=\"username\"><input type=\"password\" name=\"password\"><select name=\"country\">" +
                "<option value=\"USA\">United States</option>" +
                "<option value=\"Canada\">Canada</option>" +
                "</select></form>";
        Document doc = Jsoup.parse(html, new ParseSetup().parser(FormElementTestParser.INSTANCE));
        FormElement formElement = (FormElement) doc.body();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
        Connection.KeyVal usernameKeyVal = null;
        Connection.KeyVal passwordKeyVal = null;
        Connection.KeyVal countryKeyVal = null;
        for (Connection.KeyVal keyVal : formData) {
            if (keyVal.key().equals("username")) {
                usernameKeyVal = keyVal;
            } else if (keyVal.key().equals("password")) {
                passwordKeyVal = keyVal;
            } else if (keyVal.key().equals("country")) {
                countryKeyVal = keyVal;
            }
        }
        assertEquals("username", usernameKeyVal.key());
        assertEquals("", usernameKeyVal.value());
        assertEquals("password", passwordKeyVal.key());
        assertEquals("", passwordKeyVal.value());
        assertEquals("country", countryKeyVal.key());
        assertTrue(countryKeyVal.value().startsWith("Canada") || countryKeyVal.value().startsWith("USA"));
    }
}

class FormElementTestParser implements Parser.Parser {
    static final Parser.Parser INSTANCE = new FormElementTestParser();

    @Override
    public Element parse(String s, AttributeSet attributeSet, ParseContext parseContext) {
        return new FormElement(Mockito.mock(Tag.class), "", attributeSet);
    }
}