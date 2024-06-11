import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_WithOneMock_ReturnOngoingInjecterWithThenInjectTrue() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        Field field = getFieldInstance();
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertTrue(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_WithMultipleMocks_ReturnOngoingInjecterWithThenInjectFalse() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = getFieldInstance();
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertFalse(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_WithOneMock_SetMatchingMockWithFieldSetter() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object matchingMock = new Object();
        mocks.add(matchingMock);
        Field field = getFieldInstance();
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject();

        // Assert
        try {
            FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
            assertEquals(matchingMock, fieldSetter.getFieldInstance());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_WithOneMock_SetMatchingMockWithFieldSetter_ThrowsMockitoException() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object matchingMock = new Object();
        mocks.add(matchingMock);
        Field field = getFieldInstance();
        Object fieldInstance = new Object();
        Exception expectedException = new Exception("injecting dependency failed");

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.setExceptionToThrow(expectedException);

        ongoingInjecter.thenInject();
    }

    private Field getFieldInstance() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("mocks");
        } catch (NoSuchFieldException e) {
            fail("Field not found");
            return null;
        }
    }

}