import org.junit.Before;
import org.junit.Test;
import org.mockito.ExactAnswer;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.Runners;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object target;
    private Field targetField;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;

    @Before
    public void setup() {
        target = new Object();
        targetField = new Field("testField", String.class, target.getClass());
        fieldInstance = mock(String.class);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);

        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testSingleMock() {
        List<Object> mocks = Arrays.asList(mock1);

        when(mock1.getClass()).thenReturn(target.getClass());
        when(mock1).thenAnswer(new ExactAnswer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object value = invocation.getArgument(0);
                Field field = invocation.getMethod().getParameterAnnotations()[0].annotationType();
                when(value.getClass()).thenReturn(field.getType());
                return fieldInstance;
            }
        });

        OngoingInjecter result = filter.filterCandidate(mocks, targetField, target);

        result.thenInject();

        verify(mock1).setField(targetField, fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testSingleMockException() {
        List<Object> mocks = Arrays.asList(mock1);

        when(mock1.getClass()).thenReturn(target.getClass());
        when(mock1).thenAnswer(new ExactAnswer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object value = invocation.getArgument(0);
                Field field = invocation.getMethod().getParameterAnnotations()[0].annotationType();
                when(value.getClass()).thenReturn(field.getType());
                throw new RuntimeException("Test exception");
            }
        });

        OngoingInjecter result = filter.filterCandidate(mocks, targetField, target);

        result.thenInject();
    }

    @Test
    public void testMultipleMocks() {
        List<Object> mocks = Arrays.asList(mock1, mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, targetField, target);

        assert (result.thenInject() == false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyMocks() {
        List<Object> mocks = Collections.emptyList();

        OngoingInjecter result = filter.filterCandidate(mocks, targetField, target);

        // This should not be reachable as the test method should throw an
        // UnsupportedOperationException before this point
        result.thenInject();
    }
}