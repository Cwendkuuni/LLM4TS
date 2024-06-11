import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        //typical use case
        assertEquals("eng", lang.guessLanguage("hello"));
        
        //edge case
        assertEquals("eng", lang.guessLanguage(""));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        //typical use case
        Languages.LanguageSet expected = Languages.LanguageSet.from("eng");
        assertEquals(expected, lang.guessLanguages("hello"));
        
        //edge case
        expected = Languages.ANY_LANGUAGE;
        assertEquals(expected, lang.guessLanguages(""));
    }

    @Test
    public void testMatches_withAcceptOnMatch() {
        Pattern pattern = Pattern.compile("hel*o");
        Set<String> languages = new HashSet<>(Arrays.asList("eng", "spa"));

        //typical use case
        Lang.LangRule rule = new Lang.LangRule(pattern, languages, true);
        assertTrue(rule.matches("hello"));
        
        //edge case
        assertFalse(rule.matches(""));
    }

    @Test
    public void testMatches_withoutAcceptOnMatch() {
        Pattern pattern = Pattern.compile("hel*o");
        Set<String> languages = new HashSet<>(Arrays.asList("eng", "spa"));

        //typical use case
        Lang.LangRule rule = new Lang.LangRule(pattern, languages, false);
        assertFalse(rule.matches("hello"));
        
        //edge case
        assertTrue(rule.matches(""));
    }

    @Test
    public void testLoadFromResource() {
        Languages languages = Languages.getInstance(NameType.GENERIC);
        
         //typical use case
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", languages);
        assertNotNull(lang);
        
        //error scenario
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Lang.loadFromResource("invalid_resource_name.txt", languages);
        });
        assertEquals("Unable to resolve required resource: org/apache/commons/codec/language/bm/lang.txt", exception.getMessage());
    }

    @Test
    public void testInstance() {
        //typical use case
        Lang lang = Lang.instance(NameType.GENERIC);
        assertNotNull(lang);
    }

}