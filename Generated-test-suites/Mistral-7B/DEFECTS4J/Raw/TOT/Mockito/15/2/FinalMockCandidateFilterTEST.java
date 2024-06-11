
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object mock1;
    private Object mock2;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        mock1 = Mockito.spy(new Object());
        mock2 = Mockito.spy(new Object());
        fieldInstance = new Object();
        field = Mockito.spy(new Field("testField", Object.class, fieldInstance));
    }

    @Test
    public void testSingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertTrue(injecter instanceof OngoingInjecter);
        assertEquals(mock1, injecter.getTarget());
    }

    @Test
    public void testMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testExceptionWhenInjecting() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        new FieldSetter(fieldInstance, field).set(mock1);
        FilterCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());

        injecter.thenInject();
    }

    @Test
    public void testEmptyMocksCollection() {
        Collection<Object> mocks = new ArrayList<>();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test(expected = NullPointerException.class)
    public void testNullField() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        Field field = null;

        FilterCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
    }
}
