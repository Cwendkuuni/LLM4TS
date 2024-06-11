import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.collections4.sequence.*;

public class SequencesComparatorTest {
    private List<Integer> sequence1;
    private List<Integer> sequence2;
    private Equator<Integer> equator;
    private SequencesComparator<Integer> comparator;
    
    @Before
    public void setUp() {
        sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        sequence2 = new ArrayList<>(Arrays.asList(2, 3, 4));
        equator = DefaultEquator.defaultEquator();
        comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    }
    
    @Test
    public void testGetScript() {
        EditScript<Integer> result = comparator.getScript();
        
        assertNotNull(result);
        assertEquals(3, result.getLength());
    }
    
    @Test
    public void testBuildSnake() {
        Snake result = comparator.buildSnake(0, 0, 3, 3);
        
        assertNotNull(result);
        assertEquals(0, result.getStart());
        assertEquals(3, result.getEnd());
        assertEquals(0, result.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        Snake result = comparator.getMiddleSnake(0, 3, 0, 3);
        
        assertNotNull(result);
        assertEquals(0, result.getStart());
        assertEquals(3, result.getEnd());
        assertEquals(0, result.getDiag());
    }
    
    @Test
    public void testBuildScript() {
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 3, script);
        
        assertEquals(3, script.getLength());
    }
    
    @Test(expected=RuntimeException.class)
    public void testBuildScript_InternalError() {
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, -1, 0, -1, script);
    }
    
    @Test
    public void testSnakeGetters() {
        Snake snake = new Snake(0, 3, 0);
        
        assertEquals(0, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
}