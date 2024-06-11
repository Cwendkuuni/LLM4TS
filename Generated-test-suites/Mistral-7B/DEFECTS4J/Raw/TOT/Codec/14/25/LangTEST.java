package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishWord() {
 String input = "apple";
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 Languages.LanguageSet expected = Languages.languageSet("en");
 Languages.LanguageSet actual = langInstance.guessLanguages(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testSpanishWord() {
 String input = "manzana";
 Lang langInstance = Lang.instance(Lang.NameType.SPANISH);
 Languages.LanguageSet expected = Languages.languageSet("es");
 Languages.LanguageSet actual = langInstance.guessLanguages(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testMixedLanguageWord() {
 String input = "manzanilla";
 Lang langInstance = Lang.instance(Lang.NameType.SPANISH);
 Languages.LanguageSet expected = Languages.languageSet("es", "en");
 Languages.LanguageSet actual = langInstance.guessLanguages(input);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyString() {
 String input = "";
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 langInstance.guessLanguages(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullString() {
 String input = null;
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 try {
 langInstance.guessLanguages(input);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got: " + e.getMessage());
 }
 }
}