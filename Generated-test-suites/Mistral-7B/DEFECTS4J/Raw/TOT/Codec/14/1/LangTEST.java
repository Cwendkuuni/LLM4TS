package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String input = "apple";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet expectedResult = Languages.LanguageSet.ENGLISH;
 Languages.LanguageSet actualResult = langInstance.guessLanguages(input);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSpanishLanguage() {
 String input = "manzana";
 Lang langInstance = Lang.instance(NameType.SPANISH);
 Languages.LanguageSet expectedResult = Languages.LanguageSet.SPANISH;
 Languages.LanguageSet actualResult = langInstance.guessLanguages(input);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testMixedLanguageInput() {
 String input = "manzana apple";
 Lang langInstance = Lang.instance(NameType.SPANISH);
 Languages.LanguageSet expectedResult = Languages.LanguageSet.SPANISH; // Spanish language should be guessed for "manzana"
 Languages.LanguageSet actualResult = langInstance.guessLanguages(input);

 // Check if English language is not present in the result
 assertTrue(actualResult.remove(Languages.Language.ENGLISH));
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String input = null;
 langInstance.guessLanguages(input);
 }

 @Test
 public void testEmptyInput() {
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String input = "";
 Languages.LanguageSet expectedResult = Languages.ANY;
 Languages.LanguageSet actualResult = langInstance.guessLanguages(input);
 assertEquals(expectedResult, actualResult);
 }
}