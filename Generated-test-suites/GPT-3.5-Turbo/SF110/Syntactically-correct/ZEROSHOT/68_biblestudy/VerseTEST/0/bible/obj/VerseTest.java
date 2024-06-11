package bible.obj;

import org.junit.*;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        Assert.assertNotNull(verse);
        Assert.assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        Assert.assertNotEquals(-1, id);
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        String text = verse.getText();
        Assert.assertNotNull(text);
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        String value = verse.getValue();
        Assert.assertNotNull(value);
        Assert.assertEquals(verse.getText(), value);
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        TranslationReference translationReference = verse.getTranslationReference();
        Assert.assertNotNull(translationReference);
        Assert.assertEquals(verse.getReference(), translationReference.getReference());
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        Reference reference = verse.getReference();
        Assert.assertNotNull(reference);
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String string = verse.toString();
        Assert.assertNotNull(string);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "Translation");
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        Assert.assertNotNull(verses);
    }
    
    @Test
    public void testGet2() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "Translation");
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        Assert.assertNotNull(verses);
    }
}