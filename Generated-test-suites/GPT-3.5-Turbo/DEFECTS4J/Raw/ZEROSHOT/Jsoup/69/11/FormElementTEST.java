import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testAddElement() {
        Element element1 = new Element(Tag.valueOf("input"), "baseURI", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "baseURI", new Attributes());

        FormElement formElement = new FormElement(Tag.valueOf("form"), "baseURI", new Attributes());
        formElement.addElement(element1);
        formElement.addElement(element2);

        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
    }

    @Test
    public void testSubmit_withoutAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        
        try {
            formElement.submit();
            fail("IllegalArgumentException should be thrown if the form's absolute action URL cannot be determined.");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testSubmit_withAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("method", "post");

        Connection connection = formElement.submit();
        
        assertEquals(Method.POST, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
        assertTrue(connection.request().data().isEmpty());
    }

    @Test
    public void testFormData_emptyForm() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "baseURI", new Attributes());

        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_withFormControls() {
        Element inputElement = new Element(Tag.valueOf("input"), "baseURI", new Attributes());
        inputElement.attr("name", "inputName");
        inputElement.val("inputValue");
        
        Element selectElement = new Element(Tag.valueOf("select"), "baseURI", new Attributes());
        selectElement.attr("name", "selectName");
        selectElement.append("<option value='option1'>Option 1</option>");
        selectElement.append("<option value='option2'>Option 2</option>");
        selectElement.select("option[value=option2]").attr("selected", "");
        
        FormElement formElement = new FormElement(Tag.valueOf("form"), "baseURI", new Attributes());
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        
        Connection.KeyVal inputKeyVal = formData.get(0);
        assertEquals("inputName", inputKeyVal.key());
        assertEquals("inputValue", inputKeyVal.value());
        
        Connection.KeyVal selectKeyVal = formData.get(1);
        assertEquals("selectName", selectKeyVal.key());
        assertEquals("option2", selectKeyVal.value());
    }
}