package org.mockito.internal.verification.argumentmatching;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = Arrays.asList(
                CoreMatchers.instanceOf(String.class),
                CoreMatchers.instanceOf(Integer.class)
        );
        Object[] arguments = {"Test", 123};
        
        Integer[] expected = {0, 1};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatchers() {
        List<Matcher> matchers = Arrays.asList();
        Object[] arguments = {};

        Integer[] expected = {};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentTypes() {
        List<Matcher> matchers = Arrays.asList(
                CoreMatchers.instanceOf(String.class),
                CoreMatchers.instanceOf(Integer.class)
        );
        Object[] arguments = {123, "Test"};

        Integer[] expected = {0, 1};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_Exception() {
        List<Matcher> matchers = Arrays.asList(CoreMatchers.instanceOf(String.class));
        Object[] arguments = {123};

        Integer[] expected = {};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, result);
    }
}