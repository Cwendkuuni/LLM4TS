import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorWithoutCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, -1, 1, 3, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidPosition() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        char ch = block.charAt(5, 5);
    }
    
    @Test
    public void testCharAtWithValidPosition() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[1] = "abcdefghij";
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        char ch = block.charAt(1, 1);
        assertEquals('b', ch);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String text = block.textAt(0, 5, 1);
    }
    
    @Test
    public void testTextAtWithValidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[1] = "abcdefghij";
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String text = block.textAt(1, 3, 1);
        assertEquals("bc", text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 1, 1);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWordWithNonEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[1] = "abcdefghij";
        scheme.lines[2] = "abcdefghij";
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        boolean hasOneWord = block.containsOnlyOneWord();
        assertTrue(hasOneWord);
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[1] = "abcdefghij";
        scheme.lines[2] = "abcdefghij";
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String expectedOutput = "Block(1,1, 3,3):\n" +
                                " ###### \n" +
                                " # bc # \n" +
                                " # bc # \n" +
                                " ###### ";
        String toString = block.toString();
        assertEquals(expectedOutput, toString);
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[1] = "abcdefghij";
        scheme.lines[2] = "abcdefghij";
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String upperLine = block.getUpperLine();
        assertEquals("bc", upperLine);
    }
    
    // Additional test cases for extractSequentialSubblocks() and extractParallelSubblocks() methods
    
}