import org.junit.*;
import static org.junit.Assert.*;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[]{new Translation()};
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetReference() {
        Verse verse = Verse.New(1);
        Reference reference = verse.getReference();
        assertNotNull(reference);
    }
    
    @Test
    public void testToString() {
        Verse verse = Verse.New(1);
        String str = verse.toString();
        assertNotNull(str);
    }  
}