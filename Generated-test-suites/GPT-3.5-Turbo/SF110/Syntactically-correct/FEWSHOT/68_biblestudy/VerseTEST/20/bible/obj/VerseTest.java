import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import bible.util.Reference;
import bible.util.Translation;
import org.junit.Test;

import java.util.ArrayList;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation translation1 = new Translation(1, "Translation1");
        Translation translation2 = new Translation(2, "Translation2");
        Translation[] translations = {translation1, translation2};
        
        ArrayList<Verse> verses = Verse.Get(translations, 1, 2, 3);
        
        assertEquals(2, verses.size());
        assertTrue(verses.contains(new Verse(1)));
        assertTrue(verses.contains(new Verse(2)));
    }
    
    @Test
    public void testGetStringTranslationId() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertEquals(1, id);
    }
    
    @Test
    public void testGetReference() {
        Translation translation = new Translation(1, "Translation");
        Book book = new Book(1, "Book");
        Reference reference = new Reference(1, 2, 3);
        
        Verse verse = Verse.Get(translation, book, reference);
        
        assertEquals(1, verse.getId());
        assertEquals("Text", verse.getText());
        assertEquals(translation, verse.getTranslationReference().getTranslation());
        assertEquals(reference, verse.getTranslationReference().getReference());
    }
    
    @Test
    public void testToString() {
        Translation translation = new Translation(1, "Translation");
        Book book = new Book(1, "Book");
        Reference reference = new Reference(1, 2, 3);
        
        Verse verse = Verse.Get(translation, book, reference);
        
        String expected = "Verse:[" + verse.getId() + " " + verse.getTranslationReference() + " " + verse.getText() + "]";
        assertEquals(expected, verse.toString());
    }
}