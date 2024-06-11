package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class LangTest {

 private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";

 @Test
 public void testInstance() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);
 assertNotNull(langEnglish);
 }

 @Test
 public void testLoadFromResource() {
 Languages english = Languages.getInstance(NameType.ENGLISH);
 Lang langEnglish = Lang.loadFromResource(LANGUAGE_RULES_RN, english);
 assertNotNull(langEnglish);
 }

 @Test
 public void testGuessLanguage() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);

 assertEquals(NameType.ENGLISH.getLanguageName(), langEnglish.guessLanguage("hello"));
 assertEquals(NameType.ENGLISH.getLanguageName(), langEnglish.guessLanguage("world"));
 assertEquals(NameType.ENGLISH.getLanguageName(), langEnglish.guessLanguage("goodbye"));

 assertEquals(NameType.ENGLISH.getLanguageName(), langEnglish.guessLanguage("hello world"));
 assertEquals(NameType.ENGLISH.getLanguageName(), langEnglish.guessLanguage("hello, world"));

 assertEquals(NameType.ENGLISH.getLanguageName(), langEnglish.guessLanguage("the quick brown fox jumps over the lazy dog"));

 assertEquals(NameType.ENGLISH.getLanguageName(), langEnglish.guessLanguage("JavasScript is a programming language"));

 assertEquals(NameType.ENGLISH.getLanguageName(), langEnglish.guessLanguage("12345"));

 assertEquals(Languages.ANY.getLanguageName(), langEnglish.guessLanguage(""));
 assertEquals(Languages.ANY.getLanguageName(), langEnglish.guessLanguage(" "));
 assertEquals(Languages.ANY.getLanguageName(), langEnglish.guessLanguage("  "));
 }

 @Test
 public void testGuessLanguages() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);

 Set<String> expected = new HashSet<String>(Arrays.asList(NameType.ENGLISH.getLanguageName(), NameType.GERMAN.getLanguageName()));
 Set<String> actual = langEnglish.guessLanguages("guten tag");
 assertEquals(expected, actual);

 assertEquals(Collections.singleton(NameType.ENGLISH.getLanguageName()), langEnglish.guessLanguages("hello"));
 }

 @Test
 public void testGuessLanguages_Empty() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);

 Set<String> actual = langEnglish.guessLanguages("");
 assertEquals(Languages.ANY_LANGUAGE, actual);
 }

 @Test
 public void testGuessLanguages_Null() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);

 Set<String> actual = langEnglish.guessLanguages(null);
 assertNull(actual);
 }
}