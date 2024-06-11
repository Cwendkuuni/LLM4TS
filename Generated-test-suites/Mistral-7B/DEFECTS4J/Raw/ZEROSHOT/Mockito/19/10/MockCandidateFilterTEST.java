package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.answers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_nullCollection_returnNull() {
        Collection<Object> mocks = null;

        assertSame(null, filter.filterCandidate(mocks, new Field("fieldName", Object.class), fieldInstance));
    }

    @Test
    public void testFilterCandidate_emptyCollection_returnSameInstance() {
        Collection<Object> mocks = new ArrayList<>();

        assertSame(fieldInstance, filter.filterCandidate(mocks, new Field("fieldName", Object.class), fieldInstance));
    }

    @Test
    public void testFilterCandidate_replaceMockedInstanceWithSameClass_returnSameInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Object mockedInstance = mock(Object.class);
        Mockito.doAnswer(answers.returns(mockedInstance)).when(filter).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));

        Object replacementInstance = spy(fieldInstance);

        assertSame(mockedInstance, filter.filterCandidate(mocks, new Field("fieldName", Object.class), replacementInstance));
        verify(filter).filterCandidate(mocks, new Field("fieldName", Object.class), replacementInstance);
    }

    @Test
    public void testFilterCandidate_replaceMockedInstanceWithDifferentClass_returnNewInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Object mockedInstance = mock(Object.class);
        Object replacementInstance = mock(AnotherClass.class);

        Mockito.doAnswer(invocation -> {
            Object current = invocation.getArgument(2);
            if (current.getClass() == fieldInstance.getClass()) {
                return mockedInstance;
            } else {
                return replacementInstance;
            }
        }).when(filter).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));

        assertSame(mockedInstance, filter.filterCandidate(mocks, new Field("fieldName", Object.class), fieldInstance));
        assertSame(replacementInstance, filter.filterCandidate(mocks, new Field("fieldName", Object.class), Mockito.spy(AnotherClass.class)));
    }

    @Test(expected = IllegalAccessException.class)
    public void testFilterCandidate_throwIllegalAccessException_whenAccessingPrivateField() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("fieldName", Object.class);
        field.setAccessible(true);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    private static class Field {
        private String name;
        private Class<?> type;

        public Field(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        public void setAccessible(boolean accessible) {
            // do nothing
        }
    }
}