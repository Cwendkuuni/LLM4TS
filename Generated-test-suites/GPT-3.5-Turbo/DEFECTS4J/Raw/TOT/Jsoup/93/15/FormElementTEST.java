import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class FormElementTest {

    FormElement formElement;
    Element formElementToAdd;

    @BeforeEach
    void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
        formElementToAdd = new Element(Tag.valueOf("input"), baseUri);
    }

    @Test
    void testElements_returnElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
    }

    @Test
    void testAddElement_addElementToForm() {
        formElement.addElement(formElementToAdd);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertFalse(elements.isEmpty());
        assertTrue(elements.contains(formElementToAdd));
    }

    @Test
    void testRemoveChild_removeElementFromForm() {
        formElement.addElement(formElementToAdd);
        formElement.removeChild(formElementToAdd);
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());
        assertFalse(elements.contains(formElementToAdd));
    }

    @Test
    void testSubmit_withoutAction_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            formElement.submit();
        });
    }

    @Test
    void testSubmit_withAction_returnConnection() {
        formElement.attr("action", "http://www.example.com");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://www.example.com", connection.request().url().toString());
    }

    @Test
    void testFormData_withEmptyForm_returnEmptyList() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    void testFormData_withDisabledElement_skipDisabledElement() {
        formElementToAdd.attr("disabled", "");
        formElement.addAttribute("method", "post");
        formElement.addElement(formElementToAdd);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    void testFormData_withInputElements_returnData() {
        formElementToAdd.attr("name", "input1");
        formElementToAdd.attr("type", "text");
        formElementToAdd.val("Value1");
        formElement.addAttribute("method", "get");
        formElement.addElement(formElementToAdd);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("Value1", formData.get(0).value());
    }

    @Test
    void testFormData_withSelectElement_returnData() {
        Element option1 = new Element(Tag.valueOf("option"), formElement.baseUri());
        option1.attr("value", "option1");
        option1.attr("selected", "");
        Element option2 = new Element(Tag.valueOf("option"), formElement.baseUri());
        option2.attr("value", "option2");
        formElementToAdd.attr("name", "select1");
        formElementToAdd.attr("type", "select");
        formElementToAdd.appendChild(option1);
        formElementToAdd.appendChild(option2);
        formElement.addAttribute("method", "post");
        formElement.addElement(formElementToAdd);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("select1", formData.get(0).key());
        asssertEquals("option1", formData.get(0).value());
    }

    @Test
    void testFormData_withCheckboxElement_checked_returnData() {
        formElementToAdd.attr("name", "checkbox1");
        formElementToAdd.attr("type", "checkbox");
        formElement.attr("checked", "");
        formElement.addAttribute("method", "get");
        formElement.addElement(formElementToAdd);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("checkbox1", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }

    @Test
    void testFormData_withCheckboxElement_unchecked_skipElement() {
        formElementToAdd.attr("name", "checkbox1");
        formElementToAdd.attr("type", "checkbox");
        formElement.addAttribute("method", "post");
        formElement.addElement(formElementToAdd);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }

    @Test
    void testFormData_withRadioElement_checked_returnData() {
        formElementToAdd.attr("name", "radio1");
        formElementToAdd.attr("type", "radio");
        formElement.attr("checked", "");
        formElement.addAttribute("method", "get");
        formElement.addElement(formElementToAdd);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertFalse(formData.isEmpty());
        assertEquals(1, formData.size());
        assertEquals("radio1", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }

    @Test
    void testFormData_withRadioElement_unchecked_skipElement() {
        formElementToAdd.attr("name", "radio1");
        formElementToAdd.attr("type", "radio");
        formElement.addAttribute("method", "post");
        formElement.addElement(formElementToAdd);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertTrue(formData.isEmpty());
    }
}