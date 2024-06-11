import org.junit.Test;
import org.junit.Assert;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder("&#65;");
        StringBuilder result = new StringBuilder();

        unescaper.translate(sb, 0, new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                result.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
                // Not used in the test case
            }

            @Override
            public void close() throws IOException {
                // Not used in the test case
            }
        });

        Assert.assertEquals("A", result.toString());
    }
}