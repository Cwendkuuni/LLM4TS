import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.AttributeConverter;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FormElementTest {

    @Test
    public void testCreateFormElement() {
        String html = "<form action='/submit' id='form1'><input name='userName' type='text' value='John'/>" +
                "<input name='password' type='password' value='password'/><button type='submit' name='submitButton'/>Submit</button></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst(Tag.NAME.equals("form"));

        assertEquals("form", formElement.tagName());
        assertEquals("/submit", formElement.absUrl("action"));
        assertFalse(formElement.hasAttr("method"));

        List<Element> formControls = formElement.elements();
        assertEquals(3, formControls.size());

        Element inputName = formControls.get(0);
        assertEquals("input", inputName.tagName());
        assertEquals("userName", inputName.attr("name"));
        assertEquals("John", inputName.attr("value"));

        Element inputPassword = formControls.get(1);
        assertEquals("input", inputPassword.tagName());
        assertEquals("password", inputPassword.attr("type"));
        assertNull(inputPassword.attr("name"));

        Element submitButton = formControls.get(2);
        assertEquals("button", submitButton.tagName());
        assertEquals("Submit", submitButton.text());
        assertEquals("submitButton", submitButton.attr("name"));
    }

    @Test
    public void testFormElementAddElement() {
        String html = "<form action='/submit' id='form1'><input name='userName' type='text' value='John'/></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst(Tag.NAME.equals("form"));

        Element checkbox = new Parser().parseFromString("<input type='checkbox' name='agreeTerms' value='true'/>", null).body();
        formElement.addElement(checkbox);

        List<Element> formControls = formElement.elements();
        assertEquals(2, formControls.size());

        Element inputName = formControls.get(0);
        assertEquals("input", inputName.tagName());
        assertEquals("userName", inputName.attr("name"));
        assertEquals("John", inputName.attr("value"));

        Element checkboxElement = formControls.get(1);
        assertEquals("input", checkboxElement.tagName());
        assertEquals("checkbox", checkboxElement.attr("type"));
        assertEquals("agreeTerms", checkboxElement.attr("name"));
        assertTrue(checkboxElement.hasAttr("value"));
        assertTrue(checkboxElement.hasAttr("checked"));
    }

    @Test
    public void testFormElementSubmit() {
        String html = "<form action='/submit' id='form1'><input name='userName' type='text' value='John'/>" +
                "<input name='password' type='password' value='password'/><input type='submit' name='submitButton'/></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst(Tag.NAME.equals("form"));

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        formElement.submit();

        List<Connection.KeyVal> formDataAfterSubmit = formElement.formData();
        assertEquals(2, formDataAfterSubmit.size());

        Connection.KeyVal formDataUserName = null;
        Connection.KeyVal formDataPassword = null;

        for (Connection.KeyVal connectionKeyVal : formDataAfterSubmit) {
            if ("userName".equals(connectionKeyVal.key())) {
                formDataUserName = connectionKeyVal;
            }
            if ("password".equals(connectionKeyVal.key())) {
                formDataPassword = connectionKeyVal;
            }
        }

        assertEquals("John", formDataUserName.value());
        assertEquals("password", formDataPassword.value());
    }

    @Test
    public void testFormElementSubmitNoActionUrl() {
        String html = "<form><input name='userName' type='text' value='John'/>" +
                "<input name='password' type='password' value='password'/><input type='submit' name='submitButton'/></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst(Tag.NAME.equals("form"));

        try {
            formElement.submit();
            fail("Exception expected: Could not determine a form action URL for submit.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormElementSubmitWithInvalidFormActionUrl() {
        String html = "<form action='javascript:void(0)'><input name='userName' type='text' value='John'/>" +
                "<input name='password' type='password' value='password'/><input type='submit' name='submitButton'/></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst(Tag.NAME.equals("form"));

        try {
            formElement.submit();
            fail("Exception expected: java.net.MalformedURLException: no protocol: javascript:void(0)");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormElementFormDataWithInvalidName() {
        String html = "<form action='/submit' id='form1'><input name='userName' type='text' value='John'/>" +
                "<input name='password' type='password' value='password'/><input type='submit' name='submitButton'/></form>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst(Tag.NAME.equals("form"));

        List<Connection.KeyVal> formData = formElement.formData();

        try {
            formData.get(0).key = "invalidName";
            fail("Exception expected: org.jsoup.nodes.AttributeConverter$InvalidAttributeNameException: Invalid attribute name 'invalidName'");
        } catch (RuntimeException e) {
            // expected
        }
    }
}