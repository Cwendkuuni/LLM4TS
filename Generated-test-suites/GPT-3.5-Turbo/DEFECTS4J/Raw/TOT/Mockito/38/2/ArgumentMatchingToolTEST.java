import org.hamcrest.Matcher;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;



public class ArgumentMatchingToolTest {
    
    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsDifferentLength() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(CoreMatchers.equalTo("test1"));

        Object[] arguments = new Object[2];
        arguments[0] = "test1";
        arguments[1] = "test2";

        // Act
        Integer[] suspiciousIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertThat(suspiciousIndexes.length, is(0));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingTypesDifferentContents() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(CoreMatchers.equalTo("test1"));

        Object[] arguments = new Object[1];
        arguments[0] = "test2";

        // Act
        Integer[] suspiciousIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertThat(suspiciousIndexes, is(new Integer[] { 0 }));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_customMatcherTypeMatches() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new CustomMatcher("test"));

        Object[] arguments = new Object[1];
        arguments[0] = "test";

        // Act
        Integer[] suspiciousIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertThat(suspiciousIndexes.length, is(0));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_customMatcherTypeDoesNotMatch() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new CustomMatcher("test"));

        Object[] arguments = new Object[1];
        arguments[0] = "differentTest";

        // Act
        Integer[] suspiciousIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertThat(suspiciousIndexes, is(new Integer[] { 0 }));
    }
}