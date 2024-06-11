import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.ResourceConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@RunWith(JUnit4.class)
public class LangTest {

    private static Lang lang;

    @BeforeClass
    public static void init() throws IOException {
        lang = Lang.instance(NameType.DEFAULT);
    }

    @Test
    public void testInstance() {
        Assert.assertNotNull(lang);
    }

    @Test
    public void testGuessLanguage_English_Word() {
        String text = "hello";
        String expected = Languages.ENGLISH.getName();
        String actual = lang.guessLanguage(text);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGuessLanguage_English_Empty() {
        String text = "";
        String actual = lang.guessLanguage(text);
        Assert.assertEquals(Languages.ANY, actual);
    }

    @Test
    public void testGuessLanguage_English_SpecialChars() {
        String text = "!@#$%^&*()_+-={[}]|\\:\"<>,.?/";
        String actual = lang.guessLanguage(text);
        Assert.assertEquals(Languages.ANY, actual);
    }

    @Test
    public void testGuessLanguage_English_Mixed() {
        String text = "hello!world@12345";
        String expected = Languages.ENGLISH.getName();
        String actual = lang.guessLanguage(text);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGuessLanguage_Null() {
        String text = null;
        lang.guessLanguage(text);
    }

    @Test
    public void testGuessLanguages_English_Word() {
        String text = "hello";
        List<String> expected = Arrays.asList(Languages.ENGLISH.getName());
        List<String> actual = lang.guessLanguages(text).getLanguages();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGuessLanguages_English_Empty() {
        String text = "";
        List<String> actual = lang.guessLanguages(text).getLanguages();
        Assert.assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testGuessLanguages_English_SpecialChars() {
        String text = "!@#$%^&*()_+-={[}]|\\:\"<>,.?/";
        List<String> actual = lang.guessLanguages(text).getLanguages();
        Assert.assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testGuessLanguages_English_Mixed() {
        String text = "hello!world@12345";
        List<String> expected = Arrays.asList(Languages.ENGLISH.getName());
        List<String> actual = lang.guessLanguages(text).getLanguages();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGuessLanguages_Multiple() {
        String text = "hola";
        Set<String> expected = new HashSet<>(Arrays.asList(Languages.SPANISH.getName(), Languages.CATALAN.getName()));
        Set<String> actual = lang.guessLanguages(text).getLanguages();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGuessLanguages_Null() {
        String text = null;
        lang.guessLanguages(text);
    }
}