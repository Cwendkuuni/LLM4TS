import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class LangTest {

    private Lang lang;
    private String input;
    private Languages.LanguageSet expectedLanguages;

    public LangTest(Lang lang, String input, Languages.LanguageSet expectedLanguages) {
        this.lang = lang;
        this.input = input;
        this.expectedLanguages = expectedLanguages;
    }

    @Before
    public void setUp() {
        // Initialize Lang with specific rules
        lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.MIXED));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {new Lang(), "hello", Languages.NO_LANGUAGES},
                {new Lang(), "world", Languages.NO_LANGUAGES},
                {new Lang(), "hola", Languages.NO_LANGUAGES},
                {new Lang(), "amigo", Languages.NO_LANGUAGES},
                {new Lang(), "bonjour", Languages.NO_LANGUAGES},
                {new Lang(), "monde", Languages.NO_LANGUAGES},
                {new Lang(), "こんにちは", Languages.NO_LANGUAGES},
                {new Lang(), "世界", Languages.NO_LANGUAGES},
                {new Lang(), "你好", Languages.NO_LANGUAGES},
                {new Lang(), "世界", Languages.NO_LANGUAGES},

                {new Lang(), "", Languages.NO_LANGUAGES}, // edge case: empty string
                {new Lang(), "  ", Languages.NO_LANGUAGES}, // edge case: whitespace only string
                {new Lang(), null, Languages.NO_LANGUAGES}, // error scenario: null input
                {new Lang(), "!@#$%", Languages.NO_LANGUAGES}, // error scenario: special characters only
        });
    }

    @Test
    public void testGuessLanguages() {
        assertEquals(expectedLanguages, lang.guessLanguages(input));
    }

    @Test
    public void testGuessLanguage() {
        String expectedLanguage = expectedLanguages.isSingleton() ? expectedLanguages.getAny() : Languages.ANY;
        assertEquals(expectedLanguage, lang.guessLanguage(input));
    }
}