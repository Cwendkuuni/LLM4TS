import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testAddElement() {
        // Creating a FormElement
        Tag formTag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // Creating an Element to be added to the form
        Tag inputTag = Tag.valueOf("input");
        String attrName = "name";
        String attrValue = "username";
        Attributes inputAttributes = new Attributes();
        inputAttributes.put(attrName, attrValue);

        Element inputElement = new Element(inputTag, baseUri, inputAttributes);

        // Adding the Element to the FormElement
        formElement.addElement(inputElement);

        // Checking if the Element was added successfully
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(inputElement, elements.get(0));
    }

    @Test
    public void testSubmit() throws IOException {
        // Creating a FormElement
        Tag formTag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // Adding form control elements to the form
        Tag inputTag1 = Tag.valueOf("input");
        String attrName1 = "name";
        String attrValue1 = "username";
        Attributes inputAttributes1 = new Attributes();
        inputAttributes1.put(attrName1, attrValue1);
        Element inputElement1 = new Element(inputTag1, baseUri, inputAttributes1);
        formElement.addElement(inputElement1);

        Tag inputTag2 = Tag.valueOf("input");
        String attrName2 = "name";
        String attrValue2 = "password";
        Attributes inputAttributes2 = new Attributes();
        inputAttributes2.put(attrName2, attrValue2);
        Element inputElement2 = new Element(inputTag2, baseUri, inputAttributes2);
        formElement.addElement(inputElement2);

        // Setting the form action attribute
        String actionUrl = "http://example.com/submit";
        formElement.attr("action", actionUrl);

        // Submitting the form and getting the Connection object
        Connection connection = formElement.submit();

        // Checking if the Connection object is prepared correctly
        assertEquals(actionUrl, connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());

        // Checking if the form data is correct
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals(attrValue1, formData.get(0).value());
        assertEquals(attrValue2, formData.get(1).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitInvalidActionUrl() {
        // Creating a FormElement without action attribute
        Tag formTag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // Submitting the form without action URL
        formElement.submit(); // Should throw IllegalArgumentException
    }

    @Test
    public void testFormData() {
        // Creating a FormElement
        Tag formTag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        // Adding form control elements to the form
        Tag inputTag1 = Tag.valueOf("input");
        String attrName1 = "name";
        String attrValue1 = "username";
        Attributes inputAttributes1 = new Attributes();
        inputAttributes1.put(attrName1, attrValue1);
        Element inputElement1 = new Element(inputTag1, baseUri, inputAttributes1);
        formElement.addElement(inputElement1);

        Tag inputTag2 = Tag.valueOf("input");
        String attrName2 = "name";
        String attrValue2 = "password";
        Attributes inputAttributes2 = new Attributes();
        inputAttributes2.put(attrName2, attrValue2);
        Element inputElement2 = new Element(inputTag2, baseUri, inputAttributes2);
        formElement.addElement(inputElement2);

        // Adding a disabled form control element to the form
        Tag inputTag3 = Tag.valueOf("input");
        String attrName3 = "name";
        String attrValue3 = "email";
        Attributes inputAttributes3 = new Attributes();
        inputAttributes3.put(attrName3, attrValue3);
        inputAttributes3.put("disabled", "");
        Element inputElement3 = new Element(inputTag3, baseUri, inputAttributes3);
        formElement.addElement(inputElement3);

        // Adding a form control element without name attribute to the form
        Tag inputTag4 = Tag.valueOf("input");
        Attributes inputAttributes4 = new Attributes();
        Element inputElement4 = new Element(inputTag4, baseUri, inputAttributes4);
        formElement.addElement(inputElement4);

        // Adding a select form control element to the form
        Tag selectTag = Tag.valueOf("select");
        String selectName = "gender";
        Element selectElement = new Element(selectTag, baseUri);
        selectElement.attr(attrName1, selectName);

        // Adding options to the select element
        Tag optionTag1 = Tag.valueOf("option");
        String optionValue1 = "male";
        Element optionElement1 = new Element(optionTag1, baseUri);
        optionElement1.val(optionValue1);
        selectElement.appendChild(optionElement1);

        Tag optionTag2 = Tag.valueOf("option");
        String optionValue2 = "female";
        Element optionElement2 = new Element(optionTag2, baseUri);
        optionElement2.val(optionValue2);
        selectElement.appendChild(optionElement2);

        Tag optionTag3 = Tag.valueOf("option");
        String optionValue3 = "other";
        Element optionElement3 = new Element(optionTag3, baseUri);
        optionElement3.val(optionValue3);
        selectElement.appendChild(optionElement3);

        formElement.addElement(selectElement);

        // Checking the form data
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(4, formData.size());
        assertEquals(attrValue1, formData.get(0).value());
        assertEquals(attrValue2, formData.get(1).value());
        assertEquals(optionValue1, formData.get(2).value());
        assertEquals(optionValue2, formData.get(3).value());
    }
}