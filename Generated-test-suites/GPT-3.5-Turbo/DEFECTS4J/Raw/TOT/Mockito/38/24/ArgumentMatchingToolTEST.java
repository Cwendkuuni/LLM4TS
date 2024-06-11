import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_TypicalCase() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(equalTo(1));
        matchers.add(equalTo("test"));
        matchers.add(instanceOf(Double.class));
        Object[] arguments = {1, "test", 3.14};
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertThat(result.length, is(equalTo(0)));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSize() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(equalTo(1));
        matchers.add(equalTo("test"));
        Object[] arguments = {1, "test", "extra"};
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertThat(result.length, is(equalTo(0)));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NotMatchingButToStringEqualsAndTypeMatches() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(containsString("test"));
        Object[] arguments = {"test"};
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertThat(result.length, is(equalTo(0)));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatches() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(instanceOf(String.class));
        Object[] arguments = {"test"};
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertThat(result.length, is(equalTo(1)));
        assertThat(result[0], is(equalTo(0)));
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherThrowsException() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        Object[] arguments = {"test"};
        when(matchers.get(0).matches(arguments[0])).thenThrow(RuntimeException.class);
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertThat(result.length, is(equalTo(0)));
    }
}