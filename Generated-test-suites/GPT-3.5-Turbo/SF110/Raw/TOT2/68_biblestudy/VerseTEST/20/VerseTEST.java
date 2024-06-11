package bible.obj;

import org.junit.Test;
import static org.junit.Assert.*;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = {new Translation(1), new Translation(2)};
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertFalse(verses.isEmpty());
    }
    
    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = new TranslationReference(new Translation(1), new Reference(1, 1, 1));
        Verse verse = new Verse(1);
        assertEquals(transRef, verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        Reference reference = new Reference(1, 1, 1);
        TranslationReference transRef = new TranslationReference(new Translation(1), reference);
        Verse verse = new Verse(1);
        assertEquals(reference, verse.getReference());
    }
    
    @Test
    public void testGetIdWithInvalidInput() {
        int id = Verse.GetId(-1, -1, -1, -1);
        assertEquals(-1, id);
    }
    
    @Test
    public void testGetWithNullTranslations() {
        ArrayList<Verse> verses = Verse.Get(null, 1, 1, 1);
        assertNotNull(verses);
        assertTrue(verses.isEmpty());
    }
    
    @Test
    public void testGetWithEmptyTranslations() {
        Translation[] translations = {};
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertTrue(verses.isEmpty());
    }
    
}