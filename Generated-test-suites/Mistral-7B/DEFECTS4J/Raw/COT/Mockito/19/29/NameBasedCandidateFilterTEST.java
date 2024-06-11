import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NameBasedCandidateFilterTest {

 private MockCandidateFilter nextFilter;
 private NameBasedCandidateFilter filterUnderTest;
 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() throws Exception {
 nextFilter = mock(MockCandidateFilter.class);
 mockUtil = mock(MockUtil.class);
 filterUnderTest = new NameBasedCandidateFilter(nextFilter);
 }

 @Test
 public void testFilterCandidate_oneMock_noConflict() throws Exception {
 Field testField = getField("testField");
 Object testMock = mock(Object.class);
 Object testInstance = new Object();

 List<Object> mocks = new ArrayList<>();
 mocks.add(testMock);

 OngoingInjecter result = filterUnderTest.filterCandidate(mocks, testField, testInstance);

 Assert.assertEquals(testMock, result.getMock());
 Assert.assertNull(result.getField());
 }

 @Test
 public void testFilterCandidate_oneMock_conflictingField() throws Exception {
 Field testField = getField("testField");
 Field conflictingField = getField("conflictingField");
 Object testMock = mock(Object.class);
 Object testInstance = new Object();

 List<Object> mocks = new ArrayList<>();
 mocks.add(testMock);

 OngoingInjecter result = filterUnderTest.filterCandidate(mocks, testField, testInstance);

 Assert.assertEquals(testMock, result.getMock());
 Assert.assertNull(result.getField());

 // Verify that next filter was called
 verify(nextFilter).filterCandidate(mocks, conflictingField, testInstance);
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_multipleMocks_conflictingNames() throws Exception {
 Field testField = getField("testField");
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Object testInstance = new Object();

 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 // Given we have 2 mocks with the same name
 Assert.assertEquals(mock1.getClass().getName(), mock2.getClass().getName());

 // When we try to filter
 OngoingInjecter result = filterUnderTest.filterCandidate(mocks, testField, testInstance);

 // Then an exception should be thrown
 }

 @Test
 public void testFilterCandidate_multipleMocks_noConflict() throws Exception {
 Field testField = getField("testField1");
 Field conflictingField = getField("testField2");
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Object testInstance = new Object();

 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 // Given we have 2 mocks with different names
 Assert.assertNotEquals(mock1.getClass().getName(), mock2.getClass().getName());

 OngoingInjecter result1 = filterUnderTest.filterCandidate(mocks, testField, testInstance);
 Assert.assertEquals(mock1, result1.getMock());

 List<Object> remainingMocks = new ArrayList<>(mocks);
 remainingMocks.remove(0);

 OngoingInjecter result2 = filterUnderTest.filterCandidate(remainingMocks, conflictingField, testInstance);
 Assert.assertEquals(mock2, result2.getMock());
 }

 private Field getField(String name) throws NoSuchFieldException, IllegalAccessException {
 AccessibleObject field = filterUnderTest.getClass().getDeclaredField(name);
 field.setAccessible(true);
 return (Field) field;
 }

}