import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private Object mock;

    @Before
    public void setUp() {
        mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
        fieldInstance = new Object();
        field = new Field("testField", Object.class) {
            @Override
            public Object get(Object obj) {
                return null;
            }

            @Override
            public void set(Object obj, Object value) {
                // empty implementation
            }
        };
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate_SingleMock_SuccessfulInjection() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(injecter.thenInject());
        try {
            new FieldSetter(fieldInstance, field).get(mock);
            assertTrue(true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}