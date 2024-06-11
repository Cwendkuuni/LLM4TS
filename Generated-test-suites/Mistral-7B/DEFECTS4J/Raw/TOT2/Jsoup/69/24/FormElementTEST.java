import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import static org.junit.Assert.assertNotNull;

public class FormElementTest {
   // ...
   @Test
   public void testElements() {
      FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
      assertNotNull(formElement.elements());
   }
   // ...
}