import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;

public class FormElementTest {

    private FormElement form;

    @Before
    public void setup() {
        form = new FormElement(Tag.form, "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = form.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());

        Element element = new Element(Tag.input, "", new Attributes());
        form.addElement(element);

        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.input, "", new Attributes());
        FormElement updatedForm = form.addElement(element);

        assertEquals(1, form.elements().size());
        assertEquals(1, updatedForm.elements().size());
        assertTrue(form.elements().contains(element));
        assertTrue(updatedForm.elements().contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.input, "", new Attributes());
        form.addElement(element);

        form.removeChild(element);

        assertEquals(0, form.elements().size());
    }

    @Test
    public void testSubmit() {
        Connection connection = form.submit();
        assertNotNull(connection);

        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals("GET", connection.request().method());
    }

    @Test
    public void testSubmitWithAction() {
        form.attr("action", "/submit");
        Connection connection = form.submit();
        assertNotNull(connection);

        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals("GET", connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMissingAction() {
        form.submit();
    }

    @Test
    public void testFormData() {
        Element input = new Element(Tag.input, "", new Attributes());
        input.attr("name", "name");
        input.attr("value", "John Doe");
        form.addElement(input);

        Element select = new Element(Tag.select, "", new Attributes());
        select.attr("name", "gender");
        Element option = new Element(Tag.option, "", new Attributes());
        option.attr("value", "male");
        option.attr("selected");
        select.addElement(option);
        form.addElement(select);

        assertEquals(2, form.formData().size());

        Connection.KeyVal keyVal1 = form.formData().get(0);
        assertEquals("name", keyVal1.key());
        assertEquals("John Doe", keyVal1.value());

        Connection.KeyVal keyVal2 = form.formData().get(1);
        assertEquals("gender", keyVal2.key());
        assertEquals("male", keyVal2.value());
    }

    @Test
    public void testFormDataWithDisabledInput() {
        Element input = new Element(Tag.input, "", new Attributes());
        input.attr("name", "name");
        input.attr("disabled");
        form.addElement(input);

        assertEquals(0, form.formData().size());
    }

    @Test
    public void testFormDataWithCheckbox() {
        Element input = new Element(Tag.input, "", new Attributes());
        input.attr("type", "checkbox");
        input.attr("name", "subscribe");
        input.attr("value", "true");
        input.attr("checked");
        form.addElement(input);

        assertEquals(1, form.formData().size());

        Connection.KeyVal keyVal = form.formData().get(0);
        assertEquals("subscribe", keyVal.key());
        assertEquals("true", keyVal.value());
    }

    @Test
    public void testFormDataWithRadio() {
        Element input1 = new Element(Tag.input, "", new Attributes());
        input1.attr("type", "radio");
        input1.attr("name", "gender");
        input1.attr("value", "male");
        input1.attr("checked");
        form.addElement(input1);

        Element input2 = new Element(Tag.input, "", new Attributes());
        input2.attr("type", "radio");
        input2.attr("name", "gender");
        input2.attr("value", "female");
        form.addElement(input2);

        assertEquals(1, form.formData().size());

        Connection.KeyVal keyVal = form.formData().get(0);
        assertEquals("gender", keyVal.key());
        assertEquals("male", keyVal.value());
    }

    @Test
    public void testFormDataWithSelect() {
        Element select = new Element(Tag.select, "", new Attributes());
        select.attr("name", "gender");
        Element option = new Element(Tag.option, "", new Attributes());
        option.attr("value", "male");
        select.addElement(option);
        form.addElement(select);

        assertEquals(1, form.formData().size());

        Connection.KeyVal keyVal = form.formData().get(0);
        assertEquals("gender", keyVal.key());
        assertEquals("male", keyVal.value());
    }
}