import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = mock(Tag.class);
        String baseUri = "http://example.com";
        Attributes attributes = mock(Attributes.class);

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Assert.assertEquals(tag, formElement.tag());
        Assert.assertEquals(baseUri, formElement.baseUri());
        Assert.assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testGetElements() {
        Elements elements = Mockito.mock(Elements.class);
        FormElement formElement = new FormElement(null, null, null);
        formElement.elements = elements;

        Assert.assertEquals(elements, formElement.elements());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = Mockito.spy(new FormElement(null, null, null));
        Element element = mock(Element.class);

        formElement.addElement(element);

        Assert.assertEquals(element, formElement.elements().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        FormElement formElement = new FormElement(null, null, null);
        formElement.submit();
    }

    @Test
    public void testFormDataEmptyForm() {
        FormElement formElement = new FormElement(null, null, null);
        List<Connection.KeyVal> result = formElement.formData();

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testFormDataSingleInput() {
        Element input = mock(Element.class);
        FormElement formElement = new FormElement(null, null, null);
        formElement.addElement(input);
        input.attr("name", "inputName");
        input.attr("value", "test");

        List<Connection.KeyVal> result = formElement.formData();

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("inputName", result.get(0).key);
        Assert.assertEquals("test", result.get(0).value);
    }

    @Test
    public void testFormDataMultipleInputs() {
        Element input1 = mock(Element.class);
        Element input2 = mock(Element.class);
        FormElement formElement = new FormElement(null, null, null);
        formElement.addElement(input1);
        formElement.addElement(input2);
        input1.attr("name", "inputName1");
        input1.attr("value", "test1");
        input2.attr("name", "inputName2");
        input2.attr("value", "test2");

        List<Connection.KeyVal> result = formElement.formData();

        Assert.assertEquals(2, result.size());
        Assert.assertEquals("inputName1", result.get(0).key);
        Assert.assertEquals("test1", result.get(0).value);
        Assert.assertEquals("inputName2", result.get(1).key);
        Assert.assertEquals("test2", result.get(1).value);
    }

    @Test
    public void testFormDataSelectElement() {
        Element select = mock(Element.class);
        Element option1 = mock(Element.class);
        Element option2 = mock(Element.class);
        FormElement formElement = new FormElement(null, null, null);
        formElement.addElement(select);
        select.attr("name", "selectName");
        select.attr("multiple", "multiple");
        Elements options = Mockito.mock(Elements.class);
        when(select.select("option[selected]")).thenReturn(options);
        when(options.size()).thenReturn(1);
        when(options.get(0)).thenReturn(option1);
        when(option1.val()).thenReturn("optionValue1");

        List<Connection.KeyVal> result = formElement.formData();

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("selectName", result.get(0).key);
        Assert.assertEquals("optionValue1", result.get(0).value);
    }

    @Test
    public void testFormDataCheckboxOrRadio() {
        Element checkbox = mock(Element.class);
        FormElement formElement = new FormElement(null, null, null);
        formElement.addElement(checkbox);
        checkbox.attr("name", "checkboxName");
        checkbox.hasAttr("checked");

        List<Connection.KeyVal> result = formElement.formData();

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("checkboxName", result.get(0).key);
        Assert.assertTrue(result.get(0).value.equals("on") || result.get(0).value.equals(""));
    }

    @Test
    public void testSubmitWithException() {
        FormElement formElement = new FormElement(null, null, null);
        try {
            formElement.submit();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("Could not determine a form action URL for submit"));
        }
    }
}