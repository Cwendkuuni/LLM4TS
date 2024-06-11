package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FormElementTest {

    @Test
    public void testNewFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals("form", formElement.tag().name());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testGetElements() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element input1 = new Element("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new Element("input", new Attributes().put("type", "text").put("name", "input2"));
        formElement.addElement(input1);
        formElement.addElement(input2);

        assertEquals(2, formElement.elements().size());
        assertTrue(formElement.elements().contains(input1));
        assertTrue(formElement.elements().contains(input2));
    }

    @Test
    public void testRemoveChild() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Element input1 = new Element("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new Element("input", new Attributes().put("type", "text").put("name", "input2"));
        formElement.addElement(input1);
        formElement.addElement(input2);

        formElement.removeChild(input1);

        assertEquals(1, formElement.elements().size());
        assertFalse(formElement.elements().contains(input1));
        assertTrue(formElement.elements().contains(input2));
    }

    @Test
    public void testSubmitEmptyForm() {
        Tag formTag = new Tag("form", new Attributes().put("action", "http://example.com"));
        String baseUri = "http://example.com";
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testSubmitFormWithInputElements() {
        String baseUri = "http://example.com";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", formAction));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element input1 = new Element("input", new Attributes().put("type", "text").put("name", "input1"));
        Element input2 = new Element("input", new Attributes().put("type", "text").put("name", "input2"));
        formElement.addElement(input1);
        formElement.addElement(input2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("input1", keyVal1.key());
        assertNull(keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("input2", keyVal2.key());
        assertNull(keyVal2.value());
    }

    @Test
    public void testSubmitFormWithSelectElement() {
        String baseUri = "http://example.com";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", formAction));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element selectElement = new Element("select", new Attributes().put("name", "selectElement"));
        Element option1 = new Element("option", new Attributes().put("value", "value1").put("selected", "selected"));
        Element option2 = new Element("option", new Attributes().put("value", "value2"));
        selectElement.addElement(option1);
        selectElement.addElement(option2);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("selectElement", keyVal.key());
        assertEquals("value1", keyVal.value());
    }

    @Test
    public void testSubmitFormWithCheckboxElement() {
        String baseUri = "http://example.com";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", formAction));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element checkboxElement = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxElement"));
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        checkboxElement.attributes().put("checked", "checked");

        formData = formElement.formData();
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("checkboxElement", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testSubmitFormWithMultipleCheckboxElements() {
        String baseUri = "http://example.com";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", formAction));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element checkboxElement1 = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxElement1"));
        Element checkboxElement2 = new Element("input", new Attributes().put("type", "checkbox").put("name", "checkboxElement1"));
        formElement.addElement(checkboxElement1);
        formElement.addElement(checkboxElement2);

        checkboxElement1.attributes().put("checked", "checked");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("checkboxElement1", keyVal1.key());
        assertEquals("on", keyVal1.value());

        checkboxElement2.attributes().put("checked", "checked");

        formData = formElement.formData();
        assertEquals(1, formData.size());

        Connection.KeyVal keyVal2 = formData.get(0);
        assertEquals("checkboxElement1", keyVal2.key());
        assertEquals("on,on", keyVal2.value());
    }

    @Test
    public void testSubmitFormWithMultipleSelectElements() {
        String baseUri = "http://example.com";
        String formAction = "http://example.com/submit";

        Tag formTag = new Tag("form", new Attributes().put("action", formAction));
        FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

        Element selectElement1 = new Element("select", new Attributes().put("name", "selectElement1"));
        Element option1_1 = new Element("option", new Attributes().put("value", "value1_1").put("selected", "selected"));
        Element option1_2 = new Element("option", new Attributes().put("value", "value1_2"));
        selectElement1.addElement(option1_1);
        selectElement1.addElement(option1_2);
        formElement.addElement(selectElement1);

        Element selectElement2 = new Element("select", new Attributes().put("name", "selectElement2"));
        Element option2_1 = new Element("option", new Attributes().put("value", "value2_1").put("selected", "selected"));
        Element option2_2 = new Element("option", new Attributes().put("value", "value2_2"));
        selectElement2.addElement(option2_1);
        selectElement2.addElement(option2_2);
        formElement.addElement(selectElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("selectElement1", keyVal1.key());
        assertEquals("value1_1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("selectElement2", keyVal2.key());
        assertEquals("value2_1", keyVal2.value());
    }
}