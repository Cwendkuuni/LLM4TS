import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.BaseMockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.internal.util.reflection.PropertyAccessor;
import org.mockito.internal.util.reflection.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private List<Object> mocks;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private Object field;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        field = ReflectUtils.findField(Object.class, "fieldName");
        MockUtil.setFieldValue(fieldInstance, field, null);
        mocks.add(mock1);
        mocks.add(mock2);
    }

    @Test
    public void testFilterCandidate_OneMock_BeanPropertySetterSuccess() {
        PropertyAccessor propertyAccessor = new PropertyAccessor(mock1);
        propertyAccessor.setValue(field, "testValue");
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = actualInjecter.thenInject();

        assertEquals(mock1, result);
        assertNotNull(ReflectUtils.getFieldValue(fieldInstance, field));
    }

    @Test(expected = BaseMockitoException.class)
    public void testFilterCandidate_OneMock_BeanPropertySetterFailure() {
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        actualInjecter.thenInject();

        Mockito.verify(new Reporter()).cannotInjectDependency(ArgumentsMatchers.notNull(), ArgumentsMatchers.notNull(Object.class), ArgumentsMatchers.notNull(BaseMockitoException.class));
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterSuccess() {
        ReflectUtils.setFieldValue(mock1, field, "testValue");

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = actualInjecter.thenInject();

        assertEquals(mock1, result);
        assertNotNull(ReflectUtils.getFieldValue(fieldInstance, field));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NullResult() {
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = actualInjecter.thenInject();

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_BeanPropertySetterSuccess() {
        PropertyAccessor propertyAccessor1 = new PropertyAccessor(mock1);
        propertyAccessor1.setValue(field, "testValue1");
        PropertyAccessor propertyAccessor2 = new PropertyAccessor(mock2);
        propertyAccessor2.setValue(field, "testValue2");

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = actualInjecter.thenInject();

        assertEquals(mock1, result);
        assertNotNull(ReflectUtils.getFieldValue(fieldInstance, field));
        assertEquals("testValue1", ReflectUtils.getFieldValue(fieldInstance, field));
    }

    @Test(expected = BaseMockitoException.class)
    public void testFilterCandidate_MultipleMocks_BeanPropertySetterFailure() {
        PropertyAccessor propertyAccessor1 = new PropertyAccessor(mock1);
        propertyAccessor1.setValue(field, "testValue1");
        PropertyAccessor propertyAccessor2 = new PropertyAccessor(mock2);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        actualInjecter.thenInject();

        Mockito.verify(new Reporter()).cannotInjectDependency(arguments(), arguments(), ArgumentsMatchers.notNull(BaseMockitoException.class));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_FieldSetterSuccess() {
        ReflectUtils.setFieldValue(mock1, field, "testValue1");
        ReflectUtils.setFieldValue(mock2, field, "testValue2");

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = actualInjecter.thenInject();

        assertEquals(mock1, result);
        assertNotNull(ReflectUtils.getFieldValue(fieldInstance, field));
        assertEquals("testValue1", ReflectUtils.getFieldValue(fieldInstance, field));
    }
}