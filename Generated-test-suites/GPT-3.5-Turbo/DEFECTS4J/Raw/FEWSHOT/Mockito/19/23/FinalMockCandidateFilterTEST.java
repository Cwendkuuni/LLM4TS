import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidateWithSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        try {
            assertEquals(mock, result);
            Mockito.verify(fieldInstance, Mockito.times(1)).set(mock);
            Mockito.verifyZeroInteractions(new BeanPropertySetter(fieldInstance, field));
            Mockito.verifyZeroInteractions(new Reporter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        try {
            assertNull(result);
            Mockito.verifyZeroInteractions(fieldInstance);
            Mockito.verifyZeroInteractions(new BeanPropertySetter(fieldInstance, field));
            Mockito.verifyZeroInteractions(new Reporter());
        } catch (Exception e) {
            e. printStackTrace();
        }
    }
}