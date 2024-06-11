import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = mock(Tag.class);
        String baseUri = "baseUri";
        Attributes attributes = mock(Attributes.class);

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Assert.assertNotNull(formElement);
        Assert.assertSame(tag, formElement.tag());
        Assert.assertEquals(baseUri, formElement.baseUri());
        Assert.assertNotNull(formElement.elements());
    }

    @Test
    public void testElementsReturnEmptyListWhenNoElements() {
        Tag tag = mock(Tag.class);
        String baseUri = "baseUri";
        Attributes attributes = mock(Attributes.class);

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        List<Element> elements = formElement.elements();
        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testAddElementToForm() {
        Tag formTag = mock(Tag.class);
        Tag inputTag = mock(Tag.class);
        String baseUri = "baseUri";
        Attributes formAttributes = mock(Attributes.class);
        Attributes inputAttributes = mock(Attributes.class);

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        Element inputElement = new FormElement(inputTag, baseUri, inputAttributes);

        formElement.addElement(inputElement);

        Elements elements = formElement.elements();
        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(1, elements.size());
        Assert.assertSame(inputElement, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitThrowsIllegalArgumentExceptionWhenNoActionAttribute() {
        Tag formTag = mock(Tag.class);
        String baseUri = "baseUri";
        Attributes attributes = mock(Attributes.class);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        formElement.submit();
    }

    @Test
    public void testSubmitReturnsConnectionObject() {
        Tag formTag = mock(Tag.class);
        String baseUri = "baseUri";
        String action = "actionURL";
        Attributes attributes = mock(Attributes.class);
        Connection connection = mock(Connection.class);

        when(formTag.hasAttr("action")).thenReturn(true);
        when(formTag.attr("action")).thenReturn(action);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        Connection result = formElement.submit();

        Assert.assertSame(connection, result);
    }

    @Test
    public void testFormDataReturnsEmptyListWhenNoElements() {
        Tag formTag = mock(Tag.class);
        String baseUri = "baseUri";
        Attributes attributes = mock(Attributes.class);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertFalse(formData.isEmpty());
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataReturnsKeyValListWithSingleElement() {
        Tag formTag = mock(Tag.class);
        String baseUri = "baseUri";
        String name = "name";
        String value = "value";
        Attributes attributes = mock(Attributes.class);
        Element element = mock(Element.class);

        when(formTag.tagName()).thenReturn("form");
        when(formTag.hasAttr("name")).thenReturn(true);
        when(formTag.attr("name")).thenReturn(name);
        when(formTag.hasAttr("value")).thenReturn(false);
        when(formTag.attr("type")).thenReturn("text");
        when(element.tagName()).thenReturn("input");
        when(element.attr("name")).thenReturn(name);
        when(element.val()).thenReturn(value);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        formElement.addElement(element);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertFalse(formData.isEmpty());
        Assert.assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        Assert.assertEquals(name, keyVal.key());
        Assert.assertEquals(value, keyVal.value());
    }

    @Test
    public void testFormDataReturnsKeyValListWithMultipleElementsOfSameName() {
        Tag formTag = mock(Tag.class);
        String baseUri = "baseUri";
        String name = "name";
        String value1 = "value1";
        String value2 = "value2";

        Attributes attributes = mock(Attributes.class);
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);

        when(formTag.tagName()).thenReturn("form");
        when(formTag.hasAttr("name")).thenReturn(true);
        when(formTag.attr("name")).thenReturn(name);

        when(formTag.select("input[name='" + name + "']")).thenReturn(new Elements(element1, element2));
        when(element1.tagName()).thenReturn("input");
        when(element1.attr("name")).thenReturn(name);
        when(element1.val()).thenReturn(value1);
        when(element2.tagName()).thenReturn("input");
        when(element2.attr("name")).thenReturn(name);
        when(element2.val()).thenReturn(value2);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertFalse(formData.isEmpty());
        Assert.assertEquals(2, formData.size());

        for (int i = 0; i < formData.size(); i++) {
            Connection.KeyVal keyVal = formData.get(i);
            if (i == 0) {
                Assert.assertEquals(name, keyVal.key());
                Assert.assertEquals(value1, keyVal.value());
            } else {
                Assert.assertEquals(name, keyVal.key());
                Assert.assertEquals(value2, keyVal.value());
            }
        }
    }

    @Test
    public void testFormDataReturnsKeyValListWithMultipleElementsOfDifferentNames() {
        Tag formTag = mock(Tag.class);
        String baseUri = "baseUri";
        String name1 = "name1";
        String value1 = "value1";
        String name2 = "name2";
        String value2 = "value2";

        Attributes attributes = mock(Attributes.class);
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);

        when(formTag.tagName()).thenReturn("form");
        when(formTag.hasAttr("name")).thenReturn(false);

        when(formTag.select("input")).thenReturn(new Elements(element1, element2));
        when(element1.tagName()).thenReturn("input");
        when(element1.attr("name")).thenReturn(name1);
        when(element1.val()).thenReturn(value1);
        when(element2.tagName()).thenReturn("input");
        when(element2.attr("name")).thenReturn(name2);
        when(element2.val()).thenReturn(value2);

        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();

        Assert.assertFalse(formData.isEmpty());
        Assert.assertEquals(2, formData.size());

        for (int i = 0; i < formData.size(); i++) {
            Connection.KeyVal keyVal = formData.get(i);
            if (i == 0) {
                Assert.assertEquals(name1, keyVal.key());
                Assert.assertEquals(value1, keyVal.value());
            } else {
                Assert.assertEquals(name2, keyVal.key());
                Assert.assertEquals(value2, keyVal.value());
            }
        }
    }
}