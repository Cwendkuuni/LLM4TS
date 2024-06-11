import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerseTest {
    
    public VerseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        assertEquals("Test Text", verse.getText());
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        assertEquals("Test Text", verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getReference());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        assertTrue(verse.toString().contains("Verse"));
        assertTrue(verse.toString().contains("1"));
        assertTrue(verse.toString().contains("Test Text"));
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[]{new Translation()};
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertFalse(verses.isEmpty());
    }
    
    @Test
    public void testGetMultiple() {
        Translation[] translations = new Translation[]{new Translation()};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertFalse(verses.isEmpty());
    }
}