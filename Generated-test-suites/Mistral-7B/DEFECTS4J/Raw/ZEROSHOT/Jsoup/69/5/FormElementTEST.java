import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FormElementTest {

    private static final String BASE_URI = "http://example.com";
    private static final String FORM_HTML = "<form action='/submit' method='post'>\n" +
            "<input type='text' name='name'>\n" +
            "<select name='select'>\n" +
            "<option value='option1'>Option 1</option>\n" +
            "<option value='option2' selected>Option 2</option>\n" +
            "</select>\n" +
            "<input type='checkbox' name='checkbox' checked>\n" +
            "<input type='radio' name='radio' checked>\n" +
            "</form>";

    private Element formElement;

    @Before
    public void setUp() {
        Document document = Jsoup.parse(FOR_HTML);
        formElement = document.selectFirst("form");
        Validate.notNull(formElement, "Form element was null.");
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.size() > 0);
    }

    @Test
    public void testAddElement() {
        Element input = new Parser().parseBodyFragment("<input type='text' name='input'/>").selectFirst("input");
        FormElement formElementWithInput = formElement.addElement(input);
        Elements elements = formElementWithInput.elements();
        assertTrue(elements.size() > formElement.elements().size());
        assertTrue(elements.contains(input));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.size() > 0);
        for (Connection.KeyVal keyVal : formData) {
            System.out.println("Key: " + keyVal.key() + ", Value: " + keyVal.value());
        }
    }
}