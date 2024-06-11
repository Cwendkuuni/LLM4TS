
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

public class LookupTranslatorTest {
    @Test
    public void testTranslate() throws IOException {
        CharSequence[][] lookup = {{"abc", "123"}, {"def", "456"}, {"ghi", "789"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        int result = translator.translate("abc", 0, out);
        Assert.assertEquals(3, result);
        Assert.assertEquals("123", out.toString());
    }
    
}
