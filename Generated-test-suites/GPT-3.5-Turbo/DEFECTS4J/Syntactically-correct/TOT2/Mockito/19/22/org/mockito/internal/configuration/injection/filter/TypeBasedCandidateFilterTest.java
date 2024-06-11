import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter nextFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };
        
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockObjectA());
        mocks.add(new MockObjectB());
        mocks.add(new MockObjectC());
        
        Field field = getField("field");
        Object fieldInstance = createFieldInstance();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    private Field getField(String name) {
        try {
            return TypeBasedCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private Object createFieldInstance() {
        return new Object();
    }
    
    private class MockObjectA {}
    private class MockObjectB {}
    private class MockObjectC {}
}