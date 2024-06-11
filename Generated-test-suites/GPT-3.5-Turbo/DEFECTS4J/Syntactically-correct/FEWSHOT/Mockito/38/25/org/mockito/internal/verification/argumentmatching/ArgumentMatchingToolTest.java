package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {
    ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsHaveDifferentSizes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(equalTo(1));

        Object[] arguments = new Object[]{1, 2};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsHaveSameSizeButTypesDontMatch() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);

        Object[] arguments = new Object[]{"test"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(0, (int)result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsHaveSameSizeAndToStringEquals() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);

        Object[] arguments = new Object[]{matcher};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsHaveSameSizeAndToStringNotEquals() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);

        Object[] arguments = new Object[]{"test"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(0, (int)result[0]);
    }
}