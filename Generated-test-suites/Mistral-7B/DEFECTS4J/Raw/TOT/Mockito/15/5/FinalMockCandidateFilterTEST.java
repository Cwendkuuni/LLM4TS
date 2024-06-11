import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    @Rule
    public MockitoJUnit mockitoRule = new MockitoJUnit();

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private Field field;

    @Before
    public void setUp() {
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        fieldInstance = mock(Object.class);
        field = Mockito.spy(Field.class);

        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithSingleMock() throws Exception {
        whenFieldSetterAccessible(field);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        actualInjecter.thenInject();

        verify(mock1).equals(fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(actualInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithException() throws Exception {
        whenFieldSetterAccessible(field);

        doThrow(new Exception("Test Exception")).when(field).set(ArgumentMatchers.any(), ArgumentMatchers.any());

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        actualInjecter.thenInject();
    }

    private void whenFieldSetterAccessible(Field field) throws IllegalAccessException {
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                fieldSetter.set(invocation.getArguments()[0]);
                return null;
            }
        }).when(field).set(ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}