import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Object fieldInstance;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private Object mockWithField;
 private Object mockWithProperty;
 private Field field;

 @Before
 public void setup() {
 filter = new FinalMockCandidateFilter();
 fieldInstance = new Object();
 mock1 = new MockObject();
 mock2 = new MockObject();
 mock3 = new MockObject();
 mockWithField = new MockObjectWithField();
 mockWithProperty = new MockObjectWithProperty();
 field = getField(mockWithProperty, "property");
 }

 @Test
 public void testFilterCandidateWithSingleMockAndPropertySetterSuccess() {
 Object result = filter.filterCandidate(Arrays.asList(mock1), field, fieldInstance);
 assertSame(mock1, result);
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidateWithSingleMockAndPropertySetterFailure() {
 filter.filterCandidate(Collections.singletonList(mock2), field, fieldInstance);
 }

 @Test
 public void testFilterCandidateWithSingleMockAndFieldSetterSuccess() {
 filter.filterCandidate(Collections.singletonList(mock3), field, fieldInstance);
 }

 @Test
 public void testFilterCandidateWithMultipleMocksAndPropertySetterSuccess() {
 Object result = filter.filterCandidate(Arrays.asList(mock1, mock2), field, fieldInstance);
 assertSame(mock1, result);
 }

 @Test
 public void testFilterCandidateWithMultipleMocksAndFieldSetterFailure() {
 Object result = filter.filterCandidate(Arrays.asList(mock1, mock2, mock3), field, fieldInstance);
 assertNull(result);
 }

 @Test
 public void testFilterCandidateWithNullMocks() {
 Object result = filter.filterCandidate(null, field, fieldInstance);
 assertNull(result);
 }

 @Test
 public void testFilterCandidateWithNullField() {
 Object result = filter.filterCandidate(Arrays.asList(mock1), null, fieldInstance);
 assertNull(result);
 }

 @Test
 public void testFilterCandidateWithNullFieldInstance() {
 Object result = filter.filterCandidate(Arrays.asList(mock1), field, null);
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidateWithNullMocksAndNullField() {
 filter.filterCandidate(null, null, null);
 }

 private Field getField(Object obj, String fieldName) {
 try {
 return obj.getClass().getDeclaredField(fieldName);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private static class MockObject {
 }

 private static class MockObjectWithField {
 private int field;
 }

 private static class MockObjectWithProperty {
 private int property;
 }
}