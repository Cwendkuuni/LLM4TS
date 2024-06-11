import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import bible.util.*;

public class VerseTest {
    
    private Verse verse;
    
    @Before
    public void setUp() {
        verse = Verse.New(1);
    }

    @Test
    public void testNew() {
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
        assertNotNull(transRef.getReference());
    }

    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertNotNull(value);
        assertEquals(verse.getText(), value);
    }

    @Test
    public void testGetText() {
        String text = verse.getText();
        assertNotNull(text);
    }

    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        assertNotNull(reference);
    }

    @Test
    public void testToString() {
        String result = verse.toString();
        assertNotNull(result);
    }

    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertFalse(verses.isEmpty());
        assertEquals(1, verses.size());
    }
}