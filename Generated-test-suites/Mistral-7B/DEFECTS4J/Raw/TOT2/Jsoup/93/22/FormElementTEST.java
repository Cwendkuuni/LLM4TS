import static org.junit.Assert.assertNotNull;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Test;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertNotNull(formElement);
    }
}