import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @After
    public void tearDown() {
        formElement = null;
    }

    @Test
    public void testElements_EmptyForm_ReturnEmptyElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement_AddValidElement_ReturnUpdatedElements() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit_ValidForm_ReturnConnectionObject() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals(formElement.baseUri(), connection.request().url());
        assertTrue(connection.request().data().isEmpty());
    }

    @Test
    public void testFormData_NonSubmittableElement_IgnoreElementInFormData() {
        Element element = new Element(Tag.valueOf("div"), "", new Attributes());
        formElement.addElement(element);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_DisabledElement_IgnoreElementInFormData() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes().put("disabled", ""));
        formElement.addElement(element);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_SelectElementWithSelectedOption_AddSelectedOptionToFormData() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes().put("selected", ""));
        Element optionElement2 = new Element(Tag.valueOf("option"), "", new Attributes());
        selectElement.appendChild(optionElement1);
        selectElement.appendChild(optionElement2);
        formElement.addElement(selectElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals(selectElement.attr("name"), keyVal.key());
        assertEquals(optionElement1.val(), keyVal.value());
    }

    @Test
    public void testFormData_SelectElementWithoutSelectedOption_AddFirstOptionToFormData() {
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes().put("selected", ""));
        Element optionElement2 = new Element(Tag.valueOf("option"), "", new Attributes());
        selectElement.appendChild(optionElement2);
        selectElement.appendChild(optionElement1);
        formElement.addElement(selectElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals(selectElement.attr("name"), keyVal.key());
        assertEquals(optionElement2.val(), keyVal.value());
    }

    @Test
    public void testFormData_CheckboxElementWithoutCheckedAttribute_IgnoreElementInFormData() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes().put("type", "checkbox"));
        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_CheckboxElementWithCheckedAttribute_AddValueToFormData() {
        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes().put("type", "checkbox").put("checked", ""));
        formElement.addElement(checkboxElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals(checkboxElement.attr("name"), keyVal.key());
        assertEquals(checkboxElement.val(), keyVal.value());
    }

    @Test
    public void testFormData_RadioElementWithoutCheckedAttribute_IgnoreElementInFormData() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes().put("type", "radio"));
        formElement.addElement(radioElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_RadioElementWithCheckedAttribute_AddValueToFormData() {
        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes().put("type", "radio").put("checked", ""));
        formElement.addElement(radioElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals(radioElement.attr("name"), keyVal.key());
        assertEquals(radioElement.val(), keyVal.value());
    }

    @Test
    public void testFormData_OtherElement_AddValueToFormData() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes().put("type", "text").put("name", "username").put("value", "John"));
        formElement.addElement(element);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals(element.attr("name"), keyVal.key());
        assertEquals(element.val(), keyVal.value());
    }
}