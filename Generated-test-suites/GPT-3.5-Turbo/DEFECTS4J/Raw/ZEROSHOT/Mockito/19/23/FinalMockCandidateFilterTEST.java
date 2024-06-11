import org.junit.*;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        // Setup
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mocks.size()).thenReturn(1);
        Object matchingMock = mock(Object.class);
        when(mocks.iterator().next()).thenReturn(matchingMock);
        BeanPropertySetter mockBeanPropertySetter = mock(BeanPropertySetter.class);
        FieldSetter mockFieldSetter = mock(FieldSetter.class);
        Reporter mockReporter = mock(Reporter.class);

        // Execute
        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter result = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        result.thenInject();

        // Verify
        try {
            verifyNew(BeanPropertySetter.class).withArguments(fieldInstance, field);
        } catch (Exception e) {
            Assert.fail("Expected to create BeanPropertySetter");
        }

        try {
            verify(mockBeanPropertySetter).set(matchingMock);
        } catch (Exception e) {
            Assert.fail("Expected to call set on BeanPropertySetter");
        }

        verifyNew(FieldSetter.class).withArguments(fieldInstance, field);

        try {
            verify(mockFieldSetter).set(matchingMock);
        } catch (Exception e) {
            Assert.fail("Expected to call set on FieldSetter");
        }

        verifyNew(Reporter.class).withNoArguments();

        try {
            verify(mockReporter).cannotInjectDependency(field, matchingMock, null);
        } catch (Exception e) {
            Assert.fail("Expected to call cannotInjectDependency on Reporter");
        }

        verifyNoMoreInteractions(mockBeanPropertySetter, mockFieldSetter, mockReporter);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        // Setup
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mocks.size()).thenReturn(2);

        // Execute
        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter result = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object actual = result.thenInject();

        // Verify
        Assert.assertNull(actual);
    }
}