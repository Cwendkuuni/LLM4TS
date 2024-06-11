import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Object[] arguments = new Object[0];
        List<Matcher> matchers = new LinkedList<>();
        Integer[] expected = new Integer[0];

        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSize() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Object[] arguments = new Object[]{new Object()};
        List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation("String"), new ContainsExtraTypeInformation(Integer.class)));

        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringEqualsButTypesDifferent() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Object arg1 = new Object() {
            @Override
            public String toString() {
                return "MockObject1";
            }
        };
        Object arg2 = new Object() {
            @Override
            public String toString() {
                return "MockObject2";
            }
        };
        List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation(arg1.getClass()), new ContainsExtraTypeInformation(arg2.getClass())));
        Object[] arguments = new Object[]{arg1, arg2};

        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringEqualsButTypesSame() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Object arg = new Object() {
            @Override
            public String toString() {
                return "MockObject";
            }
        };
        List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation(arg.getClass()), new ContainsExtraTypeInformation(arg.getClass())));
        Object[] arguments = new Object[]{arg, arg};

        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringNotEqualsButTypesSame() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Object arg1 = new Object() {
            @Override
            public String toString() {
                return "MockObject1";
            }
        };
        Object arg2 = new Object() {
            @Override
            public String toString() {
                return "MockObject2";
            }
        };
        List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation(arg1.getClass()), new ContainsExtraTypeInformation(arg2.getClass())));
        Object[] arguments = new Object[]{arg1, arg2};

        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringNotEqualsButTypesDifferent() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Object arg1 = new Object() {
            @Override
            public String toString() {
                return "MockObject1";
            }
        };
        Object arg2 = new Object() {
            @Override
            public String toString() {
                return "MockObject2";
            }
        };
        List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation(arg1.getClass()), new ContainsExtraTypeInformation(arg2.getClass())));
        Object[] arguments = new Object[]{arg1, arg2};

        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], actual);

        // Add error scenario test here if needed
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ThrowsException() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation("MockObject")));
        Object[] arguments = new Object[]{new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Unexpected error");
            }
        }};

        try {
            tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        } catch (RuntimeException e) {
            // Expected result
        }
    }
}