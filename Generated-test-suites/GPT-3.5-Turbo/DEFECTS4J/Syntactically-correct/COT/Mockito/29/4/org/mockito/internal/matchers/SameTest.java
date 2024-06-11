import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Same;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Mock
    Description description;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMatches() {
        Same same = new Same(5);
        assertTrue(same.matches(5));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        same.describeTo(description);
        Mockito.verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testDescribeToWithCharacter() {
        Same same = new Same('c');
        same.describeTo(description);
        Mockito.verify(description).appendText("same('c')");
    }

}