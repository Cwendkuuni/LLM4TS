import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Object mock1;
    private Object mock2;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        mock1 = Mockito.mock(String.class);
        mock2 = Mockito.mock(String.class);
        fieldInstance = new Object();
        field = Mockito.spy(Field.class);
    }

    @Test
    public void testSingleMock() {
        Collection<Object> mocks = Arrays.asList(mock1);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mock1, injecter.getInjectedObject());
    }

    @Test(expected = MockitoException.class)
    public void testSingleMockException() {
        Collection<Object> mocks = Arrays.asList(mock1);

        try {
            new FieldSetter(fieldInstance, field).set(mock1);
        } catch (Exception e) {
            throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
        }

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testMultipleMocks() {
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(null, injecter.getInjectedObject());
    }
}