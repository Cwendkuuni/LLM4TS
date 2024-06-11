package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;
    private Element formControlElement1;
    private Element formControlElement2;
    private Element formControlElement3;

    @Before
    public void setup() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";

        Attributes formAttributes = new Attributes();
        formAttributes.put("name", "formName");

        formElement = new FormElement(formTag, baseUri, formAttributes);

        Tag inputTag1 = new Tag("input");
        Attributes inputAttributes1 = new Attributes();
        inputAttributes1.put("name", "inputName1");
        inputAttributes1.put("type", "text");
        inputAttributes1.put("value", "inputValue1");

        formControlElement1 = new Element("input") {
            @Override
            public Tag tag() {
                return inputTag1;
            }

            @Override
            public Attributes attributes() {
                return inputAttributes1;
            }
        };

        Tag inputTag2 = new Tag("input");
        Attributes inputAttributes2 = new Attributes();
        inputAttributes2.put("name", "inputName2");
        inputAttributes2.put("type", "text");
        inputAttributes2.put("value", "inputValue2");

        formControlElement2 = new Element("input") {
            @Override
            public Tag tag() {
                return inputTag2;
            }

            @Override
            public Attributes attributes() {
                return inputAttributes2;
            }
        };

        Tag selectTag = new Tag("select");
        Attributes selectAttributes = new Attributes();
        selectAttributes.put("name", "selectName");

        Tag optionTag1 = new Tag("option");
        Attributes optionAttributes1 = new Attributes();
        optionAttributes1.put("value", "optionValue1");

        Element optionElement1 = new Element("option") {
            @Override
            public Tag tag() {
                return optionTag1;
            }

            @Override
            public Attributes attributes() {
                return optionAttributes1;
            }
        };

        Element optionElement2 = new Element("option") {
            @Override
            public Tag tag() {
                return new Tag("option");
            }

            @Override
            public Attributes attributes() {
                return new Attributes();
            }
        };

        formControlElement3 = new Element("select") {
            @Override
            public Tag tag() {
                return selectTag;
            }

            @Override
            public Attributes attributes() {
                return selectAttributes;
            }

            @Override
            public Elements children() {
                return new Elements(optionElement1, optionElement2);
            }
        };

        formElement.addElement(formControlElement1);
        formElement.addElement(formControlElement2);
        formElement.addElement(formControlElement3);
    }

    @Test
    public void testElements() {
        List<Element> actualElements = formElement.elements().stream().collect(Collectors.toList());
        List<Element> expectedElements = Arrays.asList(formControlElement1, formControlElement2, formControlElement3);
        assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testSubmitWithTextInput() {
        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.data();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("formName[inputName1]", "inputValue1"),
                new Connection.KeyVal("formName[inputName2]", "inputValue2")
        );
        assertEquals(expectedFormData, formData);
    }

    @Test
    public void testSubmitWithSelectInput() {
        formControlElement3.child(0).attr("selected", "selected");
        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = connection.data();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("formName[selectName]", "optionValue1")
        );
        assertEquals(expectedFormData, formData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoAction() {
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test
    public void testFormDataWithTextInput() {
        List<Connection.KeyVal> actualFormData = formElement.formData();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("inputName1", "inputValue1"),
                new Connection.KeyVal("inputName2", "inputValue2")
        );
        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataWithSelectInput() {
        formControlElement3.child(0).attr("selected", "selected");
        List<Connection.KeyVal> actualFormData = formElement.formData();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("selectName", "optionValue1")
        );
        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testFormDataWithMultipleSelectInput() {
        formControlElement3.child(1).attr("selected", "selected");
        List<Connection.KeyVal> actualFormData = formElement.formData();
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("selectName", "optionValue2")
        );
        assertEquals(expectedFormData, actualFormData);
    }

    @Test
    public void testEquals() {
        FormElement anotherFormElement = new FormElement(new Tag("form"), "", new Attributes());
        assertFalse(formElement.equals(anotherFormElement));

        FormElement anotherFormElementWithSameElements = new FormElement(new Tag("form"), "", new Attributes());
        for (Element element : formElement.elements()) {
            anotherFormElementWithSameElements.addElement(new Element(element.tag()) {
                @Override
                public Attributes attributes() {
                    return new Attributes(element.attributes());
                }
            });
        }
        assertTrue(formElement.equals(anotherFormElementWithSameElements));
    }
}