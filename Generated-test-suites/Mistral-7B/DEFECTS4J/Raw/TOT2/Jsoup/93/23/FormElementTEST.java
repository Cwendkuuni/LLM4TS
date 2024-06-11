import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {

    @Test
    public void testElements() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }
}