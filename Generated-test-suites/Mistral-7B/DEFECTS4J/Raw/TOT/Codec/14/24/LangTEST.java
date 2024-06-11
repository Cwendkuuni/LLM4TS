package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String text = "The quick brown fox jumps over the lazy dog.";
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 Languages.LanguageSet expectedResult = Languages.languageSet("en");
 Languages.LanguageSet actualResult = langInstance.guessLanguages(text);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testGermanLanguage() {
 String text = "Der schnelle braune Fuchs springt über den faulen Hund.";
 Lang langInstance = Lang.instance(Lang.NameType.GERMAN);
 Languages.LanguageSet expectedResult = Languages.languageSet("de");
 Languages.LanguageSet actualResult = langInstance.guessLanguages(text);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSpanishLanguage() {
 String text = "El rapido pato graso salta sobre el perro perezoso.";
 Lang langInstance = Lang.instance(Lang.NameType.SPANISH);
 Languages.LanguageSet expectedResult = Languages.languageSet("es");
 Languages.LanguageSet actualResult = langInstance.guessLanguages(text);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testMixedLanguage() {
 String text = "The quick red fox jumps over the lazy dog.";
 Lang langInstance = Lang.instance(Lang.NameType.GERMAN); // wrong language type
 Languages.LanguageSet expectedResult = Languages.ANY_LANGUAGE;
 Languages.LanguageSet actualResult = langInstance.guessLanguages(text);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedRuleFile() {
 Lang langInstance = new Lang(null, Languages.INSTANCE); // null rules
 assertNull(langInstance);
 }
}