package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        Collection<Object> mocks = null;

        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                throw new IllegalArgumentException("Collection cannot be null");
            }
        });

        try {
            mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        Field fieldToBeInjected = null;

        Object fieldInstance = mock(Object.class);

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                throw new IllegalArgumentException("Field cannot be null");
            }
        });

        try {
            mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        Field fieldToBeInjected = Mockito.mock(Field.class);

        Object fieldInstance = null;

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                throw new IllegalArgumentException("Field instance cannot be null");
            }
        });

        try {
            mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();

        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(Mockito.mock(OngoingInjecter.class));

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        verifyNoMoreInteractions(mockCandidateFilter);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_SingleObject() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object mockMockitoObject = mock(Object.class);
        when(mocks.get(0)).thenReturn(mockMockitoObject);

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                        Stub stub = Mockito.mock(OngoingInjecter.class);
                        when(stub.inject(fieldInstance)).thenAnswer(new Answer<Void>() {
                            @Override
                            public Void answer(InvocationOnMock invocation) throws Throwable {
                                Object injectedObject = invocation.getArgument(0);
                                when(mockMockitoObject.getClass().getDeclaredField(fieldToBeInjected.getName()))
                                        .thenReturn(fieldToBeInjected);
                                fieldToBeInjected.setAccessible(true);
                                fieldToBeInjected.set(mockMockitoObject, injectedObject);
                                return null;
                            }
                        });
                        return stub;
                    }
                });

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        verify(mockMockitoObject).getClass().getDeclaredField(fieldToBeInjected.getName());
        verify(mockMockitoObject).getClass().getDeclaredField(fieldToBeInjected.getName()).setAccessible(true);
        verify(mockMockitoObject).getClass().getDeclaredField(fieldToBeInjected.getName()).set(Mockito.eq(mockMockitoObject), Mockito.any());
        verify(result).inject(Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleObjects() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                        Stub stub1 = Mockito.mock(OngoingInjecter.class);
                        when(stub1.inject(fieldInstance)).thenAnswer(new Answer<Void>() {
                            @Override
                            public Void answer(InvocationOnMock invocation) throws Throwable {
                                Object injectedObject = invocation.getArgument(0);
                                when(mocks.get(0).getClass().getDeclaredField(fieldToBeInjected.getName()))
                                        .thenReturn(fieldToBeInjected);
                                fieldToBeInjected.setAccessible(true);
                                fieldToBeInjected.set(mocks.get(0), injectedObject);
                                return null;
                            }
                        });
                        Stub stub2 = Mockito.mock(OngoingInjecter.class);
                        when(stub2.inject(fieldInstance)).thenAnswer(new Answer<Void>() {
                            @Override
                            public Void answer(InvocationOnMock invocation) throws Throwable {
                                Object injectedObject = invocation.getArgument(0);
                                when(mocks.get(1).getClass().getDeclaredField(fieldToBeInjected.getName()))
                                        .thenReturn(fieldToBeInjected);
                                fieldToBeInjected.setAccessible(true);
                                fieldToBeInjected.set(mocks.get(1), injectedObject);
                                return null;
                            }
                        });
                        return new Mockito.mock(OngoingInjecter.class) {
                            @Override
                            public OngoingInjecter inject(Object toInject) {
                                if (toInject.equals(fieldInstance)) {
                                    return stub1;
                                } else {
                                    return stub2;
                                }
                            }
                        };
                    }
                });

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(Mockito.eq(mocks), Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));
        verify(mocks.get(0)).getClass().getDeclaredField(fieldToBeInjected.getName());
        verify(mocks.get(0)).getClass().getDeclaredField(fieldToBeInjected.getName()).setAccessible(true);
        verify(mocks.get(0)).getClass().getDeclaredField(fieldToBeInjected.getName()).set(Mockito.eq(mocks.get(0)), Mockito.any());
        verify(mocks.get(1)).getClass().getDeclaredField(fieldToBeInjected.getName());
        verify(mocks.get(1)).getClass().getDeclaredField(fieldToBeInjected.getName()).setAccessible(true);
        verify(mocks.get(1)).getClass().getDeclaredField(fieldToBeInjected.getName()).set(Mockito.eq(mocks.get(1)), Mockito.any());
        verify(result).inject(Mockito.eq(fieldInstance));
    }
}