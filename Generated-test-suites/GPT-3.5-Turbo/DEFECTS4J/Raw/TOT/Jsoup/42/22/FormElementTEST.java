import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setUp() {
        Tag formTag = Tag.valueOf("form");
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "https://example.com/submit");
        formAttributes.put("method", "POST");
        form = new FormElement(formTag, "https://example.com", formAttributes);
    }

    @Test
    public void testAddElement() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        form.addElement(inputElement);

        Elements elements = form.elements();

        assertTrue(elements.contains(inputElement));
    }

    @Test
    public void testSubmit() {
        form.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        form.addElement(new Element(Tag.valueOf("textarea"), "", new Attributes()));
        form.addElement(new Element(Tag.valueOf("select"), "", new Attributes()));
        form.addElement(new Element(Tag.valueOf("option"), "", new Attributes()));
        form.addElement(new Element(Tag.valueOf("option"), "", new Attributes()));
        Connection connection = form.submit();

        assertNotNull(connection);
        assertTrue(connection instanceof Connection);
        assertEquals("https://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        FormElement formWithoutAction = new FormElement(Tag.valueOf("form"), "https://example.com", new Attributes());
        formWithoutAction.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement1 = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement1.attr("name", "username");
        inputElement1.attr("value", "testuser");

        Element inputElement2 = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement2.attr("name", "password");
        inputElement2.attr("type", "password");
        inputElement2.attr("value", "testpassword");

        form.addElement(inputElement1);
        form.addElement(inputElement2);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("username", keyVal1.key());
        assertEquals("testuser", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("password", keyVal2.key());
        assertEquals("testpassword", keyVal2.value());
    }

    @Test
    public void testFormDataEmptyValues() {
        Element inputElement1 = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement1.attr("name", "username");
        inputElement1.attr("value", "");

        Element inputElement2 = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement2.attr("name", "password");
        inputElement2.attr("type", "password");
        inputElement2.attr("value", "");

        form.addElement(inputElement1);
        form.addElement(inputElement2);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataSelectCheckbox() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "options");

        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("selected", "selected");
        option1.attr("value", "1");

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "2");

        selectElement.appendChild(option1);
        selectElement.appendChild(option2);

        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(1, formData.size());

        Connection.KeyVal selectedOption = formData.get(0);
        assertEquals("options", selectedOption.key());
        assertEquals("1", selectedOption.value());
    }

    @Test
    public void testFormDataSelectNoSelectedOption() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "options");

        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "1");

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "2");

        selectElement.appendChild(option1);
        selectElement.appendChild(option2);

        form.addElement(selectElement);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(1, formData.size());

        Connection.KeyVal firstOption = formData.get(0);
        assertEquals("options", firstOption.key());
        assertEquals("1", firstOption.value());
    }

    @Test
    public void testFormDataCheckboxChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        checkboxElement.attr("value", "true");

        form.addElement(checkboxElement);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(1, formData.size());

        Connection.KeyVal checkedCheckbox = formData.get(0);
        assertEquals("agree", checkedCheckbox.key());
        assertEquals("true", checkedCheckbox.value());
    }

    @Test
    public void testFormDataCheckboxNotChecked() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("value", "true");

        form.addElement(checkboxElement);

        List<Connection.KeyVal> formData = form.formData();

        assertEquals(0, formData.size());
    }

}