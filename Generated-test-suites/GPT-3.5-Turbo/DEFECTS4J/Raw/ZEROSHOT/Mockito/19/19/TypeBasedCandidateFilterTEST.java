import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {
    
    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;
    private Field field;
    private Object fieldInstance;
    private Collection<Object> mocks;

    @Before
    public void setUp() {
        nextFilter = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
        field = new Field();
        fieldInstance = new Object();
        mocks = new ArrayList<Object>();
    }

    @Test
    public void testFilterCandidate_NoMocks() {
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(result);
        Assert.assertSame(nextFilter, result);
    }

    @Test
    public void testFilterCandidate_MockTypeMatches() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(result);
        Assert.assertSame(nextFilter, result);
    }

    @Test
    public void testFilterCandidate_NoMockTypeMatches() {
        Object mock1 = Mockito.mock(Integer.class);
        Object mock2 = Mockito.mock(Double.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(result);
        Assert.assertSame(nextFilter, result);
    }

}