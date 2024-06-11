import org.junit.Test;
import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<String> sequence1 = Arrays.asList("a", "b", "c", "d");
        List<String> sequence2 = Arrays.asList("a", "c", "e", "d");
        
        SequencesComparator<String> cmp = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = cmp.getScript();
        
        // Assert statements to verify the correctness of the script
    }

    @Test
    public void testBuildSnake() {
        // Test cases for the buildSnake method
    }

    @Test
    public void testGetMiddleSnake() {
        // Test cases for the getMiddleSnake method
    }

    @Test
    public void testBuildScript() {
        // Test cases for the buildScript method
    }

    private static class EditScript<T> {
        // Implementation of EditScript class for testing purposes
    }

    private static class KeepCommand<T> {
        // Implementation of KeepCommand class for testing purposes
    }

    private static class DeleteCommand<T> {
        // Implementation of DeleteCommand class for testing purposes
    }

    private static class InsertCommand<T> {
        // Implementation of InsertCommand class for testing purposes
    }
}