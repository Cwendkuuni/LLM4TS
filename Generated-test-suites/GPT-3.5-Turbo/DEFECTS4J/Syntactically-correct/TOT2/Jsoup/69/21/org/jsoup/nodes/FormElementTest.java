import org.junit.Test;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;

import static org.junit.Assert.*;
import org.jsoup.select.Elements;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement updatedForm = form.addElement(element);
        assertTrue(updatedForm.elements().contains(element));
    }

    @Test
    public void testSubmit() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertThrows(IllegalArgumentException.class, () -> {
            form.submit();
        });
    }

    @Test
    public void testFormData() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(form.formData().isEmpty());
    }

    @Test
    public void testFormDataWithCheckbox() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes().put("type", "checkbox").put("name", "checkbox1"));
        checkbox.attr("checked", "checked");
        FormElement updatedForm = form.addElement(checkbox);
        assertEquals("on", updatedForm.formData().get(0).value());
    }

    @Test
    public void testFormDataWithSelect() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes().put("name", "select1"));
        Element option = new Element(Tag.valueOf("option"), "http://example.com", new Attributes().put("selected", ""));
        option.append("Option 1");
        select.appendChild(option);
        FormElement updatedForm = form.addElement(select);
        assertEquals("Option 1", updatedForm.formData().get(0).value());
    }

}