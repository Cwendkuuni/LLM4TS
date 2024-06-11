import static org.junit.Assert.*;
import org.junit.Test;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertEquals(0, id);
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        String text = verse.getText();
        assertNotNull(text);
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        String value = verse.getValue();
        assertNotNull(value);
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        Reference ref = verse.getReference();
        assertNotNull(ref);
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String str = verse.toString();
        assertNotNull(str);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = {new Translation(1, "English")};
        ArrayList verses = Verse.Get(translations, 1, 2, 3);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetIdWithTranslations() {
        Translation translation = new Translation(1, "English");
        Book book = new Book(1, "Genesis");
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translation, book, chapterId, verseId);
        assertEquals(0, id);        
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = {new Translation(1, "English")};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 10);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
    }
    
}