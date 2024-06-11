package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import bible.util.Reference;
import bible.util.Translation;
import bible.util.TranslationReference;

public class VerseTest {
    
    private Verse verse;
    
    @Before
    public void setup() {
        verse = new Verse(1);
    }

    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetText() {
        assertNotNull(verse.getText());
    }
    
    @Test
    public void testGetValue() {
        assertEquals(verse.getText(), verse.getValue());
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
        assertNotNull(verse.toString());
    }
    
    @Test
    public void testGetIdWithArgs() {
        assertEquals(2, Verse.GetId(1, 1, 1, 1));
    }
    
    @Test
    public void testGet() {
        Translation translation = mock(Translation.class);
        Book book = mock(Book.class);
        Reference begin = mock(Reference.class);
        Reference end = mock(Reference.class);
        
        assertNotNull(Verse.Get(new Translation[] {translation}, 1, 1, 1));
        assertNotNull(Verse.Get(new Translation[] {translation}, begin, end));
    }
    
    @Test
    public void testGetId() {
        assertEquals(1, Verse.GetId(new Translation(), new Book(), 1, 1));
    }
    
    @Test
    public void testComplete() {
        DbResult result = mock(DbResult.class);
        
        assertNotNull(new Verse(result));
    }
    
}