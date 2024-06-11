import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.Arrays;
import org.mockito.internal.util.reflection.FieldAccessor;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        Collection<Object> mocks = Arrays.asList(new Object(), new Object(), new Object());
        Field field = new Field("testField") {
            @Override
            public Object get(Object obj) {
                return new Object();
            }
        };
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMocks() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        Collection<Object> mocks = null;
        Field field = new Field("testField") {
            @Override
            public Object get(Object obj) {
                return new Object();
            }
        };
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullField() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        Collection<Object> mocks = Arrays.asList(new Object(), new Object(), new Object());
        Field field = null;
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        Collection<Object> mocks = Arrays.asList(new Object(), new Object(), new Object());
        Field field = new Field("testField") {
            @Override
            public Object get(Object obj) {
                return new Object();
            }
        };
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullMockInCollection() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        Collection<Object> mocks = Arrays.asList(null, new Object(), new Object());
        Field field = new Field("testField") {
            @Override
            public Object get(Object obj) {
                return new Object();
            }
        };
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        Collection<Object> mocks = Arrays.asList();
        Field field = new Field("testField") {
            @Override
            public Object get(Object obj) {
                return new Object();
            }
        };
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNull(result);
    }
}