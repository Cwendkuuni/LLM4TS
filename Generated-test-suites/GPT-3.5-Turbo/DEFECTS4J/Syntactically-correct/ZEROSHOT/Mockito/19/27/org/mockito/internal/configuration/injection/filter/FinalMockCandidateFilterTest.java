import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
        mocks = Collections.singleton(mock(Object.class));
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_singleCandidate() {
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);

        when(fieldInstance.toString()).thenReturn("FieldInstance");
        when(field.toString()).thenReturn("Field");

        ongoingInjecter.thenInject();

        // Verify that BeanPropertySetter.set() was called
        BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
        assertTrue(beanPropertySetter.set(mocks.iterator().next()));

        // Verify that FieldSetter.set() was not called
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        assertFalse(fieldSetter.set(mocks.iterator().next()));
    }

    @Test
    public void testFilterCandidate_multipleCandidates() {
        mocks = Collections.emptySet();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);
        assertNull(ongoingInjecter.thenInject());
    }
}