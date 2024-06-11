import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitException() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element input1 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input1").put("value", "value1"));
        Element input2 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input2").put("value", "value2"));
        formElement.addElement(input1);
        formElement.addElement(input2);

        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(2, data.size());
        assertEquals("input1", data.get(0).key());
        assertEquals("value1", data.get(0).value());
        assertEquals("input2", data.get(1).key());
        assertEquals("value2", data.get(1).value());
    }

}