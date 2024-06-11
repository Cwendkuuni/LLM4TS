import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.bean.BeanPropertySetter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private List<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        fieldInstance = mock(Object.class);
        field = new Field("testField", Object.class, java.lang.reflect.AccessibleObject.PROTECTED);
    }

    @Test
    public void testFilterCandidate_SingleMock() {
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(actual);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_PropertySetterSuccess() {
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(actual);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_MultipleMocks_PropertySetterFailure() {
        when(field.isAccessible()).thenReturn(true);
        when(field.canWrite()).thenReturn(true);
        when(field.set(fieldInstance, anyObject())).thenThrow(new RuntimeException());

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(actual);
        verify(new Reporter()).cannotInjectDependency(eq(field), any(), any(RuntimeException.class));
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_MultipleMocks_FieldSetterFailure() {
        when(field.isAccessible()).thenReturn(true);
        when(field.canWrite()).thenReturn(true);
        doThrow(new RuntimeException()).when(new FieldSetter(fieldInstance, field)).set(any(), anyObject());

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(actual);
        verify(new Reporter()).cannotInjectDependency(eq(field), any(), any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatchingMock() {
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(actual);
        assertNull(actual.thenInject());
    }
}