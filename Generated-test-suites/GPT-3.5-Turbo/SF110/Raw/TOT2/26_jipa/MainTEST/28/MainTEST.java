import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class MainTest {

    @Test
    public void testGetFileName() {
        // Typical Use Case
        // Provide a file name and expect the same file name to be returned
        String expectedFileName = "testFile.txt";
        String actualFileName = Main.getFileName();
        assertEquals(expectedFileName, actualFileName);
        
        // Edge Case
        // Provide an empty file name and expect a prompt for input
        System.setIn(new ByteArrayInputStream("\n".getBytes()));
        String expectedFileName2 = "";
        String actualFileName2 = Main.getFileName();
        assertEquals(expectedFileName2, actualFileName2);
    }

    @Test
    public void testLoadInstructions() {
        // Typical Use Case
        // Provide an existing file name and expect instructions to be loaded successfully
        String fileName = "test_instructions.txt";
        assertTrue(Main.loadInstructions(fileName));

        // Edge Case
        // Provide a non-existing file name and expect false return
        String nonExistingFileName = "non_existing_file.txt";
        assertFalse(Main.loadInstructions(nonExistingFileName));
    }

    @Test
    public void testRemoveComment() {
        // Typical Use Case
        // Provide a string with a comment and expect the comment to be removed
        String input = "instruction ;comment";
        String expectedOutput = "instruction";
        String actualOutput = Main.removeComment(input);
        assertEquals(expectedOutput, actualOutput);

        // Edge Case
        // Provide a string without a comment and expect the same string
        String input2 = "instruction";
        String expectedOutput2 = "instruction";
        String actualOutput2 = Main.removeComment(input2);
        assertEquals(expectedOutput2, actualOutput2);
    }

    // Add more test cases for other methods in Main class

}