package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerseTest {
    @Test
    public void testNew() {
        Verse verse1 = Verse.New(1);
        assertNotNull(verse1);
        
        Verse verse2 = Verse.New(2);
        assertNotNull(verse2);
        
        assertEquals(verse1.getId(), verse2.getId());
    }
    
    @Test
    public void testGetId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        int verseId1 = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertNotNull(verseId1);
        
        int verseId2 = Verse.GetId(2, 2, 2, 2);
        assertNotNull(verseId2);
        
        assertEquals(verseId1, verseId2);
    }
    
    @Test
    public void testGet() {
        Translation translation1 = new Translation();
        Translation translation2 = new Translation();
        Translation[] translations = {translation1, translation2};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        ArrayList verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
        assertEquals(translations.length, verses.size());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        assertNotNull(verse.toString());
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getText());
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getReference());
    }
}