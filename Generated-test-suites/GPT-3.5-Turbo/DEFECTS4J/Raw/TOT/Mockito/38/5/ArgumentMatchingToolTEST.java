import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.matchers.Matcher;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchersAndArguments_returnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsCountMismatch_returnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationButMatches_returnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{"argument"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationButDoesNotMatchAndToStringDoesNotEqual_returnArrayWithIndex() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{"different"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(1, result.length);
        Assert.assertEquals(Integer.valueOf(0), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationButDoesNotMatchAndToStringEqualsButTypeDoesNotMatch_returnArrayWithIndex(){
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{"argument"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(1, result.length);
        Assert.assertEquals(Integer.valueOf(0), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationButDoesNotMatchAndToStringEqualsAndTypeMatches_returnEmptyArray(){
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{matcher};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }
}