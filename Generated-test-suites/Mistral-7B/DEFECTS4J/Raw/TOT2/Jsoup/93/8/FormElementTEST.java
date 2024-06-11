import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.ParseSetup;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElements() {
        ParseSetup setup = new ParseSetup();
        Document doc = setup.parse("<html><body><form><input type='text' name='name' /><input type='text' name='age' /></form></body></html>");
        FormElement form = (FormElement) doc.select("form").first();
        Elements expectedElements = doc.select("input");

        Elements actualElements = form.elements();

        Assert.assertEquals(expectedElements.size(), actualElements.size());
    }
}