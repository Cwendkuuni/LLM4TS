
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

  @Test
  public void testGetScript() {
    SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 5, 6));
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
  }

  @Test
  public void testBuildSnake() {
    SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 5, 6));
    Snake snake = comparator.buildSnake(0, 0, 2, 2);
    assertNotNull(snake);
  }
  
  @Test
  public void testGetMiddleSnake() {
    SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 5, 6));
    Snake snake = comparator.getMiddleSnake(0, 3, 0, 4);
    assertNull(snake);
  }

  @Test
  public void testBuildScript() {
    SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 5, 6));
    EditScript<Integer> script = new EditScript<>();
    comparator.buildScript(0, 3, 0, 4, script);
    assertNotNull(script);
  }

}
