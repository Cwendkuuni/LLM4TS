import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_HexadecimalNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        
        try {
            unescaper.translate("&#x41;", 0, new WriterHelper(sb));
            assertEquals("A", sb.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testTranslate_DecimalNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        
        try {
            unescaper.translate("&#65;", 0, new WriterHelper(sb));
            assertEquals("A", sb.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testTranslate_InvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        
        try {
            unescaper.translate("&#notanumber;", 0, new WriterHelper(sb));
            assertEquals("", sb.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Add more test cases for other methods if needed
    
    private static class WriterHelper extends Writer {
        
        private StringBuilder sb;

        public WriterHelper(StringBuilder sb) {
            this.sb = sb;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            sb.append(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException { }

        @Override
        public void close() throws IOException { }
    }
}