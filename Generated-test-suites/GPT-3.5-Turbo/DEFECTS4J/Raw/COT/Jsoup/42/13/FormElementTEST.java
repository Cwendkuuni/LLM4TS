import org.junit.Test;
import org.jsoup.Connection;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement updatedFormElement = formElement.addElement(element);
        Elements elements = updatedFormElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidAction() {
        FormElement formElement = new FormElement(
                Tag.valueOf("form"), "http://example.com", new Attributes().put("action", ""));
        formElement.submit();
    }

    @Test
    public void testSubmitValidAction() {
        FormElement formElement = new FormElement(
                Tag.valueOf("form"), "http://example.com", new Attributes().put("action", "http://example.com/action"));
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals("http://example.com/action", connection.request().url().toString());
    }

    @Test
    public void testFormDataNoElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes()
                .put("name", "inputName")
                .put("value", "inputValue"));
        formElement.addElement(inputElement);
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes()
                .put("name", "selectName"));
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes()
                .put("selected", ""));
        optionElement.val("optionValue");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("inputName", formData.get(0).key());
        assertEquals("inputValue", formData.get(0).value());
        assertEquals("selectName", formData.get(1).key());
        assertEquals("optionValue", formData.get(1).value());
    }

}