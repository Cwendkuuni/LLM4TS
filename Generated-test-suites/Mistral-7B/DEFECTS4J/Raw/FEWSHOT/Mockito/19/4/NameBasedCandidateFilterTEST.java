package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockUtil;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtilTest;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.stubs.MockInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
        nextMock = new NextMockCandidateFilter();
        filter = new NameBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_OneMockNameMatches_ReturnsMatchedMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockInfo("mock1"));
        Object fieldInstance = new Object();
        Field field = FieldUtils.getField(fieldInstance, "testField");

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Object expectedMock = mocks.get(0);
        assertEquals(expectedMock, result.getMock());
        assertEquals(field.getName(), result.getField().getName());
    }

    @Test
    public void testFilterCandidate_MultipleMocksNameMatches_ReturnsFirstOne() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockInfo("mock1"));
        mocks.add(new MockInfo("mock1"));
        Object fieldInstance = new Object();
        Field field = FieldUtils.getField(fieldInstance, "testField");

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Object expectedMock = mocks.get(0);
        assertEquals(expectedMock, result.getMock());
        assertEquals(field.getName(), result.getField().getName());
    }

    @Test
    public void testFilterCandidate_NoMockNameMatches_ReturnsNextFilterResult() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        Field field = FieldUtils.getField(fieldInstance, "testField");

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_ConflictingFields_ReturnsNextFilterResult() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockInfo("mock1"));
        mocks.add(new MockInfo("mock2"));
        Object fieldInstance = new Object();
        Field field1 = FieldUtils.getField(fieldInstance, "testField1");
        Field field2 = FieldUtils.getField(fieldInstance, "testField2");

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field1, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_ConflictingFields_ReturnsNextFilterResult_WithCorrectName() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockInfo("mock1"));
        mocks.add(new MockInfo("mock2"));
        Object fieldInstance = new Object();
        Field field1 = FieldUtils.getField(fieldInstance, "testField1");
        Field field2 = FieldUtils.getField(fieldInstance, "testField2");

        // When
        OngoingInjecter nextResult = new NextMockCandidateFilter().filterCandidate(mocks, field2, fieldInstance);

        // Then
        assertNull(nextResult);

        OngoingInjecter result = filter.filterCandidate(mocks, field1, fieldInstance);

        // Then
        Object expectedMock = mocks.get(1);
        assertEquals(expectedMock, result.getMock());
        assertEquals(field1.getName(), result.getField().getName());
    }

    private static class NextMockCandidateFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return null;
        }
    }
}