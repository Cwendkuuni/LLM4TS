package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {
    private Verse verse;
    
    @Before
    public void setUp() {
        verse = new Verse(1);
    }
    
    @Test
    public void testNew() {
        Verse newVerse = Verse.New(1);
        assertEquals(verse, newVerse);
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
        assertEquals(null, verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        assertEquals(null, verse.getReference());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.obj.Verse:[1 null null]", verse.toString());
    }
    
    @Test
    public void testGetIdWithTranslation() {
        int id = Verse.GetId(new Translation(1), new Book(1), 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        ArrayList<Verse> verses = Verse.Get(new Translation[] {new Translation(1)}, 1, 1, 1);
        assertEquals(1, verses.size());
        assertEquals(verse, verses.get(0));
    }
    
    @Test
    public void testGetWithRange() {
        ArrayList<Verse> verses = Verse.Get(new Translation[] {new Translation(1)}, new Reference(1, 1, 1), new Reference(2, 2, 2));
        assertEquals(1, verses.size());
        assertEquals(verse, verses.get(0));
    }
    
    @Test
    public void testGetIdWithTranslationId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    private static class Translation extends bible.obj.Translation {
        public Translation(int id) {
            super(id);
        }
        
        public static int Normalize(int translationId) {
            return translationId;
        }
    }
    
    private static class Reference extends bible.obj.Reference {
        public Reference(int bookId, int chapterNo, int verseNo) {
            super(bookId, chapterNo, verseNo);
        }
    }
    
    private static class Book extends bible.obj.Book {
        public Book(int id) {
            super(id);
        }
    }
    
    private static class DbResult {
        public static void Close(DbResult result) {
            // implementation
        }
        
        public boolean next() {
            return false;
        }
        
        public int getInt(String column) {
            return 1;
        }
        
        public String getString(String column) {
            return "";
        }
    }
    
    private static class Database {
        public static DbResult Query(String query) {
            return new DbResult();
        }
    }
}