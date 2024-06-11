import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IndexedStringTest {

    private IndexedString indexedString;

    @BeforeEach
    public void setup() {
        indexedString = new IndexedString("abracadabra");
    }

    @Test
    public void testCount() {
        // Test case for counting number of occurrences of a character in the string
        char c = 'a';
        int expectedCount = 5;
        int actualCount = indexedString.count(c);
        Assertions.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testCountNoOccurrences() {
        // Test case for counting number of occurrences of a character that does not exist in the string
        char c = 'z';
        int expectedCount = 0;
        int actualCount = indexedString.count(c);
        Assertions.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testIndexOf() {
        // Test case for finding the index of a target sequence in the string
        char[] target = {'a', 'b', 'r', 'a'};
        int expectedIndex = 1;
        int actualIndex = indexedString.indexOf(target);
        Assertions.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testIndexOfSingleCharacterTarget() {
        // Test case for finding the index of a single character target in the string
        char[] target = {'d'};
        int expectedIndex = 7;
        int actualIndex = indexedString.indexOf(target);
        Assertions.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testIndexOfTargetNotFound() {
        // Test case for target sequence not found in the string
        char[] target = {'z', 'x'};
        int expectedIndex = -1;
        int actualIndex = indexedString.indexOf(target);
        Assertions.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testIndexOfEmptyTarget() {
        // Test case for empty target sequence
        char[] target = {};
        int expectedIndex = -1;
        int actualIndex = indexedString.indexOf(target);
        Assertions.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testIndexOfEmptyString() {
        // Test case for empty string
        IndexedString emptyIndexedString = new IndexedString("");
        char[] target = {'a'};
        int expectedIndex = -1;
        int actualIndex = emptyIndexedString.indexOf(target);
        Assertions.assertEquals(expectedIndex, actualIndex);
    }
}