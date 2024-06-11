
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class FormElementTest {
  
    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(form.addElement(new Element(Tag.valueOf("input"), "http://example.com", new Attributes())));
        assertNotNull(form.addElement(new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes())));
    }

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(form.elements());
        assertTrue(form.elements().isEmpty());
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(form.submit());
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(form.formData());
        assertTrue(form.formData().isEmpty());

        // Test form control elements
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.attr("type", "text");
        inputElement.attr("disabled", "");

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        selectElement.attr("name", "selectName");

        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("value", "optionValue");
        optionElement.attr("selected", "");

        selectElement.appendChild(optionElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkboxElement.attr("name", "checkboxName");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");

        form.addElement(inputElement);
        form.addElement(selectElement);
        form.addElement(checkboxElement);

        assertFalse(form.formData().isEmpty());
        assertEquals(3, form.formData().size());
    }
}

