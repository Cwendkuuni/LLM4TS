
package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class VerseTest {
    private Reference reference;
    private Translation translation;
    private Book book;
    
    @Before
    public void setup() {
        reference = new Reference(1, 1, 1);
        translation = new Translation(1, "translation");
        book = new Book(1, "book");
    }
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
        assertNotNull(Verse.verseCache.get(verse.getId()));
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
        assertNotNull(Verse.translations.get(1));
        assertNotNull(Verse.translations.get(1).get(1));
        assertNotNull(Verse.translations.get(1).get(1).get(1));
        assertEquals(1, (int) Verse.translations.get(1).get(1).get(1).get(1));
    }
    
    @Test
    public void testGet() {
        ArrayList<Verse> verses = Verse.Get(new Translation[]{translation}, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(1, verses.size());
        assertEquals(1, verses.get(0).getId());
    }
    
    @Test
    public void testGetMultiple() {
        ArrayList<Verse> verses = Verse.Get(new Translation[]{translation, translation}, reference, reference);
        assertNotNull(verses);
        assertEquals(2, verses.size());
        assertEquals(1, verses.get(0).getId());
        assertEquals(1, verses.get(1).getId());
    }
    
    @Test
    public void testGetIdWithNullTranslation() {
        int id = Verse.GetId(null, 1, 1, 1);
        assertEquals(1, id);
        assertNotNull(Verse.translations.get(1));
        assertNotNull(Verse.translations.get(1).get(1));
        assertNotNull(Verse.translations.get(1).get(1).get(1));
        assertEquals(1, (int) Verse.translations.get(1).get(1).get(1).get(1));
    }
    
    @Test
    public void testGetNullTranslations() {
        ArrayList<Verse> verses = Verse.Get(null, 1, 1, 1);
        assertNotNull(verses);
        assertTrue(verses.isEmpty());
    }
    
    @Test
    public void testGetNullTranslationsWithReferences() {
        ArrayList<Verse> verses = Verse.Get(null, reference, reference);
        assertNotNull(verses);
        assertTrue(verses.isEmpty());
    }
    
    @Test
    public void testGetIdWithInvalidTranslation() {
        int id = Verse.GetId(-1, 1, 1, 1);
        assertEquals(1, id);
        assertNotNull(Verse.translations.get(1));
        assertNotNull(Verse.translations.get(1).get(1));
        assertNotNull(Verse.translations.get(1).get(1).get(1));
        assertEquals(1, (int) Verse.translations.get(1).get(1).get(1).get(1));
    }
    
    @Test
    public void testToString() {
        Verse verse = Verse.New(1);
        String expected = "bible.obj.Verse:[1 null null]";
        assertEquals(expected, verse.toString());
    }
    
    @Test
    public void testComplete() {
        DbResult result = new DbResult();
        result.addRow();
        result.setValue("Verse_ID", 1);
        result.setValue("Text", "text");
        result.setValue("Translation_ID", 1);
        result.setValue("Book_ID", 1);
        result.setValue("Chapter", 1);
        result.setValue("Verse_No", 1);
        
        Verse verse = new Verse(result);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
        assertNotNull(Verse.verseCache.get(verse.getId()));
        assertEquals("text", verse.getText());
        assertNotNull(verse.getTranslationReference());
        assertNotNull(verse.getReference());
    }
    
    @Test
    public void testReadId() {
        int id = Verse.ReadId(1, 1, 1, 1);
        assertEquals(1, id);
        assertNotNull(Verse.translations.get(1));
        assertNotNull(Verse.translations.get(1).get(1));
        assertNotNull(Verse.translations.get(1).get(1).get(1));
        assertEquals(1, (int) Verse.translations.get(1).get(1).get(1).get(1));
    }
    
    @Test
    public void testGetNullResult() {
        DbResult result = null;
        ArrayList<Verse> verses = Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertNotNull(verses);
        assertTrue(verses.isEmpty());
    }
    
    @Test
    public void testGetWithNullResult() {
        DbResult result = null;
        ArrayList<Verse> verses = Verse.Get(new Translation[]{translation}, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertNotNull(verses);
        assertTrue(verses.isEmpty());
    }
}
