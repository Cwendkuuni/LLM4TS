import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * JUnit 4 test file for the Lang class.
 */
public class LangTest {

    /**
     * Test the {@link Lang#guessLanguage(String)} method.
     */
    @Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        Assert.assertEquals(NameType.ENGLISH.getLanguage(), englishLang.guessLanguage("hello"));
        Assert.assertEquals(NameType.ENGLISH.getLanguage(), englishLang.guessLanguage("world"));
        Assert.assertEquals(NameType.ENGLISH.getLanguage(), englishLang.guessLanguage("goodbye"));
        Assert.assertEquals(NameType.ENGLISH.getLanguage(), englishLang.guessLanguage("test"));
        Assert.assertEquals(NameType.ENGLISH.getLanguage(), englishLang.guessLanguage("Java"));

        Assert.assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("hola"));
        Assert.assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("bonjour"));
        Assert.assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("guten tag"));
        Assert.assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("salut"));
        Assert.assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("hallo"));
    }

    /**
     * Test the {@link Lang#guessLanguages(String)} method.
     */
    @Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final List<String> expected = Arrays.asList(NameType.ENGLISH.getLanguage(), NameType.GERMAN.getLanguage(), NameType.FRENCH.getLanguage(), NameType.SPANISH.getLanguage());
        final Set<String> languages = englishLang.guessLanguages("hello world guten tag bonjour hola");
        Assert.assertEquals(expected, languages.getLanguages());

        final List<String> empty = Collections.emptyList();
        final Set<String> emptySet = Languages.LanguageSet.emptySet();
        final Set<String> noLangs = englishLang.guessLanguages("this word has no language");
        Assert.assertEquals(emptySet, noLangs);
        Assert.assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("this word has no language"));
    }

    /**
     * Test loading a Lang instance from a resource using the {@link Lang#loadFromResource(String, Languages)} method.
     */
    @Test
    public void testLoadFromResource() throws IOException {
        final Languages languages = Languages.instance();
        final Lang frenchLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_fr.txt", languages);

        Assert.assertNotNull(frenchLang);

        final List<String> expected = Arrays.asList(NameType.FRENCH.getLanguage(), NameType.SPANISH.getLanguage(), NameType.ITALIAN.getLanguage(), NameType.PORTUGUESE.getLanguage());
        final Set<String> languages2 = frenchLang.guessLanguages("bonjour hola hola bienvenido");
        Assert.assertEquals(expected, languages2.getLanguages());

        final List<String> empty = Collections.emptyList();
        final Set<String> emptySet = Languages.LanguageSet.emptySet();
        final Set<String> noLangs = frenchLang.guessLanguages("this word has no language");
        Assert.assertEquals(emptySet, noLangs);
        Assert.assertEquals(Lang.Languages.ANY, frenchLang.guessLanguage("this word has no language"));
    }
}