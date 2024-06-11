import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testLoadInstructions() {
        // Typical use case - loading instructions from a valid file
        String validFileName = "validFile.txt";
        assertTrue(Main.loadInstructions(validFileName));

        // Edge case - loading instructions from an empty file
        String emptyFileName = "emptyFile.txt";
        assertTrue(Main.loadInstructions(emptyFileName));

        // Error scenario - loading instructions from a non-existent file
        String nonExistentFileName = "nonExistentFile.txt";
        assertFalse(Main.loadInstructions(nonExistentFileName));
    }

    @Test
    public void testRemoveComment() {
        // Typical use case - line with comment
        String lineWithComment = "jmp 5 ; jump to instruction 5";
        assertEquals("jmp 5", Main.removeComment(lineWithComment));

        // Edge case - line without comment
        String lineWithoutComment = "inc count";
        assertEquals("inc count", Main.removeComment(lineWithoutComment));

        // Edge case - line with only comment
        String lineWithOnlyComment = "; this is a comment";
        assertEquals("", Main.removeComment(lineWithOnlyComment));
    }

    // Additional test methods for other methods in the Main class

}