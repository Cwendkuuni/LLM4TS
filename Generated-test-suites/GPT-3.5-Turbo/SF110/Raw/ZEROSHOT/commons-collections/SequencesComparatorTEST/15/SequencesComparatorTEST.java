import org.junit.Test;
import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {

  @Test
  public void testConstructor1() {
    List<Integer> sequence1 = new ArrayList<>();
    List<Integer> sequence2 = new ArrayList<>();
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    // Assert statements
  }
  
  @Test
  public void testConstructor2() {
    List<String> sequence1 = new ArrayList<>();
    List<String> sequence2 = new ArrayList<>();
    Equator<String> equator = DefaultEquator.defaultEquator();
    SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    // Assert statements
  }
  
  @Test
  public void testGetScript() {
    List<Double> sequence1 = new ArrayList<>();
    List<Double> sequence2 = new ArrayList<>();
    SequencesComparator<Double> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<Double> script = comparator.getScript();
    // Assert statements
  }
  
  @Test
  public void testBuildSnake() {
    List<Character> sequence1 = new ArrayList<>();
    List<Character> sequence2 = new ArrayList<>();
    Equator<Character> equator = DefaultEquator.defaultEquator();
    SequencesComparator<Character> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    Snake snake = comparator.buildSnake(0, 0, 0, 0);
    // Assert statements
  }
  
  @Test
  public void testGetMiddleSnake() {
    List<Short> sequence1 = new ArrayList<>();
    List<Short> sequence2 = new ArrayList<>();
    Equator<Short> equator = DefaultEquator.defaultEquator();
    SequencesComparator<Short> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    Snake snake = comparator.getMiddleSnake(0, 0, 0, 0);
    // Assert statements
  }
  
  @Test
  public void testBuildScript() {
    List<Long> sequence1 = new ArrayList<>();
    List<Long> sequence2 = new ArrayList<>();
    SequencesComparator<Long> comparator = new SequencesComparator<>(sequence1, sequence2);
    EditScript<Long> script = new EditScript<>();
    comparator.buildScript(0, 0, 0, 0, script);
    // Assert statements
  }
  
  @Test
  public void testSnakeGetters() {
    int start = 0;
    int end = 0;
    int diag = 0;
    SequencesComparator.Snake snake = new SequencesComparator.Snake(start, end, diag);
    // Assert statements
  }
}