import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import static org.mockito.Mockito.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = mock(Tag.class);
        String baseUri = "";
        Attributes attributes = mock(Attributes.class);
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements_ReturnsElements() {
        Elements elements = formElement.elements();
        Assert.assertNotNull(elements);
    }

    @Test
    public void testAddElement_AddsElement() {
        Element element = mock(Element.class);
        FormElement result = formElement.addElement(element);
        Assert.assertEquals(formElement, result);
        Assert.assertTrue(formElement.elements.contains(element));
    }

    @Test
    public void testRemoveChild_RemovesChild() {
        Node out = mock(Node.class);
        formElement.elements.add(out);
        formElement.removeChild(out);
        Assert.assertFalse(formElement.elements.contains(out));
    }

    @Test
    public void testSubmit_ReturnsConnection() {
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_ThrowsIllegalArgumentException() {
        formElement.submit();
    }

    @Test
    public void testFormData_ReturnsKeyVals() {
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
    }

    @Test
    public void testFormData_ReturnsEmptyList_NoFormControlElements() {
        Elements elements = mock(Elements.class);
        formElement.elements = elements;
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_ReturnsEmptyList_DisabledFormInputs() {
        Element element = mock(Element.class);
        when(element.hasAttr("disabled")).thenReturn(true);
        formElement.elements.add(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_ReturnsEmptyList_NoNameAttribute() {
        Element element = mock(Element.class);
        when(element.hasAttr("disabled")).thenReturn(false);
        when(element.attr("name")).thenReturn("");
        formElement.elements.add(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_ReturnsKeyVals_SelectElement() {
        Element element = mock(Element.class);
        when(element.normalName()).thenReturn("select");
        when(element.select("option[selected]")).thenReturn(new Elements());
        when(element.select("option")).thenReturn(new Elements());
        formElement.elements.add(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
    }

    @Test
    public void testFormData_ReturnsKeyVals_SelectElement_NoOptions() {
        Element element = mock(Element.class);
        when(element.normalName()).thenReturn("select");
        when(element.select("option[selected]")).thenReturn(new Elements());
        when(element.select("option")).thenReturn(null);
        formElement.elements.add(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertNotNull(formData);
    }

    @Test
    public void testFormData_ReturnsKeyVals_CheckboxElement() {
        Element element = mock(Element.class);
        when(element.hasAttr("checked")).thenReturn(true);
        when(element.attr("name")).thenReturn("checkbox");
        when(element.val()).thenReturn("value");
        formElement.elements.add(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertFalse(formData.isEmpty());
    }

    @Test
    public void testFormData_ReturnsKeyVals_CheckboxElement_NoCheckedAttribute() {
        Element element = mock(Element.class);
        when(element.hasAttr("checked")).thenReturn(false);
        formElement.elements.add(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_ReturnsKeyVals_RadioElement() {
        Element element = mock(Element.class);
        when(element.hasAttr("checked")).thenReturn(true);
        when(element.attr("name")).thenReturn("radio");
        when(element.val()).thenReturn("value");
        formElement.elements.add(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertFalse(formData.isEmpty());
    }

    @Test
    public void testFormData_ReturnsKeyVals_RadioElement_NoCheckedAttribute() {
        Element element = mock(Element.class);
        when(element.hasAttr("checked")).thenReturn(false);
        formElement.elements.add(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormData_ReturnsKeyVals_OtherElements() {
        Element element = mock(Element.class);
        when(element.attr("name")).thenReturn("name");
        formElement.elements.add(element);
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertFalse(formData.isEmpty());
    }
}