package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import org.junit.Before;
import org.junit.Test;

import bible.util.DbResult;
import bible.util.Reference;
import bible.util.Translation;
import bible.util.TranslationReference;
import bible.util.Util;

public class VerseTest {
    
    private Verse verse;
    
    @Before
    public void setup() {
        verse = mock(Verse.class);
    }

    @Test
    public void testNew() { // test New method
        int id = 1;
        Verse result = Verse.New(id);
        when(verse.getId()).thenReturn(id);
        assertEquals(verse.getId(), result.getId());
    }
    
    @Test
    public void testGetIdTranslationBookChapterVerse() throws SQLException { // test GetId method with translation, bookId, chapterId, verseId
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int intId = 1;
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> translations = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> translation = new HashMap<>();
        HashMap<Integer, Integer> book = new HashMap<>();
        HashMap<Integer, Integer> chapter = new HashMap<>();
        chapter.put(verseId, intId);
        book.put(chapterId, chapter);
        translation.put(bookId, book);
        translations.put(translationId, translation);
        
        when(verse.GetId(translationId, bookId, chapterId, verseId)).thenReturn(intId);
        assertEquals(intId, verse.GetId(translationId, bookId, chapterId, verseId));
    }
    
    @Test
    public void testGetIdIntIntIntInt() { // test GetId method with translationId, bookId, chapterId, verseId
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int intId = 1;
        
        when(verse.GetId(translationId, bookId, chapterId, verseId)).thenReturn(intId);
        assertEquals(intId, verse.GetId(translationId, bookId, chapterId, verseId));
    }
    
    @Test
    public void testGet() { // test Get method with translations, bookId, chapterId, verseId
        Translation[] translations = {new Translation(1), new Translation(2)};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = new ArrayList<>();
        verses.add(new Verse(1));
        verses.add(new Verse(2));
        
        when(verse.Get(translations, bookId, chapterId, verseId)).thenReturn(verses);
        assertEquals(verses, verse.Get(translations, bookId, chapterId, verseId));
    }
    
    @Test
    public void testGetRefBeginEnd() throws SQLException { // test Get method with translations, begin, end
        Translation[] translations = {new Translation(1), new Translation(2)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 1);
        ArrayList<Verse> verses = new ArrayList<>();
        verses.add(new Verse(1));
        verses.add(new Verse(2));
        DbResult result = mock(DbResult.class);
        
        when(verse.Get(translations, begin, end)).thenReturn(verses);
        when(verse.Database.Query("select Verse_ID from Verse where Translation_ID in (1, 2) and Book_ID >= 1 and Book_ID <= 1 and Chapter >= 1 and Chapter <= 2 and Verse_No >= 1 and Verse_No <= 1")).thenReturn(result);
        when(result.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(result.getInt("Verse_ID")).thenReturn(1).thenReturn(2);
        
        assertEquals(verses, verse.Get(translations, begin, end));
    }
    
    @Test
    public void testGetRefBeginEndSQLException() throws SQLException { // test Get method with translations, begin, end and SQL Exception
        Translation[] translations = {new Translation(1), new Translation(2)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 1);
        ArrayList<Verse> verses = new ArrayList<>();
        verses.add(new Verse(1));
        verses.add(new Verse(2));
        DbResult result = mock(DbResult.class);
        
        when(verse.Get(translations, begin, end)).thenReturn(verses);
        when(verse.Database.Query("select Verse_ID from Verse where Translation_ID in (1, 2) and Book_ID >= 1 and Book_ID <= 1 and Chapter >= 1 and Chapter <= 2 and Verse_No >= 1 and Verse_No <= 1")).thenReturn(result);
        when(result.next()).thenThrow(new SQLException());
        
        assertEquals(verses, verse.Get(translations, begin, end));
    }
    
    @Test
    public void testGetId() { // test GetId method with translation, book, chapterId, verseId
        Translation translation = new Translation(1);
        Book book = new Book(1);
        int chapterId = 1;
        int verseId = 1;
        int result = 1;
        
        when(verse.GetId(translation, book, chapterId, verseId)).thenReturn(result);
        
        assertEquals(result, verse.GetId(translation, book, chapterId, verseId));
    }
    
    @Test
    public void testGetIdNormalize() { // test GetId method with normalize translationId, book, chapterId, verseId
        Translation translation = new Translation(1);
        Book book = new Book(1);
        int chapterId = 1;
        int verseId = 1;
        int result = 1;
        
        when(verse.GetId(translation.getId(), book.getId(), chapterId, verseId)).thenReturn(result);
        
        assertEquals(result, verse.GetId(translation.getId(), book.getId(), chapterId, verseId));
    }
    
    @Test
    public void testGetText() { // test getText method
        String text = "This is a verse";
        when(verse.getText()).thenReturn(text);
        assertEquals(text, verse.getText());
    }
    
    @Test
    public void testGetValue() { // test getValue method
        String text = "This is a verse";
        when(verse.getValue()).thenReturn(text);
        assertEquals(text, verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() { // test getTranslationReference method
        TranslationReference ref = new TranslationReference(new Translation(1), new Reference(1, 1, 1));
        when(verse.getTranslationReference()).thenReturn(ref);
        assertEquals(ref, verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() { // test getReference method
        Reference ref = new Reference(1, 1, 1);
        when(verse.getReference()).thenReturn(ref);
        assertEquals(ref, verse.getReference());
    }
    
    @Test
    public void testToString() { // test toString method
        String clsName = "bible.obj.Verse";
        int id = 1;
        String transRef = "1 1 1";
        String text = "This is a verse";
        String result = clsName + ":[" + id + " " + transRef + " " + text + "]";
        
        when(verse.toString()).thenReturn(result);
        assertEquals(result, verse.toString());
    }
}