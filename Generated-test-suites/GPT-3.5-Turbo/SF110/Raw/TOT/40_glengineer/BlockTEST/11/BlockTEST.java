import glengineer.blocks.*;
import glengineer.positions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BlockTest {

    private Scheme scheme;

    @BeforeEach
    public void setUp() {
        scheme = new Scheme(5, 5);
    }

    @Test
    public void testBlockConstructorWithValidParameters_ShouldCreateBlock() {
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockConstructorWithInvalidParameters_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ConcreteBlock(scheme, -1, 0, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new ConcreteBlock(scheme, 0, -1, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new ConcreteBlock(scheme, 2, 2, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new ConcreteBlock(scheme, 0, 0, 2, 6));
        assertThrows(IllegalArgumentException.class, () -> new ConcreteBlock(scheme, 0, 0, 6, 2));
    }

    @Test
    public void testBlockConstructorWithCharPositionParameters_ShouldCreateBlock() {
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);

        Block block = new ConcreteBlock(scheme, p1, p2);

        assertEquals(p1.x, block.x1);
        assertEquals(p1.y, block.y1);
        assertEquals(p2.x, block.x2);
        assertEquals(p2.y, block.y2);
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testBlockDefaultConstructor_ShouldCreateBlockWithDefaultValues() {
        Block block = new ConcreteBlock(scheme);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testSubblock_ShouldReturnSubblockWithCorrectParameters() {
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        Block subblock = block.subblock(1, 1, 3, 3);

        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
        assertEquals(scheme, subblock.getScheme());
    }

    @Test
    public void testSubblockWithInvalidParameters_ShouldThrowIllegalArgumentException() {
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        assertThrows(IllegalArgumentException.class, () -> block.subblock(-1, 1, 3, 3));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(1, 1, 6, 3));
        assertThrows(IllegalArgumentException.class, () -> block.subblock(1, 1, 3, 6));
    }

    @Test
    public void testCharAtWithValidCoordinates_ShouldReturnCorrectCharacter() {
        String line = "abcde";
        scheme.lines[2] = line;

        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        assertEquals(line.charAt(2), block.charAt(2, 2));
    }

    @Test
    public void testCharAtWithInvalidCoordinates_ShouldThrowIllegalArgumentException() {
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        assertThrows(IllegalArgumentException.class, () -> block.charAt(5, 5));
        assertThrows(IllegalArgumentException.class, () -> block.charAt(2, 6));
    }

    @Test
    public void testTextAtWithValidCoordinates_ShouldReturnCorrectSubstring() {
        String line = "abcde";
        scheme.lines[2] = line;

        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        assertEquals(line.substring(1, 3), block.textAt(1, 3, 2));
    }

    @Test
    public void testTextAtWithInvalidCoordinates_ShouldThrowIllegalArgumentException() {
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        assertThrows(IllegalArgumentException.class, () -> block.textAt(3, 2, 2));
    }

    @Test
    public void testContainsOnlyOneWordWithNonTrivialBlock_ShouldReturnTrue() {
        String line = "hello";
        scheme.lines[2] = line;

        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordWithTrivialBlock_ShouldThrowIllegalArgumentException() {
        Block block = new ConcreteBlock(scheme, 0, 0, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordWithEmptyBlock_ShouldThrowIllegalArgumentException() {
        scheme.lines[2] = "";

        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        assertThrows(IllegalArgumentException.class, () -> block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks_ShouldReturnListOfSubblocks() {
        String line1 = "abcd";
        String line2 = "efgh";
        String line3 = "ijkl";
        scheme.lines[1] = line1;
        scheme.lines[2] = line2;
        scheme.lines[3] = line3;

        Block block = new ConcreteBlock(scheme, 0, 1, 4, 3);

        List<Block> subblocks = block.extractSequentialSubblocks();

        assertEquals(3, subblocks.size());
        assertEquals(line1, subblocks.get(0).getUpperLine());
        assertEquals(line2, subblocks.get(1).getUpperLine());
        assertEquals(line3, subblocks.get(2).getUpperLine());
    }

    @Test
    public void testExtractParallelSubblocks_ShouldReturnListOfSubblocks() {
        String line1 = "ab";
        String line2 = "cd";
        String line3 = "ef";
        String line4 = "gh";
        scheme.lines[1] = line1;
        scheme.lines[2] = line2;
        scheme.lines[3] = line3;
        scheme.lines[4] = line4;

        Block block = new ConcreteBlock(scheme, 0, 1, 2, 4);

        List<Block> subblocks = block.extractParallelSubblocks();

        assertEquals(2, subblocks.size());
        assertEquals(line1 + line2, subblocks.get(0).getUpperLine());
        assertEquals(line3 + line4, subblocks.get(1).getUpperLine());
    }

    @Test
    public void testToString_ShouldReturnStringRepresentationOfBlock() {
        String line1 = "abcd";
        String line2 = "efgh";
        String line3 = "ijkl";
        scheme.lines[1] = line1;
        scheme.lines[2] = line2;
        scheme.lines[3] = line3;

        Block block = new ConcreteBlock(scheme, 0, 1, 4, 3);

        String expected = "Block(0,1, 4,3):\n" +
                " ##########\n" +
                " # abcd #\n" +
                " # efgh #\n" +
                " # ijkl #\n" +
                " ##########";

        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine_ShouldReturnCorrectUpperLine() {
        String line = "abcd";
        scheme.lines[0] = line;

        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);

        assertEquals(line, block.getUpperLine());
    }
}