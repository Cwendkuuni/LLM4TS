import org.jsoup.Connection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
    public void elements_ShouldReturnEmptyElementsList_WhenNoElementsAdded() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void addElement_ShouldAddElementToFormElementsList() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertFalse(elements.isEmpty());
        assertTrue(elements.contains(element));
    }

    @Test
    public void submit_ShouldThrowIllegalArgumentException_WhenActionAttributeNotSet() {
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void submit_ShouldReturnConnectionWithCorrectDataAndMethod_WhenActionAttributeSet() {
        String action = "http://example.com/action";
        formElement.attr("action", action);
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(action, connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void formData_ShouldReturnEmptyList_WhenNoFormControlsAdded() {
        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void formData_ShouldReturnCorrectDataForInputElements() {
        Element input1 = new Element(Tag.valueOf("input"), "", new Attributes());
        input1.attr("type", "text");
        input1.attr("name", "input1");
        input1.val("value1");
        formElement.addElement(input1);

        Element input2 = new Element(Tag.valueOf("input"), "", new Attributes());
        input2.attr("type", "text");
        input2.attr("name", "input2");
        input2.val("value2");
        formElement.addElement(input2);

        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(2, data.size());

        Connection.KeyVal keyVal1 = data.get(0);
        assertEquals("input1", keyVal1.key());
        assertEquals("value1", keyVal1.value());

        Connection.KeyVal keyVal2 = data.get(1);
        assertEquals("input2", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }

    @Test
    public void formData_ShouldReturnOnlyCheckedOptionsForSelectElement() {
        Element select = new Element(Tag.valueOf("select"), "", new Attributes());
        select.attr("name", "select");

        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "option1");
        select.appendChild(option1);

        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "option2");
        option2.attr("selected", "selected");
        select.appendChild(option2);

        Element option3 = new Element(Tag.valueOf("option"), "", new Attributes());
        option3.attr("value", "option3");
        option3.attr("selected", "selected");
        select.appendChild(option3);

        formElement.addElement(select);

        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(2, data.size());

        Connection.KeyVal keyVal1 = data.get(0);
        assertEquals("select", keyVal1.key());
        assertEquals("option2", keyVal1.value());

        Connection.KeyVal keyVal2 = data.get(1);
        assertEquals("select", keyVal2.key());
        assertEquals("option3", keyVal2.value());
    }

    @Test
    public void formData_ShouldOnlyReturnCheckedCheckboxAndRadioButtonElements() {
        Element checkbox1 = new Element(Tag.valueOf("input"), "", new Attributes());
        checkbox1.attr("type", "checkbox");
        checkbox1.attr("name", "checkbox1");
        checkbox1.attr("value", "checkbox1Value");
        checkbox1.attr("checked", "checked");
        formElement.addElement(checkbox1);

        Element checkbox2 = new Element(Tag.valueOf("input"), "", new Attributes());
        checkbox2.attr("type", "checkbox");
        checkbox2.attr("name", "checkbox2");
        checkbox2.attr("value", "checkbox2Value");
        formElement.addElement(checkbox2);

        Element radioButton1 = new Element(Tag.valueOf("input"), "", new Attributes());
        radioButton1.attr("type", "radio");
        radioButton1.attr("name", "radioButton");
        radioButton1.attr("value", "radioButton1Value");
        radioButton1.attr("checked", "checked");
        formElement.addElement(radioButton1);

        Element radioButton2 = new Element(Tag.valueOf("input"), "", new Attributes());
        radioButton2.attr("type", "radio");
        radioButton2.attr("name", "radioButton");
        radioButton2.attr("value", "radioButton2Value");
        formElement.addElement(radioButton2);

        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(3, data.size());

        Connection.KeyVal keyVal1 = data.get(0);
        assertEquals("checkbox1", keyVal1.key());
        assertEquals("checkbox1Value", keyVal1.value());

        Connection.KeyVal keyVal2 = data.get(1);
        assertEquals("radioButton", keyVal2.key());
        assertEquals("radioButton1Value", keyVal2.value());

        Connection.KeyVal keyVal3 = data.get(2);
        assertEquals("radioButton", keyVal3.key());
        assertEquals("radioButton2Value", keyVal3.value());
    }

    @Test
    public void equals_ShouldReturnTrue_WhenBothReferencesAreToSameObject() {
        assertTrue(formElement.equals(formElement));
    }

    @Test
    public void equals_ShouldReturnFalse_WhenObjectIsNotInstanceOfFormElement() {
        assertFalse(formElement.equals(new Object()));
    }

    @Test
    public void equals_ShouldReturnFalse_WhenOtherObjectIsNull() {
        assertFalse(formElement.equals(null));
    }

    @Test
    public void equals_ShouldReturnFalse_WhenTagDifferent() {
        FormElement otherFormElement = new FormElement(Tag.valueOf("input"), "http://example.com", new Attributes());
        assertFalse(formElement.equals(otherFormElement));
    }

    @Test
    public void equals_ShouldReturnFalse_WhenBaseUriDifferent() {
        FormElement otherFormElement = new FormElement(Tag.valueOf("form"), "http://differenturl.com", new Attributes());
        assertFalse(formElement.equals(otherFormElement));
    }

    @Test
    public void equals_ShouldReturnTrue_WhenAttributesAreEqual() {
        FormElement otherFormElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(formElement.equals(otherFormElement));
    }

    @Test
    public void equals_ShouldReturnFalse_WhenElementsAreDifferent() {
        FormElement otherFormElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        otherFormElement.addElement(element);
        assertFalse(formElement.equals(otherFormElement));
    }
}