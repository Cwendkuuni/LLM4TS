
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() throws Exception {
        Tag tag = new Tag("form", "", new Attributes());
        formElement = new FormElement(tag, "", new Attributes());
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement newFormElement = formElement.addElement(element);
        Assert.assertEquals(1, newFormElement.elements().size());
        Assert.assertTrue(newFormElement.elements().contains(element));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        Assert.assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        // TODO: Add test case for submit method
    }

    @Test
    public void testFormData() {
        // TODO: Add test case for formData method
    }
}
