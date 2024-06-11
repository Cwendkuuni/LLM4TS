import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void init() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = MockUtil.getField(MockitoStub.class, "fieldToMock");
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_OneMock_SingleSetter() {
        Object mock = mock(MockCandidateFilter.class);
        mocks.add(mock);

        Stub<Object> stub = mock(Object.class);
        when(mock.filterCandidate(mocks, field, fieldInstance)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object result = invocation.callRealMethod();
                BeanPropertySetter.set(fieldInstance, field, stub.getMock());
                return stub.getMock();
            }
        });

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        verify(mock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_OneMock_SingleFieldAccessFailure() {
        Object mock = mock(MockCandidateFilter.class);
        mocks.add(mock);

        when(mock.filterCandidate(mocks, field, fieldInstance)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Field access failed");
            }
        });

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_OneMock_SinglePropertySetterFailure() {
        Object mock = mock(MockCandidateFilter.class);
        mocks.add(mock);

        when(mock.filterCandidate(mocks, field, fieldInstance)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                try {
                    BeanPropertySetter.set(fieldInstance, field, invocation.getArguments()[2]);
                } catch (RuntimeException e) {
                    throw e;
                }
                return null;
            }
        });

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatchingMock() {
        Object mock1 = mock(MockCandidateFilter.class);
        Object mock2 = mock(MockCandidateFilter.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertNotNull(result);
    }
}