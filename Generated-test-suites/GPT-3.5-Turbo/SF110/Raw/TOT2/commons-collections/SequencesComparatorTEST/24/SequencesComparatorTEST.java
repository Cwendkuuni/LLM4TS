import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {
  
  // Test constructor with DefaultEquator
  @Test
  public void testConstructorWithDefaultEquator() {
    // Create two sequences
    List<Integer> sequence1 = new ArrayList<>();
    sequence1.add(1);
    sequence1.add(2);
    List<Integer> sequence2 = new ArrayList<>();
    sequence2.add(1);
    sequence2.add(2);

    // Create SequencesComparator object
    SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2);

    // Check if the SequencesComparator object is not null
    assertNotNull(sequencesComparator);
  }

  // Test constructor with specified Equator
  @Test
  public void testConstructorWithSpecifiedEquator() {
    // Create two sequences
    List<String> sequence1 = new ArrayList<>();
    sequence1.add("Hello");
    List<String> sequence2 = new ArrayList<>();
    sequence2.add("World");

    // Create custom Equator for comparing Strings
    Equator<String> stringEquator = new Equator<String>() {
        @Override
        public boolean equate(String object1, String object2) {
            return object1.toLowerCase().equals(object2.toLowerCase());
        }
        @Override
        public int hash(String object) {
            return object.toLowerCase().hashCode();
        }
    };

    // Create SequencesComparator object
    SequencesComparator<String> sequencesComparator = new SequencesComparator<>(sequence1, sequence2, stringEquator);

    // Check if the SequencesComparator object is not null
    assertNotNull(sequencesComparator);
  }

  // Test getScript method
  @Test
  public void testGetScript() {
    // Create two sequences
    List<String> sequence1 = new ArrayList<>();
    sequence1.add("a");
    sequence1.add("b");
    List<String> sequence2 = new ArrayList<>();
    sequence2.add("c");
    sequence2.add("d");

    // Create SequencesComparator object
    SequencesComparator<String> sequencesComparator = new SequencesComparator<>(sequence1, sequence2);

    // Get the edit script
    EditScript<String> script = sequencesComparator.getScript();

    // Check if the script is not null
    assertNotNull(script);
  }

  // Additional test cases for edge cases and exceptions can be added here

}