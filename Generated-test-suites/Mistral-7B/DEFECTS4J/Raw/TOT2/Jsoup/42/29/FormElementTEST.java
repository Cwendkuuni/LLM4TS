import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Elements;

import static org.junit.Assert.assertEquals;

public class FormElementTest {
    @Test
    public void testElements() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "", formAttributes);

        Element inputElement = new Element("input", new Attributes());
        formElement.addElement(inputElement);

        Elements expectedElements = new Elements(inputElement);
        Elements actualElements = formElement.elements();

        assertEquals(expectedElements, actualElements);
    }
}