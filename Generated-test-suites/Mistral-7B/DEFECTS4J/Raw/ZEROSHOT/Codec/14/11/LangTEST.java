import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LangTest {

    private final String input;
    private final String expectedLanguage;

    public LangTest(String input, String expectedLanguage) {
        this.input = input;
        this.expectedLanguage = expectedLanguage;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"hello", "en"},
                {"hola", "es"},
                {"salut", "fr"},
                {"bonjour", "fr"},
                {"guten tag", "de"},
                {"hej", "sv"},
                {"cześć", "pl"},
                {"część", "pl"},
                {"你好", "zh-CN"},
                {"こんにちは", "ja"},
                {"merhaba", "tr"},
                {"привет", "ru"},
                {"halo", "nb"},
                {"ola", "pt"},
                {"hola amigo", "es"},
                {"hola amiga", "es"},
                {"hola amics", "ca"},
                {"hola amigxs", "ca"},
                {"hola amigas y amigos", "es"},
                {"hola amigos y amigas", "es"},
                {"hola amigos y amigas y amigos", "es"}
        });
    }

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.DEFAULT);
        String language = lang.guessLanguage(input);
        assertEquals(expectedLanguage, language);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.DEFAULT);
        Set<String> languages = lang.guessLanguages(input);
        assertTrue(languages.contains(expectedLanguage));
    }

    @Test
    public void testLangRule() {
        List<LangRule> rules = new ArrayList<>();
        rules.add(new LangRule(Pattern.compile("^hola\\s+"), new HashSet<>(Arrays.asList("es")), true));
        rules.add(new LangRule(Pattern.compile("^bonjour\\s+"), new HashSet<>(Arrays.asList("fr")), true));
        rules.add(new LangRule(Pattern.compile("^guten\\s+tag"), new HashSet<>(Arrays.asList("de")), true));
        rules.add(new LangRule(Pattern.compile("^ola"), new HashSet<>(Arrays.asList("pt")), true));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.DEFAULT));
        Set<String> languages = lang.guessLanguages(input);
        assertTrue(languages.contains(expectedLanguage));
    }
}