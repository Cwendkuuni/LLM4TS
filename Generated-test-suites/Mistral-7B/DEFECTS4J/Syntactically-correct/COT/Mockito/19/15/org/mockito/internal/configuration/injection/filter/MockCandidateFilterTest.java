import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.Field;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(fieldToBeInjected, fieldInstance);
            }
        };

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", String.class, null);
        Object fieldInstance = new Object() {};

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullCollection() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(fieldToBeInjected, fieldInstance);
            }
        };

        Collection<Object> mocks = null;
        Field fieldToBeInjected = new Field("testField", String.class, null);
        Object fieldInstance = new Object() {};

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullField() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(fieldToBeInjected, fieldInstance);
            }
        };

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = new Object() {};

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(fieldToBeInjected, fieldInstance);
            }
        };

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", String.class, null);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(fieldToBeInjected, fieldInstance);
            }
        };

        Collection<Object> mocks = Collections.emptyList();
        Field fieldToBeInjected = new Field("testField", String.class, null);
        Object fieldInstance = new Object() {};

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

}