import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.reflection.FieldSetter;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;
import static org.mockito.internal.util.reflection.Reflections.getFieldValue;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        fieldInstance = new Object();
        field = mock(Field.class);

        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_SingleMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        OngoingStubbing<Boolean> then = stub(injecter).thenInject();

        then.returns(true);

        setFieldValue(fieldInstance, field, mock);

        assertTrue(then.called());
    }
}