import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.collections4.*;

public class SequencesComparatorTest {
  
  @Test
  public void testGetScript() {
    List<String> sequence1 = new ArrayList<>();
    sequence1.add("a");
    sequence1.add("b");
    sequence1.add("c");
    
    List<String> sequence2 = new ArrayList<>();
    sequence2.add("a");
    sequence2.add("c");
    sequence2.add("d");
    
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<String> script = comparator.getScript();
    
    assertNotNull(script);
    assertEquals(5, script.size());
  }
  
  @Test
  public void testBuildSnake() {
    List<String> sequence1 = new ArrayList<>();
    sequence1.add("a");
    sequence1.add("b");
    sequence1.add("c");
    
    List<String> sequence2 = new ArrayList<>();
    sequence2.add("a");
    sequence2.add("c");
    sequence2.add("d");
    
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 3);
    
    assertNotNull(snake);
    assertEquals(1, snake.getStart());
    assertEquals(2, snake.getEnd());
    assertEquals(0, snake.getDiag());
  }
  
  @Test
  public void testGetMiddleSnake() {
    List<String> sequence1 = new ArrayList<>();
    sequence1.add("a");
    sequence1.add("b");
    sequence1.add("c");
    
    List<String> sequence2 = new ArrayList<>();
    sequence2.add("a");
    sequence2.add("c");
    sequence2.add("d");
    
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
    
    assertNotNull(snake);
    assertEquals(1, snake.getStart());
    assertEquals(2, snake.getEnd());
    assertEquals(0, snake.getDiag());
  }
  
  @Test
  public void testBuildScript() {
    List<String> sequence1 = new ArrayList<>();
    sequence1.add("a");
    sequence1.add("b");
    sequence1.add("c");
    
    List<String> sequence2 = new ArrayList<>();
    sequence2.add("a");
    sequence2.add("c");
    sequence2.add("d");
    
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<String> script = new EditScript<>();
    comparator.buildScript(0, 3, 0, 3, script);
    
    assertNotNull(script);
    assertEquals(5, script.size());
  }
  
}