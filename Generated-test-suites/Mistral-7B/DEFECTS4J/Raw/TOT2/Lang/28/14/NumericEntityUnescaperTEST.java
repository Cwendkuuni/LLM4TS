import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#160; &#x201C; &#x201D; &#x2713; &#x2714;";
        StringBuilder output = new StringBuilder();
        Writer writer = new StringBuilderWriter(output);

        int charsWritten = unescaper.translate(new CharSequence() {
            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public int length() {
                return input.length();
            }
        }, 0, writer);

        assertEquals("&#160; « » ⧮ ⧯", output.toString());
        assertEquals(16, charsWritten);
    }
}