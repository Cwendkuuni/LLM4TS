import org.jsoup.Connection;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "");
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(inputElement, elements.get(0));
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "");
        inputElement.attr("name", "username").val("john.doe");
        formElement.addElement(inputElement);
        Element selectElement = new Element(Tag.valueOf("select"), "");
        Element optionElement = new Element(Tag.valueOf("option"), "");
        optionElement.attr("selected", "").val("admin");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        System.out.println(formElement.formData());

        assertEquals(2, formElement.formData().size());
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertEquals("http://example.com", connection.request().url().toExternalForm());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        assertEquals(formElement1, formElement2);
    }

}