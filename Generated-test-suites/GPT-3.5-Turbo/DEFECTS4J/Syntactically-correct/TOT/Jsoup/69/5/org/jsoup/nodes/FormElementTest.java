import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        // Typical use case: form has multiple elements
        Element element1 = new Element(Tag.valueOf("input"), formElement.baseUri());
        Element element2 = new Element(Tag.valueOf("select"), formElement.baseUri());
        Element element3 = new Element(Tag.valueOf("textarea"), formElement.baseUri());

        formElement.addElement(element1);
        formElement.addElement(element2);
        formElement.addElement(element3);

        Elements elements = formElement.elements();

        assertNotNull(elements);
        assertEquals(3, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
        assertTrue(elements.contains(element3));

        // Edge case: form has no elements
        Elements emptyElements = new Elements();
        assertEquals(emptyElements, formElement.elements());
    }

    @Test
    public void testAddElement() {
        // Typical use case: adding a form control element
        Element element = new Element(Tag.valueOf("input"), formElement.baseUri());

        FormElement updatedFormElement = formElement.addElement(element);

        assertNotNull(updatedFormElement);
        assertSame(formElement, updatedFormElement);
        assertTrue(formElement.elements().contains(element));

        // Edge case: adding a null form control element
        FormElement nullFormElement = formElement.addElement(null);
        assertSame(formElement, nullFormElement);
        assertTrue(formElement.elements().isEmpty());
    }

    @Test
    public void testSubmit() {
        // Typical use case: form with action and method attributes
        String action = "http://www.example.com/submit";
        Attributes attributes = new Attributes();
        attributes.put("action", action);
        attributes.put("method", "POST");
        FormElement formElement = new FormElement(Tag.valueOf("form"), formElement.baseUri(), attributes);

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals(action, connection.request().url().toString());

        // Edge case: form with missing action attribute
        FormElement formElementWithoutAction = new FormElement(Tag.valueOf("form"), formElement.baseUri(), new Attributes());

        try {
            formElementWithoutAction.submit();
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        // Typical use case: form with multiple form control elements
        Element inputElement = new Element(Tag.valueOf("input"), formElement.baseUri());
        inputElement.attr("name", "inputName");
        inputElement.val("inputValue");

        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri());
        selectElement.attr("name", "selectName");
        Element optionElement1 = new Element(Tag.valueOf("option"), formElement.baseUri());
        optionElement1.attr("selected", "");
        optionElement1.val("optionValue1");
        selectElement.appendChild(optionElement1);
        Element optionElement2 = new Element(Tag.valueOf("option"), formElement.baseUri());
        optionElement2.val("optionValue2");
        selectElement.appendChild(optionElement2);

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(2, formData.size());

        Connection.KeyVal inputKeyVal = formData.get(0);
        assertEquals("inputName", inputKeyVal.key());
        assertEquals("inputValue", inputKeyVal.value());

        Connection.KeyVal selectKeyVal = formData.get(1);
        assertEquals("selectName", selectKeyVal.key());
        assertEquals("optionValue1", selectKeyVal.value());

        // Edge case: form control element with disabled attribute
        Element disabledElement = new Element(Tag.valueOf("input"), formElement.baseUri());
        disabledElement.attr("name", "disabledName");
        disabledElement.attr("disabled", "");

        formElement.addElement(disabledElement);

        formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertFalse(formData.stream().anyMatch(keyVal -> keyVal.key().equals("disabledName")));
    }

    @Test
    public void testFormDataForSelectWithoutSelectedOption() {
        // Typical use case: form with select element without selected option, should choose the first option
        Element selectElement = new Element(Tag.valueOf("select"), formElement.baseUri());
        selectElement.attr("name", "selectName");
        Element optionElement = new Element(Tag.valueOf("option"), formElement.baseUri());
        optionElement.val("optionValue");
        selectElement.appendChild(optionElement);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal selectKeyVal = formData.get(0);
        assertEquals("selectName", selectKeyVal.key());
        assertEquals("optionValue", selectKeyVal.value());
    }

    @Test
    public void testFormDataForCheckbox() {
        // Typical use case: form with checked checkbox element
        Element checkboxElement = new Element(Tag.valueOf("input"), formElement.baseUri());
        checkboxElement.attr("name", "checkboxName");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");

        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal checkboxKeyVal = formData.get(0);
        assertEquals("checkboxName", checkboxKeyVal.key());
        assertEquals("on", checkboxKeyVal.value());

        // Edge case: form with unchecked checkbox element
        Element uncheckedCheckboxElement = new Element(Tag.valueOf("input"), formElement.baseUri());
        uncheckedCheckboxElement.attr("name", "uncheckedCheckboxName");
        uncheckedCheckboxElement.attr("type", "checkbox");

        formElement.addElement(uncheckedCheckboxElement);

        formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertFalse(formData.stream().anyMatch(keyVal -> keyVal.key().equals("uncheckedCheckboxName")));
    }

    @Test
    public void testFormDataForRadio() {
        // Typical use case: form with checked radio button element
        Element radioButtonElement = new Element(Tag.valueOf("input"), formElement.baseUri());
        radioButtonElement.attr("name", "radioButtonName");
        radioButtonElement.attr("type", "radio");
        radioButtonElement.attr("checked", "");

        formElement.addElement(radioButtonElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal radioButtonKeyVal = formData.get(0);
        assertEquals("radioButtonName", radioButtonKeyVal.key());
        assertEquals("on", radioButtonKeyVal.value());

        // Edge case: form with unchecked radio button element
        Element uncheckedRadioButtonElement = new Element(Tag.valueOf("input"), formElement.baseUri());
        uncheckedRadioButtonElement.attr("name", "uncheckedRadioButtonName");
        uncheckedRadioButtonElement.attr("type", "radio");

        formElement.addElement(uncheckedRadioButtonElement);

        formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertFalse(formData.stream().anyMatch(keyVal -> keyVal.key().equals("uncheckedRadioButtonName")));
    }

    @Test
    public void testFormDataForTextarea() {
        // Typical use case: form with filled textarea element
        Element textareaElement = new Element(Tag.valueOf("textarea"), formElement.baseUri());
        textareaElement.attr("name", "textareaName");
        textareaElement.text("textareaValue");

        formElement.addElement(textareaElement);

        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());

        Connection.KeyVal textareaKeyVal = formData.get(0);
        assertEquals("textareaName", textareaKeyVal.key());
        assertEquals("textareaValue", textareaKeyVal.value());

        // Edge case: form with empty textarea element
        Element emptyTextareaElement = new Element(Tag.valueOf("textarea"), formElement.baseUri());
        emptyTextareaElement.attr("name", "emptyTextareaName");

        formElement.addElement(emptyTextareaElement);

        formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(1, formData.size());
        assertFalse(formData.stream().anyMatch(keyVal -> keyVal.key().equals("emptyTextareaName")));
    }

}