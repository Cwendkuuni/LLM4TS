import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter next;

    @Before
    public void setUp() {
        next = spy(Mockito.doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) {
                return null;
            }
        }).mock(MockCandidateFilter.class));

        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testEmptyMocks_NullResult() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        verify(next, Mockito.never()).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
    }

    @Test
    public void testInvalidField_NullResult() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = null;
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        verify(next, Mockito.never()).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
    }

    @Test
    public void testValidFieldsAndMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(ClassThatImplementsType.class));
        Field field = Mockito.field(ClassWithField.class, "myField");
        Object fieldInstance = new ClassWithField();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        // Assert that next filter is called with the correct arguments
        // and that the correct mock is returned.
    }

    @Test
    public void testInvalidMockType_NullResult() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(ClassThatDoesntImplementType.class));
        Field field = Mockito.field(ClassWithField.class, "myField");
        Object fieldInstance = new ClassWithField();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        verify(next, Mockito.never()).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
    }

    @Test
    public void testNullMocks_NullResult() {
        Field field = Mockito.field(ClassWithField.class, "myField");
        Object fieldInstance = new ClassWithField();

        Collection<Object> mocks = null;

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        verify(next, Mockito.never()).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
    }

}

class ClassWithField {
    // a simple class with a field
}

class ClassThatImplementsType {
    // a mock class that implements the type of the field
}

class ClassThatDoesntImplementType {
    // a mock class that doesn't implement the type of the field
}