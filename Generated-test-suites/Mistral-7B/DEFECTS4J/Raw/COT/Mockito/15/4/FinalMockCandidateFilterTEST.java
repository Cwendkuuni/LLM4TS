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
    public void testFilterCandidateWithSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = this.createMock();
        mocks.add(mock);

        Field field = getPrivateField(mock, "field");
        Object fieldInstance = mock;

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithSingleMockExceptionThrown() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = this.createMock();
        mocks.add(mock);

        Field field = getPrivateField(mock, "field");
        Object fieldInstance = mock;

        Stubber stubber = mock(FieldSetter.class);
        doThrow(new RuntimeException("Exception thrown")).when(stubber).set(fieldInstance, field);

        FieldSetter fieldSetter = stubber.createMock(FieldSetter.class);
        doReturn(fieldSetter).when(filter).newFieldSetter(fieldInstance, field);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = this.createMock();
        Object mock2 = this.createMock();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = getPrivateField(mock1, "field");
        Object fieldInstance = mock1;

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertFalse(injecter.thenInject());
    }

    private Object createMock() {
        Object mock = mock(Object.class);
        Answer<Void> answer = invocation -> {
            // Do something when mock is invoked
            return null;
        };

        when(mock).thenAnswer(answer);
        return mock;
    }

    private Field getPrivateField(Object object, String fieldName) {
        Field field = null;

        try {
            field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return field;
    }

}