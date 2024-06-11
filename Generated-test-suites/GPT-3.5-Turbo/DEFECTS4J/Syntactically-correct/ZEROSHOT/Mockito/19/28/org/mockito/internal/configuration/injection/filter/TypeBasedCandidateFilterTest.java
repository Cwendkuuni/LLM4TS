import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() throws Exception {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
        mocks = new ArrayList<>();
        field = TypeBasedCandidateFilterTest.class.getDeclaredField("mocks");
        fieldInstance = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Object mock3 = new Object();

        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertEquals(mock1, result);
        Assert.assertEquals(mock2, result);
        Assert.assertFalse(result.contains(mock3));

        Mockito.verify(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
    }
}