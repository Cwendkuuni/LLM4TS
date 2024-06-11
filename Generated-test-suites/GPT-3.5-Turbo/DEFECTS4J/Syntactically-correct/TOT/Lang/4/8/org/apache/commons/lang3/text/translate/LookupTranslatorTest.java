import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {
    
    @Test
    public void translate_validInput_successfullyTranslatesInput() throws IOException {
        CharSequence[] translation = {
                {"123", "OneTwoThree"},
                {"456", "FourFiveSix"},
                {"789", "SevenEightNine"}
        };
        
        LookupTranslator translator = new LookupTranslator(translation);
        StringWriter writer = new StringWriter();
        int result = translator.translate("123456789", 0, writer);
        
        assertEquals("OneTwoThreeFourFiveSixSevenEightNine", writer.toString());
        assertEquals(9, result);
    }
    
    @Test
    public void translate_invalidInput_returnsZero() throws IOException {
        CharSequence[] translation = {
                {"abc", "Alphabet"}
        };
        
        LookupTranslator translator = new LookupTranslator(translation);
        StringWriter writer = new StringWriter();
        int result = translator.translate("xyz", 0, writer);
        
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }
    
    @Test
    public void translate_inputShorterThanShortestSequence_returnsZero() throws IOException {
        CharSequence[] translation = {
                {"1234", "OneTwoThreeFour"}
        };
        
        LookupTranslator translator = new LookupTranslator(translation);
        StringWriter writer = new StringWriter();
        int result = translator.translate("12", 0, writer);
        
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }
    
    @Test
    public void translate_inputLongerThanLongestSequence_successfullyTranslatesInput() throws IOException {
        CharSequence[] translation = {
                {"abc", "Alphabet"},
                {"defg", "D E F G"}
        };
        
        LookupTranslator translator = new LookupTranslator(translation);
        StringWriter writer = new StringWriter();
        int result = translator.translate("abcdefghij", 0, writer);
        
        assertEquals("Alphabetdefghij", writer.toString());
        assertEquals(3, result);
    }

}