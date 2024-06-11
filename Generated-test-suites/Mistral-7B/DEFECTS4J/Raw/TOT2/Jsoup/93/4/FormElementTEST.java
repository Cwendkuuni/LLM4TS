import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {
    // Basic test for elements() method
    @Test
    public void testElements() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Element element1 = new Element("input");
        Element element2 = new Element("input");
        formElement.addElement(element1);
        formElement.addElement(element2);

        Elements expectedElements = new Elements(element1, element2);
        Elements actualElements = formElement.elements();

        assertEquals(expectedElements, actualElements);
    }
}