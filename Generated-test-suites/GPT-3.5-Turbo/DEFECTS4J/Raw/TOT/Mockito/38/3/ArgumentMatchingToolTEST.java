import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_typicalUseCase() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.any());
        matchers.add(Matchers.equalTo("test"));
        matchers.add(Matchers.not(Matchers.equalTo(5)));

        Object[] arguments = new Object[]{"Hello", "test", 6};

        Integer[] expectedIndexes = new Integer[]{2};

        Integer[] resultIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expectedIndexes, resultIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_edgeCase_AllMatchersNull() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(null);
        matchers.add(null);
        matchers.add(null);

        Object[] arguments = new Object[]{"Hello", "test", 6};

        Integer[] expectedIndexes = new Integer[]{0, 1, 2};

        Integer[] resultIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expectedIndexes, resultIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_errorScenario_DifferentMatcherAndArgumentTypes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.instanceOf(Integer.class));
        matchers.add(Matchers.any());
        matchers.add(Matchers.instanceOf(Float.class));

        Object[] arguments = new Object[]{"Hello", "test", 6};

        Integer[] expectedIndexes = new Integer[]{0, 2};

        Integer[] resultIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expectedIndexes, resultIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_errorScenario_ExceptionDuringMatching() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.any());
        matchers.add(Matchers.equalTo("test"));
        matchers.add(Matchers.not(Matchers.equalTo(5)));

        Object[] arguments = new Object[]{"Hello", "test", 6};

        Integer[] expectedIndexes = new Integer[]{};

        Integer[] resultIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null); // Exception: arguments is null

        Assert.assertArrayEquals(expectedIndexes, resultIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_edgeCase_EmptyData() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{};

        Integer[] expectedIndexes = new Integer[]{};

        Integer[] resultIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expectedIndexes, resultIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_edgeCase_DifferentSizeList() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.any());
        matchers.add(Matchers.any());

        Object[] arguments = new Object[]{"Hello", "test", 6};

        Integer[] expectedIndexes = new Integer[]{};

        Integer[] resultIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expectedIndexes, resultIndexes);
    }

}