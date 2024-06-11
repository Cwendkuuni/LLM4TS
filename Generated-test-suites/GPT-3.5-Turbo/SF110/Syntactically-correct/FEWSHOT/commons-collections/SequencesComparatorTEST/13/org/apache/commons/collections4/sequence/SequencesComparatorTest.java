import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.sequence.*;

public class SequencesComparatorTest {
    
  @Test
  public void testConstructor() {
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    assertNotNull(comparator);
  }
  
  @Test
  public void testConstructorWithEquator() {
    List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    List<String> sequence2 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    Equator<String> equator = DefaultEquator.defaultEquator();
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    assertNotNull(comparator);
  }
  
  @Test
  public void testGetScript() {
    List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    List<String> sequence2 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<String> script = comparator.getScript();
    assertNotNull(script);
  }
  
  @Test
  public void testBuildSnake() {
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
    Equator<Integer> equator = DefaultEquator.defaultEquator();
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    SequencesComparator.Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
    assertNotNull(snake);
  }
  
  @Test
  public void testGetMiddleSnake() {
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
    Equator<Integer> equator = DefaultEquator.defaultEquator();
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    SequencesComparator.Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
    assertNotNull(snake);
  }
  
  @Test
  public void testBuildScript() {
    List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    List<String> sequence2 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<String> script = new EditScript<>();
    comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
    assertNotNull(script);
  }
  
  @Test(expected = RuntimeException.class)
  public void testBuildScriptWithInvalidInput() {
    List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    List<String> sequence2 = new ArrayList<>(Arrays.asList("a", "b", "c"));
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<String> script = new EditScript<>();
    comparator.buildScript(0, sequence1.size(), 1, sequence2.size(), script);
  }
  
  @Test
  public void testSnakeGetStart() {
    SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 1, 2);
    assertEquals(0, snake.getStart());
  }
  
  @Test
  public void testSnakeGetEnd() {
    SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 1, 2);
    assertEquals(1, snake.getEnd());
  }
  
  @Test
  public void testSnakeGetDiag() {
    SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 1, 2);
    assertEquals(2, snake.getDiag());
  }
    
}