package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.Jsoup.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        String html = "<form><input type=\"text\" name=\"username\"></form>";
        Document doc = parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        assertEquals("form", formElement.tagName());
        assertNotNull(formElement.elements());
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testFormElementSubmission() {
        String html = "<form action=\"/login\" method=\"post\"><input type=\"text\" name=\"username\"><input type=\"password\" name=\"password\"></form>";
        Document doc = parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        formElement.addElement(doc.selectFirst("input[name=\"username\"]"));
        formElement.addElement(doc.selectFirst("input[name=\"password\"]"));
        formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection con = formElement.submit();
        assertEquals("/login", con.url());
        assertEquals(Connection.Method.POST, con.method());

        for (Connection.KeyVal keyVal : formData) {
            System.out.println("Key: " + keyVal.key() + ", Value: " + keyVal.value());
        }
    }

    @Test
    public void testFormElementWithSelect() {
        String html = "<form><select name=\"country\"><option value=\"US\">United States</option><option value=\"CA\">Canada</option></select></form>";
        Document doc = parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        formElement.addElement(doc.selectFirst("select[name=\"country\"]"));
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("country", formData.get(0).key());
        assertEquals("US", formData.get(0).value());

        Elements options = formElement.select("select[name=\"country\"] option[selected]");
        if (options.size() > 0) {
            formElement.addElement(options.first());
            formData = formElement.formData();
            assertEquals(2, formData.size());
            assertEquals("country", formData.get(0).key());
            assertEquals("US", formData.get(0).value());
            assertEquals("country", formData.get(1).key());
            assertEquals("CA", formData.get(1).value());
        }
    }

    @Test
    public void testFormElementWithCheckbox() {
        String html = "<form><input type=\"checkbox\" name=\"agree\" value=\"yes\"></form>";
        Document doc = parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        Element checkbox = doc.selectFirst("input[name=\"agree\"]");
        formElement.addElement(checkbox);
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("agree", formData.get(0).key());
        assertEquals("yes", formData.get(0).value());

        checkbox.attr("checked", "checked");
        formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("agree", formData.get(0).key());
        assertEquals("yes", formData.get(0).value());
        assertEquals("agree", formData.get(1).key());
        assertEquals("yes", formData.get(1).value());
    }

    @Test
    public void testFormElementWithRadioButton() {
        String html = "<form><input type=\"radio\" name=\"gender\" value=\"male\">Male<input type=\"radio\" name=\"gender\" value=\"female\">Female</form>";
        Document doc = parse(html);
        FormElement formElement = (FormElement) doc.selectFirst("form");
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        Element radioButton = doc.selectFirst("input[name=\"gender\"]:checked");
        if (radioButton != null) {
            formElement.addElement(radioButton);
            formData = formElement.formData();
            assertEquals(1, formData.size());
            assertEquals("gender", formData.get(0).key());
            assertEquals("male", formData.get(0).value());

            Element otherRadioButton = doc.selectFirst("input[name=\"gender\"]:not([checked])");
            formElement.addElement(otherRadioButton);
            formData = formElement.formData();
            assertEquals(2, formData.size());
            assertEquals("gender", formData.get(0).key());
            assertEquals("male", formData.get(0).value());
            assertEquals("gender", formData.get(1).key());
            assertEquals("female", formData.get(1).value());
        }
    }
}