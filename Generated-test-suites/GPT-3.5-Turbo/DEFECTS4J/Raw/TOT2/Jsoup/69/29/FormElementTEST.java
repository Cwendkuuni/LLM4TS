import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {
    
    private FormElement formElement;

    @Before
    public void setup() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);
        assertNotNull(result);
        assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionUrl() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("testuser");

        formElement.addElement(inputElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("name", "agree");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");

        formElement.addElement(checkboxElement);

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("selected", "");
        optionElement.val("option1");
        selectElement.appendChild(optionElement);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(3, formData.size());
    }
}