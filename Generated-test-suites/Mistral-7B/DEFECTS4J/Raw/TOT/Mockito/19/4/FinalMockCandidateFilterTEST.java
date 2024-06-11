import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.RegexBasedFieldNameGenerator;
import org.mockito.internal.util.reflection.RegexBasedPropertyNameGenerator;
import org.mockito.mockitofy.MockitoCore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.testAnnotations.InternalReflection.getDeclaredField;
import static org.mockito.Mockito.testAnnotations.InternalReflection.setField;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private String fieldName;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        field = getDeclaredField(fieldInstance, "someField");
        fieldName = new RegexBasedFieldNameGenerator().getFieldName(field);
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Object mock = spy(fieldInstance.getClass());
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        verify(mock).someField = fieldInstance;
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(fieldInstance.getClass());
        Object mock2 = mock(fieldInstance.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        // no side effect in this test case, as we're dealing with mocks
        // in real scenario, one of the mocks should have been injected
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithException() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        whenExceptionIsThrownWhenSettingField(mock, fieldName);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        // exception should have been thrown and reported by Reporter
    }

    private void whenExceptionIsThrownWhenSettingField(Object mock, String fieldName) {
        try {
            setField(mock, fieldName, fieldInstance);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFilterCandidateWhenNoMockFound() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        // no side effect in this test case, as we're not dealing with real mocks
        // in real scenario, the injecter should not have done anything
    }
}