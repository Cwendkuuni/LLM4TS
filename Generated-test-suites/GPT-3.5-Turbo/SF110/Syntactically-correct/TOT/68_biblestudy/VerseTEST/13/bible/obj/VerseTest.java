
package bible.obj;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class VerseTest {
    
    private Verse verse;

    @Before
    public void setUp() {
        verse = new Verse(1);
    }

    @After
    public void tearDown() {
        verse = null;
    }

    @Test
    public void testNew_ValidId_ReturnsVerseObject() {
        Verse result = Verse.New(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    public void testGetId_ValidId_ReturnsId() {
        int result = verse.getId();
        assertEquals(1, result);
    }

    @Test
    public void testGetText_ReturnsText() {
        String result = verse.getText();
        assertNotNull(result);
        assertEquals("Test Verse Text", result);
    }

    @Test
    public void testGetValue_ReturnsText() {
        String result = verse.getValue();
        assertNotNull(result);
        assertEquals("Test Verse Text", result);
    }

    @Test
    public void testGetTranslationReference_ReturnsTranslationReference() {
        TranslationReference result = verse.getTranslationReference();
        assertNotNull(result);
        assertEquals("Test Translation Reference", result.getReference());
    }
    
    @Test
    public void testGetReference_ReturnsReference() {
        Reference result = verse.getReference();
        assertNotNull(result);
        assertEquals("Test Reference", result.toString());
    }

    @Test
    public void testToString_ReturnsStringRepresentation() {
        String result = verse.toString();
        assertNotNull(result);
        assertEquals("bible.obj.Verse:[1 Test Translation Reference Test Verse Text]", result);
    }
    
    @Test
    public void testGetId_WithValidParams_ReturnsId() {
        int result = Verse.GetId(1, 1, 1, 1);
        assertTrue(result > 0);
        assertEquals(1, result);
    }
    
    @Test
    public void testGet_WithValidParams_ReturnsListOfVerses() {
        Translation[] translations = {new Translation(1, "Test Translation")};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        ArrayList<Verse> result = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }
    
    @Test
    public void testGet_WithReferenceParams_ReturnsListOfVerses() {
        Translation[] translations = {new Translation(1, "Test Translation")};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);
        
        ArrayList<Verse> result = Verse.Get(translations, begin, end);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }
    
    @Test
    public void testGetId_WithInvalidParams_ReturnsNegativeId() {
        int result = Verse.GetId(-1, 0, 0, 0);
        assertEquals(-1, result);
    }
    
    @Test
    public void testReadId_WithValidParams_ReturnsId() {
        int result = Verse.ReadId(1, 1, 1, 1);
        assertTrue(result > 0);
        assertEquals(1, result);
    }
    
    @Test
    public void testReadId_WithInvalidParams_ReturnsNegativeId() {
        int result = Verse.ReadId(-1, 0, 0, 0);
        assertEquals(-1, result);
    }
}
