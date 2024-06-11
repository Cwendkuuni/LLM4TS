
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertNotNull(formElement.elements());
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testAddElement(){
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(element, formElement.elements().get(0));
        assertEquals(formElement, result);
    }

    @Test
    public void testRemoveChild(){
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testSubmitWithAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "http://example.com/submit");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithInput() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("john.doe");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("john.doe", keyVal.value());
    }

    @Test
    public void testFormDataWithSelect() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("selected","");
        optionElement.val("1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("select", keyVal.key());
        assertEquals("1", keyVal.value());
    }

    @Test
    public void testFormDataWithDisabledInput() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("disabled","");
        inputElement.attr("name", "username");
        inputElement.val("john.doe");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithCheckboxChecked() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("type", "checkbox");
        inputElement.attr("name", "agree");
        inputElement.attr("checked","");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("agree", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataWithRadioChecked(){
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("type", "radio");
        inputElement.attr("name", "gender");
        inputElement.attr("checked","");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("gender", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataWithTextInput() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.val("john.doe");
        formElement.addElement(inputElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("john.doe", keyVal.value());
    }

}
