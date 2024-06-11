
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.ArrayList;
import bible.obj.Translation;
import bible.util.Reference;
import bible.util.TranslationReference;

public class VerseTest {

    @Test
    public void testNew() {
        int id = 1;
        Verse verse = Verse.New(id);
        assertEquals(id, verse.getId());
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterVerseId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(id, verse.getId());
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterVerseId_TranslationNotFound() {
        int translationId = 999;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(id, -1);
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterVerseId_BookNotFound() {
        int translationId = 1;
        int bookId = 999;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(id, -1);
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterVerseId_ChapterNotFound() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 999;
        int verseId = 1;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(id, -1);
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterVerseId_VerseNotFound() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 999;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(id, -1);
    }
    
    @Test
    public void testGetWithTranslationsBookChapterVerse() {
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = Verse.Get(new Translation[]{}, bookId, chapterId, verseId);
        assertEquals(0, verses.size());
    }
    
    @Test
    public void testGetWithTranslationsBeginEnd() {
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        ArrayList<Verse> verses = Verse.Get(new Translation[]{}, begin, end);
        assertEquals(0, verses.size());
    }
    
    @Test
    public void testGetText() {
        String text = "Test text";
        Verse verse = new Verse();
        verse.setText(text);
        assertEquals(text, verse.getText());
    }
    
    @Test
    public void testGetValue() {
        String text = "Test text";
        Verse verse = new Verse();
        verse.setText(text);
        assertEquals(text, verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        Translation translation = new Translation(1, "Test translation");
        Reference reference = new Reference(1, 1, 1);
        TranslationReference transRef = new TranslationReference(translation, reference);
        Verse verse = new Verse();
        verse.setTranslationReference(transRef);
        assertEquals(transRef, verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        Translation translation = new Translation(1, "Test translation");
        Reference reference = new Reference(1, 1, 1);
        TranslationReference transRef = new TranslationReference(translation, reference);
        Verse verse = new Verse();
        verse.setTranslationReference(transRef);
        assertEquals(reference, verse.getReference());
    }
    
    @Test
    public void testToString() {
        int id = 1;
        Translation translation = new Translation(1, "Test translation");
        Reference reference = new Reference(1, 1, 1);
        TranslationReference transRef = new TranslationReference(translation, reference);
        String text = "Test text";
        Verse verse = new Verse();
        verse.setTranslationReference(transRef);
        verse.setText(text);
        assertEquals("Verse:[" + id + " " + transRef + " " + text + "]", verse.toString());
    }
}
