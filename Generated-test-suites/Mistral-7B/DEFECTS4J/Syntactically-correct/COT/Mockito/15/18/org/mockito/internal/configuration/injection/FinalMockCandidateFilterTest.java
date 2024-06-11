import org.junit.Test;
import org.junit.Assert;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;

 @Before
 public void setup() {
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_OneMock_FieldAccessSuccess() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object mock = new Object() {};
 Field field = new FieldType(Object.class, "testField").getRealField();
 Object fieldInstance = new Object() {};

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 Assert.assertTrue(result.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_OneMock_FieldAccessFailure() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object mock = new Object() {};
 Field field = new FieldType(Object.class, "testField").getRealField();
 Object fieldInstance = new Object() {};

 // When, Then
 Assert.assertThrows(MockitoException.class, () -> filter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testFilterCandidate_MultipleMocks_FirstMatchSuccess() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(new Object() {});
 mocks.add(new Object() {});
 Object mock1 = mocks.get(0);
 Field field = new FieldType(Object.class, "testField").getRealField();
 Object fieldInstance = new Object() {};

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 Assert.assertTrue(result.thenInject());
 }

 @Test
 public void testFilterCandidate_MultipleMocks_NoMatch() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(new Object() {});
 mocks.add(new Object() {});
 Object fieldInstance = new Object() {};

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, new FieldType(Object.class, "testField").getRealField(), fieldInstance);

 // Then
 Assert.assertFalse(result.thenInject());
 }
}