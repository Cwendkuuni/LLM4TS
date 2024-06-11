import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        unescaper.translate("&#65;", 0, new StringWriter(sb));
        Assert.assertEquals("A", sb.toString());
    }
}