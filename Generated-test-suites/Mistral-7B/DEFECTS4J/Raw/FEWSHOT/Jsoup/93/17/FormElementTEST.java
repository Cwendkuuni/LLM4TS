package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    private static final String BASE_URI = "http://test.com";
    private static final String FORM_TAG = "form";
    private static final String TEXT_INPUT_TAG = "input";
    private static final String SELECT_INPUT_TAG = "select";
    private static final String CHECKBOX_INPUT_TAG = "input";
    private static final String RADIO_INPUT_TAG = "input";
    private static final String OPTION_TAG = "option";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String TYPE_ATTRIBUTE = "type";
    private static final String DISABLED_ATTRIBUTE = "disabled";
    private static final String CHECKED_ATTRIBUTE = "checked";
    private static final String ACTION_ATTRIBUTE = "action";
    private static final String METHOD_ATTRIBUTE = "method";

    private Element formElement;
    private Element textInputElement;
    private Element selectInputElement;
    private Element checkboxInputElement;
    private Element radioInputElement;
    private Element disabledTextInputElement;
    private Element disabledSelectInputElement;
    private Element disabledCheckboxInputElement;
    private Element disabledRadioInputElement;
    private Element actionElement;

    @Before
    public void setup() {
        Tag formTag = new Tag(FORM_TAG);
        formTag.putAttr(BASE_URI);
        formElement = new FormElement(formTag, BASE_URI, new Attributes());

        Tag textInputTag = new Tag(TEXT_INPUT_TAG);
        textInputTag.putAttr(NAME_ATTRIBUTE, "textInput");
        textInputTag.putAttr(VALUE_ATTRIBUTE, "textValue");
        textInputElement = new Element(textInputTag, BASE_URI, new Attributes());

        Tag selectInputTag = new Tag(SELECT_INPUT_TAG);
        selectInputTag.putAttr(NAME_ATTRIBUTE, "selectInput");
        Tag selectOptionTag1 = new Tag(OPTION_TAG);
        selectOptionTag1.putAttr(VALUE_ATTRIBUTE, "optionValue1");
        Tag selectOptionTag2 = new Tag(OPTION_TAG);
        selectOptionTag2.putAttr(VALUE_ATTRIBUTE, "optionValue2");
        selectInputElement = new Element(selectInputTag, BASE_URI, new Attributes());
        Elements selectOptions = selectInputElement.select("option");
        selectOptions.add(selectOptionTag1);
        selectOptions.add(selectOptionTag2);

        Tag checkboxInputTag = new Tag(CHECKBOX_INPUT_TAG);
        checkboxInputTag.putAttr(NAME_ATTRIBUTE, "checkboxInput");
        checkboxInputElement = new Element(checkboxInputTag, BASE_URI, new Attributes());

        Tag radioInputTag1 = new Tag(RADIO_INPUT_TAG);
        radioInputTag1.putAttr(NAME_ATTRIBUTE, "radioInput");
        radioInputTag1.putAttr(VALUE_ATTRIBUTE, "radioValue1");
        radioInputElement = new Element(radioInputTag1, BASE_URI, new Attributes());

        Tag radioInputTag2 = new Tag(RADIO_INPUT_TAG);
        radioInputTag2.putAttr(NAME_ATTRIBUTE, "radioInput");
        radioInputTag2.putAttr(VALUE_ATTRIBUTE, "radioValue2");
        radioInputElement.putAttr(CHECKED_ATTRIBUTE, "true");

        Tag disabledTextInputTag = new Tag(TEXT_INPUT_TAG);
        disabledTextInputTag.putAttr(NAME_ATTRIBUTE, "disabledTextInput");
        disabledTextInputTag.putAttr(DISABLED_ATTRIBUTE, "disabled");
        disabledTextInputElement = new Element(disabledTextInputTag, BASE_URI, new Attributes());

        Tag disabledSelectInputTag = new Tag(SELECT_INPUT_TAG);
        disabledSelectInputTag.putAttr(NAME_ATTRIBUTE, "disabledSelectInput");
        disabledSelectInputTag.putAttr(DISABLED_ATTRIBUTE, "disabled");
        disabledSelectInputElement = new Element(disabledSelectInputTag, BASE_URI, new Attributes());

        Tag disabledCheckboxInputTag = new Tag(CHECKBOX_INPUT_TAG);
        disabledCheckboxInputTag.putAttr(NAME_ATTRIBUTE, "disabledCheckboxInput");
        disabledCheckboxInputTag.putAttr(DISABLED_ATTRIBUTE, "disabled");
        disabledCheckboxInputElement = new Element(disabledCheckboxInputTag, BASE_URI, new Attributes());

        Tag disabledRadioInputTag1 = new Tag(RADIO_INPUT_TAG);
        disabledRadioInputTag1.putAttr(NAME_ATTRIBUTE, "disabledRadioInput");
        disabledRadioInputTag1.putAttr(DISABLED_ATTRIBUTE, "disabled");
        disabledRadioInputElement = new Element(disabledRadioInputTag1, BASE_URI, new Attributes());

        Tag actionTag = new Tag("input");
        actionTag.putAttr(TYPE_ATTRIBUTE, "hidden");
        actionTag.putAttr(NAME_ATTRIBUTE, "action");
        actionTag.putAttr(VALUE_ATTRIBUTE, "/submit");
        actionElement = new Element(actionTag, BASE_URI, new Attributes());
        formElement.addElement(actionElement);

        formElement.addElement(textInputElement);
        formElement.addElement(selectInputElement);
        formElement.addElement(checkboxInputElement);
        formElement.addElement(radioInputElement);
        formElement.addElement(disabledTextInputElement);
        formElement.addElement(disabledSelectInputElement);
        formElement.addElement(disabledCheckboxInputElement);
        formElement.addElement(disabledRadioInputElement);
    }

    @Test
    public void testElements() {
        List<Element> expectedElements = new ArrayList<>(Arrays.asList(
                textInputElement,
                selectInputElement,
                checkboxInputElement,
                radioInputElement,
                disabledTextInputElement,
                disabledSelectInputElement,
                disabledCheckboxInputElement,
                disabledRadioInputElement
        ));

        List<Element> actualElements = formElement.elements();

        assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            assertSame(expectedElements.get(i), actualElements.get(i));
        }
    }

    @Test
    public void testAddElement() {
        Element newTextInputElement = new Element(new Tag(TEXT_INPUT_TAG), BASE_URI, new Attributes());
        formElement.addElement(newTextInputElement);

        List<Element> actualElements = formElement.elements();
        List<Element> expectedElements = new ArrayList<>(Arrays.asList(
                textInputElement,
                selectInputElement,
                checkboxInputElement,
                radioInputElement,
                disabledTextInputElement,
                disabledSelectInputElement,
                disabledCheckboxInputElement,
                disabledRadioInputElement,
                newTextInputElement
        ));

        assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            assertSame(expectedElements.get(i), actualElements.get(i));
        }
    }

    @Test
    public void testRemoveChild() {
        formElement.removeChild(textInputElement);

        List<Element> actualElements = formElement.elements();
        List<Element> expectedElements = new ArrayList<>(Arrays.asList(
                selectInputElement,
                checkboxInputElement,
                radioInputElement,
                disabledTextInputElement,
                disabledSelectInputElement,
                disabledCheckboxInputElement,
                disabledRadioInputElement
        ));

        assertEquals(expectedElements.size(), actualElements.size());
        for (int i = 0; i < expectedElements.size(); i++) {
            assertSame(expectedElements.get(i), actualElements.get(i));
        }

        assertFalse(formElement.elements().contains(textInputElement));
        assertNull(textInputElement.parent());
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();

        assertEquals("/submit", connection.url());
        assertEquals(formData(), connection.data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        FormElement noActionFormElement = new FormElement(new Tag(FORM_TAG), BASE_URI, new Attributes());
        noActionFormElement.submit();
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("textInput", "textValue"));
        expectedFormData.add(HttpConnection.KeyVal.create("selectInput", "optionValue1"));
        expectedFormData.add(HttpConnection.KeyVal.create("checkboxInput", "on"));
        expectedFormData.add(HttpConnection.KeyVal.create("radioInput", "radioValue2"));

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData.size(), actualFormData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            assertEquals(expectedFormData.get(i).key, actualFormData.get(i).key);
            assertEquals(expectedFormData.get(i).value, actualFormData.get(i).value);
        }
    }

    @Test
    public void testFormDataSelectInput() {
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("selectInput", "optionValue1"));

        textInputElement.remove();
        selectInputElement.tag().putAttr("selected", "true");

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData.size(), actualFormData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            assertEquals(expectedFormData.get(i).key, actualFormData.get(i).key);
            assertEquals(expectedFormData.get(i).value, actualFormData.get(i).value);
        }
    }

    @Test
    public void testFormDataCheckboxInput() {
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("checkboxInput", "on"));

        checkboxInputElement.putAttr("checked", "true");
        textInputElement.remove();

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData.size(), actualFormData.size());
        for (int i = 0; i < expectedFormData.size(); i++) {
            assertEquals(expectedFormData.get(i).key, actualFormData.get(i).key);
            assertEquals(expectedFormData.get(i).value, actualFormData.get(i).value);
        }
    }

    @Test
    public void testFormDataDisabledElements() {
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();

        textInputElement.remove();
        selectInputElement.tag().putAttr("disabled", "disabled");
        checkboxInputElement.putAttr("disabled", "disabled");
        radioInputElement.tag().putAttr("disabled", "disabled");
        disabledTextInputElement.remove();
        disabledSelectInputElement.tag().putAttr("disabled", "disabled");
        disabledCheckboxInputElement.putAttr("disabled", "disabled");
        disabledRadioInputElement.tag().putAttr("disabled", "disabled");

        List<Connection.KeyVal> actualFormData = formElement.formData();

        assertEquals(expectedFormData.size(), actualFormData.size());
        assertTrue(actualFormData.isEmpty());
    }
}