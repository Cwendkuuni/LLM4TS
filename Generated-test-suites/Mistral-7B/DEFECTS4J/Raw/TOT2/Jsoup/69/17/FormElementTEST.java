package org.junit.tests;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertNotNull(formElement);
    }

    @Test
    public void testElements() {
        FormElement formElement = createFormElement();
        Element element = new Element("input");
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = createFormElement();
        Element element = new Element("input");
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidFormUrl() throws Exception {
        FormElement formElement = createFormElement();
        String invalidUrl = "";
        formElement.submit();
        fail("IllegalArgumentException should be thrown.");
    }

    @Test
    public void testFormDataEmptyFormElements() {
        FormElement formElement = createFormElementWithControls();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataNonFormSubmittableElements() {
        FormElement formElement = createFormElementWithNonFormSubmittableElements();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataDisabledFormControls() {
        FormElement formElement = createFormElementWithDisabledFormControls();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test(expected = NullPointerException.class)
    public void testFormElementConstructorNullInputs() {
        Tag tag = null;
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        new FormElement(tag, baseUri, attributes);
        fail("NullPointerException should be thrown.");
    }

    @Test
    public void testFormElementConstructorEmptyAttributes() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmitEmptyFormData() throws Exception {
        FormElement formElement = createFormElement();
        Connection connection = formElement.submit();
        fail("IllegalStateException should be thrown.");
    }

    private FormElement createFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        return new FormElement(tag, baseUri, attributes);
    }

    private FormElement createFormElementWithControls() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        inputTag1.attr("name", "name1");
        inputTag1.attr("value", "value1");
        Tag inputTag2 = new Tag("input");
        inputTag2.attr("name", "name2");
        inputTag2.attr("value", "value2");
        Tag selectTag = new Tag("select");
        Elements options = selectTag.select("option");
        options.add(new Tag("option").attr("value", "optionValue1").text("Option 1"));
        options.add(new Tag("option").attr("value", "optionValue2").text("Option 2"));
        selectTag.attr("name", "selectName");
        Element element = new Element("form");
        element.add(formTag);
        element.add(inputTag1);
        element.add(inputTag2);
        element.add(selectTag);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        return new FormElement(formTag, baseUri, attributes);
    }

    private FormElement createFormElementWithNonFormSubmittableElements() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input");
        inputTag.attr("name", "name");
        Tag divTag = new Tag("div"); // Non-form submittable element
        Element element = new Element("form");
        element.add(formTag);
        element.add(inputTag);
        element.add(divTag);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(element);
        return formElement;
    }

    private FormElement createFormElementWithDisabledFormControls() {
        Tag formTag = new Tag("form");
        Tag inputTag1 = new Tag("input");
        inputTag1.attr("name", "name1");
        inputTag1.attr("value", "value1");
        inputTag1.attr("disabled", "disabled");
        Tag inputTag2 = new Tag("input");
        inputTag2.attr("name", "name2");
        inputTag2.attr("value", "value2");
        Element element = new Element("form");
        element.add(formTag);
        element.add(inputTag1);
        element.add(inputTag2);
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(element);
        return formElement;
    }
}