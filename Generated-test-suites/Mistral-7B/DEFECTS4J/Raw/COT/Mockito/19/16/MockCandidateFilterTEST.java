package org.mockito.internal.configuration.injection.filter.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.utils.naming.MethodNameUtils;
import org.mockito.internal.utils.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void init() {
        mockCandidateFilter = MockitoTests.spy(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Whitebox.getInternalState(this, "mocksField"); // replace with actual field name
        Object fieldInstance = new Object();

        OngoingInjecter actual = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNotNull(actual);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullCollection() {
        Field fieldToBeInjected = Whitebox.getInternalState(this, "mocksField"); // replace with actual field name
        Object fieldInstance = new Object();

        MockCandidateFilter mockCandidateFilter = MockitoTests.spy(MockCandidateFilter.class);
        mockCandidateFilter.filterCandidate(null, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullField() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();

        MockCandidateFilter mockCandidateFilter = MockitoTests.spy(MockCandidateFilter.class);
        mockCandidateFilter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Whitebox.getInternalState(this, "mocksField"); // replace with actual field name

        MockCandidateFilter mockCandidateFilter = MockitoTests.spy(MockCandidateFilter.class);
        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, null);
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Whitebox.getInternalState(this, "mocksField"); // replace with actual field name
        Object fieldInstance = new Object();

        OngoingInjecter actual = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertEquals(0, actual.getInjectedMocks().size());
    }
}