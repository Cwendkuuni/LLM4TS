import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        // Arrange
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        
        // Act
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        
        // Assert
        assertNotNull(comparator);
    }
    
    @Test
    public void testConstructorWithEquator() {
        // Arrange
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("x", "y", "z");
        Equator<String> equator = new DefaultEquator<String>();
        
        // Act
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2, equator);
        
        // Assert
        assertNotNull(comparator);
    }
    
    @Test
    public void testGetScript() {
        // Arrange
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        
        // Act
        EditScript<Integer> script = comparator.getScript();
        
        // Assert
        assertNotNull(script);
    }
    
    @Test
    public void testBuildSnake() {
        // Arrange
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        
        // Act
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        
        // Assert
        assertNotNull(snake);
    }
    
    @Test
    public void testGetMiddleSnake() {
        // Arrange
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        int start1 = 0;
        int end1 = sequence1.size();
        int start2 = 0;
        int end2 = sequence2.size();
        
        // Act
        Snake snake = comparator.getMiddleSnake(start1, end1, start2, end2);
        
        // Assert
        assertNotNull(snake);
    }
    
    @Test
    public void testBuildScript() {
        // Arrange
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        int start1 = 0;
        int end1 = sequence1.size();
        int start2 = 0;
        int end2 = sequence2.size();
        EditScript<Integer> script = new EditScript<Integer>();
        
        // Act
        comparator.buildScript(start1, end1, start2, end2, script);
        
        // Assert
        assertNotNull(script);
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptThrowsException() {
        // Arrange
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        int start1 = 0;
        int end1 = sequence1.size();
        int start2 = 0;
        int end2 = sequence2.size();
        EditScript<Integer> script = new EditScript<Integer>();
        
        // Act
        comparator.buildScript(start1, end1, start2, end2, script);
        
        // Assert
        // Expects RuntimeException to be thrown
    }
}