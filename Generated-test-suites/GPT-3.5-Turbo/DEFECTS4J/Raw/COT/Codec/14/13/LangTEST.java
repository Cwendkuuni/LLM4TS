import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Basic test case
        String text1 = "hello";
        String expected1 = "eng";
        String result1 = lang.guessLanguage(text1);
        Assert.assertEquals(expected1, result1);
        
        // Empty string
        String text2 = "";
        String expected2 = "und";
        String result2 = lang.guessLanguage(text2);
        Assert.assertEquals(expected2, result2);
        
        // Numbers
        String text3 = "123";
        String expected3 = "und";
        String result3 = lang.guessLanguage(text3);
        Assert.assertEquals(expected3, result3);
        
        // Edge case: Text with multiple possible languages
        String text4 = "book";
        String expected4 = "eng";
        String result4 = lang.guessLanguage(text4);
        Assert.assertEquals(expected4, result4);

        String text5 = "livre";
        String expected5 = "fra";
        String result5 = lang.guessLanguage(text5);
        Assert.assertEquals(expected5, result5);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Basic test case
        String text1 = "hello";
        String[] expectedArray1 = {"eng"};
        Set expected1 = new HashSet<>(Arrays.asList(expectedArray1));
        Set result1 = lang.guessLanguages(text1);
        Assert.assertEquals(expected1, result1);
        
        // Empty string
        String text2 = "";
        String[] expectedArray2 = {"und"};
        Set expected2 = new HashSet<>(Arrays.asList(expectedArray2));
        Set result2 = lang.guessLanguages(text2);
        Assert.assertEquals(expected2, result2);
        
        // Numbers
        String text3 = "123";
        String[] expectedArray3 = {"und"};
        Set expected3 = new HashSet<>(Arrays.asList(expectedArray3));
        Set result3 = lang.guessLanguages(text3);
        Assert.assertEquals(expected3, result3);
        
        // Edge case: Text with multiple possible languages
        String text4 = "book";
        String[] expectedArray4 = {"eng"};
        Set expected4 = new HashSet<>(Arrays.asList(expectedArray4));
        Set result4 = lang.guessLanguages(text4);
        Assert.assertEquals(expected4, result4);

        String text5 = "livre";
        String[] expectedArray5 = {"fra"};
        Set expected5 = new HashSet<>(Arrays.asList(expectedArray5));
        Set result5 = lang.guessLanguages(text5);
        Assert.assertEquals(expected5, result5);
    }
}