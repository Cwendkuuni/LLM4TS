import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int consumed = unescaper.translate("&#65;", 0, writer);

        assert consumed == 6;  // 6 characters consumed (including "&#65;") from the input CharSequence
        assert writer.toString().equals("A");  // Output should be "A"
    }
}