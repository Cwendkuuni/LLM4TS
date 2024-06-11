import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Equals;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.hamcrest.Matcher;
import org.mockito.internal.debugging.Location;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        List<Matcher> matchers = Collections.singletonList(new Equals("input"));
        
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        Method method = invocation.getMethod();
        
        // Assert
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        // Arrange
        PrintSettings printSettings = new PrintSettings();
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        String result = invocationMatcher.toString(printSettings);
        
        // Assert
        assertEquals(invocation.toString(invocationMatcher.getMatchers(), printSettings), result);
    }

    @Test
    public void testMatches_WhenMatchingInvocation_ThenReturnTrue() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new Equals("input")));
        Invocation actualInvocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        
        // Act
        boolean result = invocationMatcher.matches(actualInvocation);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_WhenNotMatchingInvocation_ThenReturnFalse() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(new Equals("input")));
        Invocation actualInvocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"differentInput"});
        
        // Act
        boolean result = invocationMatcher.matches(actualInvocation);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_WhenSimilarMethod_ThenReturnTrue() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"differentInput"});
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WhenMethodOverloadedWithDifferentArgs_ThenReturnFalse() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"differentInput"});
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_WhenMethodOverloadedWithSameArgs_ThenReturnTrue() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation("mockObject", "differentMethodName", new Class[]{String.class}, new Object[]{"input"});
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_WhenSameMethod_ThenReturnTrue() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        
        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_WhenDifferentMethod_ThenReturnFalse() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = new Invocation("mockObject", "differentMethodName", new Class[]{String.class}, new Object[]{"input"});
        
        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(new Equals("input")));
        Invocation i = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        
        // Act
        invocationMatcher.captureArgumentsFrom(i);
        
        // Assert
        assertEquals("input", ((Equals) invocationMatcher.getMatchers().get(0)).getExpected());
    }
    
    @Test
    public void testGetLocation() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        Location location = invocation.getLocation();
        
        // Assert
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToString_PrintingFriendlyInvocation() {
        // Arrange
        PrintSettings printSettings = new PrintSettings();
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String result = invocationMatcher.toString(printSettings);

        // Assert
        PrintableInvocation printableInvocation = (PrintableInvocation) invocationMatcher;
        assertEquals(invocation.toString(invocationMatcher.getMatchers(), printSettings), printableInvocation.toString(printSettings));
    }

    @Test
    public void testSafelyArgumentsMatch_WhenArgumentsMatch_ThenReturnTrue() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] actualArgs = new Object[]{"input"};
        
        // Act
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch_WhenArgumentsDoNotMatch_ThenReturnFalse() {
        // Arrange
        Invocation invocation = new Invocation("mockObject", "methodName", new Class[]{String.class}, new Object[]{"input"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] actualArgs = new Object[]{"differentInput"};
        
        // Act
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        
        // Assert
        assertFalse(result);
    }
}