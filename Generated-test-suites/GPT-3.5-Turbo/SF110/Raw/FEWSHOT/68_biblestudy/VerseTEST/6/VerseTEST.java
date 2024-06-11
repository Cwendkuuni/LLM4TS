import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

    private Verse verse;
    private ArrayList translations;
    private Translation translation;
    private Book book;
    private Reference begin;
    private Reference end;
    
    @Before
    public void setup() {
        verse = new Verse(1);
        translations = new ArrayList();
        translation = new Translation();
        book = new Book();
        begin = new Reference();
        end = new Reference();
    }

    @Test
    public void testNew() {
        Verse newVerse = Verse.New(1);
        Verse cachedVerse = Verse.verseCache.get(1);
        
        assertEquals(newVerse, cachedVerse);
        assertTrue(newVerse instanceof Verse);
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterVerse() {
        int id = Verse.GetId(1, 1, 1, 1);
        
        assertEquals(1, id);
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterVerseNegative() {
        int id = Verse.GetId(0, 0, 0, 0);
        
        assertEquals(-1, id);
    }
    
    @Test
    public void testGetWithTranslationsBookChapterVerse() {
        translations.add(translation);
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        
        assertTrue(verses.contains(verse));
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGetWithTranslationsBeginEnd() {
        translations.add(translation);
        ArrayList verses = Verse.Get(translations, begin, end);
        
        assertTrue(verses.contains(verse));
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGetWithEmptyTranslationsBeginEnd() {
        ArrayList verses = Verse.Get(new Translation[]{}, begin, end);
        
        assertTrue(verses.isEmpty());
    }
    
    @Test
    public void testGetText() {
        String text = verse.getText();
        
        assertEquals("", text);
    }

    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        
        assertNull(transRef);
    }
    
    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        
        assertNull(reference);
    }
    
    @Test
    public void testToString() {
        String str = verse.toString();
        
        assertEquals("bible.obj.Verse:[1 null ]", str);
    }
    
}