import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.Invocations;
import org.mockito.internal.util.reflection.MethodType;
import org.mockito.internal.util.reflection.Reflections;

import java.lang.reflect.Collection;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private FinalMockCandidateFilter filter;
    private Reporter reporter;

    @Before
    public void setUp() {
        mocks = new ArrayList<>();
        field = Reflections.getField(Object.class, "fieldName"); // replace "fieldName" with the actual name of the field
        fieldInstance = new Object() {
            public Field field = new FieldType(field);
        };
        reporter = mock(Reporter.class);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_OneMock_Success() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter expectedInjecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                BeanPropertySetter.set(fieldInstance, field, mock);
                return mock;
            }
        };

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedInjecter, actualInjecter);
    }

    @Test
    public void testFilterCandidate_OneMock_FailPropertySetter() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        try {
            new BeanPropertySetter(fieldInstance, field).set(mock);
            fail("BeanPropertySetter should throw an exception");
        } catch (IllegalAccessException e) {
            // Expected
        }

        OngoingInjecter expectedInjecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                new FieldSetter(fieldInstance, field).set(mock);
                return mock;
            }
        };

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedInjecter, actualInjecter);
        verify(reporter).cannotInjectDependency(field, mock, null);
    }

    @Test
    public void testFilterCandidate_OneMock_FailFieldSetter() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        try {
            new FieldSetter(fieldInstance, field).set(mock);
            fail("FieldSetter should throw an exception");
        } catch (IllegalAccessException e) {
            // Expected
        }

        OngoingInjecter expectedInjecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                return null;
            }
        };

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedInjecter, actualInjecter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ReturnNullInjecter() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(actualInjecter);
    }
}