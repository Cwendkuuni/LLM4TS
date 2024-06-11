import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testLoadInstructions_emptyFileName() {
        assertFalse(Main.loadInstructions(""));
    }

    @Test
    public void testLoadInstructions_invalidFileName() {
        assertFalse(Main.loadInstructions("invalid_file.txt"));
    }

    @Test
    public void testRemoveComment_noComment() {
        String line = "mov var1, 5";
        assertEquals("mov var1, 5", Main.removeComment(line));
    }

    @Test
    public void testRemoveComment_withComment() {
        String line = "mov var1, 5 ; move value to var1";
        assertEquals("mov var1, 5", Main.removeComment(line));
    }

    @Test
    public void testSplitArguments_noComma() {
        String[] args = Main.splitArguments("var1");
        assertEquals(1, args.length);
        assertEquals("var1", args[0]);
    }

    @Test
    public void testSplitArguments_withComma() {
        String[] args = Main.splitArguments("var1, 5");
        assertEquals(2, args.length);
        assertEquals("var1", args[0]);
        assertEquals("5", args[1]);
    }

    // Add more test cases for other methods

}