import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {
	
	@Test
	public void testConstructorWithTwoSequences() {
		// Creating two sequences
		List<Integer> sequence1 = Arrays.asList(1, 2, 3);
		List<Integer> sequence2 = Arrays.asList(1, 2, 4);
		
		// Creating SequencesComparator object
		SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
		
		// Checking the sequences are initialized correctly
		assertEquals(sequence1, comparator.sequence1);
		assertEquals(sequence2, comparator.sequence2);
		
		// Checking the default Equator is used
		assertTrue(comparator.equator instanceof DefaultEquator);
	}
	
	@Test
	public void testConstructorWithTwoSequencesAndEquator() {
		// Creating two sequences
		List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
		List<String> sequence2 = Arrays.asList("apple", "banana", "durian");
		
		// Creating Equator object
		Equator<String> equator = new Equator<String>() {
			@Override
			public boolean equate(String object1, String object2) {
				return object1.startsWith(object2.substring(0, 2));
			}
		};
		
		// Creating SequencesComparator object
		SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
		
		// Checking the sequences are initialized correctly
		assertEquals(sequence1, comparator.sequence1);
		assertEquals(sequence2, comparator.sequence2);
		
		// Checking the provided Equator is used
		assertEquals(equator, comparator.equator);
	}
	
	@Test
	public void testGetScript() {
		// Creating two sequences
		List<Integer> sequence1 = Arrays.asList(1, 2, 3);
		List<Integer> sequence2 = Arrays.asList(1, 2, 4);
		
		// Creating SequencesComparator object
		SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
		
		// Getting the edit script
		EditScript<Integer> script = comparator.getScript();
		
		// Validating the edit script
		
		// First command should be a KeepCommand
		assertTrue(script.getCommands().get(0) instanceof KeepCommand);
		assertEquals(1, ((KeepCommand<Integer>) script.getCommands().get(0)).getObject().intValue());
		
		// Second command should be a KeepCommand
		assertTrue(script.getCommands().get(1) instanceof KeepCommand);
		assertEquals(2, ((KeepCommand<Integer>) script.getCommands().get(1)).getObject().intValue());
		
		// Third command should be a DeleteCommand
		assertTrue(script.getCommands().get(2) instanceof DeleteCommand);
		assertEquals(3, ((DeleteCommand<Integer>) script.getCommands().get(2)).getObject().intValue());
		
		// Fourth command should be an InsertCommand
		assertTrue(script.getCommands().get(3) instanceof InsertCommand);
		assertEquals(4, ((InsertCommand<Integer>) script.getCommands().get(3)).getObject().intValue());
	}
	
	@Test
	public void testBuildSnake() {
		// Creating two sequences
		List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
		List<String> sequence2 = Arrays.asList("apple", "banana", "durian");
		
		// Creating Equator object
		Equator<String> equator = new Equator<String>() {
			@Override
			public boolean equate(String object1, String object2) {
				return object1.startsWith(object2.substring(0, 2));
			}
		};
		
		// Creating SequencesComparator object
		SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
		
		// Building the snake
		Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
		
		// Validating the snake
		assertEquals(0, snake.getStart());
		assertEquals(2, snake.getEnd());
		assertEquals(0, snake.getDiag());
	}
	
	@Test
	public void testGetMiddleSnake() {
		// Creating two sequences
		List<Integer> sequence1 = Arrays.asList(1, 2, 3);
		List<Integer> sequence2 = Arrays.asList(1, 2, 4);
		
		// Creating SequencesComparator object
		SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
		
		// Getting the middle snake
		Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
		
		// Validating the middle snake
		assertEquals(1, snake.getStart());
		assertEquals(1, snake.getEnd());
		assertEquals(-1, snake.getDiag());
	}
	
	@Test(expected = RuntimeException.class)
	public void testBuildScriptInternalError() {
		// Creating two sequences
		List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
		List<String> sequence2 = Arrays.asList("apple", "banana", "durian");
		
		// Creating SequencesComparator object
		SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
		
		// Calling buildScript method with invalid parameters to invoke the Internal Error scenario
		comparator.buildScript(0, 0, 0, 0, new EditScript<String>());
	}
	
}