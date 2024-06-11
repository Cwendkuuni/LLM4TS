import org.apache.commons.collections4.sequence.*;
import org.apache.commons.collections4.*;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class SequencesComparatorTest {
    
  @Test
  public void testConstructor() {
     SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
     assertNotNull(comparator);
  }
  
  @Test
  public void testConstructorWithEquator() {
     Equator<Integer> equator = DefaultEquator.defaultEquator();
     SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6), equator);
     assertNotNull(comparator);
  }
  
  @Test
  public void testGetScript() {
     SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
     EditScript<Integer> script = comparator.getScript();
     assertNotNull(script);
  }
  
  @Test
  public void testBuildSnake() {
     SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4));
     SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 3);
     assertNotNull(snake);
     assertEquals(0, snake.getStart());
     assertEquals(3, snake.getEnd());
     assertEquals(0, snake.getDiag());
  }
  
  @Test
  public void testGetMiddleSnake() {
     SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4));
     SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
     assertNull(snake);
  }
  
  @Test(expected = RuntimeException.class)
  public void testGetMiddleSnakeInternalError() {
     SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
     comparator.getMiddleSnake(0, 3, 0, 3);
  }
  
  @Test
  public void testBuildScript() {
     SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4));
     EditScript<Integer> script = new EditScript<>();
     comparator.buildScript(0, 3, 0, 3, script);
     assertNotNull(script);
  }
  
  @Test
  public void testSnakeGetters() {
     SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 3, 0);
     assertEquals(0, snake.getStart());
     assertEquals(3, snake.getEnd());
     assertEquals(0, snake.getDiag());
  }

}