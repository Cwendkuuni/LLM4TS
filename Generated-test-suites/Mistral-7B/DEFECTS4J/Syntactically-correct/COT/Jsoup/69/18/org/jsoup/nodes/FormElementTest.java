package org.jsoup.nodes.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        Tag tag = mock(Tag.class);
        String baseUri = "baseUri";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        verify(tag).tagName();
        assertEquals("FORM", tag.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attrs());
    }

    @Test
    public void testGetElements() {
        Tag formTag = Parser.parseHtml("<form><input></form>").body().tag("form");
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        Element input = Parser.parseHtml("<input/>").body().tag("input");
        formElement.addElement(input);

        Elements elements = formElement.elements();

        assertEquals(1, elements.size());
        assertEquals(input, elements.get(0));
    }

    @Test
    public void testAddElement() {
        Tag formTag = Parser.parseHtml("<form></form>").body().tag("form");
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());

        Element input = Parser.parseHtml("<input/>").body().tag("input");
        FormElement expectedResult = formElement.addElement(input);

        assertEquals(formElement, expectedResult);
        assertEquals(1, formElement.elements().size());
        assertEquals(input, formElement.elements().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitThrowsIllegalArgumentExceptionWhenFormActionUrlCannotBeDetermined() {
        Tag formTag = Parser.parseHtml("<form></form>").body().tag("form");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmitPreparesConnectionFromFormValues() {
        Tag formTag = Parser.parseHtml("<form action='/' method='post'><input name='username' type='text' value='test'><input name='password' type='password' value='password'></form>").body().tag("form");
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());

        Connection connection = formElement.submit();

        List<Connection.KeyVal> expectedData = new ArrayList<>();
        expectedData.add(HttpConnection.KeyVal.create("username", "test"));
        expectedData.add(HttpConnection.KeyVal.create("password", "password"));

        assertEquals(HttpConnection.Method.POST, connection.method());
        assertEquals("/", connection.url());
        assertEquals(expectedData, connection.data());
    }

    @Test
    public void testFormDataEmptyForm() {
        Tag formTag = Parser.parseHtml("<form></form>").body().tag("form");
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSelectElement() {
        Tag formTag = Parser.parseHtml("<form><select name='country'><option value='US'>United States</option><option value='CA'>Canada</option></select></form>").body().tag("form");
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        Element select = formElement.tagName("select");
        Element optionUS = select.select("option[value='US']").first();

        List<Connection.KeyVal> formData = formElement.formData();

        Elements selectedOptions = select.select("option[selected]");
        if (selectedOptions.size() > 0) {
            Element selectedOption = selectedOptions.get(0);
            assertEquals(1, formData.size());
            assertEquals(optionUS.val(), formData.get(0).val());
            assertEquals("country", formData.get(0).key());
        } else {
            assertEquals(0, formData.size());
        }
    }

    @Test
    public void testFormDataCheckboxOrRadioElement() {
        Tag formTag = Parser.parseHtml("<form><input type='checkbox' name='agreeToTerms' value='true'><input type='radio' name='gender' value='male'>").body().tag("form");
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        Element checkbox = formElement.tagName("input").attr("type", "checkbox").first();
        Element radio = formElement.tagName("input").attr("type", "radio").first();

        List<Connection.KeyVal> formData = formElement.formData();

        if (checkbox.hasAttr("checked")) {
            assertEquals(1, formData.size());
            assertEquals("agreeToTerms", formData.get(0).key());
            assertEquals("true", formData.get(0).val());
        } else {
            assertEquals(0, formData.size());
        }

        formElement.tagName("input").attr("name", "gender").attr("type", "radio").attr("value", "male").first().attr("checked", "checked");
        formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("gender", formData.get(0).key());
        assertEquals("male", formData.get(0).val());
    }
}