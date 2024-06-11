import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBasedCandidateFilterTest {

    TypeBasedCandidateFilter filter;
    List<Object> mocks;
    Field field;
    Object fieldInstance;

    @Before
    public void setUp() {
        mocks = new ArrayList<>();
        fieldInstance = new Object();

        filter = new TypeBasedCandidateFilter(new TypeBasedCandidateFilter.OngoingInjecter() {
            @Override
            public Object getNextMock() {
                return null;
            }
        });
    }

    @Test
    public void testFilterCandidateWithMockOfSameType() {
        mocks.add(Mockito.mock(Number.class));
        mocks.add(Mockito.mock(Number.class));

        field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return Number.class;
            }
        };

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mocks.get(mocks.size() - 1), result);
    }

    @Test
    public void testFilterCandidateWithMockOfDifferentType() {
        mocks.add(Mockito.mock(Number.class));
        mocks.add(Mockito.mock(String.class));

        field = new Field("testField") {
            @Override
            public Class<?> getType() {
                return Number.class;
            }
        };

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testFilterCandidateWithEmptyList() {
        filter.filterCandidate(mocks, field, fieldInstance);
    }
}