import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}});
        StringBuffer buffer = new StringBuffer();
        try {
            int length = translator.translate("foo", 0, buffer);
            assert(length == 3);
            assert(buffer.toString().equals("bar"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}