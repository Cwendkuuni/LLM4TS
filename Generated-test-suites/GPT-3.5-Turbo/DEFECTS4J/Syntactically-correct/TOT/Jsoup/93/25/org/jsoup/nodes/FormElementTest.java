import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();

        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());

        FormElement result = formElement.addElement(element);

        assertEquals(1, result.elements().size());
        assertTrue(result.elements().contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);

        formElement.removeChild(element);

        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        formElement.attr("action", "http://www.example.com/form");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals("http://www.example.com/form", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoActionUrl() {
        formElement.submit();
    }

    @Test
    public void testSubmitGetMethod() {
        formElement.attr("action", "http://www.example.com/form");
        formElement.attr("method", "GET");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals("http://www.example.com/form", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("type", "text");
        element1.attr("name", "username");
        element1.val("John");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("type", "radio");
        element2.attr("name", "gender");
        element2.attr("value", "male");
        element2.attr("checked", "");
        formElement.addElement(element2);

        Element element3 = new Element(Tag.valueOf("input"), "", new Attributes());
        element3.attr("type", "radio");
        element3.attr("name", "gender");
        element3.attr("value", "female");
        formElement.addElement(element3);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
        assertEquals("gender", formData.get(1).key());
        assertEquals("male", formData.get(1).value());
    }

    @Test
    public void testFormDataNoName() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        element.attr("type", "text");
        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataSelect() {
        Element element = new Element(Tag.valueOf("select"), "", new Attributes());
        element.attr("name", "color");
        formElement.addElement(element);

        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "red");
        option1.attr("selected", "");
        element.appendChild(option1);

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "blue");
        element.appendChild(option2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("color", formData.get(0).key());
        assertEquals("red", formData.get(0).value());
    }

    @Test
    public void testFormDataNoSelectedOption() {
        Element element = new Element(Tag.valueOf("select"), "", new Attributes());
        element.attr("name", "color");
        formElement.addElement(element);

        Element option = new Element(Tag.valueOf("option"), "", new Attributes());
        option.attr("value", "blue");
        element.appendChild(option);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("color", formData.get(0).key());
        assertEquals("blue", formData.get(0).value());
    }

    @Test
    public void testFormDataCheckboxChecked() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("type", "checkbox");
        element1.attr("name", "fruit");
        element1.attr("value", "apple");
        element1.attr("checked", "");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("type", "checkbox");
        element2.attr("name", "fruit");
        element2.attr("value", "banana");
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("fruit", formData.get(0).key());
        assertEquals("apple", formData.get(0).value());
    }

    @Test
    public void testFormDataCheckboxNotChecked() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("type", "checkbox");
        element1.attr("name", "fruit");
        element1.attr("value", "apple");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("type", "checkbox");
        element2.attr("name", "fruit");
        element2.attr("value", "banana");
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataRadioChecked() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("type", "radio");
        element1.attr("name", "gender");
        element1.attr("value", "male");
        element1.attr("checked", "");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("type", "radio");
        element2.attr("name", "gender");
        element2.attr("value", "female");
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(1, formData.size());
        assertEquals("gender", formData.get(0).key());
        assertEquals("male", formData.get(0).value());
    }

    @Test
    public void testFormDataRadioNotChecked() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        element1.attr("type", "radio");
        element1.attr("name", "gender");
        element1.attr("value", "male");
        formElement.addElement(element1);

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        element2.attr("type", "radio");
        element2.attr("name", "gender");
        element2.attr("value", "female");
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();

        assertTrue(formData.isEmpty());
    }

}