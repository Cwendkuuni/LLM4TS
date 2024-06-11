package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test class for NameBasedCandidateFilter.
 */
public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;
    private Object mock1;
    private Object mock2;
    private Object fieldInstance;

    @Before
    public void setup() {
        mockUtil = spy(new MockUtil());
        mock1 = mock(Object.class, "mock1");
        mock2 = mock(Object.class, "mock2");
        fieldInstance = new Object();
        filter = new NameBasedCandidateFilter(new DummyMockCandidateFilter()) {
            private final List<Object> mocks = new ArrayList<>();

            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks,
                                                  Field field, Object fieldInstance) {
                this.mocks.clear();
                this.mocks.addAll(mocks);
                return super.filterCandidate(mocks, field, fieldInstance);
            }
        };
    }

    @Test
    public void testFilterCandidateOneMock() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<Object>() {{
            add(mock1);
        }};

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(mock1, result.getCandidate());
    }

    @Test
    public void testFilterCandidateTwoMocksWithSameName() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<Object>() {{
            add(mock1);
            add(mock2);
        }};

        when(mockUtil.getMockName(mock1)).thenReturn("mockName");
        when(mockUtil.getMockName(mock2)).thenReturn("mockName");

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(mock1, result.getCandidate());
    }

    @Test
    public void testFilterCandidateTwoMocksWithDifferentNames() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<Object>() {{
            add(mock1);
            add(mock2);
        }};

        when(mockUtil.getMockName(mock1)).thenReturn("mockName1");
        when(mockUtil.getMockName(mock2)).thenReturn("mockName2");

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(mock2, result.getCandidate());
    }

    @Test
    public void testFilterCandidateTwoMocksWithSameNameButDifferentType() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<Object>() {{
            add(mock1);
            add(new Object());
        }};

        when(mockUtil.getMockName(mock1)).thenReturn("mockName");

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(mock1, result.getCandidate());
    }

    @Test
    public void testFilterCandidateTwoMocksWithConflictingFieldNames() throws Exception {
        // Given
        Class<?> clazz = mock(Class.class);
        Field field1 = clazz.getField("field1");
        Field field2 = clazz.getField("field1");
        Collection<Object> mocks = new ArrayList<Object>() {{
            add(mock1);
            add(mock2);
        }};

        when(mockUtil.getMockName(mock1)).thenReturn("mockName1");
        when(mockUtil.getMockName(mock2)).thenReturn("mockName2");

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field1, fieldInstance);

        // Then
        assertEquals(mock1, result.getCandidate());

        // When
        OngoingInjecter result2 = filter.filterCandidate(mocks, field2, fieldInstance);

        // Then
        assertEquals(mock2, result2.getCandidate());
    }

    @Test
    public void testFilterCandidateEmptyMocksCollection() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<>();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(null, result.getCandidate());
    }

    @Test
    public void testFilterCandidateNullMocks() throws Exception {
        // Given
        Collection<Object> mocks = null;

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(null, result.getCandidate());
    }

    @Test
    public void testFilterCandidateNullField() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<Object>() {{
            add(mock1);
        }};

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(mock1, result.getCandidate());
    }

    @Test
    public void testFilterCandidateNullFieldInstance() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<Object>() {{
            add(mock1);
        }};

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, null, null);

        // Then
        assertEquals(mock1, result.getCandidate());
    }

    @Test
    public void testFilterCandidateWithNextMockCandidateFilter() throws Exception {
        // Given
        NameBasedCandidateFilter filter1 = new NameBasedCandidateFilter(new DummyMockCandidateFilter()) {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks,
                                                  Field field, Object fieldInstance) {
                assertEquals("field1", field.getName());
                return new OngoingInjecter(this, mock1);
            }
        };

        NameBasedCandidateFilter filter2 = new NameBasedCandidateFilter(filter1) {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks,
                                                  Field field, Object fieldInstance) {
                assertEquals("field2", field.getName());
                return new OngoingInjecter(this, mock2);
            }
        };

        Collection<Object> mocks = new ArrayList<Object>() {{
            add(mock1);
            add(mock2);
        }};

        // When
        OngoingInjecter result = filter2.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(mock2, result.getCandidate());

        // When
        OngoingInjecter result2 = filter1.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(mock1, result2.getCandidate());
    }

    /**
     * Dummy implementation of MockCandidateFilter to simplify test setup.
     */
    private static class DummyMockCandidateFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks,
                                              Field field, Object fieldInstance) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Simple POJO for testing.
     */
    private static class TestClass {
        private String field1;
        private int field2;
    }
}