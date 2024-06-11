import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = getFieldFromClass();
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_singleMock_successfullyInjected() {
        // Arrange
        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        when(mocks.size()).thenReturn(1);
        Object matchingMock = new Object();
        when(mocks.iterator().next()).thenReturn(matchingMock);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        doNothing().when(fieldSetter).set(matchingMock);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);

        // Act
        boolean result = ongoingInjecter.thenInject();

        // Assert
        verify(mocks, times(1)).size();
        verify(mocks, times(1)).iterator().next();
        verifyNew(FieldSetter.class).withArguments(fieldInstance, field);
        verify(fieldSetter, times(1)).set(matchingMock);
        assertEquals(true, result);
    }

    @Test
    public void testFilterCandidate_multipleMocks_noInjection() {
        // Arrange
        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        int mockSize = 2;
        when(mocks.size()).thenReturn(mockSize);

        // Act
        boolean result = ongoingInjecter.thenInject();

        // Assert
        verify(mocks, times(1)).size();
        assertEquals(false, result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_singleMock_exceptionThrown() throws Exception {
        // Arrange
        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        when(mocks.size()).thenReturn(1);
        Object matchingMock = new Object();
        when(mocks.iterator().next()).thenReturn(matchingMock);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        doThrow(new Exception()).when(fieldSetter).set(matchingMock);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);

        // Act
        ongoingInjecter.thenInject();
    }

    private Field getFieldFromClass() {
        Field[] fields = FinalMockCandidateFilter.class.getDeclaredFields();
        Field field = null;
        for (Field f : fields) {
            if (f.getName() == "fieldInstance") {
                field = f;
                break;
            }
        }
        return field;
    }

}