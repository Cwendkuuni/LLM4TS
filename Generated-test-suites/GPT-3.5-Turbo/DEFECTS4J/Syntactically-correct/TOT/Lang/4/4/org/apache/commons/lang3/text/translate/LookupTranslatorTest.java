import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_typicalUseCase() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new String[][] {{"cat", "chat"}, {"dog", "chien"}, {"flower", "fleur"}}
        );
        StringBuffer result = new StringBuffer();
        int length = translator.translate("cat and dog and flower", 5, result);
        assertEquals(3, length);
        assertEquals("chat", result.toString());

        result = new StringBuffer();
        length = translator.translate("cat and dog and flower", 10, result);
        assertEquals(4, length);
        assertEquals("chien", result.toString());

        result = new StringBuffer();
        length = translator.translate("cat and dog and flower", 15, result);
        assertEquals(6, length);
        assertEquals("fleur", result.toString());
    }

    @Test
    public void testTranslate_edgeCase() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new String[][] {{"book", "livre"}, {"apple", "pomme"}}
        );
        StringBuffer result = new StringBuffer();
        int length = translator.translate("I love reading a book", 4, result);
        assertEquals(4, length);
        assertEquals("livre", result.toString());

        result = new StringBuffer();
        length = translator.translate("I enjoy eating an apple", 15, result);
        assertEquals(5, length);
        assertEquals("pomme", result.toString());
    }

    @Test
    public void testTranslate_errorScenario() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new String[][] {{"good", "bueno"}, {"bad", "malo"}}
        );
        StringBuffer result = new StringBuffer();
        int length = translator.translate("This is not good!", 14, result);
        assertEquals(4, length);
        assertEquals("bueno", result.toString());

        result = new StringBuffer();
        length = translator.translate("This is bad news", 8, result);
        assertEquals(3, length);
        assertEquals("malo", result.toString());

        result = new StringBuffer();
        length = translator.translate("No matching translation", 0, result);
        assertEquals(0, length);
        assertEquals("", result.toString());
    }
}