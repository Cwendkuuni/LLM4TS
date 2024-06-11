package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;
import org.junit.Test;

public class LangTest {

  @Test
  public void testGuessLanguage() {
    Lang lang = Lang.instance(NameType.GENERIC);

    // Test basic functionality
    assertEquals("eng", lang.guessLanguage("hello"));
    assertEquals("spa", lang.guessLanguage("hola"));
    assertEquals("fra", lang.guessLanguage("bonjour"));
    assertEquals("deu", lang.guessLanguage("hallo"));
    assertEquals("ita", lang.guessLanguage("ciao"));

    // Test edge cases
    assertEquals("spa", lang.guessLanguage("amigo")); // Only matches with Spanish, not Portuguese
    assertEquals("eng", lang.guessLanguage("good")); // Only matches with English, not Dutch
  }

  @Test
  public void testGuessLanguages() {
    Lang lang = Lang.instance(NameType.GENERIC);

    // Test basic functionality
    Languages.LanguageSet result1 = lang.guessLanguages("love");
    assertTrue(result1.contains("eng"));
    assertTrue(result1.contains("ind"));

    Languages.LanguageSet result2 = lang.guessLanguages("kucing");
    assertTrue(result2.contains("ind"));
    assertTrue(result2.contains("msa"));
    assertFalse(result2.contains("eng"));
  }

}
