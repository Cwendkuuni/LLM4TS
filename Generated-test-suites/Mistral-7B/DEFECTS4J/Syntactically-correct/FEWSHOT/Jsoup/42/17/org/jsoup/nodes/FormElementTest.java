package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "baseUri", attributes);
        assertNotNull(formElement);
    }

    @Test
    public void testGetElements() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input", Arrays.asList("name", "input1"));
        Tag inputTag2 = new Tag("input", Arrays.asList("name", "input2"));
        Tag selectTag = new Tag("select", Arrays.asList("name", "select"));
        Tag optionTag1 = new Tag("option", Arrays.asList("value", "option1"));
        Tag optionTag2 = new Tag("option", Arrays.asList("value", "option2", "selected", "selected"));
        formTag.children().add(inputTag1);
        formTag.children().add(inputTag2);
        formTag.children().add(selectTag);
        selectTag.children().add(optionTag1);
        selectTag.children().add(optionTag2);

        Element formElement = new Element("form", Arrays.asList("tagName", "form"), new Attributes()) {
            @Override
            protected Tag tag() {
                return formTag;
            }
        };

        List<Element> elements = formElement.elements();
        assertEquals(3, elements.size());
        assertTrue(elements.contains(inputTag1));
        assertTrue(elements.contains(inputTag2));
        assertTrue(elements.contains(selectTag));
    }

    @Test
    public void testAddElement() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input", Arrays.asList("name", "input"));

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        Element elementToAdd = new Element("input", Arrays.asList("name", "addedInput"), new Attributes());
        FormElement expectedFormElement = new FormElement(formTag, "baseUri", new Attributes()) {
            @Override
            protected Elements elements() {
                return new Elements().add(new Element("input", Arrays.asList("name", "input"), new Attributes())).add(elementToAdd);
            }
        };

        FormElement resultFormElement = formElement.addElement(elementToAdd);
        assertEquals(expectedFormElement, resultFormElement);
    }

    @Test
    public void testSubmitWithValidAction() {
        Tag formTag = new Tag("form", Arrays.asList("action", "http://example.com/submit"));
        Tag inputTag = new Tag("input", Arrays.asList("name", "input", "type", "hidden", "value", "value1"));
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        formElement.elements().add(inputTag);

        Connection connection = formElement.submit();
        assertEquals("http://example.com/submit", connection.url());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyAction() {
        Tag formTag = new Tag("form");
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidAction() {
        Tag formTag = new Tag("form", Arrays.asList("action", ""));
        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormDataWithSelectSingleOption() {
        Tag formTag = new Tag("form");
        Tag selectTag = new Tag("select", Arrays.asList("name", "select"));
        Tag optionTag1 = new Tag("option", Arrays.asList("value", "option1", "selected"));
        Tag optionTag2 = new Tag("option", Arrays.asList("value", "option2"));
        selectTag.children().add(optionTag1);
        selectTag.children().add(optionTag2);

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes()) {
            @Override
            protected Elements elements() {
                return new Elements().add(selectTag);
            }
        };

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("option1", formData.get(0).key());
        assertEquals("option1", formData.get(0).value());
    }

    @Test
    public void testFormDataWithSelectMultipleOptions() {
        Tag formTag = new Tag("form");
        Tag selectTag = new Tag("select", Arrays.asList("name", "select"));
        Tag optionTag1 = new Tag("option", Arrays.asList("value", "option1", "selected"));
        Tag optionTag2 = new Tag("option", Arrays.asList("value", "option2", "selected"));
        selectTag.children().add(optionTag1);
        selectTag.children().add(optionTag2);

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes()) {
            @Override
            protected Elements elements() {
                return new Elements().add(selectTag);
            }
        };

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("option1", formData.get(0).key());
        assertEquals("option1", formData.get(0).value());
        assertEquals("option2", formData.get(1).key());
        assertEquals("option2", formData.get(1).value());
    }

    @Test
    public void testFormDataWithCheckboxOrRadio() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input", Arrays.asList("name", "input", "type", "checkbox", "checked", "checked"));

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes()) {
            @Override
            protected Elements elements() {
                return new Elements().add(inputTag);
            }
        };

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("input", formData.get(0).key());
        assertEquals("checked", formData.get(0).value());
    }

    @Test
    public void testFormDataWithMultipleCheckboxesOrRadios() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input", Arrays.asList("name", "input", "type", "checkbox", "checked", "checked"));
        Tag inputTag2 = new Tag("input", Arrays.asList("name", "input", "type", "checkbox", "checked", ""));

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes()) {
            @Override
            protected Elements elements() {
                return new Elements().add(inputTag1).add(inputTag2);
            }
        };

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("input", formData.get(0).key());
        assertEquals("checked", formData.get(0).value());
        assertEquals("input", formData.get(1).key());
        assertNull(formData.get(1).value());
    }

    @Test
    public void testFormDataWithEmptyForm() {
        Tag formTag = new Tag("form");

        FormElement formElement = new FormElement(formTag, "baseUri", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertEmpty(formData);
    }
}