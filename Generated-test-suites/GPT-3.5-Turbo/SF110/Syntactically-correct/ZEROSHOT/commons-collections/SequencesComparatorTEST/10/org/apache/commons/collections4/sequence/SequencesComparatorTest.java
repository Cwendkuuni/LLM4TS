import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.functors.DefaultEquator;

public class SequencesComparatorTest {

  @Test
  public void testGetScript() {
    List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
    List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<Integer> script = comparator.getScript();
    assertEquals(0, script.getCommands().size());
  }

  @Test
  public void testGetScriptWithDifferentElements() {
    List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
    List<Integer> sequence2 = Arrays.asList(1, 2, 6, 4, 5);
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<Integer> script = comparator.getScript();
    assertEquals(2, script.getCommands().size());
    assertTrue(script.getCommands().get(0) instanceof InsertCommand);
    assertTrue(script.getCommands().get(1) instanceof DeleteCommand);
  }

  @Test
  public void testGetScriptWithAddedElements() {
    List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
    List<Integer> sequence2 = Arrays.asList(1, 6, 2, 3, 4, 5);
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<Integer> script = comparator.getScript();
    assertEquals(1, script.getCommands().size());
    assertTrue(script.getCommands().get(0) instanceof InsertCommand);
  }

  @Test
  public void testGetScriptWithDeletedElements() {
    List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
    List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<Integer> script = comparator.getScript();
    assertEquals(1, script.getCommands().size());
    assertTrue(script.getCommands().get(0) instanceof DeleteCommand);
  }

}