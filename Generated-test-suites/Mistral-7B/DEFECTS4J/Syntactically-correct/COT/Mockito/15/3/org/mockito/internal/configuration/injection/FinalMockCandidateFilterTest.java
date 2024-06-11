import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Object mock = mock(Object.class);
        Object fieldInstance = new Object();
        Field field = new Field(String.class, "testField");

        OngoingInjecter expectedResult = new OngoingInjecter() {
            @Override
            public boolean thenInject() {
                try {
                    new FieldSetter(fieldInstance, field).set(mock);
                    return true;
                } catch (Exception e) {
                    throw new MockitoException("Problems injecting dependency in testField", e);
                }
            }
        };

        OngoingInjecter result = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithOneMockException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Object mock = mock(Object.class);
        Object fieldInstance = new Object();
        Field field = new Field("testField", String.class);

        Answer answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                throw new Exception("Test exception");
            }
        };

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.setAnswer(answer);

        try {
            filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);
        } catch (MockitoException e) {
            Assert.assertEquals("Problems injecting dependency in testField", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = new Object();
        Field field = new Field("testField", String.class);

        OngoingInjecter result = filter.filterCandidate(List.of(mock1, mock2), field, fieldInstance);

        Assert.assertFalse(result.thenInject());
    }

}