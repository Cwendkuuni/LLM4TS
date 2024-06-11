package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    public void testNew() {
        assertEquals(verse, Verse.New(1));
    }
    
    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetText() {
        assertEquals("", verse.getText());
    }
    
    @Test
    public void testGetValue() {
        assertEquals("", verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        assertNotNull(verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        assertNotNull(verse.getReference());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.obj.Verse:[1 " + verse.getTranslationReference() + " " + verse.getText() + "]", verse.toString());
    }
    
    @Test
    public void testGetIdWithParams() {
        assertEquals(1, Verse.GetId(new Translation(1), new Book(1), 1, 1));
    }
    
    @Test
    public void testGetWithTranslationsArray() {
        ArrayList<Verse> verses = Verse.Get(new Translation[]{new Translation(1)}, 1, 1, 1);
        assertEquals(1, verses.size());
        assertEquals(verse, verses.get(0));
    }
    
    @Test
    public void testGetWithReferences() {
        ArrayList<Verse> verses = Verse.Get(new Translation[]{new Translation(1)}, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(1, verses.size());
        assertEquals(verse, verses.get(0));
    }
    
    @Test
    public void testGetIdWithParamsAndNormalization() {
        assertEquals(1, Verse.GetId(1, 1, 1, 1));
    }
    
    @Test
    public void testVerseCacheContainsNewVerse() {
        Verse newVerse = Verse.New(2);
        assertTrue(Verse.verseCache.containsValue(newVerse));
    }
}