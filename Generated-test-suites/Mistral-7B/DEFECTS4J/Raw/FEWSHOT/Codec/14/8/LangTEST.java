package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.DETECT);
        assertNotNull(lang);
    }

    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.ENGLISH);
        assertNotNull(lang);
    }

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.DETECT);
        String word = "hello";
        String language = lang.guessLanguage(word);
        assertEquals(Languages.ENGLISH.getLanguageName(), language);

        word = "hola";
        language = lang.guessLanguage(word);
        assertEquals(Languages.SPANISH.getLanguageName(), language);

        word = "nihongo";
        language = lang.guessLanguage(word);
        assertEquals(Languages.JAPANESE.getLanguageName(), language);

        word = "bonjour";
        language = lang.guessLanguage(word);
        assertEquals(Languages.FRENCH.getLanguageName(), language);

        word = "salut";
        language = lang.guessLanguage(word);
        assertEquals(Languages.FRENCH.getLanguageName(), language);

        word = "hallo";
        language = lang.guessLanguage(word);
        assertEquals(Languages.GERMAN.getLanguageName(), language);

        word = "czesc";
        language = lang.guessLanguage(word);
        assertEquals(Languages.POLISH.getLanguageName(), language);

        word = "ola";
        language = lang.guessLanguage(word);
        assertEquals(Languages.PORTUGUESE.getLanguageName(), language);

        word = "cześć";
        language = lang.guessLanguage(word);
        assertEquals(Languages.POLISH.getLanguageName(), language);

        word = "안녕하세요";
        language = lang.guessLanguage(word);
        assertEquals(Languages.KOREAN.getLanguageName(), language);

        word = "你好";
        language = lang.guessLanguage(word);
        assertEquals(Languages.SIMPLIFIED_CHINESE.getLanguageName(), language);

        word = "你好，世界";
        language = lang.guessLanguage(word);
        assertEquals(Languages.SIMPLIFIED_CHINESE.getLanguageName(), language);

        word = "こんにちは";
        language = lang.guessLanguage(word);
        assertEquals(Languages.JAPANESE.getLanguageName(), language);

        word = "こんにちは、世界";
        language = lang.guessLanguage(word);
        assertEquals(Languages.JAPANESE.getLanguageName(), language);

        word = "hello world";
        language = lang.guessLanguage(word);
        assertEquals(Languages.ANY.getLanguageName(), language);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.DETECT);
        String word = "hello";
        Languages.LanguageSet languages = lang.guessLanguages(word);
        assertEquals(Languages.ENGLISH, languages.getAny());
        assertEquals(Arrays.asList(Languages.ENGLISH, Languages.IRISH), languages.getLanguages());

        word = "hola";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.SPANISH, languages.getAny());
        assertEquals(Arrays.asList(Languages.SPANISH, Languages.CASTILIAN), languages.getLanguages());

        word = "nihongo";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.JAPANESE, languages.getAny());
        assertEquals(Arrays.asList(Languages.JAPANESE), languages.getLanguages());

        word = "bonjour";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.FRENCH, languages.getAny());
        assertEquals(Arrays.asList(Languages.FRENCH, Languages.FRANCAIS), languages.getLanguages());

        word = "salut";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.FRENCH, languages.getAny());
        assertEquals(Arrays.asList(Languages.FRENCH, Languages.FRANCAIS), languages.getLanguages());

        word = "hallo";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.GERMAN, languages.getAny());
        assertEquals(Arrays.asList(Languages.GERMAN, Languages.GERMANY), languages.getLanguages());

        word = "czesc";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.POLISH, languages.getAny());
        assertEquals(Arrays.asList(Languages.POLISH, Languages.POLONIA), languages.getLanguages());

        word = "ola";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.PORTUGUESE, languages.getAny());
        assertEquals(Arrays.asList(Languages.PORTUGUESE, Languages.PORTUGAL), languages.getLanguages());

        word = "część";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.POLISH, languages.getAny());
        assertEquals(Arrays.asList(Languages.POLISH, Languages.POLONIA), languages.getLanguages());

        word = "안녕하세요";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.KOREAN, languages.getAny());
        assertEquals(Arrays.asList(Languages.KOREAN), languages.getLanguages());

        word = "你好";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.SIMPLIFIED_CHINESE, languages.getAny());
        assertEquals(Arrays.asList(Languages.SIMPLIFIED_CHINESE, Languages.MANDARIN), languages.getLanguages());

        word = "你好，世界";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.SIMPLIFIED_CHINESE, languages.getAny());
        assertEquals(Arrays.asList(Languages.SIMPLIFIED_CHINESE, Languages.MANDARIN), languages.getLanguages());

        word = "こんにちは";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.JAPANESE, languages.getAny());
        assertEquals(Arrays.asList(Languages.JAPANESE), languages.getLanguages());

        word = "こんにちは、世界";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.JAPANESE, languages.getAny());
        assertEquals(Arrays.asList(Languages.JAPANESE), languages.getLanguages());

        word = "hello world";
        languages = lang.guessLanguages(word);
        assertEquals(Languages.ANY, languages.getAny());
        assertNull(languages.getLanguages());
    }
}