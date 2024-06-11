import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SequencesComparatorTest {
  
  // Test constructor with two sequences
  @Test
  public void testConstructor() {
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    
    assertNotNull(comparator);
    assertEquals(sequence1, comparator.sequence1);
    assertEquals(sequence2, comparator.sequence2);
    assertNotNull(comparator.equator);
    assertNotNull(comparator.vDown);
    assertNotNull(comparator.vUp);
  }
  
  // Test constructor with two sequences and equator
  @Test
  public void testConstructorWithEquator() {
    List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    List<String> sequence2 = new ArrayList<>(Arrays.asList("x", "y", "z"));
    Equator<String> equator = new Equator<>();
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    
    assertNotNull(comparator);
    assertEquals(sequence1, comparator.sequence1);
    assertEquals(sequence2, comparator.sequence2);
    assertEquals(equator, comparator.equator);
    assertNotNull(comparator.vDown);
    assertNotNull(comparator.vUp);
  }
  
  // Test getScript method
  @Test
  public void testGetScript() {
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<Integer> script = comparator.getScript();
    
    assertNotNull(script);
  }
  
  // Test buildSnake method
  @Test
  public void testBuildSnake() {
    List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    List<String> sequence2 = new ArrayList<>(Arrays.asList("x", "y", "z"));
    Equator<String> equator = new Equator<>();
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
    
    assertNotNull(snake);
    assertEquals(0, snake.getStart());
    assertEquals(0, snake.getDiag());
    assertEquals(3, snake.getEnd());
  }
  
  // Test getMiddleSnake method
  @Test
  public void testGetMiddleSnake() {
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    Snake middleSnake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
    
    assertNull(middleSnake);
  }
  
  // Test buildScript method
  @Test
  public void testBuildScript() {
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<Integer> script = new EditScript<>();
    comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
    
    assertNotNull(script);
  }
  
  // Test Snake inner class
  @Test
  public void testSnake() {
    SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 3, 0);
    
    assertEquals(0, snake.getStart());
    assertEquals(3, snake.getEnd());
    assertEquals(0, snake.getDiag());
  }
}