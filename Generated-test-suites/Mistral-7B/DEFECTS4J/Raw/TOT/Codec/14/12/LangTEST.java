package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String input = "The quick brown fox jumps over the lazy dog";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet guessedLanguageSet = langInstance.guessLanguages(input);
 assertEquals(Languages.ENGLISH, guessedLanguageSet.getAny());
 }

 @Test
 public void testNonEnglishLanguage() {
 String input = "这个词是中文";
 Lang langInstance = Lang.instance(NameType.SIMPLIFIED_CHINESE);
 Languages.LanguageSet guessedLanguageSet = langInstance.guessLanguages(input);
 assertEquals(Languages.SIMPLIFIED_CHINESE, guessedLanguageSet.getAny());
 }

 @Test
 public void testMixedLanguage() {
 String input = "The quick 很 brown fox 跳过 the 懒 dog";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet guessedLanguageSet = langInstance.guessLanguages(input);
 assertFalse(guessedLanguageSet.isSingleton());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedResource() {
 Lang langInstance = new Lang(Collections.emptyList(null), Languages.getInstance(NameType.ENGLISH));
 assertTrue(langInstance.guessLanguages("test").isEmpty());
 }
}