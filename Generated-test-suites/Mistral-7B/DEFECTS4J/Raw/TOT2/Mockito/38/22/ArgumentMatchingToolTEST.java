package org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.contains;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(contains(1));
        matchers.add(contains("two"));
        matchers.add(typeMatches(Integer[].class));
        Object[] arguments = new Object[]{1, "two", new Integer[3]};
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertThat(indexes, is(instanceOf(Integer[].class)));
        assertThat(indexes, contains(1, 2));
        assertThat(indexes.length, is(2));
    }

    @Test
    public void testSafelyMatchesWithExceptions() {
        Matcher<Integer> matcher = containsNull(); // a custom matcher that throws an exception for null arguments
        Object[] arguments = new Object[]{new Integer(1), null};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        try {
            boolean result = tool.safelyMatches(matcher, null);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // expected
        }

        try {
            boolean result = tool.safelyMatches(matcher, new Integer(1));
            assertTrue(result);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            boolean result = tool.safelyMatches(matcher, arguments[1]);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // expected
        }
    }

    private Matcher<Object> containsNull() {
        // implementation of a custom matcher that throws an exception for null arguments
    }
}