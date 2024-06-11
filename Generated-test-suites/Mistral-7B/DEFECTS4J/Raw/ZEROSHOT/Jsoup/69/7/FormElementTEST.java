import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class FormElementTest {

    private Element formElement;
    private Element textInput;
    private Element selectElement;
    private Element checkboxElement;

    @Before
    public void setUp() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/submit");
        formElement = new FormElement(formTag, "", formAttributes);

        Tag textInputTag = new Tag("input");
        textInput = new Element("input") {};
        textInput.attributes().put("name", "textInput");
        textInput.attributes().put("type", "text");

        Elements formControls = formElement.elements();
        formControls.add(textInput);

        Tag selectElementTag = new Tag("select");
        selectElement = new Element("select") {};
        selectElement.attributes().put("name", "selectElement");

        formControls.add(selectElement);

        Tag optionTag1 = new Tag("option");
        optionTag1.text("Option 1");
        Element optionElement1 = new Element("option") {};
        optionElement1.attributes().put("selected", "selected");
        optionElement1.content(optionTag1);

        Tag optionTag2 = new Tag("option");
        optionTag2.text("Option 2");
        Element optionElement2 = new Element("option") {};
        optionElement2.content(optionTag2);

        selectElement.children().add(optionElement1);
        selectElement.children().add(optionElement2);

        Tag checkboxElementTag = new Tag("input");
        checkboxElementTag.attributes().put("name", "checkboxElement");
        checkboxElementTag.attributes().put("type", "checkbox");

        checkboxElement = new Element("input") {};
        checkboxElement.tagName(checkboxElementTag.tagName());
        checkboxElement.attributes().putAll(checkboxElementTag.attributes());

        formControls.add(checkboxElement);
    }

    @Test
    public void testFormElementCreation() {
        assertNotNull(formElement);
        assertEquals("form", formElement.tagName());
    }

    @Test
    public void testFormControls() {
        List<Element> formControls = formElement.elements();
        assertEquals(3, formControls.size());

        assertSame(textInput, formControls.get(0));
        assertSame(selectElement, formControls.get(1));
        assertSame(checkboxElement, formControls.get(2));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNullAction() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(3, formData.size());

        assertEquals("textInput", formData.get(0).key);
        assertEquals("", formData.get(0).val);

        assertEquals("selectElement", formData.get(1).key);
        assertEquals("Option 1", formData.get(1).val);

        assertEquals("checkboxElement", formData.get(2).key);
        assertNull(formData.get(2).val);
    }
}