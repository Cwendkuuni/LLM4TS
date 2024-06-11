
import org.junit.Test;
import java.io.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input1 = new StringBuilder("&amp;");
        Writer output1 = new StringWriter();
        unescaper.translate(input1, 0, output1);
        assert output1.toString().equals("&");

        StringBuilder input2 = new StringBuilder("&#65;");
        Writer output2 = new StringWriter();
        unescaper.translate(input2, 0, output2);
        assert output2.toString().equals("A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateSemicolonRequired() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder("&#65");
        Writer output = new StringWriter();
        unescaper.translate(input, 0, output);
    }

    @Test
    public void testTranslateSemicolonOptional() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringBuilder input = new StringBuilder("&#65");
        Writer output = new StringWriter();
        unescaper.translate(input, 0, output);
        assert output.toString().equals("A");
    }

    @Test
    public void testTranslateErrorIfNoSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder input = new StringBuilder("&#65");
        Writer output = new StringWriter();
        unescaper.translate(input, 0, output);
        assert output.toString().equals("A");
    }
}
