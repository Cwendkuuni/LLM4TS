import org.junit.Test;
import org.junit.Assert;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_normalInput() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "hello";
        String expectedOutput = "h*el*l*o";
        
        String actualOutput = translator.translate(input);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testTranslate_nullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = null;
        
        String actualOutput = translator.translate(input);
        
        Assert.assertNull(actualOutput);
    }
    
    @Test
    public void testTranslate_emptyInput() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "";
        String expectedOutput = "";
        
        String actualOutput = translator.translate(input);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testTranslate_withSpecialCharacters() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "@#$%^&*";
        String expectedOutput = "@#$%^&*";
        
        String actualOutput = translator.translate(input);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testTranslate_withUnicodeCharacters() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "こんにちは";
        String expectedOutput = "こんに*ちは";
        
        String actualOutput = translator.translate(input);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testTranslate_withMixedCharacters() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Hello @#$%^&* こんにちは";
        String expectedOutput = "H*e*l*l*o * @#$%^&* * こんに*ちは";
        
        String actualOutput = translator.translate(input);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            
            char[] c = Character.toChars(Character.codePointAt(input, index));
            out.write(c);
            
            return 1;
        }
    }
}