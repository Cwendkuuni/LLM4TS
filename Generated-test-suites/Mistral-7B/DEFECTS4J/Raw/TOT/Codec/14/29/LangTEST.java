package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String input = "the quick brown fox jumps over the lazy dog";
 Lang lang = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet expected = Languages.LanguageSet.ENGLISH;
 Languages.LanguageSet actual = lang.guessLanguages(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testGermanLanguage() {
 String input = "der schnelle grune Fuchs springt über den faulen Hund";
 Lang lang = Lang.instance(NameType.GERMAN);
 Languages.LanguageSet expected = Languages.LanguageSet.GERMAN;
 Languages.LanguageSet actual = lang.guessLanguages(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testMixedLanguage() {
 String input = "the quick brown fox jumps over the lazy dog, auf dem Hund sitzt der Hund";
 Lang lang = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet expected = Languages.LanguageSet.ENGLISH; // or any language with English as an option
 Languages.LanguageSet actual = lang.guessLanguages(input);
 assertTrue(expected.contains(Languages.ENGLISH));
 assertTrue(expected.contains(Languages.GERMAN));
 assertTrue(actual.containsAllOf(expected));
 }

 @Test
 public void testEmptyString() {
 String input = "";
 Lang lang = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet expected = Languages.NO_LANGUAGES;
 Languages.LanguageSet actual = lang.guessLanguages(input);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedRuleLine() {
 Lang lang = new Lang(Collections.emptyList(new LangRule(Pattern.compile(".*"), null, false)), Languages.ENGLISH);
 String input = "the quick brown fox jumps over the lazy dog";
 lang.guessLanguages(input);
 }
}