import org.junit.Test;
import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {
  
  @Test
  public void testGetScript() {
    //Test cases for getScript() method
    List<Integer> sequence1 = new ArrayList(Arrays.asList(1, 2, 3, 4, 5));
    List<Integer> sequence2 = new ArrayList(Arrays.asList(1, 2, 4, 6, 8));
    SequencesComparator<Integer> comparator = new SequencesComparator(sequence1, sequence2);
    EditScript<Integer> script = comparator.getScript();
    //Assert statements to validate the result
  
  
    sequence1 = new ArrayList(Arrays.asList(1, 2, 3, 4, 5));
    sequence2 = new ArrayList(Arrays.asList(1, 2, 3, 4, 5));
    comparator = new SequencesComparator(sequence1, sequence2);
    script = comparator.getScript();
    //Assert statements to validate the result
  
  
    sequence1 = new ArrayList(Arrays.asList(1, 2, 3, 4, 5));
    sequence2 = new ArrayList(Arrays.asList(5, 4, 3, 2, 1));
    comparator = new SequencesComparator(sequence1, sequence2);
    script = comparator.getScript();
    //Assert statements to validate the result
  }
  
  
  @Test
  public void testBuildSnake() {
    //Test cases for buildSnake() method
    SequencesComparator<Integer> comparator = new SequencesComparator();
    Snake snake = comparator.buildSnake(0, 0, 0, 0);
    //Assert statements to validate the result
  
  
    snake = comparator.buildSnake(0, 1, 1, 0);
    //Assert statements to validate the result
  
  
    snake = comparator.buildSnake(1, 2, 2, 1);
    //Assert statements to validate the result
  }
  
  
  @Test
  public void testGetMiddleSnake() {
    //Test cases for getMiddleSnake() method
    SequencesComparator<Integer> comparator = new SequencesComparator();
    Snake middleSnake = comparator.getMiddleSnake(0, 0, 0, 0);
    //Assert statements to validate the result
  
  
    middleSnake = comparator.getMiddleSnake(0, 1, 1, 0);
    //Assert statements to validate the result
  
  
    middleSnake = comparator.getMiddleSnake(1, 2, 2, 1);
    //Assert statements to validate the result
  }
  
  
  @Test
  public void testBuildScript() {
    //Test cases for buildScript() method
    SequencesComparator<Integer> comparator = new SequencesComparator();
    EditScript<Integer> script = comparator.buildScript(0, 0, 0, 0);
    //Assert statements to validate the result
  
  
    script = comparator.buildScript(0, 1, 1, 0);
    //Assert statements to validate the result
  
  
    script = comparator.buildScript(1, 2, 2, 1);
    //Assert statements to validate the result
  }
  
  
  @Test
  public void testSnakeClass() {
    //Test cases for Snake class methods
    SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 0, 0);
    int start = snake.getStart();
    //Assert statements to validate the result
  
  
    int end = snake.getEnd();
    //Assert statements to validate the result
  
  
    int diag = snake.getDiag();
    //Assert statements to validate the result
  }
}