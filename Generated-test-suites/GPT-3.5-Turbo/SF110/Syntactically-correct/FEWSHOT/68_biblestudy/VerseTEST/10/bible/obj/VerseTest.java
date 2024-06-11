import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertEquals(1, verse.getId());
        
        verse = Verse.New(2);
        assertEquals(2, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertEquals(1, id);
        
        id = Verse.GetId(5, 6, 7, 8);
        assertEquals(5, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation();
        translations[1] = new Translation();
        
        ArrayList<Verse> verses = Verse.Get(translations, 1, 2, 3);
        assertEquals(2, verses.size());
        
        ArrayList<Verse> emptyVerses = Verse.Get(null, 1, 2, 3);
        assertEquals(0, emptyVerses.size());
    }
    
    @Test
    public void testGetIdWithTranslationAndReference() {
        Translation translation = new Translation();
        Book book = new Book();
        
        int id = Verse.GetId(translation, book, 1, 2);
        assertEquals(1, id);
        
        id = Verse.GetId(translation, book, 3, 4);
        assertEquals(3, id);
    }
    
    @Test
    public void testGetWithTranslationsAndReference() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation();
        translations[1] = new Translation();
        
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(2, verses.size());
        
        translations = new Translation[0];
        
        ArrayList<Verse> emptyVerses = Verse.Get(translations, begin, end);
        assertEquals(0, emptyVerses.size());
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse();
        verse.setText("This is a test verse");
        assertEquals("This is a test verse", verse.getText());
        
        verse.setText("Another test verse");
        assertEquals("Another test verse", verse.getText());
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse();
        verse.setText("This is a test verse");
        assertEquals("This is a test verse", verse.getValue());
        
        verse.setText("Another test verse");
        assertEquals("Another test verse", verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse();
        TranslationReference ref = new TranslationReference();
        verse.setTranslationReference(ref);
        assertEquals(ref, verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse();
        TranslationReference ref = new TranslationReference();
        verse.setTranslationReference(ref);
        assertEquals(ref.getReference(), verse.getReference());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse();
        verse.setId(1);
        verse.setTranslationReference(new TranslationReference());
        verse.setText("This is a test verse");
        
        assertEquals("Verse:[1 " + verse.getTranslationReference() + " This is a test verse]", verse.toString());
    }
}