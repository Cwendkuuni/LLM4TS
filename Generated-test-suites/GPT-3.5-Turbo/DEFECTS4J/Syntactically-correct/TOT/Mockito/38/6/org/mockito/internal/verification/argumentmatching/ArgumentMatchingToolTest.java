import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {
    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeDifferent_ReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeOne_MatcherNotInstanceOfContainsExtraTypeInformation_ReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new Matcher());
        Object[] arguments = new Object[]{};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeOne_MatcherInstanceOfContainsExtraTypeInformation_DoesNotMatch_ReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeOne_MatcherInstanceOfContainsExtraTypeInformation_Matches_ReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new ContainsExtraTypeInformation()};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeOne_MatcherInstanceOfContainsExtraTypeInformation_DoesNotTypeMatch_ReturnOneElementArrayWithZero() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new Object()};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeOne_MatcherInstanceOfContainsExtraTypeInformation_TypeMatchesButDoesNotSafeMatch_ReturnOneElementArrayWithZero() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new ContainsExtraTypeInformation("Test")};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeOne_MatcherInstanceOfContainsExtraTypeInformation_TypeMatchesAndSafeMatches_ReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new ContainsExtraTypeInformation()};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeThree_AllMatchersNotInstanceOfContainsExtraTypeInformation_ReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new Matcher());
        matchers.add(new Matcher());
        matchers.add(new Matcher());
        Object[] arguments = new Object[]{new Object(), new Object(), new Object()};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeThree_AllMatchersInstanceOfContainsExtraTypeInformation_NotMatching_ReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new Object(), new Object(), new Object()};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeThree_AllMatchersInstanceOfContainsExtraTypeInformation_OneTypeDoesNotMatch_ReturnOneElementArrayWithIndexOne() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation("Test"));
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new Object(), new ContainsExtraTypeInformation(), new Object()};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(1, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeThree_AllMatchersInstanceOfContainsExtraTypeInformation_OneTypeDoesNotMatchButSafelyMatches_ReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation("Test"));
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new Object(), new ContainsExtraTypeInformation("Test"), new Object()};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeThree_AllMatchersInstanceOfContainsExtraTypeInformation_AllTypesMatchButDoesNotSafeMatch_ReturnOneElementArrayWithIndexTwo() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation("Test"));
        Object[] arguments = new Object[]{new Object(), new Object(), new ContainsExtraTypeInformation()};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(2, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeThree_AllMatchersInstanceOfContainsExtraTypeInformation_AllTypesMatchAndSafeMatch_ReturnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation("Test"));
        Object[] arguments = new Object[]{new Object(), new Object(), new ContainsExtraTypeInformation("Test")};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }

}