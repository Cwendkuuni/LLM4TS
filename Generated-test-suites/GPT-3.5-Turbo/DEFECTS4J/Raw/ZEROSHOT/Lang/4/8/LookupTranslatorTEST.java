import org.junit.Assert;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[][] {
                        {"a", "1"},
                        {"b", "2"},
                        {"c", "3"}
                }
        );

        Assert.assertEquals("1", translator.translate("a", 0, null));
        Assert.assertEquals("2", translator.translate("b", 0, null));
        Assert.assertEquals("3", translator.translate("c", 0, null));
        Assert.assertEquals(0, translator.translate("d", 0, null));
    }

    @Test
    public void testTranslate_WithEmptyLookup() throws Exception {
        LookupTranslator translator = new LookupTranslator();

        Assert.assertEquals(0, translator.translate("a", 0, null));
        Assert.assertEquals(0, translator.translate("b", 0, null));
        Assert.assertEquals(0, translator.translate("c", 0, null));
        Assert.assertEquals(0, translator.translate("d", 0, null));
    }

    @Test
    public void testTranslate_WithNullLookup() throws Exception {
        LookupTranslator translator = new LookupTranslator(null);

        Assert.assertEquals(0, translator.translate("a", 0, null));
        Assert.assertEquals(0, translator.translate("b", 0, null));
        Assert.assertEquals(0, translator.translate("c", 0, null));
        Assert.assertEquals(0, translator.translate("d", 0, null));
    }

    @Test
    public void testTranslate_WithInvalidLookup() throws Exception {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[][] {
                        {"a", "1"},
                        {"b", "2"},
                        {"c"}
                }
        );

        Assert.assertEquals("1", translator.translate("a", 0, null));
        Assert.assertEquals("2", translator.translate("b", 0, null));
        Assert.assertEquals(0, translator.translate("c", 0, null));
        Assert.assertEquals(0, translator.translate("d", 0, null));
    }

    @Test
    public void testTranslate_WithLongestMatch() throws Exception {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[][] {
                        {"aa", "1"},
                        {"aaa", "2"},
                        {"aaaa", "3"}
                }
        );

        Assert.assertEquals("1", translator.translate("aa", 0, null));
        Assert.assertEquals("2", translator.translate("aaa", 0, null));
        Assert.assertEquals("3", translator.translate("aaaa", 0, null));
        Assert.assertEquals("2", translator.translate("aaaaa", 0, null));
    }
}