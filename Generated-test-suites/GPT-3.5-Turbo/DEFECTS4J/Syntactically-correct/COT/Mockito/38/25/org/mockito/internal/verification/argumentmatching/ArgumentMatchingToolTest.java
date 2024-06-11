
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = Arrays.asList(
            new ContainsExtraTypeInformation(),
            new ContainsExtraTypeInformation()
        );
        Object[] arguments = new Object[2];

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchedTypes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = Arrays.asList(
            new ContainsExtraTypeInformation(),
            new ContainsExtraTypeInformation()
        );
        Object[] arguments = new Object[] {new Object(), "string"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchedTypesAndToString() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = Arrays.asList(
            new ContainsExtraTypeInformation(),
            new ContainsExtraTypeInformation()
        );
        Object[] arguments = new Object[] {new Object(), "string"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchedTypesAndToStringAndTypeMatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = Arrays.asList(
            new ContainsExtraTypeInformation(),
            new ContainsExtraTypeInformation()
        );
        Object[] arguments = new Object[] {new Object(), "string"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchedTypesAndToStringAndNoTypeMatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = Arrays.asList(
            new ContainsExtraTypeInformation(),
            new ContainsExtraTypeInformation()
        );
        Object[] arguments = new Object[] {new Object(), "string"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }
}

