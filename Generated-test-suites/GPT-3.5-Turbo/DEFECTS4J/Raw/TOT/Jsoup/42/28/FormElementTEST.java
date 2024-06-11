import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(1, result.elements().size());
        assertEquals(element, result.elements().get(0));
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());

        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), formElement.baseUri(), new Attributes());

        formElement.addElement(element1);
        formElement.addElement(element2);

        elements = formElement.elements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
    }

    @Test
    public void testSubmit() {
        formElement.attr("action", "/submit");
        Connection connection = formElement.submit();
        assertTrue(connection instanceof Connection);

        String expectedAction = "http://example.com/submit";
        assertEquals(expectedAction, connection.request().url().toString());

        assertEquals(Connection.Method.GET, connection.request().method());
        assertTrue(connection.request().data().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_noActionAttribute() {
        formElement.submit();
    }

    @Test
    public void testSubmit_withMethodAttribute() {
        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        inputElement.attr("name", "username");
        inputElement.attr("type", "text");
        inputElement.val("John");

        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("John", keyVal.value());
    }

    @Test
    public void testFormData_checkboxChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkboxElement.attr("name", "acceptTerms");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");

        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("acceptTerms", keyVal.key());
        assertEquals("", keyVal.value());
    }

    @Test
    public void testFormData_checkboxNotChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        checkboxElement.attr("name", "acceptTerms");
        checkboxElement.attr("type", "checkbox");

        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormData_radioChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radioElement.attr("name", "gender");
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "");

        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("gender", keyVal.key());
        assertEquals("", keyVal.value());
    }

    @Test
    public void testFormData_radioNotChecked() {
        Element radioElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        radioElement.attr("name", "gender");
        radioElement.attr("type", "radio");

        formElement.addElement(radioElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(formElement.equals(formElement));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(formElement.equals(new Element(Tag.valueOf("div"), formElement.baseUri(), new Attributes())));
    }

    @Test
    public void testEquals_equalFormElements() {
        FormElement otherFormElement = new FormElement(
                Tag.valueOf("form"), formElement.baseUri(), new Attributes());
        assertTrue(formElement.equals(otherFormElement));
    }

    @Test
    public void testEquals_notEqualFormElements() {
        FormElement otherFormElement = new FormElement(
                Tag.valueOf("form"), "http://example2.com", new Attributes());
        assertFalse(formElement.equals(otherFormElement));
    }

    @Test
    public void testEquals_differentFormElements() {
        FormElement otherFormElement = new FormElement(
                Tag.valueOf("div"), formElement.baseUri(), new Attributes());
        assertFalse(formElement.equals(otherFormElement));
    }

}