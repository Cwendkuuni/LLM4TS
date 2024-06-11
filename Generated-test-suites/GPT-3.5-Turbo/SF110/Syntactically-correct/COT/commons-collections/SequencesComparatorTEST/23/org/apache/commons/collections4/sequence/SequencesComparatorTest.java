
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {
    
    @Test
    public void testConstructor(){
        // Test case to check if the constructor initializes the instance variables properly
        
        // Create two lists
        List<Integer> seq1 = Arrays.asList(1, 2, 3);
        List<Integer> seq2 = Arrays.asList(4, 5, 6);
        
        // Create an instance of SequencesComparator using the lists
        SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
        
        // Check if the instance variables of the SequencesComparator object are initialized correctly
        assertEquals(seq1, comparator.sequence1);
        assertEquals(seq2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
        // Check if the size of the vDown and vUp arrays are correct
        assertEquals(3 + 3 + 2, comparator.vDown.length);
        assertEquals(3 + 3 + 2, comparator.vUp.length);
    }
    
    @Test
    public void testConstructorWithEquator(){
        // Test case to check if the constructor with Equator parameter initializes the instance variables properly
        
        // Create two lists
        List<String> seq1 = Arrays.asList("a", "b", "c");
        List<String> seq2 = Arrays.asList("x", "y", "z");
        
        // Create an Equator object
        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String o1, String o2) {
                return o1.equalsIgnoreCase(o2);
            }

            @Override
            public int hash(String o) {
                return o.hashCode();
            }
        };
        
        // Create an instance of SequencesComparator using the lists and the custom Equator
        SequencesComparator<String> comparator = new SequencesComparator<>(seq1, seq2, equator);
        
        // Check if the instance variables of the SequencesComparator object are initialized correctly
        assertEquals(seq1, comparator.sequence1);
        assertEquals(seq2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        // Check if the size of the vDown and vUp arrays are correct
        assertEquals(3 + 3 + 2, comparator.vDown.length);
        assertEquals(3 + 3 + 2, comparator.vUp.length);
    }
    
    @Test
    public void testGetScript(){
        // Test case to check if the getScript method returns a valid EditScript object
        
        // Create two lists
        List<Integer> seq1 = Arrays.asList(1, 2, 3);
        List<Integer> seq2 = Arrays.asList(4, 5, 6);
        
        // Create an instance of SequencesComparator using the lists
        SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
        
        // Call the getScript method and check if the returned EditScript object is not null
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }
    
    @Test
    public void testBuildSnake(){
        // Test case to check if the buildSnake method returns a valid Snake object
        
        // Create an instance of SequencesComparator
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        
        // Call the buildSnake method and check if the returned Snake object is not null
        Snake snake = comparator.buildSnake(0, 0, 3, 3);
        assertNotNull(snake);
        // Check if the start, end and diag values of the Snake object are correct
        assertEquals(0, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake(){
        // Test case to check if the getMiddleSnake method returns a valid Snake object
        
        // Create an instance of SequencesComparator
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("a", "b", "c"), Arrays.asList("x", "y", "z"));
        
        // Call the getMiddleSnake method and check if the returned Snake object is not null
        Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        assertNotNull(snake);
        // Check if the start, end and diag values of the Snake object are correct
        assertEquals(0, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testBuildScript(){
        // Test case to check if the buildScript method appends the correct commands to the EditScript object
        
        // Create an instance of SequencesComparator
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        
        // Create an EditScript object
        EditScript<Integer> script = new EditScript<>();
        
        // Call the buildScript method and pass the EditScript object
        comparator.buildScript(0, 3, 0, 3, script);
        
        // Check if the EditScript object contains the correct commands
        List<Command<Integer>> commands = script.getCommands();
        assertEquals(3, commands.size());
        
        assertTrue(commands.get(0) instanceof KeepCommand);
        assertEquals(1, ((KeepCommand<Integer>)commands.get(0)).getObject().intValue());
        
        assertTrue(commands.get(1) instanceof KeepCommand);
        assertEquals(2, ((KeepCommand<Integer>)commands.get(1)).getObject().intValue());
        
        assertTrue(commands.get(2) instanceof KeepCommand);
        assertEquals(3, ((KeepCommand<Integer>)commands.get(2)).getObject().intValue());
    }
}
