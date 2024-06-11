
package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerseTest {
    @Test
    public void testGetId() {
        Translation translation = new Translation(1, "Translation 1");
        Book book = new Book(1, "Book 1");
        int chapterId = 1;
        int verseId = 1;
        assertEquals(1, Verse.GetId(translation, book, chapterId, verseId));
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation(1, "Translation 1");
        translations[1] = new Translation(2, "Translation 2");
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        assertEquals(1, Verse.Get(translations, bookId, chapterId, verseId).size());
    }
    
    @Test
    public void testGetReference() {
        TranslationReference transRef = new TranslationReference(new Translation(1, "Translation 1"), new Reference(1, 1, 1));
        Verse verse = new Verse();
        verse.setTranslationReference(transRef);
        assertEquals(transRef, verse.getTranslationReference());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse();
        verse.setId(1);
        verse.setText("This is a verse");
        TranslationReference transRef = new TranslationReference(new Translation(1, "Translation 1"), new Reference(1, 1, 1));
        verse.setTranslationReference(transRef);
        assertEquals("Verse:[1 Translation 1 This is a verse]", verse.toString());
    }
    
    @Test
    public void testNew() {
        Verse verse1 = Verse.New(1);
        Verse verse2 = Verse.New(1);
        assertEquals(verse1, verse2);
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse();
        verse.setText("This is a verse");
        assertEquals("This is a verse", verse.getText());
    }
}

