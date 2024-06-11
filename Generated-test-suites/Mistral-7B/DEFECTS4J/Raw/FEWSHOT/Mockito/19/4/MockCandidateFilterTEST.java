package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidateNullCollection() {
        assertNull(filter.filterCandidate(null, null, null));
    }

    @Test
    public void testFilterCandidateNullField() {
        Collection<Object> mocks = new ArrayList<>();
        assertNull(filter.filterCandidate(mocks, null, null));
    }

    @Test
    public void testFilterCandidateNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        assertNull(filter.filterCandidate(mocks, field, null));
    }

    @Test
    public void testFilterCandidateEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", String.class, this.getClass());
        Object fieldInstance = "testInstance";
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateSingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = new Field("testField", String.class, this.getClass());
        Object fieldInstance = "testInstance";
        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        Field field = new Field("testField", String.class, this.getClass());
        Object fieldInstance = "testInstance";
        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateSameClass() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(spy(this.getClass()));
        Field field = new Field("testField", String.class, this.getClass());
        Object fieldInstance = "testInstance";
        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateDifferentClass() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = new Field("testField", Integer.class, this.getClass());
        Object fieldInstance = 1;
        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateSameFieldOnDifferentInstances() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(spy(this.getClass()));
        mocks.add(spy(this.getClass()));
        Field field = new Field("testField", String.class, this.getClass());
        Object fieldInstance = "testInstance";
        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateDifferentFields() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(spy(this.getClass()));
        Field field1 = new Field("testField1", String.class, this.getClass());
        Field field2 = new Field("testField2", String.class, this.getClass());
        Object fieldInstance = "testInstance";
        Object result = filter.filterCandidate(mocks, field1, fieldInstance);
        assertNull(result);
    }

}