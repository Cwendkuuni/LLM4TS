import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {
    @Test
    public void testElementsMethod() {
        Document doc = Jsoup.parse("<form><input type='text' name='name' /></form>");
        FormElement formElement = doc.select("form").first();
        Elements elements = formElement.elements();
        assertNotNull(elements);
    }
}