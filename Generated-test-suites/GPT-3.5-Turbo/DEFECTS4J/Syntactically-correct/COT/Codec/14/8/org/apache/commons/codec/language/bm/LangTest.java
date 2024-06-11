import org.junit.Test;

public class LangTest {

  @Test
  public void testGuessLanguage() {
    Lang lang = Lang.instance(NameType.GENERIC);
    String result = lang.guessLanguage("hello");
    assertEquals("eng", result);
  }

  @Test
  public void testGuessLanguages() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet result = lang.guessLanguages("hello");
    assertTrue(result.contains("eng"));
  }

  @Test
  public void testGuessLanguage_emptyString() {
    Lang lang = Lang.instance(NameType.GENERIC);
    String result = lang.guessLanguage("");
    assertEquals("und", result);
  }

  @Test
  public void testGuessLanguages_emptyString() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet result = lang.guessLanguages("");
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGuessLanguage_nullString() {
    Lang lang = Lang.instance(NameType.GENERIC);
    String result = lang.guessLanguage(null);
    assertEquals("und", result);
  }

  @Test
  public void testGuessLanguages_nullString() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet result = lang.guessLanguages(null);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGuessLanguage_invalidString() {
    Lang lang = Lang.instance(NameType.GENERIC);
    String result = lang.guessLanguage("234567");
    assertEquals("und", result);
  }

  @Test
  public void testGuessLanguages_invalidString() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet result = lang.guessLanguages("234567");
    assertTrue(result.isEmpty());
  }

  @Test
  public void testGuessLanguage_edgeCases() {
    Lang lang = Lang.instance(NameType.GENERIC);
    String result = lang.guessLanguage("ab");
    assertEquals("und", result);

    result = lang.guessLanguage("a");
    assertEquals("und", result);

    result = lang.guessLanguage("abcdefghijklmnopqrstuvwxyz");
    assertEquals("und", result);
  }

  @Test
  public void testGuessLanguages_edgeCases() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet result = lang.guessLanguages("ab");
    assertTrue(result.isEmpty());

    result = lang.guessLanguages("a");
    assertTrue(result.isEmpty());

    result = lang.guessLanguages("abcdefghijklmnopqrstuvwxyz");
    assertTrue(result.isEmpty());
  }

}