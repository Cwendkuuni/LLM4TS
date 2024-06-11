import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        // Initialize the FormElement object with necessary parameters
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements_emptyForm_returnsEmptyElements() {
        // Arrange

        // Act
        Elements elements = formElement.elements();

        // Assert
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement_validElement_addsElementToForm() {
        // Arrange
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());

        // Act
        FormElement result = formElement.addElement(element);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.elements().size());
        assertTrue(result.elements().contains(element));
    }

    @Test
    public void testSubmit_validForm_returnsConnectionObject() {
        // Arrange

        // Act
        Connection result = formElement.submit();

        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_noActionUrl_throwsIllegalArgumentException() {
        // Arrange
        formElement.removeAttr("action");

        // Act
        formElement.submit();
    }

    @Test
    public void testFormData_emptyForm_returnsEmptyList() {
        // Arrange

        // Act
        List<Connection.KeyVal> result = formElement.formData();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFormData_selectTagWithSelectedOption_returnsSelectedOptionValue() {
        // Arrange
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("selected", "selected");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        // Act
        List<Connection.KeyVal> result = formElement.formData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(optionElement.val(), result.get(0).value());
    }

    @Test
    public void testFormData_selectTagWithoutSelectedOption_returnsFirstOptionValue() {
        // Arrange
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        // Act
        List<Connection.KeyVal> result = formElement.formData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(optionElement.val(), result.get(0).value());
    }

    @Test
    public void testFormData_checkboxWithCheckedAttribute_returnsCheckboxValue() {
        // Arrange
        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);

        // Act
        List<Connection.KeyVal> result = formElement.formData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(checkboxElement.val(), result.get(0).value());
    }

    @Test
    public void testFormData_radioWithCheckedAttribute_returnsRadioValue() {
        // Arrange
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("type", "radio");
        radioElement.attr("checked", "checked");
        formElement.addElement(radioElement);

        // Act
        List<Connection.KeyVal> result = formElement.formData();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(radioElement.val(), result.get(0).value());
    }

    @Test
    public void testFormData_radioWithoutCheckedAttribute_returnsEmptyList() {
        // Arrange
        Element radioElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radioElement.attr("type", "radio");
        formElement.addElement(radioElement);

        // Act
        List<Connection.KeyVal> result = formElement.formData();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}