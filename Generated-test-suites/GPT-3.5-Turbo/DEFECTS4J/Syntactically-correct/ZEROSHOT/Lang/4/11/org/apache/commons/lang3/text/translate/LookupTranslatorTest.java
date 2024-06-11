import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    private LookupTranslator translator;

    @Before
    public void setup() {
        translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"b", "2"}, {"c", "3"}});
    }

    @Test
    public void testTranslate() throws Exception {
        assertEquals(1, translator.translate("a", 0, null));
        assertEquals(1, translator.translate("a", 0, null));
        assertEquals(1, translator.translate("a", 0, null));
        assertEquals(0, translator.translate("aa", 0, null));
        assertEquals(0, translator.translate("aaa", 0, null));
        assertEquals(0, translator.translate("aaaa", 0, null));
    }

    @Test
    public void testTranslate_invalidIndex() throws Exception {
        assertEquals(0, translator.translate("a", 1, null));
        assertEquals(0, translator.translate("b", 2, null));
        assertEquals(0, translator.translate("c", 3, null));
        assertEquals(0, translator.translate("aa", 3, null));
        assertEquals(0, translator.translate("aaa", 4, null));
        assertEquals(0, translator.translate("aaaa", 5, null));
    }

    @Test
    public void testTranslate_writerNotNull() throws Exception {
        StringBuilder writer = new StringBuilder();
        translator.translate("a", 0, writer);
        assertEquals("1", writer.toString());

        writer = new StringBuilder();
        translator.translate("b", 0, writer);
        assertEquals("2", writer.toString());

        writer = new StringBuilder();
        translator.translate("c", 0, writer);
        assertEquals("3", writer.toString());
    }

    @Test
    public void testTranslate_writerNull() throws Exception {
        assertEquals(1, translator.translate("a", 0, null));
        assertEquals(1, translator.translate("b", 0, null));
        assertEquals(1, translator.translate("c", 0, null));
    }

}