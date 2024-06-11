import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Field field = getField("fieldInstance", FinalMockCandidateFilter.class);
        Object fieldInstance = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(new Object());

        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        Assert.assertEquals(mocks.get(0), result);
        Assert.assertTrue(new BeanPropertySetter(fieldInstance, field).isSet());
        Assert.assertTrue(new FieldSetter(fieldInstance, field).isSet());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Field field = getField("fieldInstance", FinalMockCandidateFilter.class);
        Object fieldInstance = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(new Object(), new Object());

        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        Assert.assertNull(result);
        Assert.assertFalse(new BeanPropertySetter(fieldInstance, field).isSet());
        Assert.assertFalse(new FieldSetter(fieldInstance, field).isSet());
    }

    @Test
    public void testFilterCandidate_noMocks() {
        Field field = getField("fieldInstance", FinalMockCandidateFilter.class);
        Object fieldInstance = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.emptyList();

        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        Assert.assertNull(result);
        Assert.assertFalse(new BeanPropertySetter(fieldInstance, field).isSet());
        Assert.assertFalse(new FieldSetter(fieldInstance, field).isSet());
    }
    
    @Test
    public void testFilterCandidate_exceptionHandling() {
        Field field = getField("fieldInstance", FinalMockCandidateFilter.class);
        Object fieldInstance = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(new Object());

        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        Assert.assertEquals(mocks.get(0), result);
        Assert.assertTrue(new BeanPropertySetter(fieldInstance, field).isSet());
        Assert.assertTrue(new FieldSetter(fieldInstance, field).isSet());

        Assert.assertThrows(Reporter.class, () -> new Reporter().cannotInjectDependency(field, result, new RuntimeException()));
    }

    private Field getField(String fieldName, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new IllegalArgumentException("Field not found: " + fieldName);
    }
}