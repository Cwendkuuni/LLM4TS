import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = new CharSequence[][] {{new String("key1"), new String("value1")}, {new String("key2"), new String("value2")}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.lookupMap.size());
        assertNotNull(translator.lookupMap.get("key1"));
        assertNotNull(translator.lookupMap.get("key2"));
    }
}