package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FormElementTest {

    private FormElement form;
    private Connection connection;

    @Before
    public void setup() {
        String html = "<html><body>" +
                "<form action='/submit' method='post'>" +
                "<input type='text' name='username' value='John'>" +
                "<input type='hidden' name='password' value='password'>" +
                "<select name='country'>" +
                "<option value='USA'>United States</option>" +
                "<option value='CA'>Canada</option>" +
                "</select>" +
                "<input type='checkbox' name='agree' value='yes' checked>" +
                "<input type='submit' value='Submit'>" +
                "</form>" +
                "</body></html>";
        Document doc = Jsoup.parse(html);
        form = (FormElement) doc.body().selectFirst("form");
        connection = form.submit();
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertNotNull(elements);
        assertEquals(5, elements.size());

        Element inputUsername = elements.selectFirst("input[name='username']");
        Element selectCountry = elements.selectFirst("select[name='country']");
        Element checkboxAgree = elements.selectFirst("input[name='agree']");
        Element inputPassword = elements.selectFirst("input[name='password']");
        Element inputSubmit = elements.selectFirst("input[type='submit']");

        assertEquals("input", inputUsername.tag().name());
        assertEquals("text", inputUsername.attr("type"));
        assertEquals("John", inputUsername.val());

        assertEquals("select", selectCountry.tag().name());
        assertEquals(2, selectCountry.select("option").size());
        assertEquals("United States", selectCountry.select("option[value='USA']").text());

        assertEquals("input", checkboxAgree.tag().name());
        assertEquals("checkbox", checkboxAgree.attr("type"));
        assertTrue(checkboxAgree.hasAttr("checked"));

        assertNull(inputPassword.val());

        assertEquals("input", inputSubmit.tag().name());
        assertEquals("submit", inputSubmit.attr("type"));
    }

    @Test
    public void testSubmitWithEmptyForm() {
        FormElement emptyForm = new FormElement(Tag.FORM, "", new Attributes());
        assertThrows(IllegalArgumentException.class, () -> emptyForm.submit());
    }

    @Test
    public void testSubmitWithMissingActionAttribute() {
        FormElement missingActionForm = new FormElement(Tag.FORM, "", new Attributes().put("method", "post"));
        assertThrows(IllegalArgumentException.class, () -> missingActionForm.submit());
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = form.formData();
        List<Connection.KeyVal> expectedData = new ArrayList<>(Arrays.asList(
                new Connection.KeyVal("username", "John"),
                new Connection.KeyVal("country", "USA"),
                new Connection.KeyVal("agree", "yes")
        ));

        assertEquals(3, formData.size());
        for (int i = 0; i < formData.size(); i++) {
            assertEquals(expectedData.get(i).key, formData.get(i).key);
            assertEquals(expectedData.get(i).value, formData.get(i).value);
        }
    }
}