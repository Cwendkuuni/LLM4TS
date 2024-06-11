import static org.junit.Assert.assertArrayEquals;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new java.util.ArrayList<>();
        matchers.add(Matchers.is(new Integer(1)));
        matchers.add(Matchers.is(new Integer(2)));
        matchers.add(Matchers.is(new String("three")));
        matchers.add(Matchers.is(new Integer(4)));

        Object[] arguments = new Object[]{new Integer(1), new Integer(2), new String("three"), new Integer(4)};

        Integer[] expectedIndexes = new Integer[]{2};
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expectedIndexes, actualIndexes);
    }
}