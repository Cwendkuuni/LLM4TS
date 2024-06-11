package org.mockito.internal.configuration.injection.filter.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class MockCandidateFilterTest {

    @Spy
    private MockCandidateFilter filter;

    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        fieldToBeInjected = MockitoJUnitRunner.class.getDeclaredField("mocks");
        fieldToBeInjected.setAccessible(true);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        try {
            filter.filterCandidate(null, fieldToBeInjected, fieldInstance);
            assert false : "Should have thrown NullPointerException";
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidate_NullField() {
        try {
            filter.filterCandidate(mocks, null, fieldInstance);
            assert false : "Should have thrown NullPointerException";
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        try {
            filter.filterCandidate(mocks, fieldToBeInjected, null);
            assert false : "Should have thrown NullPointerException";
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidate_BasicScenario() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_FieldAlreadyInjected() {
        Object mock = spy(fieldInstance);
        mocks.add(mock);

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFilterCandidate_ModifiableCollection() {
        Collection<Object> modifiableMocks = new ArrayList<>() {{
            add(mock(Object.class));
        }};

        filter.filterCandidate(modifiableMocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        OngoingInjecter result = filter.filterCandidate(new ArrayList<>(), fieldToBeInjected, fieldInstance);

        assertNotNull(result);
    }

}