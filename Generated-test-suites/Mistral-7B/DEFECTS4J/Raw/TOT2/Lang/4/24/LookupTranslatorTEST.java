import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = new CharSequence[][]{{"Key1", "Value1"}, {"Key2", "Value2"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.lookupMap.size());
        assertEquals("Value1", translator.lookupMap.get("Key1"));
        assertEquals("Value2", translator.lookupMap.get("Key2"));
        assertEquals(2, translator.shortest);
        assertEquals(2, translator. longest);
    }
}