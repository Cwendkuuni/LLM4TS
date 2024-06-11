import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ParseSetup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.jsoup.helper.Validate.notEmpty;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        String html = "<form action=\"http://example.com\">" +
                "<input type=\"text\" name=\"username\" />" +
                "<input type=\"password\" name=\"password\" />" +
                "</form>";

        Element form = new Parser(ParseSetup.defaultSetup()).parse(html).selectFirst("form");
        FormElement formElement = new FormElement(Tag.FORM, form.baseUri(), form.attributes());

        Assert.assertNotNull(formElement);
        Assert.assertEquals(form.tagName(), formElement.tagName());
        Assert.assertNotNull(formElement.elements());
        Assert.assertEquals(2, formElement.elements().size());
    }

    @Test
    public void testElementsMethod() {
        String html = "<form action=\"http://example.com\">" +
                "<input type=\"text\" name=\"username\" />" +
                "<input type=\"password\" name=\"password\" />" +
                "</form>";

        Element form = new Parser(ParseSetup.defaultSetup()).parse(html).selectFirst("form");
        FormElement formElement = new FormElement(Tag.FORM, form.baseUri(), form.attributes());

        Element element1 = new Elements(form.select("input[name=username]")).first();
        FormElement.FormControlElement formControlElement1 = formElement.addElement(element1);

        Element element2 = new Elements(form.select("input[name=password]")).first();
        FormElement.FormControlElement formControlElement2 = formElement.addElement(element2);

        Assert.assertNotNull(formControlElement1);
        Assert.assertNotNull(formControlElement2);
        Assert.assertEquals(formElement.elements().size(), 2);
    }

    @Test
    public void testSubmitMethod() {
        String html = "<form action=\"http://example.com\">" +
                "<input type=\"text\" name=\"username\" value=\"John\" />" +
                "<input type=\"password\" name=\"password\" value=\"Password123\" />" +
                "</form>";

        Element form = new Parser(ParseSetup.defaultSetup()).parse(html).selectFirst("form");
        FormElement formElement = new FormElement(Tag.FORM, form.baseUri(), form.attributes());

        Connection connection = formElement.submit();
        notEmpty(connection.url(), "Form submission failed, no URL returned");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithNoAction() {
        String html = "<form>" +
                "<input type=\"text\" name=\"username\" value=\"John\" />" +
                "<input type=\"password\" name=\"password\" value=\"Password123\" />" +
                "</form>";

        Element form = new Parser(ParseSetup.defaultSetup()).parse(html).selectFirst("form");
        FormElement formElement = new FormElement(Tag.FORM, form.baseUri(), form.attributes());

        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodWithEmptyFormData() {
        String html = "<form action=\"http://example.com\">" +
                "<input type=\"text\" name=\"username\" value=\"John\" />" +
                "<input type=\"password\" name=\"password\" value=\"Password123\" />" +
                "</form>";

        Element form = new Parser(ParseSetup.defaultSetup()).parse(html).selectFirst("form");
        FormElement formElement = new FormElement(Tag.FORM, form.baseUri(), form.attributes());

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
        formElement.submit();
    }

    @Test
    public void testFormDataMethod() {
        String html = "<form action=\"http://example.com\">" +
                "<input type=\"text\" name=\"username\" value=\"John\" />" +
                "<input type=\"password\" name=\"password\" value=\"Password123\" />" +
                "</form>";

        Element form = new Parser(ParseSetup.defaultSetup()).parse(html).selectFirst("form");
        FormElement formElement = new FormElement(Tag.FORM, form.baseUri(), form.attributes());

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        Assert.assertEquals("username", keyVal1.key());
        Assert.assertEquals("John", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        Assert.assertEquals("password", keyVal2.key());
        Assert.assertEquals("Password123", keyVal2.value());
    }
}