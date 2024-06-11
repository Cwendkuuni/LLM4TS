import org.junit.*;
import java.util.*;
import org.apache.commons.collections4.equator.DefaultEquator;
import org.apache.commons.collections4.equator.Equator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;

public class SequencesComparatorTest {

  @Test
  public void testConstructorWithDefaultEquator() {
    List<String> sequence1 = Arrays.asList("a", "b", "c");
    List<String> sequence2 = Arrays.asList("a", "b", "c");
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    Assert.assertEquals(sequence1, comparator.sequence1);
    Assert.assertEquals(sequence2, comparator.sequence2);
    Assert.assertTrue(comparator.equator instanceof DefaultEquator);
    Assert.assertNotNull(comparator.vDown);
    Assert.assertNotNull(comparator.vUp);
  }

  @Test
  public void testConstructorWithCustomEquator() {
    List<Integer> sequence1 = Arrays.asList(1, 2, 3);
    List<Integer> sequence2 = Arrays.asList(4, 5, 6);
    Equator<Integer> equator = (o1, o2) -> o1 % 2 == o2 % 2;
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    Assert.assertEquals(sequence1, comparator.sequence1);
    Assert.assertEquals(sequence2, comparator.sequence2);
    Assert.assertEquals(equator, comparator.equator);
  }

  @Test
  public void testGetScriptWithEqualSequences() {
    List<String> sequence = Arrays.asList("a", "b", "c");
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence, sequence);
    EditScript<String> script = comparator.getScript();
    Assert.assertTrue(script.isEmpty());
  }

  @Test
  public void testGetScriptWithDifferentSequences() {
    List<String> sequence1 = Arrays.asList("a", "b", "c");
    List<String> sequence2 = Arrays.asList("a", "x", "c", "y");
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<String> script = comparator.getScript();
    Assert.assertEquals(3, script.size());
    Assert.assertTrue(script.getCommand(0) instanceof KeepCommand);
    Assert.assertTrue(script.getCommand(1) instanceof InsertCommand);
    Assert.assertTrue(script.getCommand(2) instanceof KeepCommand);
  }

  @Test(expected = RuntimeException.class)
  public void testGetMiddleSnakeWithInvalidStartEndIndices() {
    List<Integer> sequence1 = Collections.emptyList();
    List<Integer> sequence2 = Arrays.asList(1, 2, 3);
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    comparator.getMiddleSnake(0, 1, 0, 5);
  }

  @Test
  public void testBuildScriptWithEmptySequences() {
    List<Integer> sequence1 = Collections.emptyList();
    List<Integer> sequence2 = Collections.emptyList();
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<Integer> script = new EditScript<>();
    comparator.buildScript(0, 0, 0, 0, script);
    Assert.assertTrue(script.isEmpty());
  }

  @Test
  public void testBuildScriptWithEqualSequences() {
    List<String> sequence = Arrays.asList("a", "b", "c");
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence, sequence);
    EditScript<String> script = new EditScript<>();
    comparator.buildScript(0, sequence.size(), 0, sequence.size(), script);
    Assert.assertEquals(sequence.size(), script.size());
    for (int i = 0; i < sequence.size(); i++) {
      Assert.assertTrue(script.getCommand(i) instanceof KeepCommand);
      Assert.assertEquals(sequence.get(i), script.getCommand(i).getObject());
    }
  }

  @Test
  public void testBuildScriptWithDifferentSequences() {
    List<String> sequence1 = Arrays.asList("a", "b", "c");
    List<String> sequence2 = Arrays.asList("a", "x", "c", "y");
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<String> script = new EditScript<>();
    comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
    Assert.assertEquals(3, script.size());
    Assert.assertTrue(script.getCommand(0) instanceof KeepCommand);
    Assert.assertTrue(script.getCommand(1) instanceof InsertCommand);
    Assert.assertTrue(script.getCommand(2) instanceof KeepCommand);
  }

}