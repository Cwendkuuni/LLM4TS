
import org.jsoup.nodes.FormElement;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Connection;

import java.util.List;
import org.jsoup.select.Elements;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import java.util.ArrayList;


public class FormElementTest {

    private FormElement formElement;
    private Element testElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);

        testElement = new Element(Tag.valueOf("input"), "https://example.com", new Attributes());
        testElement.attr("name", "testName");
        testElement.attr("value", "testValue");
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assert(elements != null);
    }

    @Test
    public void testAddElement() {
        FormElement fe = formElement.addElement(testElement);
        assert(fe == formElement);
        assert(formElement.elements().size() == 1);
    }
    
    @Test
    public void testRemoveChild() {
        formElement.addElement(testElement);
        formElement.removeChild(testElement);
        assert(formElement.elements().size() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit() {
        Connection conn = formElement.submit();
    }

    @Test
    public void testFormData() {
        formElement.addElement(testElement);
        List<Connection.KeyVal> data = formElement.formData();
        assert(data.size() == 1);
        assert(data.get(0).key().equals("testName"));
        assert(data.get(0).value().equals("testValue"));
    }

}
