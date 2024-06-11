import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        // Create a dummy form element for testing
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);

        // Add some dummy form control elements to the form
        Element inputElement = new Element(Tag.valueOf("input"), baseUri);
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.val("JohnDoe");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), baseUri);
        Element optionElement1 = new Element(Tag.valueOf("option"), baseUri);
        optionElement1.attr("value", "option1");
        optionElement1.text("Option 1");
        selectElement.append(optionElement1);
        Element optionElement2 = new Element(Tag.valueOf("option"), baseUri);
        optionElement2.attr("value", "option2");
        optionElement2.text("Option 2");
        selectElement.append(optionElement2);
        formElement.addElement(selectElement);

        Element checkboxElement1 = new Element(Tag.valueOf("input"), baseUri);
        checkboxElement1.attr("type", "checkbox");
        checkboxElement1.attr("name", "checkbox1");
        checkboxElement1.val("value1");
        checkboxElement1.attr("checked", "checked");
        formElement.addElement(checkboxElement1);

        Element checkboxElement2 = new Element(Tag.valueOf("input"), baseUri);
        checkboxElement2.attr("type", "checkbox");
        checkboxElement2.attr("name", "checkbox2");
        checkboxElement2.val("value2");
        formElement.addElement(checkboxElement2);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(4, elements.size());

        // Check if all added form control elements are present in the returned elements
        assertTrue(elements.hasAttr("name", "username"));
        assertTrue(elements.hasAttr("name", "checkbox1"));
        assertTrue(elements.hasAttr("name", "checkbox2"));
        assertTrue(elements.hasAttr("value", "option1"));
    }

    @Test
    public void testAddElement() {
        Element newInputElement = new Element(Tag.valueOf("input"), "http://example.com");
        newInputElement.attr("type", "password");
        newInputElement.attr("name", "password");

        FormElement newFormElement = formElement.addElement(newInputElement);

        assertNotNull(newFormElement);
        assertSame(formElement, newFormElement);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(5, elements.size());

        // Check if the new form control element is present in the form
        assertTrue(elements.hasAttr("name", "password"));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);

        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());

        List<Connection.KeyVal> formData = connection.request().data();
        assertNotNull(formData);
        assertEquals(3, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("username", "JohnDoe")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("checkbox1", "value1")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("checkbox2", "value2")));

        // Test if 'action' attribute is empty
        FormElement formElementNoAction = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertThrows(IllegalArgumentException.class, formElementNoAction::submit);
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(3, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("username", "JohnDoe")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("checkbox1", "value1")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("checkbox2", "value2")));

        // Test if form control without 'name' attribute is excluded
        Element inputElementNoName = new Element(Tag.valueOf("input"), "http://example.com");
        inputElementNoName.attr("type", "text");
        formElement.addElement(inputElementNoName);
        formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(3, formData.size());

        // Test if 'select' element with no selected 'option' sets the first option as value
        Element selectElementNoSelectedOption = new Element(Tag.valueOf("select"), "http://example.com");
        selectElementNoSelectedOption.attr("name", "selectField");
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com");
        optionElement.attr("value", "option1");
        optionElement.text("Option 1");
        selectElementNoSelectedOption.append(optionElement);
        formElement.addElement(selectElementNoSelectedOption);
        formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(4, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("selectField", "option1")));

        // Test if 'checkbox' element without 'checked' attribute is excluded
        Element checkboxElementUnchecked = new Element(Tag.valueOf("input"), "http://example.com");
        checkboxElementUnchecked.attr("type", "checkbox");
        checkboxElementUnchecked.attr("name", "checkbox3");
        checkboxElementUnchecked.val("unchecked");
        formElement.addElement(checkboxElementUnchecked);
        formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(4, formData.size());
    }

    @Test
    public void testEquals() {
        FormElement sameFormElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(formElement.equals(sameFormElement));

        FormElement differentFormElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        differentFormElement.addElement(new Element(Tag.valueOf("input"), "http://example.com"));
        assertFalse(formElement.equals(differentFormElement));

        assertFalse(formElement.equals(null));
        assertFalse(formElement.equals(new Object()));
    }

}