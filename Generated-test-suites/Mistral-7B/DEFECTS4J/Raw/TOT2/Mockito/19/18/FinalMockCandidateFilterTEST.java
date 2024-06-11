import org.junit.Before;
import org.junit.Test;
import org.mockito.Assert;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.SimpleField;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private Collection<Object> mocks;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        fieldInstance = mock(Object.class);
        field = new SimpleField("fieldName", Object.class, fieldInstance);
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Object mock = mocks.iterator().next();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertNotNull(result.thenInject());
        assertSame(mock, result.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertNull(result.thenInject());
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionThrownWhenInjectingField() {
        FieldSetter fieldSetterSpy = spy(new FieldSetter(fieldInstance, field));
        filter = new FinalMockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                try {
                    if (!new BeanPropertySetter(fieldInstance, field).set(mocks.iterator().next())) {
                        fieldSetterSpy.set(fieldInstance, fieldInstance);
                    }
                } catch (RuntimeException e) {
                    throw e;
                }
                return new OngoingInjecter() {
                    public Object thenInject() {
                        return null;
                    }
                };
            }
        };
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithEmptyMocksCollection() {
        OngoingInjecter result = filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
        Assert.assertNotNull(result);
        Assert.assertNull(result.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullField() {
        Assert.assertNull(filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        Assert.assertNull(filter.filterCandidate(mocks, field, null));
    }

    @Test
    public void testFilterCandidateWithNullMocksCollection() {
        Assert.assertNull(filter.filterCandidate(null, field, fieldInstance));
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionThrownWhenNullMockInMocksCollection() {
        filter.filterCandidate(Arrays.asList(null), field, fieldInstance);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionThrownWhenNullFieldName() {
        String nullName = null;
        Field nullField = new Field(nullName, Object.class, fieldInstance);
        filter.filterCandidate(mocks, nullField, fieldInstance);
    }
}