import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerseTest {
    private static Verse verse;
    
    @BeforeClass
    public static void setUp() {
        verse = Verse.New(1);
    }
    
    @Test
    public void testNew() {
        Verse verse1 = Verse.New(2);
        assertNotNull(verse1);
        assertEquals(2, verse1.getId());
        assertNotSame(verse, verse1);
        
        Verse verse2 = Verse.New(1);
        assertNotNull(verse2);
        assertEquals(1, verse2.getId());
        assertEquals(verse, verse2);
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
        
        int id2 = Verse.GetId(0, 0, 0, 0);
        assertEquals(-1, id2);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[] {new Translation(1), new Translation(2)};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = verse.Get(translations, bookId, chapterId, verseId);
        
        assertEquals(2, verses.size());
        assertEquals(1, verses.get(0).getId());
        assertEquals(2, verses.get(1).getId());
    }
    
    @Test
    public void testGetByRange() {
        Translation[] translations = new Translation[] {new Translation(1), new Translation(2)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        ArrayList<Verse> verses = verse.Get(translations, begin, end);
        
        assertEquals(4, verses.size());
        assertEquals(1, verses.get(0).getId());
        assertEquals(2, verses.get(1).getId());
        assertEquals(3, verses.get(2).getId());
        assertEquals(4, verses.get(3).getId());
    }
    
    @Test
    public void testGetText() {
        String text = verse.getText();
        assertNotNull(text);
        assertEquals("This is verse 1", text);
    }
    
    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
        assertEquals(1, transRef.getTranslation().getId());
        assertEquals(1, transRef.getReference().getBookId());
        assertEquals(1, transRef.getReference().getChapterNo());
        assertEquals(1, transRef.getReference().getVerseNo());
    }
    
    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        assertNotNull(reference);
        assertEquals(1, reference.getBookId());
        assertEquals(1, reference.getChapterNo());
        assertEquals(1, reference.getVerseNo());
    }
    
    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertNotNull(value);
        assertEquals("This is verse 1", value);
    }
    
    @Test
    public void testToString() {
        String string = verse.toString();
        assertNotNull(string);
        assertEquals("Verse:[1 Translation:[1] Reference:[1 1 1] This is verse 1]", string);
    }
}