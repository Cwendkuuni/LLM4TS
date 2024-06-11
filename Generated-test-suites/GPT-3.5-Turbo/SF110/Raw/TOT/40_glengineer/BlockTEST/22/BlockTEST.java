import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class BlockTest {
    
    // Test case for Block constructor with valid parameters
    // Typical use case
    @Test
    public void testBlockConstructorWithValidParameters() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 2, 3, 4);
        assertEquals(1, block.x1);
        assertEquals(2, block.y1);
        assertEquals(3, block.x2);
        assertEquals(4, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    // Test case for Block constructor with invalid parameters
    // Edge case: x1 < 0
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidParameters_X1LessThanZero() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, -1, 2, 3, 4);
    }
    
    // Test case for Block constructor with invalid parameters
    // Edge case: y1 < 0
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidParameters_Y1LessThanZero() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, -2, 3, 4);
    }
    
    // Test case for Block constructor with invalid parameters
    // Edge case: x1 > x2
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidParameters_X1GreaterThanX2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 3, 2, 1, 4);
    }
    
    // Test case for Block constructor with invalid parameters
    // Edge case: y1 > y2
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidParameters_Y1GreaterThanY2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 4, 3, 2);
    }
    
    // Test case for Block constructor with invalid parameters
    // Edge case: x2 > scheme.x2
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidParameters_X2GreaterThanSchemeX2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 2, 12, 4);
    }
    
    // Test case for Block constructor with invalid parameters
    // Edge case: y2 > scheme.y2
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidParameters_Y2GreaterThanSchemeY2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 2, 3, 12);
    }
    
    // Test case for charAt method with valid parameters
    // Typical use case
    @Test
    public void testCharAtWithValidParameters() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        scheme.lines[1] = "ABC";
        scheme.lines[2] = "DEF";
        scheme.lines[3] = "GHI";
        char result = block.charAt(2, 2);
        assertEquals('I', result);
    }
    
    // Test case for charAt method with invalid parameters
    // Error scenario: x > x2
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidParameters_XGreaterThanX2() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        char result = block.charAt(4, 2);
    }
    
    // Test case for charAt method with invalid parameters
    // Error scenario: y > y2
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidParameters_YGreaterThanY2() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        char result = block.charAt(2, 4);
    }
    
    // Test case for textAt method with valid parameters
    // Typical use case
    @Test
    public void testTextAtWithValidParameters() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        scheme.lines[1] = "ABC";
        scheme.lines[2] = "DEF";
        scheme.lines[3] = "GHI";
        String result = block.textAt(1, 2, 2);
        assertEquals("EF", result);
    }
    
    // Test case for textAt method with invalid parameters
    // Error scenario: x1 > x2
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidParameters_X1GreaterThanX2() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String result = block.textAt(2, 1, 2);
    }
    
    // Test case for textAt method with invalid parameters
    // Error scenario: y > y2
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidParameters_YGreaterThanY2() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String result = block.textAt(1, 2, 4);
    }
    
    // Test case for containsOnlyOneWord method with valid parameters
    // Typical use case
    @Test
    public void testContainsOnlyOneWordWithValidParameters() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        scheme.lines[1] = "ABC";
        scheme.lines[2] = "DEF";
        scheme.lines[3] = "GHI";
        boolean result = block.containsOnlyOneWord();
        assertFalse(result);
    }
    
    // Test case for containsOnlyOneWord method with invalid parameters
    // Error scenario: isTrivial() returns true
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithInvalidParameters_TrivialBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteTrivialBlock(scheme);
        boolean result = block.containsOnlyOneWord();
    }
    
    // Test case for containsOnlyOneWord method with invalid parameters
    // Error scenario: firstWord_Horizontally().y == block.y2
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithInvalidParameters_EmptyBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        boolean result = block.containsOnlyOneWord();
    }
    
    // Test case for extractSequentialSubblocks method
    // Not possible to propose specific test case without implementation details
    
    // Test case for extractParallelSubblocks method
    // Not possible to propose specific test case without implementation details
    
    // Test case for toString method
    // Not possible to propose specific test case without printing the result
    
    // Test case for getUpperLine method
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        scheme.lines[1] = "ABC";
        scheme.lines[2] = "DEF";
        scheme.lines[3] = "GHI";
        String result = block.getUpperLine();
        assertEquals("ABC", result);
    }
    
}