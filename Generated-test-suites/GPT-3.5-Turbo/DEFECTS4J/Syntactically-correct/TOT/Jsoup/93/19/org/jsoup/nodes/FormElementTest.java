import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements_ReturnsEmptyElementsObject_WhenNoFormControlsAdded() {
        Elements expected = new Elements();
        Elements actual = formElement.elements();
        assertEquals(expected, actual);
    }

    @Test
    public void testElements_ReturnsAddedFormControls_WhenControlsAdded() {
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes());

        formElement.addElement(element1);
        formElement.addElement(element2);

        Elements expected = new Elements(element1, element2);
        Elements actual = formElement.elements();
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveChild_RemovesChildElementFromElements_WhenChildElementExists() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);

        formElement.removeChild(element);

        Elements expected = new Elements();
        Elements actual = formElement.elements();
        assertEquals(expected, actual);
    }

    @Test
    public void testSubmit_ReturnsConnectionWithGetMethodAndBaseUri_WhenFormActionNotSpecified() {
        Connection expected = Jsoup.connect("http://example.com")
                .data(formElement.formData())
                .method(Connection.Method.GET);

        Connection actual = formElement.submit();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_ThrowsIllegalArgumentException_WhenFormActionCannotBeDetermined() {
        formElement.attr("action", "");
        formElement.submit();
    }

    @Test
    public void testSubmit_ReturnsConnectionWithPostMethodAndActionUrl_WhenFormActionSpecified() {
        formElement.attr("action", "http://example.com/formHandler");
        Connection expected = Jsoup.connect("http://example.com/formHandler")
                .data(formElement.formData())
                .method(Connection.Method.POST);

        Connection actual = formElement.submit();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormData_ReturnsEmptyList_WhenNoFormControlsAdded() {
        List<Connection.KeyVal> expected = new ArrayList<>();
        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormData_ReturnsFormControlsWithValue_WhenFormControlsAdded() {
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("selected", "");
        option1.val("option1");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.val("option2");
        select.appendChild(option1);
        select.appendChild(option2);

        formElement.addElement(select);

        Element checkbox = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox.attr("type", "checkbox");
        checkbox.attr("name", "checkbox");
        checkbox.attr("checked", "");
        checkbox.val("checked");

        formElement.addElement(checkbox);

        Element textInput = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        textInput.attr("type", "text");
        textInput.attr("name", "textInput");
        textInput.val("inputValue");

        formElement.addElement(textInput);

        List<Connection.KeyVal> expected = new ArrayList<>();
        expected.add(HttpConnection.KeyVal.create("select", "option1"));
        expected.add(HttpConnection.KeyVal.create("checkbox", "checked"));
        expected.add(HttpConnection.KeyVal.create("textInput", "inputValue"));

        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormData_IgnoresDisabledFormInputs_WhenFormControlsContainDisabledInputs() {
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("selected", "");
        option1.val("option1");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.val("option2");
        select.appendChild(option1);
        select.appendChild(option2);

        formElement.addElement(select);

        Element disabledInput = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        disabledInput.attr("type", "text");
        disabledInput.attr("name", "disabled");
        disabledInput.attr("disabled", "");

        formElement.addElement(disabledInput);

        List<Connection.KeyVal> expected = new ArrayList<>();
        expected.add(HttpConnection.KeyVal.create("select", "option1"));

        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormData_IgnoresFormControlWithoutName_WhenFormControlsContainControlWithoutName() {
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("selected", "");
        option1.val("option1");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.val("option2");
        select.appendChild(option1);
        select.appendChild(option2);

        formElement.addElement(select);

        Element inputWithoutName = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputWithoutName.attr("type", "text");

        formElement.addElement(inputWithoutName);

        List<Connection.KeyVal> expected = new ArrayList<>();
        expected.add(HttpConnection.KeyVal.create("select", "option1"));

        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormData_AddsOnlyCheckedCheckboxOrRadioControls_WhenFormControlsContainCheckboxOrRadioControlsWithCheckedAttribute() {
        Element checkbox1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox1.attr("type", "checkbox");
        checkbox1.attr("name", "checkbox1");
        checkbox1.attr("checked", "");
        checkbox1.val("checked1");

        formElement.addElement(checkbox1);

        Element checkbox2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox2.attr("type", "checkbox");
        checkbox2.attr("name", "checkbox2");
        checkbox2.val("unchecked");

        formElement.addElement(checkbox2);

        Element radio1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radio1.attr("type", "radio");
        radio1.attr("name", "radio");
        radio1.attr("checked", "");
        radio1.val("checked");

        formElement.addElement(radio1);

        Element radio2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        radio2.attr("type", "radio");
        radio2.attr("name", "radio");
        radio2.val("unchecked");

        formElement.addElement(radio2);

        List<Connection.KeyVal> expected = new ArrayList<>();
        expected.add(HttpConnection.KeyVal.create("checkbox1", "checked1"));
        expected.add(HttpConnection.KeyVal.create("radio", "checked"));

        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormData_AddsAllFormControlsWithValue_WhenFormControlsContainMixOfControls() {
        Element select = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element option1 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option1.attr("selected", "");
        option1.val("option1");
        Element option2 = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        option2.val("option2");
        select.appendChild(option1);
        select.appendChild(option2);

        formElement.addElement(select);

        Element checkbox1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox1.attr("type", "checkbox");
        checkbox1.attr("name", "checkbox1");
        checkbox1.attr("checked", "");
        checkbox1.val("checked1");

        formElement.addElement(checkbox1);

        Element checkbox2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        checkbox2.attr("type", "checkbox");
        checkbox2.attr("name", "checkbox2");
        checkbox2.val("unchecked");

        formElement.addElement(checkbox2);

        Element textInput = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        textInput.attr("type", "text");
        textInput.attr("name", "textInput");
        textInput.val("inputValue");

        formElement.addElement(textInput);

        List<Connection.KeyVal> expected = new ArrayList<>();
        expected.add(HttpConnection.KeyVal.create("select", "option1"));
        expected.add(HttpConnection.KeyVal.create("checkbox1", "checked1"));
        expected.add(HttpConnection.KeyVal.create("textInput", "inputValue"));

        List<Connection.KeyVal> actual = formElement.formData();
        assertEquals(expected, actual);
    }
}