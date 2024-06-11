import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = createMock();
        mocks.add(mock);

        Field field = getField("mockField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(result instanceof OngoingInjecter);
        Stubber stubber = createNiceMock(mock.getClass());
        stubber.expects(new InvocationOnMock(field)).proceed();
        result.thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                stubber.verify();
                return null;
            }
        });
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = createMock();
        Object mock2 = createMock();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = getField("mockField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertFalse(result instanceof OngoingInjecter);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithExceptionWhenOneMock() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = createMock();
        mocks.add(mock);

        Field field = getField("mockField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        new FieldSetter(fieldInstance, field).set(mock);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithExceptionWhenMultipleMocks() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = createMock();
        Object mock2 = createMock();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = getField("mockField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        new FieldSetter(fieldInstance, field).set(mock1);
    }

    private Object createMock() {
        Stubber stubber = createNiceMock(Object.class);
        return stubber.mock(Object.class);
    }

    private Field getField(String name) {
        try {
            return Class.forName("java.lang.reflect.Field").getField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}