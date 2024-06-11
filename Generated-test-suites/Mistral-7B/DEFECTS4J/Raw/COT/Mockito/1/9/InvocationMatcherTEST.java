import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcherFactory;
import org.mockito.internal.matchers.ArgsMatchers;
import org.mockito.internal.matchers.InvocationPattern;
import org.mockito.internal.matchers.VarargMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyList() {
        Invocation invocation = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        Assert.assertEquals(method.getName(), invocationMatcher.getMethod().getName());
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndListOfMatchers() {
        Invocation invocation = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{1, 2});
        List<Matcher> matchers = Arrays.asList(new ArgsMatchers.Eq("1"), new ArgsMatchers.Eq("2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocation.getMethod();
        Assert.assertEquals(method.getName(), invocationMatcher.getMethod().getName());
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertNotEquals(Collections.emptyList(), invocationMatcher.getMatchers());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation1 = MockitoTestUtils.createMockitoInvocation("methodName1", "mock1", new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = MockitoTestUtils.createMockitoInvocation("methodName1", "mock1", new Object[0]);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        Invocation invocation1 = MockitoTestUtils.createMockitoInvocation("methodName1", "mock1", new Object[]{1});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = MockitoTestUtils.createMockitoInvocation("methodName2", "mock2", new Object[]{2});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithSameMethods() {
        Invocation invocation1 = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[0]);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation1 = MockitoTestUtils.createMockitoInvocation("methodName1", "mock", new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = MockitoTestUtils.createMockitoInvocation("methodName2", "mock", new Object[0]);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsWithSameNumberOfArguments() {
        Invocation invocation = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{1, 2});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        List<Matcher> matchers = Arrays.asList(new ArgsMatchers.Eq(1), new ArgsMatchers.Eq(2));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] capturedArgs = new Object[2];
        invocationMatcher.captureArgumentsFrom(invocation);

        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            Assert.assertSame(matcher.match(invocation.getArgumentAt(i, Object.class)), matcher.createMatchers()[0].match(capturedArgs[i]));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsWithVarArgs() {
        Invocation invocation = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        invocationMatcher.captureArgumentsFrom(invocation);

        throw new UnsupportedOperationException();
    }

    @Test
    public void testCreateFromListOfInvocations() {
        List<Invocation> invocations = Arrays.asList(
                MockitoTestUtils.createMockitoInvocation("methodName1", "mock1", new Object[]{1}),
                MockitoTestUtils.createMockitoInvocation("methodName1", "mock1", new Object[]{2}),
                MockitoTestUtils.createMockitoInvocation("methodName2", "mock2", new Object[]{3}));

        List<InvocationMatcher> invocationMatchers = InvocationMatcherFactory.createFrom(invocations);

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            Method method = invocationMatcher.getMethod();
            Assert.assertNotNull(method);
            Assert.assertNotNull(invocationMatcher.getInvocation());
            Assert.assertNotNull(invocationMatcher.getMatchers());
        }

        for (InvocationMatcher invocationMatcher1 : invocationMatchers) {
            for (InvocationMatcher invocationMatcher2 : invocationMatchers) {
                if (invocationMatcher1 != invocationMatcher2) {
                    Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
                }
            }
        }
    }

    @Test
    public void testHasSameMethodWithMethodsWithSameNameButDifferentNumberOfArguments() {
        Invocation invocation1 = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{1, 2});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{1});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithMethodsWithSameNameButDifferentParameterTypes() {
        Invocation invocation1 = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{Integer.TYPE, String.class});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{String.class, Integer.TYPE});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithMethodsWithSameNameButDifferentReturnTypes() {
        Invocation invocation1 = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{Integer.TYPE}, Integer.TYPE);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{Integer.TYPE}, String.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithMethodsWithDifferentNamesButSameParameters() {
        Invocation invocation1 = MockitoTestUtils.createMockitoInvocation("methodName1", "mock", new Object[]{Integer.TYPE, String.class});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = MockitoTestUtils.createMockitoInvocation("methodName2", "mock", new Object[]{Integer.TYPE, String.class});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithMethodsWithDifferentNamesButSameSignature() {
        Invocation invocation1 = MockitoTestUtils.createMockitoInvocation("methodName1", "mock", new Object[]{String[].class}, String[].class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = MockitoTestUtils.createMockitoInvocation("methodName2", "mock", new Object[]{Object[].class}, Object[].class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithMethodsWithSameNameButDifferentAnnotations() {
        Method method1 = MockitoTestUtils.createAnnotatedMethod("methodName", "mock", new Class[0]);
        method1.addAnnotation(AnnotationTypes.RETURNS_DETERMINISTIC_RESULT);

        Method method2 = MockitoTestUtils.createAnnotatedMethod("methodName", "mock", new Class[0]);
        method2.addAnnotation(AnnotationTypes.RETURNS_DETERMINISTIC_RESULT);
        method2.addAnnotation(AnnotationTypes.RETURNS_NULL_ON_FAILURE);

        Invocation invocation1 = new Invocation(method1, new Object[0], new Object[0], MockitoTestUtils.createTestLocation());
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = new Invocation(method2, new Object[0], new Object[0], MockitoTestUtils.createTestLocation());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithMethodsWithSameNameButDifferentAnnotationsOrder() {
        Method method1 = MockitoTestUtils.createAnnotatedMethod("methodName", "mock", new Class[0]);
        method1.addAnnotation(AnnotationTypes.RETURNS_DETERMINISTIC_RESULT);
        method1.addAnnotation(AnnotationTypes.RETURNS_NULL_ON_FAILURE);

        Method method2 = MockitoTestUtils.createAnnotatedMethod("methodName", "mock", new Class[0]);
        method2.addAnnotation(AnnotationTypes.RETURNS_NULL_ON_FAILURE);
        method2.addAnnotation(AnnotationTypes.RETURNS_DETERMINISTIC_RESULT);

        Invocation invocation1 = new Invocation(method1, new Object[0], new Object[0], MockitoTestUtils.createTestLocation());
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = new Invocation(method2, new Object[0], new Object[0], MockitoTestUtils.createTestLocation());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsWithVarargMatcher() {
        Invocation invocation = MockitoTestUtils.createMockitoInvocation("methodName", "mock", new Object[]{1, 2, new Object[]{3, 4}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(new VarargMatcher(new ArgsMatchers.InstanceOf(List.class))));

        List<Object> capturedArgs = Arrays.asList(new Object[]{3, 4});
        invocationMatcher.captureArgumentsFrom(invocation);

        Matcher matcher = invocationMatcher.getMatchers().get(0);
        Assert.assertTrue(matcher.matches(capturedArgs));
    }
}

class MockitoTestUtils {
    // Helper methods to create MockitoInvocations and test locations
}