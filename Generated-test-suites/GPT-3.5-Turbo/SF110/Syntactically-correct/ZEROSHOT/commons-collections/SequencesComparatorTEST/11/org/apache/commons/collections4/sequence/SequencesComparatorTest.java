import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {

  @Test
  public void testConstructor1() {
    List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1,2,3));
    List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(4,5,6));
    SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
    assertEquals(sequence1, comparator.sequence1);
    assertEquals(sequence2, comparator.sequence2);
    assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
  }

  @Test
  public void testConstructor2() {
    List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1,2,3));
    List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(4,5,6));
    Equator<Integer> equator = DefaultEquator.defaultEquator();
    SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2, equator);
    assertEquals(sequence1, comparator.sequence1);
    assertEquals(sequence2, comparator.sequence2);
    assertEquals(equator, comparator.equator);
  }

  @Test
  public void testGetScript() {
    List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1,2,3));
    List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(4,5,6));
    SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
    EditScript<Integer> script = comparator.getScript();
    // add assertions for the expected script
  }

  @Test
  public void testBuildSnake() {
    // add test cases for buildSnake method
  }

  @Test
  public void testGetMiddleSnake() {
    // add test cases for getMiddleSnake method
  }

  @Test
  public void testBuildScript() {
    // add test cases for buildScript method
  }

  // additional test methods for other methods in SequencesComparator class

}