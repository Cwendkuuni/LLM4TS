import org.junit.Test;
import org.hamcrest.MatcherAssert;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.List;
import org.hamcrest.Matchers;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsAreEmpty_ReturnsEmptyArray() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> emptyMatchers = new LinkedList<>();
        Object[] emptyArguments = new Object[0];
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(emptyMatchers, emptyArguments);
        
        MatcherAssert.assertThat(result, Matchers.arrayWithSize(0));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersSizeIsNotEqualToArgumentsSize_ReturnsEmptyArray() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Matchers.any());
        
        Object[] arguments = new Object[2];
        arguments[0] = "test";
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        MatcherAssert.assertThat(result, Matchers.arrayWithSize(0));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatcherMatchesWithTypeMismatch_ReturnsSuspiciousArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        ContainsExtraTypeInformation suspiciousMatcher = new ContainsExtraTypeInformation(String.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(suspiciousMatcher);
        
        Object[] arguments = new Object[1];
        arguments[0] = 123;
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        MatcherAssert.assertThat(result, Matchers.arrayContaining(0));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatcherMatchesWithNoTypeMismatch_ReturnsEmptyArray() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        ContainsExtraTypeInformation suspiciousMatcher = new ContainsExtraTypeInformation(String.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(suspiciousMatcher);
        
        Object[] arguments = new Object[1];
        arguments[0] = "test";
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        MatcherAssert.assertThat(result, Matchers.arrayWithSize(0));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatcherMatchesAndToStringDoesNotEqual_ReturnsSuspiciousArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        ContainsExtraTypeInformation suspiciousMatcher = new ContainsExtraTypeInformation(String.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(suspiciousMatcher);
        
        Object[] arguments = new Object[1];
        arguments[0] = "not the same";
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        MatcherAssert.assertThat(result, Matchers.arrayContaining(0));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatcherMatchesAndToStringEquals_ReturnsEmptyArray() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        ContainsExtraTypeInformation suspiciousMatcher = new ContainsExtraTypeInformation(String.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(suspiciousMatcher);
        
        Object[] arguments = new Object[1];
        arguments[0] = "test";
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        MatcherAssert.assertThat(result, Matchers.arrayWithSize(0));
    }
}