import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Test;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse1 = Verse.New(1);
        assertEquals(1, verse1.getId());
        
        Verse verse2 = Verse.New(2);
        assertEquals(2, verse2.getId());
    }
    
    @Test
    public void testGetId() {
        int id1 = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id1);
        
        int id2 = Verse.GetId(2, 3, 4, 5);
        assertEquals(2, id2);
    }
    
    @Test
    public void testGet() {
        Translation translation = new Translation(1, "Translation 1");
        Book book = new Book(1, "Book 1");
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        
        Translation[] translations = { translation };
        ArrayList<Verse> verses = Verse.Get(translations, book.getId(), begin.getChapterNo(), begin.getVerseNo());
        assertEquals(1, verses.size());
        
        verses = Verse.Get(translations, begin, end);
        assertEquals(0, verses.size());
    }
    
    @Test
    public void testGetText() {
        Verse verse1 = new Verse(1);
        assertEquals("Text 1", verse1.getText());
        
        Verse verse2 = new Verse(2);
        assertEquals("Text 2", verse2.getText());
    }
    
    @Test
    public void testGetValue() {
        Verse verse1 = new Verse(1);
        assertEquals("Text 1", verse1.getValue());
        
        Verse verse2 = new Verse(2);
        assertEquals("Text 2", verse2.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        Translation translation = new Translation(1, "Translation 1");
        Reference reference = new Reference(1, 2, 3);
        TranslationReference transRef = new TranslationReference(translation, reference);
        
        Verse verse1 = new Verse(1);
        assertEquals(transRef, verse1.getTranslationReference());
        
        Verse verse2 = new Verse(2);
        assertEquals(transRef, verse2.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        Translation translation = new Translation(1, "Translation 1");
        Reference reference = new Reference(1, 2, 3);
        TranslationReference transRef = new TranslationReference(translation, reference);
        
        Verse verse1 = new Verse(1);
        assertEquals(reference, verse1.getReference());
        
        Verse verse2 = new Verse(2);
        assertEquals(reference, verse2.getReference());
    }
    
    @Test
    public void testToString() {
        Verse verse1 = new Verse(1);
        assertEquals("Verse:[1 Translation 1 Text 1]", verse1.toString());
        
        Verse verse2 = new Verse(2);
        assertEquals("Verse:[2 Translation 2 Text 2]", verse2.toString());
    }
}