import org.junit.Test;
import org.jsoup.Connection;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(null, null, null);
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(null, null, null);
        FormElement newFormElement = formElement.addElement(new Element(null, null, null));
        assertEquals(1, formElement.elements().size());
        assertEquals(newFormElement, formElement);
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(null, null, null);
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(null, null, null);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }

    @Test
    public void testEquals() {
        FormElement formElement = new FormElement(null, null, null);
        boolean equals = formElement.equals(null);
        assertFalse(equals);
    }
}