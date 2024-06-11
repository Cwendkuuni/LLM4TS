import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        assertEquals(1, updatedFormElement.elements().size());
        assertEquals(element, updatedFormElement.elements().get(0));
        assertSame(updatedFormElement, formElement);  // Chaining test
    }

    @Test
    public void testSubmitWithValidAction() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        // Action attribute is not set, exception should be thrown
        FormElement formElementWithoutAction = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElementWithoutAction.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("test_user");
        
        Element disabledInputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        disabledInputElement.attr("name", "password");
        disabledInputElement.attr("disabled", "disabled");

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "gender");

        Element optionElement1 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement1.attr("selected", "selected");
        optionElement1.val("male");
        selectElement.appendChild(optionElement1);

        Element optionElement2 = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement2.val("female");
        selectElement.appendChild(optionElement2);

        formElement.addElement(inputElement);
        formElement.addElement(disabledInputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("username", keyVal1.key());
        assertEquals("test_user", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("gender", keyVal2.key());
        assertEquals("male", keyVal2.value());
    }
}