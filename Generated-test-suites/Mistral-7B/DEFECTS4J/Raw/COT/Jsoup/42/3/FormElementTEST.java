import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;
import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Assert.assertNotNull(formElement);
        Assert.assertEquals("form", formElement.tagName());
    }

    @Test
    public void testElementsMethodReturnEmptyListWhenFormHasNoControls() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Elements elements = formElement.elements();
        Assert.assertTrue(elements.isEmpty());
    }

    @Test
    public void testElementsMethodReturnListOfControlsForFormWithControls() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();

        Tag inputTag1 = new Tag("input");
        inputTag1.attr("type", "text");
        inputTag1.attr("name", "field1");

        Tag inputTag2 = new Tag("input");
        inputTag2.attr("type", "text");
        inputTag2.attr("name", "field2");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);

        Elements elements = formElement.elements();
        List<Element> expectedElements = new ArrayList<>();
        expectedElements.add(inputTag1);
        expectedElements.add(inputTag2);

        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(expectedElements, elements);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodThrowsIllegalArgumentExceptionWhenFormHasNoActionAttribute() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        formElement.submit();
    }

    @Test
    public void testSubmitMethodPreparesConnectionToFormActionURLWithFormData() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();
        formTag.attr("action", "http://example.com/submit-form");

        Tag inputTag1 = new Tag("input");
        inputTag1.attr("type", "text");
        inputTag1.attr("name", "field1");
        inputTag1.attr("value", "value1");

        Tag inputTag2 = new Tag("input");
        inputTag2.attr("type", "text");
        inputTag2.attr("name", "field2");
        inputTag2.attr("value", "value2");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);

        Connection connection = formElement.submit();

        // The exact implementation of formData is unknown, but we can verify that the correct method is called and
        // the correct Connection object is prepared with the correct method and URL.
        Assert.assertEquals(Connection.Method.GET, connection.method());
        Assert.assertEquals("http://example.com/submit-form", connection.url());
    }

    @Test
    public void testFormDataMethodReturnsEmptyListWhenFormHasNoControls() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataMethodReturnsListOfKeyValuesForFormWithControls() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();

        Tag inputTag1 = new Tag("input");
        inputTag1.attr("type", "text");
        inputTag1.attr("name", "field1");
        inputTag1.attr("value", "value1");

        Tag inputTag2 = new Tag("input");
        inputTag2.attr("type", "text");
        inputTag2.attr("name", "field2");
        inputTag2.attr("value", "value2");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.addElement(inputTag1);
        formElement.addElement(inputTag2);

        List<Connection.KeyVal> formData = formElement.formData();
        List<Connection.KeyVal> expectedFormData = new ArrayList<>();
        expectedFormData.add(new Connection.KeyVal("field1", "value1"));
        expectedFormData.add(new Connection.KeyVal("field2", "value2"));

        Assert.assertEquals(expectedFormData, formData);
    }

    @Test
    public void testFormEqualsMethodReturnsTrueWhenComparingTwoEqualFormElements() {
        Tag tag1 = new Tag("form");
        String baseUri1 = "http://example.com";
        Attributes attributes1 = new Attributes();

        Tag inputTag1 = new Tag("input");
        inputTag1.attr("type", "text");
        inputTag1.attr("name", "field1");
        inputTag1.attr("value", "value1");

        Tag inputTag2 = new Tag("input");
        inputTag2.attr("type", "text");
        inputTag2.attr("name", "field2");
        inputTag2.attr("value", "value2");

        FormElement formElement1 = new FormElement(tag1, baseUri1, attributes1);
        formElement1.addElement(inputTag1);
        formElement1.addElement(inputTag2);

        Tag tag2 = new Tag("form");
        String baseUri2 = "http://example.com";
        Attributes attributes2 = new Attributes();

        FormElement formElement2 = new FormElement(tag2, baseUri2, attributes2);
        formElement2.addElement(inputTag1);
        formElement2.addElement(inputTag2);

        Assert.assertTrue(formElement1.equals(formElement2));
    }

    @Test
    public void testFormEqualsMethodReturnsFalseWhenComparingTwoUnequalFormElements() {
        Tag tag1 = new Tag("form");
        String baseUri1 = "http://example.com";
        Attributes attributes1 = new Attributes();

        Tag inputTag1 = new Tag("input");
        inputTag1.attr("type", "text");
        inputTag1.attr("name", "field1");
        inputTag1.attr("value", "value1");

        Tag inputTag2 = new Tag("input");
        inputTag2.attr("type", "text");
        inputTag2.attr("name", "field2");
        inputTag2.attr("value", "value2");

        FormElement formElement1 = new FormElement(tag1, baseUri1, attributes1);
        formElement1.addElement(inputTag1);
        formElement1.addElement(inputTag2);

        Tag tag2 = new Tag("form");
        String baseUri2 = "http://example.com";
        Attributes attributes2 = new Attributes();

        Tag inputTag3 = new Tag("input");
        inputTag3.attr("type", "text");
        inputTag3.attr("name", "field1");
        inputTag3.attr("value", "value1");

        Tag inputTag4 = new Tag("input");
        inputTag4.attr("type", "text");
        inputTag4.attr("name", "field2");
        inputTag4.attr("value", "value3");

        FormElement formElement2 = new FormElement(tag2, baseUri2, attributes2);
        formElement2.addElement(inputTag3);
        formElement2.addElement(inputTag4);

        Assert.assertFalse(formElement1.equals(formElement2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodThrowsIllegalArgumentExceptionWhenFormHasNoActionAttribute() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        formElement.submit();
    }

    @Test
    public void testSubmitMethodThrowsIllegalArgumentExceptionWhenFormHasNoNameAttributeForSelectElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();

        Tag selectTag = new Tag("select");
        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);
        formElement.addElement(selectTag);

        Assert.assertThrows(IllegalArgumentException.class, () -> formElement.submit());
    }
}