import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ContainsExtraTypeInformation("string", "expected"));
        matchers.add(new ContainsExtraTypeInformation("integer", "actual"));
        Object[] arguments = new Object[]{"string", "expected", new Integer(1)};

        Integer[] expectedIndexes = new Integer[]{1};
        Integer[] actualIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expectedIndexes, actualIndexes);
    }
}