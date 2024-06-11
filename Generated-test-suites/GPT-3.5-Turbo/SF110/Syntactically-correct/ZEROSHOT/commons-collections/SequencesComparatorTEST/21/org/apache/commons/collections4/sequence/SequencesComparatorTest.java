import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SequencesComparatorTest {

  @Test
  public void testGetScript() {
    // Arrange
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4));
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    
    // Act
    EditScript<Integer> script = comparator.getScript();
    
    // Assert
    assertNotNull(script);
    assertEquals(3, script.getCommands().size());
    assertEquals(KeepCommand.class, script.getCommands().get(0).getClass());
    assertEquals(KeepCommand.class, script.getCommands().get(1).getClass());
    assertEquals(InsertCommand.class, script.getCommands().get(2).getClass());
  }

  @Test
  public void testBuildSnake() {
    // Arrange
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4));
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    int start = 0;
    int diag = 0;
    int end1 = sequence1.size();
    int end2 = sequence2.size();
    
    // Act
    SequencesComparator.Snake snake = comparator.buildSnake(start, diag, end1, end2);
    
    // Assert
    assertNotNull(snake);
    assertEquals(start, snake.getStart());
    assertEquals(diag, snake.getDiag());
    assertEquals(end1, snake.getEnd());
  }

  @Test
  public void testGetMiddleSnake() {
    // Arrange
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4));
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    int start1 = 0;
    int end1 = sequence1.size();
    int start2 = 0;
    int end2 = sequence2.size();
    
    // Act
    SequencesComparator.Snake snake = comparator.getMiddleSnake(start1, end1, start2, end2);
    
    // Assert
    assertNotNull(snake);
    assertTrue(snake.getStart() >= start1 && snake.getStart() <= end1);
    assertTrue(snake.getDiag() >= start1 - start2 - 1 && snake.getDiag() <= end1 - end2 + 1);
    assertTrue(snake.getEnd() >= start1 && snake.getEnd() <= end1);
  }

  @Test
  public void testBuildScript() {
    // Arrange
    List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4));
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    int start1 = 0;
    int end1 = sequence1.size();
    int start2 = 0;
    int end2 = sequence2.size();
    EditScript<Integer> script = new EditScript<>();
    
    // Act
    comparator.buildScript(start1, end1, start2, end2, script);
    
    // Assert
    assertNotNull(script);
    assertEquals(3, script.getCommands().size());
    assertEquals(KeepCommand.class, script.getCommands().get(0).getClass());
    assertEquals(KeepCommand.class, script.getCommands().get(1).getClass());
    assertEquals(InsertCommand.class, script.getCommands().get(2).getClass());
  }
}