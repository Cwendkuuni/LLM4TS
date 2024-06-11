import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import org.jsoup.helper.HttpConnection;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals(tag.tagName(), formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testGetElements() {
        Elements expectedElements = new Elements();
        Element element1 = new Element("input", new Attributes());
        Element element2 = new Element("textarea", new Attributes());
        expectedElements.add(element1);
        expectedElements.add(element2);

        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.add(element1);
        formElement.add(element2);

        EqualsBuilder eqBuilder = EqualsBuilder.isEqualsIgnoringOrder(expectedElements);
        assertTrue(eqBuilder.isEqual(formElement.elements()));
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element elementToAdd = new Element("input", new Attributes());

        formElement.addElement(elementToAdd);
        Element elementAdded = formElement.elements().first();

        assertEquals(elementToAdd, elementAdded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNullFormAction() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithEmptyFormData() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.add(new Element("input", new Attributes().put("name", "name")));
        formElement.submit();
    }

    @Test
    public void testFormDataWithSingleSelectElement() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element selectElement = new Element("select", new Attributes());
        Element optionElement = new Element("option", new Attributes().put("value", "optionValue"));
        selectElement.add(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("name", "optionValue"));

        List<Connection.KeyVal> actualFormData = formElement.formData();
        EqualsBuilder eqBuilder = EqualsBuilder.isEqualsIgnoringOrder(expectedFormData);
        assertTrue(eqBuilder.isEqual(actualFormData));
    }

    @Test
    public void testFormDataWithMultipleSelectElements() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element selectElement1 = new Element("select", new Attributes());
        Element optionElement1 = new Element("option", new Attributes().put("value", "optionValue1"));
        selectElement1.add(optionElement1);
        formElement.addElement(selectElement1);

        Element selectElement2 = new Element("select", new Attributes());
        Element optionElement2 = new Element("option", new Attributes().put("value", "optionValue2"));
        selectElement2.add(optionElement2);
        formElement.addElement(selectElement2);

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(HttpConnection.KeyVal.create("name1", "optionValue1"));
        expectedFormData.add(HttpConnection.KeyVal.create("name2", "optionValue2"));

        List<Connection.KeyVal> actualFormData = formElement.formData();
        EqualsBuilder eqBuilder = EqualsBuilder.isEqualsIgnoringOrder(expectedFormData);
        assertTrue(eqBuilder.isEqual(actualFormData));
    }

    @Test
    public void testFormDataWithMultipleCheckboxElements() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element checkboxElement1 = new Element("input", new Attributes().put("type", "checkbox").put("name", "name").put("value", "value1"));
        Element checkboxElement2 = new Element("input", new Attributes().put("type", "checkbox").put("name", "name").put("value", "value2"));
        formElement.addElement(checkboxElement1);
        formElement.addElement(checkboxElement2);

        boolean checkbox1Selected = checkboxElement1.hasAttr("checked");
        boolean checkbox2Selected = checkboxElement2.hasAttr("checked");

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        if (checkbox1Selected) {
            expectedFormData.add(HttpConnection.KeyVal.create("name", "value1"));
        }

        if (checkbox2Selected) {
            expectedFormData.add(HttpConnection.KeyVal.create("name", "value2"));
        }

        List<Connection.KeyVal> actualFormData = formElement.formData();
        EqualsBuilder eqBuilder = EqualsBuilder.isEqualsIgnoringOrder(expectedFormData);
        assertTrue(eqBuilder.isEqual(actualFormData));
    }

    @Test
    public void testFormDataWithMultipleRadioElements() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element radioButtonElement1 = new Element("input", new Attributes().put("type", "radio").put("name", "name").put("value", "value1"));
        Element radioButtonElement2 = new Element("input", new Attributes().put("type", "radio").put("name", "name").put("value", "value2"));
        formElement.addElement(radioButtonElement1);
        formElement.addElement(radioButtonElement2);

        boolean radioButton1Selected = radioButtonElement1.hasAttr("checked");
        boolean radioButton2Selected = radioButtonElement2.hasAttr("checked");

        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        if (radioButton1Selected) {
            expectedFormData.add(HttpConnection.KeyVal.create("name", "value1"));
        } else if (radioButton2Selected) {
            expectedFormData.add(HttpConnection.KeyVal.create("name", "value2"));
        }

        List<Connection.KeyVal> actualFormData = formElement.formData();
        EqualsBuilder eqBuilder = EqualsBuilder.isEqualsIgnoringOrder(expectedFormData);
        assertTrue(eqBuilder.isEqual(actualFormData));
    }
}