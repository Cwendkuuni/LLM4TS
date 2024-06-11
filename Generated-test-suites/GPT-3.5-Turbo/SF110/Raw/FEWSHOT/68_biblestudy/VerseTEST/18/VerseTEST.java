package bible.obj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import org.junit.Test;

import bible.util.DbResult;
import bible.util.Database;
import bible.util.Reference;
import bible.util.Translation;
import bible.util.TranslationReference;
import bible.util.Util;

public class VerseTest {
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
    }
    
    @Test
    public void testGetId() {
        Translation translation = new Translation();
        Book book = new Book();
        int chapterId = 1;
        int verseId = 1;
        
        int id = Verse.GetId(translation, book, chapterId, verseId);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation();
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse();
        TranslationReference transRef = new TranslationReference();
        verse.setTranslationReference(transRef);
        
        assertEquals(transRef, verse.getTranslationReference());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse();
        verse.setId(1);
        verse.setTransRef(new TranslationReference());
        verse.setText("text");
        
        String expected = "bible.obj.Verse:[1 TranslationReference text]";
        assertEquals(expected, verse.toString());
    }
    
    @Test
    public void testGetId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(1, id);
    }
    
    @Test
    public void testComplete() {
        DbResult result = new DbResult();
        result.setInt("Verse_ID", 1);
        result.setString("Text", "text");
        result.setInt("Translation_ID", 1);
        result.setInt("Book_ID", 1);
        result.setInt("Chapter", 1);
        result.setInt("Verse_No", 1);
        
        Verse verse = new Verse();
        verse.complete(result);
        
        assertEquals(1, verse.getId());
        assertEquals("text", verse.getText());
        assertNotNull(verse.getTranslationReference());
    }
    
    @Test
    public void testReadId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertEquals(1, id);
    }
}