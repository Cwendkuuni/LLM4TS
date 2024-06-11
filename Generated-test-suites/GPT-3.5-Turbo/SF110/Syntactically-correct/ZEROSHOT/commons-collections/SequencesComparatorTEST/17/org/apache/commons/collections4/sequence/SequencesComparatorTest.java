import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

  @Test
  public void testGetScript() {
    // create a SequencesComparator object
    List<Integer> sequence1 = Arrays.asList(1, 2, 3);
    List<Integer> sequence2 = Arrays.asList(4, 5, 6);
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

    // invoke the getScript method and check the returned EditScript object
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
    assertEquals(4, script.size());
    assertEquals(new KeepCommand<>(1), script.getCommand(0));
    assertEquals(new InsertCommand<>(4), script.getCommand(1));
    assertEquals(new KeepCommand<>(2), script.getCommand(2));
    assertEquals(new InsertCommand<>(5), script.getCommand(3));
  }

  @Test
  public void testBuildSnake() {
    // create a SequencesComparator object
    List<Integer> sequence1 = Arrays.asList(1, 2, 3);
    List<Integer> sequence2 = Arrays.asList(4, 5, 6);
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

    // invoke the buildSnake method and check the returned Snake object
    Snake snake = comparator.buildSnake(0, 0, 3, 3);
    assertNotNull(snake);
    assertEquals(0, snake.getStart());
    assertEquals(3, snake.getEnd());
    assertEquals(0, snake.getDiag());
  }

  @Test
  public void testGetMiddleSnake() {
    // create a SequencesComparator object
    List<Integer> sequence1 = Arrays.asList(1, 2, 3);
    List<Integer> sequence2 = Arrays.asList(4, 5, 6);
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

    // invoke the getMiddleSnake method and check the returned Snake object
    Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
    assertNotNull(snake);
    assertEquals(1, snake.getStart());
    assertEquals(2, snake.getEnd());
    assertEquals(0, snake.getDiag());
  }

  @Test
  public void testBuildScript() {
    // create a SequencesComparator object
    List<Integer> sequence1 = Arrays.asList(1, 2, 3);
    List<Integer> sequence2 = Arrays.asList(4, 5, 6);
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

    // create an empty EditScript object
    EditScript<Integer> script = new EditScript<>();

    // invoke the buildScript method and check the modified EditScript object
    comparator.buildScript(0, 3, 0, 3, script);
    assertNotNull(script);
    assertEquals(4, script.size());
    assertEquals(new KeepCommand<>(1), script.getCommand(0));
    assertEquals(new InsertCommand<>(4), script.getCommand(1));
    assertEquals(new KeepCommand<>(2), script.getCommand(2));
    assertEquals(new InsertCommand<>(5), script.getCommand(3));
  }

}