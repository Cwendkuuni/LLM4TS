import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements_ReturnsEmptyElements_WhenNoElementsAdded() {
        Elements elements = formElement.elements();
        Assert.assertTrue(elements.isEmpty());
    }

    @Test
    public void testElements_ReturnsCorrectElements_WhenElementsAdded() {
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("select"), "", new Attributes());
        formElement.addElement(element1);
        formElement.addElement(element2);

        Elements elements = formElement.elements();
        Assert.assertEquals(2, elements.size());
        Assert.assertEquals(element1, elements.get(0));
        Assert.assertEquals(element2, elements.get(1));
    }

    @Test
    public void testAddElement_ReturnsFormElementWithAddedElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());

        FormElement result = formElement.addElement(element);

        Elements elements = result.elements();
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit_ReturnsConnectionWithCorrectMethodAndAction() {
        Element formElement = new Element(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("action", "https://example.com");
        formElement.attr("method", "post");
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        form.addElement(formElement);

        Connection connection = form.submit();

        Assert.assertEquals(Connection.Method.POST, connection.request().method());
        Assert.assertEquals("https://example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_ThrowsIllegalArgumentException_WhenNoActionURL() {
        formElement.submit();
    }

    @Test
    public void testFormData_ReturnsEmptyList_WhenNoFormControls() {
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_ReturnsCorrectData_WhenFormControlsPresent() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.attr("value", "inputValue");
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "selectName");
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("selected", "selected");
        optionElement.val("optionValue");
        selectElement.appendChild(optionElement);
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(2, formData.size());
        Assert.assertEquals("inputName", formData.get(0).key());
        Assert.assertEquals("inputValue", formData.get(0).value());
        Assert.assertEquals("selectName", formData.get(1).key());
        Assert.assertEquals("optionValue", formData.get(1).value());
    }

    @Test
    public void testEquals_ReturnsTrue_WhenBothObjectsAreEqual() {
        FormElement formElement2 = new FormElement(formElement.tag(), formElement.baseUri(), formElement.attributes());
        Assert.assertTrue(formElement.equals(formElement2));
    }

    @Test
    public void testEquals_ReturnsFalse_WhenObjectsAreDifferent() {
        FormElement formElement2 = new FormElement(Tag.valueOf("div"), formElement.baseUri(), formElement.attributes());
        Assert.assertFalse(formElement.equals(formElement2));
    }
}