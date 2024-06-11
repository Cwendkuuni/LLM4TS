import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

    private Verse verse;
    
    @Before
    public void setUp() {
        verse = Verse.New(1);
    }
    
    @Test
    public void testGetId() {
        int id = verse.getId();
        assertNotNull(id);
    }
    
    @Test
    public void testGetText() {
        String text = verse.getText();
        assertNotNull(text);
    }
    
    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertNotNull(value);
    }
    
    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
    }
    
    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        assertNotNull(reference);
    }
    
    @Test
    public void testToString() {
        String str = verse.toString();
        assertNotNull(str);
    }
    
    @Test
    public void testGetIdWithParams() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertNotNull(id);
    }
    
    @Test
    public void testGetWithTranslations() {
        Translation[] translations = new Translation[]{new Translation()};
        int bookId = 2;
        int chapterId = 3;
        int verseId = 4;
        ArrayList verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = new Translation[]{new Translation()};
        Reference begin = new Reference(1, 2, 3);
        Reference end = new Reference(4, 5, 6);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetIdWithParams2() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertNotNull(id);
    }
    
}