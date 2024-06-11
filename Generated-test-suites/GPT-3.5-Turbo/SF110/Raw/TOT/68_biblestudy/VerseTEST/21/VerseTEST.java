import static org.junit.Assert.*;
import org.junit.*;

public class VerseTest {

    Verse verse;
    Translation translation;
    Book book;
    int chapterId;
    int verseId;

    @Before
    public void setUp() {
        verse = Verse.New(1);
        translation = new Translation("English");
        book = new Book("Genesis");
        chapterId = 1;
        verseId = 1;
    }

    @Test
    public void testNew() {
        Verse verse1 = Verse.New(1);
        assertNotNull(verse1);
        assertEquals(1, verse1.getId());
    }

    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testGetText() {
        String text = verse.getText();
        assertNotNull(text);
    }

    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertNotNull(value);
        assertEquals(verse.getText(), value);
    }

    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
    }

    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        assertNotNull(reference);
    }

    @Test
    public void testToString() {
        String str = verse.toString();
        assertNotNull(str);
        assertEquals("Verse:[" + verse.getId() + " " + verse.getTranslationReference() + " " + verse.getText() + "]", str);
    }

    @Test
    public void testGetWithTranslations() {
        Translation[] translations = {translation};
        ArrayList verses = Verse.Get(translations, book.getId(), chapterId, verseId);
        assertNotNull(verses);
    }

    @Test
    public void testGetWithReferences() {
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 10);
        Translation[] translations = {translation};
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
    }

    @After
    public void tearDown() {
        verse = null;
        translation = null;
        book = null;
        chapterId = 0;
        verseId = 0;
    }

}