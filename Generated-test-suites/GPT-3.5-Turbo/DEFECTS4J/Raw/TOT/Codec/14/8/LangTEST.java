import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage_typicalCases() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Typical cases
        assertEquals("en", lang.guessLanguage("hello"));
        assertEquals("es", lang.guessLanguage("hola"));
        assertEquals("de", lang.guessLanguage("guten tag"));
    }
    
    @Test
    public void testGuessLanguage_edgeCases() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Edge cases
        assertEquals("en", lang.guessLanguage("apples"));
        assertEquals("en", lang.guessLanguage("potatoes"));
        assertEquals("en", lang.guessLanguage("flowers"));
        assertEquals("en", lang.guessLanguage("birds"));
        assertEquals("en", lang.guessLanguage("is"));
        
        assertEquals("es", lang.guessLanguage("manzanas"));
        assertEquals("es", lang.guessLanguage("papas"));
        assertEquals("es", lang.guessLanguage("flores"));
        assertEquals("es", lang.guessLanguage("pájaros"));
        assertEquals("es", lang.guessLanguage("huevo"));
        
        assertEquals("de", lang.guessLanguage("äpfel"));
        assertEquals("de", lang.guessLanguage("kartoffeln"));
        assertEquals("de", lang.guessLanguage("blumen"));
        assertEquals("de", lang.guessLanguage("vögel"));
        assertEquals("de", lang.guessLanguage("haus"));
    }
    
    @Test
    public void testGuessLanguage_errorScenarios() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Error scenarios
        assertEquals(Languages.ANY, lang.guessLanguage(""));
        assertEquals(Languages.ANY, lang.guessLanguage(null));
    }
    
    @Test
    public void testGuessLanguages_typicalCases() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Typical cases
        Languages.LanguageSet expectedEn = Languages.LanguageSet.from("en");
        Languages.LanguageSet expectedEs = Languages.LanguageSet.from("es");
        Languages.LanguageSet expectedDe = Languages.LanguageSet.from("de");
        
        assertEquals(expectedEn, lang.guessLanguages("hello"));
        assertEquals(expectedEs, lang.guessLanguages("hola"));
        assertEquals(expectedDe, lang.guessLanguages("guten tag"));
    }
    
    @Test
    public void testGuessLanguages_edgeCases() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Edge cases
        Languages.LanguageSet expectedEn = Languages.LanguageSet.from("en");
        Languages.LanguageSet expectedEs = Languages.LanguageSet.from("es");
        Languages.LanguageSet expectedDe = Languages.LanguageSet.from("de");
        
        assertEquals(expectedEn, lang.guessLanguages("apples"));
        assertEquals(expectedEn, lang.guessLanguages("potatoes"));
        assertEquals(expectedEn, lang.guessLanguages("flowers"));
        assertEquals(expectedEn, lang.guessLanguages("birds"));
        assertEquals(expectedEn, lang.guessLanguages("is"));
        
        assertEquals(expectedEs, lang.guessLanguages("manzanas"));
        assertEquals(expectedEs, lang.guessLanguages("papas"));
        assertEquals(expectedEs, lang.guessLanguages("flores"));
        assertEquals(expectedEs, lang.guessLanguages("pájaros"));
        assertEquals(expectedEs, lang.guessLanguages("huevo"));
        
        assertEquals(expectedDe, lang.guessLanguages("äpfel"));
        assertEquals(expectedDe, lang.guessLanguages("kartoffeln"));
        assertEquals(expectedDe, lang.guessLanguages("blumen"));
        assertEquals(expectedDe, lang.guessLanguages("vögel"));
        assertEquals(expectedDe, lang.guessLanguages("haus"));
    }
    
    @Test
    public void testGuessLanguages_errorScenarios() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Error scenarios
        Languages.LanguageSet expectedAny = Languages.ANY_LANGUAGE;
        
        assertEquals(expectedAny, lang.guessLanguages(""));
        assertEquals(expectedAny, lang.guessLanguages(null));
    }
    
}