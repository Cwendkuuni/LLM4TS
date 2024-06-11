import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals; // for assertEquals

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = new CharSequence[][]{{"Key1", "Value1"}, {"Key2", "Value2"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.lookupMap.size());
    }
}