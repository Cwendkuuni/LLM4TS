import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                Stubber stubber = org.mockito.Mockito.mock(OngoingInjecter.class);
                return stubber;
            }
        };

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = new Object();

        OngoingInjecter actual = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNotNull(actual);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMocks() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(new InvocationOnMock(new Object()) {});
            }
        };

        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = new Object();

        mockCandidateFilter.filterCandidate(null, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(new InvocationOnMock(new Object()) {});
            }
        };

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, this);

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullFieldToBeInjected() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(new InvocationOnMock(new Object()) {});
            }
        };

        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();

        mockCandidateFilter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithEmptyMocksCollection() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(new InvocationOnMock(new Object()) {});
            }
        };

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = new Object();

        OngoingInjecter actual = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNotNull(actual);
        Assert.assertTrue(mocks.isEmpty());
    }

}