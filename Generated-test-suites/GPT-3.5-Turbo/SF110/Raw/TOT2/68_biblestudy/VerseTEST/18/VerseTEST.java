import static org.junit.Assert.*;
import org.junit.Test;

public class VerseTest {

    @Test
    public void testNew() {
        // Test the New method by checking if a Verse is created with the correct id
        Verse verse = Verse.New(1);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetId() {
        // Test the GetId method by providing valid input and checking if the correct id is returned
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testGetIdNoTranslation() {
        // Test the GetId method with no available translation and check if it returns -1
        int id = Verse.GetId(10, 1, 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetTranslationReference() {
        // Test the getTranslationReference method by checking if it returns the correct TranslationReference object
        TranslationReference transRef = new TranslationReference(null, new Reference(1, 1, 1));
        Verse verse = new Verse(1);
        assertEquals(transRef, verse.getTranslationReference());
    }

    @Test
    public void testGetReference() {
        // Test the getReference method by checking if it returns the correct Reference object
        Reference ref = new Reference(1, 1, 1);
        Verse verse = new Verse(1);
        assertEquals(ref, verse.getReference());
    }

    @Test
    public void testToString() {
        // Test the toString method by checking if it returns the expected string representation
        Verse verse = new Verse(1);
        String expected = "bible.obj.Verse:[1 null bible.obj.Reference:[1 1 1]]";
        assertEquals(expected, verse.toString());
    }
}