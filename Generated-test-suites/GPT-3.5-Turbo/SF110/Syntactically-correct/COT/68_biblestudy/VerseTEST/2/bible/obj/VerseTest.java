import static org.junit.Assert.*;
import org.junit.Test;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
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
        assertEquals("text", verse.getText());
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        assertEquals("text", verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = new TranslationReference(null, null);
        Verse verse = new Verse(1);
        verse.transRef = transRef;
        assertEquals(transRef, verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        Reference reference = new Reference(1, 1, 1);
        TranslationReference transRef = new TranslationReference(null, reference);
        Verse verse = new Verse(1);
        verse.transRef = transRef;
        assertEquals(reference, verse.getReference());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        assertEquals("bible.obj.Verse:[1 null null]", verse.toString());
    }
    
    @Test
    public void testGet() {
        Translation[] translations = {null};
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGet2() {
        Translation[] translations = {null};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertEquals(1, verses.size());
    }
}