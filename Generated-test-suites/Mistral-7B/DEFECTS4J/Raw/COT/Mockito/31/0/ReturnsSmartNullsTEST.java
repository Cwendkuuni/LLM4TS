import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object target;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 target = mock(TestClass.class, Answers.CALLS_REAL_METHODS);
 }

 @Test
 public void testAnswerNullMethod() {
 Method method = nullMethod();
 verify(target).methodCall(any(String.class));

 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 assertNull(result);
 }

 @Test
 public void testAnswerSerializableMethod() {
 Method method = serializableMethod();
 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 assertNotNull(result);
 assertInstanceOf(Serializable.class, result);
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerNonMockableMethodThrowsNPE() {
 Method method = finalClassMethod();
 try {
 returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 } catch (Throwable e) {
 throw new AssertionError("Expected NPE but got: " + e.getMessage(), e);
 }
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testAnswerToStringMethod() {
 Method method = toStringMethod();
 try {
 returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 } catch (Throwable e) {
 String expectedMessage = "SmartNull returned by unstubbed toString() method on mock";
 verify(target).toString();
 assertNotNull(e.getMessage());
 assertFalse(e.getMessage().contains("NullPointerException"));
 assertFalse(e.getMessage().contains("null"));
 assertFalse(e.getMessage().contains("NPE"));
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testAnswerUnstubbedMethod() {
 Method method = unstubbedMethod();
 try {
 returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 } catch (Throwable e) {
 String expectedMessage = "SmartNull returned by unstubbed " + method.getName() + "() method on mock";
 verify(target).methodCall(any(String.class));
 assertNotNull(e.getMessage());
 assertFalse(e.getMessage().contains("NullPointerException"));
 assertFalse(e.getMessage().contains("null"));
 assertFalse(e.getMessage().contains("NPE"));
 assertTrue(e.getMessage().startsWith(expectedMessage));
 }
 }

 private Method nullMethod() {
 return new Method("methodCall", new Class[0]);
 }

 private Method serializableMethod() {
 return new Method("methodCall", new Class[0]) {
 @Override
 public Class<?> getReturnType() {
 return Serializable.class;
 }
 };
 }

 private Method finalClassMethod() {
 return new Method("finalMethod", new Class[0]) {
 @Override
 public Class<?> getReturnType() {
 return finalClass.class;
 }
 };
 }

 private Method toStringMethod() {
 return new Method("toString", new Class[0]) {
 @Override
 public Class<?> getReturnType() {
 return String.class;
 }
 };
 }

 private Method unstubbedMethod() {
 return new Method("unstubbedMethod", new Class[0]) {
 @Override
 public Class<?> getReturnType() {
 return Integer.class;
 }
 };
 }
}