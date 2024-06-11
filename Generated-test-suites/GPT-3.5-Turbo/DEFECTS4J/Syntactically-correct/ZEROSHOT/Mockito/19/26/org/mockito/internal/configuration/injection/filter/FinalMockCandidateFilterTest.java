import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = new Field();
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(ongoingInjecter);
        Assert.assertEquals(mocks.get(0), ongoingInjecter.thenInject());
        verifyNoInteractions(ongoingInjecter);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class, RETURNS_DEEP_STUBS));
        Field field = new Field();
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(ongoingInjecter);
        Assert.assertNull(ongoingInjecter.thenInject());
        verifyNoInteractions(ongoingInjecter);
    }
}