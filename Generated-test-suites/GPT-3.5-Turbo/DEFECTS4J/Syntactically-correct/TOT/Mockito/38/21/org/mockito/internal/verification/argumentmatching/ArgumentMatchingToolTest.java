import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @After
    public void tearDown() {
        argumentMatchingTool = null;
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsEqual() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                new Matcher(),
                new Matcher(),
                new Matcher()
        );
        Object[] arguments = new Object[]{"arg1", "arg2", "arg3"};

        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        Assert.assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsNotEqualButToStringEqual() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                new Matcher(),
                new Matcher(),
                new Matcher()
        );
        Object[] arguments = new Object[]{1, "2", 3.0};

        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        Assert.assertEquals(3, indexes.length);
        Assert.assertTrue(Arrays.asList(indexes).contains(0));
        Assert.assertTrue(Arrays.asList(indexes).contains(1));
        Assert.assertTrue(Arrays.asList(indexes).contains(2));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsNotEqualButToStringEqualAndMismatchedType() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                new Matcher(),
                new Matcher(),
                new Matcher()
        );
        Object[] arguments = new Object[]{1, true, 3.0};

        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        Assert.assertEquals(2, indexes.length);
        Assert.assertTrue(Arrays.asList(indexes).contains(1));
        Assert.assertTrue(Arrays.asList(indexes).contains(2));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_incorrectNumberOfMatchers() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                new Matcher(),
                new Matcher()
        );
        Object[] arguments = new Object[]{"arg1", "arg2", "arg3"};

        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        Assert.assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchersList() {
        // Arrange
        List<Matcher> matchers = Arrays.asList();
        Object[] arguments = new Object[]{"arg1", "arg2", "arg3"};

        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        Assert.assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyArgumentsArray() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                new Matcher(),
                new Matcher(),
                new Matcher()
        );
        Object[] arguments = new Object[]{};

        // Act
        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        Assert.assertEquals(0, indexes.length);
    }
}