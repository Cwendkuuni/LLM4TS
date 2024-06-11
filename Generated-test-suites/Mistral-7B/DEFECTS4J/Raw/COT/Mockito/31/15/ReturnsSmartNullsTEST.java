import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 @Mock
 private Object mockObject;

 @Test
 public void testAnswerNullMethod() throws Exception {
     returnsSmartNulls = new ReturnsSmartNulls();
     Mockito.when(mockObject.methodThatReturnsNull()).thenAnswer(returnsSmartNulls);
     Object result = mockObject.methodThatReturnsNull();
     assertSmartNull(result);
 }

 @Test
 public void testAnswerEmptyArrayMethod() throws Exception {
     returnsSmartNulls = new ReturnsSmartNulls();
     Mockito.when(mockObject.methodThatReturnsEmptyArray()).thenAnswer(returnsSmartNulls);
     Object[] result = mockObject.methodThatReturnsEmptyArray();
     assertSmartNullArray(result);
 }

 @Test
 public void testAnswerPrimitiveTypeMethod() throws Exception {
     returnsSmartNulls = new ReturnsSmartNulls();
     Mockito.when(mockObject.methodThatReturnsInt()).thenAnswer(returnsSmartNulls);
     int result = mockObject.methodThatReturnsInt();
     assertEquals(-1, result.hashCode()); // SmartNull hashcode is -1
 }

 @Test
 public void testAnswerStringMethod() throws Exception {
     returnsSmartNulls = new ReturnsSmartNulls();
     Mockito.when(mockObject.methodThatReturnsString()).thenAnswer(returnsSmartNulls);
     String result = mockObject.methodThatReturnsString();
     assertEquals("SmartNull returned by unstubbed methodThatReturnsString() method on mock", result);
 }

 @Test
 public void testAnswerThrowableMethod() throws Exception {
     returnsSmartNulls = new ReturnsSmartNulls();
     Mockito.when(mockObject.methodThatThrowsException()).thenAnswer(returnsSmartNulls);
     try {
         mockObject.methodThatThrowsException();
     } catch (Throwable e) {
         verify(mockObject).methodThatThrowsException(); // the method was called
     }
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerNullReturnTypeMethod() throws Exception {
     returnsSmartNulls = new ReturnsSmartNulls();
     Mockito.when(mockObject.methodThatReturnsNullObject()).thenAnswer(returnsSmartNulls);
     mockObject.methodThatReturnsNullObject(); // should throw NPE
 }

 @Test
 public void testAnswerNotMockableClassMethod() throws Exception {
     returnsSmartNulls = new ReturnsSmartNulls();
     Mockito.when(mockObject.methodThatReturnsFinalClass()).thenAnswer(returnsSmartNulls);
     Object result = mockObject.methodThatReturnsFinalClass();
     assertNull(result);
 }

 @Test
 public void testAnswerArrayOfNotMockableClassMethod() throws Exception {
     returnsSmartNulls = new ReturnsSmartNulls();
     Mockito.when(mockObject.methodThatReturnsArrayOfFinalClass()).thenAnswer(returnsSmartNulls);
     Object[] result = mockObject.methodThatReturnsArrayOfFinalClass();
     assertArrayEquals(new Object[0], result);
 }

 private void assertSmartNull(Object o) {
     assertNotNull(o);
     assertEquals(-1, o.hashCode());
 }

 private void assertSmartNullArray(Object[] o) {
     assertNotNull(o);
     assertEquals(0, o.length);
     for (Object element : o) {
         assertSmartNull(element);
     }
 }
}