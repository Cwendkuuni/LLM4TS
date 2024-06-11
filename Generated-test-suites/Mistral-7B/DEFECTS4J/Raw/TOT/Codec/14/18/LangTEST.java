package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String input = "hello world";
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet guessedLang = englishLang.guessLanguages(input);
 assertEquals(Languages.ENGLISH, guessedLang.getAny());
 }

 @Test
 public void testGermanLanguage() {
 String input = "guten tag";
 Lang germanLang = Lang.instance(NameType.GERMAN);
 Languages.LanguageSet guessedLang = germanLang.guessLanguages(input);
 assertEquals(Languages.GERMAN, guessedLang.getAny());
 }

 @Test
 public void testMixedLanguage() {
 String input = "hello guten tag";
 Lang englishGermanLang = Lang.instance(NameType.ENGLISH_GERMAN);
 Languages.LanguageSet guessedLang = englishGermanLang.guessLanguages(input);
 assertEquals(Languages.ENGLISH, guessedLang.getFirst());
 assertEquals(Languages.GERMAN, guessedLang.getSecond());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRuleFormat() {
 Lang invalidRuleLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/invalidRuleLang.txt", Languages.getInstance(NameType.ENGLISH));
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testEmptyRuleFile() {
 Lang emptyRuleLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/emptyRuleLang.txt", Languages.getInstance(NameType.ENGLISH));
 assertEquals(0, emptyRuleLang.rules.size());
 }

}