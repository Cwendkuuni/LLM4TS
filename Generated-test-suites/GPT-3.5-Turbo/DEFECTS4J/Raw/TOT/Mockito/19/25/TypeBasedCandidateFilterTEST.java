import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    @Test
    public void filterCandidate_shouldCallNextFilterWithValidMock() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        ClassA mockA = mock(ClassA.class);
        ClassB mockB = mock(ClassB.class);
        ClassC mockC = mock(ClassC.class);
        mocks.add(mockA);
        mocks.add(mockB);
        mocks.add(mockC);

        Field field = MyClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(any(List.class), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_shouldOnlyPassMocksOfTypeA() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        ClassA mockA = mock(ClassA.class);
        ClassB mockB = mock(ClassB.class);
        ClassC mockC = mock(ClassC.class);
        mocks.add(mockA);
        mocks.add(mockB);
        mocks.add(mockC);

        Field field = MyClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(filteredListCapture.capture(), eq(field), eq(fieldInstance));
        List<Object> filteredList = filteredListCapture.getValue();
        for (Object mock : filteredList) {
            assertSame(ClassA.class, mock.getClass()); // Only mocks of type A should be passed
        }
    }

    @Test
    public void filterCandidate_shouldNotPassAnyMocksIfNoMocksOfTypeA() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        ClassB mockB = mock(ClassB.class);
        ClassC mockC = mock(ClassC.class);
        mocks.add(mockB);
        mocks.add(mockC);

        Field field = MyClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(filteredListCapture.capture(), eq(field), eq(fieldInstance));
        List<Object> filteredList = filteredListCapture.getValue();
        assertEquals(0, filteredList.size()); // No mocks of type A, should not pass any mocks
    }

    @Test
    public void filterCandidate_shouldPassAllMocksIfAllMocksAreOfTypeA() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        ClassA mockA1 = mock(ClassA.class);
        ClassA mockA2 = mock(ClassA.class);
        ClassA mockA3 = mock(ClassA.class);
        mocks.add(mockA1);
        mocks.add(mockA2);
        mocks.add(mockA3);

        Field field = MyClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(filteredListCapture.capture(), eq(field), eq(fieldInstance));
        List<Object> filteredList = filteredListCapture.getValue();
        assertEquals(3, filteredList.size()); // All mocks are of type A, should pass all mocks
    }

    @Test(expected = NoSuchFieldException.class)
    public void filterCandidate_shouldThrowExceptionIfFieldNotFound() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        ClassA mockA = mock(ClassA.class);
        mocks.add(mockA);

        Field field = MyClass.class.getDeclaredField("nonExistentField");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        // Expecting NoSuchFieldException to be thrown
    }

    @Test
    public void filterCandidate_shouldCallNextFilterWithEmptyMocksIfInitialMocksIsEmpty() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        Field field = MyClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(Collections.emptyList(), eq(field), eq(fieldInstance));
    }
}

interface MockCandidateFilter {
    void filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance);
}

class MyClass {
    private Object field;
}

class ClassA {}
class ClassB {}
class ClassC {}