package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

    private static final Matcher<Object> MATCHER = new Matcher<Object>() {
        @Override
        public boolean matches(Object item) {
            return true;
        }
    };

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<Matcher>();
        Object[] arguments = new Object[3];

        matchers.add(MATCHER);
        matchers.add(MATCHER);
        matchers.add(MATCHER);

        arguments[0] = null;
        arguments[1] = new Object();
        arguments[2] = new Object();

        Integer[] expected = {1, 2};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, result);
    }
}