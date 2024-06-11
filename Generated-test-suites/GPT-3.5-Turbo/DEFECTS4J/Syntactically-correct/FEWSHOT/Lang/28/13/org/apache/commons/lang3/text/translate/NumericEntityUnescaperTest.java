import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @After
    public void tearDown() {
        unescaper = null;
    }

    @Test
    public void testTranslate() {
        StringBuilder builder = new StringBuilder();
        String input = "Test input &amp;&#35xfff;";
        int result = unescaper.translate(input, 0, new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                for (int i = off; i < off + len; i++) {
                    builder.append(cbuf[i]);
                }
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        });
        String expected = "Test input &";
        assertEquals(expected, builder.toString());
        assertEquals(2, result);
    }
}