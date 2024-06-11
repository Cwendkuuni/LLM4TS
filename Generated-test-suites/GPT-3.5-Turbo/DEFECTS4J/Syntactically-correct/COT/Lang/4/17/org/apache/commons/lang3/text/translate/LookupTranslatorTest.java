
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"one", "1"}, {"two", "2"}, {"three", "3"}});
        
        assertEquals(2, translator.translate("onetwothree", 0, new StringBuilder()));
        assertEquals(0, translator.translate("four", 0, new StringBuilder()));
    }
    
    @Test
    public void testConstructor() throws Exception {
        LookupTranslator translator = new LookupTranslator();
        
        assertNotNull(translator);
    }
}
