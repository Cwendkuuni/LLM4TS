
package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

public class VerseTest {
    
    private static Verse verse;
    
    @BeforeClass
    public static void setUp() {
        verse = Verse.New(1);
    }
    
    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetText() {
        assertNotNull(verse.getText());
    }
    
    @Test
    public void testGetValue() {
        assertEquals(verse.getText(), verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        assertNotNull(verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        assertNotNull(verse.getReference());
    }
    
    @Test
    public void testToString() {
        assertNotNull(verse.toString());
    }
    
    @Test
    public void testGetIdWithParameters() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertNotNull(id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[] { new Translation() };
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = new Translation[] { new Translation() };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 10);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
    }
    
    @Test
    public void testReadId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertEquals(-1, id);
    }
}
